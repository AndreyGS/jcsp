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

import org.jetbrains.annotations.Nullable;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * TODO: place description here
 */
interface ICspDataCompositeSubProcessorFactory<P>
{
    P createPrimitiveBooleanProcessor();

    P createPrimitiveByteProcessor();

    P createPrimitiveShortProcessor();

    P createPrimitiveIntProcessor();

    P createPrimitiveLongProcessor();

    P createPrimitiveCharProcessor();

    P createPrimitiveFloatProcessor();

    P createPrimitiveDoubleProcessor();

    P createStringProcessor(boolean reference, Charset charset);

    P createOrdinaryClassProcessor(Class<?> clazz, boolean reference, @Nullable Class<?> implementationClazz);

    P createStringCollectionProcessor(boolean reference, boolean elementReference, Charset elementCharset);

    P createOrdinaryCollectionProcessor(boolean reference, Class<?> elementClazz, boolean elementReference,
        @Nullable Class<?> elementImplementationClazz);

    P createCollectionProcessor(boolean reference, P elementProcessor);

    P createStringKeyMapProcessor(boolean reference, boolean keyReference, Charset keyCharset,
        Class<?> valueClazz, boolean valueReference, @Nullable Class<?> valueImplementationClazz);

    P createStringValueMapProcessor(boolean reference, Class<?> keyClazz, boolean keyReference,
        @Nullable Class<?> keyImplementationClazz, boolean valueReference, Charset valueCharset);

    P createOrdinaryMapProcessor(boolean reference, Class<?> keyClazz, boolean keyReference,
        @Nullable Class<?> keyImplementationClazz, Class<?> valueClazz, boolean valueReference,
        @Nullable Class<?> valueImplementationClazz);

    P createStringStringMapProcessor(boolean reference, boolean keyReference, Charset keyCharset,
        boolean valueReference, Charset valueCharset);

    P createMapProcessor(boolean reference, P keyProcessor, P mapProcessor);

    P createArbitraryGenericProcessor(Class<?> clazz, boolean reference, @Nullable Class<?> implementationClazz,
        Map<String, P> typeVariableNameAndProcessors);

    P createPrimitiveBooleanArrayProcessor(boolean reference, int fixedSize);

    P createPrimitiveByteArrayProcessor(boolean reference, int fixedSize);

    P createPrimitiveShortArrayProcessor(boolean reference, int fixedSize);

    P createPrimitiveIntArrayProcessor(boolean reference, int fixedSize);

    P createPrimitiveLongArrayProcessor(boolean reference, int fixedSize);

    P createPrimitiveCharArrayProcessor(boolean reference, int fixedSize);

    P createPrimitiveFloatArrayProcessor(boolean reference, int fixedSize);

    P createPrimitiveDoubleArrayProcessor(boolean reference, int fixedSize);

    P createStringArrayProcessor(boolean reference, int fixedSize, boolean componentReference,
        Charset componentCharset);

    P createOrdinaryClassArrayProcessor(boolean reference, int fixedSize, Class<?> componentClazz,
        boolean componentReference, @Nullable Class<?> componentImplementationClazz);

    P createArrayProcessor(boolean reference, int fixedSize, P componentProcessor);

    P createTypeVariableProcessor(boolean reference, String typeVariableName);

    P createArrayWithPrimitiveComponentProcessor(boolean reference, boolean fixedSize, Class<?> arrayClazz);

    H createGenericProcessor(boolean reference, Class<?> clazz, int typeParametersNumber);
}
