package com.portalpolinema.backend.mahasiswa;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.EnumType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "mahasiswa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mahasiswa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "SMALLINT UNSIGNED", nullable = false)
    private int id_mahasiswa;

    @NotBlank
    @Column(nullable = false, length = 120)
    private String nama;

    @NotBlank
    @Column(nullable = false, length = 12, unique = true)
    private String nim;

    public enum JenisKelamin {
        @JsonProperty("Laki - laki")
        LAKI_LAKI,
        @JsonProperty("Perempuan")
        PEREMPUAN
    }

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JenisKelamin jenisKelamin;

    @ManyToOne
    @JoinColumn(nullable = false, name = "id_prodi")
    private com.portalpolinema.backend.prodi.Prodi prodi;

    @NotNull
    @Column(nullable = false)
    private Date tglMasuk;

    @Column()
    private Date tglLulus;

    public enum Status {
        Aktif,
        Alumni
    }

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @NotBlank
    @Column(nullable = false, length = 20)
    private String foto;

}
