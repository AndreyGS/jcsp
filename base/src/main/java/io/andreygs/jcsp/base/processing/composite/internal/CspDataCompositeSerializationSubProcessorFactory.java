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

import io.andreygs.jcsp.base.processing.ICspDataSerializationProcessor;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.Charset;
import java.util.Collection;

/**
 * TODO: place description here
 */
final class CspDataCompositeSerializationSubProcessorFactory
    implements ICspDataCompositeSubProcessorFactory<ICspDataSerializationProcessor<?>>
{
    @Override
    public ICspDataCompositeSerializationSubProcessorHolder createCollectionProcessor(boolean reference, Class<?> clazz)
    {
        return new CspDataCompositeCollectionSerializationProcessor(reference);
    }

    @Override
    public ICspDataSerializationProcessor<Collection<?>> createCollectionProcessor(boolean reference,
        Class<?> elementClazz, boolean elementReference)
    {
        return new CspDataCompositeCollectionSerializationProcessor(reference, elementClazz, elementReference);
    }

    @Override
    public ICspDataSerializationProcessor<Collection<String>>
        createStringCollectionProcessor(boolean reference, boolean elementReference, Charset charset)
    {
        return new CspDataCompositeStringCollectionSerializationProcessor(reference, elementReference, charset);
    }

    @Override
    public ICspDataCompositeSerializationSubProcessorHolder createMapProcessor(boolean reference)
    {
        return new CspDataCompositeMapSerializationProcessor(reference);
    }

    @Override
    public ICspDataCompositeSerializationSubProcessorHolder createArrayProcessor(boolean reference, boolean fixedSize)
    {
        return new CspDataCompositeArraySerializationProcessor(reference, fixedSize);
    }

    @Override
    public ICspDataCompositeSerializationProcessor createArrayWithPrimitiveComponentProcessor(boolean reference,
        boolean fixedSize, Class<?> arrayClazz)
    {
        return new CspDataCompositeArrayWithPrimitiveComponentSerializationProcessor(reference, fixedSize, arrayClazz);
    }

    @Override
    public ICspDataCompositeSerializationSubProcessorHolder createGenericProcessor(boolean reference, Class<?> clazz,
        int typeParametersNumber)
    {
        return new CspDataCompositeGenericSerializationProcessor(reference, clazz, typeParametersNumber);
    }

    @Override
    public ICspDataSerializationProcessor<Object> createOrdinaryClassProcessor(boolean reference,
        Class<?> declaredClazz, ICspImplementationClassValues cspImplementationClassValues)
    {
        return new CspDataCompositeOrdinaryClassSerializationProcessor(reference, declaredClazz);
    }

    @Override
    public ICspDataSerializationProcessor<String> createStringProcessor(boolean reference, Charset charset)
    {
        return new CspDataCompositeStringSerializationProcessor(reference, charset);
    }

    @Override
    public Class<?> selectClassForProcessing(Class<?> declaredClazz,
        ICspImplementationClassValues cspImplementationClassValues)
    {
        return cspImplementationClassValues.isDeserializationOnly()
                   || cspImplementationClassValues.getImplementationClazz() == null
               ? declaredClazz
               : cspImplementationClassValues.getImplementationClazz();
    }
}
