package io.andreygs.jcsp_base.context.internal;

import io.andreygs.jcsp_base.types.api.CspCommonFlags;
import io.andreygs.jcsp_base.types.api.CspMessageType;
import io.andreygs.jcsp_base.types.api.CspProtocolVersion;

import java.nio.ByteBuffer;
import java.util.List;

public class CspMessageSerializationCommonContext
    extends AbstractCspMessageCommon
    implements ICspMessageSerializationCommonContext
{
    private final CspByteBuffer serializationBuffer;

    public CspMessageSerializationCommonContext(CspProtocolVersion cspProtocolVersion, CspMessageType cspMessageType
        , List<CspCommonFlags> cspCommonFlags)
    {
        super(cspProtocolVersion, cspMessageType, cspCommonFlags);
        serializationBuffer = new CspByteBuffer();
    }

    @Override
    public ByteBuffer getBinaryData()
    {
        return serializationBuffer.getByteBuffer();
    }

    @Override
    public CspByteBuffer getSerializationBuffer()
    {
        return serializationBuffer;
    }
}
