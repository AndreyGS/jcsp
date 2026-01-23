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

package io.andreygs.jcsp.base.test.message.internal;

import io.andreygs.jcsp.base.message.buffer.internal.ICspDeserializationBuffer;
import io.andreygs.jcsp.base.test.CommonUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Unit-tests for {@link ICspDeserializationBuffer} contract.
 */
public abstract class AbstractICspDeserializationBufferTests
{
    @Test
    public void getByteBufferTest()
    {
        ByteBuffer byteBuffer = ByteBuffer.allocate(0);
        ICspDeserializationBuffer cspDeserializationBuffer = createCspDeserializationBuffer(byteBuffer);

        Assertions.assertEquals(byteBuffer, cspDeserializationBuffer.getByteBuffer());
    }

    @Test
    public void readByteTest()
    {
        readPrimitive((byte)1, (byte)105, ByteBuffer::put, ICspDeserializationBuffer::readByte);
    }

    @Test
    public void readShortTest()
    {
        readPrimitive((short)11, (short)1050, ByteBuffer::putShort, ICspDeserializationBuffer::readShort);
    }

    @Test
    public void readIntTest()
    {
        readPrimitive(111, 105000, ByteBuffer::putInt, ICspDeserializationBuffer::readInt);
    }

    @Test
    public void readLongTest()
    {
        readPrimitive((long)1111, 105000000000L, ByteBuffer::putLong, ICspDeserializationBuffer::readLong);
    }

    @Test
    public void readCharTest()
    {
        readPrimitive((char)11, (char)1050, ByteBuffer::putChar, ICspDeserializationBuffer::readChar);
    }

    @Test
    public void readFloatTest()
    {
        readPrimitive(11.1F, 1050.0F, ByteBuffer::putFloat, ICspDeserializationBuffer::readFloat);
    }

    @Test
    public void readDoubleTest()
    {
        readPrimitive(11.1D, 1050.01D, ByteBuffer::putDouble, ICspDeserializationBuffer::readDouble);
    }

    @Test
    public void readByteArrayTest()
    {
        ByteBuffer byteBuffer = ByteBuffer.allocate(128);

        byte[] value1 = { 1, 2, 3 };
        byte[] value2 = { 4, 5, 6, 7 };
        byteBuffer.put(value1);
        byteBuffer.put(value2);
        byteBuffer.flip();

        byte[] readValue1 = new byte[3];
        byte[] readValue2 = new byte[4];

        ICspDeserializationBuffer cspDeserializationBuffer = createCspDeserializationBuffer(byteBuffer);
        cspDeserializationBuffer.read(readValue1);
        cspDeserializationBuffer.read(readValue2);

        Assertions.assertArrayEquals(value1, readValue1);
        Assertions.assertArrayEquals(value2, readValue2);
    }

    @Test
    public void readShortArrayTest()
    {
        ByteBuffer byteBuffer = ByteBuffer.allocate(128);
        ShortBuffer shortBuffer = byteBuffer.asShortBuffer();

        short[] value1 = { 1, 2, 3 };
        short[] value2 = { 4, 5, 6, 7 };
        shortBuffer.put(value1);
        shortBuffer.put(value2);

        int sizeOfWrittenValue = CommonUtils.getPrimitiveSize(value1[0]) * (value1.length + value2.length);
        byteBuffer.position(sizeOfWrittenValue);
        byteBuffer.flip();

        short[] readValue1 = new short[3];
        short[] readValue2 = new short[4];

        ICspDeserializationBuffer cspDeserializationBuffer = createCspDeserializationBuffer(byteBuffer);

        cspDeserializationBuffer.read(readValue1);
        cspDeserializationBuffer.read(readValue2);

        Assertions.assertArrayEquals(value1, readValue1);
        Assertions.assertArrayEquals(value2, readValue2);
    }

    @Test
    public void readIntArrayTest()
    {
        ByteBuffer byteBuffer = ByteBuffer.allocate(128);
        IntBuffer shortBuffer = byteBuffer.asIntBuffer();

        int[] value1 = { 1, 2, 3 };
        int[] value2 = { 4, 5, 6, 7 };
        shortBuffer.put(value1);
        shortBuffer.put(value2);

        int sizeOfWrittenValue = CommonUtils.getPrimitiveSize(value1[0]) * (value1.length + value2.length);
        byteBuffer.position(sizeOfWrittenValue);
        byteBuffer.flip();

        int[] readValue1 = new int[3];
        int[] readValue2 = new int[4];

        ICspDeserializationBuffer cspDeserializationBuffer = createCspDeserializationBuffer(byteBuffer);

        cspDeserializationBuffer.read(readValue1);
        cspDeserializationBuffer.read(readValue2);

        Assertions.assertArrayEquals(value1, readValue1);
        Assertions.assertArrayEquals(value2, readValue2);
    }

