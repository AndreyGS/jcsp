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

package io.andreygs.jcsp.internal.model.protocol.message.config.factory;

import io.andreygs.jcsp.api.model.protocol.CspCommonFlag;
import io.andreygs.jcsp.api.model.protocol.CspDataFlag;
import io.andreygs.jcsp.api.model.protocol.CspProtocolVersion;
import io.andreygs.jcsp.api.model.protocol.ICspInterfaceVersion;
import io.andreygs.jcsp.api.model.protocol.message.config.ICspDataMessageConfigExtension;
import io.andreygs.jcsp.api.model.protocol.message.config.ICspMessageConfig;
import io.andreygs.jcsp.api.model.protocol.message.config.factory.ICspMessageConfigFactory;
import io.andreygs.jcsp.api.model.protocol.utils.CspCommonFlagUtils;
import io.andreygs.jcsp.api.model.protocol.utils.CspDataFlagUtils;
import io.andreygs.jcsp.api.model.protocol.utils.CspInterfaceVersionUtils;
import io.andreygs.jcsp.api.model.protocol.utils.CspProtocolVersionUtils;
import io.andreygs.jcsp.internal.model.protocol.message.config.CspDataMessageConfigExtension;
import io.andreygs.jcsp.internal.model.protocol.message.config.CspMessageConfig;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * TODO: place description here
 */
public final class CspMessageConfigFactory implements ICspMessageConfigFactory
{
    @Override
    public ICspMessageConfig createCspMessageCommonConfig(@Nullable CspProtocolVersion cspProtocolVersion,
        @Nullable Set<CspCommonFlag> cspCommonFlags)
    {
        return new CspMessageConfig(
            cspProtocolVersion == null ? CspProtocolVersionUtils.DEFAULT_CSP_PROTOCOL_VERSION : cspProtocolVersion,
            cspCommonFlags == null ? CspCommonFlagUtils.DEFAULT_CSP_COMMON_FLAGS : cspCommonFlags);
    }

    @Override
    public ICspDataMessageConfigExtension createCspDataMessageConfigExtension(
        @Nullable ICspInterfaceVersion cspInterfaceVersion, @Nullable Set<CspDataFlag> cspDataFlags)
    {
        return new CspDataMessageConfigExtension(
            cspInterfaceVersion == null ? CspInterfaceVersionUtils.DEFAULT_CSP_INTERFACE_VERSION : cspInterfaceVersion,
            cspDataFlags == null ? CspDataFlagUtils.DEFAULT_CSP_DATA_FLAGS : cspDataFlags
        );
    }
}
