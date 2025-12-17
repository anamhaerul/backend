package com.portalpolinema.backend.dosen;

import com.portalpolinema.backend.shared.SlugUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

@Service
public class DosenFileService {

    @Value("${portalpolinema.upload-dir-dosen}")
    private String uploadDirDosen;

    /**
     * Simpan foto dosen baru.
     * namaBase bisa pakai nidn atau nama dosen.
     */
    // simpan foto baru
    public String storeFotoDosen(String namaBase, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String ext = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex > 0) {
            ext = originalFilename.substring(dotIndex); // ".jpg", ".png", dll
        }

        String slug = SlugUtil.slugify(namaBase);
        if (slug.isBlank()) {
            slug = "dosen";
        }

        String newFilename = slug + ext;

        Path uploadPath = Paths.get(uploadDirDosen);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path target = uploadPath.resolve(newFilename);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        return newFilename;
    }

    /**
     * Rename foto lama kalau nama / nidn berubah tapi tidak upload file baru.
     */
    public String renameExistingFoto(String oldFilename, String namaBase) throws IOException {
        if (oldFilename == null || oldFilename.isBlank()) {
            return oldFilename;
        }

        String ext = "";
        int dotIndex = oldFilename.lastIndexOf('.');
        if (dotIndex > 0) {
            ext = oldFilename.substring(dotIndex);
        }

        String slug = SlugUtil.slugify(namaBase);
        if (slug.isBlank()) {
            slug = "dosen";
        }

        String newFilename = slug + ext;

        // kalau sama, tidak perlu rename
        if (newFilename.equals(oldFilename)) {
            return oldFilename;
        }

        Path uploadPath = Paths.get(uploadDirDosen);
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

    public void deleteFotoDosen(String filename) throws IOException {
        if (filename == null || filename.isBlank()) {
            return;
        }

        Path uploadPath = Paths.get(uploadDirDosen);
        Path target = uploadPath.resolve(filename);
        if (Files.exists(target)) {
            Files.delete(target);
        }
    }
}
