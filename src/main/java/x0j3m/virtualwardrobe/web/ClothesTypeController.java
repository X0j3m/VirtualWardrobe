package x0j3m.virtualwardrobe.web;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import x0j3m.virtualwardrobe.model.ClothesType;
import x0j3m.virtualwardrobe.service.ClothesTypeService;

import java.net.URI;

@RestController
@RequestMapping("/clothes-types")
public class ClothesTypeController {
    private final ClothesTypeService clothesTypeService;

    public ClothesTypeController(ClothesTypeService clothesTypeService) {
        this.clothesTypeService = clothesTypeService;
    }

    @GetMapping("{id}")
    public ResponseEntity<ClothesType> getClothesType(@PathVariable Long id) {
        try {
            ClothesType clothesType = clothesTypeService.getClothesType(id);
            if (clothesType == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(clothesType);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<Iterable<ClothesType>> getAllClothesTypes(Pageable pageable) {
        try {
            Iterable<ClothesType> clothesTypes = clothesTypeService.getAllClothesTypes(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    pageable.getSort()
            );
            if (clothesTypes.iterator().hasNext()) {
                return ResponseEntity.ok(clothesTypes);
            }
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Void> saveClothesType(@RequestBody ClothesType clothesType) {
        try {
            Long savedId = clothesTypeService.saveClothesType(clothesType);
            URI location = URI.create("/clothes-types/" + savedId);
            return ResponseEntity.created(location).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteClothesType(@PathVariable Long id) {
        try {
            clothesTypeService.deleteClothesType(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("{id}")
    public ResponseEntity<Void> updateClothesType(@PathVariable Long id, @RequestBody ClothesType update) {
        try {
            if (clothesTypeService.getClothesType(id) != null) {
                clothesTypeService.updateClothesType(id, update);
                URI location = URI.create("/clothes-types/" + id);
                return ResponseEntity.created(location).build();
            }
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
