package org.example.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UrlEntry {
    private long id;
    private AppUser appUser;
    private String token;
    private String baseUrl;
}