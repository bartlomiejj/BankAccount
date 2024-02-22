package bank.currency.domain;

import bank.currency.domain.dto.CurrenciesDto;
import bank.currency.infrastructure.db.CurrencyEntity;
import bank.currency.infrastructure.provider.protocol.AbstractProviderResponse;
import bank.shared.Currency;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.Date;
import java.util.HashSet;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface CurrenciesMapper {

    CurrenciesMapper INSTANCE = Mappers.getMapper(CurrenciesMapper.class);

    default CurrenciesDto mapToDto(AbstractProviderResponse abstractProviderResponse) {
        return new CurrenciesDto(mapCurrencies(abstractProviderResponse));
    }

    default HashSet<CurrenciesDto.Currency> mapCurrencies(AbstractProviderResponse abstractProviderResponse) {
        return abstractProviderResponse.getCurrencies().stream()
                .map(currency -> mapCurrency(currency, abstractProviderResponse.getLastUpdated(), abstractProviderResponse.getSource()))
                .collect(Collectors.toCollection(HashSet::new));
    }

    CurrenciesDto.Currency mapCurrency(Currency currency, Date lastUpdated, String source);

    @Mapping(target = "id", ignore = true)
    CurrencyEntity update(@MappingTarget CurrencyEntity currency, CurrenciesDto.Currency currencyDto);

    @Mapping(target = "id", ignore = true)
    CurrencyEntity mapToEntity(CurrenciesDto.Currency currencyDto);

    CurrenciesDto.Currency mapToDto(CurrencyEntity currencyEntity);

}
