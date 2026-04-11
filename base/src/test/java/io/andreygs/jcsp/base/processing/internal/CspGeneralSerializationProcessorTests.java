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

package io.andreygs.jcsp.base.processing.internal;

import io.andreygs.jcsp.base.message.ICspDataMessage;
import io.andreygs.jcsp.base.processing.ICspDataProcessorRegistry;
import io.andreygs.jcsp.base.processing.ICspDataSerializationProcessor;
import io.andreygs.jcsp.base.processing.buffer.internal.ICspSerializationBuffer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit-tests for {@link CspDataGeneralSerializationProcessor}.
 */
@ExtendWith(MockitoExtension.class)
public class CspGeneralSerializationProcessorTests
{
    @Mock
    private ICspSerializationBuffer buffer;
    @Mock
    private ICspDataProcessorRegistry<ICspDataSerializationProcessor> cspProcessorRegistry;
    @Mock
    private ICspDataProcessorGenerator<ICspDataSerializationProcessor> processorGenerator;
    @Mock
    private ICspDataMessage cspDataMessage;

    private CspDataGeneralSerializationProcessor cspDataGeneralSerializationProcessor;

    @BeforeEach
    public void setup()
    {
        cspDataGeneralSerializationProcessor = new CspDataGeneralSerializationProcessor(buffer, cspProcessorRegistry,
            processorGenerator, cspDataMessage);
    }

    @Test
    public void serializeBooleanTrueTest()
    {
        cspDataGeneralSerializationProcessor.serialize(true);
        Mockito.verify(buffer).writeByte((byte) 1);
    }

    @Test
    public void serializeBooleanFalseTest()
    {
        cspDataGeneralSerializationProcessor.serialize(false);
        Mockito.verify(buffer).writeByte((byte) 0);
    }

    @Test
    public void serializeByteSizeOfIntegersAreEqualTest()
    {
        byte value = 11;
        byte sizeOfInteger = 1;

        cspDataGeneralSerializationProcessor.serializeByte(value);
        Mockito.verify(buffer, Mockito.never()).writeByte(sizeOfInteger);
        Mockito.verify(buffer).writeByte(value);
    }

    @Test
    public void serializeByteSizeOfIntegersAreNotEqualTest()
    {
        byte value = 11;
        byte sizeOfInteger = 1;

        Mockito.when(cspDataMessage.isSizeOfIntegersMayBeNotEqual()).thenReturn(true);

        cspDataGeneralSerializationProcessor.serializeByte(value);
        Mockito.verify(buffer).writeByte(sizeOfInteger);
        Mockito.verify(buffer).writeByte(value);
    }

    @Test
    public void serializeShortSizeOfIntegersAreEqualTest()
    {
        short value = 1111;
        byte sizeOfInteger = 2;

        cspDataGeneralSerializationProcessor.serializeShort(value);
        Mockito.verify(buffer, Mockito.never()).writeByte(sizeOfInteger);
        Mockito.verify(buffer).writeShort(value);
    }

    @Test
    public void serializeShortSizeOfIntegersAreNotEqualTest()
    {
        short value = 1111;
        byte sizeOfInteger = 2;

        Mockito.when(cspDataMessage.isSizeOfIntegersMayBeNotEqual()).thenReturn(true);

        cspDataGeneralSerializationProcessor.serializeShort(value);
        Mockito.verify(buffer).writeByte(sizeOfInteger);
        Mockito.verify(buffer).writeShort(value);
    }

    @Test
    public void serializeIntSizeOfIntegersAreEqualTest()
    {
        int value = 111111;
        byte sizeOfInteger = 4;

        cspDataGeneralSerializationProcessor.serializeInt(value);
        Mockito.verify(buffer, Mockito.never()).writeByte(sizeOfInteger);
        Mockito.verify(buffer).writeInt(value);
    }

    @Test
    public void serializeIntSizeOfIntegersAreNotEqualTest()
    {
        int value = 111111;
        byte sizeOfInteger = 4;

        Mockito.when(cspDataMessage.isSizeOfIntegersMayBeNotEqual()).thenReturn(true);

        cspDataGeneralSerializationProcessor.serializeInt(value);
        Mockito.verify(buffer).writeByte(sizeOfInteger);
        Mockito.verify(buffer).writeInt(value);
    }

