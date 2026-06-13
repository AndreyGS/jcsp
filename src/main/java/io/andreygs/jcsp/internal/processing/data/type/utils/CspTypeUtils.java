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

package io.andreygs.jcsp.internal.processing.data.type.utils;

import io.andreygs.jcsp.api.exception.CspRuntimeException;
import io.andreygs.jcsp.api.protocol.CspStatus;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.Charset;
import java.text.MessageFormat;

/**
 * TODO: place description here
 */
public class CspTypeUtils
{
    public static Charset requireStringCharset(@Nullable String charsetName)
    {
        if (charsetName == null)
        {
            throw CspRuntimeException.createCspRuntimeException(CspStatus.ERROR_IN_STRUCT_FORMAT,
                MessageFormat.format(Messages.CspStatus_Error_in_struct_format_Property__0__for_struct__1__not_set,
                    "charset", "CSP String"));
        }
        return Charset.forName(charsetName);
    }

    public static <T> Class<?> requireClassProcessorForCollectionOrMap(@Nullable Class<?> overrideClazz,
        Class<?> declaredClazz, Class<T> baseClazz, boolean overrideWithUpperBound)
    {
        if (overrideClazz != null)
        {
            validateProcessorOverrideClass(overrideClazz, declaredClazz, overrideWithUpperBound);
            return overrideClazz;
        }
        return baseClazz;
    }

    public static Class<?> requireClassProcessor(@Nullable Class<?> overrideClazz, Class<?> declaredClazz,
        boolean overrideWithUpperBound)
    {
        if (overrideClazz != null)
        {
            validateProcessorOverrideClass(overrideClazz, declaredClazz, overrideWithUpperBound);
        }
        return overrideClazz != null ? overrideClazz : declaredClazz;
    }

    public static void validateFixedArraySize(@Nullable Integer fixedArraySize)
    {
        if (fixedArraySize != null && fixedArraySize < 1)
        {
            throw CspRuntimeException.createCspRuntimeException(CspStatus.ERROR_IN_STRUCT_FORMAT,
                Messages.CspStatus_Error_in_struct_format_Array_size_number_is_invalid);
        }
    }

    public static void validateCollectionArgumentsSize(int argumentSize)
    {
        if (argumentSize != 1)
        {
            throw CspRuntimeException.createCspRuntimeException(CspStatus.ERROR_IN_STRUCT_FORMAT,
                Messages.CspStatus_Error_in_struct_format_Collection_has_invalid_type_arguments_number);
        }
    }

    public static void validateMapArgumentsSize(int argumentSize)
    {
        if (argumentSize != 2)
        {
            throw CspRuntimeException.createCspRuntimeException(CspStatus.ERROR_IN_STRUCT_FORMAT,
                Messages.CspStatus_Error_in_struct_format_Map_has_invalid_type_arguments_number);
        }
    }

    public static void validateWildCardBoundsLength(int lowerBoundLength, int upperBoundLength)
    {
        if (lowerBoundLength == 0 && upperBoundLength == 0)
        {
            throw CspRuntimeException.createCspRuntimeException(CspStatus.ERROR_IN_STRUCT_FORMAT,
                Messages.CspStatus_Error_in_struct_format_Unbound_wildcard_type_cannot_be_processed);
        }
    }

    public static void validateImplementationOverrideClass(Class<?> implementationClazz, Class<?> declaredClazz)
    {
        if (!declaredClazz.isAssignableFrom(implementationClazz))
        {
            throw CspRuntimeException.createCspRuntimeException(CspStatus.ERROR_IN_STRUCT_FORMAT,
                MessageFormat.format(
                    Messages.CspStatus_Error_in_struct_format_Class__0__cannot_be_implementation_of_class__1,
                    implementationClazz, declaredClazz));
        }
    }

    public static void validateProcessorOverrideClass(Class<?> overrideClazz, Class<?> declaredClazz,
        boolean overrideWithUpperBound)
    {
        boolean canOverride = overrideWithUpperBound
                              ? declaredClazz.isAssignableFrom(overrideClazz)
                              : overrideClazz.isAssignableFrom(declaredClazz);
        if (!canOverride || declaredClazz.getTypeParameters().length != overrideClazz.getTypeParameters().length)
        {
            throw CspRuntimeException.createCspRuntimeException(CspStatus.ERROR_IN_STRUCT_FORMAT,
                MessageFormat.format(Messages.CspStatus_Error_in_struct_format_Class__0__cannot_override_class__1,
                    overrideWithUpperBound ? overrideClazz : declaredClazz,
                    overrideWithUpperBound ? declaredClazz : overrideClazz));
        }
    }
}
