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

package io.andreygs.jcsp.internal.infrastructure.resource;

import io.andreygs.jcsp.internal.infrastructure.IParametrizedStringInterpolator;

import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.lang.reflect.Modifier;
import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Loader for resource messages that optionally contain template variables.
 */
public class ResourceMessagesWithTemplateVariablesLoader implements IResourceMessagesLoader
{
    private final List<IParametrizedStringInterpolator> stringInterpolators;

    /**
     * Constructs and instance.
     *
     * @param stringInterpolators List of string interpolators that will be using in template variables unreferencing.
     */
    public ResourceMessagesWithTemplateVariablesLoader(List<IParametrizedStringInterpolator> stringInterpolators)
    {
        this.stringInterpolators = List.copyOf(stringInterpolators);
    }

    @Override
    public void loadMessages(Class<?> clazz)
    {
        try
        {
            ResourceBundle bundle = ResourceBundle.getBundle(clazz.getPackageName() + ".messages",
                Locale.getDefault());
            for (Field field : clazz.getDeclaredFields())
            {
                if (Modifier.isStatic(field.getModifiers()) && Modifier.isPublic(field.getModifiers())
                        && !Modifier.isFinal(field.getModifiers()) && field.getType() == String.class)
                {
                    String key = field.getName();
                    String value = bundle.getString(key);
                    for (IParametrizedStringInterpolator interpolator : stringInterpolators)
                    {
                        value = interpolator.interpolate(value);
                    }
                    field.setAccessible(true);
                    field.set(null, value);
                }
            }
        }
        catch (MissingResourceException | ClassCastException | InaccessibleObjectException | IllegalAccessException e)
        {
            // We should not use resource message here because it can lead to cyclic error triggering
            throw new IllegalArgumentException(
                MessageFormat.format("Cannot load messages for class {0}", clazz), e);
        }
    }
}
