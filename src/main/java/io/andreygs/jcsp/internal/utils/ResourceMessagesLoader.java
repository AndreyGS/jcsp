/**
 * @author Andrey Grabov-Smetankin <ukbpyh@gmail.com>
 * <p>
 * License
 * <p>
 * Copyright 2025 Andrey Grabov-Smetankin <ukbpyh@gmail.com>
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files
 * (the "Software"), to deal in the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge,
 * publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so,
 * subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package io.andreygs.jcsp.internal.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TODO: place description here
 */
public class ResourceMessagesLoader
{
    private static final Properties properties = new Properties();

    static
    {
        initCommonConstants();
    }

    public static void loadMessages(Class<?> clazz)
    {
        try
        {
            ResourceBundle bundle = ResourceBundle.getBundle(clazz.getPackageName() + ".messages",
                Locale.getDefault());
            for (Field field : clazz.getDeclaredFields())
            {
                if (Modifier.isStatic(field.getModifiers()) && Modifier.isPublic(field.getModifiers())
                        && field.getType() == String.class)
                {
                    String key = field.getName();
                    String value = bundle.getString(key);
                    value = replacePlaceholders(value);
                    field.setAccessible(true);
                    field.set(null, value);
                }
            }
        }
        catch (IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
    }

    private static void initCommonConstants()
    {
        ResourceBundle bundle = ResourceBundle.getBundle(ResourceMessagesLoader.class.getPackageName() + ".messages",
            Locale.getDefault());
        for (String key : bundle.keySet())
        {
            properties.put(key, bundle.getString(key));
        }
    }

    private static String replacePlaceholders(String value)
    {
        if (value.contains("${"))
        {
            String regex = "\\$\\{([^}]+)}";
            Pattern pattern = Pattern.compile(regex);
            while (value.contains("${"))
            {
                Matcher matcher = pattern.matcher(value);
                if (matcher.find())
                {
                    String property = matcher.group(1);
                    String propertyValue = properties.getProperty(property);
                    value = value.replaceAll("\\$\\{" + property + "}", propertyValue);
                }
            }
        }
        return value;
    }

    private ResourceMessagesLoader()
    {
    }
}
