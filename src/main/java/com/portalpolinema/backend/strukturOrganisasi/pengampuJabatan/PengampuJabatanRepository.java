package com.portalpolinema.backend.strukturOrganisasi.pengampuJabatan;

import org.springframework.data.jpa.repository.JpaRepository;

import com.portalpolinema.backend.strukturOrganisasi.kategoriJabatan.KategoriJabatan;
import com.portalpolinema.backend.strukturOrganisasi.periodeJabatan.PeriodeJabatan;

public interface PengampuJabatanRepository extends JpaRepository<PengampuJabatan, Integer> {
    boolean existsByKategoriJabatanAndPeriodeJabatan(KategoriJabatan kategoriJabatan, PeriodeJabatan periodeJabatan);

}