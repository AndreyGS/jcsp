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

import io.andreygs.jcsp.base.message.buffer.internal.ICspSerializationBuffer;
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
 * Unit-tests for {@link ICspSerializationBuffer} contract.
 */
public abstract class AbstractICspSerializationBufferTests
{
    @Test
    public void getByteBufferTest()
    {
        ICspSerializationBuffer cspSerializationBuffer = createCspSerializationBuffer(null, null);
        Assertions.assertNotNull(cspSerializationBuffer.getByteBuffer());
    }

    @Test
    public void applyEndiannessTest()
    {
        ICspSerializationBuffer cspSerializationBuffer = createCspSerializationBuffer(null, null);
        cspSerializationBuffer.applyEndianness(ByteOrder.BIG_ENDIAN);

        Assertions.assertEquals(ByteOrder.BIG_ENDIAN, cspSerializationBuffer.getByteBuffer().order());

        cspSerializationBuffer.applyEndianness(ByteOrder.LITTLE_ENDIAN);
        Assertions.assertEquals(ByteOrder.LITTLE_ENDIAN, cspSerializationBuffer.getByteBuffer().order());
    }

    @Test
    public void isDirectBufferTest()
    {
        ICspSerializationBuffer cspSerializationBuffer = createCspSerializationBuffer(null,
                                                                                     true);

        Assertions.assertTrue(cspSerializationBuffer.isDirectBuffer());
        ICspSerializationBuffer cspSerializationBuffer2 = createCspSerializationBuffer(0, false);
        Assertions.assertFalse(cspSerializationBuffer2.isDirectBuffer());
    }

    @Test
    public void writeByteTest()
    {
        writePrimitive((byte) 100, (byte) 101, ICspSerializationBuffer::write, ByteBuffer::get);
    }

    @Test
    public void writeShortTest()
    {
        writePrimitive((short) 10000, (short) 10001, ICspSerializationBuffer::write, ByteBuffer::getShort);
    }

    @Test
    public void writeIntTest()
    {
        writePrimitive(10000000, 10000001, ICspSerializationBuffer::write, ByteBuffer::getInt);
    }

    @Test
    public void writeLongTest()
    {
        writePrimitive(10000000000L, 10000000001L, ICspSerializationBuffer::write, ByteBuffer::getLong);
    }

    @Test
    public void writeCharTest()
    {
        writePrimitive('a', 'b', ICspSerializationBuffer::write, ByteBuffer::getChar);
    }

    @Test
    public void writeFloatTest()
    {
        writePrimitive(10000000F, 10000001F, ICspSerializationBuffer::write, ByteBuffer::getFloat);
    }

    @Test
    public void writeDoubleTest()
    {
        writePrimitive(1000000000000000000D, 1000000000000000001D, ICspSerializationBuffer::write, ByteBuffer::getDouble);
    }

    @Test
    public void writeByteArrayTest()
    {
        ICspSerializationBuffer cspSerializationBuffer = createCspSerializationBuffer(null, null);

        byte[] value1 = { 1, 2, 3 };
        byte[] value2 = { 4, 5, 6, 7 };
        cspSerializationBuffer.write(value1);
        cspSerializationBuffer.write(value2);
        cspSerializationBuffer.commitBuffer();

        int sizeOfWrittenValue = value1.length + value2.length;
        Assertions.assertEquals(sizeOfWrittenValue, cspSerializationBuffer.getByteBuffer().limit());

        byte[] writtenValue1 = new byte[3];
        byte[] writtenValue2 = new byte[4];
        cspSerializationBuffer.getByteBuffer().get(writtenValue1);
        cspSerializationBuffer.getByteBuffer().get(writtenValue2);
        Assertions.assertArrayEquals(value1, writtenValue1);
        Assertions.assertArrayEquals(value2, writtenValue2);
    }