    @Test
    public void serializeLongSizeOfIntegersAreEqualTest()
    {
        long value = 1111111111L;
        byte sizeOfInteger = 8;

        cspDataGeneralSerializationProcessor.serializeLong(value);
        Mockito.verify(buffer, Mockito.never()).writeByte(sizeOfInteger);
        Mockito.verify(buffer).writeLong(value);
    }

    @Test
    public void serializeLongSizeOfIntegersAreNotEqualTest()
    {
        long value = 1111111111L;
        byte sizeOfInteger = 8;

        Mockito.when(cspDataMessage.isSizeOfIntegersMayBeNotEqual()).thenReturn(true);

        cspDataGeneralSerializationProcessor.serializeLong(value);
        Mockito.verify(buffer).writeByte(sizeOfInteger);
        Mockito.verify(buffer).writeLong(value);
    }

    @Test
    public void serializeCharTest()
    {
        char value = 'a';

        cspDataGeneralSerializationProcessor.serializeChar(value);
        Mockito.verify(buffer).writeChar(value);
    }

    @Test
    public void serializeFloatTest()
    {
        float value = 1.11F;

        cspDataGeneralSerializationProcessor.serializeFloat(value);
        Mockito.verify(buffer).writeFloat(value);
    }

    @Test
    public void serializeDoubleTest()
    {
        double value = 1.11;

        cspDataGeneralSerializationProcessor.serializeDouble(value);
        Mockito.verify(buffer).writeDouble(value);
    }

    @Test
    public void serializeBooleanArrayTest()
    {
        boolean[] value = new boolean[] { true, false };

        cspDataGeneralSerializationProcessor.serialize(value);
        Mockito.verify(buffer).writeByte((byte) 1);
        Mockito.verify(buffer).writeByte((byte) 0);
    }

    @Test
    @SuppressWarnings("DataFlowIssue")
    public void serializeBooleanArrayNullTest()
    {
        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> cspDataGeneralSerializationProcessor.serialize((boolean[])null));
    }

    @Test
    @SuppressWarnings("DataFlowIssue")
    public void serializeBooleanArrayContextNullTest()
    {
        boolean[] value = new boolean[] { true, false };

        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> cspDataGeneralSerializationProcessor.serialize(value));
    }

    @Test
    public void serializeBooleanArrayAsReferenceTest()
    {
        boolean[] value = new boolean[] { true, false };

        Mockito.when(cspDataMessage.isAllowUnmanagedPointers()).thenReturn(true);

        cspDataGeneralSerializationProcessor.serialize(value, true, true);
        Mockito.verify(buffer).writeByte((byte) 1);
        Mockito.verify(buffer).writeByte((byte) 1);
        Mockito.verify(buffer).writeByte((byte) 0);
    }

    @Test
    public void serializeBooleanArrayNullAsReferenceTest()
    {
        Mockito.when(cspDataMessage.isAllowUnmanagedPointers()).thenReturn(true);

        cspDataGeneralSerializationProcessor.serialize((boolean[]) null, true, false);
        Mockito.verify(buffer, Mockito.never()).writeInt(1);
        Mockito.verify(buffer).writeByte((byte) 0);
    }

    @Test
    public void serializeBooleanArrayNotFixedTest()
    {
        boolean[] value = new boolean[] { true, false };

        Mockito.when(cspDataMessage.isAllowUnmanagedPointers()).thenReturn(true);

        cspDataGeneralSerializationProcessor.serialize(value, false, false);
        Mockito.verify(buffer).writeByte((byte) 2);
        Mockito.verify(buffer, Mockito.calls(1)).writeByte((byte) 1);
        Mockito.verify(buffer).writeByte((byte) 0);
    }

    @Test
    public void serializeBooleanArrayAsReferenceNotFixedTest()
    {
        Mockito.when(cspDataMessage.isAllowUnmanagedPointers()).thenReturn(true);

        cspDataGeneralSerializationProcessor.serialize((boolean[]) null, true, false);
        Mockito.verify(buffer, Mockito.calls(2)).writeByte((byte) 1);
        Mockito.verify(buffer).writeByte((byte) 2);
        Mockito.verify(buffer).writeByte((byte) 0);
    }
}
