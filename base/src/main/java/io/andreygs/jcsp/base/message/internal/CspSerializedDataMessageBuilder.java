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

package io.andreygs.jcsp.base.message.internal;

import io.andreygs.jcsp.base.message.api.ICspDataMessage;
import io.andreygs.jcsp.base.message.api.ICspSerializedDataMessageBuilder;
import io.andreygs.jcsp.base.processing.internal.Serializer;
import io.andreygs.jcsp.base.types.api.CspCommonFlags;
import io.andreygs.jcsp.base.types.api.CspDataFlags;
import io.andreygs.jcsp.base.types.api.CspInterfaceVersion;
import io.andreygs.jcsp.base.types.api.CspProtocolVersion;
import io.andreygs.jcsp.base.types.api.ICspSerializable;
import io.andreygs.jcsp.base.utils.api.IBufferResizeStrategy;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * TODO: place description here
 */
public class CspSerializedDataMessageBuilder extends AbstractCspSerializedMessageCommonBuilder
    implements ICspSerializedDataMessageBuilder
{
    private @Nullable CspInterfaceVersion cspInterfaceVersion;
    private @Nullable List<CspDataFlags> cspDataFlags;

    @Override
    public ICspSerializedDataMessageBuilder setBufferInitialCapacity(int initialBufferCapacity)
    {
        super.setBufferInitialCapacity(initialBufferCapacity);
        return this;
    }

    @Override
    public ICspSerializedDataMessageBuilder setDirectBuffer(boolean directBuffer)
    {
        super.setDirectBuffer(directBuffer);
        return this;
    }

    @Override
    public ICspSerializedDataMessageBuilder setBufferResizeStrategy(IBufferResizeStrategy bufferResizeStrategy)
    {
        super.setBufferResizeStrategy(bufferResizeStrategy);
        return this;
    }

    @Override
    public ICspSerializedDataMessageBuilder setCspProtocolVersion(CspProtocolVersion cspProtocolVersion)
    {
        super.setCspProtocolVersion(cspProtocolVersion);
        return this;
    }

    @Override
    public ICspSerializedDataMessageBuilder setCspCommonFlags(List<CspCommonFlags> cspCommonFlags)
    {
        super.setCspCommonFlags(cspCommonFlags);
        return this;
    }

    @Override
    public ICspSerializedDataMessageBuilder setInterfaceVersion(CspInterfaceVersion cspInterfaceVersion)
    {
        this.cspInterfaceVersion = cspInterfaceVersion;
        return this;
    }

    @Override
    public ICspSerializedDataMessageBuilder setCspDataFlags(List<CspDataFlags> cspDataFlags)
    {
        this.cspDataFlags = cspDataFlags;
        return this;
    }

    @Override
    public ICspDataMessage serialize(ICspSerializable cspSerializable)
    {
        return Serializer.serializeDataMessage(getInitialBufferCapacity().get(), getDirectBuffer().get(),
                                               getBufferResizeStrategy().get(), getCspProtocolVersion().get(),
                                               getCspCommonFlags().get(), cspInterfaceVersion, cspDataFlags,
                                               cspSerializable);
    }
}
