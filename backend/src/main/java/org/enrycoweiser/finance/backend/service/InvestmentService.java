package org.enrycoweiser.finance.backend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.enrycoweiser.finance.backend.domain.Asset;
import org.enrycoweiser.finance.backend.domain.Investment;
import org.enrycoweiser.finance.backend.domain.InvestmentHistory;
import org.enrycoweiser.finance.backend.repository.AssetRepository;
import org.enrycoweiser.finance.backend.repository.InvestmentHistoryRepository;
import org.enrycoweiser.finance.backend.repository.InvestmentRepository;
import org.enrycoweiser.finance.backend.security.SEManager;
import org.enrycoweiser.finance.backend.standard.EntityService;
import org.enrycoweiser.finance.backend.utils.ErrorUtils;
import org.enrycoweiser.finance.backend.utils.filter.InvestmentFilter;
import org.enrycoweiser.finance.shared.api.request.InvestmentRequest;
import org.enrycoweiser.finance.shared.api.response.InvestmentResponse;
import org.enrycoweiser.finance.shared.dto.AssetDto;
import org.enrycoweiser.finance.shared.dto.InvestmentDto;
import org.enrycoweiser.finance.shared.utils.FilterUtils;
import org.enrycoweiser.finance.shared.utils.StaticUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
@RequiredArgsConstructor
public class InvestmentService implements EntityService<InvestmentDto,
        InvestmentRequest,
        InvestmentResponse> {

    private final InvestmentRepository investmentRepository;
    private final InvestmentHistoryRepository investmentHistoryRepository;
    private final AssetRepository assetRepository;

    @Transactional
    @Override
    public InvestmentResponse save(InvestmentRequest request) {
        Investment s;
        InvestmentResponse investmentResponse = new InvestmentResponse();
        Optional<Investment> investment;

        try {
            investment = investmentRepository.retrieveById(request.getId());
        } catch (Exception ex) {
            investmentResponse.createErrorResponse(ex.getMessage(), ErrorUtils.ERR_001_CODE);
            return investmentResponse;
        }

        if (investment.isPresent()) {
            s = investment.get();
        } else {
            s = new Investment();
            s.setUserId(SEManager.getId());
        }

        if(request.getAsset() == null) {
            investmentResponse.createErrorResponse(ErrorUtils.API_005, ErrorUtils.API_005_CODE);
            return investmentResponse;
        }

        Optional<Asset> asset = assetRepository.retrieveById(request.getAsset().getId());
        if(asset.isPresent() && StaticUtils.NET_WORTH_LIQUIDITY.equals(asset.get().getCategory())) {
            investmentResponse.createErrorResponse(ErrorUtils.API_011, ErrorUtils.API_011_CODE);
            return investmentResponse;
        }

        if(isAssetUsed(request.getAsset(), s.getId())) {
            investmentResponse.createErrorResponse(ErrorUtils.API_012, ErrorUtils.API_012_CODE);
            return investmentResponse;
        }

        s.setAsset(new Asset(request.getAsset()));
        s.setActive(request.getActive());

        try {
            investmentRepository.save(s);
        } catch (Exception ex) {
            investmentResponse.createErrorResponse(ErrorUtils.API_001, ErrorUtils.API_001_CODE);
            return investmentResponse;
        }

        investmentResponse.createResponse();

        return investmentResponse;
    }

    @Transactional
    @Override
    public InvestmentResponse delete(InvestmentRequest request) {
        InvestmentResponse investmentResponse = new InvestmentResponse();
        Optional<Investment> investment;

        try {
            investment = investmentRepository.retrieveById(request.getId());
        } catch (Exception ex) {
            investmentResponse.createErrorResponse(ex.getMessage(), ErrorUtils.ERR_001_CODE);
            return investmentResponse;
        }

        if (investment.isPresent()) {
            try {
                investmentHistoryRepository.deleteAll(retrieveHistories(investment.get().getId()));
                investmentRepository.delete(investment.get());
            } catch (Exception ex) {
                investmentResponse.createErrorResponse(ErrorUtils.API_002, ErrorUtils.API_002_CODE);
                return investmentResponse;
            }
        } else {
            investmentResponse.createErrorResponse(ErrorUtils.API_003, ErrorUtils.API_003_CODE);
            return investmentResponse;
        }

        investmentResponse.createResponse();

        return investmentResponse;
    }

    @Transactional
    @Override
    public InvestmentResponse retrieve(InvestmentRequest request) {
        InvestmentResponse investmentResponse = new InvestmentResponse();
        Map<String, Object> filters = InvestmentFilter.createFilterMap(request);

        try {
            List<Investment> investments = investmentRepository.retrieveByFilters(filters);

            List<InvestmentDto> dtos = new ArrayList<>();
            for (Investment s : investments) {
                InvestmentDto dto = s.toDto();
                calculatePosition(dto);
                dtos.add(dto);
            }

            investmentResponse = new InvestmentResponse(dtos);
        } catch (Exception ex) {
            investmentResponse.createErrorResponse(ex.getMessage(), ErrorUtils.EXC_001);
            return investmentResponse;
        }

        investmentResponse.createResponse();

        return investmentResponse;
    }

    /*
        An asset can belong to one investment only: true if another investment (not this one) already uses it.
     */
    protected boolean isAssetUsed(AssetDto asset, Long investmentId) {
        Map<String, Object> filters = new HashMap<>();
        filters.put(FilterUtils.INVESTMENT_ASSET, asset);

        for (Investment i : investmentRepository.retrieveByFilters(filters)) {
            if(!Objects.equals(i.getId(), investmentId)) {
                return true;
            }
        }

        return false;
    }

    protected List<InvestmentHistory> retrieveHistories(Long investmentId) {
        Map<String, Object> filters = new HashMap<>();
        filters.put(FilterUtils.INVESTMENT_HISTORY_INVESTMENT, investmentId);

        return investmentHistoryRepository.retrieveByFilters(filters);
    }

    /*
        shares       = shares bought - shares sold
        averagePrice = amount spent on the buys / shares bought (fees excluded)
        invested     = shares * averagePrice, the cost of the shares still held
        fees         = sum of all the fees
     */
    protected void calculatePosition(InvestmentDto dto) {
        BigDecimal boughtAmount = BigDecimal.ZERO;
        BigDecimal boughtShares = BigDecimal.ZERO;
        BigDecimal soldShares = BigDecimal.ZERO;
        BigDecimal fees = BigDecimal.ZERO;

        for (InvestmentHistory h : retrieveHistories(dto.getId())) {
            if(h.getFee() != null) {
                fees = fees.add(h.getFee());
            }

            if(h.getShares() == null) {
                continue;
            }

            if(h.isBuy()) {
                boughtShares = boughtShares.add(h.getShares());
                if(h.getAmount() != null) {
                    boughtAmount = boughtAmount.add(h.getAmount());
                }
            } else if(h.isSell()) {
                soldShares = soldShares.add(h.getShares());
            }
        }

        BigDecimal shares = boughtShares.subtract(soldShares);

        BigDecimal averagePrice = BigDecimal.ZERO;
        if(boughtShares.signum() != 0) {
            averagePrice = boughtAmount.divide(boughtShares, 4, RoundingMode.HALF_UP);
        }

        dto.setShares(shares);
        dto.setAveragePrice(averagePrice);
        dto.setInvested(shares.multiply(averagePrice).setScale(2, RoundingMode.HALF_UP));
        dto.setFees(fees);
    }
}
