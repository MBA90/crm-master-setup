package com.crm.mastersetup.web.rest;

import com.crm.mastersetup.dto.CountryDTO;
import com.crm.mastersetup.service.CountryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/countries")
public class CountryController {

    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping
    public List<CountryDTO> listCountries() {
        return countryService.listCountries();
    }
}
