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

import io.andreygs.jcsp.base.message.ICspSerializedMessageCommonBuilder;
import io.andreygs.jcsp.base.types.CspCommonFlags;
import io.andreygs.jcsp.base.types.CspProtocolVersion;
import io.andreygs.jcsp.base.utils.IBufferResizeStrategy;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

/**
 * TODO: place description here
 */
public abstract class AbstractCspSerializedMessageCommonBuilder implements ICspSerializedMessageCommonBuilder
{
    private @Nullable Integer initialBufferCapacity;
    private @Nullable Boolean directBuffer;
    private @Nullable IBufferResizeStrategy bufferResizeStrategy;
    private @Nullable CspProtocolVersion cspProtocolVersion;
    private @Nullable List<CspCommonFlags> cspCommonFlags;

    @Override
    public ICspSerializedMessageCommonBuilder setBufferInitialCapacity(int initialBufferCapacity)
    {
        this.initialBufferCapacity = initialBufferCapacity;
        return this;
    }

    @Override
    public ICspSerializedMessageCommonBuilder setDirectBuffer(boolean directBuffer)
    {
        this.directBuffer = directBuffer;
        return this;
    }

    @Override
    public ICspSerializedMessageCommonBuilder setBufferResizeStrategy(IBufferResizeStrategy bufferResizeStrategy)
    {
        this.bufferResizeStrategy = bufferResizeStrategy;
        return this;
    }

    @Override
    public ICspSerializedMessageCommonBuilder setCspProtocolVersion(CspProtocolVersion cspProtocolVersion)
    {
        this.cspProtocolVersion = cspProtocolVersion;
        return this;
    }

    @Override
    public ICspSerializedMessageCommonBuilder setCspCommonFlags(List<CspCommonFlags> cspCommonFlags)
    {
        this.cspCommonFlags = cspCommonFlags;
        return this;
    }

    protected Optional<Integer> getInitialBufferCapacity()
    {
        return Optional.ofNullable(initialBufferCapacity);
    }

    protected Optional<Boolean> getDirectBuffer()
    {
        return Optional.ofNullable(directBuffer);
    }

    protected Optional<IBufferResizeStrategy> getBufferResizeStrategy()
    {
        return Optional.ofNullable(bufferResizeStrategy);
    }

    protected Optional<CspProtocolVersion> getCspProtocolVersion()
    {
        return Optional.ofNullable(cspProtocolVersion);
    }

    protected Optional<List<CspCommonFlags>> getCspCommonFlags()
    {
        return Optional.ofNullable(cspCommonFlags);
    }
}
