package com.portalpolinema.backend.strukturOrganisasi.kategoriJabatan;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "kategori_jabatan")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class KategoriJabatan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "TINYINT UNSIGNED", nullable = false)
    private int id_kategori_jabatan;

    @NotBlank
    @Column(nullable = false, length = 100, unique = true)
    private String nama;
}
