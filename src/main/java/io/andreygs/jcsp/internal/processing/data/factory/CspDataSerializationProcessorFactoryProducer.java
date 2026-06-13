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

package io.andreygs.jcsp.internal.processing.data.factory;

import io.andreygs.jcsp.api.processing.data.clazz.ICspClassSerializationProcessor;
import io.andreygs.jcsp.internal.processing.data.clazz.ICspClassProcessorDescriptorProvider;
import io.andreygs.jcsp.internal.processing.data.clazz.ICspClassProcessorGenerator;
import io.andreygs.jcsp.internal.processing.data.clazz.ICspClassProcessorRegistry;
import io.andreygs.jcsp.internal.processing.data.clazz.factory.CspClassProcessorDescriptorProviderFactory;
import io.andreygs.jcsp.internal.processing.data.clazz.factory.CspClassSerializationProcessorGeneratorFactory;
import io.andreygs.jcsp.internal.processing.data.clazz.factory.ICspClassProcessorDescriptorProviderFactory;
import io.andreygs.jcsp.internal.processing.data.clazz.factory.ICspClassProcessorGeneratorFactory;
import io.andreygs.jcsp.internal.processing.data.type.ICspTypeProcessorProvider;
import io.andreygs.jcsp.internal.processing.data.type.ICspTypeProcessorRegistry;
import io.andreygs.jcsp.internal.processing.data.type.ICspTypeSerializationProcessor;
import io.andreygs.jcsp.internal.processing.data.type.factory.CspTypeSerializationProcessorProviderFactory;
import io.andreygs.jcsp.internal.processing.data.type.factory.ICspTypeProcessorProviderFactory;

/**
 * TODO: place description here
 */
public class CspDataSerializationProcessorFactoryProducer implements ICspDataSerializationProcessorFactoryProducer
{
    private static final ICspTypeProcessorProviderFactory<ICspTypeSerializationProcessor>
        DEFAULT_CSP_TYPE_PROCESSOR_PROVIDER_FACTORY = new CspTypeSerializationProcessorProviderFactory();
    private static final ICspClassProcessorGeneratorFactory<ICspClassSerializationProcessor<?>, ICspTypeSerializationProcessor>
        DEFAULT_CSP_CLASS_PROCESSOR_GENERATOR_FACTORY = new CspClassSerializationProcessorGeneratorFactory();
    private static final ICspClassProcessorDescriptorProviderFactory<ICspClassSerializationProcessor<?>>
        DEFAULT_CSP_CLASS_PROCESSOR_DESCRIPTOR_PROVIDER_FACTORY = new CspClassProcessorDescriptorProviderFactory<>();

    @Override
    public ICspDataSerializationProcessorFactory produce(
        ICspClassProcessorRegistry<ICspClassSerializationProcessor<?>> cspClassProcessorRegistry,
        ICspTypeProcessorRegistry<ICspTypeSerializationProcessor> cspTypeProcessorRegistry)
    {
        ICspTypeProcessorProvider<ICspTypeSerializationProcessor> cspTypeProcessorProvider =
            DEFAULT_CSP_TYPE_PROCESSOR_PROVIDER_FACTORY.create(cspTypeProcessorRegistry);
        ICspClassProcessorGenerator<ICspClassSerializationProcessor<?>> cspClassProcessorGenerator =
            DEFAULT_CSP_CLASS_PROCESSOR_GENERATOR_FACTORY.create(cspTypeProcessorProvider);
        ICspClassProcessorDescriptorProvider<ICspClassSerializationProcessor<?>> cspClassProcessorDescriptorProvider =
            DEFAULT_CSP_CLASS_PROCESSOR_DESCRIPTOR_PROVIDER_FACTORY.create(cspClassProcessorRegistry,
                cspClassProcessorGenerator);
        return new CspDataSerializationProcessorFactory(cspClassProcessorDescriptorProvider, cspTypeProcessorProvider);
    }
}
