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

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedArrayType;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.AnnotatedTypeVariable;
import java.lang.reflect.AnnotatedWildcardType;
import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * TODO: place description here
 */
public class AnnotatedTypeUtils
{
    private AnnotatedTypeUtils()
    {
    }

    public static <T> Optional<T> findAnnotation(AnnotatedType annotatedType, Class<T> annotationClazz)
    {
        Annotation[] annotations =  annotatedType.getAnnotations();
        Optional<Annotation> annotationOpt = Arrays.stream(annotations)
           .filter(annotation -> annotation.annotationType().equals(annotationClazz))
           .findFirst();
        if (annotationOpt.isEmpty())
        {
            return Optional.empty();
        }
        @SuppressWarnings("unchecked")
        T annotation = (T)annotationOpt.get();
        return Optional.of(annotation);
    }

    /**
     * Resolves class of annotated type.
     * <p>
     * If annotatedType is instanceof {@link AnnotatedTypeVariable} or {@link AnnotatedWildcardType} then
     * {@link Optional#empty()} is returned. Because first doesn't have class at all, and second can have more
     * than one class as bound and
     *
     * @param annotatedType
     * @return
     */
    public static Optional<Class<?>> resolveClazz(AnnotatedType annotatedType)
    {
        if (annotatedType instanceof AnnotatedParameterizedType annotatedParameterizedType)
        {
            return requireAnnotatedParametrizedTypeClass(annotatedParameterizedType);
        }
        else if (annotatedType instanceof AnnotatedArrayType annotatedArrayType)
        {
            return resolveClazz(annotatedArrayType);
        }
        else if (annotatedType instanceof AnnotatedTypeVariable annotatedTypeVariable)
        {
            return resolveClazz(annotatedTypeVariable);
        }
        else if (annotatedType instanceof AnnotatedWildcardType annotatedWildcardType)
        {
            return Optional.empty();
        }
        else
        {
            Class<?> clazz = (Class<?>)annotatedType.getType();
            return Optional.of(clazz);
        }
    }

    public static Class<?> requireAnnotatedParametrizedTypeClass(AnnotatedParameterizedType annotatedParameterizedType)
    {
        ParameterizedType parameterizedType = (ParameterizedType)annotatedParameterizedType.getType();
        return (Class<?>)parameterizedType.getRawType();
    }

    public static Optional<Class<?>> resolveClazz(AnnotatedArrayType annotatedArrayType)
    {
        AnnotatedType annotatedComponentType = annotatedArrayType.getAnnotatedGenericComponentType();
        Optional<Class<?>> componentClass = resolveClazz(annotatedComponentType);
        if (componentClass.isEmpty())
        {
            return Optional.empty();
        }
        Object testArray = Array.newInstance(componentClass.get(), 0);
        return Optional.of(testArray.getClass());
    }

    public static Optional<Class<?>> resolveClazz(AnnotatedTypeVariable annotatedTypeVariable)
    {
        return Optional.empty();
    }

    public static String requireTypeVariableName(AnnotatedTypeVariable annotatedTypeVariable)
    {
        TypeVariable<?> typeVariable = (TypeVariable<?>)annotatedTypeVariable;
        return typeVariable.getName();
    }

    public static WildcardBoundaries requireWildcardBoundaries(AnnotatedWildcardType annotatedWildcardType)
    {
        WildcardBoundaryType boundaryType = WildcardBoundaryType.getWildcardBoundaryType(annotatedWildcardType);
        AnnotatedType[] annotatedTypes =  boundaryType == WildcardBoundaryType.LOWER_BOUNDED
                                          ? annotatedWildcardType.getAnnotatedLowerBounds()
                                          : annotatedWildcardType.getAnnotatedUpperBounds();
        return new WildcardBoundaries(boundaryType,  annotatedTypes);
    }

    public enum WildcardBoundaryType
    {
        UNBOUNDED,
        LOWER_BOUNDED,
        UPPER_BOUNDED;

        public static WildcardBoundaryType getWildcardBoundaryType(AnnotatedWildcardType annotatedWildcardType)
        {
            return annotatedWildcardType.getAnnotatedLowerBounds().length > 0
                   ? LOWER_BOUNDED
                   : annotatedWildcardType.getAnnotatedUpperBounds().length > 0
                     ? UPPER_BOUNDED
                     : UNBOUNDED;
        }
    }

    public static class WildcardBoundaries
    {
        private final WildcardBoundaryType boundaryType;
        private final List<AnnotatedType> boundaryAnnotatedTypes;

        public WildcardBoundaries(WildcardBoundaryType boundaryType, AnnotatedType[] boundaryAnnotatedTypes)
        {
            this.boundaryType = boundaryType;
            this.boundaryAnnotatedTypes = List.of(boundaryAnnotatedTypes);
        }

        public WildcardBoundaryType getBoundaryType()
        {
            return boundaryType;
        }

        public List<AnnotatedType> getBoundaryAnnotatedTypes()
        {
            return boundaryAnnotatedTypes;
        }
    }
}
