package bank.account.domain;

import bank.account.api.protocol.AccountRequest;
import bank.account.api.protocol.AccountResponse;
import bank.account.api.protocol.CustomerResponse;
import bank.account.domain.dto.AccountDto;
import bank.account.domain.dto.CustomerDto;
import bank.account.infrastructure.db.Account;
import bank.account.infrastructure.db.CustomerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface AccountMapper {

    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    @Mapping(target = "id", ignore = true)
    CustomerEntity update(@MappingTarget CustomerEntity customerEntity, CustomerDto customerDto);

    @Mapping(target = "id", ignore = true)
    CustomerEntity mapToEntity(CustomerDto customerDto);

    CustomerDto mapToDto(CustomerEntity customerEntity);

    @Mapping(target = "accountNumber", ignore = true)
    @Mapping(target = "balance", source = "initialBalance")
    AccountDto mapToDto(AccountRequest accountRequest);

    AccountDto mapToDto(Account account);

    CustomerResponse mapToResponse(CustomerDto customerDto);

    AccountResponse mapToResponse(AccountDto accountDto);

}
