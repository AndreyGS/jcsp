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

import io.andreygs.jcsp.base.processing.buffer.internal.ICspSerializationBuffer;
import io.andreygs.jcsp.base.processing.context.ICspDataMessageSerializationContext;
import io.andreygs.jcsp.base.types.CspDataFlags;
import io.andreygs.jcsp.base.types.CspRuntimeException;
import io.andreygs.jcsp.base.types.ICspSerializable;

import java.nio.charset.Charset;

/**
 * General-purpose CSP serialization processor for Data Message Body.
 * <p>
 * It is the access point on every top-struct and field serialization.
 * {@link ICspSerializationProcessor} must use this to serialize struct to which it belongs.
 * <p>
 * It uses {@link ICspDataMessageSerializationContext} to get respective serialization settings
 * and {@link ICspSerializationBuffer}.write(...) methods to write raw values of primitives and primitive arrays.
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
     *    <li>If {@link CspDataFlags#SIZE_OF_INTEGERS_MAY_BE_NOT_EQUAL} is set, size of
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
     *    <li>If {@link CspDataFlags#SIZE_OF_INTEGERS_MAY_BE_NOT_EQUAL} is set, size of
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
     *    <li>If {@link CspDataFlags#SIZE_OF_INTEGERS_MAY_BE_NOT_EQUAL} is set, size of
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
     *    <li>If {@link CspDataFlags#SIZE_OF_INTEGERS_MAY_BE_NOT_EQUAL} is set, size of
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
     * <p>
     * Please, note that using Java char is not recommended in CSP in most cases. Use it only when you really
     * need utf-16 character that is outside the internals of the {@link String} string and this is explicitly
     * specified in the CSP Interface. However, it seems highly unlikely that this would be the case.
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
     *    <li>If asReference is set. Pointer mark (type, size and content of mark are depends on which CSP Data flags
     *    are set)</li>
     *    <li>If fixedSize is not set and (asReference is not set or (this is the first object occurrence and
     *    ({@link CspDataFlags#CHECK_RECURSIVE_POINTERS} or
     *    {@link CspDataFlags#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE}
     *    are set))). Number of elements in long(!) format</li>
     *    <li>If asReference is not set or (this is the first object occurrence and
     *    ({@link CspDataFlags#CHECK_RECURSIVE_POINTERS} or
     *    {@link CspDataFlags#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE}
     *    are set)). The array elements one by one writing 1 if element equals true, and 0 otherwise. Every element In
     *    one octet.</li>
     * </ol>
     *
     * @param value Value to serialize.
     * @param asReference Should field value be threatened as reference (CSP pointer).
     *                    If true, then pointer mark will be added to serialized data, and if
     *                    {@link CspDataFlags#CHECK_RECURSIVE_POINTERS} or
     *                    {@link CspDataFlags#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE}
     *                    are set then only first occurrence of this object with the same option value
     *                    will be serialized fully, all others would be referenced to the first one.
     *                    If false, then no pointer mark to serialized data will be added, only size and elements will
     *                    be serialized.
     *                    <p>
     *                    It can be set true only if {@link CspDataFlags#ALLOW_UNMANAGED_POINTERS} is set.
     * @param fixedSize If true, then it counts that this array has fixed size by its CSP Interface definition. So no
     *                  array length should be written and false otherwise.
     * @param context Current serialization message context.
     * @throws CspRuntimeException if asReference equal true and
     * {@link CspDataFlags#ALLOW_UNMANAGED_POINTERS} not set.
     */
    void serialize(boolean[] value, boolean asReference, boolean fixedSize,
                   ICspDataMessageSerializationContext context);

    /**
     * Serializes byte[] field content.
     * <p>
     * This includes:
     * <ol>
     *    <li>If asReference is set. Pointer mark (type, size and content of mark are depends on which CSP Data flags
     *    are set)</li>
     *    <li>If fixedSize is not set and (asReference is not set or (this is the first object occurrence and
     *    ({@link CspDataFlags#CHECK_RECURSIVE_POINTERS} or
     *    {@link CspDataFlags#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE}
     *    are set))). Number of elements in long(!) format</li>
     *    <li>If {@link CspDataFlags#SIZE_OF_INTEGERS_MAY_BE_NOT_EQUAL} is set and (asReference is not set or
     *    (this is the first object occurrence and ({@link CspDataFlags#CHECK_RECURSIVE_POINTERS} or
     *    {@link CspDataFlags#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE} are set))). Size of byte
     *    (number 1) in 1 octet.</li>
     *    <li>If asReference is not set or (this is the first object occurrence and
     *    ({@link CspDataFlags#CHECK_RECURSIVE_POINTERS} or
     *    {@link CspDataFlags#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE}
     *    are set)). The array elements themselves.</li>
     * </ol>
     *
     * @param value Value to serialize.
     * @param asReference Should field value be threatened as reference (CSP pointer).
     *                    If true, then pointer mark will be added to serialized data, and if
     *                    {@link CspDataFlags#CHECK_RECURSIVE_POINTERS} or
     *                    {@link CspDataFlags#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE}
     *                    are set then only first occurrence of this object with the same option value
     *                    will be serialized fully, all others would be referenced to the first one.
     *                    If false, then no pointer mark to serialized data will be added, only size and elements will
     *                    be serialized.
     *                    <p>
     *                    It can be set true only if {@link CspDataFlags#ALLOW_UNMANAGED_POINTERS} is set.
     * @param fixedSize If true, then it counts that this array has fixed size by its CSP Interface definition. So no
     *                  array length should be written and false otherwise.
     * @param context Current serialization message context.
     * @throws CspRuntimeException if asReference equal true and
     * {@link CspDataFlags#ALLOW_UNMANAGED_POINTERS} not set.
     */
    void serialize(byte[] value, boolean asReference, boolean fixedSize, ICspDataMessageSerializationContext context);

    /**
     * Serializes short[] field content.
     * <p>
     * This includes:
     * <ol>
     *    <li>If asReference is set. Pointer mark (type, size and content of mark are depends on which CSP Data flags
     *    are set)</li>
     *    <li>If fixedSize is not set and (asReference is not set or (this is the first object occurrence and
     *    ({@link CspDataFlags#CHECK_RECURSIVE_POINTERS} or
     *    {@link CspDataFlags#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE}
     *    are set))). Number of elements in long(!) format</li>
     *    <li>If {@link CspDataFlags#SIZE_OF_INTEGERS_MAY_BE_NOT_EQUAL} is set and (asReference is not set or
     *    (this is the first object occurrence and ({@link CspDataFlags#CHECK_RECURSIVE_POINTERS} or
     *    {@link CspDataFlags#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE} are set))). Size of short
     *    (number 2) in 1 octet.</li>
     *    <li>If asReference is not set or (this is the first object occurrence and
     *    ({@link CspDataFlags#CHECK_RECURSIVE_POINTERS} or
     *    {@link CspDataFlags#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE}
     *    are set)). The array elements themselves.</li>
     * </ol>
     *
     * @param value Value to serialize.
     * @param asReference Should field value be threatened as reference (CSP pointer).
     *                    If true, then pointer mark will be added to serialized data, and if
     *                    {@link CspDataFlags#CHECK_RECURSIVE_POINTERS} or
     *                    {@link CspDataFlags#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE}
     *                    are set then only first occurrence of this object with the same option value
     *                    will be serialized fully, all others would be referenced to the first one.
     *                    If false, then no pointer mark to serialized data will be added, only size and elements will
     *                    be serialized.
     *                    <p>
     *                    It can be set true only if {@link CspDataFlags#ALLOW_UNMANAGED_POINTERS} is set.
     * @param fixedSize If true, then it counts that this array has fixed size by its CSP Interface definition. So no
     *                  array length should be written and false otherwise.
     * @param context Current serialization message context.
     * @throws CspRuntimeException if asReference equal true and
     * {@link CspDataFlags#ALLOW_UNMANAGED_POINTERS} not set.
     */
    void serialize(short[] value, boolean asReference, boolean fixedSize, ICspDataMessageSerializationContext context);

    /**
     * Serializes int[] field content.
     * <p>
     * This includes:
     * <ol>
     *    <li>If asReference is set. Pointer mark (type, size and content of mark are depends on which CSP Data flags
     *    are set)</li>
     *    <li>If fixedSize is not set and (asReference is not set or (this is the first object occurrence and
     *    ({@link CspDataFlags#CHECK_RECURSIVE_POINTERS} or
     *    {@link CspDataFlags#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE}
     *    are set))). Number of elements in long(!) format</li>
     *    <li>If {@link CspDataFlags#SIZE_OF_INTEGERS_MAY_BE_NOT_EQUAL} is set and (asReference is not set or
     *    (this is the first object occurrence and ({@link CspDataFlags#CHECK_RECURSIVE_POINTERS} or
     *    {@link CspDataFlags#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE} are set))). Size of int
     *    (number 4) in 1 octet.</li>
     *    <li>If asReference is not set or (this is the first object occurrence and
     *    ({@link CspDataFlags#CHECK_RECURSIVE_POINTERS} or
     *    {@link CspDataFlags#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE}
     *    are set)). The array elements themselves.</li>
     * </ol>
     *
     * @param value Value to serialize.
     * @param asReference Should field value be threatened as reference (CSP pointer).
     *                    If true, then pointer mark will be added to serialized data, and if
     *                    {@link CspDataFlags#CHECK_RECURSIVE_POINTERS} or
     *                    {@link CspDataFlags#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE}
     *                    are set then only first occurrence of this object with the same option value
     *                    will be serialized fully, all others would be referenced to the first one.
     *                    If false, then no pointer mark to serialized data will be added, only size and elements will
     *                    be serialized.
     *                    <p>
     *                    It can be set true only if {@link CspDataFlags#ALLOW_UNMANAGED_POINTERS} is set.
     * @param fixedSize If true, then it counts that this array has fixed size by its CSP Interface definition. So no
     *                  array length should be written and false otherwise.
     * @param context Current serialization message context.
     * @throws CspRuntimeException if asReference equal true and
     * {@link CspDataFlags#ALLOW_UNMANAGED_POINTERS} not set.
     */
    void serialize(int[] value, boolean asReference, boolean fixedSize, ICspDataMessageSerializationContext context);

    /**
     * Serializes long[] field content.
     * <p>
     * This includes:
     * <ol>
     *    <li>If asReference is set. Pointer mark (type, size and content of mark are depends on which CSP Data flags
     *    are set)</li>
     *    <li>If fixedSize is not set and (asReference is not set or (this is the first object occurrence and
     *    ({@link CspDataFlags#CHECK_RECURSIVE_POINTERS} or
     *    {@link CspDataFlags#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE}
     *    are set))). Number of elements in long(!) format</li>
     *    <li>If {@link CspDataFlags#SIZE_OF_INTEGERS_MAY_BE_NOT_EQUAL} is set and (asReference is not set or
     *    (this is the first object occurrence and ({@link CspDataFlags#CHECK_RECURSIVE_POINTERS} or
     *    {@link CspDataFlags#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE} are set))). Size of long
     *    (number 8) in 1 octet.</li>
     *    <li>If asReference is not set or (this is the first object occurrence and
     *    ({@link CspDataFlags#CHECK_RECURSIVE_POINTERS} or
     *    {@link CspDataFlags#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE}
     *    are set)). The array elements themselves.</li>
     * </ol>
     *
     * @param value Value to serialize.
     * @param asReference Should field value be threatened as reference (CSP pointer).
     *                    If true, then pointer mark will be added to serialized data, and if
     *                    {@link CspDataFlags#CHECK_RECURSIVE_POINTERS} or
     *                    {@link CspDataFlags#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE}
     *                    are set then only first occurrence of this object with the same option value
     *                    will be serialized fully, all others would be referenced to the first one.
     *                    If false, then no pointer mark to serialized data will be added, only size and elements will
     *                    be serialized.
     *                    <p>
     *                    It can be set true only if {@link CspDataFlags#ALLOW_UNMANAGED_POINTERS} is set.
     * @param fixedSize If true, then it counts that this array has fixed size by its CSP Interface definition. So no
     *                  array length should be written and false otherwise.
     * @param context Current serialization message context.
     * @throws CspRuntimeException if asReference equal true and
     * {@link CspDataFlags#ALLOW_UNMANAGED_POINTERS} not set.
     */
    void serialize(long[] value, boolean asReference, boolean fixedSize, ICspDataMessageSerializationContext context);

    /**
     * Serializes char[] field content.
     * <p>
     * This includes:
     * <ol>
     *    <li>If asReference is set. Pointer mark (type, size and content of mark are depends on which CSP Data flags
     *    are set)</li>
     *    <li>If fixedSize is not set and (asReference is not set or (this is the first object occurrence and
     *    ({@link CspDataFlags#CHECK_RECURSIVE_POINTERS} or
     *    {@link CspDataFlags#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE}
     *    are set))). Number of elements in long(!) format</li>
     *    <li>If asReference is not set or (this is the first object occurrence and
     *    ({@link CspDataFlags#CHECK_RECURSIVE_POINTERS} or
     *    {@link CspDataFlags#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE}
     *    are set)). The array elements themselves.</li>
     * </ol>
     * <p>
     * Please, note that using Java char is not recommended in CSP in most cases. Use it only when you really
     * need utf-16 characters that are outside the internals of the {@link String} string and this is explicitly
     * specified in the CSP Interface. However, it seems highly unlikely that this would be the case.
     *
     * @param value Value to serialize.
     * @param asReference Should field value be threatened as reference (CSP pointer).
     *                    If true, then pointer mark will be added to serialized data, and if
     *                    {@link CspDataFlags#CHECK_RECURSIVE_POINTERS} or
     *                    {@link CspDataFlags#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE}
     *                    are set then only first occurrence of this object with the same option value
     *                    will be serialized fully, all others would be referenced to the first one.
     *                    If false, then no pointer mark to serialized data will be added, only size and elements will
     *                    be serialized.
     *                    <p>
     *                    It can be set true only if {@link CspDataFlags#ALLOW_UNMANAGED_POINTERS} is set.
     * @param fixedSize If true, then it counts that this array has fixed size by its CSP Interface definition. So no
     *                  array length should be written and false otherwise.
     * @param context Current serialization message context.
     * @throws CspRuntimeException if asReference equal true and
     * {@link CspDataFlags#ALLOW_UNMANAGED_POINTERS} not set.
     */
    void serialize(char[] value, boolean asReference, boolean fixedSize, ICspDataMessageSerializationContext context);

    /**
     * Serializes float[] field content.
     * <p>
     * This includes:
     * <ol>
     *    <li>If asReference is set. Pointer mark (type, size and content of mark are depends on which CSP Data flags
     *    are set)</li>
     *    <li>If fixedSize is not set and (asReference is not set or (this is the first object occurrence and
     *    ({@link CspDataFlags#CHECK_RECURSIVE_POINTERS} or
     *    {@link CspDataFlags#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE}
     *    are set))). Number of elements in long(!) format</li>
     *    <li>If asReference is not set or (this is the first object occurrence and
     *    ({@link CspDataFlags#CHECK_RECURSIVE_POINTERS} or
     *    {@link CspDataFlags#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE}
     *    are set)). The array elements themselves.</li>
     * </ol>
     *
     * @param value Value to serialize.
     * @param asReference Should field value be threatened as reference (CSP pointer).
     *                    If true, then pointer mark will be added to serialized data, and if
     *                    {@link CspDataFlags#CHECK_RECURSIVE_POINTERS} or
     *                    {@link CspDataFlags#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE}
     *                    are set then only first occurrence of this object with the same option value
     *                    will be serialized fully, all others would be referenced to the first one.
     *                    If false, then no pointer mark to serialized data will be added, only size and elements will
     *                    be serialized.
     *                    <p>
     *                    It can be set true only if {@link CspDataFlags#ALLOW_UNMANAGED_POINTERS} is set.
     * @param fixedSize If true, then it counts that this array has fixed size by its CSP Interface definition. So no
     *                  array length should be written and false otherwise.
     * @param context Current serialization message context.
     * @throws CspRuntimeException if asReference equal true and
     * {@link CspDataFlags#ALLOW_UNMANAGED_POINTERS} not set.
     */
    void serialize(float[] value, boolean asReference, boolean fixedSize, ICspDataMessageSerializationContext context);

    /**
     * Serializes double[] field content.
     * <p>
     * This includes:
     * <ol>
     *    <li>If asReference is set. Pointer mark (type, size and content of mark are depends on which CSP Data flags
     *    are set)</li>
     *    <li>If fixedSize is not set and (asReference is not set or (this is the first object occurrence and
     *    ({@link CspDataFlags#CHECK_RECURSIVE_POINTERS} or
     *    {@link CspDataFlags#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE}
     *    are set))). Number of elements in long(!) format</li>
     *    <li>If asReference is not set or (this is the first object occurrence and
     *    ({@link CspDataFlags#CHECK_RECURSIVE_POINTERS} or
     *    {@link CspDataFlags#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE}
     *    are set)). The array elements themselves.</li>
     * </ol>
     *
     * @param value Value to serialize.
     * @param asReference Should field value be threatened as reference (CSP pointer).
     *                    If true, then pointer mark will be added to serialized data, and if
     *                    {@link CspDataFlags#CHECK_RECURSIVE_POINTERS} or
     *                    {@link CspDataFlags#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE}
     *                    are set then only first occurrence of this object with the same option value
     *                    will be serialized fully, all others would be referenced to the first one.
     *                    If false, then no pointer mark to serialized data will be added, only size and elements will
     *                    be serialized.
     *                    <p>
     *                    It can be set true only if {@link CspDataFlags#ALLOW_UNMANAGED_POINTERS} is set.
     * @param fixedSize If true, then it counts that this array has fixed size by its CSP Interface definition. So no
     *                  array length should be written and false otherwise.
     * @param context Current serialization message context.
     * @throws CspRuntimeException if asReference equal true and
     * {@link CspDataFlags#ALLOW_UNMANAGED_POINTERS} not set.
     */
    void serialize(double[] value, boolean asReference, boolean fixedSize, ICspDataMessageSerializationContext context);

    /**
     * Serializes String field instance.
     * <p>
     * This includes:
     * <ol>
     *    <li>If asReference is set. Pointer mark (type, size and content of mark are depends on which CSP Data flags
     *    are set)</li>
     *    <li>If asReference is not set or (this is the first object occurrence and
     *    ({@link CspDataFlags#CHECK_RECURSIVE_POINTERS} or
     *    {@link CspDataFlags#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE}
     *    are set)). Number of octets in string with selected {@link Charset}. Important: this is not necessary
     *    would be length of String!</li>
     *    <li>If asReference is not set or (this is the first object occurrence and
     *    ({@link CspDataFlags#CHECK_RECURSIVE_POINTERS} or
     *    {@link CspDataFlags#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE}
     *    are set)). Characters of string without terminating null if present.</li>
     * </ol>
     *
     * @param value Object to serialize.
     * @param asReference Should field value be threatened as reference (CSP pointer).
     *                    If true, then pointer mark will be added to serialized data, and if
     *                    {@link CspDataFlags#CHECK_RECURSIVE_POINTERS} or
     *                    {@link CspDataFlags#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE}
     *                    are set then only first occurrence of this object with the same option value
     *                    will be serialized fully, all others would be referenced to the first one.
     *                    If false, then no pointer mark to serialized data will be added, only size and elements will
     *                    be serialized.
     *                    <p>
     *                    It can be set true only if {@link CspDataFlags#ALLOW_UNMANAGED_POINTERS} is set.
     * @param charset Charset according {@link ICspSerializable} struct specification.
     * @param context Current serialization message context.
     */
    void serialize(String value, boolean asReference, Charset charset, ICspDataMessageSerializationContext context);

    /**
     * Serializes {@link ICspSerializable} field or top-object instance.
     * <p>
     * This includes:
     * <ol>
     *    <li>If asReference is set. Pointer mark (type, size and content of mark are depends on which CSP Data flags
     *    are set)</li>
     *    <li>If asReference is not set or (this is the first object occurrence and
     *    ({@link CspDataFlags#CHECK_RECURSIVE_POINTERS} or
     *    {@link CspDataFlags#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE}
     *    are set)). If instance version is not equals to target one, converting to temporary object using
     *    version convertor.
     *    </li>
     *    <li>If asReference is not set or (this is the first object occurrence and
     *    ({@link CspDataFlags#CHECK_RECURSIVE_POINTERS} or
     *    {@link CspDataFlags#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE}
     *    are set)). Serialization of parent class if present.</li>
     *    <li>If asReference is not set or (this is the first object occurrence and
     *    ({@link CspDataFlags#CHECK_RECURSIVE_POINTERS} or
     *    {@link CspDataFlags#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE}
     *    are set)). Serialization of every serializable field by order specified by CSP Interface.</li>
     * </ol>
     *
     * @param value Object to serialize.
     * @param asReference Should field value be threatened as reference (CSP pointer).
     *                    If true, then pointer mark will be added to serialized data, and if
     *                    {@link CspDataFlags#CHECK_RECURSIVE_POINTERS} or
     *                    {@link CspDataFlags#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE}
     *                    are set then only first occurrence of this object with the same option value
     *                    will be serialized fully, all others would be referenced to the first one.
     *                    If false, then no pointer mark to serialized data will be added, only size and elements will
     *                    be serialized.
     *                    <p>
     *                    It can be set true only if {@link CspDataFlags#ALLOW_UNMANAGED_POINTERS} is set.
     * @param context Current serialization message context.
     */
    void serialize(ICspSerializable value, boolean asReference, ICspDataMessageSerializationContext context);

    /**
     * Serializes Object field.
     * <p>
     * This includes:
     * <ol>
     *    <li>If asReference is set. Pointer mark (type, size and content of mark are depends on which CSP Data flags
     *    are set)</li>
     *    <li>If asReference is not set or (this is the first object occurrence and
     *    ({@link CspDataFlags#CHECK_RECURSIVE_POINTERS} or
     *    {@link CspDataFlags#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE}
     *    are set)). Serialization of parent class if present.</li>
     *    <li>If asReference is not set or (this is the first object occurrence and
     *    ({@link CspDataFlags#CHECK_RECURSIVE_POINTERS} or
     *    {@link CspDataFlags#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE}
     *    are set)). Serialization of every serializable field by order specified by CSP Interface.</li>
     * </ol>
     *
     * @param value Object to serialize.
     * @param asReference Should field value be threatened as reference (CSP pointer).
     *                    If true, then pointer mark will be added to serialized data, and if
     *                    {@link CspDataFlags#CHECK_RECURSIVE_POINTERS} or
     *                    {@link CspDataFlags#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE}
     *                    are set then only first occurrence of this object with the same option value
     *                    will be serialized fully, all others would be referenced to the first one.
     *                    If false, then no pointer mark to serialized data will be added, only size and elements will
     *                    be serialized.
     *                    <p>
     *                    It can be set true only if {@link CspDataFlags#ALLOW_UNMANAGED_POINTERS} is set.
     * @param context Current serialization message context.
     */
    void serialize(Object value, boolean asReference, ICspDataMessageSerializationContext context);
}
