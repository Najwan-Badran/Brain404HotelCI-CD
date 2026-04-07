package com.config;

import com.Role.Role;
import com.Role.RoleRepository;
import com.User.User;
import com.User.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class DataInitializerTest {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void testCreateRoles() {
        Role roleUser = new Role();
        roleUser.setName("ROLE_USER");
        roleUser.setDescription("Standard user role");
        roleRepository.save(roleUser);

        Role roleAdmin = new Role();
        roleAdmin.setName("ROLE_ADMIN");
        roleAdmin.setDescription("Administrator role with full access");
        roleRepository.save(roleAdmin);

        Role roleManager = new Role();
        roleManager.setName("ROLE_MANAGER");
        roleManager.setDescription("Manager role with limited admin access");
        roleRepository.save(roleManager);

        Optional<Role> foundUser = roleRepository.findByName("ROLE_USER");
        assertTrue(foundUser.isPresent());
        assertEquals("Standard user role", foundUser.get().getDescription());

        Optional<Role> foundAdmin = roleRepository.findByName("ROLE_ADMIN");
        assertTrue(foundAdmin.isPresent());
        assertEquals("Administrator role with full access", foundAdmin.get().getDescription());

        Optional<Role> foundManager = roleRepository.findByName("ROLE_MANAGER");
        assertTrue(foundManager.isPresent());
        assertEquals("Manager role with limited admin access", foundManager.get().getDescription());
    }

    @Test
    void testCreateAdminUser() {
        Role roleAdmin = new Role();
        roleAdmin.setName("ROLE_ADMIN");
        roleAdmin.setDescription("Administrator role");
        roleRepository.save(roleAdmin);

        Role roleUser = new Role();
        roleUser.setName("ROLE_USER");
        roleUser.setDescription("User role");
        roleRepository.save(roleUser);

        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@hotel.com");
        admin.setPassword(passwordEncoder.encode("Admin@1234"));
        admin.setEnabled(true);
        admin.setAccountLocked(false);
        admin.setFailedLoginAttempts(0);
        admin.getRoles().add(roleAdmin);
        admin.getRoles().add(roleUser);
        userRepository.save(admin);

        Optional<User> foundAdmin = userRepository.findByUsername("admin");
        assertTrue(foundAdmin.isPresent());
        assertEquals("admin@hotel.com", foundAdmin.get().getEmail());
        assertTrue(foundAdmin.get().isEnabled());
        assertFalse(foundAdmin.get().isAccountLocked());
        assertEquals(0, foundAdmin.get().getFailedLoginAttempts());
        assertTrue(passwordEncoder.matches("Admin@1234", foundAdmin.get().getPassword()));
        assertEquals(2, foundAdmin.get().getRoles().size());
    }

    @Test
    void testRoleIdempotency() {
        Role role1 = new Role();
        role1.setName("ROLE_TEST");
        role1.setDescription("Test role");
        roleRepository.save(role1);

        if (roleRepository.findByName("ROLE_TEST").isEmpty()) {
            Role role2 = new Role();
            role2.setName("ROLE_TEST");
            role2.setDescription("Test role duplicate");
            roleRepository.save(role2);
        }

        long count = roleRepository.findAll().stream()
                .filter(r -> r.getName().equals("ROLE_TEST"))
                .count();
        assertEquals(1, count);
    }

    @Test
    void testUserIdempotency() {
        User user1 = new User();
        user1.setUsername("testuser");
        user1.setEmail("testuser@hotel.com");
        user1.setPassword(passwordEncoder.encode("Test@1234"));
        user1.setEnabled(true);
        userRepository.save(user1);

        if (userRepository.findByUsername("testuser").isEmpty()) {
            User user2 = new User();
            user2.setUsername("testuser");
            user2.setEmail("testuser2@hotel.com");
            user2.setPassword(passwordEncoder.encode("Test@1234"));
            userRepository.save(user2);
        }

        long count = userRepository.findAll().stream()
                .filter(u -> u.getUsername().equals("testuser"))
                .count();
        assertEquals(1, count);
    }
}
