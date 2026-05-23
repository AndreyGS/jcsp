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
import io.andreygs.jcsp.api.model.buffer.dto.ISerializationBufferConfig;
import io.andreygs.jcsp.api.model.buffer.dto.factory.ISerializationBufferConfigFactory;
import io.andreygs.jcsp.api.model.protocol.message.ICspDataMessage;
import io.andreygs.jcsp.api.model.protocol.message.config.ICspDataMessageConfigExtension;
import io.andreygs.jcsp.api.model.protocol.ICspVersionable;
import io.andreygs.jcsp.api.model.protocol.message.config.ICspMessageConfig;
import io.andreygs.jcsp.api.model.protocol.message.config.factory.ICspMessageConfigFactory;
import io.andreygs.jcsp.api.model.protocol.utils.CspTypeToken;
import io.andreygs.jcsp.api.processing.data.clazz.ICspClassSerializationProcessor;
import io.andreygs.jcsp.internal.processing.data.ICspProcessorRegistry;
import io.andreygs.jcsp.internal.processing.ICspSerializationWorkflow;
import io.andreygs.jcsp.internal.processing.data.type.ICspTypeSerializationProcessor;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * TODO: place description here
 */
public class CspSerializationSession implements ICspSerializationSession
{
    private final ICspSerializationWorkflow serializationWorkflow;
    private final ICspProcessorRegistry<ICspClassSerializationProcessor<?>, ICspTypeSerializationProcessor>
        processorRegistry;
    private final ISerializationBufferConfigFactory serializationBufferConfigFactory;
    private final ICspMessageConfigFactory cspMessageConfigFactory;
    private ISerializationBufferConfig defaultBufferConfig;
    private ICspMessageConfig defaultMessageConfig;
    private ICspDataMessageConfigExtension defaultDataMessageConfigExtension;

    public CspSerializationSession(ICspSerializationWorkflow serializationWorkflow,
        ICspProcessorRegistry<ICspClassSerializationProcessor<?>, ICspTypeSerializationProcessor> processorRegistry,
        ISerializationBufferConfigFactory serializationBufferConfigFactory,
        ICspMessageConfigFactory cspMessageConfigFactory)
    {
        this.serializationWorkflow = Objects.requireNonNull(serializationWorkflow);
        this.processorRegistry = Objects.requireNonNull(processorRegistry);
        this.serializationBufferConfigFactory = Objects.requireNonNull(serializationBufferConfigFactory);
        this.defaultBufferConfig = serializationBufferConfigFactory.provideDefaultBufferConfig();
        this.cspMessageConfigFactory = Objects.requireNonNull(cspMessageConfigFactory);
        initDefaultMessageConfigs();
    }

    @Override
    public <T> void registerSerializationProcessor(Class<T> clazz, ICspClassSerializationProcessor<T> classProcessor)
    {
        processorRegistry.registerClassProcessor(clazz, classProcessor);
    }

    @Override
    public void unregisterSerializationProcessor(Class<?> clazz)
    {
        processorRegistry.unregisterClassProcessor(clazz);
    }

    @Override
    public void unregisterTypeSerializationProcessor(CspTypeToken<?> cspTypeToken)
    {
        processorRegistry.unregisterTypeProcessor(cspTypeToken.getAnnotatedType());
    }

    @Override
    public void setDefaultBufferConfig(ISerializationBufferConfig config)
    {
        defaultBufferConfig = Objects.requireNonNull(config);
    }

    @Override
    public void setDefaultMessageConfig(ICspMessageConfig config)
    {
        defaultMessageConfig = Objects.requireNonNull(config);
    }

    @Override
    public void setDefaultDataMessageConfigExtension(ICspDataMessageConfigExtension config)
    {
        defaultDataMessageConfigExtension = Objects.requireNonNull(config);
    }

    @Override
    public ISerializationBufferConfigFactory getSerializationBufferConfigFactory()
    {
        return serializationBufferConfigFactory;
    }

    @Override
    public ICspMessageConfigFactory getCspMessageConfigFactory()
    {
        return cspMessageConfigFactory;
    }

    @Override
    public <T extends ICspVersionable> ICspDataMessage<T> serializeData(ICspVersionable struct, Class<T> clazz)
    {
        return serializationWorkflow.serializeDataMessage(Objects.requireNonNull(struct), Objects.requireNonNull(clazz),
            defaultBufferConfig, defaultMessageConfig, defaultDataMessageConfigExtension);
    }

    @Override
    public <T extends ICspVersionable> ICspDataMessage<T> serializeData(ICspVersionable struct, Class<T> clazz,
        @Nullable ISerializationBufferConfig customBufferConfig, @Nullable ICspMessageConfig customMessageConfig,
        @Nullable ICspDataMessageConfigExtension customDataMessageConfigExtension)
    {
        return serializationWorkflow.serializeDataMessage(Objects.requireNonNull(struct), Objects.requireNonNull(clazz),
            customBufferConfig != null ?  customBufferConfig : defaultBufferConfig,
            customMessageConfig != null ? customMessageConfig : defaultMessageConfig,
            customDataMessageConfigExtension != null ? customDataMessageConfigExtension : defaultDataMessageConfigExtension);
    }

    private void initDefaultMessageConfigs()
    {
        defaultMessageConfig = cspMessageConfigFactory.createCspMessageCommonConfig(null, null);
        defaultDataMessageConfigExtension = cspMessageConfigFactory.createCspDataMessageConfigExtension(null, null);
    }
}
