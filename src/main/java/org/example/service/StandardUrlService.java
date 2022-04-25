package org.example.service;

import org.example.api.NamedUrlHolder;
import org.example.api.UrlHolder;
import org.example.api.UserUrlListRequest;

import java.util.List;
import java.util.Optional;

public interface StandardUrlService {

    UrlHolder generateStandardShortUrl(UrlHolder urlHolder);

    String getBaseUrl(String token);

    UrlHolder generateNamedUrl(NamedUrlHolder namedUrlHolder);

    List<?> getUserUrl(UserUrlListRequest request);
}
