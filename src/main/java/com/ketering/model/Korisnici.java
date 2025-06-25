package com.ketering.model;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
public class Korisnici {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "KorisnikID", nullable = false)
    private Integer id;

    @Column(name = "Ime", length = 100)
    private String ime;

    @Column(name = "Prezime", length = 100)
    private String prezime;

    @Column(name = "Email", length = 100)
    private String email;

    @Column(name = "Lozinka")
    private String lozinka;

    @ColumnDefault("'Klijent'")
    @Column(name = "Uloga")
    private String uloga;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    public String getUloga() {
        return uloga;
    }

    public void setUloga(String uloga) {
        this.uloga = uloga;
    }

}