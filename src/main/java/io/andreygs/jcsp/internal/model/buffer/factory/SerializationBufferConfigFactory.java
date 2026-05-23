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

package io.andreygs.jcsp.internal.model.buffer.factory;

import io.andreygs.jcsp.api.model.buffer.AllocationType;
import io.andreygs.jcsp.api.model.buffer.IBufferResizeStrategy;
import io.andreygs.jcsp.api.model.buffer.dto.ISerializationBufferConfig;
import io.andreygs.jcsp.api.model.buffer.dto.factory.ISerializationBufferConfigFactory;
import io.andreygs.jcsp.internal.model.buffer.dto.SerializationBufferConfig;
import org.jetbrains.annotations.Nullable;

/**
 * TODO: place description here
 */
public class SerializationBufferConfigFactory implements ISerializationBufferConfigFactory
{
    /**
     * Default capacity of buffer when it is created, if no explicit value was provided.
     */
    private static final int DEFAULT_CAPACITY_SIZE = 256;
    /**
     * Default immutable cached instance of {@link IBufferResizeStrategy}.
     */
    private static final IBufferResizeStrategy
        DEFAULT_BUFFER_RESIZE_STRATEGY = new BufferResizeStrategyFactory().provideDefaultBufferResizeStrategy();
    /**
     * Default immutable cached instance of {@link ISerializationBufferConfig}.
     */
    private static final ISerializationBufferConfig DEFAULT_BUFFER_CONFIG =
        new SerializationBufferConfig(DEFAULT_CAPACITY_SIZE, AllocationType.DIRECT, DEFAULT_BUFFER_RESIZE_STRATEGY);

    @Override
    public ISerializationBufferConfig createBufferConfig(@Nullable Integer initialBufferCapacity,
        @Nullable AllocationType allocationType, @Nullable IBufferResizeStrategy bufferResizeStrategy)
    {
        return new SerializationBufferConfig(
            initialBufferCapacity == null ? DEFAULT_CAPACITY_SIZE : initialBufferCapacity,
            allocationType == null ? AllocationType.DIRECT : allocationType,
            bufferResizeStrategy ==  null ? DEFAULT_BUFFER_RESIZE_STRATEGY : bufferResizeStrategy);
    }

    @Override
    public ISerializationBufferConfig provideDefaultBufferConfig()
    {
        return DEFAULT_BUFFER_CONFIG;
    }
}
