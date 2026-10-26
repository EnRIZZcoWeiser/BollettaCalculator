package org.enrycoweiser.finance.backend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.enrycoweiser.finance.backend.domain.Asset;
import org.enrycoweiser.finance.backend.domain.Expense;
import org.enrycoweiser.finance.backend.domain.NetWorth;
import org.enrycoweiser.finance.backend.domain.NetWorthEntry;
import org.enrycoweiser.finance.backend.repository.AssetRepository;
import org.enrycoweiser.finance.backend.repository.ExpenseRepository;
import org.enrycoweiser.finance.backend.repository.NetWorthEntryRepository;
import org.enrycoweiser.finance.backend.repository.NetWorthRepository;
import org.enrycoweiser.finance.backend.security.SEManager;
import org.enrycoweiser.finance.backend.standard.EntityService;
import org.enrycoweiser.finance.backend.utils.ErrorUtils;
import org.enrycoweiser.finance.backend.utils.filter.NetWorthFilter;
import org.enrycoweiser.finance.shared.api.request.NetWorthRequest;
import org.enrycoweiser.finance.shared.api.response.NetWorthResponse;
import org.enrycoweiser.finance.shared.dto.NetWorthDto;
import org.enrycoweiser.finance.shared.utils.FilterUtils;
import org.enrycoweiser.finance.shared.utils.StaticUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class NetWorthService implements EntityService<NetWorthDto,
                                                        NetWorthRequest,
                                                        NetWorthResponse> {

    private final NetWorthRepository netWorthRepository;
    private final NetWorthEntryRepository netWorthEntryRepository;
    private final AssetRepository assetRepository;
    private final ExpenseRepository expenseRepository;

    @Transactional
    @Override
    public NetWorthResponse save(NetWorthRequest request) {
        NetWorth n;
        NetWorthResponse netWorthResponse = new NetWorthResponse();
        Optional<NetWorth> netWorth;

        try {
            netWorth = netWorthRepository.retrieveById(request.getId());
        } catch (Exception ex) {
            netWorthResponse.createErrorResponse(ex.getMessage(), ErrorUtils.ERR_001_CODE);
            return netWorthResponse;
        }

        boolean newItem = netWorth.isEmpty();

        if(netWorth.isPresent()) {
            n = netWorth.get();
        } else {
            n = new NetWorth();
            n.setUserId(SEManager.getId());
        }

        n.setDate(request.getDate());

        try {
            netWorthRepository.save(n);

            if(newItem) {
                createEntries(n);
            }
        } catch (Exception ex) {
            netWorthResponse.createErrorResponse(ErrorUtils.API_001, ErrorUtils.API_001_CODE);
            return netWorthResponse;
        }

        netWorthResponse.createResponse();

        return netWorthResponse;
    }

    @Transactional
    @Override
    public NetWorthResponse delete(NetWorthRequest request) {
        NetWorthResponse netWorthResponse = new NetWorthResponse();
        Optional<NetWorth> netWorth;

        try {
            netWorth = netWorthRepository.retrieveById(request.getId());
        } catch (Exception ex) {
            netWorthResponse.createErrorResponse(ex.getMessage(), ErrorUtils.ERR_001_CODE);
            return netWorthResponse;
        }

        if(netWorth.isPresent()) {
            try {
                netWorthEntryRepository.deleteAll(retrieveEntries(netWorth.get().getId()));
                netWorthRepository.delete(netWorth.get());
            } catch (Exception ex) {
                netWorthResponse.createErrorResponse(ErrorUtils.API_002, ErrorUtils.API_002_CODE);
                return netWorthResponse;
            }
        } else {
            netWorthResponse.createErrorResponse(ErrorUtils.API_003, ErrorUtils.API_003_CODE);
            return netWorthResponse;
        }

        netWorthResponse.createResponse();

        return netWorthResponse;
    }

    @Transactional
    @Override
    public NetWorthResponse retrieve(NetWorthRequest request) {
        NetWorthResponse netWorthResponse = new NetWorthResponse();
        Map<String, Object> filters = NetWorthFilter.createFilterMap(request);

        try {
            List<NetWorth> netWorths = netWorthRepository.retrieveByFilters(filters);

            List<NetWorthDto> dtos = new ArrayList<>();
            for (NetWorth n : netWorths) {
                NetWorthDto dto = n.toDto();
                calculateTotals(dto);
                dtos.add(dto);
            }

            netWorthResponse = new NetWorthResponse(dtos);
        } catch (Exception ex) {
            netWorthResponse.createErrorResponse(ex.getMessage(), ErrorUtils.EXC_001);
            return netWorthResponse;
        }

        netWorthResponse.createResponse();

        return netWorthResponse;
    }

    /*
        A new net worth starts with one empty row for every active asset:
        the values are filled in afterwards from the detail view.
     */
    protected void createEntries(NetWorth n) {
        Map<String, Object> filters = new HashMap<>();
        filters.put(FilterUtils.ASSET_ONLY_ACTIVE, true);

        List<Asset> assets = assetRepository.retrieveByFilters(filters);
        for (Asset a : assets) {
            NetWorthEntry e = new NetWorthEntry();
            e.setNetWorth(n);
            e.setAsset(a);
            e.setValue(null);
            e.setUserId(SEManager.getId());

            netWorthEntryRepository.save(e);
        }
    }

    protected List<NetWorthEntry> retrieveEntries(Long netWorthId) {
        Map<String, Object> filters = new HashMap<>();
        filters.put(FilterUtils.NET_WORTH_ENTRY_NET_WORTH, netWorthId);

        return netWorthEntryRepository.retrieveByFilters(filters);
    }

    /*
        total       = sum of all the rows
        investments = sum of all the rows except liquidity
        spending    = sum of the expenses in the month of the net worth date
     */
    protected void calculateTotals(NetWorthDto dto) {
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal investments = BigDecimal.ZERO;

        for (NetWorthEntry e : retrieveEntries(dto.getId())) {
            if(e.getValue() == null) {
                continue;
            }

            total = total.add(e.getValue());

            if(e.getAsset() != null && !StaticUtils.NET_WORTH_LIQUIDITY.equals(e.getAsset().getCategory())) {
                investments = investments.add(e.getValue());
            }
        }

        BigDecimal spending = BigDecimal.ZERO;

        if(dto.getDate() != null) {
            LocalDate date = dto.getDate();

            Map<String, Object> filters = new HashMap<>();
            filters.put(FilterUtils.EXPENSE_DATE_FROM, date.withDayOfMonth(1));
            filters.put(FilterUtils.EXPENSE_DATE_TO, date.withDayOfMonth(date.lengthOfMonth()));

            for (Expense e : expenseRepository.retrieveByFilters(filters)) {
                if(e.getMoney() != null) {
                    spending = spending.add(e.getMoney());
                }
            }
        }

        dto.setTotal(total);
        dto.setInvestments(investments);
        dto.setSpending(spending);
    }
}
