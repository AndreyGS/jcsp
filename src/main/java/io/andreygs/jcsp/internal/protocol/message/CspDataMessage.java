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

package io.andreygs.jcsp.internal.protocol.message;

import io.andreygs.jcsp.api.protocol.message.ICspDataMessage;
import io.andreygs.jcsp.api.protocol.message.context.ICspDataMessageContextExtension;
import io.andreygs.jcsp.api.protocol.message.context.ICspMessageContext;
import io.andreygs.jcsp.api.protocol.CspMessageType;
import io.andreygs.jcsp.api.protocol.ICspVersionable;

import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * TODO: place description here
 */
public final class CspDataMessage<T extends ICspVersionable>
    implements ICspDataMessage<T>
{
    private final ByteBuffer buffer;
    private final ICspMessageContext messageContext;
    private final T struct;
    private final ICspDataMessageContextExtension dataDataMessageContextExtension;

    public CspDataMessage(ByteBuffer buffer,
        ICspMessageContext messageContext, T struct, ICspDataMessageContextExtension dataDataMessageContextExtension)
    {
        this.buffer = Objects.requireNonNull(buffer);
        this.messageContext = Objects.requireNonNull(messageContext);
        this.struct = Objects.requireNonNull(struct);
        this.dataDataMessageContextExtension = Objects.requireNonNull(dataDataMessageContextExtension);
    }

    @Override
    public CspMessageType getCspMessageType()
    {
        return CspMessageType.DATA;
    }


    @Override
    public ByteBuffer getBuffer()
    {
        return buffer;
    }

    @Override
    public ICspMessageContext getMessageContext()
    {
        return messageContext;
    }

    @Override
    public T getStruct()
    {
        return struct;
    }

    @Override
    public ICspDataMessageContextExtension getMessageDataContext()
    {
        return dataDataMessageContextExtension;
    }
}
