package com.ketering.controller;

import com.ketering.repository.JelovnikRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/jelovnik")
public class JelovnikController {

    @Autowired
    private JelovnikRepository jelovnikRepo;

    @GetMapping
    public String sviJelovnici(Model model) {
        model.addAttribute("jela", jelovnikRepo.findAll());
        return "jelovnik/pregled";
    }

    @GetMapping("/{id}")
    public String prikazJela(@PathVariable Integer id, Model model) {
        model.addAttribute("jelo", jelovnikRepo.findById(id).orElseThrow());
        return "jelovnik/detalji";
    }
}

