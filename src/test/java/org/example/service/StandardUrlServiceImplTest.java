package org.example.service;

import org.example.api.NamedUrlHolder;
import org.example.api.UrlHolder;
import org.example.configuration.ApplicationProperties;
import org.example.domain.UrlEntry;
import org.example.exceptions.RequestValidationException;
import org.example.exceptions.TokenGenerationException;
import org.example.repository.UrlEntryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.MessageSourceAccessor;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class StandardUrlServiceImplTest {

    public static final String USER_URL = "url";
//    public static final UrlEntry PERSISTED_ENTRY = new UrlEntry(1, "persistedToken", USER_URL);
    public static final String TOKEN = "token";
    public static final String HOST = "http://localhost:8080";
    public static final int TIMEOUT = 3000;
    public static final int MAX_COLLISION_COUNT = 2;
    public static final String USER_DEFINED_TOKEN = "user-defined-token";
    @Mock
    private UrlEntryRepository urlEntryRepository;
    @Mock
    private TokenGenerator tokenGenerator;
    @Mock
    private URLPingService urlPingService;
    @Mock
    private MessageSourceAccessor messageSourceAccessor;
    @Mock
    private ApplicationProperties applicationProperties;

//    @Test
//    void shouldGenerateMockUrlByCombiningOriginAndToken() {
//        ArgumentCaptor<UrlEntry> urlEntryArgumentCaptor = ArgumentCaptor.forClass(UrlEntry.class);
//        StandardUrlServiceImpl standardUrlService
//                = new StandardUrlServiceImpl(urlEntryRepository, tokenGenerator, urlPingService,
//                messageSourceAccessor, applicationProperties);
//        Mockito.when(urlEntryRepository.findByUrl(USER_URL)).thenReturn(Optional.empty());
//        Mockito.when(applicationProperties.isDirectValidation()).thenReturn(true);
//        Mockito.when(applicationProperties.getTimeout()).thenReturn(TIMEOUT);
//        Mockito.when(applicationProperties.getMaxCollisionCount()).thenReturn(MAX_COLLISION_COUNT);
//        Mockito.when(applicationProperties.getHost()).thenReturn(HOST);
//        Mockito.when(urlPingService.pingURL(USER_URL, TIMEOUT)).thenReturn(true);
//        Mockito.when(tokenGenerator.generate()).thenReturn(TOKEN);
//        Mockito.when(urlEntryRepository.findByToken(TOKEN)).thenReturn(Optional.empty());
//        UrlHolder actual = standardUrlService.generateStandardShortUrl(new UrlHolder(USER_URL));
//        Mockito.verify(urlEntryRepository).save(urlEntryArgumentCaptor.capture());
//        UrlEntry urlEntryToSave = urlEntryArgumentCaptor.getValue();
//        UrlHolder expected = new UrlHolder(HOST + "/" + TOKEN);
//        Assertions.assertEquals(new UrlEntry(0, TOKEN, USER_URL) ,urlEntryToSave);
//        Assertions.assertEquals(expected, actual);
//    }
//
//    @Test
//    void shouldThrowExceptionWhenDirectValidationFailsInGenerateStandardShortUrl() {
//        StandardUrlServiceImpl standardUrlService
//                = new StandardUrlServiceImpl(urlEntryRepository, tokenGenerator, urlPingService,
//                messageSourceAccessor, applicationProperties);
//        Mockito.when(urlEntryRepository.findByUrl(USER_URL)).thenReturn(Optional.empty());
//        Mockito.when(applicationProperties.isDirectValidation()).thenReturn(true);
//        Mockito.when(applicationProperties.getTimeout()).thenReturn(TIMEOUT);
//        Mockito.when(urlPingService.pingURL(USER_URL, TIMEOUT)).thenReturn(false);
//        Mockito.when(messageSourceAccessor.getMessage("validation.url.unable-connect")).thenReturn("message");
//        Assertions.assertThrows(RequestValidationException.class,
//                () -> standardUrlService.generateStandardShortUrl(new UrlHolder(USER_URL)));
//    }
//
//    @Test
//    void shouldThrowExceptionWhenURLisAlreadyBoundToSomeTokenInGenerateStandardShortUrl() {
//        StandardUrlServiceImpl standardUrlService
//                = new StandardUrlServiceImpl(urlEntryRepository, tokenGenerator, urlPingService,
//                messageSourceAccessor, applicationProperties);
//        Mockito.when(urlEntryRepository.findByUrl(USER_URL)).thenReturn(Optional.of(PERSISTED_ENTRY));
//        Assertions.assertThrows(RequestValidationException.class,
//                () -> standardUrlService.generateStandardShortUrl(new UrlHolder(USER_URL)));
//    }
//
//    @Test
//    void shouldThrowExceptionWhenUnableToCreateUniqueToken() {
//        ArgumentCaptor<UrlEntry> urlEntryArgumentCaptor = ArgumentCaptor.forClass(UrlEntry.class);
//        StandardUrlServiceImpl standardUrlService
//                = new StandardUrlServiceImpl(urlEntryRepository, tokenGenerator, urlPingService,
//                messageSourceAccessor, applicationProperties);
//        Mockito.when(urlEntryRepository.findByUrl(USER_URL)).thenReturn(Optional.empty());
//        Mockito.when(applicationProperties.isDirectValidation()).thenReturn(true);
//        Mockito.when(applicationProperties.getTimeout()).thenReturn(TIMEOUT);
//        Mockito.when(applicationProperties.getMaxCollisionCount()).thenReturn(MAX_COLLISION_COUNT);
//        Mockito.when(urlPingService.pingURL(USER_URL, TIMEOUT)).thenReturn(true);
//        Mockito.when(tokenGenerator.generate()).thenReturn(TOKEN);
//        Mockito.when(urlEntryRepository.findByToken(TOKEN)).thenReturn(Optional.of(PERSISTED_ENTRY));
//        Assertions.assertThrows(TokenGenerationException.class,
//                () -> standardUrlService.generateStandardShortUrl(new UrlHolder(USER_URL)));
//    }
//
//    @Test
//    void shouldRetrySpecifiedTimesToGenerateTokenOnCollision() {
//        ArgumentCaptor<UrlEntry> urlEntryArgumentCaptor = ArgumentCaptor.forClass(UrlEntry.class);
//        StandardUrlServiceImpl standardUrlService
//                = new StandardUrlServiceImpl(urlEntryRepository, tokenGenerator, urlPingService,
//                messageSourceAccessor, applicationProperties);
//        Mockito.when(urlEntryRepository.findByUrl(USER_URL)).thenReturn(Optional.empty());
//        Mockito.when(applicationProperties.isDirectValidation()).thenReturn(true);
//        Mockito.when(applicationProperties.getTimeout()).thenReturn(TIMEOUT);
//        Mockito.when(applicationProperties.getMaxCollisionCount()).thenReturn(MAX_COLLISION_COUNT);
//        Mockito.when(urlPingService.pingURL(USER_URL, TIMEOUT)).thenReturn(true);
//        Mockito.when(tokenGenerator.generate()).thenReturn(TOKEN);
//        Mockito.when(urlEntryRepository.findByToken(TOKEN)).thenReturn(Optional.of(PERSISTED_ENTRY));
//        try {
//            standardUrlService.generateStandardShortUrl(new UrlHolder(USER_URL));
//        } catch (TokenGenerationException exception){
//
//        }
//        Mockito.verify(tokenGenerator, Mockito.times(MAX_COLLISION_COUNT)).generate();
//    }
//
//    @Test
//    void shouldReturnBaseUrlCorrespondingToProvidedToken() {
//        StandardUrlServiceImpl standardUrlService
//                = new StandardUrlServiceImpl(urlEntryRepository, tokenGenerator, urlPingService,
//                messageSourceAccessor, applicationProperties);
//        Mockito.when(urlEntryRepository.findByToken(TOKEN)).thenReturn(Optional.of(PERSISTED_ENTRY));
//        String actual = standardUrlService.getBaseUrl(TOKEN);
//        Assertions.assertEquals(PERSISTED_ENTRY.getBaseUrl(), actual);
//    }
//
//    @Test
//    void shouldGenerateUrlBasedOnProvidedByUserName() {
//        ArgumentCaptor<UrlEntry> urlEntryArgumentCaptor = ArgumentCaptor.forClass(UrlEntry.class);
//        StandardUrlServiceImpl standardUrlService
//                = new StandardUrlServiceImpl(urlEntryRepository, tokenGenerator, urlPingService,
//                messageSourceAccessor, applicationProperties);
//        Mockito.when(urlEntryRepository.findByUrl(USER_URL)).thenReturn(Optional.empty());
//        Mockito.when(applicationProperties.isDirectValidation()).thenReturn(true);
//        Mockito.when(applicationProperties.getTimeout()).thenReturn(TIMEOUT);
//        Mockito.when(applicationProperties.getHost()).thenReturn(HOST);
//        Mockito.when(urlPingService.pingURL(USER_URL, TIMEOUT)).thenReturn(true);
//        Mockito.when(urlEntryRepository.findByToken(USER_DEFINED_TOKEN)).thenReturn(Optional.empty());
//        UrlHolder actual = standardUrlService.generateNamedUrl(new NamedUrlHolder(USER_URL, USER_DEFINED_TOKEN));
//        Mockito.verify(urlEntryRepository).save(urlEntryArgumentCaptor.capture());
//        UrlEntry urlEntryToSave = urlEntryArgumentCaptor.getValue();
//        UrlHolder expected = new UrlHolder(HOST + "/" + USER_DEFINED_TOKEN);
//        Assertions.assertEquals(new UrlEntry(0, USER_DEFINED_TOKEN, USER_URL) ,urlEntryToSave);
//        Assertions.assertEquals(expected, actual);
//    }
//
//    @Test
//    void shouldThrowExceptionWhenDirectValidationFailsInGenerateNamedUrl() {
//        StandardUrlServiceImpl standardUrlService
//                = new StandardUrlServiceImpl(urlEntryRepository, tokenGenerator, urlPingService,
//                messageSourceAccessor, applicationProperties);
//        Mockito.when(urlEntryRepository.findByUrl(USER_URL)).thenReturn(Optional.empty());
//        Mockito.when(applicationProperties.isDirectValidation()).thenReturn(true);
//        Mockito.when(applicationProperties.getTimeout()).thenReturn(TIMEOUT);
//        Mockito.when(urlPingService.pingURL(USER_URL, TIMEOUT)).thenReturn(false);
//        Mockito.when(messageSourceAccessor.getMessage("validation.url.unable-connect")).thenReturn("message");
//        Assertions.assertThrows(RequestValidationException.class,
//                () -> standardUrlService.generateNamedUrl(new NamedUrlHolder(USER_URL, USER_DEFINED_TOKEN)));
//    }
//
//    @Test
//    void shouldThrowExceptionWhenURLisAlreadyBoundToSomeTokenInGenerateNamedUrl() {
//        StandardUrlServiceImpl standardUrlService
//                = new StandardUrlServiceImpl(urlEntryRepository, tokenGenerator, urlPingService,
//                messageSourceAccessor, applicationProperties);
//        Mockito.when(urlEntryRepository.findByUrl(USER_URL)).thenReturn(Optional.of(PERSISTED_ENTRY));
//        Assertions.assertThrows(RequestValidationException.class,
//                () -> standardUrlService.generateNamedUrl(new NamedUrlHolder(USER_URL, USER_DEFINED_TOKEN)));
//    }
}