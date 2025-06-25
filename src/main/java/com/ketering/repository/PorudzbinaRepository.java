package com.ketering.repository;

import com.ketering.model.Korisnici;
import com.ketering.model.Porudzbine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface PorudzbinaRepository extends JpaRepository<Porudzbine, Integer> {
    List<Porudzbine> findByKorisnikID(Korisnici korisnik);

    @Query("SELECT p FROM Porudzbine p WHERE p.korisnikID = :korisnik"
            + " AND (:status IS NULL OR p.status = :status)")
    Page<Porudzbine> findByKorisnikAndStatus(@Param("korisnik") Korisnici korisnik,
                                             @Param("status") String status,
                                             Pageable pageable);

    Page<Porudzbine> findByStatus(String status, Pageable pageable);


}
