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

import io.andreygs.jcsp.internal.infrastructure.IParametrizedStringInterpolator;
import io.andreygs.jcsp.internal.infrastructure.ITemplateVariableValueProvider;
import io.andreygs.jcsp.internal.infrastructure.factory.MultiValueProviderParametrizedStringInterpolatorFactory;
import io.andreygs.jcsp.internal.infrastructure.resource.IResourceMessagesLoader;
import io.andreygs.jcsp.internal.infrastructure.resource.ResourceMessagesWithTemplateVariablesLoader;
import io.andreygs.jcsp.internal.infrastructure.resource.ResourceConstantValueProviderRegistry;

import java.util.List;

/**
 * Stateless factory for creating {@link ResourceMessagesWithTemplateVariablesLoader} instances.
 * <p>
 * Uses "io.andreygs.jcsp.internal.infrastructure.resource" bundle as a source for
 * {@link ITemplateVariableValueProvider} that is using by {@link IParametrizedStringInterpolator} for default
 * {@link IResourceMessagesLoader} creation.
 */
public class ResourceMessagesWithTemplateVariablesLoaderFactory implements IResourceMessagesLoaderFactory
{
    private static final String INFRASTRUCTURE_RESOURCE_PACKAGE_NAME =
        "io.andreygs.jcsp.internal.infrastructure.resource";

    @Override
    public IResourceMessagesLoader create()
    {
        ITemplateVariableValueProvider templateVariableValueProvider =
            ResourceConstantValueProviderRegistry
                .getInstance()
                .requireProvider(INFRASTRUCTURE_RESOURCE_PACKAGE_NAME);
        IParametrizedStringInterpolator parametrizedStringInterpolator =
            new MultiValueProviderParametrizedStringInterpolatorFactory()
                .create(List.of(templateVariableValueProvider));
        return new ResourceMessagesWithTemplateVariablesLoader(List.of(parametrizedStringInterpolator));
    }

    @Override
    public IResourceMessagesLoader create(List<IParametrizedStringInterpolator> stringInterpolators)
    {
        return new ResourceMessagesWithTemplateVariablesLoader(stringInterpolators);
    }
}
