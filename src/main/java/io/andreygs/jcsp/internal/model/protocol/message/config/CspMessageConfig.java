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

package io.andreygs.jcsp.internal.model.protocol.message.config;

import io.andreygs.jcsp.api.model.protocol.CspCommonFlag;
import io.andreygs.jcsp.api.model.protocol.CspProtocolVersion;
import io.andreygs.jcsp.api.model.protocol.message.config.ICspMessageConfig;
import io.andreygs.jcsp.api.model.protocol.utils.CspFlagUtils;

import java.util.Objects;
import java.util.Set;

/**
 * TODO: place description here
 */
public final class CspMessageConfig implements ICspMessageConfig
{
    private final CspProtocolVersion cspProtocolVersion;
    private final boolean bitness32;
    private final boolean bigEndian;

    public CspMessageConfig(CspProtocolVersion cspProtocolVersion, Set<CspCommonFlag> cspCommonFlags)
    {
        this.cspProtocolVersion = Objects.requireNonNull(cspProtocolVersion);
        int flagMask = CspFlagUtils.calculateFlagMask(cspCommonFlags);
        bitness32 = CspFlagUtils.isFlagSet(flagMask, CspCommonFlag.BITNESS_32);
        bigEndian = CspFlagUtils.isFlagSet(flagMask, CspCommonFlag.BIG_ENDIAN);
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
}
