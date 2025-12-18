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

package io.andreygs.jcsp.base.message.internal;

import io.andreygs.jcsp.base.utils.IBufferResizeStrategy;
import io.andreygs.jcsp.base.utils.internal.BufferDoublingResizeStrategy;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * This class works as buffer for writing raw data in serialization process.
 */
public class CspSerializationByteBuffer
{
    /**
     * Default capacity of {@link ByteBuffer internal buffer} when it is created, if no explicit value is provided.
     */
    public static final int DEFAULT_CAPACITY_SIZE = 256;

    /**
     * {@link ByteBuffer internal buffer} that will be used in operations.
     */
    private ByteBuffer byteBuffer;

    /**
     * Does {@link ByteBuffer internal buffer} contain a direct buffer.
     *
     * @see ByteBuffer
     */
    private final Boolean directBuffer;

    /**
     * Strategy of {@link ByteBuffer internal buffer} resizing.
     */
    private final IBufferResizeStrategy bufferResizeStrategy;
    
    /**
     * Creates CspSerializationByteBuffer with default parameters.
     * <p/>
     * Default parameters are:
     * <ul>
     *     <li>Initial {@link ByteBuffer internal buffer} capacity is equal to {@link #DEFAULT_CAPACITY_SIZE}.</li>
     *     <li>Internal buffer is {@link ByteBuffer#isDirect() direct}.</li>
     *     <li>Internal buffer resizing strategy is {@link BufferDoublingResizeStrategy}.</li>
     * </ul>
     *
     * @return instance of CspSerializationByteBuffer.
     */
    public static CspSerializationByteBuffer createDefault()
    {
        return new CspSerializationByteBuffer(DEFAULT_CAPACITY_SIZE, true, BufferDoublingResizeStrategy.INSTANCE);
    }

    /**
     * Creates CspSerializationByteBuffer.
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
    public static CspSerializationByteBuffer create(@Nullable Integer initialBufferCapacity, @Nullable Boolean directBuffer,
                                                    @Nullable IBufferResizeStrategy bufferResizeStrategy)
    {
        return new CspSerializationByteBuffer(
            initialBufferCapacity != null ? initialBufferCapacity : DEFAULT_CAPACITY_SIZE,
            directBuffer != null ? directBuffer : true,
            bufferResizeStrategy != null ? bufferResizeStrategy : BufferDoublingResizeStrategy.INSTANCE);
    }

    /**
     * Constructs CspSerializationByteBuffer.
     *
     * @param initialBufferCapacity Initial capacity of buffer. Must not be negative.
     * @param directBuffer Is buffer direct or not.
     *                     You should consider that direct buffer may not have underlying array that can be retrieved.
     *                     Non-direct buffer, otherwise will always have underlying array, and it can be used in all
     *                     cases safely, but it has at least one additional copying when passed to any native function,
     *                     including methods of sockets and streams.
     *                     So if you want to pass data by net buffer should be direct. But many cryptographic libraries,
     *                     on the other hand, are using byte[], and you must either use non-direct buffer or
     *                     convert make additionally copy to byte[] from direct buffer manually.
     * @param bufferResizeStrategy Strategy of buffer resizing.
     * @see ByteBuffer
     */
    private CspSerializationByteBuffer(int initialBufferCapacity, boolean directBuffer,
                                       IBufferResizeStrategy bufferResizeStrategy)
    {
        this.directBuffer = directBuffer;
        setByteBuffer(initialBufferCapacity);
        this.bufferResizeStrategy = bufferResizeStrategy;
    }

    /**
     * Gets whether underlying storage has direct buffer or not.
     *
     * @return direct buffer or not.
     */
    public boolean isDirectBuffer()
    {
        return directBuffer;
    }

    /**
     * Gets underlying buffer.
     *
     * @return underlying buffer.
     */
    public ByteBuffer getByteBuffer()
    {
        return byteBuffer;
    }

    /**
     * Writes single byte value to buffer.
     *
     * @param value Value to write.
     */
    public void write(byte value)
    {
        expandBufferIfNeed(Byte.BYTES);
        byteBuffer.put(value);
    }

