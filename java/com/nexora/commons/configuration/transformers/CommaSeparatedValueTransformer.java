package com.nexora.commons.configuration.transformers;

import com.nexora.commons.configuration.TransformationTypeInfo;

import java.util.ArrayList;
import java.util.List;

public abstract class CommaSeparatedValueTransformer<T> extends PropertyTransformer<T>
{
	
	@Override
	protected final T parseObject(String value, TransformationTypeInfo typeInfo) throws Exception
	{
		return parseObject(splitAndTrimValues(value), typeInfo);
	}
	
	protected abstract T parseObject(List<String> value, TransformationTypeInfo typeInfo) throws Exception;
	
 
	protected final List<String> splitAndTrimValues(String value)
	{
		List<String> tokensList = new ArrayList<>();
		boolean inQuotes = false;
		StringBuilder b = new StringBuilder(value.length());
		for (char c : value.toCharArray())
		{
			switch (c)
			{
				case ',':
					if (inQuotes)
						break;
					tokensList.add(trim(b));
					b.setLength(0);
					continue;
				case '\"':
					inQuotes = !inQuotes;
			}
			b.append(c);
		}
		String lastValue = trim(b);
		if (!lastValue.isEmpty()) // don't add empty strings if it's the only element (no comma present) or if it's the last one
			tokensList.add(lastValue);
		return tokensList;
	}
	
	public String trim(StringBuilder input)
	{
		String output = input.toString().trim();
		// strip quotes if string starts AND ends with one
		if (output.length() > 1 && output.charAt(0) == '\"' && output.charAt(output.length() - 1) == '\"')
			output = output.substring(1, output.length() - 1);
		return output;
	}
}
