package org.example.service;

import org.example.api.UrlHolder;

public interface NamedUrlGenerationService {
    UrlHolder generateNamedShortUrl(String url, String userUrl);
}
