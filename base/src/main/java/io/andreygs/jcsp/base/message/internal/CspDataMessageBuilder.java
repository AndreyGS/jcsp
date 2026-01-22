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

import io.andreygs.jcsp.base.message.ICspDataMessage;
import io.andreygs.jcsp.base.message.ICspDataMessageBuilder;
import io.andreygs.jcsp.base.processing.ICspProcessorProvider;
import io.andreygs.jcsp.base.processing.ICspSerializationProcessor;
import io.andreygs.jcsp.base.processing.internal.Serializer;
import io.andreygs.jcsp.base.types.CspCommonFlags;
import io.andreygs.jcsp.base.types.CspDataFlags;
import io.andreygs.jcsp.base.types.ICspInterfaceVersion;
import io.andreygs.jcsp.base.types.CspProtocolVersion;
import io.andreygs.jcsp.base.types.ICspSerializable;
import io.andreygs.jcsp.base.utils.IBufferResizeStrategy;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * TODO: place description here
 */
class CspDataMessageBuilder extends AbstractCspMessageBuilder
    implements ICspDataMessageBuilder
{
    private @Nullable ICspInterfaceVersion cspInterfaceVersion;
    private @Nullable List<CspDataFlags> cspDataFlags;

    CspDataMessageBuilder(ICspProcessorProvider<ICspSerializationProcessor> cspSerializationProcessorProvider)
    {
        super(cspSerializationProcessorProvider);
    }

    @Override
    public ICspDataMessageBuilder setBufferInitialCapacity(int initialBufferCapacity)
    {
        super.setBufferInitialCapacity(initialBufferCapacity);
        return this;
    }

    @Override
    public ICspDataMessageBuilder setDirectBuffer(boolean directBuffer)
    {
        super.setDirectBuffer(directBuffer);
        return this;
    }

    @Override
    public ICspDataMessageBuilder setBufferResizeStrategy(IBufferResizeStrategy bufferResizeStrategy)
    {
        super.setBufferResizeStrategy(bufferResizeStrategy);
        return this;
    }

    @Override
    public ICspDataMessageBuilder setCspProtocolVersion(CspProtocolVersion cspProtocolVersion)
    {
        super.setCspProtocolVersion(cspProtocolVersion);
        return this;
    }

    @Override
    public ICspDataMessageBuilder setCspCommonFlags(List<CspCommonFlags> cspCommonFlags)
    {
        super.setCspCommonFlags(cspCommonFlags);
        return this;
    }

    @Override
    public ICspDataMessageBuilder setInterfaceVersion(ICspInterfaceVersion cspInterfaceVersion)
    {
        this.cspInterfaceVersion = cspInterfaceVersion;
        return this;
    }

    @Override
    public ICspDataMessageBuilder setCspDataFlags(List<CspDataFlags> cspDataFlags)
    {
        this.cspDataFlags = cspDataFlags;
        return this;
    }

    @Override
    public ICspDataMessage serialize(ICspSerializable cspSerializable)
    {
        return Serializer.serializeDataMessage(getCspSerializationProcessorProvider(), getInitialBufferCapacity(), getDirectBuffer(),
                                               getBufferResizeStrategy(), getCspProtocolVersion(),
                                               getCspCommonFlags(), cspInterfaceVersion, cspDataFlags,
                                               cspSerializable);
    }
}
