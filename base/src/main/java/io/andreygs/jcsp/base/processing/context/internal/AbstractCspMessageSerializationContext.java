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

package io.andreygs.jcsp.base.processing.context.internal;

import io.andreygs.jcsp.base.processing.ICspGeneralSerializationProcessor;
import io.andreygs.jcsp.base.processing.buffer.internal.ICspSerializationBuffer;
import io.andreygs.jcsp.base.processing.ICspProcessorProvider;
import io.andreygs.jcsp.base.processing.ICspSerializationProcessor;
import io.andreygs.jcsp.base.types.CspCommonFlags;
import io.andreygs.jcsp.base.types.CspProtocolVersion;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * TODO: place description here
 */
sealed abstract class AbstractCspMessageSerializationContext
    implements ICspMessageSerializationContext
    permits CspDataMessageSerializationContext, CspStatusMessageSerializationContext
{
    private final ICspGeneralSerializationProcessor cspGeneralSerializationProcessor;
    private final ICspProcessorProvider<ICspSerializationProcessor> cspSerializationProcessorProvider;
    private final ICspSerializationBuffer cspSerializationBuffer;
    private final CspProtocolVersion cspProtocolVersion;
    private final List<CspCommonFlags> cspCommonFlags;

    public AbstractCspMessageSerializationContext(ICspGeneralSerializationProcessor cspGeneralSerializationProcessor,
                                                  ICspProcessorProvider<ICspSerializationProcessor> cspSerializationProcessorProvider,
                                                  ICspSerializationBuffer cspSerializationBuffer,
                                                  CspProtocolVersion cspProtocolVersion,
                                                  List<CspCommonFlags> cspCommonFlags)
    {
        this.cspGeneralSerializationProcessor = cspGeneralSerializationProcessor;
        this.cspSerializationProcessorProvider = cspSerializationProcessorProvider;
        this.cspSerializationBuffer = cspSerializationBuffer;
        this.cspProtocolVersion = cspProtocolVersion;
        this.cspCommonFlags = cspCommonFlags;
    }

    @Override
    public ICspGeneralSerializationProcessor getCspGeneralSerializationProcessor()
    {
        return cspGeneralSerializationProcessor;
    }

    @Override
    public ICspProcessorProvider<ICspSerializationProcessor> getCspSerializationProcessorProvider()
    {
        return cspSerializationProcessorProvider;
    }

    @Override
    public ByteBuffer getBinaryData()
    {
        return cspSerializationBuffer.getByteBuffer();
    }

    @Override
    public CspProtocolVersion getCspProtocolVersion()
    {
        return cspProtocolVersion;
    }

    @Override
    public List<CspCommonFlags> getCspCommonFlags()
    {
        return cspCommonFlags;
    }

    @Override
    public ICspSerializationBuffer getCspSerializationBuffer()
    {
        return cspSerializationBuffer;
    }
}
