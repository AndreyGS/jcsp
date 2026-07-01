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

import io.andreygs.jcsp.CommonUtils;
import io.andreygs.jcsp.api.processing.buffer.AllocationType;
import io.andreygs.jcsp.api.processing.buffer.IBufferResizeStrategy;
import io.andreygs.jcsp.api.processing.buffer.ISerializationBufferConfig;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit-tests for {@link SerializationBuffer}.
 */
public class SerializationBufferTest
{
    private final IBufferResizeStrategy FAKE_BUFFER_RESIZE_STRATEGY = (currentCapacity, minimumRequiredSize) -> minimumRequiredSize;

    @Test
    public void testGetBuffer()
    {
        ISerializationBufferConfig bufferConfig = createCspSerializationBufferConfig(0, AllocationType.DIRECT);
        IBuffer buffer = new SerializationBuffer(bufferConfig);
        assertThat(buffer).isNotNull();
    }

    @Test
    public void testApplyEndianness()
    {
        ISerializationBufferConfig bufferConfig = createCspSerializationBufferConfig(0, AllocationType.DIRECT);
        IBuffer buffer = new SerializationBuffer(bufferConfig);
        
        buffer.applyEndianness(ByteOrder.BIG_ENDIAN);

        assertThat(buffer.getBuffer().order()).isEqualTo(ByteOrder.BIG_ENDIAN);

        buffer.applyEndianness(ByteOrder.LITTLE_ENDIAN);

        assertThat(buffer.getBuffer().order()).isEqualTo(ByteOrder.LITTLE_ENDIAN);
    }

    @Test
    public void testIsDirectBuffer()
    {
        ISerializationBufferConfig bufferConfig = createCspSerializationBufferConfig(0, AllocationType.DIRECT);
        ISerializationBuffer serializationBuffer = new SerializationBuffer(bufferConfig);

        assertThat(serializationBuffer.getAllocationType()).isEqualTo(AllocationType.DIRECT);

        ISerializationBufferConfig bufferConfig2 = createCspSerializationBufferConfig(0, AllocationType.HEAP);
        ISerializationBuffer serializationBuffer2 = new SerializationBuffer(bufferConfig2);

        assertThat(serializationBuffer2.getAllocationType()).isEqualTo(AllocationType.HEAP);
    }

    @Test
    public void testCreateCspSerializationBuffer()
    {
        int capacitySize = 0;
        boolean directBuffer = false;
        int expandRatio = 17;
        IBufferResizeStrategy customBufferResizeStrategy =
            (currentCapacity, minimumRequiredSize) -> (minimumRequiredSize) * expandRatio;

        ISerializationBufferConfig bufferConfig = mock(ISerializationBufferConfig.class);
        when(bufferConfig.getInitialCapacity()).thenReturn(capacitySize);
        when(bufferConfig.getAllocationType()).thenReturn(AllocationType.HEAP);
        when(bufferConfig.getResizeStrategy()).thenReturn(customBufferResizeStrategy);

        ISerializationBuffer cspSerializationByteBuffer =
            new SerializationBuffer(bufferConfig);

        assertThat(cspSerializationByteBuffer.getBuffer().capacity()).isEqualTo(capacitySize);
        assertThat(cspSerializationByteBuffer.getAllocationType()).isEqualTo(AllocationType.HEAP);

        cspSerializationByteBuffer.writeByte((byte)5);
        assertThat(cspSerializationByteBuffer.getBuffer().capacity()).isEqualTo(expandRatio);
    }

    @Test
    public void testWriteByte()
    {
        writePrimitive((byte) 100, (byte) 101, ISerializationBuffer::writeByte, ByteBuffer::get);
    }

    @Test
    public void testWriteShort()
    {
        writePrimitive((short) 10000, (short) 10001, ISerializationBuffer::writeShort, ByteBuffer::getShort);
    }

    @Test
    public void testWriteInt()
    {
        writePrimitive(10000000, 10000001, ISerializationBuffer::writeInt, ByteBuffer::getInt);
    }

    @Test
    public void testWriteLong()
    {
        writePrimitive(10000000000L, 10000000001L, ISerializationBuffer::writeLong, ByteBuffer::getLong);
    }

    @Test
    public void testWriteChar()
    {
        writePrimitive('a', 'b', ISerializationBuffer::writeChar, ByteBuffer::getChar);
    }

    @Test
    public void testWriteFloat()
    {
        writePrimitive(10000000F, 10000001F, ISerializationBuffer::writeFloat, ByteBuffer::getFloat);
    }

    @Test
    public void testWriteDouble()
    {
        writePrimitive(1000000000000000000D, 1000000000000000001D, ISerializationBuffer::writeDouble, ByteBuffer::getDouble);
    }

