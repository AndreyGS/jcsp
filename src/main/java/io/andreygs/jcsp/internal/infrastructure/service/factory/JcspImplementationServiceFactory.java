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

package io.andreygs.jcsp.internal.infrastructure.service.factory;

import io.andreygs.jcsp.api.exception.JcspRuntimeException;
import io.andreygs.jcsp.internal.infrastructure.service.IJcspServiceKey;
import io.andreygs.jcsp.internal.infrastructure.service.IJcspServiceProvider;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;

/**
 * TODO: place description here
 */
class JcspImplementationServiceFactory implements IJcspServiceFactory
{
    private final IJcspServiceKey serviceKey;
    private final Constructor<?> constructor;
    private final List<IJcspServiceKey> injectedParameters;

    JcspImplementationServiceFactory(IJcspServiceKey serviceKey, Constructor<?> constructor,
        List<IJcspServiceKey> injectedParameters)
    {
        this.serviceKey = Objects.requireNonNull(serviceKey);
        this.constructor = Objects.requireNonNull(constructor);
        this.injectedParameters = List.copyOf(injectedParameters);
    }

    @Override
    public Object create(IJcspServiceProvider serviceProvider)
    {
        Object[] constructorArguments = new Object[injectedParameters.size()];
        for (int i = 0; i < injectedParameters.size(); ++i)
        {
            IJcspServiceKey serviceKey = injectedParameters.get(i);
            constructorArguments[i] = serviceProvider.provide(serviceKey);
        }
        try
        {
            return constructor.newInstance(constructorArguments);
        }
        catch (InstantiationException | IllegalAccessException | InvocationTargetException e)
        {
            throw JcspRuntimeException.forClassError(
                "Cannot instantiate class \"" + constructor.getDeclaringClass() + "\" with constructor \"" +
                    constructor + "\"", e);
        }
    }

    @Override
    public IJcspServiceKey getServiceKey()
    {
        return serviceKey;
    }
}
