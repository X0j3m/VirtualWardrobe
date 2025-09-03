package x0j3m.virtualwardrobe.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import x0j3m.virtualwardrobe.model.Color;

import java.util.Optional;

@Repository
public interface ColorRepository extends CrudRepository<Color, Long>, PagingAndSortingRepository<Color, Long> {
    Optional<Color> findByName(String name);

    void deleteByName(String name);
}
