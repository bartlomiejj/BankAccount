package bank.account.domain;

import bank.account.api.protocol.ExchangeRequest;
import bank.account.domain.dto.CustomerDto;

interface ExchangeService {

    CustomerDto exchange(ExchangeRequest exchangeRequest);

}
