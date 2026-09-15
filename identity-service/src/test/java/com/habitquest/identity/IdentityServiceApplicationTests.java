package com.habitquest.identity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
        properties = "JWT_SECRET=0123456789abcdef0123456789abcdef"
)
class IdentityServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
