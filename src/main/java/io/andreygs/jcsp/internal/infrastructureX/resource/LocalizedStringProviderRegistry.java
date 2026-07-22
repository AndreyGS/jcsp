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

package io.andreygs.jcsp.internal.infrastructureX.resource;

import io.andreygs.jcsp.internal.infrastructure.resource.ILocalizedStringProvider;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TODO: place description here
 */
public class LocalizedStringProviderRegistry implements ILocalizedStringProviderRegistry
{
    private final Map<String, ILocalizedStringProvider> providers = new ConcurrentHashMap<>();
    private final ILocalizedStringProviderFactory localizedStringProviderFactory;

    public LocalizedStringProviderRegistry(ILocalizedStringProviderFactory localizedStringProviderFactory)
    {
        this.localizedStringProviderFactory = Objects.requireNonNull(localizedStringProviderFactory);
    }

    @Override
    public ILocalizedStringProvider requireProvider(Class<?> targetClass)
    {
        ILocalizedStringProvider provider = providers.get(targetClass.getName());
        if (provider != null)
        {
            return provider;
        }
        synchronized (providers)
        {
            provider = providers.get(targetClass.getName());
            if (provider != null)
            {
                return provider;
            }

        }
        return null;
    }

    @Override
    public ILocalizedStringProvider requireProvider(Class<?> targetClass,
        List<ILocalizedStringProvider> templateVariableValueProviders)
    {
        return null;
    }
}
