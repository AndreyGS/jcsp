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

package io.andreygs.jcsp.internal.processing.buffer;

import io.andreygs.jcsp.api.processing.buffer.AllocationType;
import io.andreygs.jcsp.api.processing.buffer.IBufferResizeStrategy;
import io.andreygs.jcsp.api.processing.buffer.dto.ISerializationBufferConfig;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Objects;

/**
 * This class works as buffer for writing raw data in CSP serialization process.
 * <p>
 * It is a wrapper over {@link ByteBuffer} optimized for use in CSP serialization process.
 */
public final class SerializationBuffer implements ISerializationBuffer
{

    private ByteBuffer byteBuffer;

    private final AllocationType allocationType;

    private final IBufferResizeStrategy bufferResizeStrategy;

    /**
     * Constructs an instance.
     *
     * @param serializationBufferConfig Buffer configuration.
     */
    public SerializationBuffer(ISerializationBufferConfig serializationBufferConfig)
    {
        this.allocationType = serializationBufferConfig.getAllocationType();
        setByteBuffer(serializationBufferConfig.getInitialCapacity());
        this.bufferResizeStrategy = Objects.requireNonNull(serializationBufferConfig.getResizeStrategy());
    }

    @Override
    public AllocationType getAllocationType()
    {
        return allocationType;
    }

    @Override
    public ByteBuffer getBuffer()
    {
        return byteBuffer;
    }

    @Override
    public void applyEndianness(ByteOrder byteOrder)
    {
        byteBuffer.order(byteOrder);
    }

    @Override
    public void writeByte(byte value)
    {
        expandBufferIfNeed(Byte.BYTES);
        byteBuffer.put(value);
    }

    @Override
    public void writeShort(short value)
    {
        expandBufferIfNeed(Short.BYTES);
        byteBuffer.putShort(value);
    }

    @Override
    public void writeInt(int value)
    {
        expandBufferIfNeed(Integer.BYTES);
        byteBuffer.putInt(value);
    }

    @Override
    public void writeLong(long value)
    {
        expandBufferIfNeed(Long.BYTES);
        byteBuffer.putLong(value);
    }

    @Override
    public void writeChar(char value)
    {
        expandBufferIfNeed(Character.BYTES);
        byteBuffer.putChar(value);
    }

    @Override
    public void writeFloat(float value)
    {
        expandBufferIfNeed(Float.BYTES);
        byteBuffer.putFloat(value);
    }

    @Override
    public void writeDouble(double value)
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
        int addingDataSize = value.length * Character.BYTES;
        expandBufferIfNeed(addingDataSize);
        byteBuffer.asCharBuffer().put(value);
        byteBuffer.position(byteBuffer.position() + addingDataSize);
    }

    @Override
    public void write(float[] value)
    {
        int addingDataSize = value.length * Float.BYTES;
        expandBufferIfNeed(addingDataSize);
        byteBuffer.asFloatBuffer().put(value);
        byteBuffer.position(byteBuffer.position() + addingDataSize);
    }

    @Override
    public void write(double[] value)
    {
        int addingDataSize = value.length * Double.BYTES;
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
     * <p>
     * It is expected that this method is only called from {@link @expandBufferIfNeed}
     * and Constructor (which in turn called from Factory, that filters not applicable values)
     * where precalculated capacity is a positive number.
     * So no {@link IllegalArgumentException} is awaiting here.
     *
     * @param capacity Capacity of new ByteBuffer. Must be positive.
     * @throws IllegalArgumentException if capacity turned out to be negative somehow.
     */
    private void setByteBuffer(int capacity)
    {
        if (allocationType == AllocationType.DIRECT)
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
     * @param addingDataSize Size of data that should be written to buffer. Must be positive.
     * @throws ArithmeticException if new buffer capacity size will overflow an int.
     */
    private void expandBufferIfNeed(int addingDataSize)
    {
        int minimumRequiredSize = Math.addExact(byteBuffer.position(), addingDataSize);
        if (minimumRequiredSize > byteBuffer.capacity())
        {
            ByteBuffer oldByteBuffer = byteBuffer;
            int newCapacity = bufferResizeStrategy.calculateNewSize(byteBuffer.capacity(), minimumRequiredSize);
            setByteBuffer(newCapacity);
            oldByteBuffer.flip();
            byteBuffer.put(oldByteBuffer);
        }
    }
}
