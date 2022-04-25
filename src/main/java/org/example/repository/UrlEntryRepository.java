package org.example.repository;

import org.example.domain.AppUser;
import org.example.domain.UrlEntry;

import java.util.List;
import java.util.Optional;

public interface UrlEntryRepository {
    @Deprecated
    Optional<UrlEntry> findByToken(String token);
    Optional<String> findBaseUrlByToken(String token);
    UrlEntry save(UrlEntry urlEntry);
    @Deprecated
    Optional<UrlEntry> findByUrl(String url);
    Optional<Long> checkByBaseUrl(String baseUrl);
    Optional<UrlEntry> findById(long id);
    List<UrlEntry> findAll(int pageNumber, int pageSize, String sort, boolean direction);
    List<UrlEntry> findAllByUser(int pageNumber, int pageSize, String sort, boolean direction, long userId);
    boolean deleteById(long id);
    UrlEntry update(UrlEntry urlEntry);
}
