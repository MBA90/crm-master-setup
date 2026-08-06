package com.crm.mastersetup.service.impl;

import com.crm.mastersetup.dto.CountryDTO;
import com.crm.mastersetup.mapper.CountryMapper;
import com.crm.mastersetup.repository.CountryRepository;
import com.crm.mastersetup.service.CountryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;
    private final CountryMapper countryMapper;

    public CountryServiceImpl(CountryRepository countryRepository, CountryMapper countryMapper) {
        this.countryRepository = countryRepository;
        this.countryMapper = countryMapper;
    }

    @Override
    public List<CountryDTO> listCountries() {
        return countryRepository.findAllByOrderByNameAsc().stream()
                .map(countryMapper::toDTO)
                .toList();
    }
}
