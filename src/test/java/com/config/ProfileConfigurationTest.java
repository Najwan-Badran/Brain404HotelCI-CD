package com.config;

import com.Amenity.AmenityRepository;
import com.Booking.BookingRepository;
import com.Hotel.HotelRepository;
import com.Role.Role;
import com.Role.RoleRepository;
import com.Room.RoomRepository;
import com.RoomType.RoomTypeRepository;
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
class ProfileConfigurationTest {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomTypeRepository roomTypeRepository;

    @Autowired
    private AmenityRepository amenityRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void testEmptyProfileSimulation() {
        assertEquals(0, hotelRepository.count());
        assertEquals(0, roomRepository.count());
        assertEquals(0, roomTypeRepository.count());
        assertEquals(0, amenityRepository.count());
        assertEquals(0, bookingRepository.count());
    }

    @Test
    void testEmptyProfileWithRolesAndAdmin() {
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

        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@hotel.com");
        admin.setPassword(passwordEncoder.encode("Admin@1234"));
        admin.setEnabled(true);
        admin.setAccountLocked(false);
        admin.setFailedLoginAttempts(0);
        admin.getRoles().add(roleAdmin);
        admin.getRoles().add(roleUser);
        admin.getRoles().add(roleManager);
        userRepository.save(admin);

        assertEquals(3, roleRepository.count());
        assertEquals(1, userRepository.count());
        assertEquals(0, hotelRepository.count());
        assertEquals(0, roomRepository.count());
        assertEquals(0, bookingRepository.count());

        Optional<User> foundAdmin = userRepository.findByUsername("admin");
        assertTrue(foundAdmin.isPresent());
        assertEquals(3, foundAdmin.get().getRoles().size());
    }

    @Test
    void testSchemaCreation() {
        assertDoesNotThrow(() -> roleRepository.count());
        assertDoesNotThrow(() -> userRepository.count());
        assertDoesNotThrow(() -> hotelRepository.count());
        assertDoesNotThrow(() -> roomRepository.count());
        assertDoesNotThrow(() -> roomTypeRepository.count());
        assertDoesNotThrow(() -> amenityRepository.count());
        assertDoesNotThrow(() -> bookingRepository.count());
    }

    @Test
    void testRepositoriesAreInjected() {
        assertNotNull(roleRepository);
        assertNotNull(userRepository);
        assertNotNull(hotelRepository);
        assertNotNull(roomRepository);
        assertNotNull(roomTypeRepository);
        assertNotNull(amenityRepository);
        assertNotNull(bookingRepository);
        assertNotNull(passwordEncoder);
    }

    @Test
    void testPasswordEncoderWorks() {
        String rawPassword = "TestPassword@123";
        String encoded = passwordEncoder.encode(rawPassword);

        assertNotEquals(rawPassword, encoded);
        assertTrue(passwordEncoder.matches(rawPassword, encoded));
        assertFalse(passwordEncoder.matches("WrongPassword", encoded));
    }

    @Test
    void testRoleCreationAndRetrieval() {
        Role role = new Role();
        role.setName("ROLE_TEST");
        role.setDescription("Test role for profile testing");
        role.setActive(true);
        Role saved = roleRepository.save(role);

        assertNotNull(saved.getId());

        Optional<Role> found = roleRepository.findByName("ROLE_TEST");
        assertTrue(found.isPresent());
        assertEquals("Test role for profile testing", found.get().getDescription());
        assertTrue(found.get().isActive());
    }

    @Test
    void testUserCreationAndRetrieval() {
        User user = new User();
        user.setUsername("profiletest");
        user.setEmail("profiletest@test.com");
        user.setPassword(passwordEncoder.encode("Test@1234"));
        user.setEnabled(true);
        user.setAccountLocked(false);
        user.setFailedLoginAttempts(0);
        User saved = userRepository.save(user);

        assertNotNull(saved.getId());

        Optional<User> foundByUsername = userRepository.findByUsername("profiletest");
        assertTrue(foundByUsername.isPresent());

        Optional<User> foundByEmail = userRepository.findByEmail("profiletest@test.com");
        assertTrue(foundByEmail.isPresent());

        assertEquals(foundByUsername.get().getId(), foundByEmail.get().getId());
    }

    @Test
    void testExistsByMethods() {
        User user = new User();
        user.setUsername("existstest");
        user.setEmail("existstest@test.com");
        user.setPassword(passwordEncoder.encode("Test@1234"));
        user.setEnabled(true);
        userRepository.save(user);

        assertTrue(userRepository.existsByUsername("existstest"));
        assertTrue(userRepository.existsByEmail("existstest@test.com"));
        assertFalse(userRepository.existsByUsername("nonexistent"));
        assertFalse(userRepository.existsByEmail("nonexistent@test.com"));
    }

    @Test
    void testTransactionalBehavior() {
        Role role = new Role();
        role.setName("ROLE_TRANSACTIONAL");
        role.setDescription("Testing transactional behavior");
        roleRepository.save(role);

        Optional<Role> found = roleRepository.findByName("ROLE_TRANSACTIONAL");
        assertTrue(found.isPresent());

        found.get().setDescription("Updated description");
        roleRepository.save(found.get());

        Optional<Role> updated = roleRepository.findByName("ROLE_TRANSACTIONAL");
        assertTrue(updated.isPresent());
        assertEquals("Updated description", updated.get().getDescription());
    }
}
