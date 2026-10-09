package com.Gym.Service;

import com.Gym.Dto.User.Auth.UserLoginDto;
import com.Gym.Dto.User.Auth.UserRegisterRequest;
import com.Gym.Exception.ObjectNotFound;
import com.Gym.Model.Address.Address;
import com.Gym.Model.Users_Models.User;
import com.Gym.Repository.User.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password4j.Argon2Password4jPasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;




@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtEncoder jwtEncoder;

    @Mock
    private Argon2Password4jPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User sampleUser;
    private UUID userId;


    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        sampleUser = new User();
        sampleUser.setId(userId);
        sampleUser.setEmail("usuario@academia.com");
        sampleUser.setCpf("123.456.789-00");
        sampleUser.setRoles(Collections.emptySet()); // Garante que a lista de roles não seja nula no login
    }
    @Test
    void findById() {
       when(userRepository.findById(userId)).thenReturn(Optional.of(sampleUser));

        User resultado = userService.findById(userId);

        assertNotNull(resultado);
        assertEquals(userId, resultado.getId());
        verify(userRepository, times(1)).findById(userId);
    }
    @Test
    @DisplayName("Should throw ObjectNotFound when user ID does not exist")
    void shouldThrowExceptionWhenIdNotFound() {

        UUID nonExistingId = UUID.randomUUID();
        when(userRepository.findById(nonExistingId)).thenReturn(Optional.empty());


        assertThrows(ObjectNotFound.class, () -> {
            userService.findById(nonExistingId);
        });

        verify(userRepository).findById(nonExistingId);
    }

    @Test
    void login() {
         UserLoginDto loginDto = new UserLoginDto("usuario@academia.com", "senha123");

        User userSpy = spy(sampleUser);
        doReturn(true).when(userSpy).isPasswordCorrect(eq("senha123"), any());

        when(userRepository.findByEmail(loginDto.getEmail())).thenReturn(Optional.of(userSpy));

        Jwt mockJwt = mock(Jwt.class);
        when(mockJwt.getTokenValue()).thenReturn("token_jwt_gerado_com_sucesso");
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(mockJwt);

        String token = userService.login(loginDto, 3600L);

        assertNotNull(token);
        assertEquals("token_jwt_gerado_com_sucesso", token);
    }
    @Test
    @DisplayName("Should throw BadCredentialsException when password is incorrect")
    void shouldThrowExceptionWhenPasswordIsIncorrect() {

        UserLoginDto loginDto = new UserLoginDto("usuario@academia.com", "senha_errada");
        User userSpy = spy(sampleUser);

        doReturn(false).when(userSpy).isPasswordCorrect(eq("senha_errada"), any());

        when(userRepository.findByEmail(loginDto.getEmail())).thenReturn(Optional.of(userSpy));


        assertThrows(BadCredentialsException.class, () -> {
            userService.login(loginDto, 3600L);
        });

        verify(jwtEncoder, never()).encode(any());
    }

    @Test
    void findByEmail() {

        String email = "usuario@academia.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(sampleUser));

        User resultado = userService.findByEmail(email);


        assertNotNull(resultado);
        assertEquals(email, resultado.getEmail());
    }

    @Test
    void register() {

        UserRegisterRequest request = new UserRegisterRequest();
        request.setCompleteName("João Silva");
        request.setEmail("joao@academia.com");
        request.setCpf("111.222.333-44");
        request.setAddress(new Address());
        request.getAddress().setCep("40000-012");
        request.getAddress().setCity("Fortaleza");
        request.getAddress().setStreet("Rua domingos ");


        userService.register(request);

       verify(userRepository, times(1)).save(any(User.class));
    }


    @Test
    void generateMatricula() {

        LocalDate hoje = LocalDate.now();
        String anoMesAtual = hoje.getYear() + String.format("%02d", hoje.getMonthValue());


        sampleUser.setCpf("12345678900");
        String finalDoCpfEsperado = "8900";
        String matriculaEsperada = anoMesAtual + finalDoCpfEsperado;

        String matriculaGerada = userService.generateMatricula(sampleUser);

        assertEquals(matriculaEsperada, matriculaGerada);
    }
}