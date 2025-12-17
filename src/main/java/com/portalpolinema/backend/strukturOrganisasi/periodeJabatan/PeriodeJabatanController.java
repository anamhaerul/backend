package com.portalpolinema.backend.strukturOrganisasi.periodeJabatan;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Year;
import java.util.List;

@RestController
@RequestMapping("/api/periode-jabatan")
@RequiredArgsConstructor

public class PeriodeJabatanController {
    private final PeriodeJabatanService periodeJabatanService;

    // get all
    @GetMapping
    public List<PeriodeJabatan> list() {
        return periodeJabatanService.list();
    }

    // get by id
    @GetMapping("/{id_periode_jabatan}")
    public PeriodeJabatan get(@PathVariable("id_periode_jabatan") Integer id_periode_jabatan) {
        return periodeJabatanService.getById(id_periode_jabatan);
    }

    // create
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PeriodeJabatan> create(
            @RequestParam("start") Integer start,
            @RequestParam("end") Integer end,
            @RequestParam(value = "foto", required = false) MultipartFile foto) throws IOException {
        PeriodeJabatan periodeJabatan = periodeJabatanService.create(start, end, foto);
        return ResponseEntity.ok(periodeJabatan);
    }

    // update
    @PutMapping(value = "/{id_periode_jabatan}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PeriodeJabatan> update(
            @PathVariable("id_periode_jabatan") Integer id_periode_jabatan,
            @RequestParam("start") Integer start,
            @RequestParam("end") Integer end,
            @RequestParam(value = "foto", required = false) MultipartFile foto) throws IOException {
        PeriodeJabatan periodeJabatan = periodeJabatanService.update(id_periode_jabatan, start, end, foto);
        return ResponseEntity.ok(periodeJabatan);
    }

    // delete
    @DeleteMapping("/{id_periode_jabatan}")
    public ResponseEntity<Void> delete(@PathVariable("id_periode_jabatan") Integer id_periode_jabatan)
            throws IOException {
        periodeJabatanService.delete(id_periode_jabatan);
        return ResponseEntity.noContent().build();
    }
}
