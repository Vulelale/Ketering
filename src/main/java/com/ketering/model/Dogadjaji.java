package com.ketering.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Dogadjaji {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DogadjajID")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "KorisnikID")
    private Korisnici korisnik;

    @Column(name = "Naziv", length = 100)
    private String naziv;

    @Column(name = "Datum")
    private LocalDateTime datum;

    @Column(name = "Lokacija", length = 255)
    private String lokacija;

    @Column(name = "BrojOsoba")
    private Integer brojOsoba;

    @Lob
    @Column(name = "Opis")
    private String opis;

    @OneToMany(mappedBy = "dogadjaj", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StavkeDogadjaja> stavke;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Korisnici getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(Korisnici korisnik) {
        this.korisnik = korisnik;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public LocalDateTime getDatum() {
        return datum;
    }

    public void setDatum(LocalDateTime datum) {
        this.datum = datum;
    }

    public String getLokacija() {
        return lokacija;
    }

    public void setLokacija(String lokacija) {
        this.lokacija = lokacija;
    }

    public Integer getBrojOsoba() {
        return brojOsoba;
    }

    public void setBrojOsoba(Integer brojOsoba) {
        this.brojOsoba = brojOsoba;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public List<StavkeDogadjaja> getStavke() {
        return stavke;
    }

    public void setStavke(List<StavkeDogadjaja> stavke) {
        this.stavke = stavke;
    }
}

