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

import io.andreygs.jcsp.base.processing.ICspDataGeneralDeserializationProcessor;
import io.andreygs.jcsp.base.processing.composite.ICspDataCompositeDeserializationProcessor;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;

/**
 * TODO: place description here
 */
final class CspDataCompositeGenericDeserializationProcessor
    extends AbstractCspDataCompositeSubProcessorHolder<ICspDataCompositeDeserializationProcessor>
    implements ICspDataCompositeDeserializationProcessor, ICspDataCompositeDeserializationSubProcessorHolder
{
    private final ICspDataCompositeProcessorDeserializationDelegate compositeProcessorDelegate;

    CspDataCompositeGenericDeserializationProcessor(boolean reference, Class<?> clazz, int typeParametersNumber)
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

    private CspDataCompositeGenericDeserializationProcessor(CspDataCompositeGenericDeserializationProcessor processor, boolean reference)
    {
        super(processor, reference);
        this.compositeProcessorDelegate = processor.compositeProcessorDelegate;
    }

    @Override
    public <T> @Nullable T deserialize(ICspDataGeneralDeserializationProcessor generalDeserializationProcessor)
    {
        return compositeProcessorDelegate.deserialize(generalDeserializationProcessor);
    }

    @Override
    protected ICspDataCompositeDeserializationProcessor createCopyInstanceWithOverriddenReference(boolean reference)
    {
        return new CspDataCompositeGenericDeserializationProcessor(this, reference);
    }

    @Override
    protected ICspDataCompositeDeserializationProcessor getThisAsProcessor()
    {
        return this;
    }

    private interface ICspDataCompositeProcessorDeserializationDelegate
    {
        <T> @Nullable T deserialize(ICspDataGeneralDeserializationProcessor generalDeserializationProcessor);
    }

    private class CollectionCompositeProcessor implements ICspDataCompositeProcessorDeserializationDelegate
    {
        @Override
        public <T> @Nullable T deserialize(ICspDataGeneralDeserializationProcessor generalDeserializationProcessor)
        {
            return null;
        }
    }

    private class MapCompositeProcessor implements ICspDataCompositeProcessorDeserializationDelegate
    {
        @Override
        public <T> @Nullable T deserialize(ICspDataGeneralDeserializationProcessor generalDeserializationProcessor)
        {
            return null;
        }
    }

    private class RandomGenericCompositeProcessor implements ICspDataCompositeProcessorDeserializationDelegate
    {
        private final Class<?> clazz;

        private RandomGenericCompositeProcessor(Class<?> clazz)
        {
            this.clazz = clazz;
        }

        @Override
        public <T> @Nullable T deserialize(ICspDataGeneralDeserializationProcessor generalDeserializationProcessor)
        {
            return null;
        }
    }
}
