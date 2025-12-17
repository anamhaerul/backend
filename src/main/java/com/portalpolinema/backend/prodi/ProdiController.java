package com.portalpolinema.backend.prodi;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/prodi")
@RequiredArgsConstructor
public class ProdiController {

    private final ProdiService prodiService;

    @GetMapping
    public List<Prodi> list() {
        return prodiService.list();
    }

    @GetMapping("/{id_prodi}") // <-- ganti
    public Prodi get(@PathVariable("id_prodi") Integer id_prodi) {
        return prodiService.getById(id_prodi);
    }

    @GetMapping("/count")
    public long count() {
        return prodiService.count();
    }

    // Create
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Prodi> create(
            @RequestParam("nama") String nama,
            @RequestParam("jenjang") String jenjang,
            @RequestParam("deskripsi") String deskripsi,
            @RequestParam("karir") String karir,
            @RequestParam("akreditasi") String akreditasi,
            @RequestParam(value = "foto", required = false) MultipartFile foto) throws IOException {
        Prodi prodi = prodiService.create(nama, jenjang, deskripsi, karir, akreditasi, foto);
        return ResponseEntity.ok(prodi);
    }

    // update
    @PutMapping(value = "/{id_prodi}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE) // <-- ganti
    public ResponseEntity<Prodi> update(
            @PathVariable("id_prodi") Integer id_prodi, // <-- ganti
            @RequestParam("nama") String nama,
            @RequestParam("jenjang") String jenjang,
            @RequestParam("deskripsi") String deskripsi,
            @RequestParam("karir") String karir,
            @RequestParam("akreditasi") String akreditasi,
            @RequestParam(value = "foto", required = false) MultipartFile foto) throws IOException {
        Prodi prodi = prodiService.update(id_prodi, nama, jenjang, deskripsi, karir, akreditasi, foto);
        return ResponseEntity.ok(prodi);
    }

    // delete
    @DeleteMapping("/{id_prodi}") // <-- ganti
    public ResponseEntity<Void> delete(@PathVariable("id_prodi") Integer id_prodi) throws IOException {
        prodiService.delete(id_prodi);
        return ResponseEntity.noContent().build();
    }
}
