/**
 * @author Andrey Grabov-Smetankin <ukbpyh@gmail.com>
 * <p>
 * License
 * <p>
 * Copyright 2025 Andrey Grabov-Smetankin <ukbpyh@gmail.com>
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files
 * (the "Software"), to deal in the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge,
 * publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so,
 * subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package io.andreygs.jcsp.base.test.message.internal;

import io.andreygs.jcsp.base.message.buffer.internal.CspSerializationBufferFactory;
import io.andreygs.jcsp.base.message.buffer.internal.ICspSerializationBuffer;
import io.andreygs.jcsp.base.utils.IBufferResizeStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Unit-tests for {@link CspSerializationBufferFactory}.
 */
public class CspSerializationBufferFactoryTests
{
    @Test
    public void createCspSerializationBufferTest()
    {
        int capacitySize = 0;
        boolean directBuffer = false;
        int expandRatio = 17;
        IBufferResizeStrategy customBufferResizeStrategy =
            (currentCapacity, minimumRequiredSize) -> (minimumRequiredSize) * expandRatio;

        ICspSerializationBuffer cspSerializationByteBuffer =
            CspSerializationBufferFactory.createCspSerializationBuffer(capacitySize, directBuffer, customBufferResizeStrategy);

        Assertions.assertEquals(capacitySize, cspSerializationByteBuffer.getByteBuffer().capacity());
        Assertions.assertFalse(cspSerializationByteBuffer.isDirectBuffer());

        cspSerializationByteBuffer.write((byte)5);
        Assertions.assertEquals(expandRatio, cspSerializationByteBuffer.getByteBuffer().capacity());
    }

    @Test
    public void createCspSerializationBufferWithDefaultParamsTest()
    {
        ICspSerializationBuffer cspSerializationBuffer = CspSerializationBufferFactory.createCspSerializationBuffer(null, null, null);
        Assertions.assertEquals(CspSerializationBufferFactory.DEFAULT_CAPACITY_SIZE,
                                cspSerializationBuffer.getByteBuffer().capacity());
        Assertions.assertTrue(cspSerializationBuffer.isDirectBuffer());
    }

    /**
     * Unit-tests for {@link ICspSerializationBuffer} instance that is getting from
     * {@link CspSerializationBufferFactory#createCspSerializationBuffer(Integer, Boolean, IBufferResizeStrategy)}.
     */
    @Nested
    public class CreateCspSerializationBufferResultTests extends AbstractICspSerializationBufferTests
    {
        @Override
        protected ICspSerializationBuffer createCspSerializationBuffer(Integer initialBufferCapacity, Boolean directBuffer)
        {
            return CspSerializationBufferFactory.createCspSerializationBuffer(initialBufferCapacity, directBuffer, null);
        }
    }
}
