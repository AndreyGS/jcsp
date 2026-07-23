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

package io.andreygs.jcsp.internal.infrastructure.service.factory;

import io.andreygs.jcsp.api.exception.JcspRuntimeException;
import io.andreygs.jcsp.internal.infrastructure.service.IJcspServiceKey;
import io.andreygs.jcsp.internal.infrastructure.service.JcspServiceKey;
import io.andreygs.jcsp.internal.infrastructure.service.utils.JcspInjectedUtils;
import io.andreygs.jcsp.internal.infrastructure.utils.JcspReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO: place description here
 */
public class JcspServiceFactoryProducer implements IJcspServiceFactoryProducer
{
    @Override
    public IJcspServiceFactory produceImplementationFactory(String interfaceClassName, String serviceName,
        String[] genericTypeVariableClassNames, String implementationClassName)
    {
        Class<?> interfaceClass = JcspReflectionUtils.requireClass(interfaceClassName);
        Class<?> implementationClass = JcspReflectionUtils.requireClass(implementationClassName);
        if (!interfaceClass.isAssignableFrom(implementationClass))
        {
            throw JcspRuntimeException.forClassError(
                "Class \"" + implementationClass + "\" is not assignable from " + interfaceClass, null);
        }
        List<Class<?>> genericTypeVariableClasses = parseGenericTypeVariableClasses(genericTypeVariableClassNames);
        IJcspServiceKey serviceKey = new JcspServiceKey(interfaceClass, genericTypeVariableClasses, serviceName);
        Constructor<?> constructor = JcspInjectedUtils.requireInjectedOrDefaultConstructor(implementationClass);
        List<IJcspServiceKey> injectedParameters = JcspInjectedUtils.requireInjectedParameters(constructor);
        return new JcspImplementationServiceFactory(serviceKey, constructor, injectedParameters);
    }

    @Override
    public IJcspServiceFactory produceFactoryFactory(String interfaceClassName, String serviceName,
        String[] genericTypeVariableClassNames, String factoryClassName)
    {
        return produceFactoryCommon(interfaceClassName, serviceName, genericTypeVariableClassNames, factoryClassName,
            List.of());
    }

    @Override
    public IJcspServiceFactory produceGenericFactoryFactory(String interfaceClassName, String serviceName,
        String[] genericTypeVariableClassNames, String factoryClassName)
    {
        Class<?> interfaceClass = JcspReflectionUtils.requireClass(interfaceClassName);
        return produceFactoryCommon(interfaceClassName, serviceName, genericTypeVariableClassNames, factoryClassName,
            List.of(interfaceClass), Class.class);
    }

    private List<Class<?>> parseGenericTypeVariableClasses(String[] genericTypeVariableClassNames)
    {
        List<Class<?>> genericTypeVariableClasses = genericTypeVariableClassNames.length == 0
                                                 ? List.of()
                                                 : new ArrayList<>(genericTypeVariableClassNames.length);
        for (String genericTypeVariablesClassName : genericTypeVariableClassNames)
        {
            Class<?> genericTypeVariableClass = JcspReflectionUtils.requireClass(genericTypeVariablesClassName);
            genericTypeVariableClasses.add(genericTypeVariableClass);
        }
        return genericTypeVariableClasses;
    }

    private IJcspServiceFactory produceFactoryCommon(String interfaceClassName, String serviceName,
        String[] genericTypeVariableClassNames, String factoryClassName, List<Object> factoryMethodArguments,
        Class<?>... factoryMethodParameters)
    {
        Class<?> interfaceClass = JcspReflectionUtils.requireClass(interfaceClassName);
        List<Class<?>> genericTypeVariableClasses = parseGenericTypeVariableClasses(genericTypeVariableClassNames);
        IJcspServiceKey serviceKey = new JcspServiceKey(interfaceClass, genericTypeVariableClasses, serviceName);
        Class<?> factoryClass = JcspReflectionUtils.requireClass(factoryClassName);
        Constructor<?> constructor = JcspInjectedUtils.requireInjectedOrDefaultConstructor(factoryClass);
        List<IJcspServiceKey> injectedParameters = JcspInjectedUtils.requireInjectedParameters(constructor);
        Method method = JcspReflectionUtils.requireMethod(factoryClass, "create", interfaceClass, factoryMethodParameters);
        return new JcspFactoryServiceFactory(serviceKey, constructor, injectedParameters, method, factoryMethodArguments);
    }
}
