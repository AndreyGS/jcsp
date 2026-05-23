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

package io.andreygs.jcsp.internal.processing.data;

import io.andreygs.jcsp.api.processing.data.ICspClassDeserializationProcessor;
import io.andreygs.jcsp.api.processing.data.ICspClassSerializationProcessor;
import io.andreygs.jcsp.internal.processing.data.model.IGenericClassProcessorHolder;
import io.andreygs.jcsp.internal.processing.data.model.ITypeVariableDescriptor;
import io.andreygs.jcsp.internal.processing.data.type.ICspTypeDeserializationProcessor;
import io.andreygs.jcsp.internal.processing.data.type.ICspTypeSerializationProcessor;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Registry for class and type processors that are using for serialization/deserialization processes.
 * <p>
 * Thread-safe. Uses {@link ConcurrentHashMap} instances for implementing thread-safety.
 *
 * @param <CP> {@link ICspClassSerializationProcessor} or {@link ICspClassDeserializationProcessor},
 *            depending on what kind of registry is.
 * @param <TP> {@link ICspTypeSerializationProcessor} or {@link ICspTypeDeserializationProcessor},
 *            depending on what kind of registry is.
 */
public final class CspProcessorRegistry<CP, TP>
    implements ICspProcessorRegistry<CP, TP>
{
    private final Map<Class<?>, CP> ordinaryClassProcessors = new ConcurrentHashMap<>();
    private final Map<Class<?>, IGenericClassProcessorHolder<CP>> genericClassProcessors = new ConcurrentHashMap<>();
    private final Map<AnnotatedType, TP> typeProcessors = new ConcurrentHashMap<>();

    @Override
    public void registerClassProcessor(Class<?> clazz, CP classProcessor)
    {
        if (clazz.isPrimitive() || clazz.isArray())
        {
            throw new IllegalArgumentException(Messages.CspProcessorRegistry_Illegal_type_group);
        }
        if (clazz == String.class || clazz == Collection.class || clazz == Map.class)
        {
            throw new IllegalArgumentException(Messages.CspProcessorRegistry_Illegal_class);
        }
        if (clazz.getTypeParameters().length == 0)
        {
            ordinaryClassProcessors.put(clazz, classProcessor);
        }
        else
        {
            TypeVariable<? extends Class<?>>[] typeVariables = clazz.getTypeParameters();
            Set<String> typeVariableNames =
                Arrays.stream(typeVariables).map(TypeVariable::getName).collect(Collectors.toUnmodifiableSet());
            GenericClassProcessorHolder<CP> genericProcessorHolder =
                new GenericClassProcessorHolder<>(classProcessor, typeVariableNames);
            genericClassProcessors.put(clazz, genericProcessorHolder);
        }
    }

    @Override
    public void registerTypeProcessor(AnnotatedType annotatedType, TP typeProcessor)
    {
        typeProcessors.put(annotatedType, typeProcessor);
    }

    @Override
    public Optional<CP> findOrdinaryClassProcessor(Class<?> clazz)
    {
         return Optional.ofNullable(ordinaryClassProcessors.get(clazz));
    }

    @Override
    public Optional<IGenericClassProcessorHolder<CP>> findGenericClassProcessor(Class<?> clazz)
    {
        return Optional.ofNullable(genericClassProcessors.get(clazz));
    }

    @Override
    public Optional<TP> findTypeProcessor(AnnotatedType annotatedType)
    {
        return Optional.ofNullable(typeProcessors.get(annotatedType));
    }

    @Override
    public void unregisterClassProcessor(Class<?> clazz)
    {
        if (clazz.getTypeParameters().length == 0)
        {
            ordinaryClassProcessors.remove(clazz);
        }
        else
        {
            genericClassProcessors.remove(clazz);
        }
    }

    @Override
    public void unregisterTypeProcessor(AnnotatedType annotatedType)
    {
        typeProcessors.remove(annotatedType);
    }

    private IGenericClassProcessorHolder<CP> createGenericClassProcessorHolder(Class<?> clazz, CP classProcessor)
    {
        TypeVariable<? extends Class<?>>[] typeVariables = clazz.getTypeParameters();
        Set<ITypeVariableDescriptor> typeVariableTraits = new HashSet<>();
        for (TypeVariable<? extends Class<?>> typeVariable : typeVariables)
        {
            Type[] types = typeVariable.getBounds();
            if (types.length > 0)
            {
                Set<Class<?>> boundClasses = new HashSet<>();
                for (Type type : types)
                {
                    if (type instanceof ParameterizedType parameterizedType)
                    {
                        throw new IllegalArgumentException(Messages.CspProcessorRegistry_Illegal_type_group);;
                    }
                    else if (type instanceof GenericArrayType arrayType)
                    {
                        return createArrayProcessorSwitch(annotatedArrayType);
                    }
                    else if (type instanceof TypeVariable typeVariable)
                    {
                        return createTypeVariableProcessor(annotatedTypeVariable);
                    }
                    else if (type instanceof WildcardType wildcardType)
                    {
                        return createWildcardProcessorSwitch(annotatedWildcardType);
                    }
                    else
                    {
                        Type type = annotatedType.getType();
                        if (!(type instanceof Class<?> declaredClazz))
                }
            }
        }
        Set<String> typeVariableNames =
            Arrays.stream(typeVariables).map(TypeVariable::getName).collect(Collectors.toUnmodifiableSet());
        GenericClassProcessorHolder<CP> genericProcessorHolder =
            new GenericClassProcessorHolder<>(classProcessor, typeVariableNames);

    }

    /**
     * Holder for generic class processors.
     *
     * @param <CP> {@link ICspClassSerializationProcessor} or {@link ICspClassDeserializationProcessor}.
     */
    private static class GenericClassProcessorHolder<CP> implements IGenericClassProcessorHolder<CP>
    {
        private final CP classProcessor;
        private final Set<String> typeVariableNames;

        /**
         * Constructs an instance.
         *
         * @param classProcessor Class processor for generic class.
         * @param typeVariableNames Unmodifiable set of generic class type variables.
         */
        public GenericClassProcessorHolder(CP classProcessor, Set<String> typeVariableNames)
        {
            this.classProcessor = classProcessor;
            this.typeVariableNames = typeVariableNames;
        }

        @Override
        public CP getClassProcessor()
        {
            return classProcessor;
        }

        @Override
        public Set<String> getTypeVariableNames()
        {
            return typeVariableNames;
        }
    }
}
