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

package io.andreygs.jcsp.api.service;

import io.andreygs.jcsp.api.exception.JcspRuntimeException;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TODO: place description here
 */
public final class JcspServiceProvider
{
    private final Map<Class<?>, CreationSettings> services = new ConcurrentHashMap<>();
    private final Map<Class<?>, Object> runningServices = new ConcurrentHashMap<>();
    private final boolean customServicesMode;
    @SuppressWarnings("NotNullFieldNotInitialized" /* It is always initialized */ )
    private String loadedPath;

    public static JcspServiceProvider getInstance()
    {
        JcspServiceProvider instance = Holder.INSTANCE;
        if (instance.customServicesMode)
        {
            String testPath = System.getProperty("lib.config.services.custom");;
            if (testPath != null && !instance.loadedPath.equals(testPath))
            {
                instance.reload(testPath);
            }
        }
        return instance;
    }

    public JcspServiceProvider()
    {
        String testPath = System.getProperty("lib.config.services.custom");;
        if (testPath.isEmpty())
        {
            customServicesMode = false;
            reload("infrastructure/services.xml");
        }
        else
        {
            customServicesMode = true;
            reload(testPath);
        }
    }

    public <S> S provide(Class<S> serviceClass)
    {
        S runningService = serviceClass.cast(runningServices.get(serviceClass));
        if (runningService != null)
        {
            return runningService;
        }
        return createService(serviceClass);
    }

    private <S> S createService(Class<S> serviceClass)
    {
        synchronized (runningServices)
        {
            S runningService = serviceClass.cast(runningServices.get(serviceClass));
            if (runningService != null)
            {
                return runningService;
            }
            CreationSettings serviceCreationSettings = services.get(serviceClass);
            if (serviceCreationSettings == null)
            {
               // throw new NoSuchElementException(
                  //  MessageFormat.format(Messages.ServiceProviderDelegate_Service__0__not_registered, serviceClass.getName()));
            }

        }
    }

    private void reload(String currentPath)
    {
        if (currentPath.equals(loadedPath))
        {
            return;
        }
        runningServices.clear();
        try (InputStream in = JcspServiceProvider.class.getClassLoader().getResourceAsStream(currentPath))
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(in);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("service");
            for (int i = 0; i < nodeList.getLength(); i++)
            {
                Node node = nodeList.item(i);
                if (!(node instanceof Element element))
                {
                    continue;
                }
                String interfaceName = element.getAttribute("interface").trim();
                Class<?> interfaceClass = Class.forName(interfaceName);
                CreationSettings creationSettings = produceCreationSettings(interfaceClass, element);
                services.put(interfaceClass, creationSettings);
            }
            loadedPath = currentPath;
        }
        catch (IOException | ParserConfigurationException | SAXException e)
        {
            throw JcspRuntimeException.forXmlLoadingError("Error while loading \"" + currentPath + "\"", e);
        }
        catch (ClassNotFoundException e)
        {
            throw JcspRuntimeException.forClassError("Error while loading \"" + currentPath + "\"", e);
        }
    }

    private CreationSettings produceCreationSettings(Class<?> interfaceClass, Element element)
    {
        String instantiationResponsibleClassName = element.getAttribute("implementation").trim();
        if (!instantiationResponsibleClassName.isEmpty())
        {
            Class<?> clazz = requireClass(instantiationResponsibleClassName);
            requireDefaultConstructor(clazz);
            if (!interfaceClass.isAssignableFrom(clazz))
            {
                throw JcspRuntimeException.forClassError("Class \"" + instantiationResponsibleClassName +
                                                             "\" is not assignable from " + interfaceClass, null);
            }
            return new CreationSettings(CreationType.IMPLEMENTATION, clazz);
        }
        instantiationResponsibleClassName = element.getAttribute("factory").trim();
        if (!instantiationResponsibleClassName.isEmpty())
        {
            Class<?> clazz = requireClass(instantiationResponsibleClassName);
            requireMethod(clazz, "create", interfaceClass);
            return new CreationSettings(CreationType.FACTORY, clazz);
        }
        instantiationResponsibleClassName = element.getAttribute("genericFactory").trim();
        if (!instantiationResponsibleClassName.isEmpty())
        {
            Class<?> clazz = requireClass(instantiationResponsibleClassName);
            requireMethod(clazz, "create", interfaceClass, Class.class);
            return new CreationSettings(CreationType.GENERIC_FACTORY, clazz);
        }
        throw JcspRuntimeException.forClassError("Element \"" + element + "\" does not have any of the "
                                               + "implementation|factory|genericFactory attributes", null);
    }

    private static Class<?> requireClass(String className)
    {
        try
        {
            return Class.forName(className);
        }
        catch (ClassNotFoundException e)
        {
            throw JcspRuntimeException.forClassError("Class \"" + className + "\" not found", null);
        }
    }

    private static Constructor<?> requireDefaultConstructor(Class<?> clazz)
    {
        Constructor<?> constructor;
        try
        {
            constructor = clazz.getDeclaredConstructor();
        }
        catch (NoSuchMethodException e)
        {
            throw JcspRuntimeException.forClassError(
                "Class \"" + clazz.getName() + "\" has no default constructor", null);
        }
        if (!Modifier.isPublic(constructor.getModifiers()))
        {
            throw JcspRuntimeException.forClassError(
                "Class \"" + clazz.getName() + "\" default constructor is not public", null);
        }
        return constructor;
    }

    private static Method requireMethod(Class<?> clazz, String methodName, Class<?> returnType,
        Class<?>... parameterTypes)
    {
        Method method;
        try
        {
            method = clazz.getDeclaredMethod(methodName, parameterTypes);
        }
        catch (NoSuchMethodException e)
        {
            throw JcspRuntimeException.forClassError(
                "Class \"" + clazz.getName() + "\" has no method \"" + methodName +
                    "(" + arrayToString(parameterTypes, ",") + ")\"", null);
        }
        if (!returnType.isAssignableFrom(method.getReturnType()))
        {
            throw JcspRuntimeException.forClassError(
                "Class \"" + returnType.getName() + "\" is not assignable from \"" +
                    method.getReturnType() + "\" return type", null);
        }
        return method;
    }

    private static <T> String arrayToString(@Nullable T @Nullable [] array, String delimiter) {
        if (array == null || array.length == 0)
        {
            return "";
        }
        StringJoiner joiner = new StringJoiner(delimiter);
        for (T element : array)
        {
            joiner.add(element == null ? "null" : element.toString());
        }
        return joiner.toString();
    }

    private static class Holder
    {
        private static final JcspServiceProvider INSTANCE = new JcspServiceProvider();
    }

    private enum CreationType
    {
        IMPLEMENTATION, FACTORY, GENERIC_FACTORY
    }

    private static class CreationSettings
    {
        private final CreationType creationType;
        private final Class<?> instantiationResponsibleClass;

        private CreationSettings(CreationType creationType, Class<?> instantiationResponsibleClass)
        {
            this.creationType = Objects.requireNonNull(creationType);
            this.instantiationResponsibleClass = Objects.requireNonNull(instantiationResponsibleClass);
        }

        public CreationType getCreationType()
        {
            return creationType;
        }

        public Class<?> getInstantiationResponsibleClass()
        {
            return instantiationResponsibleClass;
        }
    }

    private static class ServiceCreator
    {
        private final Class<?> targetClass;
        private final CreationSettings creationSettings;

        private ServiceCreator(Class<?> targetClass, CreationSettings creationSettings)
        {
            this.targetClass = Objects.requireNonNull(targetClass);
            this.creationSettings = Objects.requireNonNull(creationSettings);
        }
    }
}