    @Test
    public void writeShortArrayTest()
    {
        ICspSerializationBuffer cspSerializationBuffer = createCspSerializationBuffer(null, null);

        short[] value1 = { 1, 2, 3 };
        short[] value2 = { 4, 5, 6, 7 };
        cspSerializationBuffer.write(value1);
        cspSerializationBuffer.write(value2);
        cspSerializationBuffer.commitBuffer();

        int sizeOfWrittenValue = CommonUtils.getPrimitiveSize(value1[0]) * (value1.length + value2.length);
        Assertions.assertEquals(sizeOfWrittenValue, cspSerializationBuffer.getByteBuffer().limit());

        short[] writtenValue1 = new short[3];
        short[] writtenValue2 = new short[4];
        ShortBuffer shortBuffer = cspSerializationBuffer.getByteBuffer().asShortBuffer();
        shortBuffer.get(writtenValue1);
        shortBuffer.get(writtenValue2);
        Assertions.assertArrayEquals(value1, writtenValue1);
        Assertions.assertArrayEquals(value2, writtenValue2);
    }

    @Test
    public void writeIntArrayTest()
    {
        ICspSerializationBuffer cspSerializationBuffer = createCspSerializationBuffer(null, null);

        int[] value1 = { 1, 2, 3 };
        int[] value2 = { 4, 5, 6, 7 };
        cspSerializationBuffer.write(value1);
        cspSerializationBuffer.write(value2);
        cspSerializationBuffer.commitBuffer();

        int sizeOfWrittenValue = CommonUtils.getPrimitiveSize(value1[0]) * (value1.length + value2.length);
        Assertions.assertEquals(sizeOfWrittenValue, cspSerializationBuffer.getByteBuffer().limit());

        int[] writtenValue1 = new int[3];
        int[] writtenValue2 = new int[4];
        IntBuffer intBuffer = cspSerializationBuffer.getByteBuffer().asIntBuffer();
        intBuffer.get(writtenValue1);
        intBuffer.get(writtenValue2);
        Assertions.assertArrayEquals(value1, writtenValue1);
        Assertions.assertArrayEquals(value2, writtenValue2);
    }


    @Test
    public void writeLongArrayTest()
    {
        ICspSerializationBuffer cspSerializationBuffer = createCspSerializationBuffer(null, null);

        long[] value1 = { 1, 2, 3 };
        long[] value2 = { 4, 5, 6, 7 };
        cspSerializationBuffer.write(value1);
        cspSerializationBuffer.write(value2);
        cspSerializationBuffer.commitBuffer();

        int sizeOfWrittenValue = CommonUtils.getPrimitiveSize(value1[0]) * (value1.length + value2.length);
        Assertions.assertEquals(sizeOfWrittenValue, cspSerializationBuffer.getByteBuffer().limit());

        long[] writtenValue1 = new long[3];
        long[] writtenValue2 = new long[4];
        LongBuffer longBuffer = cspSerializationBuffer.getByteBuffer().asLongBuffer();
        longBuffer.get(writtenValue1);
        longBuffer.get(writtenValue2);
        Assertions.assertArrayEquals(value1, writtenValue1);
        Assertions.assertArrayEquals(value2, writtenValue2);
    }

    @Test
    public void writeCharArrayTest()
    {
        ICspSerializationBuffer cspSerializationBuffer = createCspSerializationBuffer(null, null);

        char[] value1 = { 1, 2, 3 };
        char[] value2 = { 4, 5, 6, 7 };
        cspSerializationBuffer.write(value1);
        cspSerializationBuffer.write(value2);
        cspSerializationBuffer.commitBuffer();

        int sizeOfWrittenValue = CommonUtils.getPrimitiveSize(value1[0]) * (value1.length + value2.length);
        Assertions.assertEquals(sizeOfWrittenValue, cspSerializationBuffer.getByteBuffer().limit());

        char[] writtenValue1 = new char[3];
        char[] writtenValue2 = new char[4];
        CharBuffer charBuffer = cspSerializationBuffer.getByteBuffer().asCharBuffer();
        charBuffer.get(writtenValue1);
        charBuffer.get(writtenValue2);
        Assertions.assertArrayEquals(value1, writtenValue1);
        Assertions.assertArrayEquals(value2, writtenValue2);
    }

