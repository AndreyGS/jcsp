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

package io.andreygs.jcsp.internal.infrastructure.service.utils;

import io.andreygs.jcsp.api.exception.JcspRuntimeException;
import io.andreygs.jcsp.internal.annotation.JcspInject;
import io.andreygs.jcsp.internal.infrastructure.service.IJcspServiceKey;
import io.andreygs.jcsp.internal.infrastructure.service.JcspServiceKey;
import io.andreygs.jcsp.internal.infrastructure.utils.JcspReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * TODO: place description here
 */
public class JcspInjectedUtils
{
    public static Optional<Constructor<?>> resolveInjectedConstructor(Class<?> clazz)
    {
        Constructor<?>[] constructors = clazz.getConstructors();
        for (Constructor<?> constructor : constructors)
        {
            if (constructor.isAnnotationPresent(JcspInject.class))
            {
                return Optional.of(constructor);
            }
        }
        return Optional.empty();
    }

    public static Constructor<?> requireInjectedOrDefaultConstructor(Class<?> clazz)
    {
        Optional<Constructor<?>> constructor = resolveInjectedConstructor(clazz);
        if (constructor.isPresent())
        {
            return constructor.get();
        }
        return JcspReflectionUtils.requireDefaultConstructor(clazz);
    }

    public static List<IJcspServiceKey> requireInjectedParameters(Constructor<?> constructor)
    {
        Parameter[] parameters = constructor.getParameters();
        List<IJcspServiceKey> injectedParameters = new ArrayList<>(parameters.length);
        for (Parameter parameter : parameters)
        {
            JcspInject injectAnnotation = parameter.getAnnotation(JcspInject.class);
            if (injectAnnotation == null)
            {
                throw JcspRuntimeException.forClassError(
                    "Constructor \"" + constructor + "\" parameter \"" + parameter +
                        "\" has no \"" + JcspInject.class + "\" annotation", null);
            }
            Class<?> parameterClass = parameter.getType();
            List<Class<?>> genericTypeVariableClasses = new ArrayList<>();
            if (parameter.getParameterizedType() instanceof ParameterizedType parametrizedType)
            {
                for (Type typeArgument : parametrizedType.getActualTypeArguments())
                {
                    if (typeArgument instanceof Class<?> clazz)
                    {
                        genericTypeVariableClasses.add(clazz);
                    }
                    else if (typeArgument instanceof ParameterizedType parameterizedTypeArgument)
                    {
                        genericTypeVariableClasses.add((Class<?>)parameterizedTypeArgument.getRawType());
                    }
                    else
                    {
                        throw JcspRuntimeException.forClassError(
                            "Parameter \"" + parameter + "\" of constructor \"" + constructor +
                                "\" has inappropriate type arguments", null);
                    }
                }
            }
            IJcspServiceKey serviceKey = new JcspServiceKey(parameterClass, genericTypeVariableClasses,
                injectAnnotation.value());
            injectedParameters.add(serviceKey);
        }
        return injectedParameters;
    }

    private JcspInjectedUtils()
    {
    }
}
