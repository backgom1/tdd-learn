package learn.tdd.infra.repository;

import learn.tdd.domain.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller,Long> {

    boolean existsByEmailOrUsername(String email,String username);
}
