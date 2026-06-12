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

package io.andreygs.jcsp.internal.processing.buffer.dto;

import io.andreygs.jcsp.api.processing.buffer.AllocationType;
import io.andreygs.jcsp.api.processing.buffer.IBufferResizeStrategy;
import io.andreygs.jcsp.api.processing.buffer.dto.ISerializationBufferConfig;

import java.util.Objects;

/**
 * Serialization buffer configuration defined by constructor arguments.
 */
public class SerializationBufferConfig
    implements ISerializationBufferConfig
{
    private final int initialCapacity;
    private final AllocationType allocationType;
    private final IBufferResizeStrategy resizeStrategy;

    /**
     * Constructs an instance.
     * <p>
     * Public visibility is intended for factory usage and unit-testing of this class only.
     *
     * @param initialCapacity Initial capacity of buffer. Must not be negative.
     * @param allocationType Type of buffer allocation.
     * @param resizeStrategy Strategy of buffer resizing.
     * @throws IllegalArgumentException if bufferInitialCapacity is negative.
     */
    public SerializationBufferConfig(int initialCapacity, AllocationType allocationType,
        IBufferResizeStrategy resizeStrategy)
    {
        if (initialCapacity < 0)
        {
            throw new IllegalArgumentException("bufferInitialCapacity < 0");
        }
        this.initialCapacity = initialCapacity;
        this.allocationType = Objects.requireNonNull(allocationType);
        this.resizeStrategy = Objects.requireNonNull(resizeStrategy);
    }

    @Override
    public int getInitialCapacity()
    {
        return initialCapacity;
    }

    @Override
    public AllocationType getAllocationType()
    {
        return allocationType;
    }

    @Override
    public IBufferResizeStrategy getResizeStrategy()
    {
        return resizeStrategy;
    }
}
