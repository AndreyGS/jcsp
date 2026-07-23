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

package io.andreygs.jcsp.internal.processing.factory;

import io.andreygs.jcsp.api.processing.data.clazz.ICspClassSerializationProcessor;
import io.andreygs.jcsp.internal.protocol.message.context.factory.ICspMessageContextFactory;
import io.andreygs.jcsp.internal.processing.buffer.factory.ISerializationBufferFactory;
import io.andreygs.jcsp.internal.processing.buffer.factory.SerializationBufferFactory;
import io.andreygs.jcsp.internal.processing.data.clazz.ICspClassProcessorRegistry;
import io.andreygs.jcsp.internal.processing.data.factory.CspDataSerializationProcessorFactoryProducer;
import io.andreygs.jcsp.internal.processing.data.factory.ICspDataSerializationProcessorFactoryProducer;
import io.andreygs.jcsp.internal.processing.data.type.ICspTypeProcessorRegistry;
import io.andreygs.jcsp.internal.processing.data.type.ICspTypeSerializationProcessor;
import io.andreygs.jcsp.internal.protocol.message.context.factory.CspMessageContextFactory;
import io.andreygs.jcsp.internal.protocol.message.factory.CspMessageFactory;
import io.andreygs.jcsp.internal.processing.ICspSerializationWorkflow;
import io.andreygs.jcsp.internal.processing.CspSerializationWorkflow;
import io.andreygs.jcsp.internal.protocol.message.factory.ICspMessageFactory;

/**
 * TODO: place description here
 */
public final class CspSerializationWorkflowFactory
    implements ICspSerializationWorkflowFactory
{
    private static final ISerializationBufferFactory DEFAULT_SERIALIZATION_BUFFER_FACTORY =
        new SerializationBufferFactory();
    private static final ICspMessageContextFactory DEFAULT_CSP_MESSAGE_CONTEXT_FACTORY =
        new CspMessageContextFactory();
    private static final ICspMessageFactory DEFAULT_CSP_MESSAGE_FACTORY = new CspMessageFactory();
    private static final ICspDataSerializationProcessorFactoryProducer
        DEFAULT_CSP_DATA_SERIALIZATION_PROCESSOR_FACTORY_PRODUCER = new CspDataSerializationProcessorFactoryProducer();

    @Override
    public ICspSerializationWorkflow create(
        ICspClassProcessorRegistry<ICspClassSerializationProcessor<?>> cspClassProcessorRegistry,
        ICspTypeProcessorRegistry<ICspTypeSerializationProcessor> cspTypeProcessorRegistry)
    {
        return new CspSerializationWorkflow(
            DEFAULT_SERIALIZATION_BUFFER_FACTORY,
            DEFAULT_CSP_MESSAGE_CONTEXT_FACTORY,
            DEFAULT_CSP_MESSAGE_FACTORY,
            DEFAULT_CSP_DATA_SERIALIZATION_PROCESSOR_FACTORY_PRODUCER.produce(cspClassProcessorRegistry,
                cspTypeProcessorRegistry));
    }
}
