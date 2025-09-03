package x0j3m.virtualwardrobe.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import x0j3m.virtualwardrobe.model.Clothes;

@Repository
public interface ClothesRepository extends CrudRepository<Clothes, Long>, PagingAndSortingRepository<Clothes, Long> {
    Iterable<Clothes> findByColor_Name(String colorName);

    Iterable<Clothes> findByType_Name(String typeName);
}
