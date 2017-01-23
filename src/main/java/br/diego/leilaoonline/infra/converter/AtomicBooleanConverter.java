package br.diego.leilaoonline.infra.converter;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply=true)
public class AtomicBooleanConverter implements  AttributeConverter<AtomicBoolean, Boolean> {

	@Override
	public Boolean convertToDatabaseColumn(AtomicBoolean attribute) {
		if (attribute != null)
			return attribute.get();
					
		return Boolean.FALSE;
	}

	@Override
	public AtomicBoolean convertToEntityAttribute(Boolean dbData) {
		return new AtomicBoolean(dbData);
	} 
 
} 