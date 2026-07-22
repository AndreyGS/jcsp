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

package io.andreygs.jcsp.internal.infrastructure.resource.factory;

import io.andreygs.jcsp.internal.infrastructure.resource.IJcspMessageProvider;
import io.andreygs.jcsp.internal.infrastructure.resource.IJcspParametrizedStringInterpolator;
import io.andreygs.jcsp.internal.infrastructureX.ITemplateVariableValueProvider;
import io.andreygs.jcsp.internal.infrastructureX.resource.factory.IResourceMessagesLoaderFactory;
import io.andreygs.jcsp.internal.infrastructureX.resource.factory.ResourceMessagesLoaderFactory;
import io.andreygs.jcsp.internal.infrastructure.resource.ILocalizedStringProvider;
import io.andreygs.jcsp.internal.infrastructureX.resource.IResourceMessagesReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * TODO: place description here
 */
public class JcspMessageProviderFactory implements IJcspMessageProviderFactory
{
    private static final IResourceMessagesLoaderFactory DEFAULT_RESOURCE_MESSAGES_LOADER_FACTORY =
        new ResourceMessagesLoaderFactory();
    private static final IJcspParametrizedStringInterpolatorFactory DEFAULT_PARAMETRIZED_STRING_INTERPOLATOR_FACTORY =
        new JcspParametrizedStringInterpolatorFactory();
    private static final IJcspResourceReaderFactory DEFAULT_RESOURCE_READER_FACTORY =
        new JcspResourceReaderFactory();

    @Override
    public IJcspMessageProvider create(String packageName)
    {

    }

    @Override
    public ILocalizedStringProvider create(Class<?> targetClass,
        List<ILocalizedStringProvider> templateVariableStringValueProviders)
    {
        List<ITemplateVariableValueProvider> templateVariableValueProviders =
            getTemplateVariableValueProviders(templateVariableStringValueProviders);
        IJcspParametrizedStringInterpolator parametrizedStringInterpolator =
            DEFAULT_PARAMETRIZED_STRING_INTERPOLATOR_FACTORY.create(templateVariableValueProviders);
        IResourceMessagesReader resourceMessagesLoader =
            DEFAULT_RESOURCE_MESSAGES_LOADER_FACTORY.create(List.of(parametrizedStringInterpolator));
        Map<String, String> readData = resourceMessagesLoader.read(targetClass);
        return new JcspMessageProvider(readData);
    }

    private static List<ITemplateVariableValueProvider> getTemplateVariableValueProviders(
        List<ILocalizedStringProvider> templateVariableStringValueProviders)
    {
        List<ITemplateVariableValueProvider> templateVariableValueProviders =
            new ArrayList<>(templateVariableStringValueProviders.size());
        for (ILocalizedStringProvider templateVariableStringValueProvider : templateVariableStringValueProviders)
        {
            ITemplateVariableValueProvider templateVariableValueProvider =
                name -> Optional.of(templateVariableStringValueProvider.provide(name));
            templateVariableValueProviders.add(templateVariableValueProvider);
        }
        return templateVariableValueProviders;
    }
}
