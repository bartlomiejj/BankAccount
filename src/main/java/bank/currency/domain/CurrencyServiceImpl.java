package bank.currency.domain;

import bank.currency.domain.exceptions.BadRateException;
import bank.currency.domain.dto.CurrenciesDto;
import bank.currency.infrastructure.db.CurrencyEntity;
import bank.currency.infrastructure.db.CurrencyRepository;
import bank.currency.infrastructure.provider.NBPprovider.NBPService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@RequiredArgsConstructor
@Component
@Slf4j
class CurrencyServiceImpl implements CurrencyService {

    private final NBPService nbpService;

    private final CurrencyRepository currencyRepository;

    @Override
    public BigDecimal getRate(String target) {
        return getCurrentFromDb(target)
                .map(CurrenciesMapper.INSTANCE::mapToDto)
                .or(() -> getCurrentFromProvider(target))
                .map(CurrenciesDto.Currency::getMid)
                .orElseThrow(() -> new BadRateException("Could not find rate for " + target));
    }

    private Optional<CurrencyEntity> getCurrentFromDb(String target) {
        return currencyRepository.findByCodeAndLastUpdated(target, Date.valueOf(LocalDate.now()));
    }

    private Optional<CurrenciesDto.Currency> getCurrentFromProvider(String target) {
        Optional<CurrenciesDto.Currency> targetCurrency = CurrenciesMapper.INSTANCE.mapToDto(nbpService.getCurrency(target))
                .getCurrencies().stream()
                .filter(currency -> currency.getCode().equals(target))
                .findFirst();

        if (targetCurrency.isPresent()) {
            CurrencyEntity currencyEntity = CurrenciesMapper.INSTANCE.mapToEntity(targetCurrency.get());
            currencyRepository.save(currencyEntity);
        }

        return targetCurrency;
    }

    @Override
    public void refresh() {
        CurrenciesDto currenciesDto = CurrenciesMapper.INSTANCE.mapToDto(nbpService.getCurrencies());

        if (isNotEmpty(currenciesDto.getCurrencies())) {
            Map<String, CurrencyEntity> entityMap = currencyRepository.findAll().stream()
                            .collect(Collectors.toMap(CurrencyEntity::getCode, Function.identity()));

            Set<CurrencyEntity> updatedEntities = getUpdatedEntities(currenciesDto, entityMap);

            currencyRepository.saveAll(updatedEntities);
        }
    }

    private static Set<CurrencyEntity> getUpdatedEntities(CurrenciesDto currenciesDTO, Map<String, CurrencyEntity> entityMap) {
        return currenciesDTO.getCurrencies().stream()
                .map(dto -> updateEntity(dto, entityMap.getOrDefault(dto.getCode(), new CurrencyEntity())))
                .collect(Collectors.toSet());
    }

    private static CurrencyEntity updateEntity(CurrenciesDto.Currency dto, CurrencyEntity entity) {
        return CurrenciesMapper.INSTANCE.update(entity, dto);
    }

}
