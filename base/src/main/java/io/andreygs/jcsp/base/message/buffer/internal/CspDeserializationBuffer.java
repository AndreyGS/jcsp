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

import io.andreygs.jcsp.base.message.buffer.ICspDeserializationBuffer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * This class works as buffer for reading raw data in CSP deserialization process.
 * <p/>
 * It is a wrapper over {@link ByteBuffer} optimized for use in CSP deserialization process.
 */
public final class CspDeserializationBuffer
    implements ICspDeserializationBuffer
{
    /**
     * {@link ByteBuffer} that will be used in operations.
     */
    private final ByteBuffer byteBuffer;

    /**
     * Is {@link ByteBuffer} a direct buffer.
     *
     * @see ByteBuffer
     */
    private boolean directBuffer;

    /**
     * Constructs CspDeserializationByteBuffer.
     *
     * @param byteBuffer Buffer that contains CSP serialized data.
     */
    CspDeserializationBuffer(ByteBuffer byteBuffer)
    {
        this.byteBuffer = byteBuffer;
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
    public byte readByte()
    {
        return byteBuffer.get();
    }

    @Override
    public short readShort()
    {
        return byteBuffer.getShort();
    }

    @Override
    public int readInt()
    {
        return byteBuffer.getInt();
    }

    @Override
    public long readLong()
    {
        return byteBuffer.getLong();
    }

    @Override
    public char readChar()
    {
        return byteBuffer.getChar();
    }

    @Override
    public float readFloat()
    {
        return byteBuffer.getFloat();
    }

    @Override
    public double readDouble()
    {
        return byteBuffer.getDouble();
    }

    @Override
    public void read(byte[] value)
    {
        byteBuffer.get(value);
    }

    @Override
    public void read(short[] value)
    {
        byteBuffer.asShortBuffer().get(value);
        int readDataSize = value.length * Short.BYTES;
        byteBuffer.position(byteBuffer.position() + readDataSize);
    }

    @Override
    public void read(int[] value)
    {
        byteBuffer.asIntBuffer().get(value);
        int readDataSize = value.length * Integer.BYTES;
        byteBuffer.position(byteBuffer.position() + readDataSize);
    }

    @Override
    public void read(long[] value)
    {
        byteBuffer.asLongBuffer().get(value);
        int readDataSize = value.length * Long.BYTES;
        byteBuffer.position(byteBuffer.position() + readDataSize);
    }

    @Override
    public void read(char[] value)
    {
        byteBuffer.asCharBuffer().get(value);
        int readDataSize = value.length * Character.BYTES;
        byteBuffer.position(byteBuffer.position() + readDataSize);
    }

    @Override
    public void read(float[] value)
    {
        byteBuffer.asFloatBuffer().get(value);
        int readDataSize = value.length * Float.BYTES;
        byteBuffer.position(byteBuffer.position() + readDataSize);
    }

    @Override
    public void read(double[] value)
    {
        byteBuffer.asDoubleBuffer().get(value);
        int readDataSize = value.length * Double.BYTES;
        byteBuffer.position(byteBuffer.position() + readDataSize);
    }
}
