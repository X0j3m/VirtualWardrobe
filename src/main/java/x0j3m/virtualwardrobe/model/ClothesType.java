package x0j3m.virtualwardrobe.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Entity
@Table(name = "clothes_types")
public class ClothesType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;
    @Column(name = "name", nullable = false, unique = true)
    private final String name;
    @Enumerated(EnumType.STRING)
    @Column(name = "layer", nullable = false)
    private final ClothesLayer layer;

    public ClothesType(String name, ClothesLayer layer) {
        this.id = null;
        this.name = name;
        this.layer = layer;
    }

    public static ClothesType merge(ClothesType base, ClothesType update) {
        Long id = update.getId() == null ? base.id : update.getId();
        String name = update.getName() == null ? base.name : update.getName();
        ClothesLayer layer = update.getLayer() == null ? base.layer : update.getLayer();
        return new ClothesType(id, name, layer);
    }
}
