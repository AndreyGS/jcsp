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

package io.andreygs.jcsp.internal.model.buffer;

import io.andreygs.jcsp.CommonUtils;
import io.andreygs.jcsp.internal.factory.model.processing.buffer.CspSerializationBufferFactory;
import io.andreygs.jcsp.api.model.buffer.IBufferResizeStrategy;
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

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit-tests for {@link CspSerializationBuffer}.
 */
public class CspSerializationBufferTests
{
    @Test
    public void testGetByteBuffer()
    {
        ICspBuffer cspBuffer = new CspSerializationBuffer(0, false,
                                                          CspSerializationBufferFactory.DEFAULT_BUFFER_RESIZE_STRATEGY);
        assertThat(cspBuffer).isNotNull();
    }

    @Test
    public void testApplyEndianness()
    {
        ICspBuffer cspBuffer = new CspSerializationBuffer(0, false,
                                                          CspSerializationBufferFactory.DEFAULT_BUFFER_RESIZE_STRATEGY);
        cspBuffer.applyEndianness(ByteOrder.BIG_ENDIAN);

        assertThat(cspBuffer.getByteBuffer().order()).isEqualTo(ByteOrder.BIG_ENDIAN);

        cspBuffer.applyEndianness(ByteOrder.LITTLE_ENDIAN);

        assertThat(cspBuffer.getByteBuffer().order()).isEqualTo(ByteOrder.LITTLE_ENDIAN);
    }

    @Test
    public void testIsDirectBuffer()
    {
        ICspSerializationBuffer cspSerializationBuffer = createCspSerializationBuffer(0,
                                                                                      true);

        assertThat(cspSerializationBuffer.isDirectBuffer()).isTrue();

        ICspSerializationBuffer cspSerializationBuffer2 = createCspSerializationBuffer(0, false);

        assertThat(cspSerializationBuffer2.isDirectBuffer()).isFalse();
    }

    @Test
    public void testCreateCspSerializationBuffer()
    {
        int capacitySize = 0;
        boolean directBuffer = false;
        int expandRatio = 17;
        IBufferResizeStrategy customBufferResizeStrategy =
            (currentCapacity, minimumRequiredSize) -> (minimumRequiredSize) * expandRatio;

        ICspSerializationBuffer cspSerializationByteBuffer =
            new CspSerializationBuffer(capacitySize, directBuffer, customBufferResizeStrategy);

        assertThat(cspSerializationByteBuffer.getByteBuffer().capacity()).isEqualTo(capacitySize);
        assertThat(cspSerializationByteBuffer.isDirectBuffer()).isFalse();

        cspSerializationByteBuffer.writeByte((byte)5);
        assertThat(cspSerializationByteBuffer.getByteBuffer().capacity()).isEqualTo(expandRatio);
    }

    @Test
    public void testWriteByte()
    {
        writePrimitive((byte) 100, (byte) 101, ICspSerializationBuffer::writeByte, ByteBuffer::get);
    }

    @Test
    public void testWriteShort()
    {
        writePrimitive((short) 10000, (short) 10001, ICspSerializationBuffer::writeShort, ByteBuffer::getShort);
    }

    @Test
    public void testWriteInt()
    {
        writePrimitive(10000000, 10000001, ICspSerializationBuffer::writeInt, ByteBuffer::getInt);
    }

    @Test
    public void testWriteLong()
    {
        writePrimitive(10000000000L, 10000000001L, ICspSerializationBuffer::writeLong, ByteBuffer::getLong);
    }

    @Test
    public void testWriteChar()
    {
        writePrimitive('a', 'b', ICspSerializationBuffer::writeChar, ByteBuffer::getChar);
    }

    @Test
    public void testWriteFloat()
    {
        writePrimitive(10000000F, 10000001F, ICspSerializationBuffer::writeFloat, ByteBuffer::getFloat);
    }

    @Test
    public void testWriteDouble()
    {
        writePrimitive(1000000000000000000D, 1000000000000000001D, ICspSerializationBuffer::writeDouble, ByteBuffer::getDouble);
    }

