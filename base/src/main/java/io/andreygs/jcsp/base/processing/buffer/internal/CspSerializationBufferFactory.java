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

package io.andreygs.jcsp.base.processing.buffer.internal;

import io.andreygs.jcsp.base.processing.buffer.IBufferResizeStrategy;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

/**
 * Sole implementation of {@link ICspSerializationBufferFactory}.
 * <p>
 * Creates following class instances:
 * <ul>
 *     <li>{@link ICspSerializationBuffer} -> {@link CspSerializationBuffer}</li>
 * </ul>
 * <p>
 * When creating serialization buffer it uses next default values:
 * <ul>
 *     <li>initialBufferCapacity - {@link #DEFAULT_CAPACITY_SIZE default capacity}</li>
 *     <li>directBuffer - true</li>
 *     <li>bufferResizeStrategy - {@link #DEFAULT_BUFFER_RESIZE_STRATEGY default buffer resize strategy}</li>
 * </ul>
 */
public final class CspSerializationBufferFactory
    implements ICspSerializationBufferFactory
{
    /**
     * Default capacity of {@link ByteBuffer} of {@link ICspSerializationBuffer} when it is created, if no explicit
     * value was provided.
     */
    public static final int DEFAULT_CAPACITY_SIZE = 256;
    /**
     * Default immutable cached instance of {@link IBufferResizeStrategy}.
     * <p>
     * Thread-safe.
     */
    public static final IBufferResizeStrategy DEFAULT_BUFFER_RESIZE_STRATEGY = new DoublingBufferSizeStrategy();

    @Override
    public ICspSerializationBuffer createCspSerializationBuffer(@Nullable Integer initialBufferCapacity,
                                                                @Nullable Boolean directBuffer,
                                                                @Nullable IBufferResizeStrategy bufferResizeStrategy)
    {
        int initialBufferCapacityLocal = initialBufferCapacity == null ? DEFAULT_CAPACITY_SIZE : initialBufferCapacity;
        boolean directBufferLocal = directBuffer == null || directBuffer;
        IBufferResizeStrategy bufferResizeStrategyLocal = bufferResizeStrategy == null
            ? DEFAULT_BUFFER_RESIZE_STRATEGY
            : bufferResizeStrategy;
        return new CspSerializationBuffer(initialBufferCapacityLocal, directBufferLocal, bufferResizeStrategyLocal);
    }
}
