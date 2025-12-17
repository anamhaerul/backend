package com.portalpolinema.backend.prodi;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.portalpolinema.backend.dosen.Dosen;
import com.portalpolinema.backend.prodi.Prodi.Akreditasi;
import com.portalpolinema.backend.prodi.Prodi.Jenjang;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdiService {

    private final ProdiRepository prodiRepository;
    private final ProdiFileService prodiFileService;

    // get all
    public List<Prodi> list() {
        return prodiRepository.findAll();
    }

    // get by id
    public Prodi getById(Integer id_prodi) {
        return prodiRepository.findById(id_prodi)
                .orElseThrow(() -> new RuntimeException("Prodi tidak ditemukan"));
    }

    // coutn
    public long count() {
        return prodiRepository.count();
    }

    // create
    public Prodi create(String nama, String jenjang, String deskripsi, String karir, String akreditasi,
            MultipartFile foto)
            throws IOException {
        Prodi pd = new Prodi();
        pd.setNama(nama);
        pd.setJenjang(Jenjang.valueOf(jenjang));
        pd.setDeskripsi(deskripsi);
        pd.setKarir(karir);
        pd.setAkreditasi(Akreditasi.valueOf(akreditasi));

        String filename = prodiFileService.saveFotoProdi(nama, foto);
        pd.setFoto(filename);

        return prodiRepository.save(pd);
    }

    // === UPDATE: ganti gambar / rename saja ===
    public Prodi update(Integer id_prodi, String nama, String jenjang, String deskripsi, String karir,
            String akreditasi,
            MultipartFile foto)
            throws IOException {
        Prodi pd = getById(id_prodi);

        String oldNama = pd.getNama();
        String oldFoto = pd.getFoto();

        // update nama
        pd.setNama(nama);
        pd.setJenjang(Jenjang.valueOf(jenjang));
        pd.setDeskripsi(deskripsi);
        pd.setKarir(karir);
        pd.setAkreditasi(Akreditasi.valueOf(akreditasi));

        if (foto != null && !foto.isEmpty()) {
            // KASUS 1: user upload gambar baru → hapus file lama, simpan file baru
            if (oldFoto != null) {
                prodiFileService.deleteFotoProdi(oldFoto);
            }
            String newFilename = prodiFileService.saveFotoProdi(nama, foto);
            pd.setFoto(newFilename);

        } else {
            // KASUS 2: tidak upload gambar baru → kalau nama berubah, rename file
            if (oldFoto != null && !oldNama.equals(nama)) {
                String renamedFilename = prodiFileService.renameFotoProdi(oldFoto, nama);
                pd.setFoto(renamedFilename);
            }
        }

        return prodiRepository.save(pd);
    }

    public void delete(Integer id_prodi) throws IOException {

        Prodi prodi = getById(id_prodi);
        prodiRepository.delete(prodi);

        if (prodi.getFoto() != null && !prodi.getFoto().isBlank() && !"default-prodi.png".equals(prodi.getFoto())) {
            prodiFileService.deleteFotoProdi(prodi.getFoto());
        }

    }
}
