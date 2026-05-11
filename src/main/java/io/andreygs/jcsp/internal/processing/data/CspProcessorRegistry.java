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

import io.andreygs.jcsp.api.model.protocol.utils.CspTypeToken;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Sole implementation of {@link ICspProcessorRegistry}.
 * <p>
 * Uses RW lock to access to WeakHashMap of classes with processors.
 */
public final class CspProcessorRegistry<P, TP>
    implements ICspProcessorRegistry<P, TP>
{
    private final Map<Class<?>, P> ordinaryClassProcessors = new ConcurrentHashMap<>();
    private final Map<Class<?>, IGenericClassProcessorHolder<P>> genericClassProcessors = new ConcurrentHashMap<>();
    private final Map<AnnotatedType, TP> typeProcessors = new ConcurrentHashMap<>();

    @Override
    public void registerClassProcessor(Class<?> clazz, P classProcessor)
    {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(classProcessor, "classProcessor");
        if (clazz.isPrimitive() || clazz.isArray() || clazz.isEnum())
        {
            throw new IllegalArgumentException("Only ordinary and generic classes can be added!");
        }
        if (clazz.getTypeParameters().length == 0)
        {
            ordinaryClassProcessors.put(clazz, classProcessor);
        }
        else
        {
            TypeVariable<? extends Class<?>>[] typeVariables = clazz.getTypeParameters();
            List<String> typeVariableNames = Arrays.stream(typeVariables).map(TypeVariable::getName).toList();
            GenericClassProcessorHolder<P> genericProcessorHolder =
                new GenericClassProcessorHolder<>(classProcessor, typeVariableNames);
            genericClassProcessors.put(clazz, genericProcessorHolder);
        }
    }

    @Override
    public void registerTypeProcessor(AnnotatedType annotatedType, TP typeProcessor)
    {
        typeProcessors.put(Objects.requireNonNull(annotatedType, "annotatedType"),
            Objects.requireNonNull(typeProcessor, "typeProcessor"));
    }

    @Override
    public Optional<P> findOrdinaryProcessor(Class<?> clazz)
    {
         return Optional.ofNullable(ordinaryClassProcessors.get(clazz));
    }

    @Override
    public Optional<IGenericClassProcessorHolder<P>> findGenericProcessor(Class<?> clazz)
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
    public void unregisterTypeProcessor(CspTypeToken<?> cspTypeToken)
    {
        typeProcessors.remove(cspTypeToken.getAnnotatedType());
    }

    private static class GenericClassProcessorHolder<P> implements IGenericClassProcessorHolder<P>
    {
        private final P classProcessor;
        private final List<String> typeVariableNames;

        public GenericClassProcessorHolder(P classProcessor, List<String> typeVariableNames)
        {
            this.classProcessor = classProcessor;
            this.typeVariableNames = typeVariableNames;
        }

        public P getClassProcessor()
        {
            return classProcessor;
        }

        public List<String> getTypeVariableNames()
        {
            return typeVariableNames;
        }
    }
}
