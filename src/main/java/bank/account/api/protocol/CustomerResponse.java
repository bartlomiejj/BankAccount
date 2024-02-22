package bank.account.api.protocol;

import lombok.Data;

import java.util.List;

@Data
public class CustomerResponse {

    private String name;

    private String surname;

    private String pesel;

    private List<AccountResponse> accounts;

}
