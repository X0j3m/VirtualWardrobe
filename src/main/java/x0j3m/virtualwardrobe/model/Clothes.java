package x0j3m.virtualwardrobe.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Entity
@Table(name = "clothes")
public class Clothes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;
    @ManyToOne
    @JoinColumn(name = "color_id")
    private final Color color;
    @ManyToOne
    @JoinColumn(name = "type_id")
    private final ClothesType type;

    public Clothes(Color color, ClothesType type) {
        this.id = null;
        this.color = color;
        this.type = type;
    }
}
