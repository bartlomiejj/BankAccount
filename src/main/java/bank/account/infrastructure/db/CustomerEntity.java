package bank.account.infrastructure.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "customer")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customer", indexes = {
        @Index(name = "pesel", columnList = "pesel", unique = true)
})
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String surname;

    private String pesel;

    @ElementCollection
    @CollectionTable(name = "account", joinColumns = @JoinColumn(name = "owner_id"))
    private Set<Account> accounts;

}
