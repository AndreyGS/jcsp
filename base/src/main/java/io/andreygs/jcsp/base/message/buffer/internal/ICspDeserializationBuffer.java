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
 * TODO: place description here
 */
public non-sealed interface ICspDeserializationBuffer
    extends ICspBuffer
{
    /**
     * Reads byte value from buffer.
     *
     * @return byte value.
     */
    byte readByte();

    /**
     * Reads short value from buffer.
     *
     * @return short value.
     */
    short readShort();

    /**
     * Reads int value from buffer.
     *
     * @return int value.
     */
    int readInt();

    /**
     * Reads long value from buffer.
     *
     * @return long value.
     */
    long readLong();

    /**
     * Reads char value from buffer.
     *
     * @return char value.
     */
    char readChar();

    /**
     * Reads float value from buffer.
     *
     * @return float value.
     */
    float readFloat();

    /**
     * Reads double value from buffer.
     *
     * @return double value.
     */
    double readDouble();

    /**
     * Reads byte array from buffer.
     *
     * @param value Instance that will be filled with values from buffer.
     */
    void read(byte[] value);

    /**
     * Reads short array from buffer.
     *
     * @param value Instance that will be filled with values from buffer.
     */
    void read(short[] value);

    /**
     * Reads int array from buffer.
     *
     * @param value Instance that will be filled with values from buffer.
     */
    void read(int[] value);

    /**
     * Reads long array from buffer.
     *
     * @param value Instance that will be filled with values from buffer.
     */
    void read(long[] value);

    /**
     * Reads char array from buffer.
     *
     * @param value Instance that will be filled with values from buffer.
     */
    void read(char[] value);

    /**
     * Reads float array from buffer.
     *
     * @param value Instance that will be filled with values from buffer.
     */
    void read(float[] value);

    /**
     * Reads double array from buffer.
     *
     * @param value Instance that will be filled with values from buffer.
     */
    void read(double[] value);
}
