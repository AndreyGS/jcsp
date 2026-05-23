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

package io.andreygs.jcsp.internal.processing.data;

import io.andreygs.jcsp.api.processing.data.clazz.ICspClassSerializationProcessor;
import io.andreygs.jcsp.internal.model.buffer.ISerializationBuffer;
import io.andreygs.jcsp.internal.processing.data.type.ICspTypeSerializationProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.calls;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit-tests for {@link CspSerializationProcessor}.
 */
@ExtendWith(MockitoExtension.class)
public class CspGeneralSerializationProcessorTests
{
    @Mock
    private ISerializationBuffer buffer;
    @Mock
    private ICspProcessorRegistry<ICspClassSerializationProcessor<?>, ICspTypeSerializationProcessor>
        cspProcessorRegistry;
    @Mock
    private ICspClassProcessorGenerator<ICspClassSerializationProcessor<?>> processorGenerator;
    @Mock
    private ICspDataMessage cspDataMessage;

    private CspSerializationProcessor cspDataGeneralSerializationProcessor;

    @BeforeEach
    public void setup()
    {
        cspDataGeneralSerializationProcessor = new CspSerializationProcessor(buffer, cspProcessorRegistry,
            processorGenerator, cspDataMessage);
    }

    @Test
    public void testSerializeBooleanTrue()
    {
        cspDataGeneralSerializationProcessor.serialize(true);
        verify(buffer).writeByte((byte) 1);
    }

    @Test
    public void testSerializeBooleanFalse()
    {
        cspDataGeneralSerializationProcessor.serialize(false);
        verify(buffer).writeByte((byte) 0);
    }

    @Test
    public void testSerializeByteSizeOfIntegersAreEqual()
    {
        byte value = 11;
        byte sizeOfInteger = 1;

        cspDataGeneralSerializationProcessor.serializeByte(value);
        verify(buffer, never()).writeByte(sizeOfInteger);
        verify(buffer).writeByte(value);
    }

    @Test
    public void testSerializeByteSizeOfIntegersAreNotEqual()
    {
        byte value = 11;
        byte sizeOfInteger = 1;

        when(cspDataMessage.isSizeOfIntegersMayBeNotEqual()).thenReturn(true);

        cspDataGeneralSerializationProcessor.serializeByte(value);
        verify(buffer).writeByte(sizeOfInteger);
        verify(buffer).writeByte(value);
    }

    @Test
    public void testSerializeShortSizeOfIntegersAreEqual()
    {
        short value = 1111;
        byte sizeOfInteger = 2;

        cspDataGeneralSerializationProcessor.serializeShort(value);
        verify(buffer, never()).writeByte(sizeOfInteger);
        verify(buffer).writeShort(value);
    }

    @Test
    public void testSerializeShortSizeOfIntegersAreNotEqual()
    {
        short value = 1111;
        byte sizeOfInteger = 2;

        when(cspDataMessage.isSizeOfIntegersMayBeNotEqual()).thenReturn(true);

        cspDataGeneralSerializationProcessor.serializeShort(value);
        verify(buffer).writeByte(sizeOfInteger);
        verify(buffer).writeShort(value);
    }

    @Test
    public void testSerializeIntSizeOfIntegersAreEqual()
    {
        int value = 111111;
        byte sizeOfInteger = 4;

        cspDataGeneralSerializationProcessor.serializeInt(value);
        verify(buffer, never()).writeByte(sizeOfInteger);
        verify(buffer).writeInt(value);
    }

    @Test
    public void testSerializeIntSizeOfIntegersAreNotEqual()
    {
        int value = 111111;
        byte sizeOfInteger = 4;

        when(cspDataMessage.isSizeOfIntegersMayBeNotEqual()).thenReturn(true);

        cspDataGeneralSerializationProcessor.serializeInt(value);
        verify(buffer).writeByte(sizeOfInteger);
        verify(buffer).writeInt(value);
    }

    @Test
    public void testSerializeLongSizeOfIntegersAreEqual()
    {
        long value = 1111111111L;
        byte sizeOfInteger = 8;

        cspDataGeneralSerializationProcessor.serializeLong(value);
        verify(buffer, never()).writeByte(sizeOfInteger);
        verify(buffer).writeLong(value);
    }

    @Test
    public void testSerializeLongSizeOfIntegersAreNotEqual()
    {
        long value = 1111111111L;
        byte sizeOfInteger = 8;

        when(cspDataMessage.isSizeOfIntegersMayBeNotEqual()).thenReturn(true);

        cspDataGeneralSerializationProcessor.serializeLong(value);
        verify(buffer).writeByte(sizeOfInteger);
        verify(buffer).writeLong(value);
    }

    @Test
    public void testSerializeChar()
    {
        char value = 'a';

        cspDataGeneralSerializationProcessor.serializeChar(value);
        verify(buffer).writeChar(value);
    }

    @Test
    public void testSerializeFloat()
    {
        float value = 1.11F;

        cspDataGeneralSerializationProcessor.serializeFloat(value);
        verify(buffer).writeFloat(value);
    }

    @Test
    public void testSerializeDouble()
    {
        double value = 1.11;

        cspDataGeneralSerializationProcessor.serializeDouble(value);
        verify(buffer).writeDouble(value);
    }

    @Test
    public void testSerializeBooleanArray()
    {
        boolean[] value = new boolean[] { true, false };

        cspDataGeneralSerializationProcessor.serialize(value);
        verify(buffer).writeByte((byte) 1);
        verify(buffer).writeByte((byte) 0);
    }

    @Test
    @SuppressWarnings("DataFlowIssue" /* "Intentional contract nullability violation for test" */)
    public void testSerializeBooleanArrayNull()
    {
        assertThatThrownBy(() -> cspDataGeneralSerializationProcessor.serialize((boolean[]) null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    public void testSerializeBooleanArrayContextNull()
    {
        boolean[] value = new boolean[] { true, false };

        assertThatThrownBy(() -> cspDataGeneralSerializationProcessor.serialize(value))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    public void testSerializeBooleanArrayReference()
    {
        boolean[] value = new boolean[] { true, false };

        when(cspDataMessage.isAllowUnmanagedPointers()).thenReturn(true);

        cspDataGeneralSerializationProcessor.serialize(value, true, true);
        verify(buffer).writeByte((byte) 1);
        verify(buffer).writeByte((byte) 1);
        verify(buffer).writeByte((byte) 0);
    }

    @Test
    public void testSerializeBooleanArrayNullReference()
    {
        when(cspDataMessage.isAllowUnmanagedPointers()).thenReturn(true);

        cspDataGeneralSerializationProcessor.serialize((boolean[]) null, true, false);
        verify(buffer, never()).writeInt(1);
        verify(buffer).writeByte((byte) 0);
    }

    @Test
    public void testSerializeBooleanArrayNotFixed()
    {
        boolean[] value = new boolean[] { true, false };

        when(cspDataMessage.isAllowUnmanagedPointers()).thenReturn(true);

        cspDataGeneralSerializationProcessor.serialize(value, false, false);
        verify(buffer).writeByte((byte) 2);
        verify(buffer, calls(1)).writeByte((byte) 1);
        verify(buffer).writeByte((byte) 0);
    }

    @Test
    public void testSerializeBooleanArrayReferenceNotFixed()
    {
        when(cspDataMessage.isAllowUnmanagedPointers()).thenReturn(true);

        cspDataGeneralSerializationProcessor.serialize((boolean[]) null, true, false);
        verify(buffer, calls(2)).writeByte((byte) 1);
        verify(buffer).writeByte((byte) 2);
        verify(buffer).writeByte((byte) 0);
    }
}
