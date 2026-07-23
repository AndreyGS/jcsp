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

import io.andreygs.jcsp.api.exception.JcspRuntimeException;
import io.andreygs.jcsp.internal.infrastructure.service.factory.IJcspServiceFactory;
import io.andreygs.jcsp.internal.infrastructure.service.factory.IJcspServiceFactoryProducer;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * TODO: place description here
 */
public class JcspDomXmlServiceConfigReader implements IJcspXmlServiceConfigReader
{
    private final IJcspServiceFactoryProducer serviceFactoryProducer;

    public JcspDomXmlServiceConfigReader(IJcspServiceFactoryProducer serviceFactoryProducer)
    {
        this.serviceFactoryProducer = Objects.requireNonNull(serviceFactoryProducer);
    }

    @Override
    public Map<IJcspServiceKey, IJcspServiceFactory> read(Supplier<@Nullable InputStream> streamSupplier)
    {
        Map<IJcspServiceKey, IJcspServiceFactory> factories = new HashMap<>();
        try (InputStream xmlStream = streamSupplier.get())
        {
            if (xmlStream == null)
            {
                throw JcspRuntimeException.forXmlLoadingError(
                    "Stream of IoC configuration returned from stream supplier is null", null);
            }
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlStream);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("service");
            for (int i = 0; i < nodeList.getLength(); i++)
            {
                if (!(nodeList.item(i) instanceof Element element))
                {
                    continue;
                }
                String interfaceClassName = element.getAttribute("interface").trim();
                String serviceName = element.getAttribute("name").trim();
                String[] genericTypeVariableClassNames = element.getAttribute("generic").trim().split(",");
                IJcspServiceFactory serviceFactory;
                String instantiatorClassName = element.getAttribute("implementation").trim();
                if (!instantiatorClassName.isEmpty())
                {
                    serviceFactory = serviceFactoryProducer.produceFactoryFactory(interfaceClassName, serviceName,
                            genericTypeVariableClassNames, instantiatorClassName);
                    factories.put(serviceFactory.getServiceKey(), serviceFactory);
                    continue;
                }
                instantiatorClassName = element.getAttribute("factory").trim();
                if (!instantiatorClassName.isEmpty())
                {
                    serviceFactory = serviceFactoryProducer.produceFactoryFactory(interfaceClassName, serviceName,
                            genericTypeVariableClassNames, instantiatorClassName);
                    factories.put(serviceFactory.getServiceKey(), serviceFactory);
                    continue;
                }
                instantiatorClassName = element.getAttribute("genericFactory").trim();
                if (!instantiatorClassName.isEmpty())
                {
                    serviceFactory = serviceFactoryProducer.produceGenericFactoryFactory(interfaceClassName, serviceName,
                        genericTypeVariableClassNames, instantiatorClassName);
                    factories.put(serviceFactory.getServiceKey(), serviceFactory);
                    continue;
                }
                throw  JcspRuntimeException.forXmlLoadingError(
                    "Element \"" + element + "\" is malformed: instantiator class name is absent", null);
            }
            return factories;
        }
        catch (IOException | ParserConfigurationException | SAXException e)
        {
            throw JcspRuntimeException.forXmlLoadingError(
                "Error while reading IoC configuration from xml stream", e);
        }
    }
}