    @Test
    public void writeFloatArrayTest()
    {
        ICspSerializationBuffer cspSerializationBuffer = createCspSerializationBuffer(null, null);

        float[] value1 = { 1, 2, 3 };
        float[] value2 = { 4, 5, 6, 7 };
        cspSerializationBuffer.write(value1);
        cspSerializationBuffer.write(value2);
        cspSerializationBuffer.commitBuffer();

        int sizeOfWrittenValue = CommonUtils.getPrimitiveSize(value1[0]) * (value1.length + value2.length);
        Assertions.assertEquals(sizeOfWrittenValue, cspSerializationBuffer.getByteBuffer().limit());

        float[] writtenValue1 = new float[3];
        float[] writtenValue2 = new float[4];
        FloatBuffer floatBuffer = cspSerializationBuffer.getByteBuffer().asFloatBuffer();
        floatBuffer.get(writtenValue1);
        floatBuffer.get(writtenValue2);
        Assertions.assertArrayEquals(value1, writtenValue1);
        Assertions.assertArrayEquals(value2, writtenValue2);
    }

    @Test
    public void writeDoubleArrayTest()
    {
        ICspSerializationBuffer cspSerializationBuffer = createCspSerializationBuffer(null, null);

        double[] value1 = { 1, 2, 3 };
        double[] value2 = { 4, 5, 6, 7 };
        cspSerializationBuffer.write(value1);
        cspSerializationBuffer.write(value2);
        cspSerializationBuffer.commitBuffer();

        int sizeOfWrittenValue = CommonUtils.getPrimitiveSize(value1[0]) * (value1.length + value2.length);
        Assertions.assertEquals(sizeOfWrittenValue, cspSerializationBuffer.getByteBuffer().limit());

        double[] writtenValue1 = new double[3];
        double[] writtenValue2 = new double[4];
        DoubleBuffer doubleBuffer = cspSerializationBuffer.getByteBuffer().asDoubleBuffer();
        doubleBuffer.get(writtenValue1);
        doubleBuffer.get(writtenValue2);
        Assertions.assertArrayEquals(value1, writtenValue1);
        Assertions.assertArrayEquals(value2, writtenValue2);
    }

    @Test
    public void commitBufferTest()
    {
        int capacitySize = 16;

        ICspSerializationBuffer cspSerializationBuffer = createCspSerializationBuffer(capacitySize, true);
        int i = 5;
        cspSerializationBuffer.write(i);

        ByteBuffer byteBuffer = cspSerializationBuffer.getByteBuffer();
        Assertions.assertEquals(capacitySize, byteBuffer.limit());
        Assertions.assertEquals(CommonUtils.getPrimitiveSize(i), byteBuffer.position());

        cspSerializationBuffer.commitBuffer();
        Assertions.assertEquals(CommonUtils.getPrimitiveSize(i), byteBuffer.limit());
        Assertions.assertEquals(0, byteBuffer.position());
    }

    protected abstract ICspSerializationBuffer createCspSerializationBuffer(Integer initialBufferCapacity,
                                                                            Boolean directBuffer);

    private <T> void writePrimitive(T value1, T value2, BiConsumer<ICspSerializationBuffer, T> writeFunction,
                                    Function<ByteBuffer, T> readValueFunction)
    {
        ICspSerializationBuffer cspSerializationBuffer = createCspSerializationBuffer(null, null);

        writeFunction.accept(cspSerializationBuffer, value1);
        writeFunction.accept(cspSerializationBuffer, value2);
        cspSerializationBuffer.commitBuffer();

        ByteBuffer byteBuffer = cspSerializationBuffer.getByteBuffer();
        Assertions.assertEquals(CommonUtils.getPrimitiveSize(value1) * 2, byteBuffer.limit());

        Assertions.assertEquals(value1, readValueFunction.apply(byteBuffer));
        Assertions.assertEquals(value2, readValueFunction.apply(byteBuffer));
    }
}
