package x0j3m.virtualwardrobe.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Entity
@Table(name = "colors")
public class Color {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;
    @Column(name = "name", nullable = false, unique = true)
    private final String name;

    public Color(String name) {
        this.id = null;
        this.name = name;
    }

    public static Color merge(Color base, Color update) {
        Long id = update.getId() == null ? base.id : update.getId();
        String name = update.getName() == null ? base.name : update.getName();
        return new Color(id, name);
    }
}
