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

import io.andreygs.jcsp.base.processing.ICspDataGeneralSerializationProcessor;
import io.andreygs.jcsp.base.processing.composite.ICspDataCompositeSerializationProcessor;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;

/**
 * TODO: place description here
 */
final class CspDataCompositeGenericSerializationProcessor
    extends AbstractCspDataCompositeSubProcessorHolder<ICspDataCompositeSerializationProcessor>
    implements ICspDataCompositeSerializationProcessor, ICspDataCompositeSerializationSubProcessorHolder
{
    private final ICspDataCompositeProcessorSerializationDelegate compositeProcessorDelegate;

    CspDataCompositeGenericSerializationProcessor(boolean reference, Class<?> clazz, int typeParametersNumber)
    {
        super(reference, typeParametersNumber);
        if (clazz.isAssignableFrom(Collection.class))
        {
            compositeProcessorDelegate = new CollectionCompositeProcessor();
        }
        else if (clazz.isAssignableFrom(Map.class))
        {
            compositeProcessorDelegate = new MapCompositeProcessor();
        }
        else
        {
            compositeProcessorDelegate = new RandomGenericCompositeProcessor(clazz);
        }
    }

    private CspDataCompositeGenericSerializationProcessor(CspDataCompositeGenericSerializationProcessor processor,
        boolean reference)
    {
        super(processor, reference);
        this.compositeProcessorDelegate = processor.compositeProcessorDelegate;
    }

    @Override
    public void serialize(@Nullable Object value, ICspDataGeneralSerializationProcessor generalSerializationProcessor)
    {
        compositeProcessorDelegate.serialize(value, generalSerializationProcessor);
    }

    @Override
    protected ICspDataCompositeSerializationProcessor createCopyInstanceWithOverriddenReference(boolean reference)
    {
        return new CspDataCompositeGenericSerializationProcessor(this, reference);
    }

    @Override
    protected ICspDataCompositeSerializationProcessor getThisAsProcessor()
    {
        return this;
    }

    private interface ICspDataCompositeProcessorSerializationDelegate
    {
        void serialize(@Nullable Object value, ICspDataGeneralSerializationProcessor generalSerializationProcessor);
    }

    private class CollectionCompositeProcessor implements ICspDataCompositeProcessorSerializationDelegate
    {
        @Override
        public void serialize(@Nullable Object value,
            ICspDataGeneralSerializationProcessor generalSerializationProcessor)
        {
            generalSerializationProcessor.serializeComposite((Collection<?>)value, isReference(), getSubProcessor(0));
        }
    }

    private class MapCompositeProcessor implements ICspDataCompositeProcessorSerializationDelegate
    {
        @Override
        public void serialize(@Nullable Object value,
            ICspDataGeneralSerializationProcessor generalSerializationProcessor)
        {
            generalSerializationProcessor.serializeComposite((Map<?, ?>)value, isReference(),
                getSubProcessor(0), getSubProcessor(1));
        }
    }

    private class RandomGenericCompositeProcessor implements ICspDataCompositeProcessorSerializationDelegate
    {
        private final Class<?> clazz;

        private RandomGenericCompositeProcessor(Class<?> clazz)
        {
            this.clazz = clazz;
        }

        @Override
        public void serialize(@Nullable Object value,
            ICspDataGeneralSerializationProcessor generalSerializationProcessor)
        {
            generalSerializationProcessor.serializeComposite(value, isReference(), clazz, getSubProcessors());
        }
    }
}
