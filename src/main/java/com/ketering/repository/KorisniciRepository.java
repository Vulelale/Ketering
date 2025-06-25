
package com.ketering.repository;

import com.ketering.model.Korisnici;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KorisniciRepository extends JpaRepository<Korisnici, Integer> {
    boolean existsByEmail(String email);
    Korisnici findByEmail(String email);
}