    @Test
    public void testWriteByteArray()
    {
        ISerializationBufferConfig bufferConfig = createCspSerializationBufferConfig(0, AllocationType.DIRECT);
        ISerializationBuffer serializationBuffer = new SerializationBuffer(bufferConfig);

        byte[] value1 = { 1, 2, 3 };
        byte[] value2 = { 4, 5, 6, 7 };
        serializationBuffer.write(value1);
        serializationBuffer.write(value2);
        serializationBuffer.commitBuffer();

        int sizeOfWrittenValue = value1.length + value2.length;
        assertThat(serializationBuffer.getBuffer().limit()).isEqualTo(sizeOfWrittenValue);

        byte[] writtenValue1 = new byte[3];
        byte[] writtenValue2 = new byte[4];
        serializationBuffer.getBuffer().get(writtenValue1);
        serializationBuffer.getBuffer().get(writtenValue2);
        assertThat(writtenValue1).isEqualTo(value1);
        assertThat(writtenValue2).isEqualTo(value2);
    }

    @Test
    public void testWriteShortArray()
    {
        ISerializationBufferConfig bufferConfig = createCspSerializationBufferConfig(0, AllocationType.DIRECT);
        ISerializationBuffer serializationBuffer = new SerializationBuffer(bufferConfig);

        short[] value1 = { 1, 2, 3 };
        short[] value2 = { 4, 5, 6, 7 };
        serializationBuffer.write(value1);
        serializationBuffer.write(value2);
        serializationBuffer.commitBuffer();

        int sizeOfWrittenValue = CommonUtils.getPrimitiveSize(value1[0]) * (value1.length + value2.length);
        assertThat(serializationBuffer.getBuffer().limit()).isEqualTo(sizeOfWrittenValue);

        short[] writtenValue1 = new short[3];
        short[] writtenValue2 = new short[4];
        ShortBuffer shortBuffer = serializationBuffer.getBuffer().asShortBuffer();
        shortBuffer.get(writtenValue1);
        shortBuffer.get(writtenValue2);
        assertThat(writtenValue1).isEqualTo(value1);
        assertThat(writtenValue2).isEqualTo(value2);
    }

    @Test
    public void testWriteIntArray()
    {
        ISerializationBufferConfig bufferConfig = createCspSerializationBufferConfig(0, AllocationType.DIRECT);
        ISerializationBuffer serializationBuffer = new SerializationBuffer(bufferConfig);

        int[] value1 = { 1, 2, 3 };
        int[] value2 = { 4, 5, 6, 7 };
        serializationBuffer.write(value1);
        serializationBuffer.write(value2);
        serializationBuffer.commitBuffer();

        int sizeOfWrittenValue = CommonUtils.getPrimitiveSize(value1[0]) * (value1.length + value2.length);
        assertThat(serializationBuffer.getBuffer().limit()).isEqualTo(sizeOfWrittenValue);

        int[] writtenValue1 = new int[3];
        int[] writtenValue2 = new int[4];
        IntBuffer intBuffer = serializationBuffer.getBuffer().asIntBuffer();
        intBuffer.get(writtenValue1);
        intBuffer.get(writtenValue2);
        assertThat(writtenValue1).isEqualTo(value1);
        assertThat(writtenValue2).isEqualTo(value2);
    }


    @Test
    public void testWriteLongArray()
    {
        ISerializationBufferConfig bufferConfig = createCspSerializationBufferConfig(0, AllocationType.DIRECT);
        ISerializationBuffer serializationBuffer = new SerializationBuffer(bufferConfig);

        long[] value1 = { 1, 2, 3 };
        long[] value2 = { 4, 5, 6, 7 };
        serializationBuffer.write(value1);
        serializationBuffer.write(value2);
        serializationBuffer.commitBuffer();

        int sizeOfWrittenValue = CommonUtils.getPrimitiveSize(value1[0]) * (value1.length + value2.length);
        assertThat(serializationBuffer.getBuffer().limit()).isEqualTo(sizeOfWrittenValue);

        long[] writtenValue1 = new long[3];
        long[] writtenValue2 = new long[4];
        LongBuffer longBuffer = serializationBuffer.getBuffer().asLongBuffer();
        longBuffer.get(writtenValue1);
        longBuffer.get(writtenValue2);
        assertThat(writtenValue1).isEqualTo(value1);
        assertThat(writtenValue2).isEqualTo(value2);
    }

    @Test
    public void testWriteCharArray()
    {
        ISerializationBufferConfig bufferConfig = createCspSerializationBufferConfig(0, AllocationType.DIRECT);
        ISerializationBuffer serializationBuffer = new SerializationBuffer(bufferConfig);

        char[] value1 = { 1, 2, 3 };
        char[] value2 = { 4, 5, 6, 7 };
        serializationBuffer.write(value1);
        serializationBuffer.write(value2);
        serializationBuffer.commitBuffer();

        int sizeOfWrittenValue = CommonUtils.getPrimitiveSize(value1[0]) * (value1.length + value2.length);
        assertThat(serializationBuffer.getBuffer().limit()).isEqualTo(sizeOfWrittenValue);

        char[] writtenValue1 = new char[3];
        char[] writtenValue2 = new char[4];
        CharBuffer charBuffer = serializationBuffer.getBuffer().asCharBuffer();
        charBuffer.get(writtenValue1);
        charBuffer.get(writtenValue2);
        assertThat(writtenValue1).isEqualTo(value1);
        assertThat(writtenValue2).isEqualTo(value2);
    }

