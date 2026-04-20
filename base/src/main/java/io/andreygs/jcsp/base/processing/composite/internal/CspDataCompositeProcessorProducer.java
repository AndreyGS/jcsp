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

import io.andreygs.jcsp.base.processing.annotations.CspReference;
import io.andreygs.jcsp.base.processing.annotations.CspStringCharset;
import io.andreygs.jcsp.base.processing.composite.AbstractAnnotatedTypeExtractor;
import io.andreygs.jcsp.base.processing.composite.ICspDataCompositeSerializationProcessor;
import io.andreygs.jcsp.base.types.CspRuntimeException;
import io.andreygs.jcsp.base.types.CspStatus;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedArrayType;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.AnnotatedTypeVariable;
import java.lang.reflect.AnnotatedWildcardType;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Optional;

/**
 * TODO: place description here
 */
class CspDataCompositeProcessorProducer<P> implements ICspDataCompositeProcessorProducer<P>
{
    private final ICspDataCompositeSubProcessorFactory<P> subprocessorFactory;

    CspDataCompositeProcessorProducer(ICspDataCompositeSubProcessorFactory<P> subprocessorFactory)
    {
        this.subprocessorFactory = subprocessorFactory;
    }

    @Override
    public P produceProcessor(AnnotatedType annotatedType)
    {
        return null;
    }

    private P produceProcessorSwitch(AnnotatedType annotatedType, @Nullable String typeVariableName)
    {
        if (annotatedType instanceof AnnotatedParameterizedType annotatedParameterizedType)
        {
            return produceProcessor(annotatedParameterizedType);
        }
        else if (annotatedType instanceof AnnotatedArrayType annotatedArrayType)
        {
            return produceProcessor(annotatedArrayType);
        }
        else if (annotatedType instanceof AnnotatedTypeVariable annotatedTypeVariable)
        {
            return produceProcessor(annotatedTypeVariable);
        }
        else if (annotatedType instanceof AnnotatedWildcardType annotatedWildcardType)
        {
            throw new IllegalArgumentException("Wildcard type cannot present in serialization!");
        }
        else
        {
            Type type = annotatedType.getType();
            if (!(type instanceof Class<?> clazz))
            {
                throw new IllegalArgumentException("Unsupported type: " + type);
            }
            return produceOrdinaryClassProcessor(annotatedType, clazz, typeVariableName);
        }
    }

    private P produceOrdinaryClassProcessor(AnnotatedType annotatedType, Class<?> clazz,
        @Nullable String typeVariableName)
    {
        Annotation[] annotations = annotatedType.getAnnotations();
        boolean reference = isReference(annotations);
        if (clazz == String.class)
        {
            return subprocessorFactory.createStringProcessor(typeVariableName, reference, requireCharset(annotations));
        }
        
    }

    private boolean isReference(Annotation[] annotations)
    {
        return Arrays.stream(annotations).anyMatch(
            annotation -> annotation.annotationType().equals(CspReference.class));
    }

    private Charset requireCharset(Annotation[] annotations)
    {
        Optional<Annotation> charsetAnnotation = Arrays.stream(annotations).filter(
            annotation -> annotation.annotationType().equals(CspStringCharset.class)).findFirst();
        if (charsetAnnotation.isEmpty())
        {
            throw CspRuntimeException.createCspRuntimeException(CspStatus.ERROR_IN_STRUCTURE_FORMAT,
                MessageFormat.format(
                    Messages.CspStatus_Error_in_structure_format_property__0__for_structure__1__not_set, "charset",
                    "CSP String"));
        }
        CspStringCharset cspStringCharset = (CspStringCharset)charsetAnnotation.get();
        String charset = cspStringCharset.value();
        return Charset.forName(cspStringCharset.value());
    }
}
