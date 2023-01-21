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
@Table(name = "Shop")
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "name", nullable = false)
    private String name;

    @Basic
    @Column(name = "address", nullable = false)
    private String address;

    @Basic
    @Column(name = "code", nullable = false)
    private String code;

    @Transient
    private boolean checked = false;

    @ManyToOne(fetch = FetchType.EAGER)
    private ShopFunction shopFunction;

    @OneToOne(fetch = FetchType.EAGER)
    private User user;

    @OneToMany(mappedBy = "shop")
    @JsonIgnore
    @ToString.Exclude
    private Collection<Command> commands;

    @OneToMany(mappedBy = "shop")
    @JsonIgnore
    @ToString.Exclude
    private Collection<ShopInventory> shopInventories;

    public Shop(String name, String address, String code, ShopFunction shopFunction, User user){
        this.name = name;
        this.address= address;
        this.code = code;
        this.shopFunction = shopFunction;
        this.user = user;
    }
}
