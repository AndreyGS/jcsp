package io.andreygs.jcsp_base.context.internal;

import io.andreygs.jcsp_base.context.api.ICspMessageCommonContext;
import io.andreygs.jcsp_base.types.api.CspMessageType;
import io.andreygs.jcsp_base.types.api.CspProtocolVersion;

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
    public boolean isBitness32()
    {
        return false;
    }

    @Override
    public boolean isBigEndianFormat()
    {
        return false;
    }

    @Override
    public boolean isEndiannessDifference()
    {
        return false;
    }

    @Override
    public boolean areProtocolVersionsNotMatch()
    {
        return false;
    }

    @Override
    public boolean areEndiannessNotMatch()
    {
        return false;
    }
}
