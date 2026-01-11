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

package io.andreygs.jcsp.base.message.buffer.internal;

import io.andreygs.jcsp.base.message.buffer.ICspDeserializationBuffer;
import io.andreygs.jcsp.base.message.buffer.ICspSerializationBuffer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *  Buffer for data that is using in CSP serialization/deserialization processes.
 *  <p>
 *  Current interface holds common for both (serialization and deserialization) operations.
 *  It is the wrapper of {@link ByteBuffer} optimized for use in CSP operations.
 */
public sealed interface ICspBuffer permits ICspSerializationBuffer, ICspDeserializationBuffer
{
    /**
     * Gets underlying buffer.
     *
     * @return underlying buffer.
     */
    ByteBuffer getByteBuffer();

    /**
     * Applies endianness to underlying ByteBuffer operations.
     *
     * @param byteOrder Endianness byte order.
     * @see ByteBuffer#order()
     */
    void applyEndianness(ByteOrder byteOrder);
}