    @Test
    public void testWriteByteArray()
    {
        ICspSerializationBuffer cspSerializationBuffer = createCspSerializationBuffer(0, true);

        byte[] value1 = { 1, 2, 3 };
        byte[] value2 = { 4, 5, 6, 7 };
        cspSerializationBuffer.write(value1);
        cspSerializationBuffer.write(value2);
        cspSerializationBuffer.commitBuffer();

        int sizeOfWrittenValue = value1.length + value2.length;
        assertThat(cspSerializationBuffer.getByteBuffer().limit()).isEqualTo(sizeOfWrittenValue);

        byte[] writtenValue1 = new byte[3];
        byte[] writtenValue2 = new byte[4];
        cspSerializationBuffer.getByteBuffer().get(writtenValue1);
        cspSerializationBuffer.getByteBuffer().get(writtenValue2);
        assertThat(writtenValue1).isEqualTo(value1);
        assertThat(writtenValue2).isEqualTo(value2);
    }

    @Test
    public void testWriteShortArray()
    {
        ICspSerializationBuffer cspSerializationBuffer = createCspSerializationBuffer(0, true);

        short[] value1 = { 1, 2, 3 };
        short[] value2 = { 4, 5, 6, 7 };
        cspSerializationBuffer.write(value1);
        cspSerializationBuffer.write(value2);
        cspSerializationBuffer.commitBuffer();

        int sizeOfWrittenValue = CommonUtils.getPrimitiveSize(value1[0]) * (value1.length + value2.length);
        assertThat(cspSerializationBuffer.getByteBuffer().limit()).isEqualTo(sizeOfWrittenValue);

        short[] writtenValue1 = new short[3];
        short[] writtenValue2 = new short[4];
        ShortBuffer shortBuffer = cspSerializationBuffer.getByteBuffer().asShortBuffer();
        shortBuffer.get(writtenValue1);
        shortBuffer.get(writtenValue2);
        assertThat(writtenValue1).isEqualTo(value1);
        assertThat(writtenValue2).isEqualTo(value2);
    }

    @Test
    public void testWriteIntArray()
    {
        ICspSerializationBuffer cspSerializationBuffer = createCspSerializationBuffer(0, true);

        int[] value1 = { 1, 2, 3 };
        int[] value2 = { 4, 5, 6, 7 };
        cspSerializationBuffer.write(value1);
        cspSerializationBuffer.write(value2);
        cspSerializationBuffer.commitBuffer();

        int sizeOfWrittenValue = CommonUtils.getPrimitiveSize(value1[0]) * (value1.length + value2.length);
        assertThat(cspSerializationBuffer.getByteBuffer().limit()).isEqualTo(sizeOfWrittenValue);

        int[] writtenValue1 = new int[3];
        int[] writtenValue2 = new int[4];
        IntBuffer intBuffer = cspSerializationBuffer.getByteBuffer().asIntBuffer();
        intBuffer.get(writtenValue1);
        intBuffer.get(writtenValue2);
        assertThat(writtenValue1).isEqualTo(value1);
        assertThat(writtenValue2).isEqualTo(value2);
    }


    @Test
    public void testWriteLongArray()
    {
        ICspSerializationBuffer cspSerializationBuffer = createCspSerializationBuffer(0, true);

        long[] value1 = { 1, 2, 3 };
        long[] value2 = { 4, 5, 6, 7 };
        cspSerializationBuffer.write(value1);
        cspSerializationBuffer.write(value2);
        cspSerializationBuffer.commitBuffer();

        int sizeOfWrittenValue = CommonUtils.getPrimitiveSize(value1[0]) * (value1.length + value2.length);
        assertThat(cspSerializationBuffer.getByteBuffer().limit()).isEqualTo(sizeOfWrittenValue);

        long[] writtenValue1 = new long[3];
        long[] writtenValue2 = new long[4];
        LongBuffer longBuffer = cspSerializationBuffer.getByteBuffer().asLongBuffer();
        longBuffer.get(writtenValue1);
        longBuffer.get(writtenValue2);
        assertThat(writtenValue1).isEqualTo(value1);
        assertThat(writtenValue2).isEqualTo(value2);
    }

    @Test
    public void testWriteCharArray()
    {
        ICspSerializationBuffer cspSerializationBuffer = createCspSerializationBuffer(0, true);

        char[] value1 = { 1, 2, 3 };
        char[] value2 = { 4, 5, 6, 7 };
        cspSerializationBuffer.write(value1);
        cspSerializationBuffer.write(value2);
        cspSerializationBuffer.commitBuffer();

        int sizeOfWrittenValue = CommonUtils.getPrimitiveSize(value1[0]) * (value1.length + value2.length);
        assertThat(cspSerializationBuffer.getByteBuffer().limit()).isEqualTo(sizeOfWrittenValue);

        char[] writtenValue1 = new char[3];
        char[] writtenValue2 = new char[4];
        CharBuffer charBuffer = cspSerializationBuffer.getByteBuffer().asCharBuffer();
        charBuffer.get(writtenValue1);
        charBuffer.get(writtenValue2);
        assertThat(writtenValue1).isEqualTo(value1);
        assertThat(writtenValue2).isEqualTo(value2);
    }

