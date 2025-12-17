package com.portalpolinema.backend.strukturOrganisasi.pengampuJabatan;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.portalpolinema.backend.dosen.DosenRepository;
import com.portalpolinema.backend.strukturOrganisasi.kategoriJabatan.KategoriJabatan;
import com.portalpolinema.backend.strukturOrganisasi.kategoriJabatan.KategoriJabatanRepository;
import com.portalpolinema.backend.strukturOrganisasi.periodeJabatan.PeriodeJabatan;
import com.portalpolinema.backend.strukturOrganisasi.periodeJabatan.PeriodeJabatanRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PengampuJabatanService {
    private final PengampuJabatanRepository pengampuJabatanRepository;
    private final DosenRepository dosenRepository;
    private final PeriodeJabatanRepository periodeJabatanRepository;
    private final KategoriJabatanRepository kategoriJabatanRepository;

    public List<PengampuJabatan> list() {
        return pengampuJabatanRepository.findAll();
    }

    public PengampuJabatan getById(Integer id_pengampu_jabatan) {
        return pengampuJabatanRepository.findById(id_pengampu_jabatan)
                .orElseThrow(() -> new RuntimeException("pengampu jabatan tidak ditemukan"));
    }

    public long count() {
        return pengampuJabatanRepository.count();
    }

    // CREATE
    public PengampuJabatan create(Integer dosen, Integer periode_jabatan, Integer kategori_jabatan) throws IOException {

        var dosen_obj = dosenRepository.findById(
                dosen)
                .orElseThrow(() -> new RuntimeException("dosen tidak ditemukan"));

        var periode_jabatan_obj = periodeJabatanRepository.findById(
                periode_jabatan)
                .orElseThrow(() -> new RuntimeException("periode jabatan tidak ditemukan"));

        var kategori_jabatan_obj = kategoriJabatanRepository.findById(
                kategori_jabatan)
                .orElseThrow(() -> new RuntimeException("kategori jabatan tidak ditemukan"));

        if (pengampuJabatanRepository.existsByKategoriJabatanAndPeriodeJabatan(kategori_jabatan_obj,
                periode_jabatan_obj)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Kategori di periode " + periode_jabatan_obj.getStart() + " - "
                            + periode_jabatan_obj.getEnd() + " sudah terpakai");
        }

        // simpan pengampu jabatan
        PengampuJabatan pj = new PengampuJabatan();

        pj.setDosen(dosen_obj);
        pj.setPeriodeJabatan(periode_jabatan_obj);
        pj.setKategoriJabatan(kategori_jabatan_obj);

        return pengampuJabatanRepository.save(pj);
    }

    // UPDATE
    public PengampuJabatan update(Integer id_pengampu_jabatan, Integer dosenBaru, Integer periode_jabatanBaru,
            Integer kategori_jabatanBaru) throws IOException {

        PengampuJabatan pj = getById(id_pengampu_jabatan);

        if (dosenBaru != null) {
            var dosen_obj = dosenRepository.findById(
                    dosenBaru)
                    .orElseThrow(() -> new RuntimeException("dosen tidak ditemukan"));
            pj.setDosen(dosen_obj);
        }

        if (periode_jabatanBaru != null) {
            var periode_jabatan_obj = periodeJabatanRepository.findById(
                    periode_jabatanBaru)
                    .orElseThrow(() -> new RuntimeException("periode jabatan tidak ditemukan"));
            pj.setPeriodeJabatan(periode_jabatan_obj);
        }

        if (kategori_jabatanBaru != null) {
            var kategori_jabatan_obj = kategoriJabatanRepository.findById(
                    kategori_jabatanBaru)
                    .orElseThrow(() -> new RuntimeException("kategori jabatan tidak ditemukan"));
            pj.setKategoriJabatan(kategori_jabatan_obj);
        }

        return pengampuJabatanRepository.save(pj);
    }

    public void delete(Integer id_pengampu_jabatan) throws IOException {
        PengampuJabatan pengampuJabatan = getById(id_pengampu_jabatan);
        pengampuJabatanRepository.delete(pengampuJabatan);
    }
}
