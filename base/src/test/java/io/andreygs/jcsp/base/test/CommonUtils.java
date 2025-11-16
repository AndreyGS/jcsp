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

package io.andreygs.jcsp.base.test;

import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

/**
 * TODO: place description here
 */
public class CommonUtils
{
    public static <T> int getPrimitiveSize(T value)
    {
        Class<?> clazz = value.getClass();
        Field field;
        int sizeOfValue;
        try
        {
            field = clazz.getDeclaredField("BYTES");
            return (int) field.get(null);
        }
        catch (NoSuchFieldException | IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static <T> byte[] getByteArrayFromOtherPrimitiveArray(T[] otherPrimitiveArray)
    {
        if (otherPrimitiveArray instanceof Byte[] byteArray)
        {
            return ArrayUtils.toPrimitive(byteArray);
        }

        ByteBuffer byteBuffer = ByteBuffer.allocate(otherPrimitiveArray.length * getPrimitiveSize(otherPrimitiveArray[0]));
        for (T value : otherPrimitiveArray)
        {
            if (value instanceof Short shortValue)
            {
                byteBuffer.putShort(shortValue);
            }
            else  if (value instanceof Integer intValue)
            {
                byteBuffer.putInt(intValue);
            }
            else  if (value instanceof Long longValue)
            {
                byteBuffer.putLong(longValue);
            }
            else  if (value instanceof Character charValue)
            {
                byteBuffer.putChar(charValue);
            }
            else  if (value instanceof Float floatValue)
            {
                byteBuffer.putFloat(floatValue);
            }
            else  if (value instanceof Double doubleValue)
            {
                byteBuffer.putDouble(doubleValue);
            }
            else
            {
                throw new RuntimeException("Invalid T type");
            }
        }

        return byteBuffer.array();
    }
}
