package nl.spring.brilkwijt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import nl.spring.brilkwijt.repos.BrilRepository;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = BrilRepository.class)
public class BrilkwijtApplication {

	public static void main(String[] args) {
		SpringApplication.run(BrilkwijtApplication.class, args);
	}

}
