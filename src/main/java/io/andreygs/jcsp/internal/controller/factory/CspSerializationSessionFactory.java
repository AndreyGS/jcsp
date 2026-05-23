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

package io.andreygs.jcsp.internal.controller.factory;

import io.andreygs.jcsp.api.controller.ICspSerializationSession;
import io.andreygs.jcsp.api.controller.factory.ICspSerializationSessionFactory;
import io.andreygs.jcsp.api.model.buffer.dto.ISerializationBufferConfig;
import io.andreygs.jcsp.api.model.protocol.message.config.ICspDataMessageConfig;
import io.andreygs.jcsp.internal.controller.CspSerializationSession;
import io.andreygs.jcsp.internal.model.buffer.factory.SerializationBufferConfigFactory;
import io.andreygs.jcsp.internal.model.protocol.message.builder.factory.CspMessageBuilderFactory;
import io.andreygs.jcsp.internal.processing.data.factory.CspProcessorRegistryFactory;
import io.andreygs.jcsp.internal.processing.factory.SerializationWorkflowProvider;
import org.jetbrains.annotations.Nullable;

/**
 * TODO: place description here
 */
public class CspSerializationSessionFactory
    implements ICspSerializationSessionFactory
{
    private static final ISerializationBufferConfig DEFAULT_BUFFER_CONFIG = new SerializationBufferConfigFactory().provideDefaultBufferConfig();

    private static final ICspDataMessageConfig DEFAULT_DATA_MESSAGE_CONFIG = new SerializationBufferConfigFactory().provideDefaultBufferConfig();

    @Override
    public ICspSerializationSession createSession()
    {
        return new CspSerializationSession(
            new SerializationWorkflowProvider().provideWorkflow(),
            new CspMessageBuilderFactory(),
            new CspProcessorRegistryFactory().createProcessorRegistry(), DEFAULT_BUFFER_CONFIG, );
    }

    @Override
    public ICspSerializationSession createSession(@Nullable ISerializationBufferConfig bufferConfig,
        @Nullable ICspDataMessageConfig dataMessageConfig)
    {
        return null;
    }
}
