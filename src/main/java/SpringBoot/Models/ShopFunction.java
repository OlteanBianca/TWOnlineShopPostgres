package SpringBoot.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Getter
@Setter
@ToString
@Table(name = "ShopFunction")
public class ShopFunction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "shopFunction", nullable = false)
    private String shopFunction;

    @OneToMany(mappedBy = "shopFunction")
    @JsonIgnore
    @ToString.Exclude
    private Collection<Shop> shops;
}
