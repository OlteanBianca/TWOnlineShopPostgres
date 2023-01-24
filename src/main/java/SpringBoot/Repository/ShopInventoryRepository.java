package SpringBoot.Repository;

import SpringBoot.Models.ShopInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopInventoryRepository extends JpaRepository<ShopInventory, Long> {

    List<ShopInventory> findAllByShopId(Long id);

    ShopInventory findByProductIdAndShopId(Long productId, Long shopId);

    List<ShopInventory> findAllByShopIdAndProductNameContaining(Long id, String text);
}
