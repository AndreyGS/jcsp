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
import io.andreygs.jcsp.base.processing.typetraits.ICspArrayTypeTraits;
import io.andreygs.jcsp.base.processing.typetraits.ICspReferenceTypeTraits;
import io.andreygs.jcsp.base.types.CspDataFlags;
import io.andreygs.jcsp.base.types.CspRuntimeException;
import io.andreygs.jcsp.base.types.ICspVersionable;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Map;

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
     * This includes next steps:
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
     * This includes next steps:
     * <ol>
     *    <li>Writing size of byte (number 1) in 1 octet.
     * <p>
     *    Conditions:
     *    <ul>
     *        <li>{@link CspDataFlags#SIZE_OF_INTEGERS_MAY_BE_NOT_EQUAL} is set.</li>
     *    </ul>
     *    </li>
     *
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
     * This includes next steps:
     * <ol>
     *    <li>Writing size of short (number 2) in 1 octet.
     * <p>
     *    Conditions:
     *    <ul>
     *        <li>{@link CspDataFlags#SIZE_OF_INTEGERS_MAY_BE_NOT_EQUAL} is set.</li>
     *    </ul>
     *    </li>
     *
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
     * This includes next steps:
     * <ol>
     *    <li>Writing size of int (number 4) in 1 octet.
     * <p>
     *    Conditions:
     *    <ul>
     *        <li>{@link CspDataFlags#SIZE_OF_INTEGERS_MAY_BE_NOT_EQUAL} is set.</li>
     *    </ul>
     *    </li>
     *
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
     * This includes next steps:
     * <ol>
     *    <li>Writing size of long (number 8) in 1 octet.
     * <p>
     *    Conditions:
     *    <ul>
     *        <li>{@link CspDataFlags#SIZE_OF_INTEGERS_MAY_BE_NOT_EQUAL} is set.</li>
     *    </ul>
     *    </li>
     *
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
     * This includes next steps:
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
     * This includes next steps:
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
     * This includes next steps:
     * <ol>
     *    <li>Value number.</li>
     * </ol>
     *
     * @param value Value to serialize.
     * @param context Current serialization message context.
     */
    void serialize(double value, ICspDataMessageSerializationContext context);

    /**
     * Serializes boolean[] field, not as a reference, but as an embedded structure with fixed size dictated
     * by CSP interface.
     * <p>
     * Does the same thing as a call {@link #serialize(boolean[], boolean, boolean, ICspDataMessageSerializationContext)}
     * with false asReference and true fixedSize arguments.
     *
     * @param value Value to serialize.
     * @param context Current serialization message context.
     */
    void serialize(boolean[] value, ICspDataMessageSerializationContext context);

    /**
     * Serializes boolean[] field.
     * <p>
     * This includes next steps:
     * <ol>
     *    <li>Writing pointer mark.
     * <p>
     *    Conditions:
     *    <ul>
     *        <li>asReference is set.</li>
     *    </ul>
     * <p>
     *    Notes:
     *    <ul>
     *        <li>type, size and content of mark are depends on which CSP Data flags are set. See more information
     *        in CSP reference.</li>
     *        <li>next steps will be done only if:
     *        <ol>
     *            <li>asReference is not set or
     *            {@link CspDataFlags#CHECK_RECURSIVE_POINTERS} is set or
     *            {@link CspDataFlags#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE} is set/</li>
     *            <li>This is the first object occurrence.</li>
     *        </ol>
     *        </li>
     *    </ul>
     *    </li>
     *
     *    <li>Writing Number of elements in long(!) format
     * <p>
     *    Conditions:
     *    <ul>
     *        <li>fixedSize is not set.</li>
     *    </ul>
     *    </li>
     *
     *    <li>Writing the array elements one by one writing 1 if element equals true, and 0 otherwise. Every element In
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
     * @throws CspRuntimeException if asReference equal true and {@link CspDataFlags#ALLOW_UNMANAGED_POINTERS} not set.
     */
    void serialize(boolean @Nullable [] value, boolean asReference, boolean fixedSize,
                   ICspDataMessageSerializationContext context);

    /**
     * Serializes byte[] field, not as a reference, but as an embedded structure with fixed size dictated
     * by CSP interface.
     * <p>
     * Does the same thing as a call {@link #serialize(byte[], boolean, boolean, ICspDataMessageSerializationContext)}
     * with false asReference and true fixedSize arguments.
     *
     * @param value Value to serialize.
     * @param context Current serialization message context.
     */
    void serialize(byte[] value, ICspDataMessageSerializationContext context);

    /**
     * Serializes byte[] field.
     * <p>
     * This includes next steps:
     * <ol>
     *    <li>Writing pointer mark.
     * <p>
     *    Conditions:
     *    <ul>
     *        <li>asReference is set.</li>
     *    </ul>
     * <p>
     *    Notes:
     *    <ul>
     *        <li>type, size and content of mark are depends on which CSP Data flags are set. See more information
     *        in CSP reference.</li>
     *        <li>next steps will be done only if:
     *        <ol>
     *            <li>asReference is not set or
     *            {@link CspDataFlags#CHECK_RECURSIVE_POINTERS} is set or
     *            {@link CspDataFlags#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE} is set/</li>
     *            <li>This is the first object occurrence.</li>
     *        </ol>
     *        </li>
     *    </ul>
     *    </li>
     *
     *    <li>Writing Number of elements in long(!) format
     * <p>
     *    Conditions:
     *    <ul>
     *        <li>fixedSize is not set.</li>
     *    </ul>
     *    </li>
     *
     *    <li>Writing size of byte (number 1) in 1 octet.
     * <p>
     *    Conditions:
     *    <ul>
     *        <li>{@link CspDataFlags#SIZE_OF_INTEGERS_MAY_BE_NOT_EQUAL} is set.</li>
     *    </ul>
     *    </li>
     *
     *    <li>Writing the array vector.</li>
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
     * @throws CspRuntimeException if asReference equal true and {@link CspDataFlags#ALLOW_UNMANAGED_POINTERS} not set.
     */
    void serialize(byte @Nullable [] value, boolean asReference, boolean fixedSize, ICspDataMessageSerializationContext context);

    /**
     * Serializes short[] field, not as a reference, but as an embedded structure with fixed size dictated
     * by CSP interface.
     * <p>
     * Does the same thing as a call {@link #serialize(short[], boolean, boolean, ICspDataMessageSerializationContext)}
     * with false asReference and true fixedSize arguments.
     *
     * @param value Value to serialize.
     * @param context Current serialization message context.
     */
    void serialize(short[] value, ICspDataMessageSerializationContext context);

    /**
     * Serializes short[] field.
     * <p>
     * This includes next steps:
     * <ol>
     *    <li>Writing pointer mark.
     * <p>
     *    Conditions:
     *    <ul>
     *        <li>asReference is set.</li>
     *    </ul>
     * <p>
     *    Notes:
     *    <ul>
     *        <li>type, size and content of mark are depends on which CSP Data flags are set. See more information
     *        in CSP reference.</li>
     *        <li>next steps will be done only if:
     *        <ol>
     *            <li>asReference is not set or
     *            {@link CspDataFlags#CHECK_RECURSIVE_POINTERS} is set or
     *            {@link CspDataFlags#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE} is set/</li>
     *            <li>This is the first object occurrence.</li>
     *        </ol>
     *        </li>
     *    </ul>
     *    </li>
     *
     *    <li>Writing Number of elements in long(!) format
     * <p>
     *    Conditions:
     *    <ul>
     *        <li>fixedSize is not set.</li>
     *    </ul>
     *    </li>
     *
     *    <li>Writing size of short (number 2) in 1 octet.
     * <p>
     *    Conditions:
     *    <ul>
     *        <li>{@link CspDataFlags#SIZE_OF_INTEGERS_MAY_BE_NOT_EQUAL} is set.</li>
     *    </ul>
     *    </li>
     *
     *    <li>Writing the array vector.</li>
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
     * @throws CspRuntimeException if asReference equal true and {@link CspDataFlags#ALLOW_UNMANAGED_POINTERS} not set.
     */
    void serialize(short @Nullable [] value, boolean asReference, boolean fixedSize, ICspDataMessageSerializationContext context);

    /**
     * Serializes int[] field, not as a reference, but as an embedded structure with fixed size dictated
     * by CSP interface.
     * <p>
     * Does the same thing as a call {@link #serialize(int[], boolean, boolean, ICspDataMessageSerializationContext)}
     * with false asReference and true fixedSize arguments.
     *
     * @param value Value to serialize.
     * @param context Current serialization message context.
     */
    void serialize(int[] value, ICspDataMessageSerializationContext context);

    /**
     * Serializes int[] field.
     * <p>
     * This includes next steps:
     * <ol>
     *    <li>Writing pointer mark.
     * <p>
     *    Conditions:
     *    <ul>
     *        <li>asReference is set.</li>
     *    </ul>
     * <p>
     *    Notes:
     *    <ul>
     *        <li>type, size and content of mark are depends on which CSP Data flags are set. See more information
     *        in CSP reference.</li>
     *        <li>next steps will be done only if:
     *        <ol>
     *            <li>asReference is not set or
     *            {@link CspDataFlags#CHECK_RECURSIVE_POINTERS} is set or
     *            {@link CspDataFlags#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE} is set/</li>
     *            <li>This is the first object occurrence.</li>
     *        </ol>
     *        </li>
     *    </ul>
     *    </li>
     *
     *    <li>Writing Number of elements in long(!) format
     * <p>
     *    Conditions:
     *    <ul>
     *        <li>fixedSize is not set.</li>
     *    </ul>
     *    </li>
     *
     *    <li>Writing size of int (number 4) in 1 octet.
     * <p>
     *    Conditions:
     *    <ul>
     *        <li>{@link CspDataFlags#SIZE_OF_INTEGERS_MAY_BE_NOT_EQUAL} is set.</li>
     *    </ul>
     *    </li>
     *
     *    <li>Writing the array vector.</li>
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
     * @throws CspRuntimeException if asReference equal true and {@link CspDataFlags#ALLOW_UNMANAGED_POINTERS} not set.
     */
    void serialize(int @Nullable [] value, boolean asReference, boolean fixedSize, ICspDataMessageSerializationContext context);

    /**
     * Serializes long[] field, not as a reference, but as an embedded structure with fixed size dictated
     * by CSP interface.
     * <p>
     * Does the same thing as a call {@link #serialize(long[], boolean, boolean, ICspDataMessageSerializationContext)}
     * with false asReference and true fixedSize arguments.
     *
     * @param value Value to serialize.
     * @param context Current serialization message context.
     */
    void serialize(long[] value, ICspDataMessageSerializationContext context);

    /**
     * Serializes long[] field.
     * <p>
     * This includes next steps:
     * <ol>
     *    <li>Writing pointer mark.
     * <p>
     *    Conditions:
     *    <ul>
     *        <li>asReference is set.</li>
     *    </ul>
     * <p>
     *    Notes:
     *    <ul>
     *        <li>type, size and content of mark are depends on which CSP Data flags are set. See more information
     *        in CSP reference.</li>
     *        <li>next steps will be done only if:
     *        <ol>
     *            <li>asReference is not set or
     *            {@link CspDataFlags#CHECK_RECURSIVE_POINTERS} is set or
     *            {@link CspDataFlags#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE} is set/</li>
     *            <li>This is the first object occurrence.</li>
     *        </ol>
     *        </li>
     *    </ul>
     *    </li>
     *
     *    <li>Writing Number of elements in long(!) format
     * <p>
     *    Conditions:
     *    <ul>
     *        <li>fixedSize is not set.</li>
     *    </ul>
     *    </li>
     *
     *    <li>Writing size of long (number 8) in 1 octet.
     * <p>
     *    Conditions:
     *    <ul>
     *        <li>{@link CspDataFlags#SIZE_OF_INTEGERS_MAY_BE_NOT_EQUAL} is set.</li>
     *    </ul>
     *    </li>
     *
     *    <li>Writing the array vector.</li>
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
     * @throws CspRuntimeException if asReference equal true and {@link CspDataFlags#ALLOW_UNMANAGED_POINTERS} not set.
     */
    void serialize(long @Nullable [] value, boolean asReference, boolean fixedSize, ICspDataMessageSerializationContext context);

    /**
     * Serializes char[] field, not as a reference, but as an embedded structure with fixed size dictated
     * by CSP interface.
     * <p>
     * Does the same thing as a call {@link #serialize(char[], boolean, boolean, ICspDataMessageSerializationContext)}
     * with false asReference and true fixedSize arguments.
     * <p>
     * Please, note that using Java char is not recommended in CSP in most cases. Use it only when you really
     * need utf-16 characters that are outside the internals of the {@link String} string and this is explicitly
     * specified in the CSP Interface. However, it seems highly unlikely that this would be the case.
     *
     * @param value Value to serialize.
     * @param context Current serialization message context.
     */
    void serialize(char[] value, ICspDataMessageSerializationContext context);

    /**
     * Serializes char[] field.
     * <p>
     * This includes next steps:
     * <ol>
     *    <li>Writing pointer mark.
     * <p>
     *    Conditions:
     *    <ul>
     *        <li>asReference is set.</li>
     *    </ul>
     * <p>
     *    Notes:
     *    <ul>
     *        <li>type, size and content of mark are depends on which CSP Data flags are set. See more information
     *        in CSP reference.</li>
     *        <li>next steps will be done only if:
     *        <ol>
     *            <li>asReference is not set or
     *            {@link CspDataFlags#CHECK_RECURSIVE_POINTERS} is set or
     *            {@link CspDataFlags#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE} is set/</li>
     *            <li>This is the first object occurrence.</li>
     *        </ol>
     *        </li>
     *    </ul>
     *    </li>
     *
     *    <li>Writing Number of elements in long(!) format
     * <p>
     *    Conditions:
     *    <ul>
     *        <li>fixedSize is not set.</li>
     *    </ul>
     *
     *    <li>Writing the array vector.</li>
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
     * @throws CspRuntimeException if asReference equal true and {@link CspDataFlags#ALLOW_UNMANAGED_POINTERS} not set.
     */
    void serialize(char @Nullable [] value, boolean asReference, boolean fixedSize, ICspDataMessageSerializationContext context);

    /**
     * Serializes float[] field, not as a reference, but as an embedded structure with fixed size dictated
     * by CSP interface.
     * <p>
     * Does the same thing as a call {@link #serialize(float[], boolean, boolean, ICspDataMessageSerializationContext)}
     * with false asReference and true fixedSize arguments.
     *
     * @param value Value to serialize.
     * @param context Current serialization message context.
     */
    void serialize(float[] value, ICspDataMessageSerializationContext context);

    /**
     * Serializes float[] field.
     * <p>
     * This includes next steps:
     * <ol>
     *    <li>Writing pointer mark.
     * <p>
     *    Conditions:
     *    <ul>
     *        <li>asReference is set.</li>
     *    </ul>
     * <p>
     *    Notes:
     *    <ul>
     *        <li>type, size and content of mark are depends on which CSP Data flags are set. See more information
     *        in CSP reference.</li>
     *        <li>next steps will be done only if:
     *        <ol>
     *            <li>asReference is not set or
     *            {@link CspDataFlags#CHECK_RECURSIVE_POINTERS} is set or
     *            {@link CspDataFlags#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE} is set/</li>
     *            <li>This is the first object occurrence.</li>
     *        </ol>
     *        </li>
     *    </ul>
     *    </li>
     *
     *    <li>Writing Number of elements in long(!) format
     * <p>
     *    Conditions:
     *    <ul>
     *        <li>fixedSize is not set.</li>
     *    </ul>
     *    </li>
     *
     *    <li>Writing the array vector.</li>
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
     * @throws CspRuntimeException if asReference equal true and {@link CspDataFlags#ALLOW_UNMANAGED_POINTERS} not set.
     */
    void serialize(float @Nullable [] value, boolean asReference, boolean fixedSize, ICspDataMessageSerializationContext context);

    /**
     * Serializes double[] field, not as a reference, but as an embedded structure with fixed size dictated
     * by CSP interface.
     * <p>
     * Does the same thing as a call {@link #serialize(double[], boolean, boolean, ICspDataMessageSerializationContext)}
     * with false asReference and true fixedSize arguments.
     *
     * @param value Value to serialize.
     * @param context Current serialization message context.
     */
    void serialize(double[] value, ICspDataMessageSerializationContext context);

    /**
     * Serializes double[] field.
     * <p>
     * This includes next steps:
     * <ol>
     *    <li>Writing pointer mark.
     * <p>
     *    Conditions:
     *    <ul>
     *        <li>asReference is set.</li>
     *    </ul>
     * <p>
     *    Notes:
     *    <ul>
     *        <li>type, size and content of mark are depends on which CSP Data flags are set. See more information
     *        in CSP reference.</li>
     *        <li>next steps will be done only if:
     *        <ol>
     *            <li>asReference is not set or
     *            {@link CspDataFlags#CHECK_RECURSIVE_POINTERS} is set or
     *            {@link CspDataFlags#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE} is set/</li>
     *            <li>This is the first object occurrence.</li>
     *        </ol>
     *        </li>
     *    </ul>
     *    </li>
     *
     *    <li>Writing Number of elements in long(!) format
     * <p>
     *    Conditions:
     *    <ul>
     *        <li>fixedSize is not set.</li>
     *    </ul>
     *    </li>
     *
     *    <li>Writing the array vector.</li>
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
     * @throws CspRuntimeException if asReference equal true and {@link CspDataFlags#ALLOW_UNMANAGED_POINTERS} not set.
     */
    void serialize(double @Nullable [] value, boolean asReference, boolean fixedSize, ICspDataMessageSerializationContext context);

    /**
     * Serializes String not as a reference, but as an embedded structure.
     * <p>
     * Does the same thing as a call {@link #serialize(String, boolean, Charset, ICspDataMessageSerializationContext)}
     * with false asReference argument.
     *
     * @param value Object to serialize.
     * @param charset Charset according to CSP Interface specification.
     * @param context Current serialization message context.
     */
    void serialize(String value, Charset charset, ICspDataMessageSerializationContext context);

    /**
     * Serializes String field.
     * <p>
     * This includes next steps:
     * <ol>
     *    <li>Writing pointer mark.
     * <p>
     *    Conditions:
     *    <ul>
     *        <li>asReference is set.</li>
     *    </ul>
     * <p>
     *    Notes:
     *    <ul>
     *        <li>type, size and content of mark are depends on which CSP Data flags are set. See more information
     *        in CSP reference.</li>
     *        <li>next steps will be done only if:
     *        <ol>
     *            <li>asReference is not set or
     *            {@link CspDataFlags#CHECK_RECURSIVE_POINTERS} is set or
     *            {@link CspDataFlags#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE} is set/</li>
     *            <li>This is the first object occurrence.</li>
     *        </ol>
     *        </li>
     *    </ul>
     *    </li>
     *
     *    <li>Writing number of octets in string with selected {@link Charset}. Important: this is not necessary
     *    would be length of String!</li>
     *
     *    <li>Writing characters of string without terminating null if present.</li>
     *
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
     * @param charset Charset according to CSP Interface specification.
     * @param context Current serialization message context.
     * @throws CspRuntimeException if asReference equal true and {@link CspDataFlags#ALLOW_UNMANAGED_POINTERS} not set.
     */
    void serialize(@Nullable String value, boolean asReference, Charset charset, ICspDataMessageSerializationContext context);

    /**
     * Serializes Object as an embedded structure.
     * <p>
     * Object should not be any of: array, {@link String}, {@link Collection}, {@link Map}.
     *
     * @param value Object to serialize.
     * @param clazz The class that will be serialized. It is for choice which {@link ICspSerializationProcessor}
     *              shall be used, as long as value can implement different interfaces and inherits different classes
     *              (and some of them may be not part of CSP interface),
     * @param context Current serialization message context.
     * @throws CspRuntimeException if some serialized object fields or their nested fields will be serialized
     * as references when {@link CspDataFlags#ALLOW_UNMANAGED_POINTERS} not set.
     */
    void serialize(Object value, Class<?> clazz, ICspDataMessageSerializationContext context);

    /**
     * Serializes Object.
     * <p>
     * This includes next steps:
     * <ol>
     *    <li>Writing pointer mark.
     * <p>
     *    Conditions:
     *    <ul>
     *        <li>{@link ICspReferenceTypeTraits#isReference()} of cspObjectTypeTraits evals to true.</li>
     *    </ul>
     * <p>
     *    Notes:
     *    <ul>
     *        <li>type, size and content of mark are depends on which CSP Data flags are set. See more information
     *        in CSP reference.</li>
     *        <li>next steps will be done only if:
     *        <ol>
     *            <li>asReference is not set or
     *            {@link CspDataFlags#CHECK_RECURSIVE_POINTERS} is set or
     *            {@link CspDataFlags#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE} is set/</li>
     *            <li>This is the first object occurrence.</li>
     *        </ol>
     *        </li>
     *    </ul>
     *    </li>
     *
     *    <li>Serializing of container/array.
     * <p>
     *    Conditions (one of):
     *    <ul>
     *        <li>{@link ICspReferenceTypeTraits#getDeclaredClazz()} is implementing {@link java.util.Collection} or {@link java.util.Map}</li>
     *        <li>cspObjectTypeTraits has type {@link ICspArrayTypeTraits}.</li>
     *    </ul>
     * <p>
     *    Note: next steps will be done only if this is not container/array.
     *
     *    <li>Converting to temporary object for serialization using version convertor.
     * <p>
     *    Conditions:
     *    <ol>
     *        <li>Value instance type is implementing {@link ICspVersionable}.</li>
     *        <li>Value private version is differing from target interface version (from context).</li>
     *    </ol>
     *    </li>
     *
     *    <li>Serialization of parent classes starting with the highest.
     * <p>
     *    Conditions:
     *    <ol>
     *        <li>Parent class is implementing ICspSerializable or {@link ICspSerializationProcessor} for
     *        specific class is explicitly serializes its parent.</li>
     *    </ol>
     *    </li>
     *
     *    <li>Serialization of every serializable field by order specified by CSP Interface.</li>
     * </ol>
     *
     * @param value Object to serialize.
     * @param cspObjectTypeTraits Traits of value type according to CSP reference.
     * @param context Current serialization message context.
     * @throws CspRuntimeException if asReference equal true and {@link CspDataFlags#ALLOW_UNMANAGED_POINTERS} not set.
     */
    void serialize(@Nullable Object value, ICspReferenceTypeTraits cspObjectTypeTraits, ICspDataMessageSerializationContext context);
}

