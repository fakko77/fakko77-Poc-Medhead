package com.medhead.appGestion.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@Entity
public class HospitalSpecialisation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "HOSPITAL_ID")
    private Hospital hospital;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "SPECIALISATION_ID")
    private Specialisation specialisation;

    private Long availableLocation;



}
