package x0j3m.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(force = true)
@Entity
@Table(name = "CLOTHES_TYPE")
public class ClothesType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;
    @Column(name = "TYPE_NAME", nullable = false, unique = true)
    private final String name;
    @Column(name = "LAYER", nullable = false)
    private final ClothesLayer layer;

    public ClothesType(String name, ClothesLayer layer) {
        this.id = null;
        this.name = name;
        this.layer = layer;
    }
}
