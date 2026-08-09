package com.crm.mastersetup.service.impl;

import com.crm.mastersetup.dto.CountryDTO;
import com.crm.mastersetup.dto.StateDTO;
import com.crm.mastersetup.service.CountryService;
import com.crm.mastersetup.service.LocationFacade;
import com.crm.mastersetup.service.StateService;
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
    public List<CountryDTO> listCountries() {
        return countryService.listCountries();
    }

    @Override
    public List<StateDTO> listStatesByCountry(String countryCodeIso2) {
        CountryDTO country = countryService.findCountryByIso2(countryCodeIso2);

        return stateService.listStatesByCountry(country.id());
    }
}