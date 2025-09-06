package io.andreygs.jcsp;

import io.andreygs.jcsp_base.types.api.CspCommonFlags;
import io.andreygs.jcsp_base.types.api.CspMessageType;
import io.andreygs.jcsp_base.types.api.CspProtocolVersion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class Simple
{
    public static void main(String[] args)
    {
        Locale.setDefault(new Locale("ru", "RU"));
        var x1 = CspCommonFlags.stringDescription(new ArrayList<CspCommonFlags>(), false, false);
        var x2 = CspCommonFlags.stringDescription(new ArrayList<CspCommonFlags>(), false, true);
        var x3 = CspCommonFlags.stringDescription(new ArrayList<CspCommonFlags>(), true, false);
        var x4 = CspCommonFlags.stringDescription(new ArrayList<CspCommonFlags>(), true, true);
        var x5 = CspCommonFlags.stringDescription(Arrays.asList(CspCommonFlags.BIG_ENDIAN, CspCommonFlags.ENDIANNESS_DIFFERENCE), false, false);
        var x6 = CspCommonFlags.stringDescription(Arrays.asList(CspCommonFlags.BIG_ENDIAN, CspCommonFlags.ENDIANNESS_DIFFERENCE), false, true);
        var x7 = CspCommonFlags.stringDescription(Arrays.asList(CspCommonFlags.BIG_ENDIAN, CspCommonFlags.ENDIANNESS_DIFFERENCE), true, false);
        var x8 = CspCommonFlags.stringDescription(Arrays.asList(CspCommonFlags.BIG_ENDIAN, CspCommonFlags.ENDIANNESS_DIFFERENCE), true, true);
        String test = CspMessageType.STATUS.toString();
        CspMessageSerializationCommonContext commonContext  = new CspMessageSerializationCommonContext(
            CspProtocolVersion.CSP_VERSION_2);
        var buffer = commonContext.getBinaryData();
        buffer.write(1L);
        buffer.write((short)5);
        int[] x = new int[2];
        x[0] = 2;
        x[1] = 6;
        buffer.write(x);
        buffer.commitBuffer();

        var byteBuffer = buffer.getByteBuffer();
        while (byteBuffer.hasRemaining()) {
            byte b = byteBuffer.get();
            System.out.printf("%02X ", b);
        }
        System.out.println();
    }
}
