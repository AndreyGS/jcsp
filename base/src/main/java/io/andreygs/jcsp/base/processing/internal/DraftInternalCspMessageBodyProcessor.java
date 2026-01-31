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

import io.andreygs.jcsp.base.processing.ICspSerializationProcessor;
import io.andreygs.jcsp.base.processing.context.ICspDataMessageSerializationContext;
import io.andreygs.jcsp.base.types.CspRuntimeException;
import io.andreygs.jcsp.base.types.CspStatus;
import io.andreygs.jcsp.base.types.ICspSerializable;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Optional;

/**
 * TODO: place description here
 */
public class DraftInternalCspMessageBodyProcessor
{
    private DraftInternalCspMessageBodyProcessor()
    {

    }

    public static void serialize(boolean value, ICspDataMessageSerializationContext context)
    {
        context.getCspSerializationBuffer().write((byte)(value ? 1 : 0));
    }

    public static void serialize(byte value, ICspDataMessageSerializationContext context)
    {
        context.getCspSerializationBuffer().write(value);
    }

    public static void serialize(short value, ICspDataMessageSerializationContext context)
    {
        context.getCspSerializationBuffer().write(value);
    }

    public static void serialize(int value, ICspDataMessageSerializationContext context)
    {
        context.getCspSerializationBuffer().write(value);
    }

    public static void serialize(long value, ICspDataMessageSerializationContext context)
    {
        context.getCspSerializationBuffer().write(value);
    }

    public static void serialize(char value, ICspDataMessageSerializationContext context)
    {
        context.getCspSerializationBuffer().write(value);
    }

    public static void serialize(float value, ICspDataMessageSerializationContext context)
    {
        context.getCspSerializationBuffer().write(value);
    }

    public static void serialize(double value, ICspDataMessageSerializationContext context)
    {
        context.getCspSerializationBuffer().write(value);
    }

    public static void serialize(boolean[] value, ICspDataMessageSerializationContext context)
    {
        for (boolean item : value)
        {
            context.getCspSerializationBuffer().write((byte)(item ? 1 : 0));
        }
    }

    public static void serialize(byte[] value, ICspDataMessageSerializationContext context)
    {
        context.getCspSerializationBuffer().write(value);
    }

    public static void serialize(short[] value, ICspDataMessageSerializationContext context)
    {
        context.getCspSerializationBuffer().write(value);
    }

    public static void serialize(int[] value, ICspDataMessageSerializationContext context)
    {
        context.getCspSerializationBuffer().write(value);
    }

    public static void serialize(long[] value, ICspDataMessageSerializationContext context)
    {
        context.getCspSerializationBuffer().write(value);
    }

    public static void serialize(char[] value, ICspDataMessageSerializationContext context)
    {
        context.getCspSerializationBuffer().write(value);
    }

    public static void serialize(float[] value, ICspDataMessageSerializationContext context)
    {
        context.getCspSerializationBuffer().write(value);
    }

    public static void serialize(double[] value, ICspDataMessageSerializationContext context)
    {
        context.getCspSerializationBuffer().write(value);
    }

    public static void serialize(String value, ICspDataMessageSerializationContext context)
    {
        context.getCspSerializationBuffer().write((long)value.length());
        context.getCspSerializationBuffer().write(value.getBytes(StandardCharsets.UTF_16BE));
    }

    public static void serialize(String value, Charset charset, ICspDataMessageSerializationContext context)
    {
        context.getCspSerializationBuffer().write((long)value.length());
        context.getCspSerializationBuffer().write(value.getBytes(charset));
    }

    public static void serialize(ICspSerializable value, Class<?> clazz, ICspDataMessageSerializationContext context)
    {
        Class<?> parentClazz = clazz.getSuperclass();
        if (ICspSerializable.class.isAssignableFrom(parentClazz))
        {
            serialize(value, parentClazz, context);
        }

        Arrays.stream(value.getCspFieldNames()).forEach(fieldName -> {
            try
            {
                Field field = clazz.getDeclaredField(fieldName);
                Class<?> fieldClazz = field.getDeclaringClass();
                serializationMethodExecutor(value, context, fieldClazz, field);
            }
            catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e)
            {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     *
     * @param value
     * @param context
     *
     * @throws io.andreygs.jcsp.base.types.CspRuntimeException when there is no such method.
     */
    public static void serialize(Object value, ICspDataMessageSerializationContext context)
    {
        Optional<ICspSerializationProcessor> cspSerializationProcessor =
            context.getCspSerializationProcessorRegistrar().findProcessor(value.getClass());
        //cspSerializationProcessor.serialize(value, context);
  /*
        if (cspSerializationProcessor.isEmpty())
        {
            throw CspRuntimeException.createCspRuntimeException(CspStatus.NO_SUCH_HANDLER,
                                                                MessageFormat.format(
                                                                    Messages.CspStatus_No_Such_Handler_ext_No_specialized_processor_for__0__,
                                                                    clazz));
        }*/
    }

    private static void serializationMethodExecutor(ICspSerializable value, ICspDataMessageSerializationContext context,
                                  Class<?> fieldClazz, Field field) throws IllegalAccessException
    {
        if (ICspSerializable.class.isAssignableFrom(fieldClazz))
        {
            serialize((ICspSerializable) field.get(value), fieldClazz, context);
        }
        else if (fieldClazz == boolean.class)
        {
            serialize(field.getBoolean(value), context);
        }
        else if (fieldClazz == byte.class)
        {
            serialize(field.getByte(value), context);
        }
        else if (fieldClazz == short.class)
        {
            serialize(field.getShort(value), context);
        }
        else if (fieldClazz == int.class)
        {
            serialize(field.getInt(value), context);
        }
        else if (fieldClazz == long.class)
        {
            serialize(field.getLong(value), context);
        }
        else if (fieldClazz == char.class)
        {
            serialize(field.getChar(value), context);
        }
        else if (fieldClazz == float.class)
        {
            serialize(field.getFloat(value), context);
        }
        else if (fieldClazz == double.class)
        {
            serialize(field.getDouble(value), context);
        }
        else if (fieldClazz == boolean[].class)
        {
            serialize((boolean[]) field.get(value), context);
        }
        else if (fieldClazz == byte[].class)
        {
            serialize((byte[]) field.get(value), context);
        }
        else if (fieldClazz == short[].class)
        {
            serialize((short[]) field.get(value), context);
        }
        else if (fieldClazz == int[].class)
        {
            serialize((int[]) field.get(value), context);
        }
        else if (fieldClazz == long[].class)
        {
            serialize((long[]) field.get(value), context);
        }
        else if (fieldClazz == char[].class)
        {
            serialize((char[]) field.get(value), context);
        }
        else if (fieldClazz == float[].class)
        {
            serialize((float[]) field.get(value), context);
        }
        else if (fieldClazz == double[].class)
        {
            serialize((double[]) field.get(value), context);
        }
        else if (fieldClazz.isArray())
        {
            int elementsCount = Array.getLength(value);
            Class<?> elementsClazz = fieldClazz.getComponentType();

            if (ICspSerializable.class.isAssignableFrom(fieldClazz.getComponentType()))
            {
                for (int i = 0; i < elementsCount; ++i)
                {
                    Object element = Array.get(value, i);
                    serialize((ICspSerializable) element, elementsClazz, context);
                }
            }
            else
            {
                for (int i = 0; i < elementsCount; ++i)
                {
                    Object element = Array.get(value, i);
                    serialize(element, context);
                }
            }
        }
        else
        {
            serialize(field.get(value), context);
        }
    }
}
