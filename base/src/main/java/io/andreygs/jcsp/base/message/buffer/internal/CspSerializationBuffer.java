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

package io.andreygs.jcsp.base.message.buffer.internal;

import io.andreygs.jcsp.base.utils.IBufferResizeStrategy;
import io.andreygs.jcsp.base.utils.internal.BufferDoublingResizeStrategy;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * This class works as buffer for writing raw data in CSP serialization process.
 * <p>
 * It is a wrapper over {@link ByteBuffer} optimized for use in CSP serialization process.
 */
public final class CspSerializationBuffer
    implements ICspSerializationBuffer
{
    /**
     * {@link ByteBuffer} that will be used in operations.
     */
    private ByteBuffer byteBuffer;

    /**
     * Is {@link ByteBuffer} a direct buffer.
     *
     * @see ByteBuffer
     */
    private final Boolean directBuffer;

    /**
     * Strategy of {@link ByteBuffer} resizing.
     */
    private final IBufferResizeStrategy bufferResizeStrategy;

    /**
     * Constructs CspSerializationByteBuffer.
     *
     * @param initialBufferCapacity Initial capacity of buffer. Must not be negative.
     *                              If it equals null, then {@link #DEFAULT_CAPACITY_SIZE default capacity} value will be used.
     * @param directBuffer Is buffer direct or not.
     *                     You should consider that direct buffer may not have underlying array that can be retrieved.
     *                     Non-direct buffer, otherwise will always have underlying array, and it can be used in all
     *                     cases safely, but it has at least one additional copying when passed to any native function,
     *                     including methods of sockets and streams.
     *                     So if you want to pass data by net buffer should be direct. But many cryptographic libraries,
     *                     on the other hand, are using byte[], and you must either use non-direct buffer or
     *                     convert make additionally copy to byte[] from direct buffer manually.
     *                     If it equals null, then direct buffer will be used.
     * @param bufferResizeStrategy Strategy of buffer resizing.
     *                             If it equals null, then {@link BufferDoublingResizeStrategy} will be used.
     * @see ByteBuffer
     */
    CspSerializationBuffer(@Nullable Integer initialBufferCapacity, @Nullable Boolean directBuffer,
                           @Nullable IBufferResizeStrategy bufferResizeStrategy)
    {
        this.directBuffer = directBuffer != null ? directBuffer : true;
        setByteBuffer(initialBufferCapacity != null ? initialBufferCapacity : DEFAULT_CAPACITY_SIZE);
        this.bufferResizeStrategy = bufferResizeStrategy != null ? bufferResizeStrategy : BufferDoublingResizeStrategy.INSTANCE;
    }

    @Override
    public boolean isDirectBuffer()
    {
        return directBuffer;
    }

    @Override
    public ByteBuffer getByteBuffer()
    {
        return byteBuffer;
    }

    @Override
    public void applyEndianness(ByteOrder byteOrder)
    {
        byteBuffer.order(byteOrder);
    }

    @Override
    public void write(byte value)
    {
        expandBufferIfNeed(Byte.BYTES);
        byteBuffer.put(value);
    }

    @Override
    public void write(short value)
    {
        expandBufferIfNeed(Short.BYTES);
        byteBuffer.putShort(value);
    }

    @Override
    public void write(int value)
    {
        expandBufferIfNeed(Integer.BYTES);
        byteBuffer.putInt(value);
    }

    @Override
    public void write(long value)
    {
        expandBufferIfNeed(Long.BYTES);
        byteBuffer.putLong(value);
    }

    @Override
    public void write(char value)
    {
        expandBufferIfNeed(Character.BYTES);
        byteBuffer.putChar(value);
    }

    @Override
    public void write(float value)
    {
        expandBufferIfNeed(Float.BYTES);
        byteBuffer.putFloat(value);
    }

    @Override
    public void write(double value)
    {
        expandBufferIfNeed(Double.BYTES);
        byteBuffer.putDouble(value);
    }

    @Override
    public void write(byte[] value)
    {
        expandBufferIfNeed(value.length * Byte.BYTES);
        byteBuffer.put(value);
    }

    @Override
    public void write(short[] value)
    {
        int addingDataSize = value.length * Short.BYTES;
        expandBufferIfNeed(addingDataSize);
        byteBuffer.asShortBuffer().put(value);

        // We do not need to change limit of buffer every time we write to it.
        // The only thing that should be updated is the cursor position.
        // The same applies to other {@code write()} overloads with array parameter.
        byteBuffer.position(byteBuffer.position() + addingDataSize);
    }

    @Override
    public void write(int[] value)
    {
        int addingDataSize = value.length * Integer.BYTES;
        expandBufferIfNeed(addingDataSize);
        byteBuffer.asIntBuffer().put(value);
        byteBuffer.position(byteBuffer.position() + addingDataSize);
    }

    @Override
    public void write(long[] value)
    {
        int addingDataSize = value.length * Long.BYTES;
        expandBufferIfNeed(addingDataSize);
        byteBuffer.asLongBuffer().put(value);
        byteBuffer.position(byteBuffer.position() + addingDataSize);
    }

    @Override
    public void write(char[] value)
    {
        int addingDataSize =value.length * Character.BYTES;
        expandBufferIfNeed(addingDataSize);
        byteBuffer.asCharBuffer().put(value);
        byteBuffer.position(byteBuffer.position() + addingDataSize);
    }

    @Override
    public void write(float[] value)
    {
        int addingDataSize =value.length * Float.BYTES;
        expandBufferIfNeed(addingDataSize);
        byteBuffer.asFloatBuffer().put(value);
        byteBuffer.position(byteBuffer.position() + addingDataSize);
    }

    @Override
    public void write(double[] value)
    {
        int addingDataSize =value.length * Double.BYTES;
        expandBufferIfNeed(addingDataSize);
        byteBuffer.asDoubleBuffer().put(value);
        byteBuffer.position(byteBuffer.position() + addingDataSize);
    }

    @Override
    public void commitBuffer()
    {
        byteBuffer.flip();
    }

    /**
     * Allocates and sets new ByteBuffer with the giving capacity.
     *
     * @param capacity Capacity of new ByteBuffer.
     */
    private void setByteBuffer(int capacity)
    {
        if (directBuffer)
        {
            byteBuffer = ByteBuffer.allocateDirect(capacity);
        }
        else
        {
            byteBuffer = ByteBuffer.allocate(capacity);
        }
    }

    /**
     * Expands buffer if current allocated size is not enough to write {@code addingDataSize}.
     *
     * @param addingDataSize Size of data that should be added to buffer.
     */
    private void expandBufferIfNeed(int addingDataSize)
    {
        int minimumRequiredSize = byteBuffer.position() + addingDataSize;
        if (minimumRequiredSize > byteBuffer.capacity())
        {
            ByteBuffer oldByteBuffer = byteBuffer;
            int newCapacity = bufferResizeStrategy.calculateResize(byteBuffer.capacity(), minimumRequiredSize);
            setByteBuffer(newCapacity);
            oldByteBuffer.flip();
            byteBuffer.put(oldByteBuffer);
        }
    }
}
