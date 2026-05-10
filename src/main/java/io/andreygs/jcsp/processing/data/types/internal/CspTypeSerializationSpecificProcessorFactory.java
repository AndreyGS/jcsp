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

package io.andreygs.jcsp.processing.data.types.internal;

import org.jetbrains.annotations.Nullable;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * TODO: place description here
 */
class CspTypeSerializationSpecificProcessorFactory
    implements ICspSpecificTypeProcessorFactory<ICspTypeSerializationProcessor<?>>
{
    @Override
    public ICspTypeSerializationProcessor<?> createPrimitiveBooleanProcessor()
    {
        return null;
    }

    @Override
    public ICspTypeSerializationProcessor<?> createPrimitiveByteProcessor()
    {
        return null;
    }

    @Override
    public ICspTypeSerializationProcessor<?> createPrimitiveShortProcessor()
    {
        return null;
    }

    @Override
    public ICspTypeSerializationProcessor<?> createPrimitiveIntProcessor()
    {
        return null;
    }

    @Override
    public ICspTypeSerializationProcessor<?> createPrimitiveLongProcessor()
    {
        return null;
    }

    @Override
    public ICspTypeSerializationProcessor<?> createPrimitiveCharProcessor()
    {
        return null;
    }

    @Override
    public ICspTypeSerializationProcessor<?> createPrimitiveFloatProcessor()
    {
        return null;
    }

    @Override
    public ICspTypeSerializationProcessor<?> createPrimitiveDoubleProcessor()
    {
        return null;
    }

    @Override
    public ICspTypeSerializationProcessor<?> createStringProcessor(boolean reference, Charset charset)
    {
        return null;
    }

    @Override
    public ICspTypeSerializationProcessor<?> createOrdinaryClassProcessor(Class<?> clazz, boolean reference,
        @Nullable Class<?> implementationClazz)
    {
        return null;
    }

    @Override
    public ICspTypeSerializationProcessor<?> createStringCollectionProcessor(boolean reference,
        boolean elementReference, Charset elementCharset)
    {
        return null;
    }

    @Override
    public ICspTypeSerializationProcessor<?> createOrdinaryCollectionProcessor(boolean reference,
        Class<?> elementClazz, boolean elementReference, @Nullable Class<?> elementImplementationClazz)
    {
        return null;
    }

    @Override
    public ICspTypeSerializationProcessor<?> createCollectionProcessor(boolean reference,
        ICspTypeSerializationProcessor<?> elementProcessor)
    {
        return null;
    }

    @Override
    public ICspTypeSerializationProcessor<?> createStringKeyMapProcessor(boolean reference, boolean keyReference,
        Charset keyCharset, Class<?> valueClazz, boolean valueReference, @Nullable Class<?> valueImplementationClazz)
    {
        return null;
    }

    @Override
    public ICspTypeSerializationProcessor<?> createStringValueMapProcessor(boolean reference, Class<?> keyClazz,
        boolean keyReference, @Nullable Class<?> keyImplementationClazz, boolean valueReference, Charset valueCharset)
    {
        return null;
    }

    @Override
    public ICspTypeSerializationProcessor<?> createOrdinaryMapProcessor(boolean reference, Class<?> keyClazz,
        boolean keyReference, @Nullable Class<?> keyImplementationClazz, Class<?> valueClazz, boolean valueReference,
        @Nullable Class<?> valueImplementationClazz)
    {
        return null;
    }

    @Override
    public ICspTypeSerializationProcessor<?> createStringStringMapProcessor(boolean reference,
        boolean keyReference, Charset keyCharset, boolean valueReference, Charset valueCharset)
    {
        return null;
    }

    @Override
    public ICspTypeSerializationProcessor<?> createMapProcessor(boolean reference,
        ICspTypeSerializationProcessor<?> keyProcessor, ICspTypeSerializationProcessor<?> mapProcessor)
    {
        return null;
    }

    @Override
    public ICspTypeSerializationProcessor<?> createArbitraryGenericProcessor(Class<?> clazz, boolean reference,
        @Nullable Class<?> implementationClazz,
        Map<String, ICspTypeSerializationProcessor<?>> typeVariableNameAndProcessors)
    {
        return null;
    }

    @Override
    public ICspTypeSerializationProcessor<?> createPrimitiveBooleanArrayProcessor(boolean reference, int fixedSize)
    {
        return null;
    }

    @Override
    public ICspTypeSerializationProcessor<?> createPrimitiveByteArrayProcessor(boolean reference, int fixedSize)
    {
        return null;
    }

    @Override
    public ICspTypeSerializationProcessor<?> createPrimitiveShortArrayProcessor(boolean reference, int fixedSize)
    {
        return null;
    }

    @Override
    public ICspTypeSerializationProcessor<?> createPrimitiveIntArrayProcessor(boolean reference, int fixedSize)
    {
        return null;
    }

    @Override
    public ICspTypeSerializationProcessor<?> createPrimitiveLongArrayProcessor(boolean reference, int fixedSize)
    {
        return null;
    }

    @Override
    public ICspTypeSerializationProcessor<?> createPrimitiveCharArrayProcessor(boolean reference, int fixedSize)
    {
        return null;
    }

    @Override
    public ICspTypeSerializationProcessor<?> createPrimitiveFloatArrayProcessor(boolean reference, int fixedSize)
    {
        return null;
    }

    @Override
    public ICspTypeSerializationProcessor<?> createPrimitiveDoubleArrayProcessor(boolean reference, int fixedSize)
    {
        return null;
    }

    @Override
    public ICspTypeSerializationProcessor<?> createStringArrayProcessor(boolean reference, int fixedSize,
        boolean componentReference, Charset componentCharset)
    {
        return null;
    }

    @Override
    public ICspTypeSerializationProcessor<?> createOrdinaryClassArrayProcessor(boolean reference, int fixedSize,
        Class<?> componentClazz, boolean componentReference, @Nullable Class<?> componentImplementationClazz)
    {
        return null;
    }

    @Override
    public ICspTypeSerializationProcessor<?> createArrayProcessor(boolean reference, int fixedSize,
        ICspTypeSerializationProcessor<?> componentProcessor)
    {
        return null;
    }

    @Override
    public ICspTypeSerializationProcessor<?> createTypeVariableProcessor(boolean reference,
        String typeVariableName)
    {
        return null;
    }
}
