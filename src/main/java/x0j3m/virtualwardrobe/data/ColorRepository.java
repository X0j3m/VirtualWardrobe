package x0j3m.virtualwardrobe.data;

import org.springframework.data.repository.CrudRepository;
import x0j3m.virtualwardrobe.model.Color;

import java.util.Optional;

public interface ColorRepository extends CrudRepository<Color, Long> {
    Optional<Color> findByName(String name);

    void deleteByName(String name);
}
