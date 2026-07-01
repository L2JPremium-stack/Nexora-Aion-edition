package com.nexora.commons.configuration;

import com.nexora.commons.configuration.transformers.MapTransformer;
import com.nexora.commons.configuration.transformers.PropertyTransformers;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is designed to process classes and interfaces that have fields marked with {@link Property} annotation
 * @author SoulKeeper
 */
public class ConfigurableProcessor
{
	
	private static final Logger log = LoggerFactory.getLogger(ConfigurableProcessor.class);
	private static final Pattern propertyPattern = Pattern.compile("\\$\\{([^}]+)\\}"); // finds strings enclosed in ${}
	
 
	public static Set<String> process(Properties properties, Object... objectsOrClasses)
	{
		Set<String> unusedProperties = new HashSet<>(properties.stringPropertyNames());
		for (Object object : objectsOrClasses)
		{
			if (object instanceof Class<?> clazz)
			{
				process(clazz, null, properties, unusedProperties);
			}
			else
			{
				process(object.getClass(), object, properties, unusedProperties);
			}
		}
		return unusedProperties;
	}
	
 
	private static void process(Class<?> clazz, Object obj, Properties props, Set<String> unusedProperties)
	{
		processFields(clazz, obj, props, unusedProperties);
		
		// Interfaces can't have any object fields, only static
		// So there is no need to parse interfaces for instances of objects
		// Only classes (static fields) can be located in interfaces
		if (obj == null)
		{
			for (Class<?> itf : clazz.getInterfaces())
			{
				process(itf, obj, props, unusedProperties);
			}
		}
		
		Class<?> superClass = clazz.getSuperclass();
		if (superClass != null && superClass != Object.class)
		{
			process(superClass, obj, props, unusedProperties);
		}
	}
	
	 
	private static void processFields(Class<?> clazz, Object obj, Properties props, Set<String> unusedProperties)
	{
		for (Field f : clazz.getDeclaredFields())
		{
			// Static fields should not be modified when processing object
			if (Modifier.isStatic(f.getModifiers()) && obj != null)
			{
				continue;
			}
			
			// Not static field should not be processed when parsing class
			if (!Modifier.isStatic(f.getModifiers()) && obj == null)
			{
				continue;
			}
			
			if (f.isAnnotationPresent(Property.class) || f.isAnnotationPresent(com.nexora.commons.configuration.Properties.class))
			{
				// Final fields should not be processed
				if (Modifier.isFinal(f.getModifiers()))
					throw new RuntimeException("Can't process final field " + f.getName() + " of class " + clazz.getName());
				processField(f, obj, props, unusedProperties);
			}
		}
	}
	
	 
	private static void processField(Field f, Object obj, Properties props, Set<String> unusedProperties)
	{
		boolean oldAccessible = f.canAccess(obj);
		try
		{
			if (!oldAccessible)
				f.setAccessible(true);
			Property property = f.getAnnotation(Property.class);
			com.nexora.commons.configuration.Properties properties = f.getAnnotation(com.nexora.commons.configuration.Properties.class);
			if (property != null)
			{
				if (properties != null)
					throw new UnsupportedOperationException("Field can only be annotated with @Property or @Properties, not both.");
				String key = Objects.requireNonNull(property.key(), "@Property key must not be empty");
				String value = getValue(key, property.defaultValue(), props, unusedProperties);
				if (!Property.DEFAULT_VALUE.equals(value))
					f.set(obj, transform(value, f));
				else
					log.debug("Field " + f.getName() + " of class " + f.getDeclaringClass().getName() + " wasn't modified");
			}
			else
			{
				Pattern pattern = Pattern.compile(properties.keyPattern());
				Map<String, String> values = filterProperties(pattern, props, unusedProperties);
				f.set(obj, transform(values, f));
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException("Error modifying field " + f.getName() + " of " + (obj != null ? obj : f.getDeclaringClass()), e);
		}
		finally
		{
			if (!oldAccessible)
				f.setAccessible(false);
		}
	}
	
	public static Object transform(String value, Field field) throws TransformationException
	{
		Type[] genericTypeArgs = {};
		if (field.getGenericType() instanceof ParameterizedType)
			genericTypeArgs = ((ParameterizedType) field.getGenericType()).getActualTypeArguments();
		return PropertyTransformers.get(field.getType()).transform(value, field.getType(), genericTypeArgs);
	}
	
	private static Map<String, String> filterProperties(Pattern pattern, Properties props, Set<String> unusedProperties)
	{
		Map<String, String> input = new HashMap<>();
		for (String k : props.stringPropertyNames())
		{
			Matcher matcher = pattern.matcher(k);
			if (matcher.find())
			{
				String key = matcher.groupCount() > 0 ? matcher.group(1) : k;
				String value = getValue(k, "", props, unusedProperties);
				input.put(key, value);
			}
		}
		return input;
	}
	
	public static Map<?, ?> transform(Map<String, String> values, Field field) throws Exception
	{
		Type[] genericTypeArgs = {};
		if (field.getGenericType() instanceof ParameterizedType)
			genericTypeArgs = ((ParameterizedType) field.getGenericType()).getActualTypeArguments();
		return MapTransformer.transform(values, field.getType(), genericTypeArgs);
	}
	
	private static String getValue(String key, String defaultValue, Properties props, Set<String> unusedProperties)
	{
		String value = props.getProperty(key, defaultValue);
		if (unusedProperties != null && (!Objects.equals(value, defaultValue) || props.getProperty(key) != null))
			unusedProperties.remove(key);
		if (value.trim().equals("\"\""))
			value = "";
		else
			value = replacePropertyPlaceholders(value, props);
		return value;
	}
	
	private static String replacePropertyPlaceholders(String value, Properties props)
	{
		Matcher matcher = propertyPattern.matcher(value);
		while (matcher.find())
		{
			String completeToken = matcher.group(); // ${property.name}
			String token = matcher.group(1); // property.name
			String replacement = props.getProperty(token);
			value = value.replace(completeToken, replacement == null ? "" : replacement);
		}
		return value;
	}
}
