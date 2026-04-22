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

import io.andreygs.jcsp.base.processing.annotations.CspImplementationClass;
import io.andreygs.jcsp.base.processing.annotations.CspReference;
import io.andreygs.jcsp.base.processing.annotations.CspStringCharset;
import io.andreygs.jcsp.base.processing.internal.ICspDataProcessorRegistry;
import io.andreygs.jcsp.base.types.CspRuntimeException;
import io.andreygs.jcsp.base.types.CspStatus;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedArrayType;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.AnnotatedTypeVariable;
import java.lang.reflect.AnnotatedWildcardType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * TODO: place description here
 */
class CspDataCompositeProcessorFactory<P> implements ICspDataCompositeProcessorFactory<P>
{
    private final ICspDataCompositeSubProcessorFactory<P> subprocessorFactory;

    CspDataCompositeProcessorFactory(ICspDataCompositeSubProcessorFactory<P> subprocessorFactory)
    {
        this.subprocessorFactory = subprocessorFactory;
    }

    @Override
    public P createProcessor(AnnotatedType annotatedType)
    {
        return createProcessorSwitch(annotatedType);
    }

    private P createProcessorSwitch(AnnotatedType annotatedType)
    {
        if (annotatedType instanceof AnnotatedParameterizedType annotatedParameterizedType)
        {
            return createProcessor(annotatedParameterizedType);
        }
        else if (annotatedType instanceof AnnotatedArrayType annotatedArrayType)
        {
            return createProcessor(annotatedArrayType);
        }
        else if (annotatedType instanceof AnnotatedTypeVariable annotatedTypeVariable)
        {
            return createProcessor(annotatedTypeVariable);
        }
        else if (annotatedType instanceof AnnotatedWildcardType annotatedWildcardType)
        {
            throw new IllegalArgumentException("Wildcard type cannot present in serialization!");
        }
        else
        {
            Type type = annotatedType.getType();
            if (!(type instanceof Class<?> declaredClazz))
            {
                throw new IllegalArgumentException("Unsupported type: " + type);
            }
            return createOrdinaryClassProcessor(annotatedType, declaredClazz);
        }
    }

    private P createGenericProcessor(AnnotatedParameterizedType annotatedParameterizedType,
        @Nullable String typeVariableName)
    {
        Annotation[] annotations = annotatedParameterizedType.getAnnotations();
        boolean reference = isReference(annotations);
        ParameterizedType parameterizedType = (ParameterizedType)annotatedParameterizedType.getType();
        Class<?> declaredClazz = (Class<?>)parameterizedType.getRawType();
        Class<?> clazz = selectProcessingClazz(declaredClazz, annotations);
        AnnotatedType[] annotatedTypes = annotatedParameterizedType.getAnnotatedActualTypeArguments();
        if (Collection.class.isAssignableFrom(declaredClazz))
        {
            return createCollectionProcessor(annotatedTypes, reference, clazz);
        }
        Map<String, P> typeVariableNameAndProcessors =
            getTypeVariableNameAndProcessors(annotatedTypes, declaredClazz);
    }

    private P createOrdinaryClassProcessor(AnnotatedType annotatedType, Class<?> declaredClazz)
    {
        Annotation[] annotations = annotatedType.getAnnotations();
        boolean reference = isReference(annotations);
        if (declaredClazz == String.class)
        {
            return subprocessorFactory.createStringProcessor(reference, requireCharset(annotations));
        }
        else
        {
            Class<?> clazz = selectProcessingClazz(declaredClazz, annotations);
            return subprocessorFactory.createOrdinaryClassProcessor(clazz, reference);
        }
    }

    private P createCollectionProcessor(AnnotatedType[] annotatedTypes, boolean reference, Class<?> clazz)
    {
        if (annotatedTypes.length != 1)
        {
            throw new IllegalArgumentException("Only one type allowed for collection!");
        }
        AnnotatedType elementAnnotatedType = annotatedTypes[0];
        Annotation[] elementAnnotations = elementAnnotatedType.getAnnotations();
        boolean elementReference = isReference(elementAnnotations);
        if (elementAnnotatedType.getType() instanceof Class<?> elementDeclaredClazz)
        {
            if (elementDeclaredClazz == String.class)
            {

            }
            else
            {
                Class<?> elementClazz =  selectProcessingClazz(elementDeclaredClazz, elementAnnotations);
                return subprocessorFactory.createCollectionProcessor(reference, elementClazz, elementReference);
            }
        }
        else
        {
            P subProcessor = createProcessorSwitch(elementAnnotatedType);
        }
    }

    private Map<String, P> getTypeVariableNameAndProcessors(AnnotatedType[] annotatedTypes, Class<?> declaredClazz)
    {
        Map<String, P> typeVariableNameAndProcessors = new HashMap<>();
        TypeVariable<? extends Class<?>>[] typeVariables = declaredClazz.getTypeParameters();
        for (int i = 0; i < typeVariables.length; ++i)
        {
            P subProcessor = createProcessorSwitch(annotatedTypes[i]);
            typeVariableNameAndProcessors.put(typeVariables[i].getName(), subProcessor);
        }
        return typeVariableNameAndProcessors;
    }

    private Class<?> selectProcessingClazz(Class<?> declaredClazz, Annotation[] annotations)
    {
        Optional<Annotation> implementationClazzAnnotation = Arrays.stream(annotations).filter(
            annotation -> annotation.annotationType().equals(CspImplementationClass.class)).findFirst();
        if (implementationClazzAnnotation.isEmpty())
        {
            return declaredClazz;
        }
        CspImplementationClass cspImplementationClass = (CspImplementationClass)implementationClazzAnnotation.get();
        ICspDataCompositeSubProcessorFactory.ICspImplementationClassValues cspImplementationClassValues =
            new CspImplementationClassValues(cspImplementationClass);
        return subprocessorFactory.selectClassForProcessing(declaredClazz, cspImplementationClassValues);
    }

    private static boolean isReference(Annotation[] annotations)
    {
        return Arrays.stream(annotations).anyMatch(
            annotation -> annotation.annotationType().equals(CspReference.class));
    }

    private static Charset requireCharset(Annotation[] annotations)
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

    private static class GenericProcessorHolder<P> implements ICspDataProcessorRegistry.IGenericProcessorHolder<P>
    {
        private final P processor;
        private final List<String> typeVariableNames;

        public GenericProcessorHolder(P processor, List<String> typeVariableNames)
        {
            this.processor = processor;
            this.typeVariableNames = typeVariableNames;
        }

        public P getProcessor()
        {
            return processor;
        }

        public List<String> getTypeVariableNames()
        {
            return typeVariableNames;
        }
    }

    private static class CspImplementationClassValues
        implements ICspDataCompositeSubProcessorFactory.ICspImplementationClassValues
    {
        public static final CspImplementationClassValues EMPTY_INSTANCE = new CspImplementationClassValues();

        private final @Nullable Class<?> implementationClazz;
        private final boolean deserializationOnly;

        private CspImplementationClassValues()
        {
            implementationClazz = null;
            deserializationOnly = false;
        }

        CspImplementationClassValues(CspImplementationClass cspImplementationClass)
        {
            this.implementationClazz = cspImplementationClass.value();
            this.deserializationOnly = cspImplementationClass.deserializationOnly();
        }

        public @Nullable Class<?> getImplementationClazz()
        {
            return implementationClazz;
        }

        public boolean isDeserializationOnly()
        {
            return deserializationOnly;
        }
    }
}
