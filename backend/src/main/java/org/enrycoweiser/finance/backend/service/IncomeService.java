package org.enrycoweiser.finance.backend.service;

import lombok.RequiredArgsConstructor;
import org.enrycoweiser.finance.backend.domain.Income;
import org.enrycoweiser.finance.backend.domain.PaymentMethod;
import org.enrycoweiser.finance.backend.repository.IncomeRepository;
import org.enrycoweiser.finance.backend.standard.EntityService;
import org.enrycoweiser.finance.backend.utils.ErrorUtils;
import org.enrycoweiser.finance.backend.utils.filter.IncomeFilter;
import org.enrycoweiser.finance.shared.api.request.IncomeRequest;
import org.enrycoweiser.finance.shared.api.response.IncomeResponse;
import org.enrycoweiser.finance.shared.dto.IncomeDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IncomeService implements EntityService<IncomeDto,
                                                    IncomeRequest,
                                                    IncomeResponse> {

    private final IncomeRepository incomeRepository;

    @Override
    public IncomeResponse save(IncomeRequest request) {
        Income i;
        IncomeResponse incomeResponse = new IncomeResponse();
        Optional<Income> income;

        try {
            income = incomeRepository.retrieveById(request.getId());
        } catch (Exception ex) {
            incomeResponse.createErrorResponse("", ex.getMessage());
            return incomeResponse;
        }

        if(income.isPresent()) {
            i = income.get();
        } else {
            i = new Income();
            i.setDate(request.getDate());
            i.setMoney(request.getMoney());
            i.setCategory(request.getCategory());
            i.setPaymentMethod(new PaymentMethod(request.getPaymentMethod()));
            i.setNote(request.getNote());
            i.setUserId(request.getUserId());
        }

        try {
            incomeRepository.save(i);
        }  catch (Exception ex) {
            incomeResponse.createErrorResponse(ErrorUtils.API_001, ErrorUtils.API_001_CODE);
            return incomeResponse;
        }

        incomeResponse.createResponse();

        return incomeResponse;
    }

    @Override
    public IncomeResponse delete(IncomeRequest request) {
        IncomeResponse incomeResponse = new IncomeResponse();
        Optional<Income> income;

        try {
            income = incomeRepository.retrieveById(request.getId());
        } catch (Exception ex) {
            incomeResponse.createErrorResponse("", ex.getMessage());
            return incomeResponse;
        }

        incomeResponse.createResponse();

        return incomeResponse;
    }

    @Override
    public IncomeResponse retrieve(IncomeRequest request) {
        IncomeResponse incomeResponse = new IncomeResponse();
        Map<String, Object> filters = IncomeFilter.createFilterMap(request);

        try {
            List<Income> incomes = incomeRepository.retrieveByFilters(filters);

            List<IncomeDto> dtos = new ArrayList<>();
            for (Income i : incomes) {
                IncomeDto dto = i.toDto();
                dtos.add(dto);
            }

            incomeResponse = new IncomeResponse(dtos);
        } catch (Exception ex) {
            incomeResponse.createErrorResponse("", "");
            return incomeResponse;
        }

        incomeResponse.createResponse();

        return incomeResponse;
    }
}
