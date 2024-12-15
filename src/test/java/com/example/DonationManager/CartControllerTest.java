package com.example.DonationManager;

import com.example.DonationManager.model.AppUser;
import com.example.DonationManager.model.Cart;
import com.example.DonationManager.model.Item;
import com.example.DonationManager.model.Request;
import com.example.services.ProductService;
import com.example.services.RequestService;
import com.example.DonationManager.repository.ProductRepository;
import com.example.DonationManager.repository.RequestRepository;
import com.example.DonationManager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private RequestService requestService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RequestRepository reqRepository;

    @MockBean
    private ProductRepository productRepository;

    @BeforeEach
    public void setUp() {
        // Mock setup code here
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = "USER")  
    public void testAddItemToCart() throws Exception {
        Item item = new Item();
        item.setId(1L);
        item.setTitle("Product 1");

        when(productService.findById(1L)).thenReturn(item);

        mockMvc.perform(MockMvcRequestBuilders.post("/cart/add")
                .param("productId", "1"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/cart"))
               .andExpect(flash().attribute("message", "Vous avez ajouté un produit :)"));
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = "USER")  
    public void testAddItemToCartAlreadyExists() throws Exception {
        Item item = new Item();
        item.setId(1L);
        item.setTitle("Product 1");

        when(productService.findById(1L)).thenReturn(item);

        Cart cart = new Cart();
        cart.addItem(item);

        mockMvc.perform(MockMvcRequestBuilders.post("/cart/add")
                .param("productId", "1")
                .sessionAttr("cart", cart))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"))
                .andExpect(flash().attribute("messageErr", "ce produit est deja existe :)"));
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = "USER")
    public void testRemoveItemFromCart() throws Exception {
        Cart cart = new Cart();
        Item item = new Item();
        item.setId(1L);
        item.setTitle("Product 1");

        cart.addItem(item);

        mockMvc.perform(MockMvcRequestBuilders.get("/cart/1/remove")
                .sessionAttr("cart", cart))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/cart"))
               .andExpect(flash().attribute("message", "Element supprime avec succes"));
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = "USER")  
    public void testConfirmRequestWithEmptyCart() throws Exception {
        Cart cart = new Cart();

        mockMvc.perform(MockMvcRequestBuilders.post("/cart/confirmRequest")
                .sessionAttr("cart", cart))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/cart"))
               .andExpect(flash().attribute("messageErr", "Your cart is empty."));
    }
  
    @Test
    @WithMockUser(username = "test@example.com", roles = "USER")  
    public void testConfirmRequestSuccess() throws Exception {
        Cart cart = new Cart();
        Item item = new Item();
        item.setId(1L);
        item.setTitle("Product 1");

        cart.addItem(item);
        when(requestService.findByRequesterAndItemAndStatus(any(AppUser.class), any(Item.class), any(String.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.post("/cart/confirmRequest")
                .sessionAttr("cart", cart))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/cart"))
               .andExpect(flash().attribute("message", "Vous avez valider votre demande avec succes en attendant la confiramtion du propietaire :)"));
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = "USER")  
    public void testRejectRequest() throws Exception {
        Request request = new Request();
        request.setStatus("PENDING");
        when(reqRepository.findById(1L)).thenReturn(Optional.of(request));

        mockMvc.perform(MockMvcRequestBuilders.get("/cart/refuserRequest/1"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/user/1?category=3"));
    }
}
