package com.nri.ems.Entities;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshToken {
    @Id
    @SequenceGenerator(sequenceName = "refresh_sequence", name = "refresh_sequence", initialValue = 1000, allocationSize = 1)
    @GeneratedValue(generator = "refresh_sequence", strategy = GenerationType.SEQUENCE)
    private Integer id;

    private String token;

    private Instant expiry;

    @OneToOne
    Employee employee;

}
