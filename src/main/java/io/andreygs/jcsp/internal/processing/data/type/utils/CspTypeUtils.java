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
import io.andreygs.jcsp.internal.processing.data.type.TypeBoundKind;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Map;

/**
 * Utility class for evaluating CSP type traits and their validation.
 */
public class CspTypeUtils
{
    /**
     * Requires that supplied string charset is not null.
     *
     * @param charset Charset (of CSP {@link String}).
     * @throws CspRuntimeException with {@link CspStatus#ERROR_IN_STRUCT_FORMAT} if charset is null.
     */
    public static void requireStringCharset(@Nullable Charset charset)
    {
        if (charset == null)
        {
            throw CspRuntimeException.createCspRuntimeException(CspStatus.ERROR_IN_STRUCT_FORMAT,
                MessageFormat.format(Messages.CspStatus_Error_in_struct_format_Property__0__for_struct__1__not_set,
                    "charset", "CSP String"));
        }
    }

    /**
     * Requires class which must be used for some {@link Collection} processing.
     * 
     * @param overrideClass Class whose class processor must be used instead of standard {@link Collection} data
     *                      processor methods. If not null, then declaredClass must be assignable from it.
     *                      If equals to null, then there is no override class and {@code Collection.class} will be
     *                      returned.
     * @param declaredClass Declared class.
     * @return class which must be used for some {@link Collection} processing.
     * @throws CspRuntimeException with {@link CspStatus#ERROR_IN_STRUCT_FORMAT} if overrideClass is not null and
     * declaredClass is not assignable from it.
     */
    public static Class<?> requireClassForCollectionProcessing(@Nullable Class<?> overrideClass, Class<?> declaredClass)
    {
        if (overrideClass != null)
        {
            validateOverrideClassToDeclaredClass(overrideClass, declaredClass, TypeBoundKind.UPPER_BOUND);
            return overrideClass;
        }
        return Collection.class;
    }

    /**
     * Requires class which must be used for some {@link Map} processing.
     *
     * @param overrideClass Class whose class processor must be used instead of standard {@link Map} data
     *                      processor methods. If not null, then declaredClass must be assignable from it.
     *                      If equals to null, then there is no override class and {@code Map.class} will be returned.
     * @param declaredClass Declared class.
     * @return class which must be used for some {@link Map} processing.
     * @throws CspRuntimeException with {@link CspStatus#ERROR_IN_STRUCT_FORMAT} if overrideClass is not null and
     * declaredClass is not assignable from it.
     */
    public static Class<?> requireClassForMapProcessing(@Nullable Class<?> overrideClass, Class<?> declaredClass)
    {
        if (overrideClass != null)
        {
            validateOverrideClassToDeclaredClass(overrideClass, declaredClass, TypeBoundKind.UPPER_BOUND);
            return overrideClass;
        }
        return Map.class;
    }

    /**
     * Requires class which must be used for some type processing.
     *
     * @param overrideClass Class whose class processor must be used instead of declaredClass class processor.
     *                      If not null and typeBoundKind equals to {@link TypeBoundKind#UPPER_BOUND}, then
     *                      declaredClass must be assignable from it.
     *                      If not null and typeBoundKind equals to {@link TypeBoundKind#LOWER_BOUND}, then
     *                      it must be assignable from declaredClass.
     *                      If equals to null, then there is no override class and declaredClass will be returned.
     * @param declaredClass Declared class.
     * @return class which must be used for some type processing.
     * @throws CspRuntimeException with {@link CspStatus#ERROR_IN_STRUCT_FORMAT} if overrideClass is not null and
     * assignability check for it and declaredClass is failed.
     */
    public static Class<?> requireClassForProcessing(@Nullable Class<?> overrideClass, Class<?> declaredClass,
        TypeBoundKind typeBoundKind)
    {
        if (overrideClass != null)
        {
            validateOverrideClassToDeclaredClass(overrideClass, declaredClass, typeBoundKind);
            return overrideClass;
        }
        return declaredClass;
    }

    /**
     * Validates fixed array size correctness.
     *
     * @param fixedArraySize Size of fixed array. Can be null, if array has no fixed size.
     * @throws CspRuntimeException with {@link CspStatus#ERROR_IN_STRUCT_FORMAT} if fixedArraySize is not null and
     * lesser than 1.
     */
    public static void validateFixedArraySize(@Nullable Integer fixedArraySize)
    {
        if (fixedArraySize != null && fixedArraySize < 1)
        {
            throw CspRuntimeException.createCspRuntimeException(CspStatus.ERROR_IN_STRUCT_FORMAT,
                Messages.CspStatus_Error_in_struct_format_Array_size_number_is_invalid);
        }
    }

    /**
     * Validates wildcard bounds length.
     *
     * @param overrideClass
     * @param lowerBoundLength
     * @param upperBoundLength
     */
    public static void validateWildCardBoundsLength(@Nullable Class<?> overrideClass,
        int lowerBoundLength, int upperBoundLength)
    {
        if (overrideClass == null && lowerBoundLength == 0 && upperBoundLength == 0)
        {
            throw CspRuntimeException.createCspRuntimeException(CspStatus.ERROR_IN_STRUCT_FORMAT,
                Messages.CspStatus_Error_in_struct_format_Unbound_wildcard_type_cannot_be_processed);
        }
    }

    public static void validateImplementationOverrideClass(Class<?> implementationClazz, Class<?> declaredClass)
    {
        if (!declaredClass.isAssignableFrom(implementationClazz))
        {
            throw CspRuntimeException.createCspRuntimeException(CspStatus.ERROR_IN_STRUCT_FORMAT,
                MessageFormat.format(
                    Messages.CspStatus_Error_in_struct_format_Class__0__cannot_be_implementation_of_class__1,
                    implementationClazz, declaredClass));
        }
    }

    private static void validateOverrideClassToDeclaredClass(Class<?> overrideClass, Class<?> declaredClass,
        TypeBoundKind typeBoundKind)
    {
        boolean canOverride = typeBoundKind == TypeBoundKind.UPPER_BOUND
                              ? declaredClass.isAssignableFrom(overrideClass)
                              : overrideClass.isAssignableFrom(declaredClass);
        if (!canOverride || declaredClass.getTypeParameters().length != overrideClass.getTypeParameters().length)
        {
            throw CspRuntimeException.createCspRuntimeException(CspStatus.ERROR_IN_STRUCT_FORMAT,
                MessageFormat.format(Messages.CspStatus_Error_in_struct_format_Class__0__cannot_override_class__1,
                    typeBoundKind == TypeBoundKind.UPPER_BOUND ? overrideClass : declaredClass,
                    typeBoundKind == TypeBoundKind.UPPER_BOUND ? declaredClass : overrideClass));
        }
    }
}
