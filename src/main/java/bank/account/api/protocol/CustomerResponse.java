package bank.account.api.protocol;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CustomerResponse implements Serializable {

    private static final long serialVersionUID = 8959586977692061708L;

    private String name;

    private String surname;

    private String pesel;

    private List<AccountResponse> accounts;

}
