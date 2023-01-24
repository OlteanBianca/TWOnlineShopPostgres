package SpringBoot.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "ShoppingCart")
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @ToString.Exclude
    private User user;

    @OneToMany(mappedBy = "shoppingCart")
    @JsonIgnore
    @ToString.Exclude
    private Collection<ShoppingCartProducts> shoppingCartProducts;

    public ShoppingCart(User user) {
        this.user = user;
    }
}
