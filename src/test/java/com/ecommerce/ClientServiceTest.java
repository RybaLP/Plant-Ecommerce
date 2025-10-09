//package com.ecommerce;
//
//import com.ecommerce.dto.user.RegisterDto;
//import com.ecommerce.enums.UserRole;
//import com.ecommerce.model.user.Address;
//import com.ecommerce.model.user.Client;
//
//import com.ecommerce.service.ClientService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class ClientServiceTest {
//    @Mock
//    private UserRepository userRepository;
//
//    @InjectMocks
//    private ClientService userService;
//
//    private RegisterDto registerDto;
//
//    @BeforeEach
//    void setUp() {
//        registerDto = RegisterDto.builder()
//                .username("testxdexd")
//                .password("superStrongapssword")
//                .email("text@example.com")
//                .firstName("Jan")
//                .lastName("Kowalski")
//                .phoneNumber("123123123")
//                .address(Address.builder()
//                        .street("Kwiatowa")
//                        .city("Warszawa")
//                        .postalCode("00-00t")
//                        .country("Polska")
//                        .build())
//                .build();
//    }
//
//    @Test
//    void registerNewClient_should_create_user_when_username_not_exists() {
//        when(userRepository.findByUsername("testxdexd")).thenReturn(Optional.empty());
//        when(userRepository.save(any(Client.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//        Client resultUser = (Client) userService.registerNewClient(registerDto);
//
//        assertNotNull(resultUser);
//        assertEquals("testxdexd", resultUser.getUsername());
//        assertEquals("superStrongapssword", resultUser.getPassword());
//        assertEquals(UserRole.CLIENT, resultUser.getRole());
//    }
//}
