package io.andreygs.jcsp;

import io.andreygs.jcsp_base.context.CspMessageSerializationCommonContext;

import java.nio.ByteOrder;

public class Simple
{
    public static void main(String[] args)
    {
        CspMessageSerializationCommonContext commonContext  = new CspMessageSerializationCommonContext();
        var buffer = commonContext.getBinaryData();
        buffer.write(1L);
        buffer.write((short)5);
        buffer.endiannessToWriteOperations(ByteOrder.BIG_ENDIAN);
        int[] x = new int[2];
        x[0] = 2;
        x[1] = 6;
        buffer.write(x);
        buffer.commitMessage();

        var byteBuffer = buffer.getByteBuffer();
        while (byteBuffer.hasRemaining()) {
            byte b = byteBuffer.get();
            System.out.printf("%02X ", b); // вывод байта как hex
        }
        System.out.println();
    }
}
