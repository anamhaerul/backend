package com.portalpolinema.backend.strukturOrganisasi.kategoriJabatan;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.portalpolinema.backend.prodi.Prodi;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KategoriJabatanService {

    private final KategoriJabatanRepository kategoriJabatanRepository;

    public List<KategoriJabatan> list() {
        return kategoriJabatanRepository.findAll();
    }

    // get all
    public KategoriJabatan getById(Integer id_kategori_jabatan) {
        return kategoriJabatanRepository.findById(id_kategori_jabatan)
                .orElseThrow(() -> new RuntimeException("Kategori Jabatan tidak ditemukan"));
    }

    // create
    public KategoriJabatan create(String nama) throws IOException {
        KategoriJabatan KategoriJabatan = new KategoriJabatan();
        KategoriJabatan.setNama(nama);

        return kategoriJabatanRepository.save(KategoriJabatan);
    }

    // update
    public KategoriJabatan update(Integer id_kategori_jabatan, String nama) throws IOException {
        KategoriJabatan KategoriJabatan = getById(id_kategori_jabatan);

        KategoriJabatan.setNama(nama);

        return kategoriJabatanRepository.save(KategoriJabatan);
    }

    public void delete(Integer id_kategori_jabatan) throws IOException {
        KategoriJabatan KategoriJabatan = getById(id_kategori_jabatan);

        kategoriJabatanRepository.delete(KategoriJabatan);
    }
}
