package org.sidis.userservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(properties = "spring.profiles.active=instance1")
class UserServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
