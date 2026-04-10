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

import io.andreygs.jcsp.base.processing.session.ICspDataSerializationSession;
import io.andreygs.jcsp.base.processing.traits.ICspGenericTypeTraits;
import io.andreygs.jcsp.base.processing.traits.ICspGenericTypeTraitsBuilder;
import io.andreygs.jcsp.base.types.CspDataFlag;
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
 * {@link ICspDataSerializationProcessor} must use this to serialize struct to which it belongs.
 * <p>
 * It has immutable state, and it does the same actions every time it has the same input on every method.
 * It serializes data by using supplied {@link ICspDataSerializationSession session} where it
 * takes buffer and serialization settings.
 */
public interface ICspDataGeneralSerializationProcessor
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
     * @param session Current serialization processing session
     */
    void serialize(boolean value, ICspDataSerializationSession session);

    /**
     * Serializes byte field value.
     * <p>
     * This includes next steps:
     * <ol>
     *    <li>Writing size of byte (number 1) in 1 octet.
     * <p>
     *    Conditions:
     *    <ul>
     *        <li>{@link CspDataFlag#SIZE_OF_INTEGERS_MAY_BE_NOT_EQUAL} is set.</li>
     *    </ul>
     *    </li>
     *
     *    <li>Value number.</li>
     * </ol>
     *
     * @param value Value to serialize.
     * @param session Current serialization processing session
     */
    void serializeByte(byte value, ICspDataSerializationSession session);

    /**
     * Serializes short field value.
     * <p>
     * This includes next steps:
     * <ol>
     *    <li>Writing size of short (number 2) in 1 octet.
     * <p>
     *    Conditions:
     *    <ul>
     *        <li>{@link CspDataFlag#SIZE_OF_INTEGERS_MAY_BE_NOT_EQUAL} is set.</li>
     *    </ul>
     *    </li>
     *
     *    <li>Value number.</li>
     * </ol>
     *
     * @param value Value to serialize.
     * @param session Current serialization processing session
     */
    void serializeShort(short value, ICspDataSerializationSession session);

    /**
     * Serializes int field value.
     * <p>
     * This includes next steps:
     * <ol>
     *    <li>Writing size of int (number 4) in 1 octet.
     * <p>
     *    Conditions:
     *    <ul>
     *        <li>{@link CspDataFlag#SIZE_OF_INTEGERS_MAY_BE_NOT_EQUAL} is set.</li>
     *    </ul>
     *    </li>
     *
     *    <li>Value number.</li>
     * </ol>
     *
     * @param value Value to serialize.
     * @param session Current serialization processing session
     */
    void serializeInt(int value, ICspDataSerializationSession session);

    /**
     * Serializes long field value.
     * <p>
     * This includes next steps:
     * <ol>
     *    <li>Writing size of long (number 8) in 1 octet.
     * <p>
     *    Conditions:
     *    <ul>
     *        <li>{@link CspDataFlag#SIZE_OF_INTEGERS_MAY_BE_NOT_EQUAL} is set.</li>
     *    </ul>
     *    </li>
     *
     *    <li>Value number.</li>
     * </ol>
     *
     * @param value Value to serialize.
     * @param session Current serialization processing session
     */
    void serializeLong(long value, ICspDataSerializationSession session);

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
     * @param session Current serialization processing session
     */
    void serializeChar(char value, ICspDataSerializationSession session);

    /**
     * Serializes float field value.
     * <p>
     * This includes next steps:
     * <ol>
     *    <li>Value number.</li>
     * </ol>
     *
     * @param value Value to serialize.
     * @param session Current serialization processing session
     */
    void serializeFloat(float value, ICspDataSerializationSession session);

    /**
     * Serializes double field value.
     * <p>
     * This includes next steps:
     * <ol>
     *    <li>Value number.</li>
     * </ol>
     *
     * @param value Value to serialize.
     * @param session Current serialization processing session
     */
    void serializeDouble(double value, ICspDataSerializationSession session);

    /**
     * Serializes boolean[] field, not as a reference, but as an embedded structure with fixed size dictated
     * by CSP interface.
     * <p>
     * Does the same thing as a call {@link #serialize(boolean[], boolean, boolean, ICspDataSerializationSession)}
     * with false asReference and true fixedSize arguments.
     *
     * @param value Value to serialize.
     * @param session Current serialization processing session
     */
    void serialize(boolean[] value, ICspDataSerializationSession session);

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
     *            {@link CspDataFlag#CHECK_RECURSIVE_POINTERS} is set or
     *            {@link CspDataFlag#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE} is set/</li>
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
     *                    {@link CspDataFlag#CHECK_RECURSIVE_POINTERS} or
     *                    {@link CspDataFlag#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE}
     *                    are set then only first occurrence of this object with the same option value
     *                    will be serialized fully, all others would be referenced to the first one.
     *                    If false, then no pointer mark to serialized data will be added, only size and elements will
     *                    be serialized.
     *                    <p>
     *                    It can be set true only if {@link CspDataFlag#ALLOW_UNMANAGED_POINTERS} is set.
     * @param fixedSize If true, then it counts that this array has fixed size by its CSP Interface definition. So no
     *                  array length should be written and false otherwise.
     * @param session Current serialization processing session
     * @throws CspRuntimeException if asReference equal true and {@link CspDataFlag#ALLOW_UNMANAGED_POINTERS} not set.
     */
    void serialize(boolean @Nullable [] value, boolean asReference, boolean fixedSize,
                   ICspDataSerializationSession session);

    /**
     * Serializes byte[] field, not as a reference, but as an embedded structure with fixed size dictated
     * by CSP interface.
     * <p>
     * Does the same thing as a call {@link #serialize(byte[], boolean, boolean, ICspDataSerializationSession)}
     * with false asReference and true fixedSize arguments.
     *
     * @param value Value to serialize.
     * @param session Current serialization processing session
     */
    void serialize(byte[] value, ICspDataSerializationSession session);

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
     *            {@link CspDataFlag#CHECK_RECURSIVE_POINTERS} is set or
     *            {@link CspDataFlag#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE} is set/</li>
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
     *        <li>{@link CspDataFlag#SIZE_OF_INTEGERS_MAY_BE_NOT_EQUAL} is set.</li>
     *    </ul>
     *    </li>
     *
     *    <li>Writing the array vector.</li>
     * </ol>
     *
     * @param value Value to serialize.
     * @param asReference Should field value be threatened as reference (CSP pointer).
     *                    If true, then pointer mark will be added to serialized data, and if
     *                    {@link CspDataFlag#CHECK_RECURSIVE_POINTERS} or
     *                    {@link CspDataFlag#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE}
     *                    are set then only first occurrence of this object with the same option value
     *                    will be serialized fully, all others would be referenced to the first one.
     *                    If false, then no pointer mark to serialized data will be added, only size and elements will
     *                    be serialized.
     *                    <p>
     *                    It can be set true only if {@link CspDataFlag#ALLOW_UNMANAGED_POINTERS} is set.
     * @param fixedSize If true, then it counts that this array has fixed size by its CSP Interface definition. So no
     *                  array length should be written and false otherwise.
     * @param session Current serialization processing session
     * @throws CspRuntimeException if asReference equal true and {@link CspDataFlag#ALLOW_UNMANAGED_POINTERS} not set.
     */
    void serialize(byte @Nullable [] value, boolean asReference, boolean fixedSize,
                   ICspDataSerializationSession session);

    /**
     * Serializes short[] field, not as a reference, but as an embedded structure with fixed size dictated
     * by CSP interface.
     * <p>
     * Does the same thing as a call {@link #serialize(short[], boolean, boolean, ICspDataSerializationSession)}
     * with false asReference and true fixedSize arguments.
     *
     * @param value Value to serialize.
     * @param session Current serialization processing session
     */
    void serialize(short[] value, ICspDataSerializationSession session);

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
     *            {@link CspDataFlag#CHECK_RECURSIVE_POINTERS} is set or
     *            {@link CspDataFlag#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE} is set/</li>
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
     *        <li>{@link CspDataFlag#SIZE_OF_INTEGERS_MAY_BE_NOT_EQUAL} is set.</li>
     *    </ul>
     *    </li>
     *
     *    <li>Writing the array vector.</li>
     * </ol>
     *
     * @param value Value to serialize.
     * @param asReference Should field value be threatened as reference (CSP pointer).
     *                    If true, then pointer mark will be added to serialized data, and if
     *                    {@link CspDataFlag#CHECK_RECURSIVE_POINTERS} or
     *                    {@link CspDataFlag#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE}
     *                    are set then only first occurrence of this object with the same option value
     *                    will be serialized fully, all others would be referenced to the first one.
     *                    If false, then no pointer mark to serialized data will be added, only size and elements will
     *                    be serialized.
     *                    <p>
     *                    It can be set true only if {@link CspDataFlag#ALLOW_UNMANAGED_POINTERS} is set.
     * @param fixedSize If true, then it counts that this array has fixed size by its CSP Interface definition. So no
     *                  array length should be written and false otherwise.
     * @param session Current serialization processing session
     * @throws CspRuntimeException if asReference equal true and {@link CspDataFlag#ALLOW_UNMANAGED_POINTERS} not set.
     */
    void serialize(short @Nullable [] value, boolean asReference, boolean fixedSize,
                   ICspDataSerializationSession session);

    /**
     * Serializes int[] field, not as a reference, but as an embedded structure with fixed size dictated
     * by CSP interface.
     * <p>
     * Does the same thing as a call {@link #serialize(int[], boolean, boolean, ICspDataSerializationSession)}
     * with false asReference and true fixedSize arguments.
     *
     * @param value Value to serialize.
     * @param session Current serialization processing session
     */
    void serialize(int[] value, ICspDataSerializationSession session);

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
     *            {@link CspDataFlag#CHECK_RECURSIVE_POINTERS} is set or
     *            {@link CspDataFlag#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE} is set/</li>
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
     *        <li>{@link CspDataFlag#SIZE_OF_INTEGERS_MAY_BE_NOT_EQUAL} is set.</li>
     *    </ul>
     *    </li>
     *
     *    <li>Writing the array vector.</li>
     * </ol>
     *
     * @param value Value to serialize.
     * @param asReference Should field value be threatened as reference (CSP pointer).
     *                    If true, then pointer mark will be added to serialized data, and if
     *                    {@link CspDataFlag#CHECK_RECURSIVE_POINTERS} or
     *                    {@link CspDataFlag#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE}
     *                    are set then only first occurrence of this object with the same option value
     *                    will be serialized fully, all others would be referenced to the first one.
     *                    If false, then no pointer mark to serialized data will be added, only size and elements will
     *                    be serialized.
     *                    <p>
     *                    It can be set true only if {@link CspDataFlag#ALLOW_UNMANAGED_POINTERS} is set.
     * @param fixedSize If true, then it counts that this array has fixed size by its CSP Interface definition. So no
     *                  array length should be written and false otherwise.
     * @param session Current serialization processing session
     * @throws CspRuntimeException if asReference equal true and {@link CspDataFlag#ALLOW_UNMANAGED_POINTERS} not set.
     */
    void serialize(int @Nullable [] value, boolean asReference, boolean fixedSize,
                   ICspDataSerializationSession session);

    /**
     * Serializes long[] field, not as a reference, but as an embedded structure with fixed size dictated
     * by CSP interface.
     * <p>
     * Does the same thing as a call {@link #serialize(long[], boolean, boolean, ICspDataSerializationSession)}
     * with false asReference and true fixedSize arguments.
     *
     * @param value Value to serialize.
     * @param session Current serialization processing session
     */
    void serialize(long[] value, ICspDataSerializationSession session);

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
     *            {@link CspDataFlag#CHECK_RECURSIVE_POINTERS} is set or
     *            {@link CspDataFlag#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE} is set/</li>
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
     *        <li>{@link CspDataFlag#SIZE_OF_INTEGERS_MAY_BE_NOT_EQUAL} is set.</li>
     *    </ul>
     *    </li>
     *
     *    <li>Writing the array vector.</li>
     * </ol>
     *
     * @param value Value to serialize.
     * @param asReference Should field value be threatened as reference (CSP pointer).
     *                    If true, then pointer mark will be added to serialized data, and if
     *                    {@link CspDataFlag#CHECK_RECURSIVE_POINTERS} or
     *                    {@link CspDataFlag#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE}
     *                    are set then only first occurrence of this object with the same option value
     *                    will be serialized fully, all others would be referenced to the first one.
     *                    If false, then no pointer mark to serialized data will be added, only size and elements will
     *                    be serialized.
     *                    <p>
     *                    It can be set true only if {@link CspDataFlag#ALLOW_UNMANAGED_POINTERS} is set.
     * @param fixedSize If true, then it counts that this array has fixed size by its CSP Interface definition. So no
     *                  array length should be written and false otherwise.
     * @param session Current serialization processing session
     * @throws CspRuntimeException if asReference equal true and {@link CspDataFlag#ALLOW_UNMANAGED_POINTERS} not set.
     */
    void serialize(long @Nullable [] value, boolean asReference, boolean fixedSize,
                   ICspDataSerializationSession session);

    /**
     * Serializes char[] field, not as a reference, but as an embedded structure with fixed size dictated
     * by CSP interface.
     * <p>
     * Does the same thing as a call {@link #serialize(char[], boolean, boolean, ICspDataSerializationSession)}
     * with false asReference and true fixedSize arguments.
     * <p>
     * Please, note that using Java char is not recommended in CSP in most cases. Use it only when you really
     * need utf-16 characters that are outside the internals of the {@link String} string and this is explicitly
     * specified in the CSP Interface. However, it seems highly unlikely that this would be the case.
     *
     * @param value Value to serialize.
     * @param session Current serialization processing session
     */
    void serialize(char[] value, ICspDataSerializationSession session);

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
     *            {@link CspDataFlag#CHECK_RECURSIVE_POINTERS} is set or
     *            {@link CspDataFlag#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE} is set/</li>
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
     *                    {@link CspDataFlag#CHECK_RECURSIVE_POINTERS} or
     *                    {@link CspDataFlag#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE}
     *                    are set then only first occurrence of this object with the same option value
     *                    will be serialized fully, all others would be referenced to the first one.
     *                    If false, then no pointer mark to serialized data will be added, only size and elements will
     *                    be serialized.
     *                    <p>
     *                    It can be set true only if {@link CspDataFlag#ALLOW_UNMANAGED_POINTERS} is set.
     * @param fixedSize If true, then it counts that this array has fixed size by its CSP Interface definition. So no
     *                  array length should be written and false otherwise.
     * @param session Current serialization processing session
     * @throws CspRuntimeException if asReference equal true and {@link CspDataFlag#ALLOW_UNMANAGED_POINTERS} not set.
     */
    void serialize(char @Nullable [] value, boolean asReference, boolean fixedSize,
                   ICspDataSerializationSession session);

    /**
     * Serializes float[] field, not as a reference, but as an embedded structure with fixed size dictated
     * by CSP interface.
     * <p>
     * Does the same thing as a call {@link #serialize(float[], boolean, boolean, ICspDataSerializationSession)}
     * with false asReference and true fixedSize arguments.
     *
     * @param value Value to serialize.
     * @param session Current serialization processing session
     */
    void serialize(float[] value, ICspDataSerializationSession session);

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
     *            {@link CspDataFlag#CHECK_RECURSIVE_POINTERS} is set or
     *            {@link CspDataFlag#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE} is set/</li>
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
     *                    {@link CspDataFlag#CHECK_RECURSIVE_POINTERS} or
     *                    {@link CspDataFlag#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE}
     *                    are set then only first occurrence of this object with the same option value
     *                    will be serialized fully, all others would be referenced to the first one.
     *                    If false, then no pointer mark to serialized data will be added, only size and elements will
     *                    be serialized.
     *                    <p>
     *                    It can be set true only if {@link CspDataFlag#ALLOW_UNMANAGED_POINTERS} is set.
     * @param fixedSize If true, then it counts that this array has fixed size by its CSP Interface definition. So no
     *                  array length should be written and false otherwise.
     * @param session Current serialization processing session
     * @throws CspRuntimeException if asReference equal true and {@link CspDataFlag#ALLOW_UNMANAGED_POINTERS} not set.
     */
    void serialize(float @Nullable [] value, boolean asReference, boolean fixedSize,
                   ICspDataSerializationSession session);

    /**
     * Serializes double[] field, not as a reference, but as an embedded structure with fixed size dictated
     * by CSP interface.
     * <p>
     * Does the same thing as a call {@link #serialize(double[], boolean, boolean, ICspDataSerializationSession)}
     * with false asReference and true fixedSize arguments.
     *
     * @param value Value to serialize.
     * @param session Current serialization processing session
     */
    void serialize(double[] value, ICspDataSerializationSession session);

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
     *            {@link CspDataFlag#CHECK_RECURSIVE_POINTERS} is set or
     *            {@link CspDataFlag#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE} is set/</li>
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
     *                    {@link CspDataFlag#CHECK_RECURSIVE_POINTERS} or
     *                    {@link CspDataFlag#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE}
     *                    are set then only first occurrence of this object with the same option value
     *                    will be serialized fully, all others would be referenced to the first one.
     *                    If false, then no pointer mark to serialized data will be added, only size and elements will
     *                    be serialized.
     *                    <p>
     *                    It can be set true only if {@link CspDataFlag#ALLOW_UNMANAGED_POINTERS} is set.
     * @param fixedSize If true, then it counts that this array has fixed size by its CSP Interface definition. So no
     *                  array length should be written and false otherwise.
     * @param session Current serialization processing session
     * @throws CspRuntimeException if asReference equal true and {@link CspDataFlag#ALLOW_UNMANAGED_POINTERS} not set.
     */
    void serialize(double @Nullable [] value, boolean asReference, boolean fixedSize,
                   ICspDataSerializationSession session);

    /**
     * Serializes String not as a reference, but as an embedded structure.
     * <p>
     * Does the same thing as a call {@link #serialize(String, boolean, Charset, ICspDataSerializationSession)}
     * with false asReference argument.
     *
     * @param value Object to serialize.
     * @param charset Charset according to CSP Interface specification.
     * @param session Current serialization processing session
     */
    void serialize(String value, Charset charset, ICspDataSerializationSession session);

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
     *            {@link CspDataFlag#CHECK_RECURSIVE_POINTERS} is set or
     *            {@link CspDataFlag#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE} is set/</li>
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
     *                    {@link CspDataFlag#CHECK_RECURSIVE_POINTERS} or
     *                    {@link CspDataFlag#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE}
     *                    are set then only first occurrence of this object with the same option value
     *                    will be serialized fully, all others would be referenced to the first one.
     *                    If false, then no pointer mark to serialized data will be added, only size and elements will
     *                    be serialized.
     *                    <p>
     *                    It can be set true only if {@link CspDataFlag#ALLOW_UNMANAGED_POINTERS} is set.
     * @param charset Charset according to CSP Interface specification.
     * @param session Current serialization processing session
     * @throws CspRuntimeException if asReference equal true and {@link CspDataFlag#ALLOW_UNMANAGED_POINTERS} not set.
     */
    void serialize(@Nullable String value, boolean asReference, Charset charset, ICspDataSerializationSession session);

    /**
     * Serializes Object as an embedded structure.
     * <p>
     * Object should not be any of: array, {@link String}, {@link Collection}, {@link Map}.
     * <p>
     * Does the same thing as a call {@link #serialize(Object, boolean, Class, ICspDataSerializationSession)}
     * with false asReference argument.
     *
     * @param value Object to serialize.
     * @param clazz The class that will be serialized. It is for choice which {@link ICspDataSerializationProcessor}
     *              shall be used, as long as value can implement different interfaces and inherits different classes
     *              (and some of them may be not part of CSP interface),
     * @param session Current serialization processing session
     * @throws CspRuntimeException if some serialized object fields or their nested fields will be serialized
     * as references when {@link CspDataFlag#ALLOW_UNMANAGED_POINTERS} not set.
     */
    void serialize(Object value, Class<?> clazz, ICspDataSerializationSession session);

    /**
     * Serializes Object.
     * <p>
     * Object should not be any of: array, {@link String}, {@link Collection}, {@link Map}.
     * <p>
     * This includes next steps:
     * <ol>
     *    <li>Writing pointer mark.
     * <p>
     *    Conditions:
     *    <ul>
     *        <li>{@link ICspGenericTypeTraits#isReference()} of cspObjectTypeTraits evals to true.</li>
     *    </ul>
     * <p>
     *    Notes:
     *    <ul>
     *        <li>type, size and content of mark are depends on which CSP Data flags are set. See more information
     *        in CSP reference.</li>
     *        <li>next steps will be done only if:
     *        <ol>
     *            <li>asReference is not set or
     *            {@link CspDataFlag#CHECK_RECURSIVE_POINTERS} is set or
     *            {@link CspDataFlag#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE} is set/</li>
     *            <li>This is the first object occurrence.</li>
     *        </ol>
     *        </li>
     *    </ul>
     *    </li>
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
     *        <li>Parent class has {@link io.andreygs.jcsp.base.processing.traits.annotations.CspSerializable}
     *        attribute, or it has own {@link ICspDataSerializationProcessor} or inherited class explicitly serializes
     *        its parent(s).</li>
     *    </ol>
     *    </li>
     *
     *    <li>Serialization of every serializable field by order specified by CSP Interface.</li>
     * </ol>
     *
     * @param value Object to serialize.
     * @param asReference Should field value be threatened as reference (CSP pointer).
     *                    If true, then pointer mark will be added to serialized data, and if
     *                    {@link CspDataFlag#CHECK_RECURSIVE_POINTERS} or
     *                    {@link CspDataFlag#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE}
     *                    are set then only first occurrence of this object with the same option value
     *                    will be serialized fully, all others would be referenced to the first one.
     *                    If false, then no pointer mark to serialized data will be added, only size and elements will
     *                    be serialized.
     *                    <p>
     *                    It can be set true only if {@link CspDataFlag#ALLOW_UNMANAGED_POINTERS} is set.
     * @param clazz The class that will be serialized. It is for choice which {@link ICspDataSerializationProcessor}
     *              shall be used, as long as value can implement different interfaces and inherits different classes
     *              (and some of them may be not part of CSP interface),
     * @param session Current serialization processing session
     * @throws CspRuntimeException if some serialized object fields or their nested fields will be serialized
     * as references when {@link CspDataFlag#ALLOW_UNMANAGED_POINTERS} not set.
     */
    void serialize(Object value, boolean asReference, Class<?> clazz, ICspDataSerializationSession session);

    /**
     * Serializes generic type or array with non-primitive element type.
     * <p>
     * This includes next steps:
     * <ol>
     *    <li>Writing pointer mark.
     * <p>
     *    Conditions:
     *    <ul>
     *        <li>{@link ICspGenericTypeTraits#isReference()} of cspObjectTypeTraits evals to true.</li>
     *    </ul>
     * <p>
     *    Notes:
     *    <ul>
     *        <li>type, size and content of mark are depends on which CSP Data flags are set. See more information
     *        in CSP reference.</li>
     *        <li>next steps will be done only if:
     *        <ol>
     *            <li>asReference is not set or
     *            {@link CspDataFlag#CHECK_RECURSIVE_POINTERS} is set or
     *            {@link CspDataFlag#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE} is set/</li>
     *            <li>This is the first object occurrence.</li>
     *        </ol>
     *        </li>
     *    </ul>
     *    </li>
     *
     *    <li>Serializing of generic type by rules provided with cspGenericFieldTraits.
     *    See {@link ICspGenericTypeTraitsBuilder}
     *    </li>
     * </ol>
     *
     * @param value Generic or array to serialize.
     * @param cspGenericFieldTraits Traits of value type according to CSP reference.
     * @param session Current serialization processing session
     * @throws CspRuntimeException if asReference equal true and {@link CspDataFlag#ALLOW_UNMANAGED_POINTERS} not set.
     */
    void serialize(@Nullable Object value, ICspGenericTypeTraits cspGenericFieldTraits,
                   ICspDataSerializationSession session);


    <T> void serialize(T[] value, Class<?> elementClazz, ICspDataSerializationSession session);

    <T> void serialize(T[] value, boolean asReference, Class<?> elementClazz, ICspDataSerializationSession session);

    void serialize(String[] value, Charset charset, ICspDataSerializationSession session);

    void serialize(String[] value, boolean asReference, Charset charset, ICspDataSerializationSession session);

    <T> void serialize(Collection<T> value, Class<?> elementClazz, ICspDataSerializationSession session);

    <T> void serialize(Collection<T> value, boolean asReference, boolean valueAsReference, Class<?> elementClazz,
                       ICspDataSerializationSession session);

    void serialize(Collection<String> value, Charset charset, ICspDataSerializationSession session);

    void serialize(Collection<String> value, boolean asReference, boolean valueAsReference, Charset charset,
                   ICspDataSerializationSession session);

    <K, V> void serialize(Map<K, V> value, Class<?> keyClazz, Class<?> valueClazz,
                          ICspDataSerializationSession session);

    <K, V> void serialize(Map<K, V> value, boolean asReference, boolean keyAsReference, boolean valueAsRefence,
                          Class<?> keyClazz, Class<?> valueClazz, ICspDataSerializationSession session);

    <K> void serialize(Map<K, String> value, Class<?> keyClazz, Charset valueCharset,
                       ICspDataSerializationSession session);

    <K> void serialize(Map<K, String> value, boolean asReference, boolean keyAsReference, boolean valueAsRefence,
                          Class<?> keyClazz, Charset valueCharset, ICspDataSerializationSession session);

    <V> void serialize(Map<String, V> value, Charset keyCharset, Class<?> valueClazz,
                       ICspDataSerializationSession session);

    <V> void serialize(Map<String, V> value, boolean asReference, boolean keyAsReference, boolean valueAsRefence,
                       Charset keyCharset, Class<?> valueClazz, ICspDataSerializationSession session);

    void serialize(Map<String, String> value, Charset keyCharset, Charset valueCharset,
                   ICspDataSerializationSession session);

    void serialize(Map<String, String> value, boolean asReference, boolean keyAsReference, boolean valueAsRefence,
                  Charset keyCharset, Charset valueCharset, ICspDataSerializationSession session);
}

