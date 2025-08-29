package io.andreygs.jcsp_base.context.internal;

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
