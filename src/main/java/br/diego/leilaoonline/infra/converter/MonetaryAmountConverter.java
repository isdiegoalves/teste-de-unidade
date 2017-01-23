package br.diego.leilaoonline.infra.converter;

import static org.javamoney.moneta.FastMoney.zero;

import java.math.BigDecimal;
import java.util.Optional;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.MonetaryQuery;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.javamoney.moneta.Money;

@Converter(autoApply=true)
public class MonetaryAmountConverter implements  AttributeConverter<MonetaryAmount, BigDecimal>{ 
 
    private static final CurrencyUnit CURRENCY = Monetary.getCurrency("BRL");
    
    private static final MonetaryQuery<BigDecimal> EXTRACT_BIG_DECIMAL = (m) -> m.getNumber().numberValue(BigDecimal.class); 

	@Override
	public BigDecimal convertToDatabaseColumn(MonetaryAmount attribute) {
		  return Optional.ofNullable(attribute).orElse(zero(CURRENCY)).query(EXTRACT_BIG_DECIMAL);
	}

	@Override
	public MonetaryAmount convertToEntityAttribute(BigDecimal dbData) {
		return Money.of(dbData, CURRENCY);
	} 
} 