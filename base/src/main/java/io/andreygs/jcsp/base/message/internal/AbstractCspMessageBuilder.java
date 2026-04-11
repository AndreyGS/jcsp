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

import io.andreygs.jcsp.base.message.ICspMessageBuilder;
import io.andreygs.jcsp.base.processing.internal.ISerializationWorkflow;
import io.andreygs.jcsp.base.types.CspCommonFlag;
import io.andreygs.jcsp.base.types.CspProtocolVersion;
import io.andreygs.jcsp.base.common.internal.ArgumentChecker;
import io.andreygs.jcsp.base.processing.buffer.IBufferResizeStrategy;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * TODO: place description here
 */
abstract class AbstractCspMessageBuilder implements ICspMessageBuilder
{
    private final ISerializationWorkflow serializationWorkflow;
    private @Nullable Integer initialBufferCapacity;
    private @Nullable Boolean directBuffer;
    private @Nullable IBufferResizeStrategy bufferResizeStrategy;
    private @Nullable CspProtocolVersion cspProtocolVersion;
    private @Nullable Set<CspCommonFlag> cspCommonFlags;

    AbstractCspMessageBuilder(ISerializationWorkflow serializationWorkflow)
    {
        this.serializationWorkflow = serializationWorkflow;
    }

    @Override
    public ICspMessageBuilder setBufferInitialCapacity(int initialBufferCapacity)
        throws IllegalArgumentException
    {
        if (initialBufferCapacity < 0)
        {
            throw new IllegalArgumentException("Buffer capacity can not be negative!");
        }
        this.initialBufferCapacity = initialBufferCapacity;
        return this;
    }

    @Override
    public ICspMessageBuilder setDirectBuffer(boolean directBuffer)
    {
        this.directBuffer = directBuffer;
        return this;
    }

    @Override
    public ICspMessageBuilder setBufferResizeStrategy(IBufferResizeStrategy bufferResizeStrategy)
        throws IllegalArgumentException
    {
        ArgumentChecker.nonNull(bufferResizeStrategy);
        this.bufferResizeStrategy = bufferResizeStrategy;
        return this;
    }

    @Override
    public ICspMessageBuilder setCspProtocolVersion(CspProtocolVersion cspProtocolVersion)
        throws IllegalArgumentException
    {
        ArgumentChecker.nonNull(cspProtocolVersion);
        this.cspProtocolVersion = cspProtocolVersion;
        return this;
    }

    @Override
    public ICspMessageBuilder setCspCommonFlags(Set<CspCommonFlag> cspCommonFlags)
        throws IllegalArgumentException
    {
        ArgumentChecker.nonNull(cspCommonFlags);
        this.cspCommonFlags = Set.copyOf(cspCommonFlags);
        return this;
    }

    protected ISerializationWorkflow getSerializationWorkflow()
    {
        return serializationWorkflow;
    }

    protected @Nullable Integer getInitialBufferCapacity()
    {
        return initialBufferCapacity;
    }

    protected @Nullable Boolean getDirectBuffer()
    {
        return directBuffer;
    }

    protected @Nullable IBufferResizeStrategy getBufferResizeStrategy()
    {
        return bufferResizeStrategy;
    }

    protected @Nullable CspProtocolVersion getCspProtocolVersion()
    {
        return cspProtocolVersion;
    }

    protected @Nullable Set<CspCommonFlag> getCspCommonFlags()
    {
        return cspCommonFlags;
    }
}
