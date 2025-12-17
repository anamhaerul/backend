package com.portalpolinema.backend.mahasiswa;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.portalpolinema.backend.dosen.Dosen;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Date;

@RestController
@RequestMapping("/api/mahasiswa")
@RequiredArgsConstructor
public class MahasiswaController {
    private final MahasiswaService mahasiswaService;

    // get all
    @GetMapping
    public List<Mahasiswa> list(@RequestParam(name = "search", required = false) String search) {
        return mahasiswaService.list(search);
    }

    // get by id
    @GetMapping("/{id_mahasiswa}")
    public Mahasiswa get(@PathVariable("id_mahasiswa") Integer id_mahasiswa) {
        return mahasiswaService.getById(id_mahasiswa);
    }

    // count
    @GetMapping("/count/aktif")
    public long countAktif() {
        return mahasiswaService.countAktif();
    }

    @GetMapping("/count/alumni")
    public long countAlumni() {
        return mahasiswaService.countAlumni();
    }

    // create
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Mahasiswa> create(
            @RequestParam("nim") String nim,
            @RequestParam("nama") String nama,
            @RequestParam("jenis_kelamin") String jenisKelamin,
            @RequestParam("prodi") Integer prodi,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate tglMasuk,

            @RequestParam("status") String status,
            @RequestParam(value = "foto", required = false) MultipartFile foto) throws IOException {
        Mahasiswa mhs = mahasiswaService.create(nim, nama, jenisKelamin, prodi, tglMasuk, status, foto);
        return ResponseEntity.ok(mhs);
    }

    // Update
    @PutMapping(value = "/{id_mahasiswa}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Mahasiswa> update(
            @PathVariable("id_mahasiswa") Integer id_mahasiswa,
            @RequestParam("nim") String nim,
            @RequestParam("nama") String nama,
            @RequestParam("jenis_kelamin") String jenisKelamin,
            @RequestParam("prodi") Integer prodi,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate tglMasuk,
            @RequestParam(name = "tglLulus", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate tglLulus,
            @RequestParam("status") String status,
            @RequestParam(value = "foto", required = false) MultipartFile foto) throws IOException {

        Mahasiswa mhs = mahasiswaService.update(id_mahasiswa, nim, nama, jenisKelamin, prodi, tglMasuk, tglLulus,
                status, foto);
        return ResponseEntity.ok(mhs);
    }

    // delete

    @DeleteMapping("/{id_mahasiswa}")
    public ResponseEntity<Void> delete(@PathVariable("id_mahasiswa") Integer id_mahasiswa) throws IOException {
        mahasiswaService.delete(id_mahasiswa);
        return ResponseEntity.noContent().build();
    }
}
