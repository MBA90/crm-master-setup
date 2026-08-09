package com.crm.mastersetup.repository;

import com.crm.mastersetup.domain.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CountryRepository extends JpaRepository<Country, Integer> {

    List<Country> findAllByOrderByNameAsc();

    Country findByIso2(String iso2);
}
