package com.portalpolinema.backend.prodi;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "prodi")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Prodi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "TINYINT UNSIGNED", nullable = false)
    private int id_prodi;

    @NotBlank
    @Column(nullable = false, length = 50, unique = true)
    private String nama;

    public enum Jenjang {
        D3,
        D4,
    }

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 9)
    private Jenjang jenjang;

    public enum Akreditasi {
        Unggul,
        @JsonProperty("Baik Sekali")
        Baik_Sekali,
        Baik
    }

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 12)
    private Akreditasi akreditasi;

    @NotBlank
    @Column(nullable = false)
    private String deskripsi;

    @NotBlank
    @Column(length = 255, nullable = false)
    private String karir;

    @Column(length = 50, nullable = false)
    private String foto;

}
