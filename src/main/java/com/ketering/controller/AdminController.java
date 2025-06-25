package com.ketering.controller;

import com.ketering.model.*;
import com.ketering.repository.DogadjajiRepository;
import com.ketering.repository.KorisniciRepository;
import com.ketering.repository.JelovnikRepository;
import com.ketering.repository.PorudzbinaRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AdminController {

    @Autowired
    private KorisniciRepository korisnikRepository;

    @Autowired
    private JelovnikRepository jelovnikRepository;

    @Autowired
    private PorudzbinaRepository porudzbinaRepository;

    @Autowired
    private DogadjajiRepository dogadjajiRepository;


    @GetMapping("/admin")
    public String adminPanelRedirect(HttpSession session) {
        Korisnici korisnik = (Korisnici) session.getAttribute("user");
        if (korisnik == null || !"Administrator".equals(korisnik.getUloga())) {
            return "redirect:/login";
        }
        return "redirect:/panel";
    }

    @GetMapping("/panel")
    public String adminPanel(
            @RequestParam(defaultValue = "0") int jPage,
            @RequestParam(defaultValue = "0") int pPage,
            @RequestParam(required = false) String tip,
            @RequestParam(required = false) String status,
            Model model) {

        Pageable jeloPageable = PageRequest.of(jPage, 10);
        Pageable porudzbinePageable = PageRequest.of(pPage, 10);

        Page<Jelovnik> jelaPage;

        if (tip != null && !tip.isEmpty()) {
            try {
                Tip enumTip = Tip.valueOf(tip); // ← Konvertuje String u enum
                jelaPage = jelovnikRepository.findByTip(enumTip, jeloPageable);
            } catch (IllegalArgumentException e) {
                jelaPage = jelovnikRepository.findAll(jeloPageable); // fallback
            }
        } else {
            jelaPage = jelovnikRepository.findAll(jeloPageable);
        }


        Page<Porudzbine> porudzbinePage = (status != null && !status.isEmpty())
                ? porudzbinaRepository.findByStatus(status, porudzbinePageable)
                : porudzbinaRepository.findAll(porudzbinePageable);

        List<Dogadjaji> dogadjaji = dogadjajiRepository.findAllByOrderByDatumAsc();
        Map<Integer, BigDecimal> ukupneCeneDogadjaja = new HashMap<>();

        for (Dogadjaji d : dogadjaji) {
            BigDecimal ukupno = BigDecimal.ZERO;
            if (d.getStavke() != null) {
                for (StavkeDogadjaja stavka : d.getStavke()) {
                    BigDecimal kolicina = BigDecimal.valueOf(stavka.getKolicina());
                    BigDecimal cena = stavka.getJelovnik().getCena();
                    ukupno = ukupno.add(kolicina.multiply(cena));
                }
            }
            ukupneCeneDogadjaja.put(d.getId(), ukupno);
        }

        model.addAttribute("ukupneCeneDogadjaja", ukupneCeneDogadjaja);


        model.addAttribute("jelaPage", jelaPage);
        model.addAttribute("porudzbinePage", porudzbinePage);
        model.addAttribute("tipFilter", tip);
        model.addAttribute("statusFilter", status);
        model.addAttribute("korisnici", korisnikRepository.findAll());
        model.addAttribute("dogadjaji", dogadjaji);

        return "admin/panel";
    }

    @PostMapping("/admin/porudzbine/status")
    public String promeniStatusPorudzbine(
            @RequestParam("porudzbinaId") int porudzbinaId,
            @RequestParam("noviStatus") String noviStatus) {

        porudzbinaRepository.findById(porudzbinaId).ifPresent(porudzbina -> {
            porudzbina.setStatus(noviStatus);
            porudzbinaRepository.save(porudzbina);
        });

        return "redirect:/panel";
    }
}

