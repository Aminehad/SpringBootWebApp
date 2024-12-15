package com.example.DonationManager;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import com.example.DonationManager.model.AppUser;
import com.example.services.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)  // Refresh context after each test method
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JavaMailSender javaMailSender;  // Mock JavaMailSender to avoid dependency issues


    // Test: Show login page
    @Test
    public void testShowLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
               .andExpect(status().isOk())
               .andExpect(view().name("login"));
    }

    // Test: Show register page
    @Test
    public void testShowRegisterPage() throws Exception {
        mockMvc.perform(get("/register"))
               .andExpect(status().isOk())
               .andExpect(view().name("register"));
    }

    // Test: Register user with an existing email
    @Test
    public void testProcessRegistrationUserAlreadyExists() throws Exception {
        AppUser existingUser = new AppUser();
        existingUser.setEmail("existing@example.com");
        existingUser.setName("Existing User");

        
        when(userService.findByEmail("existing@example.com")).thenReturn(Optional.of(existingUser));

        mockMvc.perform(post("/register")
                .param("email", "existing@example.com")
                .param("name", "Existing User"))
               .andExpect(status().isOk())
               .andExpect(view().name("register"))
               .andExpect(model().attribute("messageError", "User with this email already exists!"));
    }

    // Test: Register a new user successfully
    @Test
    public void testProcessRegistrationSuccess() throws Exception {
        AppUser newUser = new AppUser();
        newUser.setEmail("new@example.com");
        newUser.setName("New User");

       
        when(userService.findByEmail("new@example.com")).thenReturn(Optional.empty());
        when(userService.saveUser(any(AppUser.class))).thenReturn(newUser);

        mockMvc.perform(post("/register")
                .param("email", "new@example.com")
                .param("name", "New User"))
            .andExpect(status().is3xxRedirection())  
            .andExpect(redirectedUrl("/login?messageSuccess=vous pouvez authentifier avec votre coordonne"));
    }
}
