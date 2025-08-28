package io.andreygs.jcsp_base.context;

import io.andreygs.jcsp_base.internal.context.CspSerializationBuffer;

public class CspMessageSerializationCommonContext
{
    private final CspSerializationBuffer binaryData;

    public CspMessageSerializationCommonContext()
    {
        binaryData = new CspSerializationBuffer();
    }

    public CspSerializationBuffer getBinaryData()
    {
        return binaryData;
    }
}
