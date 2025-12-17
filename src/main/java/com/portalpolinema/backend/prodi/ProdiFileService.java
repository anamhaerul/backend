package com.portalpolinema.backend.prodi;

import com.portalpolinema.backend.shared.SlugUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

@Service
public class ProdiFileService {

    @Value("${portalpolinema.upload-dir-prodi}")
    private String uploadDirProdi;

    public String saveFotoProdi(String namaProdi, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String slug = SlugUtil.slugify(namaProdi); // "teknologi-informasi"
        String ext = StringUtils.getFilenameExtension(file.getOriginalFilename());

        if (ext == null || ext.isBlank()) {
            ext = "jpg";
        }

        ext = ext.toLowerCase();
        String filename = slug + "." + ext; // "teknologi-informasi.jpg"

        Path uploadPath = Paths.get(uploadDirProdi);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path target = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        return filename;
    }

    // === BARU: rename file kalau hanya ganti nama prodi ===
    public String renameFotoProdi(String oldFilename, String newNamaProdi) throws IOException {
        if (oldFilename == null || oldFilename.isBlank()) {
            return null;
        }

        String ext = StringUtils.getFilenameExtension(oldFilename);
        if (ext == null || ext.isBlank()) {
            ext = "jpg";
        }
        ext = ext.toLowerCase();

        String newSlug = SlugUtil.slugify(newNamaProdi);
        String newFilename = newSlug + "." + ext;

        // kalau namanya sama persis, tidak usah apa-apa
        if (newFilename.equals(oldFilename)) {
            return oldFilename;
        }

        Path uploadPath = Paths.get(uploadDirProdi);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path oldPath = uploadPath.resolve(oldFilename);
        Path newPath = uploadPath.resolve(newFilename);

        if (Files.exists(oldPath)) {
            Files.move(oldPath, newPath, StandardCopyOption.REPLACE_EXISTING);
        }

        return newFilename;
    }

    public void deleteFotoProdi(String filename) throws IOException {
        if (filename == null || filename.isBlank())
            return;

        Path uploadPath = Paths.get(uploadDirProdi);
        Path target = uploadPath.resolve(filename);
        if (Files.exists(target)) {
            Files.delete(target);
        }
    }
}
