package com.Gym.Service;

import com.Gym.Dto.Personal.Auth.PersonalLoginDto;
import com.Gym.Dto.Personal.Auth.PersonalRegisterRequest;
import com.Gym.Dto.User.Auth.UserLoginDto;
import com.Gym.Exception.ObjectNotFound;
import com.Gym.Model.Users_Models.Personal.Personal;
import com.Gym.Repository.Personal.PersonalRepository;
import com.nimbusds.jose.shaded.gson.Strictness;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password4j.Argon2Password4jPasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static com.nimbusds.jose.shaded.gson.Strictness.LENIENT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = org.mockito.quality.Strictness.LENIENT)
@DisplayName("PersonalService Unit Tests")
class PersonalServiceTest {

    @Mock private PersonalRepository personalRepository;
    @Mock
    private JwtEncoder jwtEncoder;
    @Mock private Argon2Password4jPasswordEncoder passwordEncoder;

    @InjectMocks
    private PersonalService personalService;

    private Personal samplePersonal;
    private UUID personalId;

    @BeforeEach
    void setUp() {
        personalId = UUID.randomUUID();
        samplePersonal = new Personal();
        samplePersonal.setId(personalId);
        samplePersonal.setEmail("personal@academia.com");
        samplePersonal.setCref("123456");
        samplePersonal.setRoles(Collections.emptySet());
    }

    @Nested
    @DisplayName("Registration Operations (Cadastro)")
    class RegisterOperations {

        @Test
        @DisplayName("Should successfully persist personal when data is valid")
        void shouldRegisterPersonalWithSuccess() {

            PersonalRegisterRequest request = createRegisterRequestSample();
            when(personalRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());


            personalService.register(request);

            // Then
            verify(personalRepository).save(any(Personal.class));
        }

        @Test
        @DisplayName(" Should throw BusinessException when email is already registered")
        void shouldThrowExceptionWhenEmailAlreadyExists() {

            PersonalRegisterRequest request = createRegisterRequestSample();
            when(personalRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(samplePersonal));


            assertThrows(RuntimeException.class, () -> {
                personalService.register(request);
            });

            verify(personalRepository, never()).save(any(Personal.class));
        }

        @Test
        @DisplayName(" Should throw BusinessException when CREF/Registration code already exists")
        void shouldThrowExceptionWhenCrefAlreadyExists() {

            PersonalRegisterRequest request = createRegisterRequestSample();
            when(personalRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

           when(personalRepository.findByCref(request.getCref())).thenReturn(Optional.of(samplePersonal));


            assertThrows(RuntimeException.class, () -> {
                personalService.register(request);
            });

            verify(personalRepository, never()).save(any(Personal.class));
        }
    }

    @Nested
    @DisplayName("Authentication Operations (Login)")
    class LoginOperations {

        @Test
        @DisplayName("Should authenticate successfully and return JWT token when credentials are valid")
        void shouldAuthenticateAndReturnToken() {

            PersonalLoginDto loginDto = new PersonalLoginDto("personal@academia.com", "senhaPersonal123","123456");
            Personal personalSpy = spy(samplePersonal);

            doReturn(true).when(personalSpy).isPasswordCorrect(eq("senhaPersonal123"), any());

            when(personalRepository.findByEmail(loginDto.getEmail())).thenReturn(Optional.of(personalSpy));
            when(personalRepository.findByCref(loginDto.getCref())).thenReturn(Optional.of(personalSpy)); // <--- ADICIONE ESTA LINHA

            Jwt mockJwt = mock(Jwt.class);
            when(mockJwt.getTokenValue()).thenReturn("token_jwt_personal_sucesso");
            when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(mockJwt);

            String token = personalService.login(loginDto, 3600L);

            assertNotNull(token);
            assertEquals("token_jwt_personal_sucesso", token);
            verify(jwtEncoder).encode(any(JwtEncoderParameters.class));}

        @Test
        @DisplayName(" Should throw ResourceNotFoundException when personal email does not exist")
        void shouldThrowExceptionWhenEmailNotFound() {

            PersonalLoginDto loginDto = new PersonalLoginDto("inexistente@academia.com", "senha123" , "123456");
            when(personalRepository.findByEmail(loginDto.getEmail())).thenReturn(Optional.empty());


            assertThrows(ObjectNotFound.class, () -> {
                personalService.login(loginDto, 3600L);
            });

            verify(jwtEncoder, never()).encode(any());
        }

        @Test
        @DisplayName(" Should throw BadCredentialsException when password is incorrect")
        void shouldThrowExceptionWhenPasswordIsIncorrect() {

            PersonalLoginDto loginDto = new PersonalLoginDto("personal@academia.com", "senha_errada","123456");
            Personal personalSpy = spy(samplePersonal);


            doReturn(false).when(personalSpy).isPasswordCorrect(eq("senha_errada"), any());

            when(personalRepository.findByEmail(loginDto.getEmail())).thenReturn(Optional.of(personalSpy));
            when(personalRepository.findByCref(loginDto.getCref())).thenReturn(Optional.of(personalSpy));


            when(personalRepository.findByEmail(loginDto.getEmail())).thenReturn(Optional.of(personalSpy));

           when(personalRepository.findByCref(loginDto.getCref())).thenReturn(Optional.of(samplePersonal));


            assertThrows(BadCredentialsException.class, () -> {
                personalService.login(loginDto, 3600L);
            });

            verify(jwtEncoder, never()).encode(any(JwtEncoderParameters.class));
        }
    }



    private PersonalRegisterRequest createRegisterRequestSample() {
        PersonalRegisterRequest request = new PersonalRegisterRequest();
        request.setCompleteName("Gabriel Personal");
        request.setEmail("personal@academia.com");
        request.setCref("123456");
        request.setPassword("senhaPersonal123");
        return request;
    }
}