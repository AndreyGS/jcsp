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

package io.andreygs.jcsp.base.processing.composite.internal;

import io.andreygs.jcsp.base.processing.annotations.internal.CspAnnotationUtils;
import io.andreygs.jcsp.base.types.CspRuntimeException;
import io.andreygs.jcsp.base.types.CspStatus;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.AnnotatedWildcardType;
import java.lang.reflect.TypeVariable;
import java.util.Optional;

/**
 * TODO: place description here
 */
public class CspTypeUtils
{
    private CspTypeUtils()
    {

    }

    public static Class<?> selectProcessorClass(Class<?> declaredClazz, Class<?> overrideProcessorClazz)
    {
        Optional<Class<?>> overrideProcessorClazz = CspAnnotationUtils.resolveCspOverrideProcessorClass(annotatedType);
    }

    public static ClassOrTypeVariableName selectProcessorClassOrTypeVariableName(AnnotatedType annotatedType)
    {
        Optional<Class<?>> declaredClazz = AnnotatedTypeUtils.resolveClazz(annotatedType);
        if (declaredClazz.isEmpty())
        {
            if (annotatedType instanceof TypeVariable<?> typeVariable)
            {
                String name = typeVariable.getName();
                return new ClassOrTypeVariableName(name);
            }
            else if (annotatedType instanceof AnnotatedWildcardType annotatedWildcardType)
            {
                return selectWildcardProcessorClass(annotatedWildcardType);
            }
        }
        Optional<Class<?>> overrideProcessorClazz = CspAnnotationUtils.resolveCspOverrideProcessorClass(annotatedType);
        if (overrideProcessorClazz.isEmpty())
        {
            if (declaredClazz.isEmpty())
            {
                if (annotatedType instanceof AnnotatedWildcardType)
                {

                }
                else
                {
                    throw CspRuntimeException.createCspRuntimeException(CspStatus.ERROR_IN_STRUCT_FORMAT,
                        Messages.CspStatus_Error_in_struct_format_Unknown_type_category);
                }
            }
        }
        else
        {

        }
    }

    public static ClassOrTypeVariableName selectWildcardProcessorClassOrTypeVariableName(
        AnnotatedWildcardType annotatedWildcardType)
    {
        AnnotatedTypeUtils.WildcardBoundaries wildcardBoundaries =
            AnnotatedTypeUtils.requireWildcardBoundaries(annotatedWildcardType);
        switch (wildcardBoundaries.getBoundaryType())
        {
        case UNBOUNDED:
            return selectUnboundedWildcardProcessorClassOrTypeVariableName(annotatedWildcardType);
        case LOWER_BOUNDED:


        }
    }

    private static ClassOrTypeVariableName selectUnboundedWildcardProcessorClassOrTypeVariableName(
        AnnotatedType annotatedType)
    {
        Optional<Class<?>> overrideProcessorClazz = CspAnnotationUtils.resolveCspOverrideProcessorClass(annotatedType);
        if (overrideProcessorClazz.isEmpty())
        {
            throw CspRuntimeException.createCspRuntimeException(CspStatus.ERROR_IN_STRUCT_FORMAT,
                Messages.CspStatus_Error_in_struct_format_Unbound_wildcard_type_cannot_be_processed);
        }
        return new ClassOrTypeVariableName(overrideProcessorClazz.get());
    }

    private static ClassOrTypeVariableName selectLowerBoundedWildcardProcessorClassOrTypeVariableName(
        AnnotatedTypeUtils.WildcardBoundaries wildcardBoundaries, AnnotatedType annotatedType)
    {
        Optional<Class<?>> overrideProcessorClazz = CspAnnotationUtils.resolveCspOverrideProcessorClass(annotatedType);
        if (overrideProcessorClazz.isEmpty())
        {
            AnnotatedType firstBoundAnnotatedType = wildcardBoundaries.getBoundaryAnnotatedTypes().iterator().next();
            if (firstBoundAnnotatedType instanceof TypeVariable<?> firstBoundTypeVariable)
            {
                String name = firstBoundTypeVariable.getName();
                return new ClassOrTypeVariableName(name);
            }
            Optional<Class<?>> firstBoundAnnotatedClazz = AnnotatedTypeUtils.resolveClazz(firstBoundAnnotatedType);
            if (firstBoundAnnotatedClazz.isEmpty())
            {
                throw CspRuntimeException.createCspRuntimeException(CspStatus.ERROR_IN_STRUCT_FORMAT,
                    Messages.CspStatus_Error_in_struct_format_Unknown_type_category);
            }
            return new ClassOrTypeVariableName(firstBoundAnnotatedClazz.get());
        }
        for (AnnotatedType boundAnnotatedType : wildcardBoundaries.getBoundaryAnnotatedTypes())
        {
            boundAnnotatedType
        }
    }

    public static class ClassOrTypeVariableName
    {
        private final @Nullable Class<?> clazz;
        private final @Nullable String name;

        public ClassOrTypeVariableName(Class<?> clazz)
        {
            this.clazz = clazz;
            name = null;
        }

        public ClassOrTypeVariableName(String name)
        {
            clazz = null;
            this.name = name;
        }

        public Optional<Class<?>> getClazz()
        {
            return Optional.ofNullable(clazz);
        }

        public Optional<String> getName()
        {
            return Optional.ofNullable(name);
        }
    }
}
