package com.example.DonationManager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest(classes = DonationManagerApplication.class)
class DonationManagerApplicationTests {

	@Test
	void contextLoads() {
	}
	@Import(TestMailConfig.class) 
	public class AuthControllerTest {
	}

}
