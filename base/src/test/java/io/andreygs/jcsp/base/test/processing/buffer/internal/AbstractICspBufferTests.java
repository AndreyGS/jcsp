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

import io.andreygs.jcsp.base.processing.buffer.internal.ICspBuffer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.ByteOrder;

/**
 * Unit-tests for {@link ICspBuffer} contract.
 */
public abstract class AbstractICspBufferTests
{
    @Test
    public void getByteBufferTest()
    {
        ICspBuffer cspBuffer = createCspBuffer();
        Assertions.assertNotNull(cspBuffer.getByteBuffer(), "ByteBuffer should not be null");
    }

    @Test
    public void applyEndiannessTest()
    {
        ICspBuffer cspBuffer = createCspBuffer();
        cspBuffer.applyEndianness(ByteOrder.BIG_ENDIAN);

        String applyEndiannessTestFailed = "Endianness applying to buffer was failed!";
        Assertions.assertEquals(ByteOrder.BIG_ENDIAN, cspBuffer.getByteBuffer().order(), applyEndiannessTestFailed);

        cspBuffer.applyEndianness(ByteOrder.LITTLE_ENDIAN);
        Assertions.assertEquals(ByteOrder.LITTLE_ENDIAN, cspBuffer.getByteBuffer().order(), applyEndiannessTestFailed);
    }

    protected abstract ICspBuffer createCspBuffer();
}
