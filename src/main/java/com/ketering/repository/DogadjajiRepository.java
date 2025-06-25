package com.ketering.repository;

import com.ketering.model.Dogadjaji;
import com.ketering.model.Korisnici;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DogadjajiRepository extends JpaRepository<Dogadjaji, Integer> {
    List<Dogadjaji> findByKorisnik(Korisnici korisnik);
    List<Dogadjaji> findAllByOrderByDatumAsc();


}
