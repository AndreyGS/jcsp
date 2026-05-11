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

package io.andreygs.jcsp.internal.processing;

import io.andreygs.jcsp.api.model.protocol.message.ICspDataMessage;
import io.andreygs.jcsp.api.processing.data.ICspSerializationProcessor;
import io.andreygs.jcsp.internal.model.buffer.ICspSerializationBuffer;
import io.andreygs.jcsp.internal.factory.model.processing.buffer.ICspSerializationBufferFactory;
import io.andreygs.jcsp.api.processing.data.ICspClassSerializationProcessor;
import io.andreygs.jcsp.internal.factory.model.message.ICspMessageFactory;
import io.andreygs.jcsp.internal.factory.processing.data.ICspSerializationProcessorFactory;
import io.andreygs.jcsp.internal.processing.data.ICspProcessorRegistry;
import io.andreygs.jcsp.internal.processing.data.type.ICspTypeSerializationProcessor;
import io.andreygs.jcsp.api.model.protocol.CspCommonFlag;
import io.andreygs.jcsp.api.model.protocol.CspDataFlag;
import io.andreygs.jcsp.api.model.protocol.ICspInterfaceVersion;
import io.andreygs.jcsp.api.model.protocol.CspProtocolVersion;
import io.andreygs.jcsp.api.model.protocol.ICspVersionable;
import io.andreygs.jcsp.api.model.buffer.IBufferResizeStrategy;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * The sole implementation of {@link ISerializationWorkflow}.
 */
public final class SerializationWorkflow
    implements ISerializationWorkflow
{
    private static final CspProtocolVersion DEFAULT_CSP_PROTOCOL_VERSION = CspProtocolVersion.latestVersion();
    private static final Set<CspCommonFlag> DEFAULT_CSP_COMMON_FLAGS = Set.of(CspCommonFlag.BIG_ENDIAN);
    private static final Set<CspDataFlag> DEFAULT_CSP_DATA_FLAGS = Set.of(CspDataFlag.ALLOW_UNMANAGED_POINTERS);

    private final ICspSerializationBufferFactory cspSerializationBufferFactory;
    private final ICspMessageFactory cspMessageFactory;
    private final ICspSerializationProcessorFactory cspDataGeneralSerializationProcessorFactory;

    public SerializationWorkflow(ICspSerializationBufferFactory cspSerializationBufferFactory,
                                 ICspMessageFactory cspMessageFactory,
                                 ICspSerializationProcessorFactory cspDataGeneralSerializationProcessorFactory)
    {
        this.cspSerializationBufferFactory = cspSerializationBufferFactory;
        this.cspMessageFactory = cspMessageFactory;
        this.cspDataGeneralSerializationProcessorFactory = cspDataGeneralSerializationProcessorFactory;
    }

    @Override
    public ICspDataMessage serializeDataMessage(
        @Nullable Integer initialBufferCapacity,
        @Nullable Boolean directBuffer,
        @Nullable IBufferResizeStrategy bufferResizeStrategy,
        @Nullable CspProtocolVersion cspProtocolVersion,
        @Nullable Set<CspCommonFlag> cspCommonFlags,
        ICspProcessorRegistry<ICspClassSerializationProcessor<?>, ICspTypeSerializationProcessor<?>> cspProcessorRegistry,
        ICspVersionable cspVersionable,
        @Nullable ICspInterfaceVersion cspInterfaceVersion,
        @Nullable Set<CspDataFlag> cspDataFlags)
    {
        ICspSerializationBuffer cspSerializationBuffer =
            cspSerializationBufferFactory.createBuffer(initialBufferCapacity, directBuffer, bufferResizeStrategy);

        // TODO construction of message should be made later (right before message body serialization).
        ICspDataMessage message
                = cspMessageFactory.createCspDataMessage(cspSerializationBuffer,
                                                         cspProtocolVersion == null ? DEFAULT_CSP_PROTOCOL_VERSION : cspProtocolVersion,
                                                         cspCommonFlags == null ? DEFAULT_CSP_COMMON_FLAGS : cspCommonFlags,
                                                         cspVersionable,
                                                         cspVersionable.getClass(),
                                                         cspInterfaceVersion == null ? cspVersionable.getInterfaceVersion() : cspInterfaceVersion,
                                                         cspDataFlags == null ? DEFAULT_CSP_DATA_FLAGS : cspDataFlags);
        ICspSerializationProcessor processor =
            cspDataGeneralSerializationProcessorFactory.createGeneralSerializationProcessor(
                cspSerializationBuffer, cspProcessorRegistry, message);
        return message;
    }
}
