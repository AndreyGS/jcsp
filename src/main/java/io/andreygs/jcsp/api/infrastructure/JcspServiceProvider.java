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

package io.andreygs.jcsp.api.infrastructure;

import io.andreygs.jcsp.api.exception.JcspRuntimeException;
import io.andreygs.jcsp.internal.infrastructure.service.IJcspCreatableService;
import io.andreygs.jcsp.internal.infrastructure.service.IJcspService;
import io.andreygs.jcsp.internal.infrastructure.service.JcspService;
import io.andreygs.jcsp.internal.infrastructure.service.factory.JcspCreatableServiceFactory;
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
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TODO: place description here
 */
public final class JcspServiceProvider implements IJcspServiceProvider
{
    private final Map<IJcspService, IJcspCreatableService> services = new ConcurrentHashMap<>();
    private final Map<Class<?>, Object> runningServices = new ConcurrentHashMap<>();
    private final Map<IJcspService, Object> runningNamedServices = new ConcurrentHashMap<>();
    private final JcspCreatableServiceFactory creatableServiceFactory = new JcspCreatableServiceFactory();
    private final boolean customServicesMode;
    @SuppressWarnings("NotNullFieldNotInitialized" /* It is always initialized */)
    private String loadedPath;

    public static JcspServiceProvider getInstance()
    {
        JcspServiceProvider instance = Holder.INSTANCE;
        if (instance.customServicesMode)
        {
            String customPath = System.getProperty("lib.config.services.custom");
            if (!instance.loadedPath.equals(customPath))
            {
                instance.reload(customPath);
            }
        }
        return instance;
    }

    private JcspServiceProvider()
    {
        String testPath = System.getProperty("lib.config.services.custom");
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

    @Override
    public <S> S provide(Class<S> serviceClass)
    {
        S runningService = serviceClass.cast(runningServices.get(serviceClass));
        if (runningService != null)
        {
            return runningService;
        }
        return createService(serviceClass, "");
    }

    @Override
    public <S> S provide(Class<S> serviceClass, String serviceName)
    {
        if (serviceName.isEmpty())
        {
            return provide(serviceClass);
        }
        IJcspService service = new JcspService(serviceClass, serviceName);
        S runningService = serviceClass.cast(runningNamedServices.get(service));
        if (runningService != null)
        {
            return runningService;
        }
        return createService(serviceClass, serviceName);
    }

    private <S> S createService(Class<S> serviceClass, String serviceName)
    {
        synchronized (services)
        {
            IJcspService service = new JcspService(serviceClass, serviceName);;
            S runningService = serviceName.isEmpty()
                ? serviceClass.cast(runningServices.get(serviceClass))
                : serviceClass.cast(runningNamedServices.get(service));
            if (runningService != null)
            {
                return runningService;
            }
            IJcspCreatableService serviceCreationSettings = services.get(service);
            if (serviceCreationSettings == null)
            {
                throw new NoSuchElementException("Service \"" + serviceClass + "\" not found");
            }
            runningService = serviceCreationSettings.createService(serviceClass);
            if (serviceName.isEmpty())
            {
                runningServices.put(serviceClass, runningService);
            }
            else
            {
                runningNamedServices.put(service, runningService);
            }
            return runningService;
        }
    }

    private void reload(String currentPath)
    {
        synchronized (services)
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
                    String interfaceSpecificName = element.getAttribute("name").trim();
                    IJcspService service = new JcspService(interfaceClass, interfaceSpecificName);
                    IJcspCreatableService creatableService = creatableServiceFactory.create(interfaceClass, element,
                        this);
                    services.put(service, creatableService);
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
    }

    private static class Holder
    {
        private static final JcspServiceProvider INSTANCE = new JcspServiceProvider();
    }
}
