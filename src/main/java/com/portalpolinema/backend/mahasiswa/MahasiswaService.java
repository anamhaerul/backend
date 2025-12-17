package com.portalpolinema.backend.mahasiswa;

import java.io.IOException;
import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.portalpolinema.backend.prodi.ProdiRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MahasiswaService {
    private final mahasiswaRepository mahasiswaRepository;
    private final ProdiRepository prodiRepository;
    private final MahasiswaFileService mahasiswaFileService;

    // get all
    public List<Mahasiswa> list(String search) {
        if (search != null && !search.isBlank()) {
            try {
                // ubah jadi huruf pertama besar, sisanya kecil
                String key = search.trim();
                key = key.substring(0, 1).toUpperCase() + key.substring(1).toLowerCase();
                Mahasiswa.Status st = Mahasiswa.Status.valueOf(key); // enum Aktif/Alumni
                return mahasiswaRepository.findByStatus(st);
            } catch (IllegalArgumentException ex) {
                // fallback ke cari nama, atau kembalikan kosong
                return mahasiswaRepository.findByNamaContainingIgnoreCase(search);
            }
        }
        return mahasiswaRepository.findAll();
    }

    // get by id
    public Mahasiswa getById(Integer id_mahasiswa) {
        return mahasiswaRepository.findById(
                id_mahasiswa)
                .orElseThrow(() -> new RuntimeException("Dosen tidak ditemukan"));
    }

    // count
    public long countAktif() {
        return mahasiswaRepository.countByStatus(Mahasiswa.Status.Aktif);
    }

    public long countAlumni() {
        return mahasiswaRepository.countByStatus(Mahasiswa.Status.Alumni);
    }

    // CREATE
    public Mahasiswa create(String nim, String nama, String jenisKelamin, Integer prodi, LocalDate tglMasuk,
            String status, MultipartFile foto) throws IOException {

        List<String> errors = new ArrayList<>();
        if (mahasiswaRepository.existsByNim(nim))
            errors.add("NIM sudah terpakai");
        if (!errors.isEmpty())
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.join(" | ", errors));

        Mahasiswa mhs = new Mahasiswa();
        mhs.setNim(nim);
        mhs.setNama(nama);
        mhs.setJenisKelamin(Mahasiswa.JenisKelamin.valueOf(jenisKelamin));
        var prodi_obj = prodiRepository.findById(
                prodi)
                .orElseThrow(() -> new RuntimeException("Prodi tidak ditemukan"));
        mhs.setProdi(prodi_obj);

        mhs.setTglMasuk(java.sql.Date.valueOf(tglMasuk));

        mhs.setStatus(Mahasiswa.Status.valueOf(status));

        String filename = mahasiswaFileService.storeFotoMahasiswa(nim, foto);
        if (filename == null || filename.isBlank())
            filename = "default-mahasiswa.png";
        mhs.setFoto(filename);

        return mahasiswaRepository.save(mhs);
    }

    // UPDATE
    public Mahasiswa update(Integer id_mahasiswa, String nim, String nama, String jenisKelamin, Integer prodi,
            LocalDate tglMasuk, LocalDate tglLulus, String status, MultipartFile foto) throws IOException {

        Mahasiswa mhs = getById(id_mahasiswa);

        // update mahasiswa
        String oldFoto = mhs.getFoto();
        String oldNim = mhs.getNim();

        mhs.setNama(nama);

        mhs.setJenisKelamin(Mahasiswa.JenisKelamin.valueOf(jenisKelamin));
        if (prodi != null) {
            var prodiId = prodiRepository.findById(prodi)
                    .orElseThrow(() -> new RuntimeException("Prodi tidak ditemukan"));
            mhs.setProdi(prodiId);
        }

        mhs.setTglMasuk(java.sql.Date.valueOf(tglMasuk));
        if (tglLulus != null) {
            mhs.setTglLulus(java.sql.Date.valueOf(tglLulus));
        } else {
            mhs.setTglLulus(null);
        }
        mhs.setStatus(Mahasiswa.Status.valueOf(status));

        if (foto != null && !foto.isEmpty()) {
            if (oldFoto != null && !oldFoto.isBlank() && !"default-mahasiswa.png".equals(oldFoto)) {
                try {
                    mahasiswaFileService.deleteFotoMahasiswa(oldFoto);
                } catch (IOException ignore) {
                }
            }
            mhs.setFoto(mahasiswaFileService.storeFotoMahasiswa(nim, foto));
        } else if (!nim.equals(oldNim)
                && oldFoto != null && !oldFoto.isBlank()
                && !"default-dosen.png".equals(oldFoto)) {
            mhs.setFoto(mahasiswaFileService.renameExistingFoto(oldFoto, nim));
        }

        return mahasiswaRepository.save(mhs);
    }

    public void delete(Integer id_mahasiswa) throws IOException {
        Mahasiswa mhs = getById(id_mahasiswa);
        mahasiswaRepository.delete(mhs);

        if (mhs.getFoto() != null && !mhs.getFoto().isBlank() && !"default-mahasiswa.png".equals(mhs.getFoto())) {
            mahasiswaFileService.deleteFotoMahasiswa(mhs.getFoto());
        }
    }
}
