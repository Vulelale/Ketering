package com.ketering.model;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
public class StavkePorudzbine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "StavkaID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PorudzbinaID")
    private Porudzbine porudzbinaID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "JelovnikID")
    private Jelovnik jelovnikID;

    @ColumnDefault("1")
    @Column(name = "Kolicina")
    private Integer kolicina;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Porudzbine getPorudzbinaID() {
        return porudzbinaID;
    }

    public void setPorudzbinaID(Porudzbine porudzbinaID) {
        this.porudzbinaID = porudzbinaID;
    }

    public Jelovnik getJelovnikID() {
        return jelovnikID;
    }

    public void setJelovnikID(Jelovnik jelovnikID) {
        this.jelovnikID = jelovnikID;
    }

    public Integer getKolicina() {
        return kolicina;
    }

    public void setKolicina(Integer kolicina) {
        this.kolicina = kolicina;
    }

}