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

package io.andreygs.jcsp.base.processing;

import io.andreygs.jcsp.base.processing.context.ICspDataMessageSerializationContext;
import io.andreygs.jcsp.base.types.ICspSerializable;

import java.nio.charset.Charset;

/**
 * General-purpose CSP serialization processor for Data Message Body.
 * <p>
 * It is the access point on every struct and field serialization.
 * {@link ICspSerializationProcessor} must use this method to serialize struct to which it belongs.
 */
public interface ICspGeneralSerializationProcessor
{
    /**
     * Serializes boolean field value.
     * <p>
     * This includes:
     * <ol>
     *    <li>1 if Value equals true, and 0 otherwise. In one octet.</li>
     * </ol>
     *
     * @param value Value to serialize.
     * @param context Current serialization message context.
     */
    void serialize(boolean value, ICspDataMessageSerializationContext context);

    /**
     * Serializes byte field value.
     * <p>
     * This includes:
     * <ol>
     *    <li>If {@link io.andreygs.jcsp.base.types.CspDataFlags#SIZE_OF_INTEGERS_MAY_BE_NOT_EQUAL} is set, size of
     *    byte (number 1) in 1 octet.</li>
     *    <li>Value number.</li>
     * </ol>
     *
     * @param value Value to serialize.
     * @param context Current serialization message context.
     */
    void serialize(byte value, ICspDataMessageSerializationContext context);

    /**
     * Serializes short field value.
     * <p>
     * This includes:
     * <ol>
     *    <li>If {@link io.andreygs.jcsp.base.types.CspDataFlags#SIZE_OF_INTEGERS_MAY_BE_NOT_EQUAL} is set, size of
     *    short (number 2) in 1 octet.</li>
     *    <li>Value number.</li>
     * </ol>
     *
     * @param value Value to serialize.
     * @param context Current serialization message context.
     */
    void serialize(short value, ICspDataMessageSerializationContext context);

    /**
     * Serializes int field value.
     * <p>
     * This includes:
     * <ol>
     *    <li>If {@link io.andreygs.jcsp.base.types.CspDataFlags#SIZE_OF_INTEGERS_MAY_BE_NOT_EQUAL} is set, size of
     *    int (number 8) in 1 octet.</li>
     *    <li>Value number.</li>
     * </ol>
     *
     * @param value Value to serialize.
     * @param context Current serialization message context.
     */
    void serialize(int value, ICspDataMessageSerializationContext context);

    /**
     * Serializes long field value.
     * <p>
     * This includes:
     * <ol>
     *    <li>If {@link io.andreygs.jcsp.base.types.CspDataFlags#SIZE_OF_INTEGERS_MAY_BE_NOT_EQUAL} is set, size of
     *    long (number 8) in 1 octet.</li>
     *    <li>Value number.</li>
     * </ol>
     *
     * @param value Value to serialize.
     * @param context Current serialization message context.
     */
    void serialize(long value, ICspDataMessageSerializationContext context);

    /**
     * Serializes char field value.
     * <p>
     * This includes:
     * <ol>
     *    <li>Value number.</li>
     * </ol>
     *
     * @param value Value to serialize.
     * @param context Current serialization message context.
     */
    void serialize(char value, ICspDataMessageSerializationContext context);

    /**
     * Serializes float field value.
     * <p>
     * This includes:
     * <ol>
     *    <li>Value number.</li>
     * </ol>
     *
     * @param value Value to serialize.
     * @param context Current serialization message context.
     */
    void serialize(float value, ICspDataMessageSerializationContext context);

    /**
     * Serializes double field value.
     * <p>
     * This includes:
     * <ol>
     *    <li>Value number.</li>
     * </ol>
     *
     * @param value Value to serialize.
     * @param context Current serialization message context.
     */
    void serialize(double value, ICspDataMessageSerializationContext context);

    /**
     * Serializes boolean field value.
     * <p>
     * This includes:
     * <ol>
     *    <li>Number of elements in long(!) format</li>
     *    <li>Pointer mark (type and size of mark depends on which CSP Data flags are set)</li>
     *    <li>The array elements one by one writing 1 if element equals true, and 0 otherwise. Every element In one
     *    octet.</li>
     * </ol>
     *
     * @param value Value to serialize.
     * @param context Current serialization message context.
     */
    void serialize(boolean[] value, ICspDataMessageSerializationContext context);

    /**
     * Serializes byte[] field content.
     * <p>
     * This includes:
     * <ol>
     *    <li>Number of elements in long(!) format</li>
     *    <li>Pointer mark (type and size of mark depends on which CSP Data flags are set)</li>
     *    <li>If {@link io.andreygs.jcsp.base.types.CspDataFlags#SIZE_OF_INTEGERS_MAY_BE_NOT_EQUAL} is set, size of
     *    byte (number 1) in 1 octet.</li>
     *    <li>The array elements themselves</li>
     * </ol>
     *
     * @param value Value to serialize.
     * @param context Current serialization message context.
     */
    void serialize(byte[] value, ICspDataMessageSerializationContext context);

