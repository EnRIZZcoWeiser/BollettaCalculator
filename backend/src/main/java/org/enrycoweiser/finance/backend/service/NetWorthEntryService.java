package org.enrycoweiser.finance.backend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.enrycoweiser.finance.backend.domain.Asset;
import org.enrycoweiser.finance.backend.domain.NetWorth;
import org.enrycoweiser.finance.backend.domain.NetWorthEntry;
import org.enrycoweiser.finance.backend.repository.NetWorthEntryRepository;
import org.enrycoweiser.finance.backend.repository.NetWorthRepository;
import org.enrycoweiser.finance.backend.security.SEManager;
import org.enrycoweiser.finance.backend.standard.EntityService;
import org.enrycoweiser.finance.backend.utils.ErrorUtils;
import org.enrycoweiser.finance.backend.utils.filter.NetWorthEntryFilter;
import org.enrycoweiser.finance.shared.api.request.NetWorthEntryRequest;
import org.enrycoweiser.finance.shared.api.response.NetWorthEntryResponse;
import org.enrycoweiser.finance.shared.dto.NetWorthEntryDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NetWorthEntryService implements EntityService<NetWorthEntryDto,
                                                            NetWorthEntryRequest,
                                                            NetWorthEntryResponse> {

    private final NetWorthEntryRepository netWorthEntryRepository;
    private final NetWorthRepository netWorthRepository;

    @Transactional
    @Override
    public NetWorthEntryResponse save(NetWorthEntryRequest request) {
        NetWorthEntry e;
        NetWorthEntryResponse entryResponse = new NetWorthEntryResponse();
        Optional<NetWorthEntry> entry;

        try {
            entry = netWorthEntryRepository.retrieveById(request.getId());
        } catch (Exception ex) {
            entryResponse.createErrorResponse(ex.getMessage(), ErrorUtils.ERR_001_CODE);
            return entryResponse;
        }

        if(entry.isPresent()) {
            e = entry.get();
        } else {
            Optional<NetWorth> netWorth = netWorthRepository.retrieveById(request.getNetWorthId());
            if(netWorth.isEmpty()) {
                entryResponse.createErrorResponse(ErrorUtils.API_006, ErrorUtils.API_006_CODE);
                return entryResponse;
            }

            e = new NetWorthEntry();
            e.setNetWorth(netWorth.get());
            e.setUserId(SEManager.getId());
        }

        if(request.getAsset() == null) {
            entryResponse.createErrorResponse(ErrorUtils.API_005, ErrorUtils.API_005_CODE);
            return entryResponse;
        }
        e.setAsset(new Asset(request.getAsset()));
        e.setValue(request.getValue());

        try {
            netWorthEntryRepository.save(e);
        } catch (Exception ex) {
            entryResponse.createErrorResponse(ErrorUtils.API_001, ErrorUtils.API_001_CODE);
            return entryResponse;
        }

        entryResponse.createResponse();

        return entryResponse;
    }

    @Transactional
    @Override
    public NetWorthEntryResponse delete(NetWorthEntryRequest request) {
        NetWorthEntryResponse entryResponse = new NetWorthEntryResponse();
        Optional<NetWorthEntry> entry;

        try {
            entry = netWorthEntryRepository.retrieveById(request.getId());
        } catch (Exception ex) {
            entryResponse.createErrorResponse(ex.getMessage(), ErrorUtils.ERR_001_CODE);
            return entryResponse;
        }

        if(entry.isPresent()) {
            try {
                netWorthEntryRepository.delete(entry.get());
            } catch (Exception ex) {
                entryResponse.createErrorResponse(ErrorUtils.API_002, ErrorUtils.API_002_CODE);
                return entryResponse;
            }
        } else {
            entryResponse.createErrorResponse(ErrorUtils.API_003, ErrorUtils.API_003_CODE);
            return entryResponse;
        }

        entryResponse.createResponse();

        return entryResponse;
    }

    @Transactional
    @Override
    public NetWorthEntryResponse retrieve(NetWorthEntryRequest request) {
        NetWorthEntryResponse entryResponse = new NetWorthEntryResponse();
        Map<String, Object> filters = NetWorthEntryFilter.createFilterMap(request);

        try {
            List<NetWorthEntry> entries = netWorthEntryRepository.retrieveByFilters(filters);

            List<NetWorthEntryDto> dtos = new ArrayList<>();
            for (NetWorthEntry e : entries) {
                NetWorthEntryDto dto = e.toDto();
                dtos.add(dto);
            }

            entryResponse = new NetWorthEntryResponse(dtos);
        } catch (Exception ex) {
            entryResponse.createErrorResponse(ex.getMessage(), ErrorUtils.EXC_001);
            return entryResponse;
        }

        entryResponse.createResponse();

        return entryResponse;
    }
}
