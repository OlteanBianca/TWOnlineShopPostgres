package SpringBoot.Repository;

import SpringBoot.Models.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {

    List<Shop> findAllByShopFunctionId(Long id);

    Shop findByUserId(Long id);
}
