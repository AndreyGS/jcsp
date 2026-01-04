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

import io.andreygs.jcsp.base.message.buffer.internal.CspSerializationBuffer;
import io.andreygs.jcsp.base.test.CommonUtils;

import io.andreygs.jcsp.base.utils.IBufferResizeStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
 * Tests for {@link CspSerializationBuffer} class.
 */
public class CspSerializationBufferTests
{
    @Test
    public void testConstructor()
    {
        int capacitySize = 171;
        boolean directBuffer = false;

        CspSerializationBuffer cspSerializationByteBuffer = createCspSerializationBuffer(capacitySize, directBuffer);
        Assertions.assertEquals(capacitySize,
                                cspSerializationByteBuffer.getByteBuffer().capacity());
        Assertions.assertFalse(cspSerializationByteBuffer.isDirectBuffer());
    }

    @Test
    public void testConstructorWithDefaultParams()
    {
        CspSerializationBuffer cspSerializationBuffer = createDefaultCspSerializationBuffer();
        Assertions.assertEquals(CspSerializationBuffer.DEFAULT_CAPACITY_SIZE,
                                cspSerializationBuffer.getByteBuffer().capacity());
        Assertions.assertTrue(cspSerializationBuffer.isDirectBuffer());
    }

    @Test
    public void testGetByteBuffer()
    {
        CspSerializationBuffer cspSerializationBuffer = createDefaultCspSerializationBuffer();
        Assertions.assertNotNull(cspSerializationBuffer.getByteBuffer());
    }

    @Test
    public void testApplyEndianness()
    {
        CspSerializationBuffer cspSerializationBuffer = createDefaultCspSerializationBuffer();
        cspSerializationBuffer.applyEndianness(ByteOrder.BIG_ENDIAN);

        Assertions.assertEquals(ByteOrder.BIG_ENDIAN, cspSerializationBuffer.getByteBuffer().order());

        cspSerializationBuffer.applyEndianness(ByteOrder.LITTLE_ENDIAN);
        Assertions.assertEquals(ByteOrder.LITTLE_ENDIAN, cspSerializationBuffer.getByteBuffer().order());
    }

    @Test
    public void testIsDirectBuffer()
    {
        CspSerializationBuffer cspSerializationBuffer = createCspSerializationBuffer(null,
                                                                                     true);

        Assertions.assertTrue(cspSerializationBuffer.isDirectBuffer());
        CspSerializationBuffer cspSerializationBuffer2 = createCspSerializationBuffer(0, false);
        Assertions.assertFalse(cspSerializationBuffer2.isDirectBuffer());
    }

    @Test
    public void writeByte()
    {
        writePrimitive((byte) 100, (byte) 101, CspSerializationBuffer::write, ByteBuffer::get);
    }

    @Test
    public void writeShort()
    {
        writePrimitive((short) 10000, (short) 10001, CspSerializationBuffer::write, ByteBuffer::getShort);
    }

    @Test
    public void writeInt()
    {
        writePrimitive(10000000, 10000001, CspSerializationBuffer::write, ByteBuffer::getInt);
    }

    @Test
    public void writeLong()
    {
        writePrimitive(10000000000L, 10000000001L, CspSerializationBuffer::write, ByteBuffer::getLong);
    }

    @Test
    public void writeChar()
    {
        writePrimitive('a', 'b', CspSerializationBuffer::write, ByteBuffer::getChar);
    }

    @Test
    public void writeFloat()
    {
        writePrimitive(10000000F, 10000001F, CspSerializationBuffer::write, ByteBuffer::getFloat);
    }

    @Test
    public void writeDouble()
    {
        writePrimitive(1000000000000000000D, 1000000000000000001D, CspSerializationBuffer::write, ByteBuffer::getDouble);
    }

    @Test
    public void writeByteArray()
    {
        CspSerializationBuffer cspSerializationBuffer = createDefaultCspSerializationBuffer();

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
    public void writeShortArray()
    {
        CspSerializationBuffer cspSerializationBuffer = createDefaultCspSerializationBuffer();

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
    public void writeIntArray()
    {
        CspSerializationBuffer cspSerializationBuffer = createDefaultCspSerializationBuffer();

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
    public void writeLongArray()
    {
        CspSerializationBuffer cspSerializationBuffer = createDefaultCspSerializationBuffer();

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
    public void writeCharArray()
    {
        CspSerializationBuffer cspSerializationBuffer = createDefaultCspSerializationBuffer();

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
    public void writeFloatArray()
    {
        CspSerializationBuffer cspSerializationBuffer = createDefaultCspSerializationBuffer();

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
    public void writeDoubleArray()
    {
        CspSerializationBuffer cspSerializationBuffer = createDefaultCspSerializationBuffer();

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
    public void testCommitBuffer()
    {
        int capacitySize = 16;

        CspSerializationBuffer cspSerializationBuffer = createCspSerializationBuffer(capacitySize, true);
        int i = 5;
        cspSerializationBuffer.write(i);

        ByteBuffer byteBuffer = cspSerializationBuffer.getByteBuffer();
        Assertions.assertEquals(capacitySize, byteBuffer.limit());
        Assertions.assertEquals(CommonUtils.getPrimitiveSize(i), byteBuffer.position());

        cspSerializationBuffer.commitBuffer();
        Assertions.assertEquals(CommonUtils.getPrimitiveSize(i), byteBuffer.limit());
        Assertions.assertEquals(0, byteBuffer.position());
    }

    private <T> void writePrimitive(T value1, T value2, BiConsumer<CspSerializationBuffer, T> writeFunction,
                                    Function<ByteBuffer, T> readValueFunction)
    {
        CspSerializationBuffer cspSerializationBuffer = createDefaultCspSerializationBuffer();

        writeFunction.accept(cspSerializationBuffer, value1);
        writeFunction.accept(cspSerializationBuffer, value2);
        cspSerializationBuffer.commitBuffer();

        ByteBuffer byteBuffer = cspSerializationBuffer.getByteBuffer();
        Assertions.assertEquals(CommonUtils.getPrimitiveSize(value1) * 2, byteBuffer.limit());

        Assertions.assertEquals(value1, readValueFunction.apply(byteBuffer));
        Assertions.assertEquals(value2, readValueFunction.apply(byteBuffer));
    }

    private CspSerializationBuffer createCspSerializationBuffer(Integer initialBufferCapacity, Boolean directBuffer)
    {
        try
        {
            Constructor<CspSerializationBuffer> constructor =
                CspSerializationBuffer.class.getDeclaredConstructor(Integer.class, Boolean.class, IBufferResizeStrategy.class);
            constructor.setAccessible(true);
            return constructor.newInstance(initialBufferCapacity, directBuffer, null);
        }
        catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e)
        {
            throw new RuntimeException(e instanceof InvocationTargetException invocationTargetException ?
                                       invocationTargetException.getCause() : e);
        }
    }

    private CspSerializationBuffer createDefaultCspSerializationBuffer()
    {
        return createCspSerializationBuffer(null, null);
    }
}
