package x0j3m.virtualwardrobe.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import x0j3m.virtualwardrobe.model.ClothesType;

import java.util.Optional;

@Repository
public interface ClothesTypeRepository extends CrudRepository<ClothesType, Long> {
    Optional<ClothesType> findByName(String name);
    void deleteByName(String name);
}
