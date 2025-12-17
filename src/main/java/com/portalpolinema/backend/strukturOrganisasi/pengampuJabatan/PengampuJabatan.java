package com.portalpolinema.backend.strukturOrganisasi.pengampuJabatan;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "pengampu_jabatan", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "id_kategori_jabatan", "id_periode_jabatan" })
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class PengampuJabatan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "TINYINT UNSIGNED", nullable = false)
    private int id_pengampu_jabatan;

    @ManyToOne
    @JoinColumn(name = "id_kategori_jabatan", nullable = false)
    private com.portalpolinema.backend.strukturOrganisasi.kategoriJabatan.KategoriJabatan kategoriJabatan;

    @ManyToOne
    @JoinColumn(name = "id_periode_jabatan", nullable = false)
    private com.portalpolinema.backend.strukturOrganisasi.periodeJabatan.PeriodeJabatan periodeJabatan;

    @ManyToOne
    @JoinColumn(name = "id_dosen", nullable = false)
    private com.portalpolinema.backend.dosen.Dosen dosen;

}
