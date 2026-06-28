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
import io.andreygs.jcsp.internal.processing.data.type.ITypeBoundsDescriptor;
import io.andreygs.jcsp.internal.processing.data.type.TypeBoundKind;
import io.andreygs.jcsp.internal.processing.data.type.TypeIdKind;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * Utility class for evaluating CSP type traits of fields and their validation.
 */
public class CspFieldTypeUtils
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
     * Resolves class which must be used for processing of wildcard.
     *
     * @param overrideClass Class whose class processor must be used for processing of wildcard. If wildcard has bounds
     *                      and override class is not null, then it must conform them - if bound is upper then override
     *                      class must extend it, and if is lower, then bound class must extend override class. This
     *                      check is performing here if bounds are specific classes, but if bound is type variable, then
     *                      it couldn't be validated with current data and no checking is performed here.
     * @param typeBoundsDescriptor Descriptor of wildcard type bounds which is part of field type. Note that descriptor
     *                             can have at most one bound, because field type declaration does not allow to have
     *                             more. Can be null, if wildcard is unbound (upper-bounded with {@link Object}).
     * @return optional of class for wildcard processing and an empty optional if type bounds descriptor has type
     * variable bound.
     * @throws CspRuntimeException with {@link CspStatus#ERROR_IN_STRUCT_FORMAT} if wildcard has no override class
     * and is unbound. Also, if bound is upper and override class not extend it, or if bound is lower and bound class
     * not extend override class, or if descriptor has more than one bound.
     */
    public static Optional<Class<?>> resolveClassForWildCardProcessing(@Nullable Class<?> overrideClass,
        @Nullable ITypeBoundsDescriptor typeBoundsDescriptor)
    {
        if (overrideClass == null && typeBoundsDescriptor == null)
        {
            throw CspRuntimeException.createCspRuntimeException(CspStatus.ERROR_IN_STRUCT_FORMAT,
                Messages.CspStatus_Error_in_struct_format_Unbound_and_not_overridden_wildcard_type_cannot_be_processed);
        }
        if (typeBoundsDescriptor != null && typeBoundsDescriptor.getBoundClasses().size() > 1)
        {
            // This should never happen.
            // We can be here because of one of two reasons:
            // 1. Method was called with descriptor not belonging to part of field type.
            // 2. Java specification was changed and now allows more than one bound for wildcard in field type.
            throw CspRuntimeException.createCspRuntimeException(CspStatus.ERROR_IN_STRUCT_FORMAT,
                Messages.CspStatus_Error_in_struct_format_Internal_error_wildcard_has_illegal_number_of_bounds);
        }
        if (overrideClass != null)
        {
            if (typeBoundsDescriptor != null && typeBoundsDescriptor.getTypeIdKind() == TypeIdKind.CLASS)
            {
                TypeBoundKind typeBoundKind = typeBoundsDescriptor.getTypeBoundKind();
                Class<?> boundClass = typeBoundsDescriptor.getBoundClasses().iterator().next();
                validateOverrideClassToDeclaredClass(overrideClass, boundClass, typeBoundKind);
            }
            return Optional.of(overrideClass);
        }
        // Here (typeBoundsDescriptor != null) is always true,
        // because here (overrideClass == null) and  (overrideClass == null && typeBoundsDescriptor == null)
        // are always false.
        if (typeBoundsDescriptor.getTypeIdKind() == TypeIdKind.CLASS)
        {
            Class<?> boundClass = typeBoundsDescriptor.getBoundClasses().iterator().next();
            return Optional.of(boundClass);
        }
        return Optional.empty();
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

    /**
     * Validates that override class is conforming to declared class.
     *
     * @param overrideClass Class which is overrides declared class.
     *                      If not null and typeBoundKind equals to {@link TypeBoundKind#UPPER_BOUND}, then
     *                      declaredClass must be assignable from it.
     *                      If not null and typeBoundKind equals to {@link TypeBoundKind#LOWER_BOUND}, then
     *                      it must be assignable from declaredClass.
     * @param declaredClass Declared class.
     * @param typeBoundKind Kind of type bound.
     * @throws CspRuntimeException with {@link CspStatus#ERROR_IN_STRUCT_FORMAT} if overrideClass is not null and
     * assignability check for it and declaredClass is failed.
     */
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
