package com.portalpolinema.backend.config; // <--- PENTING: sesuaikan dengan folder

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

        @Value("${portalpolinema.upload-dir-prodi}")
        private String uploadDirProdi;

        @Value("${portalpolinema.upload-dir-dosen}")
        private String uploadDirDosen;

        @Value("${portalpolinema.upload-dir-struktur-organisasi}")
        private String uploadDirStrukturOrganisasi;

        @Value("${portalpolinema.upload-dir-mahasiswa}")
        private String uploadDirMahasiswa;

        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
                // ubah backslash ke slash, lalu tambahkan prefix "file:"
                String location = "file:" + uploadDirProdi.replace("\\", "/") + "/";
                String location2 = "file:" + uploadDirDosen.replace("\\", "/") + "/";
                String location3 = "file:" + uploadDirStrukturOrganisasi.replace("\\", "/") + "/";
                String location4 = "file:" + uploadDirMahasiswa.replace("\\", "/") + "/";

                // URL: http://localhost:8080/gambar/prodi/foto.png
                // Fisik: D:/.../backend/gambar/prodi/foto.png
                registry.addResourceHandler("/img/prodi/**")
                                .addResourceLocations(location);

                registry.addResourceHandler("/img/dosen/**")
                                .addResourceLocations(location2);

                registry.addResourceHandler("/img/struktur-organisasi/**")
                                .addResourceLocations(location3);

                registry.addResourceHandler("/img/mahasiswa/**")
                                .addResourceLocations(location4);
        }
}
