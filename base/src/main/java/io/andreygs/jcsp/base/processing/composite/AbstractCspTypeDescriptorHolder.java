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

package io.andreygs.jcsp.base.processing.composite;

import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedArrayType;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.AnnotatedTypeVariable;
import java.lang.reflect.AnnotatedWildcardType;
import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * TODO: place description here
 */
public abstract class AbstractCspTypeDescriptorHolder<T>
{
    private final CspTypeDescriptor cspTypeDescriptor;

    protected AbstractCspTypeDescriptorHolder()
    {
        this(null);
    }

    protected AbstractCspTypeDescriptorHolder(
        @Nullable Map<String, CspTypeDescriptor> genericParameterCspTypeDescriptors)
    {
        AnnotatedType annotatedType = extractAnnotatedType();
        cspTypeDescriptor = createCspTypeDescriptor(annotatedType, genericParameterCspTypeDescriptors);
    }

    public final CspTypeDescriptor getCspTypeDescriptor()
    {
        return cspTypeDescriptor;
    }

    private AnnotatedType extractAnnotatedType()
    {
        AnnotatedType annotatedType = getClass().getAnnotatedSuperclass();
        if (!(annotatedType instanceof AnnotatedParameterizedType annotatedParameterizedType))
        {
            throw new IllegalArgumentException("AbstractCspTypeDescriptorHolder must be generic!");
        }
        return annotatedParameterizedType.getAnnotatedActualTypeArguments()[0];
    }

    private static CspTypeDescriptor createCspClassTypeDescriptor(AnnotatedType annotatedType, Class<?> clazz)
    {
        return new CspTypeDescriptor(clazz, annotatedType.getAnnotations());
    }

    private static CspTypeDescriptor createCspParametrizedTypeDescriptor(AnnotatedParameterizedType annotatedParameterizedType,
        @Nullable Map<String, CspTypeDescriptor> genericParameterCspTypeDescriptors)
    {
        ParameterizedType type = (ParameterizedType)annotatedParameterizedType.getType();
        CspTypeDescriptor cspTypeDescriptor = new CspTypeDescriptor((Class<?>)type.getRawType(),
            annotatedParameterizedType.getAnnotations());
        Arrays.stream(annotatedParameterizedType.getAnnotatedActualTypeArguments()).forEach(
            annotatedType -> cspTypeDescriptor.nestedCspTypeDescriptors.add(
                createCspTypeDescriptor(annotatedType, genericParameterCspTypeDescriptors)));
        return cspTypeDescriptor;
    }

    private static CspTypeDescriptor createCspGenericArrayTypeDescriptor(AnnotatedArrayType annotatedArrayType,
        @Nullable Map<String, CspTypeDescriptor> genericParameterCspTypeDescriptors)
    {
        AnnotatedType annotatedComponentType = annotatedArrayType.getAnnotatedGenericComponentType();
        if (annotatedComponentType.getType() instanceof Class<?> componentClazz && componentClazz.isPrimitive())
        {
            Class<?> clazz = Array.newInstance(componentClazz, 0).getClass();
            return new CspTypeDescriptor(clazz, annotatedArrayType.getAnnotations());
        }
        CspTypeDescriptor componentTypeDescriptor = createCspTypeDescriptor(annotatedComponentType,
            genericParameterCspTypeDescriptors);
        Class<?> clazz = Array.newInstance(componentTypeDescriptor.getClazz(), 0).getClass();
        CspTypeDescriptor cspTypeDescriptor = new CspTypeDescriptor(clazz, annotatedArrayType.getAnnotations());
        cspTypeDescriptor.nestedCspTypeDescriptors.add(componentTypeDescriptor);
        return cspTypeDescriptor;
    }

    private static CspTypeDescriptor createCspTypeVariableTypeDescriptor(AnnotatedTypeVariable annotatedTypeVariable,
        @Nullable Map<String, CspTypeDescriptor> genericParameterCspTypeDescriptors)
    {
        if (genericParameterCspTypeDescriptors == null)
        {
            throw new IllegalArgumentException("genericCspTypeDescriptors cannot be null if there are TypeVariable<?>s!");
        }
        TypeVariable<?> typeVariable = (TypeVariable<?>)annotatedTypeVariable.getType();
        String typeVariableName = typeVariable.getName();
        CspTypeDescriptor genericParamDescriptor = genericParameterCspTypeDescriptors.get(typeVariableName);
        if (genericParamDescriptor == null)
        {
            throw new IllegalArgumentException("Type variable " + typeVariable.getName() + " not found!");
        }
        return genericParamDescriptor;
    }

    private static CspTypeDescriptor createCspTypeDescriptor(AnnotatedType annotatedType,
        @Nullable Map<String, CspTypeDescriptor> genericParameterCspTypeDescriptors)
    {
        if (annotatedType instanceof AnnotatedParameterizedType annotatedParameterizedType)
        {
            return createCspParametrizedTypeDescriptor(annotatedParameterizedType, genericParameterCspTypeDescriptors);
        }
        else if (annotatedType instanceof AnnotatedArrayType annotatedArrayType)
        {
            return createCspGenericArrayTypeDescriptor(annotatedArrayType, genericParameterCspTypeDescriptors);
        }
        else if (annotatedType instanceof AnnotatedTypeVariable annotatedTypeVariable)
        {
            return createCspTypeVariableTypeDescriptor(annotatedTypeVariable, genericParameterCspTypeDescriptors);
        }
        else if (annotatedType instanceof AnnotatedWildcardType annotatedWildcardType)
        {
            throw new IllegalArgumentException("Wildcard type not supported!");
        }
        else
        {
            Type type = annotatedType.getType();
            if (!(type instanceof Class<?> clazz))
            {
                throw new IllegalArgumentException("Unsupported type: " + type);
            }
            return createCspClassTypeDescriptor(annotatedType, clazz);
        }
    }

    public static class CspTypeDescriptor
    {
        private final Class<?> clazz;
        private final List<Annotation> annotations;
        private final List<CspTypeDescriptor> nestedCspTypeDescriptors = new ArrayList<>();

        private CspTypeDescriptor(Class<?> clazz, Annotation[] annotations)
        {
            this.clazz = clazz;
            this.annotations = List.of(annotations);
        }

        public final Class<?> getClazz()
        {
            return clazz;
        }

        public final List<Annotation> getAnnotations()
        {
            return annotations;
        }

        public final List<CspTypeDescriptor> getNestedCspTypeDescriptors()
        {
            return Collections.unmodifiableList(nestedCspTypeDescriptors);
        }
    }
}
