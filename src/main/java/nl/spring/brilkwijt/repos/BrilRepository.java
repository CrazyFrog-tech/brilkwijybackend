package nl.spring.brilkwijt.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import nl.spring.brilkwijt.repos.dto.Bril;
@Repository
public interface BrilRepository extends JpaRepository<Bril, Long> {

}
