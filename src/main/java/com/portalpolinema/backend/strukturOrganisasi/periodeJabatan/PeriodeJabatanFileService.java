package com.portalpolinema.backend.strukturOrganisasi.periodeJabatan;

import com.portalpolinema.backend.shared.SlugUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.Year;

@Service
public class PeriodeJabatanFileService {

    // create
    @Value("${portalpolinema.upload-dir-struktur-organisasi}")
    private String uploadDirPeriodeJabatan;

    public String saveFotoPeriodeJabatan(Year start, Year end, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String ext = StringUtils.getFilenameExtension(file.getOriginalFilename());

        if (ext == null || ext.isBlank()) {
            ext = "jpg";
        }

        ext = ext.toLowerCase();
        String filename = start + "-" + end + "." + ext;

        Path uploadPath = Paths.get(uploadDirPeriodeJabatan);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path target = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        return filename;
    }

    // update
    public String renameFotoPeriodeJabatan(String oldFilename, Year newstart, Year newend) throws IOException {
        if (oldFilename == null || oldFilename.isBlank()) {
            return null;
        }

        String ext = StringUtils.getFilenameExtension(oldFilename);
        if (ext == null || ext.isBlank()) {
            ext = "jpg";
        }
        ext = ext.toLowerCase();

        String newFilename = newstart + "-" + newend + "." + ext;

        // kalau namanya sama persis, tidak usah apa-apa
        if (newFilename.equals(oldFilename)) {
            return oldFilename;
        }

        Path uploadPath = Paths.get(uploadDirPeriodeJabatan);
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

    // delete
    public void deleteFotoPeriodeJabatan(String filename) throws IOException {
        if (filename == null || filename.isBlank())
            return;

        Path uploadPath = Paths.get(uploadDirPeriodeJabatan);
        Path target = uploadPath.resolve(filename);
        if (Files.exists(target)) {
            Files.delete(target);
        }
    }
}
