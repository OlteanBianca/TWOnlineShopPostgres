package SpringBoot.Repository;

import SpringBoot.Models.ShopFunction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopFunctionRepository extends JpaRepository<ShopFunction, Long> {

    ShopFunction findShopFunctionById(Long id);
}
