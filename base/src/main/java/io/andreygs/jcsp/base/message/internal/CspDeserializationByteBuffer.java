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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * TODO: place description here
 */
public class CspDeserializationByteBuffer
{
    private final ByteBuffer byteBuffer;
    private boolean directBuffer;

    public static CspDeserializationByteBuffer create(ByteBuffer byteBuffer)
    {
        return new CspDeserializationByteBuffer(byteBuffer);
    }

    private CspDeserializationByteBuffer(ByteBuffer byteBuffer)
    {
        this.byteBuffer = byteBuffer;
    }

    public ByteBuffer getByteBuffer()
    {
        return byteBuffer;
    }

    public byte readByte()
    {
        return byteBuffer.get();
    }

    public short readShort()
    {
        return byteBuffer.getShort();
    }

    public int readInt()
    {
        return byteBuffer.getInt();
    }

    public long readLong()
    {
        return byteBuffer.getLong();
    }

    public char readChar()
    {
        return byteBuffer.getChar();
    }

    public float readFloat()
    {
        return byteBuffer.getFloat();
    }

    public double readDouble()
    {
        return byteBuffer.getDouble();
    }

    public void read(byte[] value)
    {
        byteBuffer.get(value);
    }

    public void read(short[] value)
    {
        byteBuffer.asShortBuffer().get(value);
        int readDataSize = value.length * Short.BYTES;
        byteBuffer.position(byteBuffer.position() + readDataSize);
    }

    public void read(int[] value)
    {
        byteBuffer.asIntBuffer().get(value);
        int readDataSize = value.length * Integer.BYTES;
        byteBuffer.position(byteBuffer.position() + readDataSize);
    }

    public void read(long[] value)
    {
        byteBuffer.asLongBuffer().get(value);
        int readDataSize = value.length * Long.BYTES;
        byteBuffer.position(byteBuffer.position() + readDataSize);
    }

    public void read(char[] value)
    {
        byteBuffer.asCharBuffer().get(value);
        int readDataSize = value.length * Character.BYTES;
        byteBuffer.position(byteBuffer.position() + readDataSize);
    }

    public void read(float[] value)
    {
        byteBuffer.asFloatBuffer().get(value);
        int readDataSize = value.length * Float.BYTES;
        byteBuffer.position(byteBuffer.position() + readDataSize);
    }

    public void read(double[] value)
    {
        byteBuffer.asDoubleBuffer().get(value);
        int readDataSize = value.length * Double.BYTES;
        byteBuffer.position(byteBuffer.position() + readDataSize);
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
}
