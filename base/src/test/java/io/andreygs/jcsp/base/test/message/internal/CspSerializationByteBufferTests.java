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

import io.andreygs.jcsp.base.message.internal.CspSerializationByteBuffer;
import io.andreygs.jcsp.base.test.CommonUtils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * TODO: place description here
 */
public class CspSerializationByteBufferTests
{
    @Test
    void writeByte()
    {
        writePrimitive((byte) 100, (byte) 101, CspSerializationByteBuffer::write, ByteBuffer::get);
    }

    @Test
    void writeShort()
    {
        writePrimitive((short) 10000, (short) 10001, CspSerializationByteBuffer::write, ByteBuffer::getShort);
    }

    @Test
    void writeInt()
    {
        writePrimitive(10000000, 10000001, CspSerializationByteBuffer::write, ByteBuffer::getInt);
    }

    @Test
    void writeLong()
    {
        writePrimitive(10000000000L, 10000000001L, CspSerializationByteBuffer::write, ByteBuffer::getLong);
    }

    @Test
    void writeChar()
    {
        writePrimitive('a', 'b', CspSerializationByteBuffer::write, ByteBuffer::getChar);
    }

    @Test
    void writeFloat()
    {
        writePrimitive(10000000f, 10000001f, CspSerializationByteBuffer::write, ByteBuffer::getFloat);
    }

    @Test
    void writeDouble()
    {
        writePrimitive(1000000000000000000d, 1000000000000000001d, CspSerializationByteBuffer::write, ByteBuffer::getDouble);
    }

    @Test
    void writeByteArray()
    {
        CspSerializationByteBuffer cspSerializationByteBuffer = CspSerializationByteBuffer.createDefault();

        byte[] value1 = { 1, 2, 3 };
        byte[] value2 = { 4, 5, 6, 7 };
        cspSerializationByteBuffer.write(value1);
        cspSerializationByteBuffer.write(value2);
        cspSerializationByteBuffer.commitBuffer();

        int sizeOfWrittenValue = value1.length + value2.length;
        Assertions.assertEquals(cspSerializationByteBuffer.getByteBuffer().limit(), sizeOfWrittenValue);

        byte[] writtenValue1 = new byte[3];
        byte[] writtenValue2 = new byte[4];
        cspSerializationByteBuffer.getByteBuffer().get(writtenValue1);
        cspSerializationByteBuffer.getByteBuffer().get(writtenValue2);
        Assertions.assertArrayEquals(value1, writtenValue1);
        Assertions.assertArrayEquals(value2, writtenValue2);
    }

    @Test
    void writeShortArray()
    {
        CspSerializationByteBuffer cspSerializationByteBuffer = CspSerializationByteBuffer.createDefault();

        short[] value1 = { 1, 2, 3 };
        short[] value2 = { 4, 5, 6, 7 };
        cspSerializationByteBuffer.write(value1);
        cspSerializationByteBuffer.write(value2);
        cspSerializationByteBuffer.commitBuffer();

        int sizeOfWrittenValue = CommonUtils.getPrimitiveSize(value1[0]) * (value1.length + value2.length);
        Assertions.assertEquals(cspSerializationByteBuffer.getByteBuffer().limit(), sizeOfWrittenValue);

        short[] writtenValue1 = new short[3];
        short[] writtenValue2 = new short[4];
        ShortBuffer shortBuffer = cspSerializationByteBuffer.getByteBuffer().asShortBuffer();
        shortBuffer.get(writtenValue1);
        shortBuffer.get(writtenValue2);
        Assertions.assertArrayEquals(value1, writtenValue1);
        Assertions.assertArrayEquals(value2, writtenValue2);
    }

    @Test
    void writeIntArray()
    {
        CspSerializationByteBuffer cspSerializationByteBuffer = CspSerializationByteBuffer.createDefault();

        int[] value1 = { 1, 2, 3 };
        int[] value2 = { 4, 5, 6, 7 };
        cspSerializationByteBuffer.write(value1);
        cspSerializationByteBuffer.write(value2);
        cspSerializationByteBuffer.commitBuffer();

        int sizeOfWrittenValue = CommonUtils.getPrimitiveSize(value1[0]) * (value1.length + value2.length);
        Assertions.assertEquals(cspSerializationByteBuffer.getByteBuffer().limit(), sizeOfWrittenValue);

        int[] writtenValue1 = new int[3];
        int[] writtenValue2 = new int[4];
        IntBuffer intBuffer = cspSerializationByteBuffer.getByteBuffer().asIntBuffer();
        intBuffer.get(writtenValue1);
        intBuffer.get(writtenValue2);
        Assertions.assertArrayEquals(value1, writtenValue1);
        Assertions.assertArrayEquals(value2, writtenValue2);
    }


