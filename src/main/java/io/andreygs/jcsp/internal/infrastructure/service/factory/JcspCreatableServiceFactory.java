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
import io.andreygs.jcsp.api.infrastructure.IJcspServiceProvider;
import io.andreygs.jcsp.internal.infrastructure.service.CreatableByFactoryService;
import io.andreygs.jcsp.internal.infrastructure.service.CreatableByGenericFactoryService;
import io.andreygs.jcsp.internal.infrastructure.service.CreatableByImplementationService;
import io.andreygs.jcsp.internal.infrastructure.service.IJcspCreatableService;
import io.andreygs.jcsp.internal.infrastructure.service.IJcspInjectedParameter;
import io.andreygs.jcsp.internal.infrastructure.service.utils.JcspInjectedUtils;
import io.andreygs.jcsp.internal.infrastructure.utils.JcspReflectionUtils;
import org.w3c.dom.Element;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * TODO: place description here
 */
public class JcspCreatableServiceFactory
{
    public IJcspCreatableService create(Class<?> interfaceClass, Element element, IJcspServiceProvider serviceProvider)
    {
        String instantiationResponsibleClassName = element.getAttribute("implementation").trim();
        if (!instantiationResponsibleClassName.isEmpty())
        {
            Class<?> clazz = JcspReflectionUtils.requireClass(instantiationResponsibleClassName);
            if (!interfaceClass.isAssignableFrom(clazz))
            {
                throw JcspRuntimeException.forClassError("Class \"" + instantiationResponsibleClassName +
                                                             "\" is not assignable from " + interfaceClass, null);
            }
            Constructor<?> constructor = JcspInjectedUtils.requireInjectedOrDefaultConstructor(clazz);
            IJcspInjectedParameter[] injectedParameters = JcspInjectedUtils.requireInjectedParameters(constructor);
            return new CreatableByImplementationService(constructor, injectedParameters, serviceProvider);
        }
        instantiationResponsibleClassName = element.getAttribute("factory").trim();
        if (!instantiationResponsibleClassName.isEmpty())
        {
            Class<?> clazz = JcspReflectionUtils.requireClass(instantiationResponsibleClassName);
            Constructor<?> constructor = JcspInjectedUtils.requireInjectedOrDefaultConstructor(clazz);
            IJcspInjectedParameter[] injectedParameters = JcspInjectedUtils.requireInjectedParameters(constructor);
            Method method = JcspReflectionUtils.requireMethod(clazz, "create", interfaceClass);
            return new CreatableByFactoryService(constructor, injectedParameters, serviceProvider, method);
        }
        instantiationResponsibleClassName = element.getAttribute("genericFactory").trim();
        if (!instantiationResponsibleClassName.isEmpty())
        {
            Class<?> clazz = JcspReflectionUtils.requireClass(instantiationResponsibleClassName);
            Constructor<?> constructor = JcspInjectedUtils.requireInjectedOrDefaultConstructor(clazz);
            IJcspInjectedParameter[] injectedParameters = JcspInjectedUtils.requireInjectedParameters(constructor);
            Method method = JcspReflectionUtils.requireMethod(clazz, "create", interfaceClass, Class.class);
            return new CreatableByGenericFactoryService(constructor, injectedParameters, serviceProvider, method);
        }
        throw JcspRuntimeException.forClassError("Element \"" + element + "\" does not have any of the "
                                                     + "implementation|factory|genericFactory attributes", null);
    }
}
