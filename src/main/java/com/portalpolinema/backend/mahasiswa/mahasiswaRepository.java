package com.portalpolinema.backend.mahasiswa;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface mahasiswaRepository extends JpaRepository<Mahasiswa, Integer> {
    boolean existsByNim(String nim);

    List<Mahasiswa> findByStatus(Mahasiswa.Status status);

    List<Mahasiswa> findByNamaContainingIgnoreCase(String nama);

    long countByStatus(Mahasiswa.Status status);

}