    @Test
    public void testWriteFloatArray()
    {
        ISerializationBufferConfig bufferConfig = createCspSerializationBufferConfig(0, AllocationType.DIRECT);
        ISerializationBuffer serializationBuffer = new SerializationBuffer(bufferConfig);

        float[] value1 = { 1, 2, 3 };
        float[] value2 = { 4, 5, 6, 7 };
        serializationBuffer.write(value1);
        serializationBuffer.write(value2);
        serializationBuffer.commitBuffer();

        int sizeOfWrittenValue = CommonUtils.getPrimitiveSize(value1[0]) * (value1.length + value2.length);
        assertThat(serializationBuffer.getBuffer().limit()).isEqualTo(sizeOfWrittenValue);

        float[] writtenValue1 = new float[3];
        float[] writtenValue2 = new float[4];
        FloatBuffer floatBuffer = serializationBuffer.getBuffer().asFloatBuffer();
        floatBuffer.get(writtenValue1);
        floatBuffer.get(writtenValue2);
        assertThat(writtenValue1).isEqualTo(value1);
        assertThat(writtenValue2).isEqualTo(value2);
    }

    @Test
    public void testWriteDoubleArray()
    {
        ISerializationBufferConfig bufferConfig = createCspSerializationBufferConfig(0, AllocationType.DIRECT);
        ISerializationBuffer serializationBuffer = new SerializationBuffer(bufferConfig);

        double[] value1 = { 1, 2, 3 };
        double[] value2 = { 4, 5, 6, 7 };
        serializationBuffer.write(value1);
        serializationBuffer.write(value2);
        serializationBuffer.commitBuffer();

        int sizeOfWrittenValue = CommonUtils.getPrimitiveSize(value1[0]) * (value1.length + value2.length);
        assertThat(serializationBuffer.getBuffer().limit()).isEqualTo(sizeOfWrittenValue);

        double[] writtenValue1 = new double[3];
        double[] writtenValue2 = new double[4];
        DoubleBuffer doubleBuffer = serializationBuffer.getBuffer().asDoubleBuffer();
        doubleBuffer.get(writtenValue1);
        doubleBuffer.get(writtenValue2);
        assertThat(writtenValue1).isEqualTo(value1);
        assertThat(writtenValue2).isEqualTo(value2);
    }

    @Test
    public void testCommitBuffer()
    {
        int capacitySize = 16;

        ISerializationBufferConfig bufferConfig = createCspSerializationBufferConfig(capacitySize, AllocationType.DIRECT);
        ISerializationBuffer serializationBuffer = new SerializationBuffer(bufferConfig);
        int i = 5;
        serializationBuffer.writeInt(i);

        ByteBuffer byteBuffer = serializationBuffer.getBuffer();
        assertThat(byteBuffer.position()).isEqualTo(CommonUtils.getPrimitiveSize(i));

        serializationBuffer.commitBuffer();
        assertThat(byteBuffer.limit()).isEqualTo(CommonUtils.getPrimitiveSize(i));
        assertThat(byteBuffer.position()).isEqualTo(0);
    }

    private ISerializationBufferConfig createCspSerializationBufferConfig(int initialCapacity, AllocationType allocationType)
    {
        ISerializationBufferConfig bufferConfig = mock(ISerializationBufferConfig.class);
        when(bufferConfig.getInitialCapacity()).thenReturn(initialCapacity);
        when(bufferConfig.getAllocationType()).thenReturn(allocationType);
        when(bufferConfig.getResizeStrategy()).thenReturn(FAKE_BUFFER_RESIZE_STRATEGY);
        return bufferConfig;
    }

    private <T> void writePrimitive(T value1, T value2, BiConsumer<ISerializationBuffer, T> writeFunction,
                                    Function<ByteBuffer, T> readValueFunction)
    {
        ISerializationBufferConfig bufferConfig = createCspSerializationBufferConfig(0, AllocationType.DIRECT);
        ISerializationBuffer serializationBuffer = new SerializationBuffer(bufferConfig);

        writeFunction.accept(serializationBuffer, value1);
        writeFunction.accept(serializationBuffer, value2);
        serializationBuffer.commitBuffer();

        ByteBuffer byteBuffer = serializationBuffer.getBuffer();
        assertThat(byteBuffer.limit()).isEqualTo(CommonUtils.getPrimitiveSize(value1) * 2);

        assertThat(readValueFunction.apply(byteBuffer)).isEqualTo(value1);
        assertThat(readValueFunction.apply(byteBuffer)).isEqualTo(value2);
    }
}
