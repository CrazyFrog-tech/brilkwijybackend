package nl.spring.brilkwijt.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import nl.spring.brilkwijt.dto.Bril;
@Repository
public interface BrilRepository extends JpaRepository<Bril, Long> {

}
