package org.example.web;

import org.example.api.NamedUrlHolder;
import org.example.api.UrlHolder;
import org.example.api.UserUrlListRequest;
import org.example.service.StandardUrlService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@RestController
public class UrlController {

    private final StandardUrlService shortenerService;

    public UrlController(StandardUrlService shortenerService) {
        this.shortenerService = shortenerService;
    }

    @PostMapping(
            value = "/api/v1/url",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public UrlHolder getStandardUrl(@RequestBody @Valid UrlHolder url, HttpServletRequest request){
        return shortenerService.generateStandardShortUrl(url);
    }

    @PostMapping(
            value = "/api/v1/url/named",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public UrlHolder getNamedUrl(@RequestBody @Valid NamedUrlHolder namedUrlHolder){
        return shortenerService.generateNamedUrl(namedUrlHolder);
    }

//    @GetMapping("/api/v1/url/{id}")
//    public Object getAllUrls(@PathVariable("id") long id){
//        return shortenerService.getUserUrl(request);
//    }

    @GetMapping("/api/v1/url/user")
    public Object getAllUrls(@Valid UserUrlListRequest request){
        return shortenerService.getUserUrl(request);
    }

    public Object deleteUrl(){
        return  null;
    }

    public Object updateUrl(){
        return null;
    }

}
