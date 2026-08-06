package com.crm.mastersetup.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "countries")
@Getter
@Setter
@NoArgsConstructor
public class Country {

    @Id
    private Integer id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "iso2", nullable = false, unique = true, length = 2)
    private String iso2;

    @Column(name = "iso3", unique = true, length = 3)
    private String iso3;

    @Column(name = "phone_code", length = 10)
    private String phoneCode;
}
