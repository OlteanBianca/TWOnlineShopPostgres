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

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "Request")
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "approved", nullable = false)
    private boolean approved = false;

    @Basic
    @Column(name = "shopName", nullable = false)
    private String shopName;

    @Basic
    @Column(name = "address", nullable = false)
    private String address;

    @Basic
    @Column(name = "code", nullable = false)
    private String code;

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

}
