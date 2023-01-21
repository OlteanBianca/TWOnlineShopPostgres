package SpringBoot.Repository;

import SpringBoot.Models.Command;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommandRepository extends JpaRepository<Command, Long> {

    List<Command> findAllByShopIdAndAcceptedFalse(Long id);

    List<Command> findAllByShopId(Long id);
}
