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

package io.andreygs.jcsp.internal.processing.data.factory;

import io.andreygs.jcsp.api.processing.data.ICspSerializationProcessor;
import io.andreygs.jcsp.api.processing.data.clazz.ICspClassSerializationProcessor;
import io.andreygs.jcsp.internal.model.buffer.ISerializationBuffer;
import io.andreygs.jcsp.internal.processing.data.CspSerializationProcessor;
import io.andreygs.jcsp.internal.processing.data.ICspProcessorRegistry;
import io.andreygs.jcsp.internal.processing.data.type.ICspTypeSerializationProcessor;

/**
 * TODO: place description here
 */
public final class CspSerializationProcessorFactory
    implements ICspSerializationProcessorFactory
{
    /**
     * Default immutable cached instance of {@link ICspClassSerializationProcessorGeneratorProvider}.
     * <p>
     * Thread-safe.
     */
    private static final ICspClassSerializationProcessorGeneratorProvider
        DEFAULT_DATA_SERIALIZATION_PROCESSOR_GENERATOR_PROVIDER = new CspClassSerializationProcessorGeneratorProvider();

    @Override
    public ICspSerializationProcessor createGeneralSerializationProcessor(
        ISerializationBuffer cspSerializationBuffer,
        ICspProcessorRegistry<ICspClassSerializationProcessor<?>, ICspTypeSerializationProcessor> cspProcessorRegistry,
        ICspDataMessage cspDataMessage)
    {
        return new CspSerializationProcessor(cspSerializationBuffer, cspProcessorRegistry,
            DEFAULT_DATA_SERIALIZATION_PROCESSOR_GENERATOR_PROVIDER.provideCspDataProcessorGenerator(), cspDataMessage);
    }
}
