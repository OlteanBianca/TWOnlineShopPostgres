package SpringBoot.Repository;

import SpringBoot.Models.CommandProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommandProductsRepository extends JpaRepository<CommandProducts, Long> {

    List<CommandProducts> findAllByCommandId(Long id);
}
