package x0j3m.virtualwardrobe.web;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import x0j3m.virtualwardrobe.model.Color;
import x0j3m.virtualwardrobe.service.ColorService;

import java.net.URI;

@RestController
@RequestMapping("/colors")
public class ColorController {
    private final ColorService colorService;

    public ColorController(ColorService colorService) {
        this.colorService = colorService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Color> getColor(@PathVariable Long id) {
        try {
            Color color = colorService.getColor(id);
            if (color == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(color);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<Iterable<Color>> getAllColors(Pageable pageable) {
        Iterable<Color> colors = colorService.getAllColors(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort()
        );
        if (colors.iterator().hasNext()) {
            return ResponseEntity.ok(colors);
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Void> saveColor(@RequestBody Color color) {
        try {
            Long savedId = colorService.saveColor(color);
            URI location = URI.create("/colors/" + savedId);
            return ResponseEntity.created(location).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteColor(@PathVariable Long id) {
        try {
            colorService.deleteColor(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("{id}")
    public ResponseEntity<Void> updateColor(@PathVariable Long id, @RequestBody Color update) {
        try {
            if (colorService.getColor(id) != null) {
                colorService.updateColor(id, update);
                URI location = URI.create("/colors/" + id);
                return ResponseEntity.created(location).build();
            }
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
