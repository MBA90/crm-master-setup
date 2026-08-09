package com.crm.mastersetup.mapper;

import com.crm.mastersetup.domain.State;
import com.crm.mastersetup.dto.StateDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StateMapper {

    StateDTO toDTO(State state);

    State toEntity(StateDTO dto);
}