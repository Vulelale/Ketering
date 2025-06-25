
    package com.ketering.controller;

    import com.ketering.model.Korisnici;
    import com.ketering.repository.KorisniciRepository;
    import jakarta.servlet.http.HttpSession;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.*;

    @Controller
    public class AuthController {

        @Autowired
        private KorisniciRepository korisniciRepository;

        private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        @GetMapping("/register")
        public String showRegisterForm(Model model) {
            model.addAttribute("korisnici", new Korisnici());
            return "auth/register";
        }

        @PostMapping("/register")
        public String processRegister(@ModelAttribute("korisnici") Korisnici korisnik, Model model, HttpSession session) {
            if (korisniciRepository.existsByEmail(korisnik.getEmail())) {
                model.addAttribute("error", "Email je već registrovan.");
                return "auth/register";
            }

            String encodedPassword = passwordEncoder.encode(korisnik.getLozinka());
            korisnik.setLozinka(encodedPassword);
            korisnik.setUloga("Klijent");
            korisniciRepository.save(korisnik);

            session.setAttribute("poruka", "Uspešno ste se registrovali! Prijavite se.");
            return "redirect:/login";
        }


        @GetMapping("/login")
        public String showLoginForm() {
            return "auth/login";
        }

        @PostMapping("/login")
        public String processLogin(@RequestParam String email,
                                   @RequestParam String lozinka,
                                   Model model,
                                   HttpSession session) {
            Korisnici korisnik = korisniciRepository.findByEmail(email);

            if (korisnik == null || !passwordEncoder.matches(lozinka, korisnik.getLozinka())) {
                model.addAttribute("error", "Pogrešan email ili lozinka.");
                return "auth/login";
            }


            session.setAttribute("user", korisnik);


            if ("Administrator".equals(korisnik.getUloga())) {
                return "redirect:/admin";
            } else {
                session.removeAttribute("poruka");

                return "redirect:/";
            }
        }

        @GetMapping("/logout")
        public String logout(HttpSession session) {
            session.invalidate();
            return "redirect:/";
        }


    }

