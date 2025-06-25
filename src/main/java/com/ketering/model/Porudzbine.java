package com.ketering.model;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import jakarta.persistence.Column;


import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Porudzbine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PorudzbinaID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "KorisnikID")
    private Korisnici korisnikID;

    @ColumnDefault("current_timestamp()")
    @Column(name = "DatumPorudzbine", columnDefinition = "DATETIME")
    private LocalDateTime datumPorudzbine;

    @ColumnDefault("'Na čekanju'")
    @Lob
    @Column(name = "Status")
    private String status;

    @Lob
    @Column(name = "Napomena")
    private String napomena;

    @OneToMany(mappedBy = "porudzbinaID", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<StavkePorudzbine> stavke;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Korisnici getKorisnikID() {
        return korisnikID;
    }

    public void setKorisnikID(Korisnici korisnikID) {
        this.korisnikID = korisnikID;
    }

    public LocalDateTime getDatumPorudzbine() {
        return datumPorudzbine;
    }

    public void setDatumPorudzbine(LocalDateTime datumPorudzbine) {
        this.datumPorudzbine = datumPorudzbine;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNapomena() {
        return napomena;
    }

    public void setNapomena(String napomena) {
        this.napomena = napomena;
    }

    public List<StavkePorudzbine> getStavke() {
        return stavke;
    }

    public void setStavke(List<StavkePorudzbine> stavke) {
        this.stavke = stavke;
    }

}