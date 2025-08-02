package x0j3m.virtualwardrobe.service;

import org.springframework.stereotype.Service;
import x0j3m.virtualwardrobe.data.ColorRepository;
import x0j3m.virtualwardrobe.model.Color;

@Service
public class ColorService {
    private final ColorRepository colorRepository;

    public ColorService(ColorRepository colorRepository) {
        this.colorRepository = colorRepository;
    }

    public Long saveColor(Color color) throws IllegalArgumentException {
        if (color == null) {
            throw new IllegalArgumentException("Color cannot be null");
        }
        if (color.getName() == null || color.getName().isEmpty()) {
            throw new IllegalArgumentException("Color name cannot be null or empty");
        }
        if (colorRepository.findByName(color.getName()).isPresent()) {
            throw new IllegalArgumentException("Color already exists");
        }

        Color savedColor = colorRepository.save(color);
        return savedColor.getId();
    }

    public Long saveColor(String colorName) throws IllegalArgumentException {
        return saveColor(new Color(colorName));
    }

    public Color getColor(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Color id cannot be null");
        }
        if (id < 1) {
            throw new IllegalArgumentException("Color id must be greater than 0");
        }
        return colorRepository.findById(id).orElse(null);
    }

    public Color getColor(String colorName) throws IllegalArgumentException {
        if (colorName == null || colorName.isEmpty()) {
            throw new IllegalArgumentException("Color name cannot be null or empty");
        }
        return colorRepository.findByName(colorName).orElse(null);
    }

    public Iterable<Color> getAllColors() {
        return colorRepository.findAll();
    }

    public void deleteColor(Long id) throws IllegalArgumentException {
        if (id == null) {
            throw new IllegalArgumentException("Color id cannot be null");
        }
        if (id < 1) {
            throw new IllegalArgumentException("Color id must be greater than 0");
        }
        if (colorRepository.findById(id).isPresent()) {
            colorRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Color with id " + id + " does not exist");
        }
    }

    public Color updateColorName(Long id, String colorName) throws IllegalArgumentException {
        if (id == null) {
            throw new IllegalArgumentException("Color id cannot be null");
        }
        if (id < 1) {
            throw new IllegalArgumentException("Color id must be greater than 0");
        }
        if (colorName == null) {
            throw new IllegalArgumentException("Color name cannot be null");
        }

        colorRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Color with id " + id + " does not exist"));
        Color updatedColor = new Color(id, colorName);
        return colorRepository.save(updatedColor);
    }
}
