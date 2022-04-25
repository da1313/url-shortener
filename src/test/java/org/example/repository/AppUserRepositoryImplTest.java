package org.example.repository;

import org.example.domain.AppUser;
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

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = {AppUserRepositoryImplTest.DatasourceContextInitializer.class})
@Testcontainers
@Import(AppUserRepositoryImpl.class)
class AppUserRepositoryImplTest {

    public static final String USER_1_MAIL = "user_1@mail.com";
    public static final String PWD_1 = "pwd1";
    public static final int ID_1 = 1;
    public static final boolean IS_ACTIVE_1 = true;
    public static final AppUser APP_USER_1 = new AppUser(ID_1, USER_1_MAIL, PWD_1, IS_ACTIVE_1);

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

    //insert into app_user(email, password, is_active) values('user_1@mail.com', 'pwd1', true);

    @Autowired
    private AppUserRepository appUserRepository;

    @Test
    void findByEmail() {
        appUserRepository.findByEmail(USER_1_MAIL)
                .ifPresentOrElse(appUser ->  Assertions.assertEquals(APP_USER_1, appUser), Assertions::fail);
    }
}