    /**
     * Serializes short[] field content.
     * <p>
     * This includes:
     * <ol>
     *    <li>Number of elements in long(!) format</li>
     *    <li>Pointer mark (type and size of mark depends on which CSP Data flags are set)</li>
     *    <li>If {@link io.andreygs.jcsp.base.types.CspDataFlags#SIZE_OF_INTEGERS_MAY_BE_NOT_EQUAL} is set, size of
     *    short (number 2) in 1 octet.</li>
     *    <li>The array elements themselves</li>
     * </ol>
     *
     * @param value Value to serialize.
     * @param context Current serialization message context.
     */
    void serialize(short[] value, ICspDataMessageSerializationContext context);

    /**
     * Serializes int[] field content.
     * <p>
     * This includes:
     * <ol>
     *    <li>Number of elements in long(!) format</li>
     *    <li>Pointer mark (type and size of mark depends on which CSP Data flags are set)</li>
     *    <li>If {@link io.andreygs.jcsp.base.types.CspDataFlags#SIZE_OF_INTEGERS_MAY_BE_NOT_EQUAL} is set, size of
     *    int (number 4) in 1 octet.</li>
     *    <li>The array elements themselves</li>
     * </ol>
     *
     * @param value Value to serialize.
     * @param context Current serialization message context.
     */
    void serialize(int[] value, ICspDataMessageSerializationContext context);

    /**
     * Serializes long[] field content.
     * <p>
     * This includes:
     * <ol>
     *    <li>Number of elements in long(!) format</li>
     *    <li>Pointer mark (type and size of mark depends on which CSP Data flags are set)</li>
     *    <li>If {@link io.andreygs.jcsp.base.types.CspDataFlags#SIZE_OF_INTEGERS_MAY_BE_NOT_EQUAL} is set, size of
     *    long (number 8) in 1 octet.</li>
     *    <li>The array elements themselves</li>
     * </ol>
     *
     * @param value Value to serialize.
     * @param context Current serialization message context.
     */
    void serialize(long[] value, ICspDataMessageSerializationContext context);

    /**
     * Serializes char[] field content.
     * <p>
     * This includes:
     * <ol>
     *    <li>Number of elements in long(!) format</li>
     *    <li>Pointer mark (type and size of mark depends on which CSP Data flags are set)</li>
     *    <li>The array elements themselves</li>
     * </ol>
     *
     * @param value Value to serialize.
     * @param context Current serialization message context.
     */
    void serialize(char[] value, ICspDataMessageSerializationContext context);

    /**
     * Serializes float[] field content.
     * <p>
     * This includes:
     * <ol>
     *    <li>Number of elements in long(!) format</li>
     *    <li>Pointer mark (type and size of mark depends on which CSP Data flags are set)</li>
     *    <li>The array elements themselves</li>
     * </ol>
     *
     * @param value Value to serialize.
     * @param context Current serialization message context.
     */
    void serialize(float[] value, ICspDataMessageSerializationContext context);

    /**
     * Serializes double[] field content.
     * <p>
     * This includes:
     * <ol>
     *    <li>Number of elements in long(!) format</li>
     *    <li>Pointer mark (type and size of mark depends on which CSP Data flags are set)</li>
     *    <li>The array elements themselves</li>
     * </ol>
     *
     * @param value Value to serialize.
     * @param context Current serialization message context.
     */
    void serialize(double[] value, ICspDataMessageSerializationContext context);

    /**
     * Serializes String field instance.
     * <p>
     * This includes:
     * <ol>
     *    <li>Number of octets in string with selected {@link Charset}. Important this is not necessary would be length
     *    of String!</li>
     *    <li>Characters of string without terminating null if present.</li>
     * </ol>
     *
     * @param value Object to serialize.
     * @param charset Charset according {@link ICspSerializable} struct specification.
     * @param context Current serialization message context.
     */
    void serialize(String value, Charset charset, ICspDataMessageSerializationContext context);

    /**
     * Serializes {@link ICspSerializable} field instance.
     * <p>
     * This includes:
     * <ol>
     *    <li>If instance version is not equals to target one, converting to temporary object using version convertor
     *    .</li>
     *    <li>Serialization of parent class if present.</li>
     *    <li>Serialization of every serializable field by order specified by CSP Interface.</li>
     * </ol>
     *
     * @param value Object to serialize.
     * @param context Current serialization message context.
     */
    void serialize(ICspSerializable value, ICspDataMessageSerializationContext context);

    /**
     * Serializes Object field.
     * <p>
     * This includes:
     * <ol>
     *    <li>Serialization of parent class if present.</li>
     *    <li>Serialization of every serializable field by order specified by CSP Interface.</li>
     * </ol>
     *
     * @param value Object to serialize.
     * @param context Current serialization message context.
     */
    void serialize(Object value, ICspDataMessageSerializationContext context);
}
