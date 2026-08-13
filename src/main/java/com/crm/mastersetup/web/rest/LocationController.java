package com.crm.mastersetup.web.rest;

import com.crm.lib.security.SecurityRoles;
import com.crm.mastersetup.dto.CountryDTO;
import com.crm.mastersetup.dto.StateDTO;
import com.crm.mastersetup.service.LocationFacade;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/locations")
public class LocationController {

    private final LocationFacade locationFacade;

    public LocationController(LocationFacade locationFacade) {
        this.locationFacade = locationFacade;
    }

    @GetMapping("/countries")
    @PreAuthorize("hasAnyRole('" + SecurityRoles.SALES_MANAGER + "', '" + SecurityRoles.SALES_REP + "')")
    public List<CountryDTO> listCountries() {
        return locationFacade.listCountries();
    }

    @GetMapping("/countries/{countryCodeIso2}/states")
    @PreAuthorize("hasAnyRole('" + SecurityRoles.SALES_MANAGER + "', '" + SecurityRoles.SALES_REP + "')")
    public List<StateDTO> listStates(@PathVariable String countryCodeIso2) {
        return locationFacade.listStatesByCountry(countryCodeIso2);
    }
}