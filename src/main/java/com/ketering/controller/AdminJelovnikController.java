package com.ketering.controller;

import com.ketering.model.Jelovnik;
import com.ketering.repository.JelovnikRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/admin/jelovnik")
public class AdminJelovnikController {

    @Autowired
    private JelovnikRepository jelovnikRepo;

    @GetMapping("/novi")
    public String novoJelo(Model model) {
        model.addAttribute("jelovnik", new Jelovnik());
        return "admin/jelovnik-forma";
    }

    @PostMapping("/snimi")
    public String snimiJelo(@ModelAttribute Jelovnik jelovnik,
                            @RequestParam("imageFile") MultipartFile imageFile) {

        if (!imageFile.isEmpty()) {
            try {
                String folder = "src/main/resources/static/images/";
                String fileName = imageFile.getOriginalFilename();
                Path path = Paths.get(folder + fileName);
                Files.write(path, imageFile.getBytes());

                jelovnik.setSlika("/images/" + fileName); // čuva se putanja ka slici
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        jelovnikRepo.save(jelovnik);
        return "redirect:/admin";
    }

    @GetMapping("/izmeni/{id}")
    public String izmeniJelo(@PathVariable Integer id, Model model) {
        model.addAttribute("jelovnik", jelovnikRepo.findById(id).orElseThrow());
        return "admin/jelovnik-forma";
    }

    @GetMapping("/obrisi/{id}")
    public String obrisi(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            jelovnikRepo.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Jelo je uspešno obrisano.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Nije moguće obrisati jelo jer je aktivno korišćeno u događajima.");
        }

        return "redirect:/panel";
    }

}

