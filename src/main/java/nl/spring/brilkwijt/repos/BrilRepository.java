package nl.spring.brilkwijt.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import nl.spring.brilkwijt.dto.Bril;

public interface BrilRepository extends JpaRepository<Bril, Long> {

}
