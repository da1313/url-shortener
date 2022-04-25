package org.example.web;

import org.example.service.StandardUrlService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class UrlRedirectController {

    private final StandardUrlService standardUrlService;

    public UrlRedirectController(StandardUrlService standardUrlService) {
        this.standardUrlService = standardUrlService;
    }

    @GetMapping("/{token}")
    private String redirect(@PathVariable(value = "token") String token){
        String baseUrl = standardUrlService.getBaseUrl(token);
        return "redirect:" + baseUrl;
    }

}
