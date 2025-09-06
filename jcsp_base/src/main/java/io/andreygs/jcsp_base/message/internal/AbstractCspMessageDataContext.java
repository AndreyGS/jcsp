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

package io.andreygs.jcsp_base.message.internal;

import io.andreygs.jcsp_base.message.api.ICspMessageDataContext;
import io.andreygs.jcsp_base.types.api.*;

import java.util.List;
import java.util.UUID;

/**
 * TODO: place description here
 */
public abstract class AbstractCspMessageDataContext
    extends AbstractCspMessageCommonContext
    implements ICspMessageDataContext
{
    private final UUID cspId;
    private final CspInterfaceVersion cspInterfaceVersion;
    private final List<CspDataFlags> cspDataFlags;

    public AbstractCspMessageDataContext(CspProtocolVersion cspProtocolVersion, CspMessageType cspMessageType
        , List<CspCommonFlags> cspCommonFlags, UUID cspId, CspInterfaceVersion cspInterfaceVersion
        , List<CspDataFlags> cspDataFlags)
    {
        super(cspProtocolVersion, cspMessageType, cspCommonFlags);
        this.cspId = cspId;
        this.cspInterfaceVersion = cspInterfaceVersion;
        this.cspDataFlags = cspDataFlags;
    }

    @Override
    public UUID getStructUuid()
    {
        return cspId;
    }

    @Override
    public CspInterfaceVersion getInterfaceVersion()
    {
        return cspInterfaceVersion;
    }

    @Override
    public List<CspDataFlags> getCspDataFlags()
    {
        return cspDataFlags;
    }
}
