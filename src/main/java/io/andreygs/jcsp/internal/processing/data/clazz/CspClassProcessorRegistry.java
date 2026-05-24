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

package io.andreygs.jcsp.internal.processing.data.clazz;

import io.andreygs.jcsp.internal.processing.data.Messages;
import io.andreygs.jcsp.internal.processing.data.clazz.dto.ICspClassProcessorDescriptor;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TODO: place description here
 */
public class CspClassProcessorRegistry<P>
    implements ICspClassProcessorRegistry<P>
{
    private final ICspClassProcessorDescriptorGenerator cspClassProcessorDescriptorGenerator;
    private final Map<Class<?>, ICspClassProcessorDescriptor<P>> classProcessorDescriptors = new ConcurrentHashMap<>();

    public CspClassProcessorRegistry(ICspClassProcessorDescriptorGenerator cspClassProcessorDescriptorGenerator)
    {
        this.cspClassProcessorDescriptorGenerator = Objects.requireNonNull(cspClassProcessorDescriptorGenerator);
    }

    @Override
    public void registerClassProcessor(Class<?> clazz, P classProcessor)
    {
        if (clazz.isPrimitive() || clazz.isArray())
        {
            throw new IllegalArgumentException(Messages.CspProcessorRegistry_Illegal_type_group);
        }
        if (clazz == String.class || clazz == Collection.class || clazz == Map.class)
        {
            throw new IllegalArgumentException(Messages.CspProcessorRegistry_Illegal_class);
        }
        ICspClassProcessorDescriptor<P> newDescriptor =
            cspClassProcessorDescriptorGenerator.generateClassProcessorDescriptor(classProcessor, clazz);
        classProcessorDescriptors.put(clazz, newDescriptor);
    }

    @Override
    public Optional<ICspClassProcessorDescriptor<P>> resolveClassProcessorDescriptor(Class<?> clazz)
    {
        return Optional.ofNullable(classProcessorDescriptors.get(clazz));
    }

    @Override
    public void unregisterClassProcessor(Class<?> clazz)
    {
        classProcessorDescriptors.remove(clazz);
    }
}
