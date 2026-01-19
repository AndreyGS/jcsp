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

import io.andreygs.jcsp.base.processing.CspSpecializedProcessorRegistrarProvider;
import io.andreygs.jcsp.base.processing.ICspSpecializedProcessor;
import io.andreygs.jcsp.base.processing.ICspSpecializedProcessorRegistrar;
import io.andreygs.jcsp.base.processing.context.ICspDataMessageDeserializationContext;
import io.andreygs.jcsp.base.processing.context.ICspDataMessageSerializationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.BiConsumer;

/**
 * TODO: place description here
 */
public class CspSpecializedProcessingMethodProvider
    implements ICspSpecializedProcessingMethodProvider
{
    private final CspSpecializedProcessorRegistrar cspSpecializedProcessorRegistrar =
        (CspSpecializedProcessorRegistrar)CspSpecializedProcessorRegistrarProvider.provideCspSpecializedProcessorRegistrar();

    @Override
    public BiConsumer<Object, ICspDataMessageSerializationContext> provideSerializationMethod(Class<?> clazz)
    {
        cspSpecializedProcessorRegistrar.rwLock.readLock().lock();
        try
        {
            return cspSpecializedProcessorRegistrar.serializationMethods.get(clazz);
        }
        finally
        {
            cspSpecializedProcessorRegistrar.rwLock.readLock().lock();
        }
    }

    @Override
    public BiConsumer<ICspDataMessageDeserializationContext, Object>  provideDeserializationMethod(Class<?> clazz)
    {
        cspSpecializedProcessorRegistrar.rwLock.readLock().lock();
        try
        {
            return cspSpecializedProcessorRegistrar.deserializationMethods.get(clazz);
        }
        finally
        {
            cspSpecializedProcessorRegistrar.rwLock.readLock().lock();
        }
    }

    public static final class CspSpecializedProcessorRegistrar
        implements ICspSpecializedProcessorRegistrar
    {
        private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
        private final Map<Class<?>, BiConsumer<Object, ICspDataMessageSerializationContext>> serializationMethods
            = new HashMap<>();
        private final Map<Class<?>, BiConsumer<ICspDataMessageDeserializationContext, Object>> deserializationMethods
            = new HashMap<>();
        private final ICspSpecializedProcessingMethodProvider cspSpecializedProcessingMethodProvider
            = new CspSpecializedProcessingMethodProvider();

        @Override
        public void registerProcessor(Class<?> clazz, ICspSpecializedProcessor processor)
        {
            rwLock.writeLock().lock();
            try
            {
                serializationMethods.put(clazz, processor::serialize);
                deserializationMethods.put(clazz, processor::deserialize);
            }
            finally
            {
                rwLock.writeLock().lock();
            }
        }

        @Override
        public void unregisterProcessor(Class<?> clazz)
        {
            rwLock.writeLock().lock();
            try
            {
                serializationMethods.remove(clazz);
                deserializationMethods.remove(clazz);
            }
            finally
            {
                rwLock.writeLock().lock();
            }
        }
    }
}
