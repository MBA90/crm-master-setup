package com.crm.mastersetup.service;

import com.crm.mastersetup.dto.StateDTO;

import java.util.List;

public interface StateService {

    List<StateDTO> listStatesByCountry(Integer countryId);
}