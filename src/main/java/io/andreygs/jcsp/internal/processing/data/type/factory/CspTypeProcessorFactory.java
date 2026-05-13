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

package io.andreygs.jcsp.internal.processing.data.type.factory;

import io.andreygs.jcsp.internal.model.annotation.utils.CspAnnotationUtils;
import io.andreygs.jcsp.api.model.exception.CspRuntimeException;
import io.andreygs.jcsp.api.model.protocol.CspStatus;
import org.jetbrains.annotations.Nullable;

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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * TODO: place description here
 */
class CspTypeProcessorFactory<P> implements ICspTypeProcessorFactory<P>
{
    private final ICspSpecificTypeProcessorFactory<P> proxyProcessorFactory;

    CspTypeProcessorFactory(ICspSpecificTypeProcessorFactory<P> proxyProcessorFactory)
    {
        this.proxyProcessorFactory = proxyProcessorFactory;
    }

    @Override
    public P createTypeProcessor(AnnotatedType annotatedType)
    {
        return createProcessorSwitch(annotatedType, true);
    }

    private P createProcessorSwitch(AnnotatedType annotatedType, boolean overrideWithUpperBound)
    {
        if (annotatedType instanceof AnnotatedParameterizedType annotatedParameterizedType)
        {
            return createGenericProcessorSwitch(annotatedParameterizedType, overrideWithUpperBound);
        }
        else if (annotatedType instanceof AnnotatedArrayType annotatedArrayType)
        {
            return createArrayProcessorSwitch(annotatedArrayType);
        }
        else if (annotatedType instanceof AnnotatedTypeVariable annotatedTypeVariable)
        {
            return createTypeVariableProcessor(annotatedTypeVariable);
        }
        else if (annotatedType instanceof AnnotatedWildcardType annotatedWildcardType)
        {
            return createWildcardProcessorSwitch(annotatedWildcardType);
        }
        else
        {
            Type type = annotatedType.getType();
            if (!(type instanceof Class<?> declaredClazz))
            {
                throw CspRuntimeException.createCspRuntimeException(CspStatus.ERROR_IN_STRUCT_FORMAT,
                    Messages.CspStatus_Error_in_struct_format_Unknown_type_category);
            }
            return createSimpleProcessorSwitch(annotatedType, declaredClazz, overrideWithUpperBound);
        }
    }

    private P createSimpleProcessorSwitch(AnnotatedType annotatedType, Class<?> declaredClazz,
        boolean overrideWithUpperBound)
    {
        if (declaredClazz.isPrimitive())
        {
            return createPrimitiveProcessor(declaredClazz);
        }
        else if (declaredClazz == String.class)
        {
            return createStringProcessor(annotatedType);
        }
        else
        {
            return createOrdinaryClassProcessor(annotatedType, declaredClazz, overrideWithUpperBound);
        }
    }

    private P createGenericProcessorSwitch(AnnotatedParameterizedType annotatedParameterizedType,
        boolean overrideWithUpperBound)
    {
        ParameterizedType parameterizedType = (ParameterizedType)annotatedParameterizedType.getType();
        Class<?> declaredClazz = (Class<?>)parameterizedType.getRawType();
        if (Collection.class.isAssignableFrom(declaredClazz))
        {
            return createCollectionProcessorSwitch(annotatedParameterizedType, declaredClazz, overrideWithUpperBound);
        }
        else if (Map.class.isAssignableFrom(declaredClazz))
        {
            return createMapProcessorSwitch(annotatedParameterizedType, declaredClazz, overrideWithUpperBound);
        }
        else
        {
            return createArbitraryGenericProcessor(annotatedParameterizedType, declaredClazz, overrideWithUpperBound);
        }
    }

