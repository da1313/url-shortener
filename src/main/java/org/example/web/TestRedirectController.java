package org.example.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestRedirectController {

    @GetMapping("/abc/r")
    public String redirect(){
        return "redirect:/abc/d";
    }

}
