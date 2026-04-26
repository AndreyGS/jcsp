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

package io.andreygs.jcsp.base.processing.annotations.internal;

import io.andreygs.jcsp.base.processing.annotations.CspField;
import io.andreygs.jcsp.base.processing.annotations.CspFixedSizeArray;
import io.andreygs.jcsp.base.processing.annotations.CspImplementationClass;
import io.andreygs.jcsp.base.processing.annotations.CspOverrideProcessorClass;
import io.andreygs.jcsp.base.processing.annotations.CspReference;
import io.andreygs.jcsp.base.processing.annotations.CspStringCharset;
import io.andreygs.jcsp.base.processing.composite.internal.AnnotatedTypeUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Optional;

/**
 * TODO: place description here
 */
public class CspAnnotationUtils
{
    public static final int NOT_CSP_FIELD = -1;
    public static final int CSP_ARRAY_NOT_FIXED_SIZED = -1;

    public static int resolveCspFieldOrder(AnnotatedType annotatedType)
    {
        Optional<CspField> cspFieldOrder = findAnnotation(annotatedType, CspField.class);
        return cspFieldOrder.map(CspField::value).orElse(NOT_CSP_FIELD);
    }

    public static int resolveCspFixedArraySize(AnnotatedType annotatedType)
    {
        Optional<CspFixedSizeArray> cspFixedSizeArray = findAnnotation(annotatedType, CspFixedSizeArray.class);
        return cspFixedSizeArray.map(CspFixedSizeArray::value).orElse(CSP_ARRAY_NOT_FIXED_SIZED);
    }

    public static Optional<Class<?>> resolveCspImplementationClass(AnnotatedType annotatedType)
    {
        Optional<CspImplementationClass> cspOverrideProcessor = findAnnotation(annotatedType,
            CspImplementationClass.class);
        return cspOverrideProcessor.map(CspImplementationClass::value);
    }

    public static Optional<Class<?>> resolveCspOverrideProcessorClass(AnnotatedType annotatedType)
    {
        Optional<CspOverrideProcessorClass> cspOverrideProcessorClass = findAnnotation(annotatedType,
            CspOverrideProcessorClass.class);
        return cspOverrideProcessorClass.map(CspOverrideProcessorClass::value);
    }

    public static boolean isCspReference(AnnotatedType annotatedType)
    {
        Optional<CspReference> cspReference = findAnnotation(annotatedType, CspReference.class);
        return cspReference.isPresent();
    }

    public static Optional<Charset> resolveCspStringCharset(AnnotatedType annotatedType)
    {
        Optional<CspStringCharset> cspStringCharsetOpt = findAnnotation(annotatedType, CspStringCharset.class);
        return cspStringCharsetOpt.map(cspStringCharset -> Charset.forName(cspStringCharset.value()));
    }

    private static <T> Optional<T> findAnnotation(AnnotatedType annotatedType, Class<T> annotationClazz)
    {
        Annotation[] annotations =  annotatedType.getAnnotations();
        Optional<Annotation> annotationOpt = Arrays.stream(annotations)
                                                   .filter(annotation -> annotation.annotationType().equals(annotationClazz))
                                                   .findFirst();
        if (annotationOpt.isEmpty())
        {
            return Optional.empty();
        }
        @SuppressWarnings("unchecked")
        T annotation = (T)annotationOpt.get();
        return Optional.of(annotation);
    }
}
