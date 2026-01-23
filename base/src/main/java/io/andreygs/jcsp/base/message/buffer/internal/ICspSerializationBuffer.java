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

/**
 * Extension of {@link ICspBuffer} to be used in CSP serialization process.
 * <p>
 * Clients can use it for implementing ad-hoc serialization methods.
 */
public sealed interface ICspSerializationBuffer
    extends ICspBuffer
    permits CspSerializationBuffer
{
    /**
     * Gets whether underlying storage has direct buffer or not.
     *
     * @return direct buffer or not.
     */
    boolean isDirectBuffer();

    /**
     * Writes single byte value to buffer.
     *
     * @param value Value to write.
     * @throws ArithmeticException if buffer capacity needs to be expanded and new size will overflow an int.
     */
    void write(byte value);

    /**
     * Writes single short value to buffer.
     *
     * @param value Value to write.
     * @throws ArithmeticException if buffer capacity needs to be expanded and new size will overflow an int.
     */
    void write(short value);

    /**
     * Writes single int value to buffer.
     *
     * @param value Value to write.
     * @throws ArithmeticException if buffer capacity needs to be expanded and new size will overflow an int.
     */
    void write(int value);

    /**
     * Writes single long value to buffer.
     *
     * @param value Value to write.
     * @throws ArithmeticException if buffer capacity needs to be expanded and new size will overflow an int.
     */
    void write(long value);

    /**
     * Writes single char value to buffer.
     *
     * @param value Value to write.
     * @throws ArithmeticException if buffer capacity needs to be expanded and new size will overflow an int.
     */
    void write(char value);

    /**
     * Writes single float value to buffer.
     *
     * @param value Value to write.
     * @throws ArithmeticException if buffer capacity needs to be expanded and new size will overflow an int.
     */
    void write(float value);

    /**
     * Writes single double value to buffer.
     *
     * @param value Value to write.
     * @throws ArithmeticException if buffer capacity needs to be expanded and new size will overflow an int.
     */
    void write(double value);

    /**
     * Writes byte array value to buffer.
     *
     * @param value Value to write.
     * @throws ArithmeticException if buffer capacity needs to be expanded and new size will overflow an int.
     */
    void write(byte[] value);

    /**
     * Writes short array value to buffer.
     *
     * @param value Value to write.
     * @throws ArithmeticException if buffer capacity needs to be expanded and new size will overflow an int.
     */
    void write(short[] value);

    /**
     * Writes int array value to buffer.
     *
     * @param value Value to write.
     * @throws ArithmeticException if buffer capacity needs to be expanded and new size will overflow an int.
     */
    void write(int[] value);

    /**
     * Writes long array value to buffer.
     *
     * @param value Value to write.
     * @throws ArithmeticException if buffer capacity needs to be expanded and new size will overflow an int.
     */
    void write(long[] value);

    /**
     * Writes char array value to buffer.
     *
     * @param value Value to write.
     * @throws ArithmeticException if buffer capacity needs to be expanded and new size will overflow an int.
     */
    void write(char[] value);

    /**
     * Writes float array value to buffer.
     *
     * @param value Value to write.
     * @throws ArithmeticException if buffer capacity needs to be expanded and new size will overflow an int.
     */
    void write(float[] value);

    /**
     * Writes double array value to buffer.
     *
     * @param value Value to write.
     * @throws ArithmeticException if buffer capacity needs to be expanded and new size will overflow an int.
     */
    void write(double[] value);

    /**
     * Commits buffer, when serialization is completed.
     * <p>
     * Sets buffer limit (data size) to current cursor position and sets cursor to 0 position.
     * It must be called only once, after last write operation.
     */
    void commitBuffer();
}