    private P createArrayProcessorSwitch(AnnotatedArrayType annotatedArrayType)
    {
        AnnotatedType componentAnnotatedType = annotatedArrayType.getAnnotatedGenericComponentType();
        Type componentType = componentAnnotatedType.getType();
        if (componentType instanceof Class<?> componentClazz)
        {
            if (componentClazz.isPrimitive())
            {
                return createPrimitiveArrayProcessor(annotatedArrayType, componentClazz);
            }
            else if (componentClazz == String.class)
            {
                return createStringArrayProcessor(annotatedArrayType, componentAnnotatedType);
            }
            else
            {
                return createOrdinaryClassArrayProcessor(annotatedArrayType, componentAnnotatedType, componentClazz);
            }
        }
        else
        {
            return createArrayProcessor(annotatedArrayType, componentAnnotatedType);
        }
    }

    private P createWildcardProcessorSwitch(AnnotatedWildcardType annotatedWildcardType)
    {
        AnnotatedType[] lowerBoundAnnotatedTypes =  annotatedWildcardType.getAnnotatedLowerBounds();
        AnnotatedType[] upperBoundAnnotatedTypes =  annotatedWildcardType.getAnnotatedUpperBounds();
        if (lowerBoundAnnotatedTypes.length == 0 && upperBoundAnnotatedTypes.length == 0)
        {
            throw CspRuntimeException.createCspRuntimeException(CspStatus.ERROR_IN_STRUCT_FORMAT,
                Messages.CspStatus_Error_in_struct_format_Unbound_wildcard_type_cannot_be_processed);
        }
        else if (lowerBoundAnnotatedTypes.length == 1)
        {
            return createProcessorSwitch(lowerBoundAnnotatedTypes[0], false);
        }
        else
        {
            return createProcessorSwitch(upperBoundAnnotatedTypes[0], true);
        }
    }

    private P createCollectionProcessorSwitch(AnnotatedParameterizedType annotatedParameterizedType,
        Class<?> declaredClazz, boolean overrideWithUpperBound)
    {
        Class<?> processorClazz = selectProcessorClassForCollectionOrMap(annotatedParameterizedType, declaredClazz,
            Collection.class, overrideWithUpperBound);
        if (processorClazz != Collection.class)
        {
            return createArbitraryGenericProcessor(annotatedParameterizedType, declaredClazz, overrideWithUpperBound);
        }
        AnnotatedType[] annotatedTypes = annotatedParameterizedType.getAnnotatedActualTypeArguments();
        if (annotatedTypes.length != 1)
        {
            throw CspRuntimeException.createCspRuntimeException(CspStatus.ERROR_IN_STRUCT_FORMAT,
                Messages.CspStatus_Error_in_struct_format_Collection_has_invalid_type_arguments_number);
        }
        AnnotatedType elementAnnotatedType = annotatedTypes[0];
        if (!(elementAnnotatedType.getType() instanceof Class<?> elementDeclaredClazz))
        {
            return createCollectionProcessor(annotatedParameterizedType, elementAnnotatedType);
        }
        if (elementDeclaredClazz == String.class)
        {
            return createStringCollectionProcessor(annotatedParameterizedType, elementAnnotatedType);
        }
        else
        {
            return createOrdinaryCollectionProcessor(annotatedParameterizedType, elementAnnotatedType,
                elementDeclaredClazz, overrideWithUpperBound);
        }
    }

