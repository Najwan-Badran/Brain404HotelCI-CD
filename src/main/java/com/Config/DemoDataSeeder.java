package com.Config;

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
import com.Image.Image;
import com.Image.ImageEntityType;
import com.Image.ImageRepository;
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
import com.RoomAvailability.RoomAvailability;
import com.RoomAvailability.RoomAvailabilityRepository;
import com.RoomType.RoomType;
import com.RoomType.RoomTypeRepository;
import com.User.User;
import com.User.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Locale;

@Component
@Order(2)
@Profile("demo")
public class DemoDataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DemoDataSeeder.class);

    private final AddressRepository addressRepository;
    private final AmenityRepository amenityRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BookingRepository bookingRepository;
    private final BookingGuestRepository bookingGuestRepository;
    private final PaymentRepository paymentRepository;
    private final ReviewRepository reviewRepository;
    private final PasswordEncoder passwordEncoder;
    private final Random random = new Random(42);
    private final AtomicLong bookingCounter = new AtomicLong(System.currentTimeMillis());
    private final AtomicLong paymentCounter = new AtomicLong(System.currentTimeMillis());
    private final RoomAvailabilityRepository roomAvailabilityRepository;
    private final ImageRepository imageRepository;

    public DemoDataSeeder(AddressRepository addressRepository,
                          AmenityRepository amenityRepository,
                          RoomTypeRepository roomTypeRepository,
                          HotelRepository hotelRepository,
                          RoomRepository roomRepository,
                          UserRepository userRepository,
                          RoleRepository roleRepository,
                          BookingRepository bookingRepository,
                          BookingGuestRepository bookingGuestRepository,
                          PaymentRepository paymentRepository,
                          ReviewRepository reviewRepository,
                          PasswordEncoder passwordEncoder,
                          RoomAvailabilityRepository roomAvailabilityRepository,
                          ImageRepository imageRepository) {
        this.addressRepository = addressRepository;
        this.amenityRepository = amenityRepository;
        this.roomTypeRepository = roomTypeRepository;
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bookingRepository = bookingRepository;
        this.bookingGuestRepository = bookingGuestRepository;
        this.paymentRepository = paymentRepository;
        this.reviewRepository = reviewRepository;
        this.passwordEncoder = passwordEncoder;
        this.roomAvailabilityRepository = roomAvailabilityRepository;
        this.imageRepository = imageRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (hotelRepository.count() > 0) {
            log.info("Demo data already exists, skipping seeding");
            return;
        }

        log.info("Seeding demo data...");

        List<Address> hotelAddresses = seedHotelAddresses();
        List<Address> userAddresses = seedUserAddresses();
        List<Amenity> amenities = seedAmenities();
        List<RoomType> roomTypes = seedRoomTypes();
        List<Hotel> hotels = seedHotels(amenities, hotelAddresses);
        List<Room> rooms = seedRooms(hotels, roomTypes, amenities);
        seedRoomAvailability(rooms);
        List<User> users = seedUsers(userAddresses);
        List<Booking> bookings = seedBookings(users, rooms);
        seedBookingGuests(bookings);
        seedPayments(bookings);
        seedReviews(users, hotels, bookings);
        seedImages(hotels, rooms, roomTypes);

        log.info("Demo data seeding completed");
    }

    private List<Address> seedHotelAddresses() {
        List<Address> addresses = new ArrayList<>();
        Object[][] addressData = {
            {"123 Fifth Avenue", null, "New York", "NY", "USA", "10001", 40.7484, -73.9967},
            {"456 Ocean Drive", "Suite 100", "Miami", "FL", "USA", "33139", 25.7617, -80.1918},
            {"789 Alpine Road", null, "Denver", "CO", "USA", "80202", 39.7392, -104.9903},
            {"321 Michigan Avenue", "Floor 2", "Chicago", "IL", "USA", "60601", 41.8781, -87.6298},
            {"555 Westminster Lane", null, "London", null, "UK", "SW1A 1AA", 51.5074, -0.1278},
            {"88 Champs-Elysees", null, "Paris", null, "France", "75008", 48.8566, 2.3522},
            {"1-2-3 Shibuya", "Tower B", "Tokyo", null, "Japan", "150-0002", 35.6762, 139.6503},
            {"100 Circular Quay", null, "Sydney", "NSW", "Australia", "2000", -33.8688, 151.2093},
            {"Palm Jumeirah Boulevard", "Villa 50", "Dubai", null, "UAE", "00000", 25.1124, 55.1390},
            {"Passeig Maritim 50", null, "Barcelona", "Catalonia", "Spain", "08003", 41.3851, 2.1734}
        };

        for (Object[] data : addressData) {
            Address address = new Address();
            address.setAddressType(AddressType.HOTEL);
            address.setStreet((String) data[0]);
            address.setStreet2((String) data[1]);
            address.setCity((String) data[2]);
            address.setState((String) data[3]);
            address.setCountry((String) data[4]);
            address.setPostalCode((String) data[5]);
            address.setLatitude((Double) data[6]);
            address.setLongitude((Double) data[7]);
            address.setActive(true);
            addresses.add(addressRepository.save(address));
        }

        log.info("Created {} hotel addresses", addresses.size());
        return addresses;
    }

    private void seedImages(List<Hotel> hotels,
                            List<Room> rooms,
                            List<RoomType> roomTypes) {

        int created = 0;

        // -------------------
        // HOTEL IMAGES
        // -------------------
        for (Hotel hotel : hotels) {

            int imageCount = 3 + random.nextInt(4); // 3–6 images

            for (int i = 0; i < imageCount; i++) {
                Image img = new Image();

                img.setEntityType(ImageEntityType.HOTEL);
                img.setEntityId(hotel.getId());
                img.setPrimary(i == 0);
                img.setSortOrder(i);

                img.setUrl("https://picsum.photos/seed/hotel-" + hotel.getId() + "-" + i + "/1200/800");
                img.setAltText(hotel.getName() + " image " + (i + 1));
                img.setFileName("hotel_" + hotel.getId() + "_" + i + ".jpg");
                img.setContentType("image/jpeg");
                img.setFileSize(500000L + random.nextInt(1500000));

                imageRepository.save(img);
                created++;
            }
        }

        // -------------------
        // ROOM IMAGES
        // -------------------
        for (Room room : rooms) {

            int imageCount = 2 + random.nextInt(3); // 2–4 images

            for (int i = 0; i < imageCount; i++) {
                Image img = new Image();

                img.setEntityType(ImageEntityType.ROOM);
                img.setEntityId(room.getId());
                img.setPrimary(i == 0);
                img.setSortOrder(i);

                img.setUrl("https://picsum.photos/seed/room-" + room.getId() + "-" + i + "/1000/700");
                img.setAltText("Room " + room.getRoomNumber() + " image " + (i + 1));
                img.setFileName("room_" + room.getId() + "_" + i + ".jpg");
                img.setContentType("image/jpeg");
                img.setFileSize(300000L + random.nextInt(1000000));

                imageRepository.save(img);
                created++;
            }
        }

        // -------------------
        // ROOM TYPE IMAGES
        // -------------------
        for (RoomType type : roomTypes) {

            Image img = new Image();

            img.setEntityType(ImageEntityType.ROOM_TYPE);
            img.setEntityId(type.getId());
            img.setPrimary(true);
            img.setSortOrder(0);

            img.setUrl("https://picsum.photos/seed/roomtype-" + type.getId() + "/1200/800");
            img.setAltText(type.getName());
            img.setFileName("roomtype_" + type.getId() + ".jpg");
            img.setContentType("image/jpeg");
            img.setFileSize(800000L + random.nextInt(1200000));

            imageRepository.save(img);
            created++;
        }

        log.info("Created {} images", created);
    }

    private List<Address> seedUserAddresses() {
        List<Address> addresses = new ArrayList<>();
        Object[][] addressData = {
            {"100 Main Street", "Apt 4B", "Boston", "MA", "USA", "02101"},
            {"250 Oak Avenue", null, "San Francisco", "CA", "USA", "94102"},
            {"75 Elm Street", "Unit 12", "Seattle", "WA", "USA", "98101"},
            {"500 Pine Road", null, "Austin", "TX", "USA", "78701"},
            {"123 Maple Lane", "Suite 5", "Portland", "OR", "USA", "97201"},
            {"88 Cedar Court", null, "Phoenix", "AZ", "USA", "85001"},
            {"200 Birch Drive", "Apt 2A", "Atlanta", "GA", "USA", "30301"},
            {"150 Willow Way", null, "Minneapolis", "MN", "USA", "55401"},
            {"300 Spruce Street", "Floor 3", "Philadelphia", "PA", "USA", "19101"},
            {"425 Ash Boulevard", null, "Las Vegas", "NV", "USA", "89101"},
            {"600 Cherry Lane", "Apt 8C", "Nashville", "TN", "USA", "37201"},
            {"175 Poplar Avenue", null, "Charlotte", "NC", "USA", "28201"},
            {"50 Magnolia Street", "Unit 6", "New Orleans", "LA", "USA", "70112"},
            {"325 Hickory Road", null, "Columbus", "OH", "USA", "43201"},
            {"475 Walnut Court", "Suite 10", "Indianapolis", "IN", "USA", "46201"}
        };

        for (Object[] data : addressData) {
            Address address = new Address();
            address.setAddressType(AddressType.USER_BILLING);
            address.setStreet((String) data[0]);
            address.setStreet2((String) data[1]);
            address.setCity((String) data[2]);
            address.setState((String) data[3]);
            address.setCountry((String) data[4]);
            address.setPostalCode((String) data[5]);
            address.setActive(true);
            addresses.add(addressRepository.save(address));
        }

        log.info("Created {} user addresses", addresses.size());
        return addresses;
    }

    private List<Amenity> seedAmenities() {
        List<Amenity> amenities = new ArrayList<>();
        String[][] amenityData = {
            {"Free WiFi", "High-speed wireless internet access", "wifi", "Connectivity"},
            {"Swimming Pool", "Outdoor heated swimming pool", "pool", "Recreation"},
            {"Fitness Center", "24/7 fully equipped gym", "fitness", "Recreation"},
            {"Spa & Wellness", "Full-service spa treatments", "spa", "Wellness"},
            {"Restaurant", "On-site fine dining restaurant", "restaurant", "Dining"},
            {"Bar & Lounge", "Cocktail lounge and bar", "bar", "Dining"},
            {"Room Service", "24-hour in-room dining", "room_service", "Services"},
            {"Free Parking", "Complimentary on-site parking", "parking", "Transportation"},
            {"Airport Shuttle", "Free airport transfer service", "shuttle", "Transportation"},
            {"Business Center", "Meeting rooms and office services", "business", "Business"},
            {"Concierge", "24-hour concierge assistance", "concierge", "Services"},
            {"Laundry Service", "Same-day laundry and dry cleaning", "laundry", "Services"},
            {"Air Conditioning", "Individual climate control", "ac", "Comfort"},
            {"Mini Bar", "In-room refreshment center", "minibar", "In-Room"},
            {"In-Room Safe", "Electronic security safe", "safe", "Security"},
            {"Flat Screen TV", "Smart TV with streaming", "tv", "Entertainment"},
            {"Coffee Maker", "Premium coffee machine", "coffee", "In-Room"},
            {"Private Balcony", "Balcony with scenic views", "balcony", "In-Room"},
            {"Pet Friendly", "Pets welcome with amenities", "pet", "Policy"},
            {"Kids Club", "Supervised children activities", "kids", "Family"}
        };

        for (String[] data : amenityData) {
            Amenity amenity = new Amenity();
            amenity.setName(data[0]);
            amenity.setDescription(data[1]);
            amenity.setIcon(data[2]);
            amenity.setCategory(data[3]);
            amenity.setActive(true);
            amenities.add(amenityRepository.save(amenity));
        }

        log.info("Created {} amenities", amenities.size());
        return amenities;
    }

    private List<RoomType> seedRoomTypes() {
        List<RoomType> roomTypes = new ArrayList<>();
        Object[][] roomTypeData = {
            {"Standard Single", 1, 1, new BigDecimal("89.99"), new BigDecimal("109.99"), "Cozy single room with essential amenities", "/images/rooms/standard-single.jpg", 24},
            {"Standard Double", 2, 1, new BigDecimal("119.99"), new BigDecimal("149.99"), "Comfortable room with queen-size bed", "/images/rooms/standard-double.jpg", 24},
            {"Deluxe Double", 2, 1, new BigDecimal("159.99"), new BigDecimal("189.99"), "Spacious room with premium amenities", "/images/rooms/deluxe-double.jpg", 48},
            {"Twin Room", 2, 2, new BigDecimal("129.99"), new BigDecimal("159.99"), "Room with two comfortable single beds", "/images/rooms/twin-room.jpg", 24},
            {"Triple Room", 3, 3, new BigDecimal("179.99"), new BigDecimal("219.99"), "Perfect for small groups or families", "/images/rooms/triple-room.jpg", 48},
            {"Family Suite", 4, 2, new BigDecimal("249.99"), new BigDecimal("299.99"), "Ideal for families with separate living area", "/images/rooms/family-suite.jpg", 72},
            {"Junior Suite", 2, 1, new BigDecimal("299.99"), new BigDecimal("359.99"), "Elegant suite with sitting area", "/images/rooms/junior-suite.jpg", 48},
            {"Executive Suite", 2, 1, new BigDecimal("399.99"), new BigDecimal("479.99"), "Luxury suite for business travelers", "/images/rooms/executive-suite.jpg", 72},
            {"Presidential Suite", 4, 2, new BigDecimal("799.99"), new BigDecimal("999.99"), "Ultimate luxury with panoramic views", "/images/rooms/presidential-suite.jpg", 72},
            {"Penthouse", 6, 3, new BigDecimal("1299.99"), new BigDecimal("1599.99"), "Top-floor exclusive accommodation", "/images/rooms/penthouse.jpg", 72}
        };

        for (Object[] data : roomTypeData) {
            RoomType roomType = new RoomType();
            roomType.setName((String) data[0]);
            roomType.setCapacity((Integer) data[1]);
            roomType.setBeds((Integer) data[2]);
            roomType.setPricePerNight((BigDecimal) data[3]);
            roomType.setSeasonalPrice((BigDecimal) data[4]);
            roomType.setDescription((String) data[5]);
            roomType.setImagePath((String) data[6]);
            roomType.setFreeCancellationHours((Integer) data[7]);
            roomType.setCancellationRules("Free cancellation up to " + data[7] + " hours before check-in. 50% charge for late cancellation.");
            roomType.setActive(true);
            roomTypes.add(roomTypeRepository.save(roomType));
        }

        log.info("Created {} room types", roomTypes.size());
        return roomTypes;
    }

    private List<Hotel> seedHotels(List<Amenity> amenities, List<Address> hotelAddresses) {
        List<Hotel> hotels = new ArrayList<>();
        Object[][] hotelData = {
            {"Grand Plaza Hotel", "123 Fifth Avenue", "New York", "USA", "+1-212-555-0100", "contact@grandplaza.com", 5, 25},
            {"Seaside Resort & Spa", "456 Ocean Drive", "Miami", "USA", "+1-305-555-0200", "info@seasideresort.com", 4, 12},
            {"Mountain View Lodge", "789 Alpine Road", "Denver", "USA", "+1-303-555-0300", "stay@mountainview.com", 4, 8},
            {"City Center Inn", "321 Michigan Avenue", "Chicago", "USA", "+1-312-555-0400", "hello@citycenterinn.com", 3, 10},
            {"Royal Palace Hotel", "555 Westminster Lane", "London", "UK", "+44-20-555-0500", "reservations@royalpalace.co.uk", 5, 18},
            {"Le Parisien Boutique", "88 Champs-Elysees", "Paris", "France", "+33-1-555-0600", "bonjour@leparisien.fr", 4, 6},
            {"Tokyo Sky Tower Hotel", "1-2-3 Shibuya", "Tokyo", "Japan", "+81-3-555-0700", "welcome@tokyosky.jp", 5, 35},
            {"Sydney Harbour Grand", "100 Circular Quay", "Sydney", "Australia", "+61-2-555-0800", "info@harbourview.com.au", 4, 15},
            {"Dubai Oasis Resort", "Palm Jumeirah Boulevard", "Dubai", "UAE", "+971-4-555-0900", "guest@dubaiasis.ae", 5, 28},
            {"Barcelona Beach Hotel", "Passeig Maritim 50", "Barcelona", "Spain", "+34-93-555-1000", "hola@barcelonabeach.es", 4, 10}
        };

        String[] descriptions = {
            "Experience unparalleled luxury in the heart of Manhattan with stunning skyline views and world-class service.",
            "Relax in tropical paradise with direct beach access, oceanfront dining, and rejuvenating spa treatments.",
            "Escape to nature with breathtaking Rocky Mountain scenery and outdoor adventure activities.",
            "Perfect downtown location for business and leisure with easy access to attractions and entertainment.",
            "Step into British elegance with royal treatment, historic charm, and modern luxury amenities.",
            "Discover French sophistication in an intimate boutique setting near iconic landmarks.",
            "Modern Japanese hospitality meets cutting-edge design with spectacular city views.",
            "Wake up to iconic Opera House views and explore vibrant Australian culture.",
            "Indulge in Arabian luxury with unparalleled service and exclusive beachfront amenities.",
            "Mediterranean charm meets modern comfort on the beautiful Barceloneta coastline."
        };

        for (int i = 0; i < hotelData.length; i++) {
            Object[] data = hotelData[i];
            Hotel hotel = new Hotel();
            hotel.setName((String) data[0]);
            hotel.setAddress((String) data[1]);
            hotel.setCity((String) data[2]);
            hotel.setCountry((String) data[3]);
            hotel.setPhone((String) data[4]);
            hotel.setEmail((String) data[5]);
            hotel.setStarRating((Integer) data[6]);
            hotel.setNumberOfFloors((Integer) data[7]);
            hotel.setDescription(descriptions[i]);

            // Link to full address entity
            if (i < hotelAddresses.size()) {
                hotel.setFullAddress(hotelAddresses.get(i));
            }

            Set<Amenity> hotelAmenities = new HashSet<>();
            int numAmenities = 10 + random.nextInt(8);
            List<Amenity> shuffled = new ArrayList<>(amenities);
            Collections.shuffle(shuffled, random);
            for (int j = 0; j < numAmenities && j < shuffled.size(); j++) {
                hotelAmenities.add(shuffled.get(j));
            }
            hotel.setAmenities(hotelAmenities);

            hotels.add(hotelRepository.save(hotel));
        }

        log.info("Created {} hotels", hotels.size());
        return hotels;
    }

    private List<Room> seedRooms(List<Hotel> hotels, List<RoomType> roomTypes, List<Amenity> amenities) {
        List<Room> rooms = new ArrayList<>();

        String[] roomNotes = {
            "Recently renovated",
            "Corner room with extra windows",
            "Connecting room available",
            "Wheelchair accessible",
            "Pet-friendly room",
            "Extra large bathroom",
            "Balcony with city view",
            "Soundproofed walls",
            "Near elevator",
            "Away from elevator - quieter"
        };

        for (Hotel hotel : hotels) {
            int roomsPerFloor = 6 + random.nextInt(6);
            int totalFloors = Math.min(hotel.getNumberOfFloors() != null ? hotel.getNumberOfFloors() : 5, 5);

            for (int floor = 1; floor <= totalFloors; floor++) {
                for (int roomNum = 1; roomNum <= roomsPerFloor; roomNum++) {
                    Room room = new Room();
                    room.setRoomNumber(String.format(Locale.US, "%d%02d", floor, roomNum));
                    room.setFloor(floor);
                    room.setHotel(hotel);
                    room.setRoomType(roomTypes.get(random.nextInt(roomTypes.size())));
                    room.setAvailable(random.nextDouble() > 0.1);
                    room.setActive(random.nextDouble() > 0.05);
                    room.setNotes(roomNotes[random.nextInt(roomNotes.length)]);

                    Set<Amenity> roomAmenities = new HashSet<>();
                    int numAmenities = 3 + random.nextInt(5);
                    List<Amenity> shuffled = new ArrayList<>(amenities);
                    Collections.shuffle(shuffled, random);
                    for (int j = 0; j < numAmenities && j < shuffled.size(); j++) {
                        roomAmenities.add(shuffled.get(j));
                    }
                    room.setAmenities(roomAmenities);

                    rooms.add(roomRepository.save(room));
                }
            }
        }

        log.info("Created {} rooms", rooms.size());
        return rooms;
    }

    private void seedRoomAvailability(List<Room> rooms) {

        if (rooms.isEmpty()) {
            log.warn("No rooms found, skipping room availability seeding");
            return;
        }

        LocalDate startDate = LocalDate.now();
        int daysToGenerate = 60; // adjust as needed

        int created = 0;

        for (Room room : rooms) {

            BigDecimal basePrice = room.getRoomType()
                    .getPricePerNight()
                    .setScale(2, RoundingMode.HALF_UP);

            for (int i = 0; i < daysToGenerate; i++) {

                LocalDate date = startDate.plusDays(i);

                // add small dynamic pricing variation (-10% to +30%)
                double multiplier = 0.9 + (random.nextDouble() * 0.4);
                BigDecimal price = basePrice
                        .multiply(BigDecimal.valueOf(multiplier))
                        .setScale(2, RoundingMode.HALF_UP);

                boolean available = random.nextDouble() > 0.1; // 90% available

                RoomAvailability ra = new RoomAvailability();
                ra.setRoom(room);
                ra.setDate(date);
                ra.setAvailable(available);
                ra.setPrice(price);

                // optional logic
                ra.setMinStay(1);
                ra.setMaxStay(14);

                if (!available) {
                    ra.setNotes("Fully booked / blocked date");
                }

                roomAvailabilityRepository.save(ra);
                created++;
            }
        }

        log.info("Created {} room availability records", created);
    }

    private List<User> seedUsers(List<Address> userAddresses) {
        List<User> users = new ArrayList<>();
        String[][] userData = {
            {"john.smith", "john.smith@email.com"},
            {"emma.wilson", "emma.wilson@email.com"},
            {"michael.brown", "michael.brown@email.com"},
            {"sophia.davis", "sophia.davis@email.com"},
            {"james.miller", "james.miller@email.com"},
            {"olivia.garcia", "olivia.garcia@email.com"},
            {"william.martinez", "william.martinez@email.com"},
            {"ava.anderson", "ava.anderson@email.com"},
            {"benjamin.taylor", "benjamin.taylor@email.com"},
            {"isabella.thomas", "isabella.thomas@email.com"},
            {"lucas.jackson", "lucas.jackson@email.com"},
            {"mia.white", "mia.white@email.com"},
            {"henry.harris", "henry.harris@email.com"},
            {"charlotte.clark", "charlotte.clark@email.com"},
            {"alexander.lewis", "alexander.lewis@email.com"}
        };

        Optional<Role> userRole = roleRepository.findByName("ROLE_USER");

        for (int i = 0; i < userData.length; i++) {
            String[] data = userData[i];
            if (userRepository.findByUsername(data[0]).isEmpty()) {
                User user = new User();
                user.setUsername(data[0]);
                user.setEmail(data[1]);
                user.setPassword(passwordEncoder.encode("Password@123"));
                user.setEnabled(true);
                user.setAccountLocked(false);
                user.setFailedLoginAttempts(0);
                userRole.ifPresent(role -> user.setRoles(Set.of(role)));

                // Link to billing address
                if (i < userAddresses.size()) {
                    user.setBillingAddress(userAddresses.get(i));
                }

                users.add(userRepository.save(user));
            }
        }

        log.info("Created {} demo users", users.size());
        return users;
    }

    private List<Booking> seedBookings(List<User> users, List<Room> rooms) {
        List<Booking> bookings = new ArrayList<>();
        if (users.isEmpty() || rooms.isEmpty()) {
            return bookings;
        }

        LocalDate today = LocalDate.now();
        BookingStatus[] statuses = {BookingStatus.CONFIRMED, BookingStatus.CONFIRMED, BookingStatus.PENDING, BookingStatus.CANCELLED};

        String[] specialRequests = {
            "Early check-in requested",
            "Late check-out requested",
            "Extra pillows needed",
            "Quiet room preferred",
            "High floor preferred",
            "Airport pickup required",
            "Baby crib needed",
            "Wheelchair accessible room",
            "Non-smoking room required",
            "Ocean view requested"
        };

        String[] notes = {
            "VIP guest - priority service",
            "Returning customer",
            "Corporate booking",
            "Honeymoon package",
            "Birthday celebration",
            "Business trip",
            "Family vacation",
            "Anniversary trip",
            "Conference attendee",
            "Loyalty member - Gold tier"
        };

        String[] cancellationReasons = {
            "Change of travel plans",
            "Flight cancelled",
            "Medical emergency",
            "Work schedule conflict",
            "Found better rate elsewhere",
            "Personal reasons",
            "Weather conditions",
            "Visa issues"
        };

        for (int i = 0; i < 50; i++) {
            User user = users.get(random.nextInt(users.size()));
            Room room = rooms.get(random.nextInt(rooms.size()));

            int daysOffset = 1 + random.nextInt(60);
            LocalDate checkIn = today.plusDays(daysOffset);
            int nights = 1 + random.nextInt(7);
            LocalDate checkOut = checkIn.plusDays(nights);

            BigDecimal pricePerNight = room.getRoomType().getPricePerNight().setScale(2, RoundingMode.HALF_UP);
            BigDecimal subtotal = pricePerNight.multiply(BigDecimal.valueOf(nights));
            BigDecimal amenityTotal = BigDecimal.valueOf(10 + random.nextInt(50)).setScale(2, RoundingMode.HALF_UP);
            BigDecimal discount = random.nextDouble() < 0.3 ? BigDecimal.valueOf(5 + random.nextInt(20)).setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
            BigDecimal taxAmount = subtotal.add(amenityTotal).subtract(discount).multiply(BigDecimal.valueOf(0.10)).setScale(2, RoundingMode.HALF_UP);
            BigDecimal totalPrice = subtotal.add(amenityTotal).subtract(discount).add(taxAmount).setScale(2, RoundingMode.HALF_UP);

            Booking booking = new Booking();
            booking.setUser(user);
            booking.setRoom(room);
            booking.setCheckInDate(checkIn);
            booking.setCheckOutDate(checkOut);
            int capacity = room.getRoomType().getCapacity();
            booking.setNumberOfGuests(1 + random.nextInt(Math.max(1, capacity)));
            booking.setNumberOfAdults(Math.max(1, booking.getNumberOfGuests() - random.nextInt(2)));
            booking.setNumberOfChildren(Math.max(0, booking.getNumberOfGuests() - booking.getNumberOfAdults()));
            booking.setPricePerNight(pricePerNight);
            booking.setAmenityTotal(amenityTotal);
            booking.setDiscount(discount);
            booking.setTaxAmount(taxAmount);
            booking.setTotalPrice(totalPrice);
            booking.setNotes(notes[random.nextInt(notes.length)]);
            booking.setSpecialRequests(specialRequests[random.nextInt(specialRequests.length)]);
            booking.setStatus(statuses[random.nextInt(statuses.length)]);
            booking.setConfirmationNumber("BK" + bookingCounter.incrementAndGet());

            if (booking.getStatus() == BookingStatus.CANCELLED) {
                booking.setCancellationDate(LocalDateTime.now().minusDays(random.nextInt(10)));
                booking.setCancellationReason(cancellationReasons[random.nextInt(cancellationReasons.length)]);
            }

            bookings.add(bookingRepository.save(booking));
        }

        log.info("Created {} bookings", bookings.size());
        return bookings;
    }

    private void seedBookingGuests(List<Booking> bookings) {
        String[] firstNames = {"James", "Mary", "Robert", "Patricia", "John", "Jennifer", "Michael", "Linda", "David", "Elizabeth"};
        String[] lastNames = {"Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez"};
        String[] nationalities = {"USA", "UK", "Canada", "Australia", "Germany", "France", "Japan", "Brazil", "India", "Mexico"};

        int totalGuests = 0;
        for (Booking booking : bookings) {
            int numGuests = booking.getNumberOfGuests();
            for (int i = 0; i < numGuests; i++) {
                BookingGuest guest = new BookingGuest();
                guest.setBooking(booking);
                guest.setFirstName(firstNames[random.nextInt(firstNames.length)]);
                guest.setLastName(lastNames[random.nextInt(lastNames.length)]);
                guest.setEmail(guest.getFirstName().toLowerCase() + "." + guest.getLastName().toLowerCase() + random.nextInt(1000) + "@guest.com");
                guest.setPhone(String.format(Locale.US, "+1-555-%03d-%04d", random.nextInt(1000), random.nextInt(10000)));
                guest.setPrimaryGuest(i == 0);
                guest.setNationality(nationalities[random.nextInt(nationalities.length)]);
                guest.setDocumentType("Passport");
                guest.setDocumentNumber(String.format(Locale.US, "P%09d", random.nextInt(1000000000)));
                bookingGuestRepository.save(guest);
                totalGuests++;
            }
        }

        log.info("Created {} booking guests", totalGuests);
    }

    private void seedPayments(List<Booking> bookings) {
        PaymentMethod[] methods = {PaymentMethod.CREDIT_CARD, PaymentMethod.DEBIT_CARD, PaymentMethod.PAYPAL, PaymentMethod.BANK_TRANSFER, PaymentMethod.STRIPE, PaymentMethod.APPLE_PAY};

        for (Booking booking : bookings) {
            Payment payment = new Payment();
            payment.setBooking(booking);
            payment.setAmount(booking.getTotalPrice());
            payment.setPaymentMethod(methods[random.nextInt(methods.length)]);

            if (booking.getStatus() == BookingStatus.CONFIRMED) {
                payment.setStatus(PaymentStatus.PAID);
                payment.setTransactionId("TXN" + paymentCounter.incrementAndGet());
                payment.setPaymentDate(LocalDateTime.now().minusDays(1 + random.nextInt(30)));
                payment.setDescription("Full payment for booking " + booking.getConfirmationNumber());
            } else if (booking.getStatus() == BookingStatus.CANCELLED) {
                payment.setStatus(PaymentStatus.REFUNDED);
                payment.setTransactionId("TXN" + paymentCounter.incrementAndGet());
                payment.setPaymentDate(LocalDateTime.now().minusDays(15 + random.nextInt(30)));
                payment.setDescription("Payment for cancelled booking " + booking.getConfirmationNumber());
                payment.setRefundAmount(booking.getTotalPrice());
                payment.setRefundDate(booking.getCancellationDate());
                payment.setRefundReason("Booking cancelled: " + booking.getCancellationReason());
            } else {
                payment.setStatus(PaymentStatus.PENDING);
                payment.setDescription("Pending payment for booking " + booking.getConfirmationNumber());
            }

            paymentRepository.save(payment);
        }

        log.info("Created {} payments", bookings.size());
    }

    private void seedReviews(List<User> users, List<Hotel> hotels, List<Booking> bookings) {
        String[] titles = {
            "Excellent stay!", "Great experience", "Wonderful hotel", "Highly recommended",
            "Amazing service", "Perfect location", "Very comfortable", "Will come back",
            "Good value", "Nice and clean", "Friendly staff", "Beautiful views"
        };

        String[] comments = {
            "Had a wonderful stay at this hotel. The staff was extremely friendly and helpful. The room was clean and comfortable. Would definitely recommend!",
            "Great location and excellent amenities. The breakfast was delicious and the pool area was perfect for relaxation. Will definitely return.",
            "The service was impeccable from check-in to check-out. Room was spacious and well-maintained. Perfect for both business and leisure.",
            "Beautiful property with stunning views. The restaurant served amazing food and the spa treatments were rejuvenating. Highly recommend!",
            "Exceeded all expectations. The attention to detail was impressive and the staff went above and beyond to make our stay memorable.",
            "Perfect getaway destination. Clean rooms, great food, and excellent facilities. The concierge was very helpful with local recommendations.",
            "Very satisfied with my stay. The room was exactly as pictured and the amenities were top-notch. Great value for money.",
            "Lovely hotel with a warm atmosphere. The beds were extremely comfortable and the breakfast selection was impressive.",
            "Outstanding hospitality. Every request was handled promptly and professionally. The location couldn't be better.",
            "A truly memorable experience. From the elegant lobby to the comfortable rooms, everything was perfect. Will be back soon!"
        };

        List<Booking> confirmedBookings = bookings.stream()
            .filter(b -> b.getStatus() == BookingStatus.CONFIRMED || b.getStatus() == BookingStatus.PENDING)
            .toList();

        int reviewCount = 0;
        for (Booking booking : confirmedBookings) {
            if (random.nextDouble() < 0.7) {
                Review review = new Review();
                review.setUser(booking.getUser());
                review.setHotel(booking.getRoom().getHotel());
                review.setBooking(booking);
                review.setRoom(booking.getRoom());
                review.setRating(3 + random.nextInt(3));
                review.setTitle(titles[random.nextInt(titles.length)]);
                review.setComment(comments[random.nextInt(comments.length)]);
                review.setVerified(false);
                review.setApproved(random.nextDouble() < 0.8);
                review.setCleanlinessRating(3 + random.nextInt(3));
                review.setServiceRating(3 + random.nextInt(3));
                review.setLocationRating(3 + random.nextInt(3));
                review.setValueRating(3 + random.nextInt(3));

                if (random.nextDouble() < 0.3) {
                    review.setResponse("Thank you for your wonderful review! We're delighted you enjoyed your stay with us and look forward to welcoming you again.");
                    review.setResponseDate(LocalDateTime.now().minusDays(random.nextInt(10)));
                }

                reviewRepository.save(review);
                reviewCount++;
            }
        }

        log.info("Created {} reviews", reviewCount);
    }
}
