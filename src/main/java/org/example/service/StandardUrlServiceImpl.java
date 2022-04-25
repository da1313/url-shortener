package org.example.service;

import org.example.api.NamedUrlHolder;
import org.example.api.UrlHolder;
import org.example.api.UserUrlListRequest;
import org.example.configuration.ApplicationProperties;
import org.example.domain.UrlEntry;
import org.example.exceptions.RequestValidationException;
import org.example.exceptions.TokenGenerationException;
import org.example.exceptions.UrlNotFoundException;
import org.example.repository.UrlEntryRepository;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class StandardUrlServiceImpl implements StandardUrlService {

    private final UrlEntryRepository urlRepository;
    private final TokenGenerator tokenGenerator;
    private final URLPingService urlPingService;
    private final MessageSourceAccessor messageSourceAccessor;
    private final ApplicationProperties applicationProperties;

    public StandardUrlServiceImpl(UrlEntryRepository urlRepository,
                                  TokenGenerator urlTransformer,
                                  URLPingService urlPingService,
                                  MessageSourceAccessor messageSourceAccessor,
                                  ApplicationProperties applicationProperties) {
        this.urlRepository = urlRepository;
        this.tokenGenerator = urlTransformer;
        this.urlPingService = urlPingService;
        this.messageSourceAccessor = messageSourceAccessor;
        this.applicationProperties = applicationProperties;
    }

    @Override
    public UrlHolder generateStandardShortUrl(UrlHolder urlHolder) {
        checkIfURLBound(urlHolder.getUrl());
        directValidateUrl(urlHolder.getUrl());
        String token = generateToken();
        //todo extract a user from context
        urlRepository.save(new UrlEntry(0, null, token, urlHolder.getUrl()));
        return new UrlHolder(applicationProperties.getHost() + "/" + token);
    }

    @Override
    public String getBaseUrl(String token) {
        return urlRepository.findBaseUrlByToken(token)
                .orElseThrow(() -> new UrlNotFoundException(String.format("Can not find url by token: %s", token)));
    }

    @Override
    public UrlHolder generateNamedUrl(NamedUrlHolder namedUrlHolder) {
        checkIfURLBound(namedUrlHolder.getUrl());
        directValidateUrl(namedUrlHolder.getUrl());
        Optional<String> optionalUrlEntry = urlRepository.findBaseUrlByToken(namedUrlHolder.getName());
        if (optionalUrlEntry.isPresent()){
            throw new RequestValidationException(Map.of("name",
                    messageSourceAccessor.getMessage("validation.named-url.already-bound")));
        }
        //todo extract a user from context
        urlRepository.save(new UrlEntry(0, null, namedUrlHolder.getName(), namedUrlHolder.getUrl()));
        return new UrlHolder(applicationProperties.getHost() + "/" + namedUrlHolder.getName());
    }

    @Override
    public List<?> getUserUrl(UserUrlListRequest request) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(principal);
        return urlRepository.findAllByUser(request.getPageNumber(), request.getPageSize(), "id", true, 1);
    }

    private void checkIfURLBound(String url) {
        Optional<Long> optionalUrlEntry = urlRepository.checkByBaseUrl(url);
        if (optionalUrlEntry.isPresent()){
            throw new RequestValidationException(Map.of("url", "{validation.url.already-bound}"));
        }
    }

    private void directValidateUrl(String url) {
        if (applicationProperties.isDirectValidation()){
            if (!urlPingService.pingURL(url, applicationProperties.getTimeout())){
                throw new RequestValidationException(Map.of("url",
                        messageSourceAccessor.getMessage("validation.url.unable-connect")));
            }
        }
    }

    private String generateToken(){
        int count = 0;
        String token;
        do{
            if (count == applicationProperties.getMaxCollisionCount()){
                throw new TokenGenerationException("Maximum number of collisions reached while token generation");
            }
            token = tokenGenerator.generate();
            count++;
        } while (urlRepository.findByToken(token).isPresent());
        return token;
    }

}