    private P createMapProcessorSwitch(AnnotatedParameterizedType annotatedParameterizedType,
        Class<?> declaredClazz, boolean overrideWithUpperBound)
    {
        Class<?> processorClazz = selectProcessorClassForCollectionOrMap(annotatedParameterizedType, declaredClazz,
            Map.class, overrideWithUpperBound);
        if (processorClazz != Map.class)
        {
            return createArbitraryGenericProcessor(annotatedParameterizedType, declaredClazz, overrideWithUpperBound);
        }
        AnnotatedType[] annotatedTypes = annotatedParameterizedType.getAnnotatedActualTypeArguments();
        if (annotatedTypes.length != 2)
        {
            throw CspRuntimeException.createCspRuntimeException(CspStatus.ERROR_IN_STRUCT_FORMAT,
                Messages.CspStatus_Error_in_struct_format_Map_has_invalid_type_arguments_number);
        }
        AnnotatedType keyAnnotatedType = annotatedTypes[0];
        AnnotatedType valueAnnotatedType = annotatedTypes[1];
        if (!(keyAnnotatedType.getType() instanceof Class<?> keyDeclaredClazz
            && valueAnnotatedType.getType() instanceof Class<?> valueDeclaredClazz))
        {
            return createMapProcessor(annotatedParameterizedType, keyAnnotatedType, valueAnnotatedType);
        }
        if (keyDeclaredClazz == String.class && valueDeclaredClazz == String.class)
        {
            return createStringStringMapProcessor(annotatedParameterizedType, keyAnnotatedType, valueAnnotatedType);
        }
        else if (keyDeclaredClazz == String.class)
        {
            return createStringKeyMapProcessor(annotatedParameterizedType, keyAnnotatedType, valueAnnotatedType,
                valueDeclaredClazz);
        }
        else if (valueDeclaredClazz == String.class)
        {
            return createStringValueMapProcessor(annotatedParameterizedType, keyAnnotatedType, keyDeclaredClazz,
                valueAnnotatedType);
        }
        else
        {
            return createOrdinaryMapProcessor(annotatedParameterizedType, keyAnnotatedType, keyDeclaredClazz,
                valueAnnotatedType, valueDeclaredClazz);
        }
    }

    private P createPrimitiveProcessor(Class<?> declaredClazz)
    {
        if (declaredClazz == boolean.class)
        {
            return proxyProcessorFactory.createPrimitiveBooleanProcessor();
        }
        else if (declaredClazz == byte.class)
        {
            return proxyProcessorFactory.createPrimitiveByteProcessor();
        }
        else if (declaredClazz == short.class)
        {
            return proxyProcessorFactory.createPrimitiveShortProcessor();
        }
        else if (declaredClazz == int.class)
        {
            return proxyProcessorFactory.createPrimitiveIntProcessor();
        }
        else if (declaredClazz == long.class)
        {
            return proxyProcessorFactory.createPrimitiveLongProcessor();
        }
        else if (declaredClazz == char.class)
        {
            return proxyProcessorFactory.createPrimitiveCharProcessor();
        }
        else if (declaredClazz == float.class)
        {
            return proxyProcessorFactory.createPrimitiveFloatProcessor();
        }
        else if (declaredClazz == double.class)
        {
            return proxyProcessorFactory.createPrimitiveDoubleProcessor();
        }
        else
        {
            throw CspRuntimeException.createCspRuntimeException(CspStatus.ERROR_IN_STRUCT_FORMAT,
                MessageFormat.format(Messages.CspStatus_Error_in_struct_format_Primitive__0__is_not_supported,
                    declaredClazz));
        }
    }

    private P createStringProcessor(AnnotatedType annotatedType)
    {
        boolean reference = CspAnnotationUtils.isCspReference(annotatedType);
        Charset charset = requireStringCharset(annotatedType);
        return proxyProcessorFactory.createStringProcessor(reference, charset);
    }

    private P createOrdinaryClassProcessor(AnnotatedType annotatedType, Class<?> declaredClazz,
        boolean overrideWithUpperBound)
    {
        Class<?> processorClazz = selectProcessorClass(annotatedType, declaredClazz, overrideWithUpperBound);
        boolean reference = CspAnnotationUtils.isCspReference(annotatedType);
        @Nullable Class<?> implementationOverrideClazz = selectImplementationOverrideClass(annotatedType,
            declaredClazz);
        return proxyProcessorFactory.createOrdinaryClassProcessor(processorClazz, reference,
            implementationOverrideClazz);
    }

