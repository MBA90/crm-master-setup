package com.crm.mastersetup.service;

import com.crm.mastersetup.dto.CountryDTO;
import com.crm.mastersetup.dto.StateDTO;

import java.util.List;

public interface LocationFacade {

    List<CountryDTO> listCountries();

    List<StateDTO> listStatesByCountry(String countryCodeIso2);
}