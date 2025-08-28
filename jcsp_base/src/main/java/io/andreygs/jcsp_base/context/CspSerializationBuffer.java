package io.andreygs.jcsp_base.context;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class CspSerializationBuffer
{
    private ByteBuffer byteBuffer;
    private int allocatedSize;
    private boolean directBuffer;
    private IBufferExpandingSizeStrategy bufferExpandingSizeStrategy;

    private static final int DEFAULT_ALLOCATION_SIZE = 256;

    public CspSerializationBuffer()
    {
        init(false);
    }

    public CspSerializationBuffer(boolean directBuffer)
    {
        init(directBuffer);
    }

    public boolean isDirectBuffer()
    {
        return directBuffer;
    }

    public ByteBuffer getByteBuffer()
    {
        return byteBuffer;
    }

    public void write(byte value)
    {
        expandBufferIfNeed(Byte.BYTES);
        byteBuffer.put(value);
    }

    public void write(short value)
    {
        expandBufferIfNeed(Short.BYTES);
        byteBuffer.putShort(value);
    }

    public void write(int value)
    {
        expandBufferIfNeed(Integer.BYTES);
        byteBuffer.putInt(value);
    }

    public void write(long value)
    {
        expandBufferIfNeed(Long.BYTES);
        byteBuffer.putLong(value);
    }

    public void write(char value)
    {
        expandBufferIfNeed(Character.BYTES);
        byteBuffer.putChar(value);
    }

    public void write(float value)
    {
        expandBufferIfNeed(Float.BYTES);
        byteBuffer.putFloat(value);
    }

    public void write(double value)
    {
        expandBufferIfNeed(Double.BYTES);
        byteBuffer.putDouble(value);
    }

    public void write(byte[] value)
    {
        expandBufferIfNeed(value.length * Byte.BYTES);
        byteBuffer.put(value);
    }

    public void write(short[] value)
    {
        int addingDataSize = value.length * Short.BYTES;
        expandBufferIfNeed(addingDataSize);
        byteBuffer.asShortBuffer().put(value);

        // We do not need to change limit of buffer every time we write to it.
        // The only thing that should be updated is the cursor position.
        // The same applies to other {@code write()} overloads with array parameter.
        byteBuffer.position(byteBuffer.position() + addingDataSize);
    }

    public void write(int[] value)
    {
        int addingDataSize = value.length * Integer.BYTES;
        expandBufferIfNeed(addingDataSize);
        byteBuffer.asIntBuffer().put(value);
        byteBuffer.position(byteBuffer.position() + addingDataSize);
    }

    public void write(long[] value)
    {
        int addingDataSize =value.length * Long.BYTES;
        expandBufferIfNeed(addingDataSize);
        byteBuffer.asLongBuffer().put(value);
        byteBuffer.position(byteBuffer.position() + addingDataSize);
    }

    public void write(char[] value)
    {
        int addingDataSize =value.length * Character.BYTES;
        expandBufferIfNeed(addingDataSize);
        byteBuffer.asCharBuffer().put(value);
        byteBuffer.position(byteBuffer.position() + addingDataSize);
    }

    public void write(float[] value)
    {
        int addingDataSize =value.length * Float.BYTES;
        expandBufferIfNeed(addingDataSize);
        byteBuffer.asFloatBuffer().put(value);
        byteBuffer.position(byteBuffer.position() + addingDataSize);
    }

    public void write(double[] value)
    {
        int addingDataSize =value.length * Double.BYTES;
        expandBufferIfNeed(addingDataSize);
        byteBuffer.asDoubleBuffer().put(value);
        byteBuffer.position(byteBuffer.position() + addingDataSize);
    }

    public void endiannessToWriteOperations(ByteOrder byteOrder)
    {
        byteBuffer.order(byteOrder);
    }

    public void setBufferExpandingSizeStrategy(IBufferExpandingSizeStrategy bufferExpandingSizeStrategy)
    {
        this.bufferExpandingSizeStrategy = bufferExpandingSizeStrategy;
    }

    public void commitMessage()
    {
        byteBuffer.flip();
    }

    public int size()
    {
        return byteBuffer.limit();
    }

    private void init(boolean directBuffer)
    {
        this.directBuffer = directBuffer;
        setByteBuffer(DEFAULT_ALLOCATION_SIZE);
        bufferExpandingSizeStrategy = new DoubleBufferExpandingSizeStrategy();
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
    }

    private void setByteBuffer(int allocationSize)
    {
        if (directBuffer)
        {
            byteBuffer = ByteBuffer.allocateDirect(allocationSize);
        }
        else
        {
            byteBuffer = ByteBuffer.allocate(allocationSize);
        }

        allocatedSize = allocationSize;
    }

    private void expandBufferIfNeed(int addingDataSize)
    {
        int minimumRequiredSize = byteBuffer.position() + addingDataSize;
        if (minimumRequiredSize > allocatedSize)
        {
            ByteBuffer oldByteBuffer = byteBuffer;
            int newAllocationSize = bufferExpandingSizeStrategy.evalExpandedSize(allocatedSize, minimumRequiredSize);
            setByteBuffer(newAllocationSize);
            oldByteBuffer.flip();
            byteBuffer.put(oldByteBuffer);
        }
    }

    public interface IBufferExpandingSizeStrategy
    {
        int evalExpandedSize(int currentlyAllocatedSize, int minimumRequiredSize);
    }

    public static class DoubleBufferExpandingSizeStrategy
        implements IBufferExpandingSizeStrategy
    {
        @Override
        public int evalExpandedSize(int currentlyAllocatedSize, int minimumRequiredSize)
        {
            return currentlyAllocatedSize * 2;
        }
    }
}
