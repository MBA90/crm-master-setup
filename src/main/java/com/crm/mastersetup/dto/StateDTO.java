package com.crm.mastersetup.dto;

import java.math.BigDecimal;

public record StateDTO(
        Integer id,
        String name,
        Integer countryId,
        String iso2,
        String iso3,
        BigDecimal latitude,
        BigDecimal longitude
) {
}