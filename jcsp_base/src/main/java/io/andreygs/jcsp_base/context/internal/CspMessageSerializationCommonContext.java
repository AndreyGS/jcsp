package io.andreygs.jcsp_base.context.internal;

import io.andreygs.jcsp_base.context.api.ICspMessageCommonContext;
import io.andreygs.jcsp_base.types.api.CspCommonFlags;
import io.andreygs.jcsp_base.types.api.CspMessageType;
import io.andreygs.jcsp_base.types.api.CspProtocolVersion;

import java.util.List;

public class CspMessageSerializationCommonContext implements ICspMessageCommonContext
{
    private final CspSerializationBuffer binaryData;
    private final CspProtocolVersion cspProtocolVersion;

    public CspMessageSerializationCommonContext(CspProtocolVersion cspProtocolVersion)
    {
        binaryData = new CspSerializationBuffer();
        this.cspProtocolVersion = cspProtocolVersion;
    }

    public CspSerializationBuffer getBinaryData()
    {
        return binaryData;
    }

    @Override
    public CspProtocolVersion getCspProtocolVersion()
    {
        return null;
    }

    @Override
    public CspMessageType getCspMessageType()
    {
        return null;
    }

    @Override
    public List<CspCommonFlags> getCspCommonFlags()
    {
        return List.of();
    }
}
