package shop.mtcoding.securityapp.env;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class EnvVarTest {

    @Value("${meta.name}")
    private String name;

    @Test
    public void _test() throws Exception {
        System.out.println("테스트 : " + name);
    }

    @Test
    public void secret_test() {
        String key = System.getenv("HS512_SECRET");
        System.out.println("테스트 : " + key);
    }
}
