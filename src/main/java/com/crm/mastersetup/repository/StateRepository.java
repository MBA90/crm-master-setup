package com.crm.mastersetup.repository;

import com.crm.mastersetup.domain.State;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StateRepository extends JpaRepository<State, Integer> {

    List<State> findAllByCountryIdOrderByNameAsc(Integer countryId);
}