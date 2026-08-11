package com.crm.mastersetup.service.impl;

import com.crm.mastersetup.config.CacheNames;
import com.crm.mastersetup.dto.CountryDTO;
import com.crm.mastersetup.dto.StateDTO;
import com.crm.mastersetup.service.CountryService;
import com.crm.mastersetup.service.LocationFacade;
import com.crm.mastersetup.service.StateService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationFacadeImpl implements LocationFacade {

    private final CountryService countryService;
    private final StateService stateService;

    public LocationFacadeImpl(CountryService countryService, StateService stateService) {
        this.countryService = countryService;
        this.stateService = stateService;
    }

    @Override
    @Cacheable(cacheNames = CacheNames.COUNTRIES, key = "'all'")
    public List<CountryDTO> listCountries() {
        return countryService.listCountries();
    }

    @Override
    @Cacheable(cacheNames = CacheNames.STATES_BY_COUNTRY, key = "#countryCodeIso2")
    public List<StateDTO> listStatesByCountry(String countryCodeIso2) {
        CountryDTO country = countryService.findCountryByIso2(countryCodeIso2);

        return stateService.listStatesByCountry(country.id());
    }
}