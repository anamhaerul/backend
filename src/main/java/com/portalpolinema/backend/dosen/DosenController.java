package com.portalpolinema.backend.dosen;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/dosen")
@RequiredArgsConstructor

public class DosenController {

    private final DosenService dosenService;

    @GetMapping
    public List<Dosen> list() {
        return dosenService.list();
    }

    @GetMapping("/{id_dosen}") // <-- ganti
    public Dosen get(@PathVariable("id_dosen") Integer id_dosen) {
        return dosenService.getById(id_dosen);
    }

    @GetMapping("/count")
    public long count() {
        return dosenService.count();
    }

    // create
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Dosen> create(
            @RequestParam("nip") String nip,
            @RequestParam("nama") String nama,
            @RequestParam("email") String email,
            @RequestParam("jenis_kelamin") String jenis_kelamin,
            @RequestParam("prodi") Integer prodi,
            @RequestParam(value = "foto", required = false) MultipartFile foto) throws IOException {
        Dosen dosen = dosenService.create(nip, nama, email, jenis_kelamin, prodi, foto);
        return ResponseEntity.ok(dosen);
    }

    // Update
    @PutMapping(value = "/{id_dosen}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Dosen> update(
            @PathVariable("id_dosen") Integer id_dosen,
            @RequestParam("nip") String nip,
            @RequestParam("nama") String nama,
            @RequestParam("email") String email,
            @RequestParam("jenis_kelamin") String jenis_kelamin,
            @RequestParam("prodi") Integer prodi,
            @RequestParam(value = "foto", required = false) MultipartFile foto) throws IOException {

        Dosen dosen = dosenService.update(id_dosen, nip, nama, email, jenis_kelamin, prodi, foto);
        return ResponseEntity.ok(dosen);
    }

    // delete

    @DeleteMapping("/{id_dosen}")
    public ResponseEntity<Void> delete(@PathVariable("id_dosen") Integer id_dosen) throws IOException {
        dosenService.delete(id_dosen);
        return ResponseEntity.noContent().build();
    }
}
