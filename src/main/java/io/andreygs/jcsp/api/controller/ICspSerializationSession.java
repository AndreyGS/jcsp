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

package io.andreygs.jcsp.api.controller;

import io.andreygs.jcsp.api.processing.buffer.dto.ISerializationBufferConfig;
import io.andreygs.jcsp.api.processing.buffer.dto.factory.ISerializationBufferConfigFactory;
import io.andreygs.jcsp.api.protocol.message.ICspDataMessage;
import io.andreygs.jcsp.api.protocol.message.config.ICspDataMessageConfigExtension;
import io.andreygs.jcsp.api.protocol.ICspVersionable;
import io.andreygs.jcsp.api.protocol.message.config.ICspMessageConfig;
import io.andreygs.jcsp.api.protocol.message.config.factory.ICspMessageConfigFactory;
import io.andreygs.jcsp.api.processing.data.type.CspTypeToken;
import io.andreygs.jcsp.api.processing.data.clazz.ICspClassSerializationProcessor;
import org.jetbrains.annotations.Nullable;

/**
 * TODO: place description here
 *
 * ALl implementations of current interface must be thread-safe.
 */
public interface ICspSerializationSession
{
    <T> void registerSerializationProcessor(Class<T> clazz, ICspClassSerializationProcessor<T> classProcessor);

    void unregisterSerializationProcessor(Class<?> clazz);

    void unregisterTypeSerializationProcessor(CspTypeToken<?> cspTypeToken);

    void setDefaultBufferConfig(ISerializationBufferConfig config);

    void setDefaultMessageConfig(ICspMessageConfig config);

    void setDefaultDataMessageConfigExtension(ICspDataMessageConfigExtension config);

    ISerializationBufferConfigFactory getSerializationBufferConfigFactory();

    ICspMessageConfigFactory getCspMessageConfigFactory();

    <T extends ICspVersionable> ICspDataMessage<T> serializeData(ICspVersionable struct, Class<T> clazz);

    <T extends ICspVersionable> ICspDataMessage<T> serializeData(ICspVersionable struct, Class<T> clazz,
        @Nullable ISerializationBufferConfig customBufferConfig, @Nullable ICspMessageConfig customMessageConfig,
        @Nullable ICspDataMessageConfigExtension customDataMessageConfigExtension);
}
