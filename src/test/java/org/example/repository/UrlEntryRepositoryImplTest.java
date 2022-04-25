package org.example.repository;

import org.example.domain.AppUser;
import org.example.domain.UrlEntry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Collections;
import java.util.List;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = {UrlEntryRepositoryImplTest.DatasourceContextInitializer.class})
@Testcontainers
@Import(UrlEntryRepositoryImpl.class)
class UrlEntryRepositoryImplTest {

    public static final String TOKEN_1 = "token";
    public static final String TOKEN_2 = "token-3";
    public static final AppUser APP_USER_1 = new AppUser(1, "user_1@mail.com", "pwd1", true);
    public static final AppUser APP_USER_2 = new AppUser(2, "user_2@mail.com", "pwd2", true);
    public static final UrlEntry URL_ENTRY_TO_SAVE = new UrlEntry(0, APP_USER_1, "new_token", "http://new-url.com");
    public static final String URL_1 = "http://test-url.com";
    public static final String URL_2 = "http://test-url-3.com";
    public static final int URL_ENTRY_ID_1 = 1;
    public static final int URL_ENTRY_ID_2 = 4;
    public static final UrlEntry URL_ENTRY_1 = new UrlEntry(URL_ENTRY_ID_1, APP_USER_1, TOKEN_1, URL_1);
    public static final UrlEntry URL_ENTRY_2 = new UrlEntry(URL_ENTRY_ID_2, APP_USER_2, TOKEN_2, URL_2);

    @Container
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:latest");

