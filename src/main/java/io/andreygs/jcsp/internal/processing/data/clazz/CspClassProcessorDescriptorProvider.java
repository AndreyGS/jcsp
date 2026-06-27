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

import java.util.Objects;
import java.util.Optional;

/**
 * TODO: place description here
 */
public class CspClassProcessorDescriptorProvider<P>
    implements ICspClassProcessorDescriptorProvider<P>
{
    private final ICspClassProcessorRegistry<P> cspClassProcessorRegistry;
    private final ICspClassProcessorGenerator<P> cspClassProcessorGenerator;

    public CspClassProcessorDescriptorProvider(ICspClassProcessorRegistry<P> cspClassProcessorRegistry,
        ICspClassProcessorGenerator<P> cspClassProcessorGenerator)
    {
        this.cspClassProcessorRegistry = Objects.requireNonNull(cspClassProcessorRegistry);
        this.cspClassProcessorGenerator = Objects.requireNonNull(cspClassProcessorGenerator);
    }

    @Override
    public ICspClassProcessorDescriptor<P> provide(Class<?> clazz)
    {
        Optional<ICspClassProcessorDescriptor<P>> classProcessorDescriptor =
            cspClassProcessorRegistry.findClassProcessorDescriptor(clazz);
        return classProcessorDescriptor.orElse(requireClassProcessorDescriptor(clazz));
    }

    private ICspClassProcessorDescriptor<P> requireClassProcessorDescriptor(Class<?> clazz)
    {
        P classProcessor = cspClassProcessorGenerator.generate(clazz);
        cspClassProcessorRegistry.register(clazz, classProcessor);
        Optional<ICspClassProcessorDescriptor<P>> classProcessorDescriptor =
            cspClassProcessorRegistry.findClassProcessorDescriptor(clazz);
        if (classProcessorDescriptor.isPresent())
        {
            return classProcessorDescriptor.get();
        }
        throw new IllegalStateException("No class processor found for " + clazz);
    }
}
