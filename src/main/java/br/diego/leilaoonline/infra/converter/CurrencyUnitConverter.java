package br.diego.leilaoonline.infra.converter;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply=true)
public class CurrencyUnitConverter implements  AttributeConverter<CurrencyUnit, String>{ 
 
    private static final String CURRENCY = "BRL";
    
	@Override
	public String convertToDatabaseColumn(CurrencyUnit attribute) {
		return attribute != null
				? attribute.getCurrencyCode()
				: CURRENCY;
	}

	@Override
	public CurrencyUnit convertToEntityAttribute(String dbData) {
		return Monetary.getCurrency(dbData);
	} 
} 