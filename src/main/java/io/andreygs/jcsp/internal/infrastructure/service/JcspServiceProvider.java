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

package io.andreygs.jcsp.internal.infrastructure.service;

import io.andreygs.jcsp.internal.infrastructure.service.factory.IJcspServiceFactory;
import io.andreygs.jcsp.internal.infrastructure.service.factory.JcspServiceFactoryProducer;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * TODO: place description here
 */
public final class JcspServiceProvider implements IJcspServiceProvider
{
    private final Map<IJcspServiceKey, IJcspServiceFactory> serviceFactories;
    private final Map<Class<?>, Object> runningServices = new ConcurrentHashMap<>();
    private final Map<IJcspServiceKey, Object> runningComplexDefinedServices = new ConcurrentHashMap<>();

    private static final Supplier<@Nullable InputStream> PROD_STREAM_SUPPLIER =
        () -> IJcspServiceProvider.class.getResourceAsStream("/internal/infrastructure/service/services.xml");
    private static final Object TEST_INSTANCE_LOCK = new Object();
    private static volatile @Nullable IJcspServiceProvider testInstance;

    public static IJcspServiceProvider getInstance()
    {
        if (testInstance != null)
        {
            IJcspServiceProvider localTestInstance = testInstance;
            if (localTestInstance != null)
            {
                return localTestInstance;
            }
        }
        return Holder.INSTANCE;
    }

    public static void setTestInstance(IJcspXmlServiceConfigReader xmlServiceConfigReader)
    {
        checkTestMark();
        synchronized (TEST_INSTANCE_LOCK)
        {
            testInstance = new JcspServiceProvider(xmlServiceConfigReader);
        }
    }

    public static void unsetTestInstance()
    {
        checkTestMark();
        synchronized (TEST_INSTANCE_LOCK)
        {
            testInstance = null;
        }
    }

    private JcspServiceProvider(IJcspXmlServiceConfigReader xmlServiceConfigReader)
    {
        serviceFactories = Map.copyOf(xmlServiceConfigReader.read(PROD_STREAM_SUPPLIER));
    }

    @Override
    public <S> S provide(Class<S> serviceClass)
    {
        S runningService = serviceClass.cast(runningServices.get(serviceClass));
        if (runningService != null)
        {
            return runningService;
        }
        IJcspServiceKey serviceKey = new JcspServiceKey(serviceClass, List.of(), "");
        return serviceClass.cast(createService(serviceKey));
    }

    @Override
    public <S> S provide(Class<S> serviceClass, List<Class<?>> genericTypeVariableClasses,  String serviceName)
    {
        if (!matchesComplexDefinedService(genericTypeVariableClasses, serviceName))
        {
            return provide(serviceClass);
        }
        IJcspServiceKey serviceKey = new JcspServiceKey(serviceClass, genericTypeVariableClasses, serviceName);
        S runningService = serviceClass.cast(runningComplexDefinedServices.get(serviceKey));
        if (runningService != null)
        {
            return runningService;
        }
        return serviceClass.cast(createService(serviceKey));
    }

    @Override
    public Object provide(IJcspServiceKey serviceKey)
    {
        if (!matchesComplexDefinedService(serviceKey))
        {
            return provide(serviceKey.getClazz());
        }
        Object runningService = runningComplexDefinedServices.get(serviceKey);
        if (runningService != null)
        {
            return runningService;
        }
        return createService(serviceKey);
    }

    private Object createService(IJcspServiceKey serviceKey)
    {
        synchronized (serviceFactories)
        {
            Class<?> serviceClass = serviceKey.getClazz();
            boolean complexDefinedService = matchesComplexDefinedService(serviceKey);
            Object runningService = complexDefinedService
                                    ? runningComplexDefinedServices.get(serviceKey)
                                    : runningServices.get(serviceClass);
            if (runningService != null)
            {
                return runningService;
            }
            IJcspServiceFactory serviceFactory = serviceFactories.get(serviceKey);
            if (serviceFactory == null)
            {
                throw new NoSuchElementException("Service \"" + serviceKey + "\" not found");
            }
            runningService = serviceFactory.create(this);
            if (complexDefinedService)
            {
                runningComplexDefinedServices.put(serviceKey, runningService);
            }
            else
            {
                runningServices.put(serviceClass, runningService);
            }
            return runningService;
        }
    }

    private static boolean matchesComplexDefinedService(IJcspServiceKey serviceKey)
    {
        return matchesComplexDefinedService(serviceKey.getGenericTypeVariableClasses(), serviceKey.getName());
    }

    private static boolean matchesComplexDefinedService(List<Class<?>> genericTypeVariableClasses,  String serviceName)
    {
        return genericTypeVariableClasses.isEmpty() || serviceName.isEmpty();
    }

    private static void checkTestMark()
    {
        InputStream markStream = IJcspServiceProvider.class.getResourceAsStream("/infrastructure/service/test.mark");
        if (markStream == null)
        {
            throw new SecurityException("Operation is blocked. Cannot find test.mark");
        }
        try
        {
            markStream.close();
        }
        catch (IOException e)
        {
            throw new UncheckedIOException("Could not close test.mark", e);
        }
    }

    private static class Holder
    {
        private static final IJcspServiceProvider INSTANCE =
            new JcspServiceProvider(new JcspDomXmlServiceConfigReader(new JcspServiceFactoryProducer()));
    }
}
