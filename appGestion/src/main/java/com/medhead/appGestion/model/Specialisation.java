package com.medhead.appGestion.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
public class Specialisation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "specialisation")
    public Set<HospitalSpecialisation> HospitalSpecialisations;

}
