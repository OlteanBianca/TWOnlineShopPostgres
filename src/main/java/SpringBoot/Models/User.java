package SpringBoot.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Collection;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "UserData", schema = "public", catalog = "SpringBoot")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @NotBlank
    @Size(max = 20, min = 5)
    @Column(name = "username", nullable = false)
    private String username;

    @Basic
    @Email
    @NotBlank
    @Column(name = "email", unique = true, nullable = false) /* Duplicates emails not allowed */
    private String email;

    @Basic
    @NotBlank
    @Size(min = 5)
    @Column(name = "password", nullable = false)
    private String password;

    @Transient
    private String passwordConfirm;

    @Transient
    public String roleName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @ToString.Exclude
    private Role role;

    @OneToOne(mappedBy = "user")
    @JsonIgnore
    @ToString.Exclude
    private Shop shop;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    @ToString.Exclude
    private Collection<Command> commands;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    @ToString.Exclude
    private Collection<ShoppingCart> shoppingCarts;


    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