    static class DatasourceContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertyValues.of(
                            "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                            "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                            "spring.datasource.password=" + postgreSQLContainer.getPassword())
                    .applyTo(applicationContext.getEnvironment());
        }
    }

    @Autowired
    private UrlEntryRepository urlEntryRepository;

    @Test
    void findByToken() {
        urlEntryRepository.findByToken(TOKEN_1)
                .ifPresentOrElse(actual -> Assertions.assertEquals(URL_ENTRY_1, actual), Assertions::fail);
    }

    @Test
    void save() {
        UrlEntry savedEntry = urlEntryRepository.save(URL_ENTRY_TO_SAVE);
        urlEntryRepository.findById(savedEntry.getId())
                .ifPresentOrElse(urlEntry -> Assertions.assertEquals(savedEntry, urlEntry), Assertions::fail);
    }

    @Test
    void findById() {
        urlEntryRepository.findById(1)
                .ifPresentOrElse(urlEntry -> Assertions.assertEquals(URL_ENTRY_1, urlEntry), Assertions::fail);
    }

    @Test
    void findByUrl() {
        urlEntryRepository.findByUrl(URL_1)
                .ifPresentOrElse(urlEntry -> Assertions.assertEquals(URL_ENTRY_1, urlEntry), Assertions::fail);
    }

    @Test
    void findBaseUrlByToken() {
        urlEntryRepository.findBaseUrlByToken(TOKEN_1)
                .ifPresentOrElse(url -> Assertions.assertEquals(URL_1, url), Assertions::fail);
    }

    @Test
    void checkByBaseUrl() {
        urlEntryRepository.checkByBaseUrl(URL_1)
                .ifPresentOrElse(id -> Assertions.assertEquals(URL_ENTRY_ID_1, id), Assertions::fail);
    }

    @Test
    void findAllByPage() {
        List<UrlEntry> entryList = urlEntryRepository.findAll(0, 2, "id", true);
        Assertions.assertEquals(entryList.size(), 2);
        Assertions.assertEquals(URL_ENTRY_1, entryList.get(0));
        Assertions.assertEquals(1, entryList.get(0).getId());
        Assertions.assertEquals(2, entryList.get(1).getId());
    }

    @Test
    void findAllByPagePaginationTest() {
        List<UrlEntry> firstPage = urlEntryRepository.findAll(0, 2, "id", true);
        List<UrlEntry> secondPage = urlEntryRepository.findAll(1, 2, "id", true);
        Assertions.assertEquals(2, firstPage.size());
        Assertions.assertEquals(2, secondPage.size());
        Assertions.assertEquals(1, firstPage.get(0).getId());
        Assertions.assertEquals(2, firstPage.get(1).getId());
        Assertions.assertEquals(3, secondPage.get(0).getId());
        Assertions.assertEquals(4, secondPage.get(1).getId());
    }

    @Test
    void findAllByPageShouldThrowExceptionOnNegativeParameters() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> urlEntryRepository.findAll(-1, 2, "id", true));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> urlEntryRepository.findAll(0, -2, "id", true));
    }

    @Test
    void findAllByPageShouldThrowExceptionOnUnknownSortColumn() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> urlEntryRepository.findAll(0, 2, "zzz", true));
    }

    @Test
    void findAllByPageShouldAcceptSetOfSortColumn() {
        Assertions.assertDoesNotThrow(() -> urlEntryRepository.findAll(0, 2, "id", true));
        Assertions.assertDoesNotThrow(() -> urlEntryRepository.findAll(0, 2, "base_url", true));
        Assertions.assertDoesNotThrow(() -> urlEntryRepository.findAll(0, 2, "token", true));
        Assertions.assertDoesNotThrow(() -> urlEntryRepository.findAll(0, 2, "user_id", true));
    }

    @Test
    void findAllByPageSortTest() {
        List<UrlEntry> entryList = urlEntryRepository.findAll(0, 1, "id", false);
        Assertions.assertEquals(6, entryList.get(0).getId());
    }

    @Test
    void findAllByPageSortTestWithDifferentColumn() {
        List<UrlEntry> ascending = urlEntryRepository.findAll(0, 6, "base_url", true);
        List<UrlEntry> descending = urlEntryRepository.findAll(0, 6, "base_url", false);
        Collections.reverse(ascending);
        for (int i = 0; i < ascending.size(); i++) {
            Assertions.assertEquals(ascending.get(i), descending.get(i));
        }
    }

    @Test
    void findAllByUserByPage() {
        List<UrlEntry> entryList = urlEntryRepository.findAllByUser(0, 2, "id", true, APP_USER_2.getId());
        Assertions.assertEquals(2, entryList.size());
        Assertions.assertEquals(URL_ENTRY_2, entryList.get(0));
    }

    @Test
    void findAllByUserByPagePaginationTest() {
        List<UrlEntry> firstPage = urlEntryRepository.findAllByUser(0, 2, "id", true, APP_USER_2.getId());
        List<UrlEntry> secondPage = urlEntryRepository.findAllByUser(1, 2, "id", true, APP_USER_2.getId());
        Assertions.assertEquals(2, firstPage.size());
        Assertions.assertEquals(1, secondPage.size());
        Assertions.assertEquals(4, firstPage.get(0).getId());
        Assertions.assertEquals(5, firstPage.get(1).getId());
        Assertions.assertEquals(6, secondPage.get(0).getId());
    }

    @Test
    void findAllByUserByPageShouldThrowExceptionOnNegativeParameters() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> urlEntryRepository.findAllByUser(-1, 2, "id", true, APP_USER_1.getId()));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> urlEntryRepository.findAllByUser(0, -2, "id", true, APP_USER_1.getId()));
    }

    @Test
    void findAllByUserByPageShouldThrowExceptionOnUnknownSortColumn() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> urlEntryRepository.findAllByUser(0, 2, "zzz", true, APP_USER_1.getId()));
    }

    @Test
    void findAllByUserByPageShouldAcceptSetOfSortColumn() {
        Assertions.assertDoesNotThrow(() -> urlEntryRepository.findAllByUser(0, 2, "id", true, APP_USER_1.getId()));
        Assertions.assertDoesNotThrow(() -> urlEntryRepository.findAllByUser(0, 2, "base_url", true, APP_USER_1.getId()));
        Assertions.assertDoesNotThrow(() -> urlEntryRepository.findAllByUser(0, 2, "token", true, APP_USER_1.getId()));
        Assertions.assertDoesNotThrow(() -> urlEntryRepository.findAllByUser(0, 2, "user_id", true, APP_USER_1.getId()));
    }

    @Test
    void findAllByUserByPageSortTest() {
        List<UrlEntry> entryList = urlEntryRepository.findAllByUser(0, 1, "id", false, APP_USER_1.getId());
        Assertions.assertEquals(3, entryList.get(0).getId());
    }

    @Test
    void findAllByUserByPageSortTestWithDifferentColumn() {
        List<UrlEntry> ascending = urlEntryRepository.findAllByUser(0, 3, "base_url", true, APP_USER_2.getId());
        List<UrlEntry> descending = urlEntryRepository.findAllByUser(0, 3, "base_url", false, APP_USER_2.getId());
        Collections.reverse(ascending);
        for (int i = 0; i < ascending.size(); i++) {
            Assertions.assertEquals(ascending.get(i), descending.get(i));
        }
    }

}