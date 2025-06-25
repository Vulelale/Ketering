package com.ketering.model;

import jakarta.persistence.*;

@Entity
public class StavkeDogadjaja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "StavkaDogadjajaID")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DogadjajID")
    private Dogadjaji dogadjaj;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "JelovnikID")
    private Jelovnik jelovnik;

    @Column(name = "Kolicina")
    private Integer kolicina;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Dogadjaji getDogadjaj() {
        return dogadjaj;
    }

    public void setDogadjaj(Dogadjaji dogadjaj) {
        this.dogadjaj = dogadjaj;
    }

    public Jelovnik getJelovnik() {
        return jelovnik;
    }

    public void setJelovnik(Jelovnik jelovnik) {
        this.jelovnik = jelovnik;
    }

    public Integer getKolicina() {
        return kolicina;
    }

    public void setKolicina(Integer kolicina) {
        this.kolicina = kolicina;
    }
}

