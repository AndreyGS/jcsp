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

package io.andreygs.jcsp.base.processing.internal;

import io.andreygs.jcsp.base.common.internal.ArgumentChecker;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Sole implementation of {@link ICspDataProcessorRegistry}.
 * <p>
 * Uses RW lock to access to WeakHashMap of classes with processors.
 */
final class CspDataProcessorRegistry<P>
    implements ICspDataProcessorRegistry<P>
{
    private final ReadWriteLock rwLockForOrdinaryProcessors = new ReentrantReadWriteLock();
    private final ReadWriteLock rwLockForGenericProcessors = new ReentrantReadWriteLock();
    private final ReadWriteLock rwLockForCompositeProcessors = new ReentrantReadWriteLock();
    private final Map<Class<?>, P> ordinaryProcessors = new WeakHashMap<>();
    private final Map<Class<?>, IGenericProcessorHolder<P>> genericProcessors = new WeakHashMap<>();
    private final Map<AnnotatedType, P> compositeProcessors = new HashMap<>();

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
            registerOrdinaryProcessor(clazz, processor);
        }
        else
        {
            registerGenericProcessor(clazz, processor);
        }
    }

    @Override
    public void registerProcessor(AnnotatedType compositeType, P processor)
    {
        ArgumentChecker.nonNull(compositeType, processor);
        registerCompositeProcessor(compositeType, processor);
    }

    @Override
    public Optional<P> findOrdinaryProcessor(Class<?> clazz)
    {
        rwLockForOrdinaryProcessors.readLock().lock();
        try
        {
            return Optional.ofNullable(ordinaryProcessors.get(clazz));
        }
        finally
        {
            rwLockForOrdinaryProcessors.readLock().unlock();
        }
    }

    @Override
    public Optional<IGenericProcessorHolder<P>> findGenericProcessor(Class<?> clazz)
    {
        rwLockForGenericProcessors.readLock().lock();
        try
        {
            return Optional.ofNullable(genericProcessors.get(clazz));
        }
        finally
        {
            rwLockForGenericProcessors.readLock().unlock();
        }
    }

    @Override
    public Optional<P> findGenericProcessor(AnnotatedType compositeType)
    {
        rwLockForCompositeProcessors.readLock().lock();
        try
        {
            return Optional.ofNullable(compositeProcessors.get(compositeType));
        }
        finally
        {
            rwLockForCompositeProcessors.readLock().unlock();
        }
    }

    private void registerOrdinaryProcessor(Class<?> clazz, P processor)
    {
        rwLockForOrdinaryProcessors.writeLock().lock();
        try
        {
            ordinaryProcessors.put(clazz, processor);
        }
        finally
        {
            rwLockForOrdinaryProcessors.writeLock().unlock();
        }
    }

    private void registerGenericProcessor(Class<?> clazz, P processor)
    {
        TypeVariable<? extends Class<?>>[] typeVariables = clazz.getTypeParameters();
        List<String> typeVariableNames = Arrays.stream(typeVariables).map(TypeVariable::getName).toList();
        GenericProcessorHolder<P> genericProcessorHolder = new GenericProcessorHolder<>(processor, typeVariableNames);
        rwLockForGenericProcessors.writeLock().lock();
        try
        {
            genericProcessors.put(clazz, genericProcessorHolder);
        }
        finally
        {
            rwLockForGenericProcessors.writeLock().unlock();
        }
    }

    private void registerCompositeProcessor(AnnotatedType compositeType, P processor)
    {
        rwLockForCompositeProcessors.writeLock().lock();
        try
        {
            compositeProcessors.put(compositeType, processor);
        }
        finally
        {
            rwLockForCompositeProcessors.writeLock().unlock();
        }
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
