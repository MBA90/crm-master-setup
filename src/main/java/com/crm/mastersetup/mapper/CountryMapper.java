package com.crm.mastersetup.mapper;

import com.crm.mastersetup.domain.Country;
import com.crm.mastersetup.dto.CountryDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CountryMapper {

    CountryDTO toDTO(Country country);

    Country toEntity(CountryDTO dto);
}
