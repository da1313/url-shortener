package org.example.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AppUser {
    private long id;
    private String email;
    private String password;
    private boolean isActive;
}
