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

package io.andreygs.jcsp.base.processing;

import io.andreygs.jcsp.base.processing.context.ICspDataMessageSerializationContext;
import io.andreygs.jcsp.base.processing.internal.InternalCspMessageBodyProcessor;
import io.andreygs.jcsp.base.types.ICspSerializable;

import java.nio.charset.Charset;

/**
 * TODO: place description here
 */
public class CspMessageBodyProcessor
{
    private CspMessageBodyProcessor()
    {

    }

    public static void serialize(boolean value, ICspDataMessageSerializationContext context)
    {
        InternalCspMessageBodyProcessor.serialize(value, context);
    }

    public static void serialize(byte value, ICspDataMessageSerializationContext context)
    {
        InternalCspMessageBodyProcessor.serialize(value, context);
    }

    public static void serialize(short value, ICspDataMessageSerializationContext context)
    {
        InternalCspMessageBodyProcessor.serialize(value, context);
    }

    public static void serialize(int value, ICspDataMessageSerializationContext context)
    {
        InternalCspMessageBodyProcessor.serialize(value, context);
    }

    public static void serialize(long value, ICspDataMessageSerializationContext context)
    {
        InternalCspMessageBodyProcessor.serialize(value, context);
    }

    public static void serialize(char value, ICspDataMessageSerializationContext context)
    {
        InternalCspMessageBodyProcessor.serialize(value, context);
    }

    public static void serialize(float value, ICspDataMessageSerializationContext context)
    {
        InternalCspMessageBodyProcessor.serialize(value, context);
    }

    public static void serialize(double value, ICspDataMessageSerializationContext context)
    {
        InternalCspMessageBodyProcessor.serialize(value, context);
    }

    public static void serialize(byte[] value, ICspDataMessageSerializationContext context)
    {
        InternalCspMessageBodyProcessor.serialize(value, context);
    }

    public static void serialize(short[] value, ICspDataMessageSerializationContext context)
    {
        InternalCspMessageBodyProcessor.serialize(value, context);
    }

    public static void serialize(int[] value, ICspDataMessageSerializationContext context)
    {
        InternalCspMessageBodyProcessor.serialize(value, context);
    }

    public static void serialize(long[] value, ICspDataMessageSerializationContext context)
    {
        InternalCspMessageBodyProcessor.serialize(value, context);
    }

    public static void serialize(char[] value, ICspDataMessageSerializationContext context)
    {
        InternalCspMessageBodyProcessor.serialize(value, context);
    }

    public static void serialize(float[] value, ICspDataMessageSerializationContext context)
    {
        InternalCspMessageBodyProcessor.serialize(value, context);
    }

    public static void serialize(double[] value, ICspDataMessageSerializationContext context)
    {
        InternalCspMessageBodyProcessor.serialize(value, context);
    }

    public static void serialize(String value, ICspDataMessageSerializationContext context)
    {
        InternalCspMessageBodyProcessor.serialize(value, context);
    }

    public static void serialize(String value, Charset charset, ICspDataMessageSerializationContext context)
    {
        InternalCspMessageBodyProcessor.serialize(value, charset, context);
    }

    public static void serialize(ICspSerializable value, Class<?> clazz, ICspDataMessageSerializationContext context)
    {
        InternalCspMessageBodyProcessor.serialize(value, clazz, context);
    }

    public static void serialize(Object value, ICspDataMessageSerializationContext context)
    {
        InternalCspMessageBodyProcessor.serialize(value, context);
    }
}