    @Test
    public void readLongArrayTest()
    {
        ByteBuffer byteBuffer = ByteBuffer.allocate(128);
        LongBuffer shortBuffer = byteBuffer.asLongBuffer();

        long[] value1 = { 1, 2, 3 };
        long[] value2 = { 4, 5, 6, 7 };
        shortBuffer.put(value1);
        shortBuffer.put(value2);

        int sizeOfWrittenValue = CommonUtils.getPrimitiveSize(value1[0]) * (value1.length + value2.length);
        byteBuffer.position(sizeOfWrittenValue);
        byteBuffer.flip();

        long[] readValue1 = new long[3];
        long[] readValue2 = new long[4];

        ICspDeserializationBuffer cspDeserializationBuffer = createCspDeserializationBuffer(byteBuffer);

        cspDeserializationBuffer.read(readValue1);
        cspDeserializationBuffer.read(readValue2);

        Assertions.assertArrayEquals(value1, readValue1);
        Assertions.assertArrayEquals(value2, readValue2);
    }

    @Test
    public void readCharArrayTest()
    {
        ByteBuffer byteBuffer = ByteBuffer.allocate(128);
        CharBuffer shortBuffer = byteBuffer.asCharBuffer();

        char[] value1 = { 1, 2, 3 };
        char[] value2 = { 4, 5, 6, 7 };
        shortBuffer.put(value1);
        shortBuffer.put(value2);

        int sizeOfWrittenValue = CommonUtils.getPrimitiveSize(value1[0]) * (value1.length + value2.length);
        byteBuffer.position(sizeOfWrittenValue);
        byteBuffer.flip();

        char[] readValue1 = new char[3];
        char[] readValue2 = new char[4];

        ICspDeserializationBuffer cspDeserializationBuffer = createCspDeserializationBuffer(byteBuffer);

        cspDeserializationBuffer.read(readValue1);
        cspDeserializationBuffer.read(readValue2);

        Assertions.assertArrayEquals(value1, readValue1);
        Assertions.assertArrayEquals(value2, readValue2);
    }

    @Test
    public void readFloatArrayTest()
    {
        ByteBuffer byteBuffer = ByteBuffer.allocate(128);
        FloatBuffer shortBuffer = byteBuffer.asFloatBuffer();

        float[] value1 = { 1, 2, 3 };
        float[] value2 = { 4, 5, 6, 7 };
        shortBuffer.put(value1);
        shortBuffer.put(value2);

        int sizeOfWrittenValue = CommonUtils.getPrimitiveSize(value1[0]) * (value1.length + value2.length);
        byteBuffer.position(sizeOfWrittenValue);
        byteBuffer.flip();

        float[] readValue1 = new float[3];
        float[] readValue2 = new float[4];

        ICspDeserializationBuffer cspDeserializationBuffer = createCspDeserializationBuffer(byteBuffer);

        cspDeserializationBuffer.read(readValue1);
        cspDeserializationBuffer.read(readValue2);

        Assertions.assertArrayEquals(value1, readValue1);
        Assertions.assertArrayEquals(value2, readValue2);
    }

    @Test
    public void readDoubleArrayTest()
    {
        ByteBuffer byteBuffer = ByteBuffer.allocate(128);
        DoubleBuffer shortBuffer = byteBuffer.asDoubleBuffer();

        double[] value1 = { 1, 2, 3 };
        double[] value2 = { 4, 5, 6, 7 };
        shortBuffer.put(value1);
        shortBuffer.put(value2);

        int sizeOfWrittenValue = CommonUtils.getPrimitiveSize(value1[0]) * (value1.length + value2.length);
        byteBuffer.position(sizeOfWrittenValue);
        byteBuffer.flip();

        double[] readValue1 = new double[3];
        double[] readValue2 = new double[4];

        ICspDeserializationBuffer cspDeserializationBuffer = createCspDeserializationBuffer(byteBuffer);

        cspDeserializationBuffer.read(readValue1);
        cspDeserializationBuffer.read(readValue2);

        Assertions.assertArrayEquals(value1, readValue1);
        Assertions.assertArrayEquals(value2, readValue2);
    }

    @Test
    public void applyEndiannessTest()
    {
        ByteBuffer byteBuffer = ByteBuffer.allocate(32);
        ICspDeserializationBuffer cspDeserializationBuffer = createCspDeserializationBuffer(byteBuffer);
        cspDeserializationBuffer.applyEndianness(ByteOrder.BIG_ENDIAN);

        Assertions.assertEquals(ByteOrder.BIG_ENDIAN, cspDeserializationBuffer.getByteBuffer().order());

        cspDeserializationBuffer.applyEndianness(ByteOrder.LITTLE_ENDIAN);
        Assertions.assertEquals(ByteOrder.LITTLE_ENDIAN, cspDeserializationBuffer.getByteBuffer().order());
    }

    private <T> void readPrimitive(T value1, T value2, BiConsumer<ByteBuffer, T> writeFunction,
                                   Function<ICspDeserializationBuffer, T> readValueFunction)
    {
        ByteBuffer byteBuffer = ByteBuffer.allocate(32);
        writeFunction.accept(byteBuffer, value1);
        writeFunction.accept(byteBuffer, value2);
        byteBuffer.flip();

        ICspDeserializationBuffer cspDeserializationBuffer = createCspDeserializationBuffer(byteBuffer);

        Assertions.assertEquals(value1, readValueFunction.apply(cspDeserializationBuffer));
        Assertions.assertEquals(value2, readValueFunction.apply(cspDeserializationBuffer));
    }

    protected abstract ICspDeserializationBuffer createCspDeserializationBuffer(ByteBuffer byteBuffer);
}
