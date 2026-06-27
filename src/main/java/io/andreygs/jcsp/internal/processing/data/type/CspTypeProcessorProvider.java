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

package io.andreygs.jcsp.internal.processing.data.type;

import java.lang.reflect.AnnotatedType;
import java.util.Objects;
import java.util.Optional;

/**
 * Provider for type processors.
 * <p>
 * First, tries to find and return already registered type processor from {@link ICspTypeProcessorRegistry}, and if
 * there is no such generates a new one with help of {@link ICspTypeProcessorGenerator}, then registers it in registry
 * and returns it.
 *
 * @param <P> type of type processor: {@link ICspTypeSerializationProcessor} or {@link ICspTypeDeserializationProcessor}.
 */
public class CspTypeProcessorProvider<P> implements ICspTypeProcessorProvider<P>
{
    private final ICspTypeProcessorRegistry<P> cspTypeProcessorRegistry;
    private final ICspTypeProcessorGenerator<P> cspTypeProcessorGenerator;

    /**
     * Constructs an instance.
     *
     * @param cspTypeProcessorRegistry Registry for type processor.
     * @param cspTypeProcessorGenerator Generator of type processor.
     */
    public CspTypeProcessorProvider(ICspTypeProcessorRegistry<P> cspTypeProcessorRegistry,
        ICspTypeProcessorGenerator<P> cspTypeProcessorGenerator)
    {
        this.cspTypeProcessorRegistry = Objects.requireNonNull(cspTypeProcessorRegistry);
        this.cspTypeProcessorGenerator = Objects.requireNonNull(cspTypeProcessorGenerator);
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException if {@link ICspTypeProcessorGenerator#generate(AnnotatedType)} throws it.
     */
    @Override
    public P provide(AnnotatedType annotatedType)
    {
        Optional<P> typeProcessor = cspTypeProcessorRegistry.find(Objects.requireNonNull(annotatedType));
        return typeProcessor.orElse(requireTypeProcessor(annotatedType));
    }

    /**
     * Requires type processor.
     * <p>
     * Generates new type processor, registers it and returns generated one.
     *
     * @param annotatedType Type whose processor need to generate.
     * @return generated type processor.
     */
    private P requireTypeProcessor(AnnotatedType annotatedType)
    {
        P typeProcessor = cspTypeProcessorGenerator.generate(annotatedType);
        cspTypeProcessorRegistry.register(annotatedType, typeProcessor);
        return typeProcessor;
    }
}
