package com.portalpolinema.backend.strukturOrganisasi.periodeJabatan;

import java.time.Year;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "periode_jabatan")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class PeriodeJabatan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "TINYINT UNSIGNED", nullable = false)
    private int id_periode_jabatan;

    @NotNull
    @Column(columnDefinition = "YEAR", nullable = false)
    private Year start;

    @NotNull
    @Column(columnDefinition = "YEAR", nullable = false)
    private Year end;

    @NotBlank
    @Column(length = 50, nullable = false)
    private String foto;
}
