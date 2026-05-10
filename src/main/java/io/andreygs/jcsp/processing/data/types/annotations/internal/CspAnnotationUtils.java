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

package io.andreygs.jcsp.processing.data.types.annotations.internal;

import io.andreygs.jcsp.processing.data.types.annotations.CspCreateProcessor;
import io.andreygs.jcsp.processing.data.types.annotations.CspField;
import io.andreygs.jcsp.processing.data.types.annotations.CspFixedSizeArray;
import io.andreygs.jcsp.processing.data.types.annotations.CspImplementationClass;
import io.andreygs.jcsp.processing.data.types.annotations.CspOverrideProcessorClass;
import io.andreygs.jcsp.processing.data.types.annotations.CspReference;
import io.andreygs.jcsp.processing.data.types.annotations.CspString;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.Optional;

/**
 * Utils for getting settings for CSP serialization from CSP annotations.
 * <p>
 * Does not have any annotation value checking logic.
 */
public class CspAnnotationUtils
{
    /**
     * Does clazz is annotated with {@link CspCreateProcessor} annotation.
     *
     * @param clazz Clazz to test.
     * @return true if clazz is annotated with {@link CspCreateProcessor} annotation and false otherwise.
     */
    public static boolean isCspCreateProcessor(Class<?> clazz)
    {
        return clazz.getDeclaredAnnotation(CspCreateProcessor.class) != null;
    }

    /**
     * Resolves serialization sequence number of field that is serialized with CSP.
     *
     * @param field Field possibly annotated with {@link CspField} which sequence number should be resolved.
     * @return sequence number of CSP serialization belonging field that is set by {@link CspField} annotation or
     * {@link Optional#empty()} if field should not be serialized with CSP.
     */
    public static Optional<Integer> resolveCspFieldSequence(Field field)
    {
        Optional<CspField> cspFieldSequence = Optional.ofNullable(field.getDeclaredAnnotation(CspField.class));
        return cspFieldSequence.map(CspField::value);
    }

    /**
     * Resolves fixed array size.
     * <p>
     * Make sense to use it only for array types - for all others is meaningless.
     *
     * @param annotatedType Array type possibly annotated with {@link CspFixedSizeArray}.
     * @return array fixed size or {@link Optional#empty()} if type does not have {@link CspFixedSizeArray} annotation.
     */
    public static Optional<Integer> resolveCspFixedArraySize(AnnotatedType annotatedType)
    {
        Optional<CspFixedSizeArray> cspFixedSizeArray =
            Optional.ofNullable(annotatedType.getDeclaredAnnotation(CspFixedSizeArray.class));
        return cspFixedSizeArray.map(CspFixedSizeArray::value);
    }

    
    public static Optional<Class<?>> resolveCspImplementationClass(AnnotatedType annotatedType)
    {
        Optional<CspImplementationClass> cspOverrideProcessor =
            Optional.ofNullable(annotatedType.getDeclaredAnnotation(CspImplementationClass.class));
        return cspOverrideProcessor.map(CspImplementationClass::value);
    }

    public static Optional<Class<?>> resolveCspOverrideProcessorClass(AnnotatedType annotatedType)
    {
        Optional<CspOverrideProcessorClass> cspOverrideProcessorClass =
            Optional.ofNullable(annotatedType.getDeclaredAnnotation(CspOverrideProcessorClass.class));
        return cspOverrideProcessorClass.map(CspOverrideProcessorClass::value);
    }

    public static boolean isCspReference(AnnotatedType annotatedType)
    {
        return annotatedType.getDeclaredAnnotation(CspReference.class) != null;
    }

    public static Optional<Charset> resolveCspStringCharset(AnnotatedType annotatedType)
    {
        Optional<CspString> cspStringOpt = Optional.ofNullable(annotatedType.getDeclaredAnnotation(CspString.class));
        return cspStringOpt.map(cspString -> Charset.forName(cspString.value()));
    }
}
