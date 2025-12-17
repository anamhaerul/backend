package com.portalpolinema.backend.dosen;

import com.portalpolinema.backend.dosen.Dosen.JenisKelamin;
import com.portalpolinema.backend.prodi.ProdiRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DosenService {

    private final DosenRepository dosenRepository;
    private final DosenFileService dosenFileService;
    private final ProdiRepository prodiRepository;

    public List<Dosen> list() {
        return dosenRepository.findAll();
    }

    public Dosen getById(Integer id_dosen) {
        return dosenRepository.findById(id_dosen)
                .orElseThrow(() -> new RuntimeException("Dosen tidak ditemukan"));
    }

    public long count() {
        return dosenRepository.count();
    }

    // CREATE
    public Dosen create(String nip, String nama, String email, String jenis_kelamin, Integer prodi,
            MultipartFile foto) throws IOException {

        List<String> errors = new ArrayList<>();
        if (dosenRepository.existsByNip(nip))
            errors.add("NIP sudah terpakai");
        if (dosenRepository.existsByEmail(email))
            errors.add("Email sudah terpakai");
        if (!errors.isEmpty())
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.join(" | ", errors));

        Dosen dosen = new Dosen();
        dosen.setNip(nip);
        dosen.setNama(nama);
        dosen.setEmail(email);
        dosen.setJenisKelamin(JenisKelamin.valueOf(jenis_kelamin));
        var prodi_obj = prodiRepository.findById(
                prodi)
                .orElseThrow(() -> new RuntimeException("Prodi tidak ditemukan"));
        dosen.setProdi(prodi_obj);

        String filename = dosenFileService.storeFotoDosen(nip, foto);
        if (filename == null || filename.isBlank())
            filename = "default-dosen.png";
        dosen.setFoto(filename);

        return dosenRepository.save(dosen);
    }

    // UPDATE
    public Dosen update(Integer id_dosen, String nipBaru, String namaBaru, String emailBaru, String jenisKelaminBaru,
            Integer prodiIdBaru, MultipartFile fotoBaru) throws IOException {

        Dosen dosen = getById(id_dosen);

        // update dosen
        String oldFoto = dosen.getFoto();
        String oldNidn = dosen.getNip();

        dosen.setNip(nipBaru);
        dosen.setNama(namaBaru);
        dosen.setEmail(emailBaru);
        dosen.setJenisKelamin(JenisKelamin.valueOf(jenisKelaminBaru));
        if (prodiIdBaru != null) {
            var prodi = prodiRepository.findById(prodiIdBaru)
                    .orElseThrow(() -> new RuntimeException("Prodi tidak ditemukan"));
            dosen.setProdi(prodi);
        }

        if (fotoBaru != null && !fotoBaru.isEmpty()) {
            if (oldFoto != null && !oldFoto.isBlank() && !"default-dosen.png".equals(oldFoto)) {
                try {
                    dosenFileService.deleteFotoDosen(oldFoto);
                } catch (IOException ignore) {
                }
            }
            dosen.setFoto(dosenFileService.storeFotoDosen(nipBaru, fotoBaru));
        } else if (!nipBaru.equals(oldNidn)
                && oldFoto != null && !oldFoto.isBlank()
                && !"default-dosen.png".equals(oldFoto)) {
            dosen.setFoto(dosenFileService.renameExistingFoto(oldFoto, nipBaru));
        }

        return dosenRepository.save(dosen);
    }

    public void delete(Integer id_dosen) throws IOException {
        Dosen dosen = getById(id_dosen);
        dosenRepository.delete(dosen);

        if (dosen.getFoto() != null && !dosen.getFoto().isBlank() && !"default-dosen.png".equals(dosen.getFoto())) {
            dosenFileService.deleteFotoDosen(dosen.getFoto());
        }
    }
}
