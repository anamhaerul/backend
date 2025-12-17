package com.portalpolinema.backend.strukturOrganisasi.periodeJabatan;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Year;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PeriodeJabatanService {

    private final PeriodeJabatanRepository periodeJabatanRepository;
    private final PeriodeJabatanFileService periodeJabatanFileService;

    public List<PeriodeJabatan> list() {
        return periodeJabatanRepository.findAll();
    }

    public PeriodeJabatan getById(Integer id_periode_jabatan) {
        return periodeJabatanRepository.findById(id_periode_jabatan)
                .orElseThrow(() -> new RuntimeException("periode jabatan tidak ditemukan"));
    }

    public PeriodeJabatan create(Integer start, Integer end, MultipartFile foto) throws IOException {
        PeriodeJabatan pj = new PeriodeJabatan();
        pj.setStart(Year.of(start));
        pj.setEnd(Year.of(end));

        String filename = periodeJabatanFileService.saveFotoPeriodeJabatan(pj.getStart(), pj.getEnd(), foto);
        pj.setFoto(filename);

        return periodeJabatanRepository.save(pj);
    }

    // update
    public PeriodeJabatan update(Integer id_periode_jabatan, Integer start, Integer end, MultipartFile foto)
            throws IOException {
        PeriodeJabatan pj = getById(id_periode_jabatan);

        Year oldStart = pj.getStart();
        Year oldEnd = pj.getEnd();
        String oldFoto = pj.getFoto();

        pj.setStart(Year.of(start));
        pj.setEnd(Year.of(end));

        if (foto != null && !foto.isEmpty()) {
            // KASUS 1: user upload gambar baru → hapus file lama, simpan file baru
            if (oldFoto != null) {
                periodeJabatanFileService.deleteFotoPeriodeJabatan(oldFoto);
            }
            String newFilename = periodeJabatanFileService.saveFotoPeriodeJabatan(pj.getStart(), pj.getEnd(), foto);
            pj.setFoto(newFilename);

        } else if (oldFoto != null && !oldFoto.isBlank() && (!start.equals(oldStart) || !end.equals(oldEnd))) {
            pj.setFoto(periodeJabatanFileService.renameFotoPeriodeJabatan(oldFoto, pj.getStart(), pj.getEnd()));
        }

        return periodeJabatanRepository.save(pj);
    }

    // delete
    public void delete(Integer id_periode_jabatan) throws IOException {
        PeriodeJabatan periodeJabatan = getById(id_periode_jabatan);
        if (periodeJabatan.getFoto() != null) {
            periodeJabatanFileService.deleteFotoPeriodeJabatan(periodeJabatan.getFoto());
        }
        periodeJabatanRepository.delete(periodeJabatan);
    }
}
