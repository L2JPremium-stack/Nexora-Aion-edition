package com.nexora.commons.configuration.transformers;

import java.io.File;

import com.nexora.commons.configuration.TransformationTypeInfo;

/**
 * Transforms string to file by creating new file instance. It's not checked if file exists.
 * 
 * @author SoulKeeper
 */
public class FileTransformer extends PropertyTransformer<File> {

	@Override
	public boolean matches(Class<?> targetType) {
		return targetType == File.class;
	}

	@Override
	protected File parseObject(String value, TransformationTypeInfo typeInfo) {
		return new File(value);
	}
}
