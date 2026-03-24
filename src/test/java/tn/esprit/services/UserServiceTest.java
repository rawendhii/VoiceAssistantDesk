package tn.esprit.services;

import org.junit.jupiter.api.*;
import tn.esprit.entities.User;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceTest {

    static UserServices service;
    static int idUserTest;

    @BeforeAll
    static void setup() {
        service = new UserServices();
    }

    @Test
    @Order(1)
    void testAjouterUser() throws SQLException {
        User u = new User("Test User", "test.user@mail.com", "12345678", 2);

        idUserTest = service.ajouter(u);
        assertTrue(idUserTest > 0, "Generated id should be > 0");

        List<User> users = service.afficher();
        assertTrue(users.stream().anyMatch(x -> x.getId() == idUserTest
                && x.getEmail().equals("test.user@mail.com")));
    }

    @Test
    @Order(2)
    void testModifierUser() throws SQLException {
        assertTrue(idUserTest > 0, "idUserTest is not set. testAjouterUser must run first.");

        boolean ok = service.modifierInfos(idUserTest, "User Modified", "test.user.modified@mail.com");
        assertTrue(ok, "Update should return true (1 row updated).");

        List<User> users = service.afficher();
        assertTrue(users.stream().anyMatch(x -> x.getId() == idUserTest
                && x.getEmail().equals("test.user.modified@mail.com")
                && x.getFullName().equals("User Modified")));
    }

    @Test
    @Order(3)
    void testSupprimerUser() throws SQLException {
        assertTrue(idUserTest > 0, "idUserTest is not set.");

        service.supprimer(idUserTest);

        List<User> users = service.afficher();
        assertFalse(users.stream().anyMatch(x -> x.getId() == idUserTest));
    }

    @AfterAll
    static void cleanAll() throws SQLException {
        if (idUserTest > 0) {
            try {
                service.supprimer(idUserTest);
            } catch (SQLException ignored) {}
        }
    }
}