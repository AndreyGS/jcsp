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

import io.andreygs.jcsp.base.processing.annotations.CspClassOverride;
import io.andreygs.jcsp.base.processing.annotations.CspFixedSizeArray;
import io.andreygs.jcsp.base.processing.annotations.CspMultiLevelPointer;
import io.andreygs.jcsp.base.processing.annotations.CspReference;
import io.andreygs.jcsp.base.processing.annotations.CspStringCharset;
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
import java.util.Set;

/**
 * TODO: place description here
 */
public abstract class AbstractCspTypeDescriptorExtractor<T>
{
    public static final Set<Class<?>> VALID_RANDOM_REFERENCE_ANNOTATION_TYPES = Set.of(CspReference.class,
        CspClassOverride.class);

    public static final Set<Class<?>> VALID_STRING_ANNOTATION_TYPES = Set.of(CspReference.class,
        CspStringCharset.class);

    public static final Set<Class<?>> VALID_GENERIC_TYPE_ANNOTATION_TYPES = Set.of(CspReference.class,
        CspClassOverride.class);

    public static final Set<Class<?>> VALID_ARRAY_ANNOTATION_TYPES = Set.of(CspReference.class,
        CspFixedSizeArray.class, CspMultiLevelPointer.class);

    private final CspTypeDescriptor cspTypeDescriptor;

    protected AbstractCspTypeDescriptorExtractor()
    {
        this(null);
    }

    protected AbstractCspTypeDescriptorExtractor(
        @Nullable Map<String, CspTypeDescriptor> genericParameterCspTypeDescriptors)
    {
        AnnotatedType annotatedType = extractAnnotatedType();
        cspTypeDescriptor = requireCspTypeDescriptor(annotatedType, genericParameterCspTypeDescriptors);
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
                requireCspTypeDescriptor(annotatedType, genericParameterCspTypeDescriptors)));
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
        CspTypeDescriptor componentTypeDescriptor = requireCspTypeDescriptor(annotatedComponentType,
            genericParameterCspTypeDescriptors);
        Class<?> clazz = Array.newInstance(componentTypeDescriptor.getClazz(), 0).getClass();
        CspTypeDescriptor cspTypeDescriptor = new CspTypeDescriptor(clazz, annotatedArrayType.getAnnotations());
        cspTypeDescriptor.nestedCspTypeDescriptors.add(componentTypeDescriptor);
        return cspTypeDescriptor;
    }

    private static CspTypeDescriptor requireCspTypeVariableTypeDescriptor(AnnotatedTypeVariable annotatedTypeVariable,
        @Nullable Map<String, CspTypeDescriptor> genericParameterCspTypeDescriptors)
    {
        if (genericParameterCspTypeDescriptors == null)
        {
            throw new IllegalArgumentException("genericCspTypeDescriptors cannot be null if there are TypeVariable<?>s!");
        }
        TypeVariable<?> typeVariable = (TypeVariable<?>)annotatedTypeVariable.getType();
        String typeVariableName = typeVariable.getName();
        boolean typeVariableReference = Arrays.stream(annotatedTypeVariable.getAnnotations()).anyMatch(
            annotation -> annotation.annotationType() == CspReference.class);

        CspTypeDescriptor genericParamDescriptor = genericParameterCspTypeDescriptors.get(typeVariableName);
        if (genericParamDescriptor == null)
        {
            throw new IllegalArgumentException("Type variable " + typeVariable.getName() + " not found!");
        }

        return genericParamDescriptor;
    }

    private static CspTypeDescriptor overrideCspReference(AnnotatedTypeVariable annotatedTypeVariable,
        CspTypeDescriptor genericParamDescriptor)
    {
        Set<Annotation> typeVariableAnnotations = Set.of(annotatedTypeVariable.getAnnotations());
        boolean typeVariableIsReference = typeVariableAnnotations.stream().anyMatch(
            annotation -> annotation.annotationType() == CspReference.class);
        boolean genericParamIsReference =
            genericParamDescriptor.annotations.stream().anyMatch(
                annotation -> annotation.annotationType() == CspReference.class);
        if (typeVariableIsReference != genericParamIsReference)
        {
            if (!typeVariableIsReference)
            {

            }
        }
    }

    private static CspTypeDescriptor requireCspTypeDescriptor(AnnotatedType annotatedType,
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
            return requireCspTypeVariableTypeDescriptor(annotatedTypeVariable, genericParameterCspTypeDescriptors);
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

    public interface ICspTypeDescriptor
    {
        Class<?> getClazz();
        Set<Annotation> getAnnotations();
        List<ICspTypeDescriptor> getNestedCspTypeDescriptors();
    }

    private static class CspTypeVariableDescriptor
    {
        private final String name;
        private final boolean reference;

        private CspTypeVariableDescriptor(String name, boolean reference)
        {
            this.name = name;
            this.reference = reference;
        }
    }

    private static class CspTypeDescriptor implements ICspTypeDescriptor
    {
        private final Class<?> clazz;
        private final Set<Annotation> annotations;
        private final List<ICspTypeDescriptor> nestedCspTypeDescriptors = new ArrayList<>();

        private CspTypeDescriptor(Class<?> clazz, Annotation[] annotations)
        {
            this.clazz = clazz;
            this.annotations = Set.of(annotations);
        }

        public final Class<?> getClazz()
        {
            return clazz;
        }

        public final Set<Annotation> getAnnotations()
        {
            return annotations;
        }

        public final List<ICspTypeDescriptor> getNestedCspTypeDescriptors()
        {
            return Collections.unmodifiableList(nestedCspTypeDescriptors);
        }
    }
}
