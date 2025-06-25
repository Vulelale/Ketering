package com.ketering.controller;

import com.ketering.model.Dogadjaji;
import com.ketering.model.Korisnici;
import com.ketering.model.Porudzbine;
import com.ketering.model.StavkePorudzbine;
import com.ketering.repository.DogadjajiRepository;
import com.ketering.repository.KorisniciRepository;
import com.ketering.repository.PorudzbinaRepository;
import com.ketering.repository.StavkeDogadjajaRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/korisnik")
public class KorisnikController {

    @Autowired
    private KorisniciRepository korisnikRepo;

    @Autowired
    private PorudzbinaRepository porudzbinaRepo;

    @Autowired
    private DogadjajiRepository dogadjajiRepo;

    @Autowired
    private StavkeDogadjajaRepository stavkeDogadjajaRepo;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping("/dashboard")
    public String dashboard(Model model,
                            HttpSession session,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "5") int size,
                            @RequestParam(required = false) String status) {

        Korisnici korisnik = (Korisnici) session.getAttribute("user");
        if (korisnik == null || !"Klijent".equals(korisnik.getUloga())) {
            return "redirect:/login";
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Porudzbine> porudzbinePage = porudzbinaRepo.findByKorisnikAndStatus(korisnik,
                (status == null || status.isBlank()) ? null : status, pageable);

        Map<Integer, BigDecimal> ukupneCene = new HashMap<>();
        for (Porudzbine p : porudzbinePage.getContent()) {
            BigDecimal ukupnaCena = BigDecimal.ZERO;
            for (StavkePorudzbine s : p.getStavke()) {
                BigDecimal kolicina = BigDecimal.valueOf(s.getKolicina());
                BigDecimal cena = s.getJelovnikID().getCena();
                ukupnaCena = ukupnaCena.add(cena.multiply(kolicina));
            }
            ukupneCene.put(p.getId(), ukupnaCena);
        }





        List<Dogadjaji> rezervacije = dogadjajiRepo.findByKorisnik(korisnik);
        Map<Integer, BigDecimal> ukupneCeneRezervacija = new HashMap<>();
        for (Dogadjaji d : rezervacije) {
            BigDecimal ukupno = BigDecimal.ZERO;
            for (var stavka : d.getStavke()) {
                BigDecimal kolicina = BigDecimal.valueOf(stavka.getKolicina());
                BigDecimal cena = stavka.getJelovnik().getCena();
                ukupno = ukupno.add(cena.multiply(kolicina));
            }
            ukupneCeneRezervacija.put(d.getId(), ukupno);
        }

        model.addAttribute("ukupneCeneRezervacija", ukupneCeneRezervacija);

        model.addAttribute("rezervacije", rezervacije);

        model.addAttribute("korisnik", korisnik);
        model.addAttribute("porudzbinePage", porudzbinePage);
        model.addAttribute("ukupneCene", ukupneCene);
        model.addAttribute("statusFilter", status);

        return "korisnik/dashboard";
    }

    @PostMapping("/otkazi-rezervaciju/{id}")
    public String otkaziRezervaciju(@PathVariable Integer id,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        Korisnici korisnik = (Korisnici) session.getAttribute("user");
        if (korisnik == null) {
            return "redirect:/login";
        }

        Dogadjaji dogadjaj = dogadjajiRepo.findById(id).orElse(null);
        if (dogadjaj == null || !dogadjaj.getKorisnik().getId().equals(korisnik.getId())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Niste ovlašćeni za ovu akciju.");
            return "redirect:/korisnik/dashboard";
        }

        dogadjajiRepo.delete(dogadjaj);
        redirectAttributes.addFlashAttribute("successMessage", "Rezervacija je uspešno otkazana.");
        return "redirect:/korisnik/dashboard";
    }




    // Izmena profila
    @PostMapping("/izmeni")
    public String izmeniProfil(@ModelAttribute Korisnici noviPodaci, HttpSession session, RedirectAttributes redirectAttributes) {
        Korisnici korisnik = (Korisnici) session.getAttribute("user");
        if (korisnik == null) {
            return "redirect:/login";
        }

        // Update podataka
        korisnik.setIme(noviPodaci.getIme());
        korisnik.setPrezime(noviPodaci.getPrezime());

        if (noviPodaci.getLozinka() != null && !noviPodaci.getLozinka().isBlank()) {
            korisnik.setLozinka(passwordEncoder.encode(noviPodaci.getLozinka()));
        }

        korisnikRepo.save(korisnik);
        session.setAttribute("user", korisnik); // osveži korisnika u sesiji

        redirectAttributes.addFlashAttribute("successMessage", "Podaci su uspešno izmenjeni.");
        return "redirect:/korisnik/dashboard";
    }

    @PostMapping("/otkazi/{id}")
    public String otkaziPorudzbinu(@PathVariable Integer id, HttpSession session, RedirectAttributes redirectAttributes) {
        Korisnici korisnik = (Korisnici) session.getAttribute("user");
        if (korisnik == null) {
            return "redirect:/login";
        }

        Porudzbine p = porudzbinaRepo.findById(id).orElse(null);
        if (p == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Porudžbina ne postoji.");
            return "redirect:/korisnik/dashboard";
        }

        if (p.getKorisnikID() != null
                && p.getKorisnikID().getId().equals(korisnik.getId())
                && "Na čekanju".equals(p.getStatus())) {
            p.setStatus("Otkazano");
            porudzbinaRepo.save(p);
            redirectAttributes.addFlashAttribute("successMessage", "Porudžbina je uspešno otkazana.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Porudžbina nije mogla biti otkazana.");
        }

        return "redirect:/korisnik/dashboard";
    }

    @PostMapping("/potvrdi-primanje/{id}")
    public String potvrdiPrimanje(@PathVariable Integer id, HttpSession session, RedirectAttributes redirectAttributes) {
        Korisnici korisnik = (Korisnici) session.getAttribute("user");
        if (korisnik == null) {
            return "redirect:/login";
        }

        Porudzbine p = porudzbinaRepo.findById(id).orElse(null);
        if (p == null || !p.getKorisnikID().getId().equals(korisnik.getId())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Porudžbina nije pronađena ili nemate pravo pristupa.");
            return "redirect:/korisnik/dashboard";
        }

        if (!"Na čekanju".equals(p.getStatus())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Porudžbina nije u statusu koji može biti potvrđen.");
            return "redirect:/korisnik/dashboard";
        }

        p.setStatus("Isporučeno");
        porudzbinaRepo.save(p);
        redirectAttributes.addFlashAttribute("successMessage", "Hvala što ste potvrdili prijem porudžbine.");
        return "redirect:/korisnik/dashboard";
    }
}


