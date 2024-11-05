package org.sidis.lendingservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(properties = "spring.profiles.active=instance1")
class LendingServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
