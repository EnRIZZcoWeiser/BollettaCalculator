package org.enrycoweiser.finance.backend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.enrycoweiser.finance.backend.domain.PaymentMethod;
import org.enrycoweiser.finance.backend.domain.Transfer;
import org.enrycoweiser.finance.backend.repository.TransferRepository;
import org.enrycoweiser.finance.backend.security.SEManager;
import org.enrycoweiser.finance.backend.standard.EntityService;
import org.enrycoweiser.finance.backend.utils.ErrorUtils;
import org.enrycoweiser.finance.backend.utils.filter.TransferFilter;
import org.enrycoweiser.finance.shared.api.request.TransferRequest;
import org.enrycoweiser.finance.shared.api.response.TransferResponse;
import org.enrycoweiser.finance.shared.dto.TransferDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransferService implements EntityService<TransferDto,
                                                    TransferRequest,
                                                    TransferResponse> {

    private final TransferRepository transferRepository;

    @Transactional
    @Override
    public TransferResponse save(TransferRequest request) {
        Transfer t;
        TransferResponse transferResponse = new TransferResponse();
        Optional<Transfer> transfer;

        try {
            transfer = transferRepository.retrieveById(request.getId());
        } catch (Exception ex) {
            transferResponse.createErrorResponse(ex.getMessage(), ErrorUtils.ERR_001_CODE);
            return transferResponse;
        }

        if(transfer.isPresent()) {
            t = transfer.get();
        } else {
            t = new Transfer();
            t.setUserId(SEManager.getId());
        }

        t.setDate(request.getDate());
        if(request.getFromPaymentMethod() == null || request.getToPaymentMethod() == null) {
            transferResponse.createErrorResponse(ErrorUtils.API_004, ErrorUtils.API_004_CODE);
            return transferResponse;
        }
        t.setFromPaymentMethod(new PaymentMethod(request.getFromPaymentMethod()));
        t.setToPaymentMethod(new PaymentMethod(request.getToPaymentMethod()));
        t.setMoney(request.getMoney());
        t.setNote(request.getNote());

        try {
            transferRepository.save(t);
        }  catch (Exception ex) {
            transferResponse.createErrorResponse(ErrorUtils.API_001, ErrorUtils.API_001_CODE);
            return transferResponse;
        }

        transferResponse.createResponse();

        return transferResponse;
    }

    @Transactional
    @Override
    public TransferResponse delete(TransferRequest request) {
        TransferResponse transferResponse = new TransferResponse();
        Optional<Transfer> transfer;

        try {
            transfer = transferRepository.retrieveById(request.getId());
        } catch (Exception ex) {
            transferResponse.createErrorResponse(ex.getMessage(), ErrorUtils.ERR_001_CODE);
            return transferResponse;
        }

        if(transfer.isPresent()) {
            try {
                transferRepository.delete(transfer.get());
            } catch (Exception ex) {
                transferResponse.createErrorResponse(ErrorUtils.API_002, ErrorUtils.API_002_CODE);
                return transferResponse;
            }
        } else {
            transferResponse.createErrorResponse(ErrorUtils.API_003, ErrorUtils.API_003_CODE);
            return transferResponse;
        }

        transferResponse.createResponse();

        return transferResponse;
    }

    @Transactional
    @Override
    public TransferResponse retrieve(TransferRequest request) {
        TransferResponse transferResponse = new TransferResponse();
        Map<String, Object> filters = TransferFilter.createFilterMap(request);

        try {
            List<Transfer> transfers = transferRepository.retrieveByFilters(filters);

            List<TransferDto> dtos = new ArrayList<>();
            for (Transfer t : transfers) {
                TransferDto dto = t.toDto();
                dtos.add(dto);
            }

            transferResponse = new TransferResponse(dtos);
        } catch (Exception ex) {
            transferResponse.createErrorResponse(ex.getMessage(), ErrorUtils.EXC_001);
            return transferResponse;
        }

        transferResponse.createResponse();

        return transferResponse;
    }
}
