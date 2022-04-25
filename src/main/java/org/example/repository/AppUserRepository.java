package org.example.repository;

import org.example.domain.AppUser;

import java.util.Optional;

public interface AppUserRepository {
    Optional<AppUser> findByEmail(String email);
}
