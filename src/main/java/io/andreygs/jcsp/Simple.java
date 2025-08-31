package io.andreygs.jcsp;

import io.andreygs.jcsp_base.context.internal.CspMessageSerializationCommonContext;
import io.andreygs.jcsp_base.types.api.CspMessageType;

import java.util.Locale;

public class Simple
{
    public static void main(String[] args)
    {
        Locale.setDefault(new Locale("ru", "RU"));
        String test = CspMessageType.STATUS.toString();
        CspMessageSerializationCommonContext commonContext  = new CspMessageSerializationCommonContext();
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
            System.out.printf("%02X ", b); // вывод байта как hex
        }
        System.out.println();
    }
}
