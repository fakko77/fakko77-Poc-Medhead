package com.medhead.appGestion.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;
@Data
@Entity
public class Hospital {
    @Id
    @GeneratedValue
    private Integer id;

    private String name;

    private String coordinates;

    @JsonIgnore
    @OneToMany(mappedBy = "hospital")
    private Set<HospitalSpecialisation> hospitalSpecialisations;

}