    private P createStringCollectionProcessor(AnnotatedType annotatedType, AnnotatedType elementAnnotatedType)
    {
        boolean reference = CspAnnotationUtils.isCspReference(annotatedType);
        boolean elementReference = CspAnnotationUtils.isCspReference(elementAnnotatedType);
        Charset elementCharset = requireStringCharset(elementAnnotatedType);
        return proxyProcessorFactory.createStringCollectionProcessor(reference, elementReference, elementCharset);
    }

    private P createOrdinaryCollectionProcessor(AnnotatedType annotatedType, AnnotatedType elementAnnotatedType,
        Class<?> elementDeclaredClazz, boolean overrideWithUpperBound)
    {
        boolean reference = CspAnnotationUtils.isCspReference(annotatedType);
        Class<?> elementProcessorClazz = selectProcessorClass(elementAnnotatedType, elementDeclaredClazz,
            overrideWithUpperBound);
        boolean elementReference = CspAnnotationUtils.isCspReference(elementAnnotatedType);
        @Nullable Class<?> elementImplementationOverrideClazz = selectImplementationOverrideClass(elementAnnotatedType,
            elementDeclaredClazz);
        return proxyProcessorFactory.createOrdinaryCollectionProcessor(reference, elementProcessorClazz,
            elementReference, elementImplementationOverrideClazz);
    }

    private P createCollectionProcessor(AnnotatedType annotatedType, AnnotatedType elementAnnotatedType)
    {
        boolean reference = CspAnnotationUtils.isCspReference(annotatedType);
        P elementProcessor = createProcessorSwitch(elementAnnotatedType, true);
        return proxyProcessorFactory.createCollectionProcessor(reference, elementProcessor);
    }

    private P createStringStringMapProcessor(AnnotatedType annotatedType, AnnotatedType keyAnnotatedType,
        AnnotatedType valueAnnotatedType)
    {
        boolean reference = CspAnnotationUtils.isCspReference(annotatedType);
        boolean keyReference = CspAnnotationUtils.isCspReference(keyAnnotatedType);
        Charset keyCharset = requireStringCharset(keyAnnotatedType);
        boolean valueReference = CspAnnotationUtils.isCspReference(valueAnnotatedType);
        Charset valueCharset = requireStringCharset(valueAnnotatedType);
        return proxyProcessorFactory.createStringStringMapProcessor(reference, keyReference, keyCharset, valueReference
            , valueCharset);
    }

    private P createStringKeyMapProcessor(AnnotatedType annotatedType, AnnotatedType keyAnnotatedType,
        AnnotatedType valueAnnotatedType, Class<?> valueDeclaredClazz)
    {
        boolean reference = CspAnnotationUtils.isCspReference(annotatedType);
        boolean keyReference = CspAnnotationUtils.isCspReference(keyAnnotatedType);
        Charset keyCharset = requireStringCharset(keyAnnotatedType);
        Class<?> valueProcessorClazz = selectProcessorClass(valueAnnotatedType, valueDeclaredClazz, true);
        boolean valueReference = CspAnnotationUtils.isCspReference(valueAnnotatedType);
        @Nullable Class<?> valueImplementationOverrideClazz = selectImplementationOverrideClass(valueAnnotatedType,
            valueDeclaredClazz);
        return proxyProcessorFactory.createStringKeyMapProcessor(reference, keyReference, keyCharset,
            valueProcessorClazz, valueReference, valueImplementationOverrideClazz);
    }

    private P createStringValueMapProcessor(AnnotatedType annotatedType, AnnotatedType keyAnnotatedType,
        Class<?> keyDeclaredClazz, AnnotatedType valueAnnotatedType)
    {
        boolean reference = CspAnnotationUtils.isCspReference(annotatedType);
        Class<?> keyProcessorClazz = selectProcessorClass(keyAnnotatedType, keyDeclaredClazz, true);
        boolean keyReference = CspAnnotationUtils.isCspReference(keyAnnotatedType);
        @Nullable Class<?> keyImplementationOverrideClazz = selectImplementationOverrideClass(keyAnnotatedType,
            keyDeclaredClazz);
        boolean valueReference = CspAnnotationUtils.isCspReference(valueAnnotatedType);
        Charset valueCharset = requireStringCharset(valueAnnotatedType);
        return proxyProcessorFactory.createStringValueMapProcessor(reference, keyProcessorClazz, keyReference,
            keyImplementationOverrideClazz, valueReference, valueCharset);
    }

