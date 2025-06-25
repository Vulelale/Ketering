package com.ketering.controller;

import com.ketering.model.Dogadjaji;
import com.ketering.model.Korisnici;
import com.ketering.model.StavkeDogadjaja;
import com.ketering.repository.DogadjajiRepository;
import com.ketering.repository.JelovnikRepository;
import com.ketering.repository.KorisniciRepository;
import com.ketering.repository.StavkeDogadjajaRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/dogadjaji")
public class DogadjajController {

    @Autowired
    private DogadjajiRepository dogadjajiRepo;

    @Autowired
    private JelovnikRepository jelovnikRepo;

    @Autowired
    private StavkeDogadjajaRepository stavkeRepo;

    @Autowired
    private KorisniciRepository korisniciRepo;

    @GetMapping("")
    public String prikaziFormu(Model model, HttpSession session) {
        Korisnici korisnik = (Korisnici) session.getAttribute("user");
        if (korisnik == null) {
            session.setAttribute("poruka", "Morate biti ulogovani da biste rezervisali događaj.");
            return "redirect:/login";
        }

        model.addAttribute("dogadjaj", new Dogadjaji());
        model.addAttribute("korisnik", korisnik);
        model.addAttribute("jelovnik", jelovnikRepo.findAll());

        return "dogadjaji/rezervisi-dogadjaj";
    }



    @PostMapping("/snimi")
    public String snimiDogadjaj(@ModelAttribute Dogadjaji dogadjaj,
                                @RequestParam(value = "jela", required = false) List<Integer> jeloIds,
                                @RequestParam(value = "kolicine", required = false) List<Integer> kolicine,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        Korisnici korisnik = (Korisnici) session.getAttribute("user");
        if (korisnik == null) {
            session.setAttribute("poruka", "Morate biti ulogovani da biste izvršili ovu akciju.");
            return "redirect:/login";
        }

        try {
            dogadjaj.setKorisnik(korisnik);
            dogadjajiRepo.save(dogadjaj);

            if (jeloIds != null && kolicine != null) {
                for (int i = 0; i < jeloIds.size(); i++) {
                    StavkeDogadjaja stavka = new StavkeDogadjaja();
                    stavka.setDogadjaj(dogadjaj);
                    stavka.setJelovnik(jelovnikRepo.findById(jeloIds.get(i)).orElse(null));
                    stavka.setKolicina(kolicine.get(i));
                    stavkeRepo.save(stavka);
                }
            }

            redirectAttributes.addFlashAttribute("poruka", "Rezervacija uspešno kreirana!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("greska", "Greška prilikom kreiranja rezervacije.");
        }

        return "redirect:/dogadjaji";
    }


}

