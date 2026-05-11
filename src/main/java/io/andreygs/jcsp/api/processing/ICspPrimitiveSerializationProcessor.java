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

package io.andreygs.jcsp.api.processing;

/**
 * TODO: place description here
 */
public interface ICspPrimitiveSerializationProcessor
{
    /**
     * Serializes boolean field value.
     *
     * @param value Value to serialize.
     */
    void serialize(boolean value);

    /**
     * Serializes byte field value.
     *
     * @param value Value to serialize.
     */
    void serializeByte(byte value);

    /**
     * Serializes short field value.
     *
     * @param value Value to serialize.
     */
    void serializeShort(short value);

    /**
     * Serializes int field value.
     *
     * @param value Value to serialize.
     */
    void serializeInt(int value);

    /**
     * Serializes long field value.
     *
     * @param value Value to serialize.
     */
    void serializeLong(long value);

    /**
     * Serializes char field value.
     * <p>
     * Please, note that using Java char is not recommended in CSP in most cases. Use it only when you really
     * need utf-16 character that is outside the internals of the {@link String} string and this is explicitly
     * specified in the CSP Interface. However, it seems highly unlikely that this would be the case.
     *
     * @param value Value to serialize.
     */
    void serializeChar(char value);

    /**
     * Serializes float field value.
     *
     * @param value Value to serialize.
     */
    void serializeFloat(float value);

    /**
     * Serializes double field value.
     *
     * @param value Value to serialize.
     */
    void serializeDouble(double value);
}
