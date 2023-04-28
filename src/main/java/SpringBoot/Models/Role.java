package SpringBoot.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Getter
@Setter
@ToString
@Table(name = "Role", schema = "public", catalog = "SpringBoot")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false)
    private ERole name;

    @OneToMany(mappedBy = "role")
    @JsonIgnore
    @ToString.Exclude
    private Collection<User> users;
}
