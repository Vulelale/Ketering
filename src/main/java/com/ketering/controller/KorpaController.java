package com.ketering.controller;

import com.ketering.model.Jelovnik;
import com.ketering.model.Korisnici;
import com.ketering.model.Porudzbine;
import com.ketering.model.StavkePorudzbine;
import com.ketering.repository.JelovnikRepository;
import com.ketering.repository.PorudzbinaRepository;
import com.ketering.repository.StavkePorudzbineRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/korpa")
public class KorpaController {

    @Autowired
    private JelovnikRepository jelovnikRepo;

    @Autowired
    private PorudzbinaRepository porudzbinaRepo;

    @Autowired
    private StavkePorudzbineRepository stavkaRepo;

    @PostMapping("/dodaj")
    public String dodajUKorpu(@RequestParam("jelovnikId") Integer jelovnikId,
                              @RequestParam("kolicina") Integer kolicina,
                              HttpSession session) {
        Korisnici korisnik = (Korisnici) session.getAttribute("user");
        if (korisnik == null) {
            session.setAttribute("poruka", "Morate biti ulogovani da biste poručili.");
            return "redirect:/login";
        }
        List<StavkePorudzbine> korpa = (List<StavkePorudzbine>) session.getAttribute("korpa");
        if (korpa == null) korpa = new ArrayList<>();

        Jelovnik jelo = jelovnikRepo.findById(jelovnikId).orElseThrow();

        StavkePorudzbine stavka = new StavkePorudzbine();
        stavka.setJelovnikID(jelo);
        stavka.setKolicina(kolicina);
        korpa.add(stavka);

        session.setAttribute("korpa", korpa);
        return "redirect:/jelovnik?dodato";
    }

    @GetMapping("")
    public String prikaziKorpu(Model model, HttpSession session) {
        List<StavkePorudzbine> korpa = (List<StavkePorudzbine>) session.getAttribute("korpa");

        if (korpa == null) {
            korpa = new ArrayList<>();
        }

        double ukupnaCena = korpa.stream()
                .mapToDouble(s -> s.getKolicina() * s.getJelovnikID().getCena().doubleValue())
                .sum();

        model.addAttribute("korpa", korpa);
        model.addAttribute("ukupnaCena", ukupnaCena);

        return "jelovnik/korpa"; // Thymeleaf šablon na lokaciji: resources/templates/jelovnik/korpa.html
    }

    @PostMapping("/potvrdi")
    public String potvrdiPorudzbinu(@RequestParam(name = "napomena", required = false) String napomena,
                                    HttpSession session) {
        Korisnici korisnik = (Korisnici) session.getAttribute("user");
        List<StavkePorudzbine> korpa = (List<StavkePorudzbine>) session.getAttribute("korpa");

        if (korpa == null || korpa.isEmpty()) return "redirect:/korpa?empty";

        Porudzbine p = new Porudzbine();
        p.setKorisnikID(korisnik);
        p.setDatumPorudzbine(LocalDateTime.now());
        p.setStatus("Na čekanju");
        p.setNapomena(napomena != null ? napomena.trim() : "");  // ovde sada koristiš parametar napomena
        porudzbinaRepo.save(p);

        for (StavkePorudzbine stavka : korpa) {
            stavka.setPorudzbinaID(p);
            stavkaRepo.save(stavka);
        }

        session.removeAttribute("korpa");
        return "redirect:/korpa?uspesno";
    }

    @GetMapping("/ukloni")
    public String ukloniIzKorpe(@RequestParam("index") int index, HttpSession session) {
        List<StavkePorudzbine> korpa = (List<StavkePorudzbine>) session.getAttribute("korpa");

        if (korpa != null && index >= 0 && index < korpa.size()) {
            korpa.remove(index);
            session.setAttribute("korpa", korpa);
        }

        return "redirect:/korpa";
    }


}

