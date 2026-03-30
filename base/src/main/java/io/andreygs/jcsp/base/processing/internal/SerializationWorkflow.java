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

package io.andreygs.jcsp.base.processing.internal;

import io.andreygs.jcsp.base.message.ICspDataMessage;
import io.andreygs.jcsp.base.processing.ICspGeneralSerializationProcessor;
import io.andreygs.jcsp.base.processing.ICspDataProcessorRegistrar;
import io.andreygs.jcsp.base.processing.buffer.internal.ICspSerializationBuffer;
import io.andreygs.jcsp.base.processing.buffer.internal.ICspSerializationBufferFactory;
import io.andreygs.jcsp.base.processing.ICspDataSerializationProcessor;
import io.andreygs.jcsp.base.processing.state.internal.ICspDataProcessingState;
import io.andreygs.jcsp.base.processing.state.internal.ICspProcessingStateFactory;
import io.andreygs.jcsp.base.types.CspCommonFlag;
import io.andreygs.jcsp.base.types.CspDataFlag;
import io.andreygs.jcsp.base.types.ICspInterfaceVersion;
import io.andreygs.jcsp.base.types.CspProtocolVersion;
import io.andreygs.jcsp.base.types.ICspVersionable;
import io.andreygs.jcsp.base.processing.buffer.IBufferResizeStrategy;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * The sole implementation of {@link ISerializationWorkflow}.
 */
public class SerializationWorkflow
    implements ISerializationWorkflow
{
    private static final CspProtocolVersion DEFAULT_CSP_PROTOCOL_VERSION = CspProtocolVersion.latestVersion();
    private static final Set<CspCommonFlag> DEFAULT_CSP_COMMON_FLAGS = Set.of(CspCommonFlag.BIG_ENDIAN);
    private static final Set<CspDataFlag> DEFAULT_CSP_DATA_FLAGS = Set.of(CspDataFlag.ALLOW_UNMANAGED_POINTERS);

    private final ICspGeneralSerializationProcessor cspGeneralSerializationProcessor;
    private final ICspSerializationBufferFactory cspSerializationBufferFactory;
    private final ICspProcessingStateFactory<ICspGeneralSerializationProcessor, ICspSerializationBuffer> cspSerializationStateFactory;

    public SerializationWorkflow(ICspGeneralSerializationProcessor cspGeneralSerializationProcessor,
                                 ICspSerializationBufferFactory cspSerializationBufferFactory,
                                 ICspProcessingStateFactory<ICspGeneralSerializationProcessor, ICspSerializationBuffer> cspSerializationStateFactory)
    {
        this.cspGeneralSerializationProcessor = cspGeneralSerializationProcessor;
        this.cspSerializationBufferFactory = cspSerializationBufferFactory;
        this.cspSerializationStateFactory = cspSerializationStateFactory;
    }

    @Override
    public ICspDataMessage serializeDataMessage(
        @Nullable Integer initialBufferCapacity,
        @Nullable Boolean directBuffer,
        @Nullable IBufferResizeStrategy bufferResizeStrategy,
        @Nullable CspProtocolVersion cspProtocolVersion,
        @Nullable Set<CspCommonFlag> cspCommonFlags,
        ICspDataProcessorRegistrar<ICspDataSerializationProcessor> cspSerializationProcessorRegistrar,
        ICspVersionable cspVersionable,
        @Nullable ICspInterfaceVersion cspInterfaceVersion,
        @Nullable Set<CspDataFlag> cspDataFlags)
    {
        ICspSerializationBuffer cspSerializationBuffer =
            cspSerializationBufferFactory.createCspSerializationBuffer(initialBufferCapacity, directBuffer, bufferResizeStrategy);

        // TODO construction of message should be made later (right before message body serialization).
        ICspDataProcessingState<ICspGeneralSerializationProcessor, ICspSerializationBuffer, ICspDataSerializationProcessor, Object, Integer>
            cspSerializationDataMessage
                = cspSerializationStateFactory.createDataMessageState(cspGeneralSerializationProcessor,
                                                                      cspSerializationBuffer,
                                                                      cspProtocolVersion == null ? DEFAULT_CSP_PROTOCOL_VERSION : cspProtocolVersion,
                                                                      cspCommonFlags == null ? DEFAULT_CSP_COMMON_FLAGS : cspCommonFlags,
                                                                      cspSerializationProcessorRegistrar,
                                                                      null,
                                                                      cspVersionable,
                                                                      cspVersionable.getClass(),
                                                                      cspInterfaceVersion == null ? cspVersionable.getInterfaceVersion() : cspInterfaceVersion,
                                                                      cspDataFlags == null ? DEFAULT_CSP_DATA_FLAGS : cspDataFlags);
        return cspSerializationDataMessage;
    }
}