    private P createOrdinaryMapProcessor(AnnotatedType annotatedType, AnnotatedType keyAnnotatedType,
        Class<?> keyDeclaredClazz, AnnotatedType valueAnnotatedType, Class<?> valueDeclaredClazz)
    {
        boolean reference = CspAnnotationUtils.isCspReference(annotatedType);
        Class<?> keyProcessorClazz = selectProcessorClass(keyAnnotatedType, keyDeclaredClazz, true);
        boolean keyReference = CspAnnotationUtils.isCspReference(keyAnnotatedType);
        @Nullable Class<?> keyImplementationOverrideClazz = selectImplementationOverrideClass(keyAnnotatedType,
            keyDeclaredClazz);
        Class<?> valueProcessorClazz = selectProcessorClass(valueAnnotatedType, valueDeclaredClazz, true);
        boolean valueReference = CspAnnotationUtils.isCspReference(valueAnnotatedType);
        @Nullable Class<?> valueImplementationOverrideClazz = selectImplementationOverrideClass(valueAnnotatedType,
            valueDeclaredClazz);
        return proxyProcessorFactory.createOrdinaryMapProcessor(reference, keyProcessorClazz, keyReference,
            keyImplementationOverrideClazz, valueProcessorClazz, valueReference, valueImplementationOverrideClazz);
    }

    private P createMapProcessor(AnnotatedType annotatedType, AnnotatedType keyAnnotatedType,
        AnnotatedType valueAnnotatedType)
    {
        boolean reference = CspAnnotationUtils.isCspReference(annotatedType);
        P keyProcessor = createProcessorSwitch(keyAnnotatedType, true);
        P valueProcessor = createProcessorSwitch(valueAnnotatedType, true);
        return proxyProcessorFactory.createMapProcessor(reference, keyProcessor, valueProcessor);
    }

    private P createArbitraryGenericProcessor(AnnotatedParameterizedType annotatedParameterizedType,
        Class<?> declaredClazz, boolean overrideWithUpperBound)
    {
        Class<?> processorClazz = selectProcessorClass(annotatedParameterizedType, declaredClazz,
            overrideWithUpperBound);
        boolean reference = CspAnnotationUtils.isCspReference(annotatedParameterizedType);
        @Nullable Class<?> implementationOverrideClazz = selectImplementationOverrideClass(annotatedParameterizedType,
            declaredClazz);
        Map<String, P> typeVariableNameAndProcessors =
            getTypeVariableNameAndProcessors(annotatedParameterizedType, declaredClazz);
        return proxyProcessorFactory.createArbitraryGenericProcessor(processorClazz, reference,
            implementationOverrideClazz, typeVariableNameAndProcessors);
    }

