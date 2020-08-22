package pl.promotion.finder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VirtutiCebulariApplication {
    public static void main(String[] args) {
        SpringApplication.run(VirtutiCebulariApplication.class, args);
    }

}
