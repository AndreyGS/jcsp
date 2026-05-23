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

package io.andreygs.jcsp.internal.processing;

import io.andreygs.jcsp.api.model.buffer.dto.ISerializationBufferConfig;
import io.andreygs.jcsp.api.model.protocol.message.ICspDataMessage;
import io.andreygs.jcsp.api.model.protocol.message.config.ICspDataMessageConfigExtension;
import io.andreygs.jcsp.api.model.protocol.ICspVersionable;
import io.andreygs.jcsp.api.model.protocol.message.config.ICspMessageConfig;

/**
 * Workflow of CSP message serialization.
 * <p>
 * It init buffer, checks an arguments, writes the message contexts and message bodies.
 * <p>
 * It has immutable state
 */
public interface ICspSerializationWorkflow
{
    <T extends ICspVersionable> ICspDataMessage<T> serializeDataMessage(
        ICspVersionable value, Class<T> clazz,
        ISerializationBufferConfig bufferConfig, ICspMessageConfig messageConfig,
        ICspDataMessageConfigExtension dataMessageConfigExtension);
}