    private P createPrimitiveArrayProcessor(AnnotatedArrayType annotatedArrayType, Class<?> declaredClazz)
    {
        boolean reference = CspAnnotationUtils.isCspReference(annotatedArrayType);
        Optional<Integer> fixedSize = CspAnnotationUtils.resolveCspFixedArraySize(annotatedArrayType);
        if (declaredClazz == boolean[].class)
        {
            return proxyProcessorFactory.createPrimitiveBooleanArrayProcessor(reference, fixedSize.get());
        }
        else if (declaredClazz == byte[].class)
        {
            return proxyProcessorFactory.createPrimitiveByteArrayProcessor(reference, fixedSize.get());
        }
        else if (declaredClazz == short[].class)
        {
            return proxyProcessorFactory.createPrimitiveShortArrayProcessor(reference, fixedSize.get());
        }
        else if (declaredClazz == int[].class)
        {
            return proxyProcessorFactory.createPrimitiveIntArrayProcessor(reference, fixedSize.get());
        }
        else if (declaredClazz == long[].class)
        {
            return proxyProcessorFactory.createPrimitiveLongArrayProcessor(reference, fixedSize.get());
        }
        else if (declaredClazz == char[].class)
        {
            return proxyProcessorFactory.createPrimitiveCharArrayProcessor(reference, fixedSize.get());
        }
        else if (declaredClazz == float[].class)
        {
            return proxyProcessorFactory.createPrimitiveFloatArrayProcessor(reference, fixedSize.get());
        }
        else if (declaredClazz == double[].class)
        {
            return proxyProcessorFactory.createPrimitiveDoubleArrayProcessor(reference, fixedSize.get());
        }
        else
        {
            throw CspRuntimeException.createCspRuntimeException(CspStatus.ERROR_IN_STRUCT_FORMAT,
                MessageFormat.format(Messages.CspStatus_Error_in_struct_format_Primitive__0__is_not_supported,
                    declaredClazz));
        }
    }

    private P createStringArrayProcessor(AnnotatedArrayType annotatedArrayType, AnnotatedType componentAnnotatedType)
    {
        boolean reference = CspAnnotationUtils.isCspReference(annotatedArrayType);
        int fixedSize = CspAnnotationUtils.resolveCspFixedArraySize(annotatedArrayType).get();
        boolean componentReference = CspAnnotationUtils.isCspReference(componentAnnotatedType);
        Charset componentCharset = requireStringCharset(componentAnnotatedType);
        return proxyProcessorFactory.createStringArrayProcessor(reference, fixedSize, componentReference,
            componentCharset);
    }

    private P createOrdinaryClassArrayProcessor(AnnotatedArrayType annotatedArrayType,
        AnnotatedType componentAnnotatedType, Class<?> componentDeclaredClazz)
    {
        boolean reference = CspAnnotationUtils.isCspReference(annotatedArrayType);
        int fixedSize = CspAnnotationUtils.resolveCspFixedArraySize(annotatedArrayType).get();
        Class<?> componentProcessorClazz = selectProcessorClass(componentAnnotatedType, componentDeclaredClazz, true);
        boolean componentReference = CspAnnotationUtils.isCspReference(componentAnnotatedType);
        @Nullable Class<?> componentImplementationOverrideClazz = selectImplementationOverrideClass(
            componentAnnotatedType, componentDeclaredClazz);
        return proxyProcessorFactory.createOrdinaryClassArrayProcessor(reference, fixedSize, componentProcessorClazz,
            componentReference, componentImplementationOverrideClazz);
    }

    private P createArrayProcessor(AnnotatedArrayType annotatedArrayType,
        AnnotatedType componentAnnotatedType)
    {
        boolean reference = CspAnnotationUtils.isCspReference(annotatedArrayType);
        int fixedSize = CspAnnotationUtils.resolveCspFixedArraySize(annotatedArrayType).get();
        P componentProcessor = createProcessorSwitch(componentAnnotatedType, true);
        return proxyProcessorFactory.createArrayProcessor(reference, fixedSize, componentProcessor);
    }

    private P createTypeVariableProcessor(AnnotatedTypeVariable annotatedTypeVariable)
    {
        boolean reference = CspAnnotationUtils.isCspReference(annotatedTypeVariable);
        String typeVariableName = ((TypeVariable<?>)annotatedTypeVariable).getName();
        return proxyProcessorFactory.createTypeVariableProcessor(reference, typeVariableName);
    }

