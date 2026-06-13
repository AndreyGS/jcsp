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

package io.andreygs.jcsp.internal.protocol.message.context;

import io.andreygs.jcsp.api.protocol.CspProtocolVersion;
import io.andreygs.jcsp.api.protocol.message.context.ICspMessageContext;

import java.util.Objects;

/**
 * TODO: place description here
 */
public final class CspMessageContext
    implements ICspMessageContext
{
    private final CspProtocolVersion cspProtocolVersion;
    private final boolean bitness32;
    private final boolean bigEndian;
    private final boolean endiannessDifference;

    public CspMessageContext(CspProtocolVersion cspProtocolVersion, boolean bitness32, boolean bigEndian,
        boolean endiannessDifference)
    {
        this.cspProtocolVersion = Objects.requireNonNull(cspProtocolVersion);
        this.bitness32 = bitness32;
        this.bigEndian = bigEndian;
        this.endiannessDifference = endiannessDifference;
    }

    @Override
    public CspProtocolVersion getCspProtocolVersion()
    {
        return cspProtocolVersion;
    }

    @Override
    public boolean isBitness32()
    {
        return bitness32;
    }

    @Override
    public boolean isBigEndian()
    {
        return bigEndian;
    }

    @Override
    public boolean isEndiannessDifference()
    {
        return endiannessDifference;
    }
}
