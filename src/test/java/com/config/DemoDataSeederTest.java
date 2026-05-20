package com.config;

import com.Address.Address;
import com.Address.AddressRepository;
import com.Address.AddressType;
import com.Amenity.Amenity;
import com.Amenity.AmenityRepository;
import com.Booking.Booking;
import com.Booking.BookingRepository;
import com.Booking.BookingStatus;
import com.BookingGuest.BookingGuest;
import com.BookingGuest.BookingGuestRepository;
import com.Hotel.Hotel;
import com.Hotel.HotelRepository;
import com.Payment.Payment;
import com.Payment.PaymentMethod;
import com.Payment.PaymentRepository;
import com.Payment.PaymentStatus;
import com.Review.Review;
import com.Review.ReviewRepository;
import com.Role.Role;
import com.Role.RoleRepository;
import com.Room.Room;
import com.Room.RoomRepository;
import com.RoomType.RoomType;
import com.RoomType.RoomTypeRepository;
import com.User.User;
import com.User.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class DemoDataSeederTest {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AmenityRepository amenityRepository;

    @Autowired
    private RoomTypeRepository roomTypeRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingGuestRepository bookingGuestRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Role userRole;

    @BeforeEach
    void setUp() {
        userRole = new Role();
        userRole.setName("ROLE_USER");
        userRole.setDescription("Standard user role");
        userRole = roleRepository.save(userRole);
    }

    @Test
    void testSeedHotelAddress() {
        Address address = new Address();
        address.setAddressType(AddressType.HOTEL);
        address.setStreet("123 Fifth Avenue");
        address.setStreet2("Floor 10");
        address.setCity("New York");
        address.setState("NY");
        address.setCountry("USA");
        address.setPostalCode("10001");
        address.setLatitude(40.7484);
        address.setLongitude(-73.9967);
        address.setActive(true);
        addressRepository.save(address);

        Page<Address> addresses = addressRepository.findByAddressType(AddressType.HOTEL, Pageable.unpaged());
        assertEquals(1, addresses.getTotalElements());
        assertEquals("123 Fifth Avenue", addresses.getContent().get(0).getStreet());
        assertEquals("New York", addresses.getContent().get(0).getCity());
        assertEquals("USA", addresses.getContent().get(0).getCountry());
        assertNotNull(addresses.getContent().get(0).getLatitude());
        assertNotNull(addresses.getContent().get(0).getLongitude());
        assertTrue(addresses.getContent().get(0).isActive());
    }

    @Test
    void testSeedUserAddress() {
        Address address = new Address();
        address.setAddressType(AddressType.USER_BILLING);
        address.setStreet("100 Main Street");
        address.setStreet2("Apt 4B");
        address.setCity("Boston");
        address.setState("MA");
        address.setCountry("USA");
        address.setPostalCode("02101");
        address.setActive(true);
        addressRepository.save(address);

        Page<Address> addresses = addressRepository.findByAddressType(AddressType.USER_BILLING, Pageable.unpaged());
        assertEquals(1, addresses.getTotalElements());
        assertEquals("100 Main Street", addresses.getContent().get(0).getStreet());
        assertEquals("Boston", addresses.getContent().get(0).getCity());
        assertEquals(AddressType.USER_BILLING, addresses.getContent().get(0).getAddressType());
    }

    @Test
    void testHotelWithFullAddress() {
        Address address = new Address();
        address.setAddressType(AddressType.HOTEL);
        address.setStreet("456 Ocean Drive");
        address.setCity("Miami");
        address.setState("FL");
        address.setCountry("USA");
        address.setPostalCode("33139");
        address.setActive(true);
        address = addressRepository.save(address);

        Hotel hotel = new Hotel();
        hotel.setName("Beach Resort");
        hotel.setAddress("456 Ocean Drive");
        hotel.setCity("Miami");
        hotel.setCountry("USA");
        hotel.setFullAddress(address);
        hotel = hotelRepository.save(hotel);

        Hotel found = hotelRepository.findById(hotel.getId()).orElseThrow();
        assertNotNull(found.getFullAddress());
        assertEquals("456 Ocean Drive", found.getFullAddress().getStreet());
        assertEquals("Miami", found.getFullAddress().getCity());
        assertEquals("FL", found.getFullAddress().getState());
    }

    @Test
    void testUserWithBillingAddress() {
        Address billingAddress = new Address();
        billingAddress.setAddressType(AddressType.USER_BILLING);
        billingAddress.setStreet("789 Elm Street");
        billingAddress.setCity("Chicago");
        billingAddress.setState("IL");
        billingAddress.setCountry("USA");
        billingAddress.setPostalCode("60601");
        billingAddress.setActive(true);
        billingAddress = addressRepository.save(billingAddress);

        User user = new User();
        user.setUsername("address.user");
        user.setEmail("address.user@test.com");
        user.setPassword(passwordEncoder.encode("Test@1234"));
        user.setEnabled(true);
        user.setBillingAddress(billingAddress);
        user = userRepository.save(user);

        User found = userRepository.findById(user.getId()).orElseThrow();
        assertNotNull(found.getBillingAddress());
        assertEquals("789 Elm Street", found.getBillingAddress().getStreet());
        assertEquals("Chicago", found.getBillingAddress().getCity());
    }

    @Test
    void testAddressFullAddressMethod() {
        Address address = new Address();
        address.setStreet("123 Main St");
        address.setStreet2("Suite 500");
        address.setCity("New York");
        address.setState("NY");
        address.setCountry("USA");
        address.setPostalCode("10001");

        String fullAddress = address.getFullAddress();
        assertTrue(fullAddress.contains("123 Main St"));
        assertTrue(fullAddress.contains("Suite 500"));
        assertTrue(fullAddress.contains("New York"));
        assertTrue(fullAddress.contains("NY"));
        assertTrue(fullAddress.contains("10001"));
        assertTrue(fullAddress.contains("USA"));
    }

    @Test
    void testSeedAmenities() {
        Amenity amenity = new Amenity();
        amenity.setName("Free WiFi");
        amenity.setDescription("High-speed wireless internet access");
        amenity.setIcon("wifi");
        amenity.setCategory("Connectivity");
        amenity.setActive(true);
        amenityRepository.save(amenity);

        Optional<Amenity> found = amenityRepository.findAll().stream()
                .filter(a -> a.getName().equals("Free WiFi"))
                .findFirst();

        assertTrue(found.isPresent());
        assertEquals("High-speed wireless internet access", found.get().getDescription());
        assertEquals("wifi", found.get().getIcon());
        assertEquals("Connectivity", found.get().getCategory());
        assertTrue(found.get().isActive());
    }

    @Test
    void testSeedRoomTypes() {
        RoomType roomType = new RoomType();
        roomType.setName("Standard Single");
        roomType.setCapacity(1);
        roomType.setBeds(1);
        roomType.setPricePerNight(new BigDecimal("89.99"));
        roomType.setSeasonalPrice(new BigDecimal("109.99"));
        roomType.setDescription("Cozy single room with essential amenities");
        roomType.setImagePath("/images/rooms/standard-single.jpg");
        roomType.setFreeCancellationHours(24);
        roomType.setCancellationRules("Free cancellation up to 24 hours before check-in");
        roomType.setActive(true);
        roomTypeRepository.save(roomType);

        Optional<RoomType> found = roomTypeRepository.findByName("Standard Single");
        assertTrue(found.isPresent());
        assertEquals(1, found.get().getCapacity());
        assertEquals(1, found.get().getBeds());
        assertEquals(new BigDecimal("89.99"), found.get().getPricePerNight());
        assertEquals(new BigDecimal("109.99"), found.get().getSeasonalPrice());
        assertEquals("/images/rooms/standard-single.jpg", found.get().getImagePath());
        assertEquals(24, found.get().getFreeCancellationHours());
    }

    @Test
    void testSeedHotels() {
        Amenity amenity = new Amenity();
        amenity.setName("Pool");
        amenity.setDescription("Swimming pool");
        amenity.setActive(true);
        amenity = amenityRepository.save(amenity);

        Hotel hotel = new Hotel();
        hotel.setName("Grand Plaza Hotel");
        hotel.setAddress("123 Fifth Avenue");
        hotel.setCity("New York");
        hotel.setCountry("USA");
        hotel.setPhone("+1-212-555-0100");
        hotel.setEmail("contact@grandplaza.com");
        hotel.setStarRating(5);
        hotel.setNumberOfFloors(25);
        hotel.setDescription("Experience luxury in Manhattan");
        hotel.getAmenities().add(amenity);
        hotelRepository.save(hotel);

        List<Hotel> hotels = hotelRepository.findAll();
        assertEquals(1, hotels.size());
        assertEquals("Grand Plaza Hotel", hotels.get(0).getName());
        assertEquals("New York", hotels.get(0).getCity());
        assertEquals(5, hotels.get(0).getStarRating());
        assertFalse(hotels.get(0).getAmenities().isEmpty());
    }

    @Test
    void testSeedRooms() {
        RoomType roomType = new RoomType();
        roomType.setName("Standard Room");
        roomType.setCapacity(2);
        roomType.setBeds(1);
        roomType.setPricePerNight(new BigDecimal("100.00"));
        roomType.setCancellationRules("Free cancellation");
        roomType = roomTypeRepository.save(roomType);

        Hotel hotel = new Hotel();
        hotel.setName("Test Hotel");
        hotel.setAddress("123 Test St");
        hotel.setCity("Test City");
        hotel.setCountry("Test Country");
        hotel = hotelRepository.save(hotel);

        Room room = new Room();
        room.setRoomNumber("101");
        room.setFloor(1);
        room.setHotel(hotel);
        room.setRoomType(roomType);
        room.setAvailable(true);
        room.setActive(true);
        room.setNotes("Recently renovated");
        roomRepository.save(room);

        List<Room> rooms = roomRepository.findByHotelId(hotel.getId());
        assertEquals(1, rooms.size());
        assertEquals("101", rooms.get(0).getRoomNumber());
        assertEquals(1, rooms.get(0).getFloor());
        assertTrue(rooms.get(0).isAvailable());
        assertTrue(rooms.get(0).isActive());
        assertEquals("Recently renovated", rooms.get(0).getNotes());
    }

    @Test
    void testSeedUsers() {
        User user = new User();
        user.setUsername("john.smith");
        user.setEmail("john.smith@email.com");
        user.setPassword(passwordEncoder.encode("Password@123"));
        user.setEnabled(true);
        user.setAccountLocked(false);
        user.setFailedLoginAttempts(0);
        user.getRoles().add(userRole);
        userRepository.save(user);

        Optional<User> found = userRepository.findByUsername("john.smith");
        assertTrue(found.isPresent());
        assertEquals("john.smith@email.com", found.get().getEmail());
        assertTrue(found.get().isEnabled());
        assertFalse(found.get().isAccountLocked());
        assertTrue(passwordEncoder.matches("Password@123", found.get().getPassword()));
    }

    @Test
    void testSeedBookings() {
        RoomType roomType = new RoomType();
        roomType.setName("Deluxe Room");
        roomType.setCapacity(2);
        roomType.setBeds(1);
        roomType.setPricePerNight(new BigDecimal("150.00"));
        roomType.setCancellationRules("Free cancellation");
        roomType = roomTypeRepository.save(roomType);

        Hotel hotel = new Hotel();
        hotel.setName("Booking Test Hotel");
        hotel.setAddress("456 Test Ave");
        hotel = hotelRepository.save(hotel);

        Room room = new Room();
        room.setRoomNumber("201");
        room.setFloor(2);
        room.setHotel(hotel);
        room.setRoomType(roomType);
        room = roomRepository.save(room);

        User user = new User();
        user.setUsername("booking.user");
        user.setEmail("booking@test.com");
        user.setPassword(passwordEncoder.encode("Test@1234"));
        user.setEnabled(true);
        user = userRepository.save(user);

        LocalDate checkIn = LocalDate.now().plusDays(10);
        LocalDate checkOut = checkIn.plusDays(3);
        BigDecimal pricePerNight = new BigDecimal("150.00");
        BigDecimal amenityTotal = new BigDecimal("25.00");
        BigDecimal discount = new BigDecimal("10.00");
        BigDecimal subtotal = pricePerNight.multiply(BigDecimal.valueOf(3)).add(amenityTotal).subtract(discount);
        BigDecimal taxAmount = subtotal.multiply(BigDecimal.valueOf(0.10)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalPrice = subtotal.add(taxAmount).setScale(2, RoundingMode.HALF_UP);

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setRoom(room);
        booking.setCheckInDate(checkIn);
        booking.setCheckOutDate(checkOut);
        booking.setNumberOfGuests(2);
        booking.setNumberOfAdults(2);
        booking.setNumberOfChildren(0);
        booking.setPricePerNight(pricePerNight);
        booking.setAmenityTotal(amenityTotal);
        booking.setDiscount(discount);
        booking.setTaxAmount(taxAmount);
        booking.setTotalPrice(totalPrice);
        booking.setNotes("VIP guest");
        booking.setSpecialRequests("Late check-out requested");
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setConfirmationNumber("BK" + System.currentTimeMillis());
        bookingRepository.save(booking);

        Page<Booking> bookings = bookingRepository.findByUserId(user.getId(), Pageable.unpaged());
        assertEquals(1, bookings.getTotalElements());
        assertEquals(BookingStatus.CONFIRMED, bookings.getContent().get(0).getStatus());
        assertEquals(2, bookings.getContent().get(0).getNumberOfGuests());
        assertNotNull(bookings.getContent().get(0).getAmenityTotal());
        assertNotNull(bookings.getContent().get(0).getDiscount());
        assertNotNull(bookings.getContent().get(0).getNotes());
        assertNotNull(bookings.getContent().get(0).getSpecialRequests());
    }

    @Test
    void testSeedCancelledBooking() {
        RoomType roomType = new RoomType();
        roomType.setName("Cancel Test Room");
        roomType.setCapacity(2);
        roomType.setBeds(1);
        roomType.setPricePerNight(new BigDecimal("100.00"));
        roomType.setCancellationRules("Free cancellation");
        roomType = roomTypeRepository.save(roomType);

        Hotel hotel = new Hotel();
        hotel.setName("Cancel Test Hotel");
        hotel.setAddress("789 Cancel St");
        hotel = hotelRepository.save(hotel);

        Room room = new Room();
        room.setRoomNumber("301");
        room.setFloor(3);
        room.setHotel(hotel);
        room.setRoomType(roomType);
        room = roomRepository.save(room);

        User user = new User();
        user.setUsername("cancel.user");
        user.setEmail("cancel@test.com");
        user.setPassword(passwordEncoder.encode("Test@1234"));
        user.setEnabled(true);
        user = userRepository.save(user);

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setRoom(room);
        booking.setCheckInDate(LocalDate.now().plusDays(5));
        booking.setCheckOutDate(LocalDate.now().plusDays(7));
        booking.setNumberOfGuests(1);
        booking.setNumberOfAdults(1);
        booking.setPricePerNight(new BigDecimal("100.00"));
        booking.setTotalPrice(new BigDecimal("220.00"));
        booking.setTaxAmount(new BigDecimal("20.00"));
        booking.setStatus(BookingStatus.CANCELLED);
        booking.setCancellationDate(LocalDateTime.now().minusDays(2));
        booking.setCancellationReason("Change of travel plans");
        booking.setConfirmationNumber("BK" + System.currentTimeMillis());
        bookingRepository.save(booking);

        Page<Booking> bookings = bookingRepository.findByUserId(user.getId(), Pageable.unpaged());
        assertEquals(1, bookings.getTotalElements());
        assertEquals(BookingStatus.CANCELLED, bookings.getContent().get(0).getStatus());
        assertNotNull(bookings.getContent().get(0).getCancellationDate());
        assertEquals("Change of travel plans", bookings.getContent().get(0).getCancellationReason());
    }

    @Test
    void testSeedBookingGuests() {
        RoomType roomType = new RoomType();
        roomType.setName("Guest Test Room");
        roomType.setCapacity(4);
        roomType.setBeds(2);
        roomType.setPricePerNight(new BigDecimal("200.00"));
        roomType.setCancellationRules("Free cancellation");
        roomType = roomTypeRepository.save(roomType);

        Hotel hotel = new Hotel();
        hotel.setName("Guest Test Hotel");
        hotel.setAddress("111 Guest St");
        hotel = hotelRepository.save(hotel);

        Room room = new Room();
        room.setRoomNumber("401");
        room.setFloor(4);
        room.setHotel(hotel);
        room.setRoomType(roomType);
        room = roomRepository.save(room);

        User user = new User();
        user.setUsername("guest.booker");
        user.setEmail("guestbooker@test.com");
        user.setPassword(passwordEncoder.encode("Test@1234"));
        user.setEnabled(true);
        user = userRepository.save(user);

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setRoom(room);
        booking.setCheckInDate(LocalDate.now().plusDays(15));
        booking.setCheckOutDate(LocalDate.now().plusDays(18));
        booking.setNumberOfGuests(2);
        booking.setNumberOfAdults(2);
        booking.setPricePerNight(new BigDecimal("200.00"));
        booking.setTotalPrice(new BigDecimal("660.00"));
        booking.setTaxAmount(new BigDecimal("60.00"));
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setConfirmationNumber("BK" + System.currentTimeMillis());
        booking = bookingRepository.save(booking);

        BookingGuest guest1 = new BookingGuest();
        guest1.setBooking(booking);
        guest1.setFirstName("John");
        guest1.setLastName("Doe");
        guest1.setEmail("john.doe@guest.com");
        guest1.setPhone("+1-555-123-4567");
        guest1.setPrimaryGuest(true);
        guest1.setNationality("USA");
        guest1.setDocumentType("Passport");
        guest1.setDocumentNumber("P123456789");
        bookingGuestRepository.save(guest1);

        BookingGuest guest2 = new BookingGuest();
        guest2.setBooking(booking);
        guest2.setFirstName("Jane");
        guest2.setLastName("Doe");
        guest2.setEmail("jane.doe@guest.com");
        guest2.setPhone("+1-555-987-6543");
        guest2.setPrimaryGuest(false);
        guest2.setNationality("USA");
        guest2.setDocumentType("Passport");
        guest2.setDocumentNumber("P987654321");
        bookingGuestRepository.save(guest2);

        List<BookingGuest> guests = bookingGuestRepository.findByBookingId(booking.getId());
        assertEquals(2, guests.size());

        Optional<BookingGuest> primary = guests.stream().filter(BookingGuest::isPrimaryGuest).findFirst();
        assertTrue(primary.isPresent());
        assertEquals("John", primary.get().getFirstName());
    }

    @Test
    void testSeedPayments() {
        RoomType roomType = new RoomType();
        roomType.setName("Payment Test Room");
        roomType.setCapacity(2);
        roomType.setBeds(1);
        roomType.setPricePerNight(new BigDecimal("150.00"));
        roomType.setCancellationRules("Free cancellation");
        roomType = roomTypeRepository.save(roomType);

        Hotel hotel = new Hotel();
        hotel.setName("Payment Test Hotel");
        hotel.setAddress("222 Payment St");
        hotel = hotelRepository.save(hotel);

        Room room = new Room();
        room.setRoomNumber("501");
        room.setFloor(5);
        room.setHotel(hotel);
        room.setRoomType(roomType);
        room = roomRepository.save(room);

        User user = new User();
        user.setUsername("payment.user");
        user.setEmail("payment@test.com");
        user.setPassword(passwordEncoder.encode("Test@1234"));
        user.setEnabled(true);
        user = userRepository.save(user);

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setRoom(room);
        booking.setCheckInDate(LocalDate.now().plusDays(20));
        booking.setCheckOutDate(LocalDate.now().plusDays(22));
        booking.setNumberOfGuests(2);
        booking.setNumberOfAdults(2);
        booking.setPricePerNight(new BigDecimal("150.00"));
        booking.setTotalPrice(new BigDecimal("330.00"));
        booking.setTaxAmount(new BigDecimal("30.00"));
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setConfirmationNumber("BK" + System.currentTimeMillis());
        booking = bookingRepository.save(booking);

        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmount(booking.getTotalPrice());
        payment.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        payment.setStatus(PaymentStatus.PAID);
        payment.setTransactionId("TXN" + System.currentTimeMillis());
        payment.setPaymentDate(LocalDateTime.now().minusDays(5));
        payment.setDescription("Full payment for booking " + booking.getConfirmationNumber());
        paymentRepository.save(payment);

        List<Payment> payments = paymentRepository.findByBookingId(booking.getId());
        assertEquals(1, payments.size());
        assertEquals(PaymentStatus.PAID, payments.get(0).getStatus());
        assertEquals(PaymentMethod.CREDIT_CARD, payments.get(0).getPaymentMethod());
        assertNotNull(payments.get(0).getTransactionId());
        assertNotNull(payments.get(0).getPaymentDate());
        assertNotNull(payments.get(0).getDescription());
    }

    @Test
    void testSeedRefundedPayment() {
        RoomType roomType = new RoomType();
        roomType.setName("Refund Test Room");
        roomType.setCapacity(2);
        roomType.setBeds(1);
        roomType.setPricePerNight(new BigDecimal("120.00"));
        roomType.setCancellationRules("Free cancellation");
        roomType = roomTypeRepository.save(roomType);

        Hotel hotel = new Hotel();
        hotel.setName("Refund Test Hotel");
        hotel.setAddress("333 Refund St");
        hotel = hotelRepository.save(hotel);

        Room room = new Room();
        room.setRoomNumber("601");
        room.setFloor(6);
        room.setHotel(hotel);
        room.setRoomType(roomType);
        room = roomRepository.save(room);

        User user = new User();
        user.setUsername("refund.user");
        user.setEmail("refund@test.com");
        user.setPassword(passwordEncoder.encode("Test@1234"));
        user.setEnabled(true);
        user = userRepository.save(user);

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setRoom(room);
        booking.setCheckInDate(LocalDate.now().plusDays(25));
        booking.setCheckOutDate(LocalDate.now().plusDays(27));
        booking.setNumberOfGuests(1);
        booking.setNumberOfAdults(1);
        booking.setPricePerNight(new BigDecimal("120.00"));
        booking.setTotalPrice(new BigDecimal("264.00"));
        booking.setTaxAmount(new BigDecimal("24.00"));
        booking.setStatus(BookingStatus.CANCELLED);
        booking.setCancellationDate(LocalDateTime.now().minusDays(3));
        booking.setCancellationReason("Flight cancelled");
        booking.setConfirmationNumber("BK" + System.currentTimeMillis());
        booking = bookingRepository.save(booking);

        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmount(booking.getTotalPrice());
        payment.setPaymentMethod(PaymentMethod.PAYPAL);
        payment.setStatus(PaymentStatus.REFUNDED);
        payment.setTransactionId("TXN" + System.currentTimeMillis());
        payment.setPaymentDate(LocalDateTime.now().minusDays(10));
        payment.setDescription("Payment for cancelled booking");
        payment.setRefundAmount(booking.getTotalPrice());
        payment.setRefundDate(booking.getCancellationDate());
        payment.setRefundReason("Booking cancelled: Flight cancelled");
        paymentRepository.save(payment);

        List<Payment> payments = paymentRepository.findByBookingId(booking.getId());
        assertEquals(1, payments.size());
        assertEquals(PaymentStatus.REFUNDED, payments.get(0).getStatus());
        assertNotNull(payments.get(0).getRefundAmount());
        assertNotNull(payments.get(0).getRefundDate());
        assertNotNull(payments.get(0).getRefundReason());
    }

    @Test
    void testSeedReviews() {
        Hotel hotel = new Hotel();
        hotel.setName("Review Test Hotel");
        hotel.setAddress("444 Review St");
        hotel = hotelRepository.save(hotel);

        User user = new User();
        user.setUsername("review.user");
        user.setEmail("review@test.com");
        user.setPassword(passwordEncoder.encode("Test@1234"));
        user.setEnabled(true);
        user = userRepository.save(user);

        Review review = new Review();
        review.setUser(user);
        review.setHotel(hotel);
        review.setRating(5);
        review.setTitle("Excellent stay!");
        review.setComment("Had a wonderful stay at this hotel. Highly recommended!");
        review.setVerified(false);
        review.setApproved(true);
        review.setCleanlinessRating(5);
        review.setServiceRating(5);
        review.setLocationRating(4);
        review.setValueRating(4);
        review.setResponse("Thank you for your review!");
        review.setResponseDate(LocalDateTime.now().minusDays(2));
        reviewRepository.save(review);

        List<Review> reviews = reviewRepository.findByUserId(user.getId());
        assertEquals(1, reviews.size());
        assertEquals(5, reviews.get(0).getRating());
        assertEquals("Excellent stay!", reviews.get(0).getTitle());
        assertTrue(reviews.get(0).isApproved());
        assertNotNull(reviews.get(0).getCleanlinessRating());
        assertNotNull(reviews.get(0).getServiceRating());
        assertNotNull(reviews.get(0).getLocationRating());
        assertNotNull(reviews.get(0).getValueRating());
        assertNotNull(reviews.get(0).getResponse());
        assertNotNull(reviews.get(0).getResponseDate());
    }

    @Test
    void testUniqueConstraints() {
        Amenity amenity1 = new Amenity();
        amenity1.setName("Unique Amenity");
        amenity1.setDescription("Test");
        amenity1.setActive(true);
        amenityRepository.save(amenity1);

        long amenityCount = amenityRepository.findAll().stream()
                .filter(a -> a.getName().equals("Unique Amenity"))
                .count();
        assertEquals(1, amenityCount);

        User user1 = new User();
        user1.setUsername("unique.user");
        user1.setEmail("unique@test.com");
        user1.setPassword(passwordEncoder.encode("Test@1234"));
        user1.setEnabled(true);
        userRepository.save(user1);

        assertTrue(userRepository.existsByUsername("unique.user"));
        assertTrue(userRepository.existsByEmail("unique@test.com"));
    }

    @Test
    void testBigDecimalPrecision() {
        RoomType roomType = new RoomType();
        roomType.setName("Precision Test Room");
        roomType.setCapacity(2);
        roomType.setBeds(1);
        roomType.setPricePerNight(new BigDecimal("99.99"));
        roomType.setSeasonalPrice(new BigDecimal("129.99"));
        roomType.setCancellationRules("Free cancellation");
        roomType = roomTypeRepository.save(roomType);

        assertEquals(new BigDecimal("99.99"), roomType.getPricePerNight());
        assertEquals(new BigDecimal("129.99"), roomType.getSeasonalPrice());

        BigDecimal calculated = roomType.getPricePerNight()
                .multiply(BigDecimal.valueOf(3))
                .multiply(BigDecimal.valueOf(1.10))
                .setScale(2, RoundingMode.HALF_UP);

        assertEquals(new BigDecimal("329.97"), calculated);
    }
}
