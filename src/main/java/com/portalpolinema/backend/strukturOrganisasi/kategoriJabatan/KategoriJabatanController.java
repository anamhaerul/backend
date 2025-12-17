package com.portalpolinema.backend.strukturOrganisasi.kategoriJabatan;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/kategori-jabatan")
@RequiredArgsConstructor

public class KategoriJabatanController {
    private final KategoriJabatanService kategoriJabatanService;

    @GetMapping
    public List<KategoriJabatan> list() {
        return kategoriJabatanService.list();
    }

    @GetMapping("/{id_kategori_jabatan}")
    public KategoriJabatan get(@PathVariable("id_kategori_jabatan") Integer id_kategori_jabatan) {
        return kategoriJabatanService.getById(id_kategori_jabatan);
    }

    // create
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<KategoriJabatan> create(
            @RequestParam("nama") String nama) throws IOException {
        KategoriJabatan kategoriJabatan = kategoriJabatanService.create(nama);
        return ResponseEntity.ok(kategoriJabatan);
    }

    // Update
    @PutMapping(value = "/{id_kategori_jabatan}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<KategoriJabatan> update(
            @PathVariable("id_kategori_jabatan") Integer id_kategori_jabatan,
            @RequestParam("nama") String nama) throws IOException {
        KategoriJabatan kategoriJabatan = kategoriJabatanService.update(id_kategori_jabatan, nama);
        return ResponseEntity.ok(kategoriJabatan);
    }

    // delete
    @DeleteMapping("/{id_kategori_jabatan}")
    public ResponseEntity<Void> delete(@PathVariable("id_kategori_jabatan") Integer id_kategori_jabatan)
            throws IOException {
        kategoriJabatanService.delete(id_kategori_jabatan);
        return ResponseEntity.noContent().build();
    }
}
