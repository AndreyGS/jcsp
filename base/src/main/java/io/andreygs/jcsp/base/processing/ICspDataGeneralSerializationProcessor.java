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

import io.andreygs.jcsp.base.processing.annotations.CspField;
import io.andreygs.jcsp.base.processing.composite.CspTypeToken;
import io.andreygs.jcsp.base.processing.internal.ICspDataProcessorRegistry;
import io.andreygs.jcsp.base.types.CspDataFlag;
import io.andreygs.jcsp.base.types.CspRuntimeException;
import io.andreygs.jcsp.base.types.CspStatus;
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
     *  @param value Value to serialize.
     */
    void serialize(boolean value);

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
     *  @param value Value to serialize.
     */
    void serializeByte(byte value);

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
     *  @param value Value to serialize.
     */
    void serializeShort(short value);

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
     *  @param value Value to serialize.
     */
    void serializeInt(int value);

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
     *  @param value Value to serialize.
     */
    void serializeLong(long value);

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
     *  @param value Value to serialize.
     */
    void serializeChar(char value);

    /**
     * Serializes float field value.
     * <p>
     * This includes next steps:
     * <ol>
     *    <li>Value number.</li>
     * </ol>
     *  @param value Value to serialize.
     */
    void serializeFloat(float value);

    /**
     * Serializes double field value.
     * <p>
     * This includes next steps:
     * <ol>
     *    <li>Value number.</li>
     * </ol>
     *  @param value Value to serialize.
     */
    void serializeDouble(double value);

    /**
     * Serializes boolean[] field, not as a reference, but as an embedded structure with fixed size dictated
     * by CSP interface.
     * <p>
     * Does the same thing as a call {@link #serialize(boolean[], boolean, boolean)}
     * with false reference and true fixedSize arguments.
     *
     * @param value Value to serialize.
     */
    void serialize(boolean[] value);

    /**
     * Serializes boolean[] field.
     * <p>
     * This includes next steps:
     * <ol>
     *    <li>Writing pointer mark.
     * <p>
     *    Conditions:
     *    <ul>
     *        <li>reference is set.</li>
     *    </ul>
     * <p>
     *    Notes:
     *    <ul>
     *        <li>type, size and content of mark are depends on which CSP Data flags are set. See more information
     *        in CSP reference.</li>
     *        <li>next steps will be done only if:
     *        <ol>
     *            <li>reference is not set or
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
     * @param value     Value to serialize.
     * @param reference Should field value be threatened as reference (CSP pointer).
     *                  If true, then pointer mark will be added to serialized data, and if
     *                  {@link CspDataFlag#CHECK_RECURSIVE_POINTERS} or
     *                  {@link CspDataFlag#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE}
     *                  are set then only first occurrence of this object with the same option value
     *                  will be serialized fully, all others would be referenced to the first one.
     *                  If false, then no pointer mark to serialized data will be added, only size and elements will
     *                  be serialized.
     *                  <p>
     *                  It can be set true only if {@link CspDataFlag#ALLOW_UNMANAGED_POINTERS} is set.
     * @param fixedSize If true, then it counts that this array has fixed size by its CSP Interface definition. So no
     *                  array length should be written and false otherwise.
     * @throws CspRuntimeException if reference equal true and {@link CspDataFlag#ALLOW_UNMANAGED_POINTERS} not set.
     */
    void serialize(boolean @Nullable [] value, boolean reference, boolean fixedSize);

    /**
     * Serializes byte[] field, not as a reference, but as an embedded structure with fixed size dictated
     * by CSP interface.
     * <p>
     * Does the same thing as a call {@link #serialize(byte[], boolean, boolean)}
     * with false reference and true fixedSize arguments.
     *
     * @param value Value to serialize.
     */
    void serialize(byte[] value);

    /**
     * Serializes byte[] field.
     * <p>
     * This includes next steps:
     * <ol>
     *    <li>Writing pointer mark.
     * <p>
     *    Conditions:
     *    <ul>
     *        <li>reference is set.</li>
     *    </ul>
     * <p>
     *    Notes:
     *    <ul>
     *        <li>type, size and content of mark are depends on which CSP Data flags are set. See more information
     *        in CSP reference.</li>
     *        <li>next steps will be done only if:
     *        <ol>
     *            <li>reference is not set or
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
     * @param value     Value to serialize.
     * @param reference Should field value be threatened as reference (CSP pointer).
     *                  If true, then pointer mark will be added to serialized data, and if
     *                  {@link CspDataFlag#CHECK_RECURSIVE_POINTERS} or
     *                  {@link CspDataFlag#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE}
     *                  are set then only first occurrence of this object with the same option value
     *                  will be serialized fully, all others would be referenced to the first one.
     *                  If false, then no pointer mark to serialized data will be added, only size and elements will
     *                  be serialized.
     *                  <p>
     *                  It can be set true only if {@link CspDataFlag#ALLOW_UNMANAGED_POINTERS} is set.
     * @param fixedSize If true, then it counts that this array has fixed size by its CSP Interface definition. So no
     *                  array length should be written and false otherwise.
     * @throws CspRuntimeException if reference equal true and {@link CspDataFlag#ALLOW_UNMANAGED_POINTERS} not set.
     */
    void serialize(byte @Nullable [] value, boolean reference, boolean fixedSize);

    /**
     * Serializes short[] field, not as a reference, but as an embedded structure with fixed size dictated
     * by CSP interface.
     * <p>
     * Does the same thing as a call {@link #serialize(short[], boolean, boolean)}
     * with false reference and true fixedSize arguments.
     *
     * @param value Value to serialize.
     */
    void serialize(short[] value);

    /**
     * Serializes short[] field.
     * <p>
     * This includes next steps:
     * <ol>
     *    <li>Writing pointer mark.
     * <p>
     *    Conditions:
     *    <ul>
     *        <li>reference is set.</li>
     *    </ul>
     * <p>
     *    Notes:
     *    <ul>
     *        <li>type, size and content of mark are depends on which CSP Data flags are set. See more information
     *        in CSP reference.</li>
     *        <li>next steps will be done only if:
     *        <ol>
     *            <li>reference is not set or
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
     * @param value     Value to serialize.
     * @param reference Should field value be threatened as reference (CSP pointer).
     *                  If true, then pointer mark will be added to serialized data, and if
     *                  {@link CspDataFlag#CHECK_RECURSIVE_POINTERS} or
     *                  {@link CspDataFlag#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE}
     *                  are set then only first occurrence of this object with the same option value
     *                  will be serialized fully, all others would be referenced to the first one.
     *                  If false, then no pointer mark to serialized data will be added, only size and elements will
     *                  be serialized.
     *                  <p>
     *                  It can be set true only if {@link CspDataFlag#ALLOW_UNMANAGED_POINTERS} is set.
     * @param fixedSize If true, then it counts that this array has fixed size by its CSP Interface definition. So no
     *                  array length should be written and false otherwise.
     * @throws CspRuntimeException if reference equal true and {@link CspDataFlag#ALLOW_UNMANAGED_POINTERS} not set.
     */
    void serialize(short @Nullable [] value, boolean reference, boolean fixedSize);

    /**
     * Serializes int[] field, not as a reference, but as an embedded structure with fixed size dictated
     * by CSP interface.
     * <p>
     * Does the same thing as a call {@link #serialize(int[], boolean, boolean)}
     * with false reference and true fixedSize arguments.
     *
     * @param value Value to serialize.
     */
    void serialize(int[] value);

    /**
     * Serializes int[] field.
     * <p>
     * This includes next steps:
     * <ol>
     *    <li>Writing pointer mark.
     * <p>
     *    Conditions:
     *    <ul>
     *        <li>reference is set.</li>
     *    </ul>
     * <p>
     *    Notes:
     *    <ul>
     *        <li>type, size and content of mark are depends on which CSP Data flags are set. See more information
     *        in CSP reference.</li>
     *        <li>next steps will be done only if:
     *        <ol>
     *            <li>reference is not set or
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
     * @param value     Value to serialize.
     * @param reference Should field value be threatened as reference (CSP pointer).
     *                  If true, then pointer mark will be added to serialized data, and if
     *                  {@link CspDataFlag#CHECK_RECURSIVE_POINTERS} or
     *                  {@link CspDataFlag#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE}
     *                  are set then only first occurrence of this object with the same option value
     *                  will be serialized fully, all others would be referenced to the first one.
     *                  If false, then no pointer mark to serialized data will be added, only size and elements will
     *                  be serialized.
     *                  <p>
     *                  It can be set true only if {@link CspDataFlag#ALLOW_UNMANAGED_POINTERS} is set.
     * @param fixedSize If true, then it counts that this array has fixed size by its CSP Interface definition. So no
     *                  array length should be written and false otherwise.
     * @throws CspRuntimeException if reference equal true and {@link CspDataFlag#ALLOW_UNMANAGED_POINTERS} not set.
     */
    void serialize(int @Nullable [] value, boolean reference, boolean fixedSize);

    /**
     * Serializes long[] field, not as a reference, but as an embedded structure with fixed size dictated
     * by CSP interface.
     * <p>
     * Does the same thing as a call {@link #serialize(long[], boolean, boolean)}
     * with false reference and true fixedSize arguments.
     *
     * @param value Value to serialize.
     */
    void serialize(long[] value);

    /**
     * Serializes long[] field.
     * <p>
     * This includes next steps:
     * <ol>
     *    <li>Writing pointer mark.
     * <p>
     *    Conditions:
     *    <ul>
     *        <li>reference is set.</li>
     *    </ul>
     * <p>
     *    Notes:
     *    <ul>
     *        <li>type, size and content of mark are depends on which CSP Data flags are set. See more information
     *        in CSP reference.</li>
     *        <li>next steps will be done only if:
     *        <ol>
     *            <li>reference is not set or
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
     * @param value     Value to serialize.
     * @param reference Should field value be threatened as reference (CSP pointer).
     *                  If true, then pointer mark will be added to serialized data, and if
     *                  {@link CspDataFlag#CHECK_RECURSIVE_POINTERS} or
     *                  {@link CspDataFlag#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE}
     *                  are set then only first occurrence of this object with the same option value
     *                  will be serialized fully, all others would be referenced to the first one.
     *                  If false, then no pointer mark to serialized data will be added, only size and elements will
     *                  be serialized.
     *                  <p>
     *                  It can be set true only if {@link CspDataFlag#ALLOW_UNMANAGED_POINTERS} is set.
     * @param fixedSize If true, then it counts that this array has fixed size by its CSP Interface definition. So no
     *                  array length should be written and false otherwise.
     * @throws CspRuntimeException if reference equal true and {@link CspDataFlag#ALLOW_UNMANAGED_POINTERS} not set.
     */
    void serialize(long @Nullable [] value, boolean reference, boolean fixedSize);

    /**
     * Serializes char[] field, not as a reference, but as an embedded structure with fixed size dictated
     * by CSP interface.
     * <p>
     * Does the same thing as a call {@link #serialize(char[], boolean, boolean)}
     * with false reference and true fixedSize arguments.
     * <p>
     * Please, note that using Java char is not recommended in CSP in most cases. Use it only when you really
     * need utf-16 characters that are outside the internals of the {@link String} string and this is explicitly
     * specified in the CSP Interface. However, it seems highly unlikely that this would be the case.
     *
     * @param value Value to serialize.
     */
    void serialize(char[] value);

    /**
     * Serializes char[] field.
     * <p>
     * This includes next steps:
     * <ol>
     *    <li>Writing pointer mark.
     * <p>
     *    Conditions:
     *    <ul>
     *        <li>reference is set.</li>
     *    </ul>
     * <p>
     *    Notes:
     *    <ul>
     *        <li>type, size and content of mark are depends on which CSP Data flags are set. See more information
     *        in CSP reference.</li>
     *        <li>next steps will be done only if:
     *        <ol>
     *            <li>reference is not set or
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
     * @param value     Value to serialize.
     * @param reference Should field value be threatened as reference (CSP pointer).
     *                  If true, then pointer mark will be added to serialized data, and if
     *                  {@link CspDataFlag#CHECK_RECURSIVE_POINTERS} or
     *                  {@link CspDataFlag#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE}
     *                  are set then only first occurrence of this object with the same option value
     *                  will be serialized fully, all others would be referenced to the first one.
     *                  If false, then no pointer mark to serialized data will be added, only size and elements will
     *                  be serialized.
     *                  <p>
     *                  It can be set true only if {@link CspDataFlag#ALLOW_UNMANAGED_POINTERS} is set.
     * @param fixedSize If true, then it counts that this array has fixed size by its CSP Interface definition. So no
     *                  array length should be written and false otherwise.
     * @throws CspRuntimeException if reference equal true and {@link CspDataFlag#ALLOW_UNMANAGED_POINTERS} not set.
     */
    void serialize(char @Nullable [] value, boolean reference, boolean fixedSize);

    /**
     * Serializes float[] field, not as a reference, but as an embedded structure with fixed size dictated
     * by CSP interface.
     * <p>
     * Does the same thing as a call {@link #serialize(float[], boolean, boolean)}
     * with false reference and true fixedSize arguments.
     *
     * @param value Value to serialize.
     */
    void serialize(float[] value);

    /**
     * Serializes float[] field.
     * <p>
     * This includes next steps:
     * <ol>
     *    <li>Writing pointer mark.
     * <p>
     *    Conditions:
     *    <ul>
     *        <li>reference is set.</li>
     *    </ul>
     * <p>
     *    Notes:
     *    <ul>
     *        <li>type, size and content of mark are depends on which CSP Data flags are set. See more information
     *        in CSP reference.</li>
     *        <li>next steps will be done only if:
     *        <ol>
     *            <li>reference is not set or
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
     * @param value     Value to serialize.
     * @param reference Should field value be threatened as reference (CSP pointer).
     *                  If true, then pointer mark will be added to serialized data, and if
     *                  {@link CspDataFlag#CHECK_RECURSIVE_POINTERS} or
     *                  {@link CspDataFlag#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE}
     *                  are set then only first occurrence of this object with the same option value
     *                  will be serialized fully, all others would be referenced to the first one.
     *                  If false, then no pointer mark to serialized data will be added, only size and elements will
     *                  be serialized.
     *                  <p>
     *                  It can be set true only if {@link CspDataFlag#ALLOW_UNMANAGED_POINTERS} is set.
     * @param fixedSize If true, then it counts that this array has fixed size by its CSP Interface definition. So no
     *                  array length should be written and false otherwise.
     * @throws CspRuntimeException if reference equal true and {@link CspDataFlag#ALLOW_UNMANAGED_POINTERS} not set.
     */
    void serialize(float @Nullable [] value, boolean reference, boolean fixedSize);

    /**
     * Serializes double[] field, not as a reference, but as an embedded structure with fixed size dictated
     * by CSP interface.
     * <p>
     * Does the same thing as a call {@link #serialize(double[], boolean, boolean)}
     * with false reference and true fixedSize arguments.
     *
     * @param value Value to serialize.
     */
    void serialize(double[] value);

    /**
     * Serializes double[] field.
     * <p>
     * This includes next steps:
     * <ol>
     *    <li>Writing pointer mark.
     * <p>
     *    Conditions:
     *    <ul>
     *        <li>reference is set.</li>
     *    </ul>
     * <p>
     *    Notes:
     *    <ul>
     *        <li>type, size and content of mark are depends on which CSP Data flags are set. See more information
     *        in CSP reference.</li>
     *        <li>next steps will be done only if:
     *        <ol>
     *            <li>reference is not set or
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
     * @param value     Value to serialize.
     * @param reference Should field value be threatened as reference (CSP pointer).
     *                  If true, then pointer mark will be added to serialized data, and if
     *                  {@link CspDataFlag#CHECK_RECURSIVE_POINTERS} or
     *                  {@link CspDataFlag#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE}
     *                  are set then only first occurrence of this object with the same option value
     *                  will be serialized fully, all others would be referenced to the first one.
     *                  If false, then no pointer mark to serialized data will be added, only size and elements will
     *                  be serialized.
     *                  <p>
     *                  It can be set true only if {@link CspDataFlag#ALLOW_UNMANAGED_POINTERS} is set.
     * @param fixedSize If true, then it counts that this array has fixed size by its CSP Interface definition. So no
     *                  array length should be written and false otherwise.
     * @throws CspRuntimeException if reference equal true and {@link CspDataFlag#ALLOW_UNMANAGED_POINTERS} not set.
     */
    void serialize(double @Nullable [] value, boolean reference, boolean fixedSize);

    /**
     * Serializes String not as a reference, but as an embedded structure.
     * <p>
     * Does the same thing as a call {@link #serialize(String, boolean, Charset)}
     * with false reference argument.
     *
     * @param value   Object to serialize.
     * @param charset Charset according to CSP Interface specification.
     */
    void serialize(String value, Charset charset);

    /**
     * Serializes String field.
     * <p>
     * This includes next steps:
     * <ol>
     *    <li>Writing pointer mark.
     * <p>
     *    Conditions:
     *    <ul>
     *        <li>reference is set.</li>
     *    </ul>
     * <p>
     *    Notes:
     *    <ul>
     *        <li>type, size and content of mark are depends on which CSP Data flags are set. See more information
     *        in CSP reference.</li>
     *        <li>next steps will be done only if:
     *        <ol>
     *            <li>reference is not set or
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
     * @param value     Object to serialize.
     * @param reference Should field value be threatened as reference (CSP pointer).
     *                  If true, then pointer mark will be added to serialized data, and if
     *                  {@link CspDataFlag#CHECK_RECURSIVE_POINTERS} or
     *                  {@link CspDataFlag#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE}
     *                  are set then only first occurrence of this object with the same option value
     *                  will be serialized fully, all others would be referenced to the first one.
     *                  If false, then no pointer mark to serialized data will be added, only size and elements will
     *                  be serialized.
     *                  <p>
     *                  It can be set true only if {@link CspDataFlag#ALLOW_UNMANAGED_POINTERS} is set.
     * @param charset   Charset according to CSP Interface specification.
     * @throws CspRuntimeException if reference equal true and {@link CspDataFlag#ALLOW_UNMANAGED_POINTERS} not set.
     */
    void serialize(@Nullable String value, boolean reference, Charset charset);

    /**
     * Serializes Object as an embedded structure.
     * <p>
     * Object should not be any of: array, {@link String}, {@link Collection}, {@link Map}.
     * <p>
     * Does the same thing as a call {@link #serialize(Object, boolean, Class)}
     * with false reference argument.
     *
     * @param value Object to serialize.
     * @param clazz The class that will be serialized. It is for choice which {@link ICspDataSerializationProcessor}
     *              shall be used, as long as value can implement different interfaces and inherits different classes
     *              (and some of them may be not part of CSP interface),
     * @param <T>   Type of object to serialize.
     * @throws CspRuntimeException if some serialized object fields or their nested fields should be serialized
     *                             as references when {@link CspDataFlag#ALLOW_UNMANAGED_POINTERS} not set and status
     *                             will be {@link CspStatus#POINTER_WHEN_NO_ALLOW_UNMANAGED_POINTERS_SET}.
     *                             if there is no processor for clazz to handle serialization and status will be
     *                             {@link CspStatus#NO_SUCH_HANDLER}.
     */
    <T> void serialize(T value, Class<?> clazz);

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
     *        <li>reference is set.</li>
     *    </ul>
     * <p>
     *    Notes:
     *    <ul>
     *        <li>type, size and content of mark are depends on which CSP Data flags are set. See more information
     *        in CSP reference.</li>
     *        <li>next steps will be done only if:
     *        <ol>
     *            <li>reference is not set or
     *            {@link CspDataFlag#CHECK_RECURSIVE_POINTERS} is set or
     *            {@link CspDataFlag#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE} is set/</li>
     *            <li>This is the first object occurrence.</li>
     *        </ol>
     *        </li>
     *    </ul>
     *
     *    <li> Serialization of class with registered in {@link ICspDataProcessorRegistry}
     *    {@link ICspDataSerializationProcessor} or if there is no such - with one auto generated by annotations.
     *    In latest case, it will be generated in first time serialization process after program start, and saved in
     *    provided to message builder {@link ICspDataProcessorRegistry} for subsequent uses.
     * <p>
     *    The main steps of object serialization are:
     *    <ol>
     *        <li>Converting to temporary object for serialization using version convertor.
     * <p>
     *        Conditions:
     *        <ol>
     *            <li>Value instance type is implementing {@link ICspVersionable}.</li>
     *            <li>Value private version is differing from target interface version (from context).</li>
     *        </ol>
     *        </li>
     *
     *        <li>Serialization of parent classes starting with the highest.
     * <p>
     *        Conditions:
     *        <ol>
     *            <li>Parent class has {@link CspField}
     *            attribute, or it has own {@link ICspDataSerializationProcessor} or inherited class explicitly
     *            serializes its parent(s).</li>
     *        </ol>
     *        </li>
     *
     *        <li>Serialization of every serializable field by order specified by CSP Interface.</li>
     *    </ol>
     *    </li>
     *    </li>
     *
     * </ol>
     *
     * @param value     Object to serialize.
     * @param reference Should field value be threatened as reference (CSP pointer).
     *                  If true, then pointer mark will be added to serialized data, and if
     *                  {@link CspDataFlag#CHECK_RECURSIVE_POINTERS} or
     *                  {@link CspDataFlag#CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE}
     *                  are set then only first occurrence of this object with the same option value
     *                  will be serialized fully, all others would be referenced to the first one.
     *                  If false, then no pointer mark to serialized data will be added, only size and elements will
     *                  be serialized.
     *                  <p>
     *                  It can be set true only if {@link CspDataFlag#ALLOW_UNMANAGED_POINTERS} is set.
     * @param clazz     The class that will be serialized. It is for choice which {@link ICspDataSerializationProcessor}
     *                  shall be used, as long as value can implement different interfaces and inherits different classes
     *                  (and some of them may be not part of CSP interface),
     * @param <T>       Type of object to serialize.
     * @throws CspRuntimeException if some serialized object fields or their nested fields should be serialized
     *                             as references when {@link CspDataFlag#ALLOW_UNMANAGED_POINTERS} not set and status
     *                             will be {@link CspStatus#POINTER_WHEN_NO_ALLOW_UNMANAGED_POINTERS_SET}.
     *                             if there is no processor for clazz to handle serialization and status will be
     *                             {@link CspStatus#NO_SUCH_HANDLER}.
     */
    <T> void serialize(@Nullable T value, boolean reference, Class<?> clazz);

    <T> void serialize(T[] value, Class<?> elementClazz);

    <T> void serialize(@Nullable T @Nullable [] value, boolean reference, boolean fixedSize, Class<?> elementClazz,
        boolean elementReference);

    void serialize(String[] value, Charset charset);

    void serialize(@Nullable String @Nullable [] value, boolean reference, boolean fixedSize,
        boolean elementReference, Charset charset);

    <T> void serialize(Collection<T> value, Class<?> elementClazz);

    <T> void serialize(@Nullable Collection<@Nullable T> value, boolean reference, Class<?> elementClazz,
        boolean elementReference);

    void serialize(Collection<String> value, Charset charset);

    void serialize(@Nullable Collection<@Nullable String> value, boolean reference, boolean elementReference,
        Charset charset);

    <K, V> void serialize(Map<K, V> value, Class<?> keyClazz, Class<?> valueClazz);

    <K, V> void serialize(@Nullable Map<@Nullable K, @Nullable V> value, boolean reference, Class<?> keyClazz,
        boolean keyReference, Class<?> valueClazz, boolean valueRefence);

    <K> void serialize(Map<K, String> value, Class<?> keyClazz, Charset valueCharset);

    <K> void serialize(@Nullable Map<@Nullable K, @Nullable String> value, boolean reference, Class<?> keyClazz,
        boolean keyReference, boolean valueRefence, Charset valueCharset);

    <V> void serialize(Map<String, V> value, Charset keyCharset, Class<?> valueClazz);

    <V> void serialize(@Nullable Map<@Nullable String,@Nullable V> value, boolean reference, boolean keyReference,
        Charset keyCharset, Class<?> valueClazz, boolean valueRefence);

    void serialize(Map<String, String> value, Charset keyCharset, Charset valueCharset);

    void serialize(@Nullable Map<@Nullable String, @Nullable String> value, boolean reference, boolean keyReference,
        Charset keyCharset, boolean valueRefence, Charset valueCharset);

    <T> void serialize(@Nullable T value, CspTypeToken<T> cspTypeToken);
}

