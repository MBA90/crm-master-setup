package com.crm.mastersetup.dto;

public record CountryDTO(
        Integer id,
        String name,
        String iso2,
        String iso3,
        String phoneCode
) {
}
