package org.enrycoweiser.finance.backend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.enrycoweiser.finance.backend.domain.PaymentMethod;
import org.enrycoweiser.finance.backend.repository.PaymentMethodRepository;
import org.enrycoweiser.finance.backend.standard.EntityService;
import org.enrycoweiser.finance.backend.utils.ErrorUtils;
import org.enrycoweiser.finance.shared.api.request.PaymentMethodRequest;
import org.enrycoweiser.finance.shared.api.response.PaymentMethodResponse;
import org.enrycoweiser.finance.shared.dto.PaymentMethodDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentMethodService implements EntityService<PaymentMethodDto,
                                                            PaymentMethodRequest,
                                                            PaymentMethodResponse> {

  private final PaymentMethodRepository paymentMethodRepository;

  @Transactional
  @Override
  public PaymentMethodResponse save(PaymentMethodRequest request) {
    PaymentMethod pm;
    PaymentMethodResponse pmResponse = new PaymentMethodResponse();
    Optional<PaymentMethod> paymentMethod;

    try {
      paymentMethod = paymentMethodRepository.retrieveById(request.getId());
    } catch (Exception ex) {
      pmResponse.createErrorResponse(ErrorUtils.ERR_001_CODE, ex.getMessage());
      return pmResponse;
    }

    if(paymentMethod.isPresent()) {
      pm = paymentMethod.get();
    } else {
      pm = new PaymentMethod();
      pm.setName(request.getName());
      pm.setActive(request.getActive());
      pm.setUserId(request.getUserId());
    }

    try {
      paymentMethodRepository.save(pm);
    } catch (Exception ex) {
      pmResponse.createErrorResponse(ErrorUtils.API_001, ErrorUtils.API_001_CODE);
      return pmResponse;
    }

    pmResponse.createResponse();

    return pmResponse;
  }

  @Transactional
  @Override
  public PaymentMethodResponse delete(PaymentMethodRequest request) {
    PaymentMethodResponse pmResponse = new PaymentMethodResponse();
    Optional<PaymentMethod> paymentMethod;

    try {
      paymentMethod = paymentMethodRepository.retrieveById(request.getId());
    } catch (Exception ex) {
      pmResponse.createErrorResponse(ErrorUtils.ERR_001_CODE, ex.getMessage());
      return pmResponse;
    }

    if(paymentMethod.isPresent()) {
      try {
        paymentMethodRepository.delete(paymentMethod.get());
      } catch (Exception ex) {
        pmResponse.createErrorResponse(ErrorUtils.API_002, ErrorUtils.API_002_CODE);
        return pmResponse;
      }
    } else {
      pmResponse.createErrorResponse(ErrorUtils.API_003, ErrorUtils.API_003_CODE);
      return pmResponse;
    }

    pmResponse.createResponse();

    return pmResponse;
  }

  @Transactional
  @Override
  public PaymentMethodResponse retrieve(PaymentMethodRequest request) {
    PaymentMethodResponse pmResponse = new PaymentMethodResponse();
    Map<String, Object> filters = null;

    try {
      List<PaymentMethod> pms = paymentMethodRepository.retrieveByFilters(filters);

      List<PaymentMethodDto> dtos = new ArrayList<>();
      for (PaymentMethod pm : pms) {
        PaymentMethodDto dto = pm.toDto();
        dtos.add(dto);
      }

      pmResponse = new PaymentMethodResponse(dtos);
    } catch (Exception ex) {
      pmResponse.createErrorResponse("", "");
      return pmResponse;
    }

    pmResponse.createResponse();

    return pmResponse;
  }
}
