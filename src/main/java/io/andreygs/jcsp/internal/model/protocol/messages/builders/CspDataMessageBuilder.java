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

package io.andreygs.jcsp.internal.model.protocol.messages.builders;

import io.andreygs.jcsp.api.model.protocol.messages.ICspDataMessage;
import io.andreygs.jcsp.api.model.protocol.messages.builders.ICspDataMessageBuilder;
import io.andreygs.jcsp.internal.processing.data.ICspProcessorRegistry;
import io.andreygs.jcsp.api.processing.data.ICspClassSerializationProcessor;
import io.andreygs.jcsp.internal.processing.ISerializationWorkflow;
import io.andreygs.jcsp.internal.processing.data.types.ICspTypeSerializationProcessor;
import io.andreygs.jcsp.api.model.protocol.CspCommonFlag;
import io.andreygs.jcsp.api.model.protocol.CspDataFlag;
import io.andreygs.jcsp.api.model.protocol.ICspInterfaceVersion;
import io.andreygs.jcsp.api.model.protocol.CspProtocolVersion;
import io.andreygs.jcsp.api.model.protocol.ICspVersionable;
import io.andreygs.jcsp.internal.model.utils.ArgumentChecker;
import io.andreygs.jcsp.api.model.buffers.IBufferResizeStrategy;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * TODO: place description here
 */
public final class CspDataMessageBuilder extends AbstractCspMessageBuilder
    implements ICspDataMessageBuilder
{
    private final ICspProcessorRegistry<ICspClassSerializationProcessor<?>, ICspTypeSerializationProcessor<?>>
        cspSerializationProcessorRegistry;
    private @Nullable ICspInterfaceVersion cspInterfaceVersion;
    private @Nullable Set<CspDataFlag> cspDataFlags;

    public CspDataMessageBuilder(ISerializationWorkflow serializationWorkflow,
        ICspProcessorRegistry<ICspClassSerializationProcessor<?>, ICspTypeSerializationProcessor<?>> cspSerializationProcessorRegistry)
    {
        super(serializationWorkflow);
        this.cspSerializationProcessorRegistry = cspSerializationProcessorRegistry;
    }

    @Override
    public ICspDataMessageBuilder setBufferInitialCapacity(int initialBufferCapacity)
        throws IllegalArgumentException
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
        throws IllegalArgumentException
    {
        super.setBufferResizeStrategy(bufferResizeStrategy);
        return this;
    }

    @Override
    public ICspDataMessageBuilder setCspProtocolVersion(CspProtocolVersion cspProtocolVersion)
        throws IllegalArgumentException
    {
        super.setCspProtocolVersion(cspProtocolVersion);
        return this;
    }

    @Override
    public ICspDataMessageBuilder setCspCommonFlags(Set<CspCommonFlag> cspCommonFlags)
        throws IllegalArgumentException
    {
        super.setCspCommonFlags(cspCommonFlags);
        return this;
    }

    @Override
    public ICspDataMessageBuilder setInterfaceVersion(ICspInterfaceVersion cspInterfaceVersion)
        throws IllegalArgumentException
    {
        ArgumentChecker.nonNull(cspInterfaceVersion);
        this.cspInterfaceVersion = cspInterfaceVersion;
        return this;
    }

    @Override
    public ICspDataMessageBuilder setCspDataFlags(Set<CspDataFlag> cspDataFlags)
        throws IllegalArgumentException
    {
        ArgumentChecker.nonNull(cspDataFlags);
        this.cspDataFlags = Set.copyOf(cspDataFlags);
        return this;
    }

    @Override
    public ICspDataMessage serialize(ICspVersionable cspVersionable)
        throws IllegalArgumentException
    {
        ArgumentChecker.nonNull(cspVersionable);
        return getSerializationWorkflow().serializeDataMessage(
            getInitialBufferCapacity(), getDirectBuffer(), getBufferResizeStrategy(), getCspProtocolVersion(),
            getCspCommonFlags(), cspSerializationProcessorRegistry, cspVersionable, cspInterfaceVersion, cspDataFlags);
    }
}
