package com.ketering.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class Jelovnik {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "JelovnikID")
    private Integer id;

    @Column(name = "Naziv", length = 100)
    private String naziv;

    @Lob
    @Column(name = "Opis")
    private String opis;

    @Column(name = "Cena", precision = 10, scale = 2)
    private BigDecimal cena;

    @Enumerated(EnumType.STRING)
    @Column(name = "Tip")
    private Tip tip;

    @Column(name = "Slika")
    private String slika;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public BigDecimal getCena() {
        return cena;
    }

    public void setCena(BigDecimal cena) {
        this.cena = cena;
    }

    public Tip getTip() {
        return tip;
    }

    public void setTip(Tip tip) {
        this.tip = tip;
    }

    public String getSlika() {
        return slika;
    }

    public void setSlika(String slika) {
        this.slika = slika;
    }
}
