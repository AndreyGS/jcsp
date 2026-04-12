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

package io.andreygs.jcsp.base;

import io.andreygs.jcsp.base.message.ICspMessageBuilderFactory;
import io.andreygs.jcsp.base.message.internal.CspMessageBuilderFactory;
import io.andreygs.jcsp.base.processing.ICspDataProcessorRegistryFactory;
import io.andreygs.jcsp.base.processing.composite.ICspDataCompositeSerializationProcessorBuilderFactory;
import io.andreygs.jcsp.base.processing.composite.internal.CspDataCompositeSerializationProcessorBuilderFactory;
import io.andreygs.jcsp.base.processing.internal.CspDataProcessorRegistryFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO: place description here
 */
public class FactoryRegistry
{
    private static final Map<Class<?>, Object> factories = new HashMap<>();

    static
    {
        factories.put(ICspMessageBuilderFactory.class, new CspMessageBuilderFactory());
        factories.put(ICspDataProcessorRegistryFactory.class, new CspDataProcessorRegistryFactory());
        factories.put(ICspDataCompositeSerializationProcessorBuilderFactory.class,
            new CspDataCompositeSerializationProcessorBuilderFactory());
    }

    public static <F> F requireFactory(Class<F> factoryClazz)
    {
        F factory = factoryClazz.cast(factories.get(factoryClazz));
        if (factory == null)
        {
            throw new IllegalArgumentException("Factory for " + factoryClazz.getName() + " is not registered!");
        }
        return factory;
    }
}
