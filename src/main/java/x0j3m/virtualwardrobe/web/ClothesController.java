package x0j3m.virtualwardrobe.web;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import x0j3m.virtualwardrobe.model.ClothesType;
import x0j3m.virtualwardrobe.model.Color;
import x0j3m.virtualwardrobe.service.ClothesTypeService;
import x0j3m.virtualwardrobe.web.dto.ClothesRequestDTO;
import x0j3m.virtualwardrobe.model.Clothes;
import x0j3m.virtualwardrobe.service.ClothesService;

import java.net.URI;

@RestController
@RequestMapping("/clothes")
public class ClothesController {
    private final ClothesService clothesService;
    private final ClothesTypeController clothesTypeController;
    private final ColorController colorController;

    public ClothesController(ClothesService clothesService,
                             ClothesTypeController clothesTypeController,
                             ColorController colorController) {
        this.clothesService = clothesService;
        this.clothesTypeController = clothesTypeController;
        this.colorController = colorController;
    }

    @GetMapping("{id}")
    public ResponseEntity<Clothes> getClothes(@PathVariable Long id) {
        try {
            Clothes clothes = clothesService.getClothes(id);
            if (clothes == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(clothes);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<Iterable<Clothes>> getAllClothes(Pageable pageable) {
        try {
            Iterable<Clothes> clothes = clothesService
                    .getAllClothes(
                            pageable.getPageNumber(),
                            pageable.getPageSize(),
                            pageable.getSort()
                    );
            if (clothes.iterator().hasNext()) {
                return ResponseEntity.ok(clothes);
            }
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Void> saveClothes(@RequestBody ClothesRequestDTO request) {
        try {
            Long savedId = clothesService.saveClothes(
                    request.colorId(), request.typeId()
            );
            URI location = URI.create("/clothes/" + savedId);
            return ResponseEntity.created(location).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteClothes(@PathVariable Long id) {
        try {
            clothesService.deleteClothes(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("{id}")
    public ResponseEntity<Void> updateClothes(@PathVariable Long id, @RequestBody ClothesRequestDTO update) {
        try {
            if (clothesService.getClothes(id) != null) {
                Clothes clothesUpdate = clothesDTOToClothes(update);

                clothesService.updateClothes(id, clothesUpdate);

                URI location = URI.create("/clothes/" + id);
                return ResponseEntity.created(location).build();
            }
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private Clothes clothesDTOToClothes(ClothesRequestDTO dto) {
        Color color;
        ClothesType clothesType;

        Long colorId = dto.colorId();
        Long typeId = dto.typeId();

        if (colorId == null) {
            color = null;
        } else {
            HttpStatus status = (HttpStatus) colorController.getColor(colorId).getStatusCode();
            if (status != HttpStatus.OK) {
                color = colorController.getColor(colorId).getBody();
            } else {
                color = null;
            }
        }

        if (typeId == null) {
            clothesType = null;
        } else {
            HttpStatus status = (HttpStatus) clothesTypeController.getClothesType(typeId).getStatusCode();
            if (status != HttpStatus.OK) {
                clothesType = clothesTypeController.getClothesType(typeId).getBody();
            } else {
                clothesType = null;
            }
        }

        return new Clothes(color, clothesType);
    }
}
