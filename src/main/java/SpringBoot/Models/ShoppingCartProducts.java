package SpringBoot.Models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "ShoppingCartProducts")
public class ShoppingCartProducts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "quantity", nullable = false)
    private int quantity;

    @ManyToOne(fetch = FetchType.EAGER)
    private ShoppingCart shoppingCart;

    @ManyToOne(fetch = FetchType.EAGER)
    private Shop shop;

    @ManyToOne(fetch = FetchType.EAGER)
    private Product product;


    public ShoppingCartProducts(int quantity, Shop shop, Product product, ShoppingCart shoppingCart) {
        this.product = product;
        this.shop = shop;
        this.quantity = quantity;
        this.shoppingCart = shoppingCart;
    }
}
