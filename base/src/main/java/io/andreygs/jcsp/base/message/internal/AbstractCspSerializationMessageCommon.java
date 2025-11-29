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

import io.andreygs.jcsp.base.message.ICspMessageCommon;
import io.andreygs.jcsp.base.types.CspCommonFlags;
import io.andreygs.jcsp.base.types.CspMessageType;
import io.andreygs.jcsp.base.types.CspProtocolVersion;
import io.andreygs.jcsp.base.utils.IBufferResizeStrategy;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * TODO: place description here
 */
public abstract class AbstractCspSerializationMessageCommon implements ICspMessageCommon
{
    private final CspSerializationByteBuffer cspSerializationByteBuffer;
    private final CspProtocolVersion cspProtocolVersion;
    private final CspMessageType cspMessageType;
    private final List<CspCommonFlags> cspCommonFlags;

    public AbstractCspSerializationMessageCommon(@Nullable Integer initialBufferCapacity,
                                                 @Nullable Boolean directBuffer,
                                                 @Nullable IBufferResizeStrategy bufferResizeStrategy,
                                                 CspProtocolVersion cspProtocolVersion, CspMessageType cspMessageType,
                                                 List<CspCommonFlags> cspCommonFlags)
    {
        this.cspProtocolVersion = cspProtocolVersion;
        this.cspMessageType = cspMessageType;
        this.cspCommonFlags = cspCommonFlags;
        this.cspSerializationByteBuffer = CspSerializationByteBuffer.create(initialBufferCapacity, directBuffer,
                                                                            bufferResizeStrategy);
    }

    public CspSerializationByteBuffer getCspSerializationByteBuffer()
    {
        return cspSerializationByteBuffer;
    }

    @Override
    public ByteBuffer getBinaryData()
    {
        return cspSerializationByteBuffer.getByteBuffer();
    }

    @Override
    public CspProtocolVersion getCspProtocolVersion()
    {
        return cspProtocolVersion;
    }

    @Override
    public CspMessageType getCspMessageType()
    {
        return cspMessageType;
    }

    @Override
    public List<CspCommonFlags> getCspCommonFlags()
    {
        return cspCommonFlags;
    }
}
