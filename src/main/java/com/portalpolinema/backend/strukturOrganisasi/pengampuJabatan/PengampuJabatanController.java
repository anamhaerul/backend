package com.portalpolinema.backend.strukturOrganisasi.pengampuJabatan;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/pengampu_jabatan")
@RequiredArgsConstructor

public class PengampuJabatanController {
    private final PengampuJabatanService pengampuJabatanService;

    @GetMapping
    public List<PengampuJabatan> list() {
        return pengampuJabatanService.list();
    }

    @GetMapping("/count")
    public long count() {
        return pengampuJabatanService.count();
    }

    @GetMapping("/{id_pengampu_jabatan}") // <-- ganti
    public PengampuJabatan get(@PathVariable("id_pengampu_jabatan") Integer id_pengampu_jabatan) {
        return pengampuJabatanService.getById(id_pengampu_jabatan);
    }

    // create
    @PostMapping
    public ResponseEntity<PengampuJabatan> create(
            @RequestParam("dosen") Integer dosen,
            @RequestParam("periodeJabatan") Integer periodeJabatan,
            @RequestParam("kategoriJabatan") Integer kategoriJabatan) throws IOException {
        PengampuJabatan pj = pengampuJabatanService.create(dosen, periodeJabatan, kategoriJabatan);
        return ResponseEntity.ok(pj);
    }

    // Update
    @PutMapping(value = "/{id_pengampu_jabatan}")
    public ResponseEntity<PengampuJabatan> update(
            @PathVariable("id_pengampu_jabatan") Integer id_pengampu_jabatan,
            @RequestParam("dosen") Integer dosen,
            @RequestParam("periodeJabatan") Integer periodeJabatan,
            @RequestParam("kategoriJabatan") Integer kategoriJabatan) throws IOException {

        PengampuJabatan pj = pengampuJabatanService.update(id_pengampu_jabatan, dosen, periodeJabatan, kategoriJabatan);
        return ResponseEntity.ok(pj);
    }

    // delete
    @DeleteMapping("/{id_pengampu_jabatan}")
    public ResponseEntity<Void> delete(@PathVariable("id_pengampu_jabatan") Integer id_pengampu_jabatan)
            throws IOException {
        pengampuJabatanService.delete(id_pengampu_jabatan);
        return ResponseEntity.noContent().build();
    }
}