    /**
     * Writes single short value to buffer.
     *
     * @param value Value to write.
     */
    public void write(short value)
    {
        expandBufferIfNeed(Short.BYTES);
        byteBuffer.putShort(value);
    }

    /**
     * Writes single int value to buffer.
     *
     * @param value Value to write.
     */
    public void write(int value)
    {
        expandBufferIfNeed(Integer.BYTES);
        byteBuffer.putInt(value);
    }

    /**
     * Writes single long value to buffer.
     *
     * @param value Value to write.
     */
    public void write(long value)
    {
        expandBufferIfNeed(Long.BYTES);
        byteBuffer.putLong(value);
    }

    /**
     * Writes single char value to buffer.
     *
     * @param value Value to write.
     */
    public void write(char value)
    {
        expandBufferIfNeed(Character.BYTES);
        byteBuffer.putChar(value);
    }

    /**
     * Writes single float value to buffer.
     *
     * @param value Value to write.
     */
    public void write(float value)
    {
        expandBufferIfNeed(Float.BYTES);
        byteBuffer.putFloat(value);
    }

    /**
     * Writes single double value to buffer.
     *
     * @param value Value to write.
     */
    public void write(double value)
    {
        expandBufferIfNeed(Double.BYTES);
        byteBuffer.putDouble(value);
    }

    /**
     * Writes byte array value to buffer.
     *
     * @param value Value to write.
     */
    public void write(byte[] value)
    {
        expandBufferIfNeed(value.length * Byte.BYTES);
        byteBuffer.put(value);
    }

    /**
     * Writes short array value to buffer.
     *
     * @param value Value to write.
     */
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

    /**
     * Writes int array value to buffer.
     *
     * @param value Value to write.
     */
    public void write(int[] value)
    {
        int addingDataSize = value.length * Integer.BYTES;
        expandBufferIfNeed(addingDataSize);
        byteBuffer.asIntBuffer().put(value);
        byteBuffer.position(byteBuffer.position() + addingDataSize);
    }

    /**
     * Writes long array value to buffer.
     *
     * @param value Value to write.
     */
    public void write(long[] value)
    {
        int addingDataSize = value.length * Long.BYTES;
        expandBufferIfNeed(addingDataSize);
        byteBuffer.asLongBuffer().put(value);
        byteBuffer.position(byteBuffer.position() + addingDataSize);
    }

    /**
     * Writes char array value to buffer.
     *
     * @param value Value to write.
     */
    public void write(char[] value)
    {
        int addingDataSize =value.length * Character.BYTES;
        expandBufferIfNeed(addingDataSize);
        byteBuffer.asCharBuffer().put(value);
        byteBuffer.position(byteBuffer.position() + addingDataSize);
    }

    /**
     * Writes float array value to buffer.
     *
     * @param value Value to write.
     */
    public void write(float[] value)
    {
        int addingDataSize =value.length * Float.BYTES;
        expandBufferIfNeed(addingDataSize);
        byteBuffer.asFloatBuffer().put(value);
        byteBuffer.position(byteBuffer.position() + addingDataSize);
    }

    /**
     * Writes double array value to buffer.
     *
     * @param value Value to write.
     */
    public void write(double[] value)
    {
        int addingDataSize =value.length * Double.BYTES;
        expandBufferIfNeed(addingDataSize);
        byteBuffer.asDoubleBuffer().put(value);
        byteBuffer.position(byteBuffer.position() + addingDataSize);
    }

    /**
     * Applies endianness to underlying ByteBuffer operations.
     *
     * @param byteOrder Endianness byte order.
     * @see ByteBuffer#order()
     */
    public void applyEndianness(ByteOrder byteOrder)
    {
        byteBuffer.order(byteOrder);
    }

    /**
     * Commits buffer, when serialization is completed.
     * <p>
     * It must be called only once, after last write operation.
     * It internally calls {@link ByteBuffer#flip}
     */
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
     * @param addingDataSize .
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