    private Map<String, P> getTypeVariableNameAndProcessors(AnnotatedParameterizedType annotatedParameterizedType,
        Class<?> declaredClazz)
    {
        Map<String, P> typeVariableNameAndProcessors = new HashMap<>();
        AnnotatedType[] annotatedTypes = annotatedParameterizedType.getAnnotatedActualTypeArguments();
        TypeVariable<? extends Class<?>>[] typeVariables = declaredClazz.getTypeParameters();
        for (int i = 0; i < typeVariables.length; ++i)
        {
            TypeVariable<? extends Class<?>> typeVariable = typeVariables[i];
            AnnotatedType annotatedType = annotatedTypes[i];
            P subProcessor = createProcessorSwitch(annotatedType, true);
            typeVariableNameAndProcessors.put(typeVariable.getName(), subProcessor);
        }
        return typeVariableNameAndProcessors;
    }

    private static Charset requireStringCharset(AnnotatedType annotatedType)
    {
        Optional<Charset> charset = CspAnnotationUtils.resolveCspStringCharset(annotatedType);
        if (charset.isEmpty())
        {
            throw CspRuntimeException.createCspRuntimeException(CspStatus.ERROR_IN_STRUCT_FORMAT,
                MessageFormat.format(Messages.CspStatus_Error_in_struct_format_Property__0__for_struct__1__not_set,
                    "charset", "CSP String"));
        }
        return charset.get();
    }

    private static Class<?> selectProcessorClass(AnnotatedType annotatedType, Class<?> declaredClazz,
        boolean overrideWithUpperBound)
    {
        Optional<Class<?>> overrideClazz = CspAnnotationUtils.resolveCspOverrideProcessorClass(annotatedType);
        if (overrideClazz.isPresent()
                && classCannotOverrideDeclaredClass(overrideClazz.get(), declaredClazz, overrideWithUpperBound))
        {
            throw CspRuntimeException.createCspRuntimeException(CspStatus.ERROR_IN_STRUCT_FORMAT,
                MessageFormat.format(Messages.CspStatus_Error_in_struct_format_Class__0__cannot_override_class__1,
                    overrideClazz.get(), declaredClazz));
        }
        return overrideClazz.orElse(declaredClazz);
    }

    private static <T> Class<?> selectProcessorClassForCollectionOrMap(AnnotatedType annotatedType,
        Class<?> declaredClazz, Class<T> baseClazz, boolean overrideWithUpperBound)
    {
        Optional<Class<?>> overrideClazz = CspAnnotationUtils.resolveCspOverrideProcessorClass(annotatedType);
        if (overrideClazz.isPresent())
        {
            if (classCannotOverrideDeclaredClass(overrideClazz.get(), declaredClazz, overrideWithUpperBound))
            {
                throw CspRuntimeException.createCspRuntimeException(CspStatus.ERROR_IN_STRUCT_FORMAT,
                    MessageFormat.format(Messages.CspStatus_Error_in_struct_format_Class__0__cannot_override_class__1,
                        overrideClazz.get(), declaredClazz));
            }
            return overrideClazz.get();
        }
        return baseClazz;
    }

    private static @Nullable Class<?> selectImplementationOverrideClass(AnnotatedType annotatedType,
        Class<?> declaredClazz)
    {
        Optional<Class<?>> implementationClazz = CspAnnotationUtils.resolveCspImplementationClass(annotatedType);
        if (implementationClazz.isPresent() && !declaredClazz.isAssignableFrom(implementationClazz.get()))
        {
            throw CspRuntimeException.createCspRuntimeException(CspStatus.ERROR_IN_STRUCT_FORMAT,
                MessageFormat.format(
                    Messages.CspStatus_Error_in_struct_format_Class__0__cannot_be_implementation_of_class__1,
                    implementationClazz.get(), declaredClazz));
        }
        return implementationClazz.orElse(null);
    }

    private static boolean classCannotOverrideDeclaredClass(Class<?> overrideClazz, Class<?> declaredClazz,
        boolean upperBound)
    {
        boolean canOverride = upperBound
                              ? declaredClazz.isAssignableFrom(overrideClazz)
                              : overrideClazz.isAssignableFrom(declaredClazz);
        return canOverride && declaredClazz.getTypeParameters().length != overrideClazz.getTypeParameters().length;
    }
}
