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
import io.andreygs.jcsp.internal.model.utils.ArgumentChecker;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
    private final Map<Class<?>, P> ordinaryProcessors = new ConcurrentHashMap<>();
    private final Map<Class<?>, IGenericProcessorHolder<P>> genericProcessors = new ConcurrentHashMap<>();
    private final Map<AnnotatedType, TP> proxyProcessors = new ConcurrentHashMap<>();

    @Override
    public void registerProcessor(Class<?> clazz, P processor)
    {
        ArgumentChecker.nonNull(clazz, processor);
        if (clazz.isPrimitive() || clazz.isArray() || clazz.isEnum())
        {
            throw new IllegalArgumentException("Only ordinary and generic classes can be added!");
        }
        if (clazz.getTypeParameters().length == 0)
        {
            ordinaryProcessors.put(clazz, processor);
        }
        else
        {
            TypeVariable<? extends Class<?>>[] typeVariables = clazz.getTypeParameters();
            List<String> typeVariableNames = Arrays.stream(typeVariables).map(TypeVariable::getName).toList();
            GenericProcessorHolder<P> genericProcessorHolder = new GenericProcessorHolder<>(processor, typeVariableNames);
            genericProcessors.put(clazz, genericProcessorHolder);
        }
    }

    @Override
    public void registerProxyProcessor(AnnotatedType annotatedType, TP proxyProcessor)
    {
        ArgumentChecker.nonNull(annotatedType, proxyProcessor);
        proxyProcessors.put(annotatedType, proxyProcessor);
    }

    @Override
    public Optional<P> findOrdinaryProcessor(Class<?> clazz)
    {
         return Optional.ofNullable(ordinaryProcessors.get(clazz));
    }

    @Override
    public Optional<IGenericProcessorHolder<P>> findGenericProcessor(Class<?> clazz)
    {
        return Optional.ofNullable(genericProcessors.get(clazz));
    }

    @Override
    public Optional<TP> findGenericProcessor(AnnotatedType annotatedType)
    {
        return Optional.ofNullable(proxyProcessors.get(annotatedType));
    }

    @Override
    public void unregisterProcessor(Class<?> clazz)
    {
        if (clazz.getTypeParameters().length == 0)
        {
            ordinaryProcessors.remove(clazz);
        }
        else
        {
            genericProcessors.remove(clazz);
        }
    }

    @Override
    public void unregisterProxyProcessor(CspTypeToken<?> cspTypeToken)
    {
        proxyProcessors.remove(cspTypeToken.getAnnotatedType());
    }

    private static class GenericProcessorHolder<P> implements IGenericProcessorHolder<P>
    {
        private final P processor;
        private final List<String> typeVariableNames;

        public GenericProcessorHolder(P processor, List<String> typeVariableNames)
        {
            this.processor = processor;
            this.typeVariableNames = typeVariableNames;
        }

        public P getProcessor()
        {
            return processor;
        }

        public List<String> getTypeVariableNames()
        {
            return typeVariableNames;
        }
    }
}
