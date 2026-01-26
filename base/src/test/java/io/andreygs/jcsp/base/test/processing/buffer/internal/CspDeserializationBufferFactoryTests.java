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

package io.andreygs.jcsp.base.test.processing.buffer.internal;

import io.andreygs.jcsp.base.processing.buffer.internal.CspDeserializationBufferFactory;
import io.andreygs.jcsp.base.processing.buffer.internal.ICspBuffer;
import io.andreygs.jcsp.base.processing.buffer.internal.ICspDeserializationBuffer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

/**
 * Unit-tests for {@link CspDeserializationBufferFactory}.
 */
public class CspDeserializationBufferFactoryTests
{
    @Test
    public void createCspDeserializationBufferTest()
    {
        ByteBuffer byteBuffer = ByteBuffer.allocate(0);
        ICspDeserializationBuffer cspDeserializationBuffer = CspDeserializationBufferFactory.createCspDeserializationBuffer(byteBuffer);

        Assertions.assertEquals(byteBuffer, cspDeserializationBuffer.getByteBuffer(),
                                "ByteBuffer passed to factory is not equal to instance in ICspDeserializationBuffer");
    }

    /**
     * Unit-tests for {@link ICspDeserializationBuffer} instance that is getting from
     * {@link CspDeserializationBufferFactory#createCspDeserializationBuffer(ByteBuffer)}.
     */
    @Nested
    public class CreateCspDeserializationBufferResultTests extends AbstractICspDeserializationBufferTests
    {
        @Override
        protected ICspBuffer createCspBuffer()
        {
            return CspDeserializationBufferFactory.createCspDeserializationBuffer(ByteBuffer.allocate(0));
        }

        @Override
        protected ICspDeserializationBuffer createCspDeserializationBuffer(ByteBuffer byteBuffer)
        {
            return CspDeserializationBufferFactory.createCspDeserializationBuffer(byteBuffer);
        }
    }
}
