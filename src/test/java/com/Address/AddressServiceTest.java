package com.Address;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressServiceImpl addressService;

    private Address address;
    private AddressRequestDTO requestDTO;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        address = new Address();
        address.setId(1L);
        address.setAddressType(AddressType.HOTEL);
        address.setStreet("123 Main St");
        address.setStreet2("Suite 100");
        address.setCity("New York");
        address.setState("NY");
        address.setCountry("USA");
        address.setPostalCode("10001");
        address.setLatitude(40.7128);
        address.setLongitude(-74.0060);
        address.setActive(true);

        requestDTO = new AddressRequestDTO();
        requestDTO.setAddressType(AddressType.HOTEL);
        requestDTO.setStreet("123 Main St");
        requestDTO.setStreet2("Suite 100");
        requestDTO.setCity("New York");
        requestDTO.setState("NY");
        requestDTO.setCountry("USA");
        requestDTO.setPostalCode("10001");
        requestDTO.setLatitude(40.7128);
        requestDTO.setLongitude(-74.0060);

        pageable = PageRequest.of(0, 10);
    }

    @Nested
    class CreateTests {

        @Test
        void shouldCreateAddress() {
            when(addressRepository.save(any(Address.class))).thenReturn(address);

            AddressResponseDTO result = addressService.create(requestDTO);

            assertNotNull(result);
            assertEquals("123 Main St", result.getStreet());
            assertEquals("New York", result.getCity());
            assertEquals("USA", result.getCountry());
            assertEquals(AddressType.HOTEL, result.getAddressType());
            verify(addressRepository).save(any(Address.class));
        }

        @Test
        void shouldCreateAddressWithMinimalFields() {
            AddressRequestDTO minimalDTO = new AddressRequestDTO();
            minimalDTO.setStreet("456 Oak Ave");
            minimalDTO.setCity("Boston");
            minimalDTO.setCountry("USA");

            Address minimalAddress = new Address();
            minimalAddress.setId(2L);
            minimalAddress.setStreet("456 Oak Ave");
            minimalAddress.setCity("Boston");
            minimalAddress.setCountry("USA");
            minimalAddress.setActive(true);

            when(addressRepository.save(any(Address.class))).thenReturn(minimalAddress);

            AddressResponseDTO result = addressService.create(minimalDTO);

            assertNotNull(result);
            assertEquals("456 Oak Ave", result.getStreet());
            assertEquals("Boston", result.getCity());
            assertTrue(result.isActive());
        }
    }

    @Nested
    class GetTests {

        @Test
        void shouldGetAddressById() {
            when(addressRepository.findById(1L)).thenReturn(Optional.of(address));

            AddressResponseDTO result = addressService.getById(1L);

            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals("123 Main St", result.getStreet());
        }

        @Test
        void shouldThrowExceptionWhenAddressNotFound() {
            when(addressRepository.findById(999L)).thenReturn(Optional.empty());

            assertThrows(AddressNotFoundException.class, () -> addressService.getById(999L));
        }

        @Test
        void shouldGetAllAddresses() {
            Page<Address> page = new PageImpl<>(List.of(address));
            when(addressRepository.findAll(pageable)).thenReturn(page);

            Page<AddressResponseDTO> result = addressService.getAll(pageable);

            assertNotNull(result);
            assertEquals(1, result.getTotalElements());
        }

        @Test
        void shouldGetAllActiveAddresses() {
            Page<Address> page = new PageImpl<>(List.of(address));
            when(addressRepository.findByActiveTrue(pageable)).thenReturn(page);

            Page<AddressResponseDTO> result = addressService.getAllActive(pageable);

            assertNotNull(result);
            assertEquals(1, result.getTotalElements());
            assertTrue(result.getContent().get(0).isActive());
        }

        @Test
        void shouldGetAllInactiveAddresses() {
            address.setActive(false);
            Page<Address> page = new PageImpl<>(List.of(address));
            when(addressRepository.findByActiveFalse(pageable)).thenReturn(page);

            Page<AddressResponseDTO> result = addressService.getAllInactive(pageable);

            assertNotNull(result);
            assertEquals(1, result.getTotalElements());
        }
    }

    @Nested
    class UpdateTests {

        @Test
        void shouldUpdateAddress() {
            when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
            when(addressRepository.save(any(Address.class))).thenReturn(address);

            requestDTO.setStreet("456 Updated St");
            AddressResponseDTO result = addressService.update(1L, requestDTO);

            assertNotNull(result);
            verify(addressRepository).save(any(Address.class));
        }

        @Test
        void shouldThrowExceptionWhenUpdatingNonExistentAddress() {
            when(addressRepository.findById(999L)).thenReturn(Optional.empty());

            assertThrows(AddressNotFoundException.class, () -> addressService.update(999L, requestDTO));
        }
    }

    @Nested
    class DeleteTests {

        @Test
        void shouldDeleteAddress() {
            when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
            doNothing().when(addressRepository).delete(address);

            assertDoesNotThrow(() -> addressService.delete(1L));
            verify(addressRepository).delete(address);
        }

        @Test
        void shouldThrowExceptionWhenDeletingNonExistentAddress() {
            when(addressRepository.findById(999L)).thenReturn(Optional.empty());

            assertThrows(AddressNotFoundException.class, () -> addressService.delete(999L));
        }
    }

    @Nested
    class ActivationTests {

        @Test
        void shouldDeactivateAddress() {
            when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
            when(addressRepository.save(any(Address.class))).thenReturn(address);

            assertDoesNotThrow(() -> addressService.deactivate(1L));
            verify(addressRepository).save(any(Address.class));
        }

        @Test
        void shouldActivateAddress() {
            address.setActive(false);
            when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
            when(addressRepository.save(any(Address.class))).thenReturn(address);

            assertDoesNotThrow(() -> addressService.activate(1L));
            verify(addressRepository).save(any(Address.class));
        }
    }

    @Nested
    class TypeTests {

        @Test
        void shouldGetAddressesByType() {
            Page<Address> page = new PageImpl<>(List.of(address));
            when(addressRepository.findByAddressType(AddressType.HOTEL, pageable)).thenReturn(page);

            Page<AddressResponseDTO> result = addressService.getByType(AddressType.HOTEL, pageable);

            assertNotNull(result);
            assertEquals(1, result.getTotalElements());
            assertEquals(AddressType.HOTEL, result.getContent().get(0).getAddressType());
        }

        @Test
        void shouldGetActiveAddressesByType() {
            Page<Address> page = new PageImpl<>(List.of(address));
            when(addressRepository.findByAddressTypeAndActiveTrue(AddressType.HOTEL, pageable)).thenReturn(page);

            Page<AddressResponseDTO> result = addressService.getByTypeActive(AddressType.HOTEL, pageable);

            assertNotNull(result);
            assertEquals(1, result.getTotalElements());
        }
    }

    @Nested
    class SearchTests {

        @Test
        void shouldSearchAddresses() {
            Page<Address> page = new PageImpl<>(List.of(address));
            when(addressRepository.searchAddresses("New York", "USA", "NY", "10001", pageable))
                    .thenReturn(page);

            Page<AddressResponseDTO> result = addressService.search("New York", "USA", "NY", "10001", pageable);

            assertNotNull(result);
            assertEquals(1, result.getTotalElements());
        }

        @Test
        void shouldSearchActiveAddresses() {
            Page<Address> page = new PageImpl<>(List.of(address));
            when(addressRepository.searchActiveAddresses("New York", "USA", pageable)).thenReturn(page);

            Page<AddressResponseDTO> result = addressService.searchActive("New York", "USA", pageable);

            assertNotNull(result);
            assertEquals(1, result.getTotalElements());
        }
    }

    @Nested
    class UtilityTests {

        @Test
        void shouldGetDistinctCountries() {
            List<String> countries = Arrays.asList("USA", "UK", "France");
            when(addressRepository.findDistinctCountries()).thenReturn(countries);

            List<String> result = addressService.getDistinctCountries();

            assertNotNull(result);
            assertEquals(3, result.size());
            assertTrue(result.contains("USA"));
        }

        @Test
        void shouldGetCitiesByCountry() {
            List<String> cities = Arrays.asList("New York", "Boston", "Chicago");
            when(addressRepository.findDistinctCitiesByCountry("USA")).thenReturn(cities);

            List<String> result = addressService.getCitiesByCountry("USA");

            assertNotNull(result);
            assertEquals(3, result.size());
            assertTrue(result.contains("New York"));
        }

        @Test
        void shouldCheckAddressExists() {
            when(addressRepository.existsByStreetAndCityAndCountry("123 Main St", "New York", "USA"))
                    .thenReturn(true);

            boolean exists = addressService.existsByStreetAndCityAndCountry("123 Main St", "New York", "USA");

            assertTrue(exists);
        }

        @Test
        void shouldReturnFalseWhenAddressDoesNotExist() {
            when(addressRepository.existsByStreetAndCityAndCountry("Unknown St", "Unknown City", "Unknown Country"))
                    .thenReturn(false);

            boolean exists = addressService.existsByStreetAndCityAndCountry("Unknown St", "Unknown City", "Unknown Country");

            assertFalse(exists);
        }
    }
}
