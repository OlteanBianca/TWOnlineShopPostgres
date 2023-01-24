package SpringBoot.Repository;

import SpringBoot.Models.ShoppingCartProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ShoppingCartProductsRepository extends JpaRepository<ShoppingCartProducts, Long> {

    List<ShoppingCartProducts> findAllByShoppingCartId(Long id);

    @Transactional
    void removeById(Long id);

    @Transactional
    void removeAllByShoppingCartId(Long id);
}
