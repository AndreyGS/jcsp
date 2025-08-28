package io.andreygs.jcsp_base.context;

import java.nio.ByteBuffer;

public class CspMessageSerializationCommonContext
{
    private CspSerializationBuffer binaryData;

    public CspMessageSerializationCommonContext()
    {
        binaryData = new CspSerializationBuffer();
    }

    public CspSerializationBuffer getBinaryData()
    {
        return binaryData;
    }
}
