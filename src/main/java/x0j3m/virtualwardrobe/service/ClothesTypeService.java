package x0j3m.virtualwardrobe.service;

import org.springframework.stereotype.Service;
import x0j3m.virtualwardrobe.data.ClothesTypeRepository;
import x0j3m.virtualwardrobe.model.ClothesLayer;
import x0j3m.virtualwardrobe.model.ClothesType;

@Service
public class ClothesTypeService {
    private final ClothesTypeRepository clothesTypeRepository;

    public ClothesTypeService(ClothesTypeRepository clothesTypeRepository) {
        this.clothesTypeRepository = clothesTypeRepository;
    }

    public Long saveClothesType(ClothesType clothesType) throws IllegalArgumentException {
        if (clothesType == null) {
            throw new IllegalArgumentException("ClothesType cannot be null");
        }
        if (clothesType.getName() == null || clothesType.getName().isEmpty()) {
            throw new IllegalArgumentException("ClothesType name cannot be null or empty");
        }
        if (clothesType.getLayer() == null) {
            throw new IllegalArgumentException("ClothesType layer cannot be null");
        }
        if (clothesTypeRepository.findByName(clothesType.getName()).isPresent()) {
            throw new IllegalArgumentException("ClothesType named " + clothesType.getName() + " already exists");
        }

        ClothesType savedClothesType = clothesTypeRepository.save(clothesType);
        return savedClothesType.getId();
    }

    public Long saveClothesType(String clothesTypeName, ClothesLayer clothesTypeLayer) throws IllegalArgumentException {
        return saveClothesType(new ClothesType(clothesTypeName, clothesTypeLayer));
    }

    public ClothesType getClothesType(Long id) throws IllegalArgumentException {
        if (id == null) {
            throw new IllegalArgumentException("ClothesType id cannot be null");
        }
        if (id < 1) {
            throw new IllegalArgumentException("ClothesType id must be greater than 0");
        }
        return clothesTypeRepository.findById(id).orElse(null);
    }

    public ClothesType getClothesType(String clothesTypeName) throws IllegalArgumentException {
        if (clothesTypeName == null || clothesTypeName.isEmpty()) {
            throw new IllegalArgumentException("ClothesType name cannot be null or empty");
        }
        return clothesTypeRepository.findByName(clothesTypeName).orElse(null);
    }

    public Iterable<ClothesType> getAllClothesTypes() {
        return clothesTypeRepository.findAll();
    }

    public void deleteClothesType(Long id) throws IllegalArgumentException {
        if (id == null) {
            throw new IllegalArgumentException("ClothesType id cannot be null");
        }
        if (id < 1) {
            throw new IllegalArgumentException("ClothesType id must be greater than 0");
        }
        if (clothesTypeRepository.findById(id).isPresent()) {
            clothesTypeRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("ClothesType with id " + id + " does not exist");
        }
    }

    public ClothesType updateClothesTypeName(Long id, String clothesTypeName) throws IllegalArgumentException {
        if (id == null) {
            throw new IllegalArgumentException("ClothesType id cannot be null");
        }
        if (id < 1) {
            throw new IllegalArgumentException("ClothesType id must be greater than 0");
        }
        if (clothesTypeName == null) {
            throw new IllegalArgumentException("ClothesType name cannot be null");
        }
        if (clothesTypeName.isEmpty()) {
            throw new IllegalArgumentException("ClothesType name cannot be empty");
        }
        if (clothesTypeRepository.findByName(clothesTypeName).isPresent()) {
            throw new IllegalArgumentException("ClothesType named " + clothesTypeName + " already exists");
        }
        if (clothesTypeRepository.findById(id).isPresent()) {
            ClothesType updated = new ClothesType(
                    id,
                    clothesTypeName,
                    clothesTypeRepository.findById(id).get().getLayer()
            );
            return clothesTypeRepository.save(updated);
        } else {
            throw new IllegalArgumentException("ClothesType with id " + id + " does not exist");
        }
    }

    public ClothesType updateClothesTypeLayer(Long id, ClothesLayer clothesTypeLayer) throws IllegalArgumentException {
        if (id == null) {
            throw new IllegalArgumentException("ClothesType id cannot be null");
        }
        if (id < 1) {
            throw new IllegalArgumentException("ClothesType id must be greater than 0");
        }
        if (clothesTypeLayer == null) {
            throw new IllegalArgumentException("ClothesType layer cannot be null");
        }
        if (clothesTypeRepository.findById(id).isPresent()) {
            ClothesType updated = new ClothesType(
                    id,
                    clothesTypeRepository.findById(id).get().getName(),
                    clothesTypeLayer
            );
            return clothesTypeRepository.save(updated);
        } else {
            throw new IllegalArgumentException("ClothesType with id " + id + " does not exist");
        }
    }
}
