package org.example.kundtjanst.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Kontrollerar huvudsidan.
 * Mappar till root-URL "/" och returnerar index
 */
@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "index";
    }
}
