package studios.tkoh.chattide;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ChattideApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChattideApplication.class, args);
    }

}
