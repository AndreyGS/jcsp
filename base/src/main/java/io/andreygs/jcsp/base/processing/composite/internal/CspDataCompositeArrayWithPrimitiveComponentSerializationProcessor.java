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

/**
 * TODO: place description here
 */
final class CspDataCompositeArrayWithPrimitiveComponentSerializationProcessor
    extends AbstractCspDataCompositeArrayWithPrimitiveComponentProcessor<ICspDataCompositeSerializationProcessor>
    implements ICspDataCompositeSerializationProcessor
{
    private final ICspDataCompositeProcessorSerializationDelegate compositeProcessorDelegate;

    CspDataCompositeArrayWithPrimitiveComponentSerializationProcessor(boolean reference, boolean fixedSize,
        Class<?> arrayClazz)
    {
        super(reference, fixedSize);
        if (arrayClazz == byte[].class)
        {
            compositeProcessorDelegate = new ByteCompositeProcessor();
        }
        else if (arrayClazz == int[].class)
        {
            compositeProcessorDelegate = new IntCompositeProcessor();
        }
        else if (arrayClazz == long[].class)
        {
            compositeProcessorDelegate = new LongCompositeProcessor();
        }
        else if (arrayClazz == double[].class)
        {
            compositeProcessorDelegate = new DoubleCompositeProcessor();
        }
        else if (arrayClazz == boolean[].class)
        {
            compositeProcessorDelegate = new BooleanCompositeProcessor();
        }
        else if (arrayClazz == float[].class)
        {
            compositeProcessorDelegate = new FloatCompositeProcessor();
        }
        else if (arrayClazz == short[].class)
        {
            compositeProcessorDelegate = new ShortCompositeProcessor();
        }
        else if (arrayClazz == char[].class)
        {
            compositeProcessorDelegate = new CharCompositeProcessor();
        }
        else
        {
            throw new IllegalStateException("Array " + arrayClazz + " component is not primitive!");
        }
    }

    private CspDataCompositeArrayWithPrimitiveComponentSerializationProcessor(
        CspDataCompositeArrayWithPrimitiveComponentSerializationProcessor processor, boolean reference)
    {
        super(reference, processor.isFixedSize());
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
        return new CspDataCompositeArrayWithPrimitiveComponentSerializationProcessor(this, reference);
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

    private class BooleanCompositeProcessor implements ICspDataCompositeProcessorSerializationDelegate
    {
        @Override
        public void serialize(@Nullable Object value, ICspDataGeneralSerializationProcessor generalSerializationProcessor)
        {
            generalSerializationProcessor.serialize((boolean[])value, isReference(), isFixedSize());
        }
    }

    private class ByteCompositeProcessor implements ICspDataCompositeProcessorSerializationDelegate
    {
        @Override
        public void serialize(@Nullable Object value, ICspDataGeneralSerializationProcessor generalSerializationProcessor)
        {
            generalSerializationProcessor.serialize((byte[])value, isReference(), isFixedSize());
        }
    }

    private class ShortCompositeProcessor implements ICspDataCompositeProcessorSerializationDelegate
    {
        @Override
        public void serialize(@Nullable Object value, ICspDataGeneralSerializationProcessor generalSerializationProcessor)
        {
            generalSerializationProcessor.serialize((short[])value, isReference(), isFixedSize());
        }
    }

    private class IntCompositeProcessor implements ICspDataCompositeProcessorSerializationDelegate
    {
        @Override
        public void serialize(@Nullable Object value, ICspDataGeneralSerializationProcessor generalSerializationProcessor)
        {
            generalSerializationProcessor.serialize((int[])value, isReference(), isFixedSize());
        }
    }

    private class LongCompositeProcessor implements ICspDataCompositeProcessorSerializationDelegate
    {
        @Override
        public void serialize(@Nullable Object value, ICspDataGeneralSerializationProcessor generalSerializationProcessor)
        {
            generalSerializationProcessor.serialize((long[])value, isReference(), isFixedSize());
        }
    }

    private class CharCompositeProcessor implements ICspDataCompositeProcessorSerializationDelegate
    {
        @Override
        public void serialize(@Nullable Object value, ICspDataGeneralSerializationProcessor generalSerializationProcessor)
        {
            generalSerializationProcessor.serialize((char[])value, isReference(), isFixedSize());
        }
    }

    private class FloatCompositeProcessor implements ICspDataCompositeProcessorSerializationDelegate
    {
        @Override
        public void serialize(@Nullable Object value, ICspDataGeneralSerializationProcessor generalSerializationProcessor)
        {
            generalSerializationProcessor.serialize((float[])value, isReference(), isFixedSize());
        }
    }

    private class DoubleCompositeProcessor implements ICspDataCompositeProcessorSerializationDelegate
    {
        @Override
        public void serialize(@Nullable Object value, ICspDataGeneralSerializationProcessor generalSerializationProcessor)
        {
            generalSerializationProcessor.serialize((double[])value, isReference(), isFixedSize());
        }
    }
}