    @Test
    public void testWriteFloatArray()
    {
        ICspSerializationBuffer cspSerializationBuffer = createCspSerializationBuffer(0, true);

        float[] value1 = { 1, 2, 3 };
        float[] value2 = { 4, 5, 6, 7 };
        cspSerializationBuffer.write(value1);
        cspSerializationBuffer.write(value2);
        cspSerializationBuffer.commitBuffer();

        int sizeOfWrittenValue = CommonUtils.getPrimitiveSize(value1[0]) * (value1.length + value2.length);
        assertThat(cspSerializationBuffer.getByteBuffer().limit()).isEqualTo(sizeOfWrittenValue);

        float[] writtenValue1 = new float[3];
        float[] writtenValue2 = new float[4];
        FloatBuffer floatBuffer = cspSerializationBuffer.getByteBuffer().asFloatBuffer();
        floatBuffer.get(writtenValue1);
        floatBuffer.get(writtenValue2);
        assertThat(writtenValue1).isEqualTo(value1);
        assertThat(writtenValue2).isEqualTo(value2);
    }

    @Test
    public void testWriteDoubleArray()
    {
        ICspSerializationBuffer cspSerializationBuffer = createCspSerializationBuffer(0, true);

        double[] value1 = { 1, 2, 3 };
        double[] value2 = { 4, 5, 6, 7 };
        cspSerializationBuffer.write(value1);
        cspSerializationBuffer.write(value2);
        cspSerializationBuffer.commitBuffer();

        int sizeOfWrittenValue = CommonUtils.getPrimitiveSize(value1[0]) * (value1.length + value2.length);
        assertThat(cspSerializationBuffer.getByteBuffer().limit()).isEqualTo(sizeOfWrittenValue);

        double[] writtenValue1 = new double[3];
        double[] writtenValue2 = new double[4];
        DoubleBuffer doubleBuffer = cspSerializationBuffer.getByteBuffer().asDoubleBuffer();
        doubleBuffer.get(writtenValue1);
        doubleBuffer.get(writtenValue2);
        assertThat(writtenValue1).isEqualTo(value1);
        assertThat(writtenValue2).isEqualTo(value2);
    }

    @Test
    public void testCommitBuffer()
    {
        int capacitySize = 16;

        ICspSerializationBuffer cspSerializationBuffer = createCspSerializationBuffer(capacitySize, true);
        int i = 5;
        cspSerializationBuffer.writeInt(i);

        ByteBuffer byteBuffer = cspSerializationBuffer.getByteBuffer();
        assertThat(byteBuffer.position()).isEqualTo(CommonUtils.getPrimitiveSize(i));

        cspSerializationBuffer.commitBuffer();
        assertThat(byteBuffer.limit()).isEqualTo(CommonUtils.getPrimitiveSize(i));
        assertThat(byteBuffer.position()).isEqualTo(0);
    }

    private ICspSerializationBuffer createCspSerializationBuffer(int initialBufferCapacity, boolean directBuffer)
    {
        return new CspSerializationBuffer(initialBufferCapacity, directBuffer,
                                          CspSerializationBufferFactory.DEFAULT_BUFFER_RESIZE_STRATEGY);
    }

    private <T> void writePrimitive(T value1, T value2, BiConsumer<ICspSerializationBuffer, T> writeFunction,
                                    Function<ByteBuffer, T> readValueFunction)
    {
        ICspSerializationBuffer cspSerializationBuffer = createCspSerializationBuffer(0, true);

        writeFunction.accept(cspSerializationBuffer, value1);
        writeFunction.accept(cspSerializationBuffer, value2);
        cspSerializationBuffer.commitBuffer();

        ByteBuffer byteBuffer = cspSerializationBuffer.getByteBuffer();
        assertThat(byteBuffer.limit()).isEqualTo(CommonUtils.getPrimitiveSize(value1) * 2);

        assertThat(readValueFunction.apply(byteBuffer)).isEqualTo(value1);
        assertThat(readValueFunction.apply(byteBuffer)).isEqualTo(value2);
    }
}
