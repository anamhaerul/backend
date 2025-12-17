package com.portalpolinema.backend.dosen;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "dosen")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dosen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "SMALLINT UNSIGNED")
    private int id_dosen;

    @NotBlank
    @Column(nullable = false, length = 18, unique = true)
    private String nip;

    @NotBlank
    @Column(nullable = false, length = 150)
    private String nama;

    public enum JenisKelamin {
        @JsonProperty("Laki - laki")
        LAKI_LAKI,
        @JsonProperty("Perempuan")
        PEREMPUAN
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "jenis_kelamin", nullable = false, length = 9)
    private JenisKelamin jenisKelamin;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @NotBlank
    @Column(nullable = false, length = 25)
    private String foto;

    @ManyToOne
    @JoinColumn(name = "id_prodi", nullable = false)
    private com.portalpolinema.backend.prodi.Prodi prodi;

}
