package learn.tdd.infra.repository;

import learn.tdd.domain.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SellerRepository extends JpaRepository<Seller,Long> {

    boolean existsByEmailOrUsername(String email,String username);

    Optional<Seller> findByEmail(String email);

    Optional<Seller> findById(UUID uuid);
}
