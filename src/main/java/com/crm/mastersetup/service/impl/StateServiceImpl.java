package com.crm.mastersetup.service.impl;

import com.crm.mastersetup.dto.StateDTO;
import com.crm.mastersetup.mapper.StateMapper;
import com.crm.mastersetup.repository.StateRepository;
import com.crm.mastersetup.service.StateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class StateServiceImpl implements StateService {

    private final StateRepository stateRepository;
    private final StateMapper stateMapper;

    public StateServiceImpl(StateRepository stateRepository, StateMapper stateMapper) {
        this.stateRepository = stateRepository;
        this.stateMapper = stateMapper;
    }

    @Override
    public List<StateDTO> listStatesByCountry(Integer countryId) {
        return stateRepository.findAllByCountryIdOrderByNameAsc(countryId).stream()
                .map(stateMapper::toDTO)
                .toList();
    }
}