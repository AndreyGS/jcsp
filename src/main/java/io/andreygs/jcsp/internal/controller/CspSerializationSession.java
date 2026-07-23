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

package io.andreygs.jcsp.internal.controller;

import io.andreygs.jcsp.api.controller.ICspSerializationSession;
import io.andreygs.jcsp.api.processing.buffer.ISerializationBufferConfig;
import io.andreygs.jcsp.api.protocol.message.ICspDataMessage;
import io.andreygs.jcsp.api.protocol.message.config.ICspDataMessageConfigExtension;
import io.andreygs.jcsp.api.protocol.ICspVersionable;
import io.andreygs.jcsp.api.protocol.message.config.ICspMessageConfig;
import io.andreygs.jcsp.api.processing.data.clazz.ICspClassSerializationProcessor;
import io.andreygs.jcsp.internal.processing.ICspSerializationWorkflow;
import io.andreygs.jcsp.internal.processing.data.clazz.ICspClassProcessorRegistry;
import io.andreygs.jcsp.internal.processing.data.type.ICspTypeProcessorRegistry;
import io.andreygs.jcsp.internal.processing.data.type.ICspTypeSerializationProcessor;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.AnnotatedType;
import java.util.List;
import java.util.Objects;

/**
 * TODO: place description here
 */
public class CspSerializationSession implements ICspSerializationSession
{
    private final ICspClassProcessorRegistry<ICspClassSerializationProcessor<?>> classProcessorRegistry;
    private final ICspTypeProcessorRegistry<ICspTypeSerializationProcessor> typeProcessorRegistry;
    private final ISerializationBufferConfig bufferConfig;
    private final ICspMessageConfig messageConfig;
    private final ICspDataMessageConfigExtension dataMessageConfigExtension;
    private final ICspSerializationWorkflow serializationWorkflow;

    public CspSerializationSession(
        ICspClassProcessorRegistry<ICspClassSerializationProcessor<?>> classProcessorRegistry,
        ICspTypeProcessorRegistry<ICspTypeSerializationProcessor> typeProcessorRegistry,
        ISerializationBufferConfig bufferConfig,
        ICspMessageConfig messageConfig,
        ICspDataMessageConfigExtension dataMessageConfigExtension,
        ICspSerializationWorkflow serializationWorkflow)
    {
        this.serializationWorkflow = Objects.requireNonNull(serializationWorkflow);
        this.classProcessorRegistry = Objects.requireNonNull(classProcessorRegistry);
        this.typeProcessorRegistry = Objects.requireNonNull(typeProcessorRegistry);
        this.bufferConfig = Objects.requireNonNull(bufferConfig);
        this.messageConfig = Objects.requireNonNull(messageConfig);
        this.dataMessageConfigExtension = Objects.requireNonNull(dataMessageConfigExtension);

    }

    @Override
    public <T> void registerClassProcessor(Class<T> clazz, ICspClassSerializationProcessor<T> classProcessor)
    {
        classProcessorRegistry.register(clazz, classProcessor);
    }

    @Override
    public void unregisterClassProcessor(Class<?> clazz, List<AnnotatedType> associatedAnnotatedTypes)
    {
        classProcessorRegistry.unregister(clazz);
    }

    @Override
    public <T extends ICspVersionable> ICspDataMessage<T> serializeData(ICspVersionable struct, Class<T> clazz)
    {
        return serializationWorkflow.serializeDataMessage(Objects.requireNonNull(struct), Objects.requireNonNull(clazz),
            bufferConfig, messageConfig, dataMessageConfigExtension);
    }

    @Override
    public <T extends ICspVersionable> ICspDataMessage<T> serializeData(ICspVersionable struct, Class<T> clazz,
        @Nullable ISerializationBufferConfig customBufferConfig, @Nullable ICspMessageConfig customMessageConfig,
        @Nullable ICspDataMessageConfigExtension customDataMessageConfigExtension)
    {
        return serializationWorkflow.serializeDataMessage(Objects.requireNonNull(struct), Objects.requireNonNull(clazz),
            customBufferConfig != null ?  customBufferConfig : bufferConfig,
            customMessageConfig != null ? customMessageConfig : messageConfig,
            customDataMessageConfigExtension != null ? customDataMessageConfigExtension : dataMessageConfigExtension);
    }
}
