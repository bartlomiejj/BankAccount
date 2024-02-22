package bank.account.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {

    private String name;

    private String surname;

    private String pesel;

    private Set<AccountDto> accounts;

    public void addAccount(AccountDto accountDto) {
        if (Objects.isNull(accounts)) {
            accounts = new HashSet<>();
        }
        accounts.add(accountDto);
    }

    public CustomerDto(String name, String surname, String pesel) {
        this.name = name;
        this.surname = surname;
        this.pesel = pesel;
    }
}
