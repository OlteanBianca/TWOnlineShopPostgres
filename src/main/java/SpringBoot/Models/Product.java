package SpringBoot.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Date;
import java.util.Collection;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "Product", schema = "public", catalog = "SpringBoot")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "name", nullable = false)
    private String name;

    @Basic
    @Column(name = "description")
    private String description;

    @Basic
    @Column(name = "expirationDate")
    private Date expirationDate;

    @Transient
    public int quantity = 0;

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    @ToString.Exclude
    private Collection<ShopInventory> shopInventory;

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    @ToString.Exclude
    private Collection<CommandProducts> commands;

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    @ToString.Exclude
    private Collection<ShoppingCartProducts> shoppingCartProducts;


    public Product(String name, String description, Date date) {
        this.name = name;
        this.description = description;
        this.expirationDate = date;
    }
}
