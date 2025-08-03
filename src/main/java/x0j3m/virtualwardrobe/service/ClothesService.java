package x0j3m.virtualwardrobe.service;

import org.springframework.stereotype.Service;
import x0j3m.virtualwardrobe.data.ClothesRepository;
import x0j3m.virtualwardrobe.model.Clothes;
import x0j3m.virtualwardrobe.model.Color;

@Service
public class ClothesService {
    private final ClothesRepository clothesRepository;
    private final ColorService colorService;
    private final ClothesTypeService clothesTypeService;

    public ClothesService(ClothesRepository clothesRepository,
                          ColorService colorService,
                          ClothesTypeService clothesTypeService) {
        this.clothesRepository = clothesRepository;
        this.colorService = colorService;
        this.clothesTypeService = clothesTypeService;
    }

    public Long saveClothes(Long colorId, Long typeId) throws IllegalArgumentException {
        if (colorId == null || typeId == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        if (colorId < 1 || typeId < 1) {
            throw new IllegalArgumentException("Id must be greater than 0");
        }
        if (colorService.getColor(colorId) == null || clothesTypeService.getClothesType(typeId) == null) {
            throw new IllegalArgumentException("Color or type does not exist");
        }
        try {
            Clothes clothes = new Clothes(colorService.getColor(colorId), clothesTypeService.getClothesType(typeId));
            return clothesRepository.save(clothes).getId();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public Clothes getClothes(Long id) throws IllegalArgumentException {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        if (id < 1) {
            throw new IllegalArgumentException("Id must be greater than 0");
        }
        return clothesRepository.findById(id).orElse(null);
    }

    public Iterable<Clothes> getAllClothes() {
        return clothesRepository.findAll();
    }

    public Iterable<Clothes> getClothesByColor(String colorName) {
        if (colorName == null || colorName.isEmpty()) {
            throw new IllegalArgumentException("Color name cannot be null or empty");
        }
        return clothesRepository.findByColor_Name(colorName);
    }

    public Iterable<Clothes> getClothesByType(String typeName) {
        if (typeName == null || typeName.isEmpty()) {
            throw new IllegalArgumentException("ClothesType name cannot be null or empty");
        }
        return clothesRepository.findByType_Name(typeName);
    }

    public void deleteClothes(Long id) throws IllegalArgumentException {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        if (id < 1) {
            throw new IllegalArgumentException("Id must be greater than 0");
        }
        if (clothesRepository.findById(id).isPresent()) {
            clothesRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Clothes with id " + id + " does not exist");
        }
    }

    public Clothes updateClothesColor(Long id, Long colorId) throws IllegalArgumentException {
        if (id == null || colorId == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        if (id < 1 || colorId < 1) {
            throw new IllegalArgumentException("Id must be greater than 0");
        }
        try {
            colorService.getColor(colorId);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Color does not exist");
        }
        if (clothesRepository.findById(id).isPresent()) {
            Clothes updatedClothes = new Clothes(
                    id,
                    colorService.getColor(colorId),
                    clothesRepository.findById(id).get().getType()
            );
            return clothesRepository.save(updatedClothes);
        } else {
            throw new IllegalArgumentException("Clothes with id " + id + " does not exist");
        }
    }

    public Clothes updateClothesType(Long id, Long typeId) throws IllegalArgumentException {
        if (id == null || typeId == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        if (id < 1 || typeId < 1) {
            throw new IllegalArgumentException("Id must be greater than 0");
        }
        try {
            clothesTypeService.getClothesType(typeId);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        if (clothesRepository.findById(id).isPresent()) {
            Clothes updatedClothes = new Clothes(
                    id,
                    clothesRepository.findById(id).get().getColor(),
                    clothesTypeService.getClothesType(typeId)
            );
            return clothesRepository.save(updatedClothes);
        } else {
            throw new IllegalArgumentException("Clothes with id " + id + " does not exist");
        }
    }
}
