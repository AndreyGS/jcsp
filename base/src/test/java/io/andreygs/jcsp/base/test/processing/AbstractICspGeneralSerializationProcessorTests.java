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

package io.andreygs.jcsp.base.test.processing;

import io.andreygs.jcsp.base.processing.ICspGeneralSerializationProcessor;
import io.andreygs.jcsp.base.processing.buffer.internal.ICspSerializationBuffer;
import io.andreygs.jcsp.base.processing.context.ICspDataMessageSerializationContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit-tests for {@link ICspGeneralSerializationProcessor} contract.
 */
@ExtendWith(MockitoExtension.class)
public abstract class AbstractICspGeneralSerializationProcessorTests
{
    @Mock
    private ICspDataMessageSerializationContext context;
    @Mock
    private ICspSerializationBuffer buffer;

    private ICspGeneralSerializationProcessor cspGeneralSerializationProcessor;

    @BeforeEach
    public void setup()
    {
        cspGeneralSerializationProcessor = getCspGeneralSerializationProcessor();
        Mockito.when(context.getCspSerializationBuffer()).thenReturn(buffer);
    }

    @Test
    public void serializeBooleanTrueTest()
    {
        cspGeneralSerializationProcessor.serialize(true, context);
        Mockito.verify(buffer).write((byte) 1);
    }

    @Test
    public void serializeBooleanFalseTest()
    {
        cspGeneralSerializationProcessor.serialize(false, context);
        Mockito.verify(buffer).write((byte) 0);
    }

    @Test
    public void serializeByteSizeOfIntegersAreEqualTest()
    {
        byte value = 11;
        byte sizeOfInteger = 1;

        cspGeneralSerializationProcessor.serialize(value, context);
        Mockito.verify(buffer, Mockito.never()).write(sizeOfInteger);
        Mockito.verify(buffer).write(value);
    }

    @Test
    public void serializeByteSizeOfIntegersAreNotEqualTest()
    {
        byte value = 11;
        byte sizeOfInteger = 1;

        Mockito.when(context.isSizeOfIntegersMayBeNotEqual()).thenReturn(true);

        cspGeneralSerializationProcessor.serialize(value, context);
        Mockito.verify(buffer).write(sizeOfInteger);
        Mockito.verify(buffer).write(value);
    }

    @Test
    public void serializeShortSizeOfIntegersAreEqualTest()
    {
        short value = 1111;
        byte sizeOfInteger = 2;

        cspGeneralSerializationProcessor.serialize(value, context);
        Mockito.verify(buffer, Mockito.never()).write(sizeOfInteger);
        Mockito.verify(buffer).write(value);
    }

    @Test
    public void serializeShortSizeOfIntegersAreNotEqualTest()
    {
        short value = 1111;
        byte sizeOfInteger = 2;

        Mockito.when(context.isSizeOfIntegersMayBeNotEqual()).thenReturn(true);

        cspGeneralSerializationProcessor.serialize(value, context);
        Mockito.verify(buffer).write(sizeOfInteger);
        Mockito.verify(buffer).write(value);
    }

    @Test
    public void serializeIntSizeOfIntegersAreEqualTest()
    {
        int value = 111111;
        byte sizeOfInteger = 4;

        cspGeneralSerializationProcessor.serialize(value, context);
        Mockito.verify(buffer, Mockito.never()).write(sizeOfInteger);
        Mockito.verify(buffer).write(value);
    }

    @Test
    public void serializeIntSizeOfIntegersAreNotEqualTest()
    {
        int value = 111111;
        byte sizeOfInteger = 4;

        Mockito.when(context.isSizeOfIntegersMayBeNotEqual()).thenReturn(true);

        cspGeneralSerializationProcessor.serialize(value, context);
        Mockito.verify(buffer).write(sizeOfInteger);
        Mockito.verify(buffer).write(value);
    }

    @Test
    public void serializeLongSizeOfIntegersAreEqualTest()
    {
        long value = 1111111111L;
        byte sizeOfInteger = 8;

        cspGeneralSerializationProcessor.serialize(value, context);
        Mockito.verify(buffer, Mockito.never()).write(sizeOfInteger);
        Mockito.verify(buffer).write(value);
    }

    @Test
    public void serializeLongSizeOfIntegersAreNotEqualTest()
    {
        long value = 1111111111L;
        byte sizeOfInteger = 8;

        Mockito.when(context.isSizeOfIntegersMayBeNotEqual()).thenReturn(true);

        cspGeneralSerializationProcessor.serialize(value, context);
        Mockito.verify(buffer).write(sizeOfInteger);
        Mockito.verify(buffer).write(value);
    }

    @Test
    public void serializeCharTest()
    {
        char value = 'a';

        cspGeneralSerializationProcessor.serialize(value, context);
        Mockito.verify(buffer).write(value);
    }

    @Test
    public void serializeFloatTest()
    {
        float value = 1.11F;

        cspGeneralSerializationProcessor.serialize(value, context);
        Mockito.verify(buffer).write(value);
    }

    @Test
    public void serializeDoubleTest()
    {
        double value = 1.11;

        cspGeneralSerializationProcessor.serialize(value, context);
        Mockito.verify(buffer).write(value);
    }

    @Test
    public void serializeBooleanArrayTest()
    {
        boolean[] value = new boolean[] { true, false };

        cspGeneralSerializationProcessor.serialize(value, context);
        Mockito.verify(buffer).write((byte) 1);
        Mockito.verify(buffer).write((byte) 0);
    }

    @Test
    public void serializeBooleanArrayNullTest()
    {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                                cspGeneralSerializationProcessor.serialize((boolean[])null, context));
    }

    @Test
    public void serializeBooleanArrayContextNullTest()
    {
        boolean[] value = new boolean[] { true, false };

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                                                                    cspGeneralSerializationProcessor.serialize(value, null));
    }

    @Test
    public void serializeBooleanArrayAsReferenceTest()
    {
        boolean[] value = new boolean[] { true, false };

        Mockito.when(context.isAllowUnmanagedPointers()).thenReturn(true);

        cspGeneralSerializationProcessor.serialize(value, true, false, context);
        Mockito.verify(buffer).write((byte) 1);
        Mockito.verify(buffer).write((byte) 1);
        Mockito.verify(buffer).write((byte) 0);
    }

    @Test
    public void serializeBooleanArrayNullAsReferenceTest()
    {
        Mockito.when(context.isAllowUnmanagedPointers()).thenReturn(true);

        cspGeneralSerializationProcessor.serialize((boolean[]) null, true, false, context);
        Mockito.verify(buffer, Mockito.never()).write(1);
        Mockito.verify(buffer).write((byte) 0);
    }

    protected abstract ICspGeneralSerializationProcessor getCspGeneralSerializationProcessor();
}
