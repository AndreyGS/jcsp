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

package io.andreygs.jcsp_base.message.internal;

import io.andreygs.jcsp_base.utils.api.IBufferResizeStrategy;
import io.andreygs.jcsp_base.utils.internal.BufferDoublingResizeStrategy;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class CspSerializationByteBuffer
{
    @SuppressWarnings({"NotNullFieldNotInitialized", "null", "RedundantSuppression"})
    private ByteBuffer byteBuffer;
    private boolean directBuffer;
    private IBufferResizeStrategy bufferResizeStrategy;

    private static final int DEFAULT_CAPACITY_SIZE = 256;

    public CspSerializationByteBuffer(boolean directBuffer, @Nullable IBufferResizeStrategy bufferResizeStrategy)
    {
        init(directBuffer, bufferResizeStrategy);
    }

    public boolean isDirectBuffer()
    {
        return directBuffer;
    }

    public ByteBuffer getByteBuffer()
    {
        return byteBuffer;
    }

    public void write(byte value)
    {
        expandBufferIfNeed(Byte.BYTES);
        byteBuffer.put(value);
    }

    public void write(short value)
    {
        expandBufferIfNeed(Short.BYTES);
        byteBuffer.putShort(value);
    }

    public void write(int value)
    {
        expandBufferIfNeed(Integer.BYTES);
        byteBuffer.putInt(value);
    }

    public void write(long value)
    {
        expandBufferIfNeed(Long.BYTES);
        byteBuffer.putLong(value);
    }

    public void write(char value)
    {
        expandBufferIfNeed(Character.BYTES);
        byteBuffer.putChar(value);
    }

    public void write(float value)
    {
        expandBufferIfNeed(Float.BYTES);
        byteBuffer.putFloat(value);
    }

    public void write(double value)
    {
        expandBufferIfNeed(Double.BYTES);
        byteBuffer.putDouble(value);
    }

    public void write(byte[] value)
    {
        expandBufferIfNeed(value.length * Byte.BYTES);
        byteBuffer.put(value);
    }

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

    public void write(int[] value)
    {
        int addingDataSize = value.length * Integer.BYTES;
        expandBufferIfNeed(addingDataSize);
        byteBuffer.asIntBuffer().put(value);
        byteBuffer.position(byteBuffer.position() + addingDataSize);
    }

    public void write(long[] value)
    {
        int addingDataSize =value.length * Long.BYTES;
        expandBufferIfNeed(addingDataSize);
        byteBuffer.asLongBuffer().put(value);
        byteBuffer.position(byteBuffer.position() + addingDataSize);
    }

    public void write(char[] value)
    {
        int addingDataSize =value.length * Character.BYTES;
        expandBufferIfNeed(addingDataSize);
        byteBuffer.asCharBuffer().put(value);
        byteBuffer.position(byteBuffer.position() + addingDataSize);
    }

    public void write(float[] value)
    {
        int addingDataSize =value.length * Float.BYTES;
        expandBufferIfNeed(addingDataSize);
        byteBuffer.asFloatBuffer().put(value);
        byteBuffer.position(byteBuffer.position() + addingDataSize);
    }

    public void write(double[] value)
    {
        int addingDataSize =value.length * Double.BYTES;
        expandBufferIfNeed(addingDataSize);
        byteBuffer.asDoubleBuffer().put(value);
        byteBuffer.position(byteBuffer.position() + addingDataSize);
    }

    public void endiannessToWriteOperations(ByteOrder byteOrder)
    {
        byteBuffer.order(byteOrder);
    }

    public void setBufferResizeStrategy(IBufferResizeStrategy bufferResizeStrategy)
    {
        this.bufferResizeStrategy = bufferResizeStrategy;
    }

    public void commitBuffer()
    {
        byteBuffer.flip();
    }

    public int size()
    {
        return byteBuffer.limit();
    }

    private void init(boolean directBuffer, @Nullable IBufferResizeStrategy bufferResizeStrategy)
    {
        this.directBuffer = directBuffer;
        setByteBuffer(DEFAULT_CAPACITY_SIZE);
        this.bufferResizeStrategy = bufferResizeStrategy != null ? bufferResizeStrategy : new BufferDoublingResizeStrategy();
    }

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
