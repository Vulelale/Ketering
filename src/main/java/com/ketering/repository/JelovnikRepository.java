package com.ketering.repository;

import com.ketering.model.Jelovnik;
import com.ketering.model.Tip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JelovnikRepository extends JpaRepository<Jelovnik, Integer> {
    Page<Jelovnik> findByTip(Tip tip, Pageable pageable);


}