    @Test
    void writeLongArray()
    {
        CspSerializationByteBuffer cspSerializationByteBuffer = CspSerializationByteBuffer.createDefault();

        long[] value1 = { 1, 2, 3 };
        long[] value2 = { 4, 5, 6, 7 };
        cspSerializationByteBuffer.write(value1);
        cspSerializationByteBuffer.write(value2);
        cspSerializationByteBuffer.commitBuffer();

        int sizeOfWrittenValue = CommonUtils.getPrimitiveSize(value1[0]) * (value1.length + value2.length);
        Assertions.assertEquals(cspSerializationByteBuffer.getByteBuffer().limit(), sizeOfWrittenValue);

        long[] writtenValue1 = new long[3];
        long[] writtenValue2 = new long[4];
        LongBuffer longBuffer = cspSerializationByteBuffer.getByteBuffer().asLongBuffer();
        longBuffer.get(writtenValue1);
        longBuffer.get(writtenValue2);
        Assertions.assertArrayEquals(value1, writtenValue1);
        Assertions.assertArrayEquals(value2, writtenValue2);
    }

    @Test
    void writeCharArray()
    {
        CspSerializationByteBuffer cspSerializationByteBuffer = CspSerializationByteBuffer.createDefault();

        char[] value1 = { 1, 2, 3 };
        char[] value2 = { 4, 5, 6, 7 };
        cspSerializationByteBuffer.write(value1);
        cspSerializationByteBuffer.write(value2);
        cspSerializationByteBuffer.commitBuffer();

        int sizeOfWrittenValue = CommonUtils.getPrimitiveSize(value1[0]) * (value1.length + value2.length);
        Assertions.assertEquals(cspSerializationByteBuffer.getByteBuffer().limit(), sizeOfWrittenValue);

        char[] writtenValue1 = new char[3];
        char[] writtenValue2 = new char[4];
        CharBuffer charBuffer = cspSerializationByteBuffer.getByteBuffer().asCharBuffer();
        charBuffer.get(writtenValue1);
        charBuffer.get(writtenValue2);
        Assertions.assertArrayEquals(value1, writtenValue1);
        Assertions.assertArrayEquals(value2, writtenValue2);
    }

    @Test
    void writeFloatArray()
    {
        CspSerializationByteBuffer cspSerializationByteBuffer = CspSerializationByteBuffer.createDefault();

        float[] value1 = { 1, 2, 3 };
        float[] value2 = { 4, 5, 6, 7 };
        cspSerializationByteBuffer.write(value1);
        cspSerializationByteBuffer.write(value2);
        cspSerializationByteBuffer.commitBuffer();

        int sizeOfWrittenValue = CommonUtils.getPrimitiveSize(value1[0]) * (value1.length + value2.length);
        Assertions.assertEquals(cspSerializationByteBuffer.getByteBuffer().limit(), sizeOfWrittenValue);

        float[] writtenValue1 = new float[3];
        float[] writtenValue2 = new float[4];
        FloatBuffer floatBuffer = cspSerializationByteBuffer.getByteBuffer().asFloatBuffer();
        floatBuffer.get(writtenValue1);
        floatBuffer.get(writtenValue2);
        Assertions.assertArrayEquals(value1, writtenValue1);
        Assertions.assertArrayEquals(value2, writtenValue2);
    }

    @Test
    void writeDoubleArray()
    {
        CspSerializationByteBuffer cspSerializationByteBuffer = CspSerializationByteBuffer.createDefault();

        double[] value1 = { 1, 2, 3 };
        double[] value2 = { 4, 5, 6, 7 };
        cspSerializationByteBuffer.write(value1);
        cspSerializationByteBuffer.write(value2);
        cspSerializationByteBuffer.commitBuffer();

        int sizeOfWrittenValue = CommonUtils.getPrimitiveSize(value1[0]) * (value1.length + value2.length);
        Assertions.assertEquals(cspSerializationByteBuffer.getByteBuffer().limit(), sizeOfWrittenValue);

        double[] writtenValue1 = new double[3];
        double[] writtenValue2 = new double[4];
        DoubleBuffer doubleBuffer = cspSerializationByteBuffer.getByteBuffer().asDoubleBuffer();
        doubleBuffer.get(writtenValue1);
        doubleBuffer.get(writtenValue2);
        Assertions.assertArrayEquals(value1, writtenValue1);
        Assertions.assertArrayEquals(value2, writtenValue2);
    }

    @Test
    void testDefaultConstructor()
    {

    }

    private <T> void writePrimitive(T value1, T value2, BiConsumer<CspSerializationByteBuffer, T> writeFunction,
                                    Function<ByteBuffer, T> testValueFunction)
    {
        CspSerializationByteBuffer cspSerializationByteBuffer = CspSerializationByteBuffer.createDefault();

        writeFunction.accept(cspSerializationByteBuffer, value1);
        writeFunction.accept(cspSerializationByteBuffer, value2);
        cspSerializationByteBuffer.commitBuffer();

        ByteBuffer byteBuffer = cspSerializationByteBuffer.getByteBuffer();
        Assertions.assertEquals(byteBuffer.limit(), CommonUtils.getPrimitiveSize(value1) * 2);

        Assertions.assertEquals(value1, testValueFunction.apply(byteBuffer));
        Assertions.assertEquals(value2, testValueFunction.apply(byteBuffer));
    }
}
