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

package io.andreygs.jcsp.internal.infrastructure;

import io.andreygs.jcsp.api.infrastructure.IFactoryRegistry;
import io.andreygs.jcsp.internal.infrastructure.resource.factory.IResourceConstantValueProviderFactory;
import io.andreygs.jcsp.internal.infrastructure.resource.factory.IResourceMessagesLoaderFactory;
import io.andreygs.jcsp.internal.infrastructure.resource.factory.MapBasedTemplateVariableValueProviderFactory;
import io.andreygs.jcsp.internal.infrastructure.resource.factory.ResourceMessagesWithTemplateVariablesLoaderFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Registry of factories which should be created outside another factory classes.
 * <p>
 * Contain next factories:
 * <ul>
 *     <li>{@link IResourceMessagesLoaderFactory}</li>
 * </ul>
 * This registry should be used only if another only option is to create specific factory outside another factory class.
 * As the consequence new factories should be registered here only if one cannot create specific factory inside
 * another factory class.
 */
public class InternalFactoryRegistry extends AbstractFactoryRegistry
{
    private static final IFactoryRegistry INSTANCE = new InternalFactoryRegistry();
    private final Map<Class<?>, Object> factories;

    /**
     * Gets the singleton.
     *
     * @return singleton.
     */
    public static IFactoryRegistry getInstance()
    {
        return INSTANCE;
    }
    @Override
    protected Map<Class<?>, Object> getFactories()
    {
        return factories;
    }

    private InternalFactoryRegistry()
    {
        Map<Class<?>, Object> factoriesMutable = new HashMap<>();
        factoriesMutable.put(IResourceConstantValueProviderFactory.class, new MapBasedTemplateVariableValueProviderFactory());
        factoriesMutable.put(IResourceMessagesLoaderFactory.class, new ResourceMessagesWithTemplateVariablesLoaderFactory());
        factories = Map.copyOf(factoriesMutable);
    }
}
