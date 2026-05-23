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

package io.andreygs.jcsp.api.processing.data;

import io.andreygs.jcsp.api.processing.ICspPrimitiveSerializationProcessor;
import io.andreygs.jcsp.api.model.protocol.utils.CspTypeToken;
import io.andreygs.jcsp.api.model.protocol.CspDataFlag;
import io.andreygs.jcsp.api.model.exception.CspRuntimeException;
import io.andreygs.jcsp.api.model.protocol.CspStatus;
import io.andreygs.jcsp.api.processing.data.clazz.ICspClassSerializationProcessor;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Map;

/**
 * General-purpose CSP serialization processor for Data Message Body.
 * <p>
 * It is the access point on every top-struct and field serialization.
 * {@link ICspClassSerializationProcessor} must use this to serialize struct to which it belongs.
 */
public interface ICspSerializationProcessor
    extends ICspPrimitiveSerializationProcessor
{
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
     *
     * @param value     Value to serialize.
     * @param reference Should field value be threatened as reference (CSP pointer).
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
     *
     * @param value     Value to serialize.
     * @param reference Should field value be threatened as reference (CSP pointer).
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
     *
     * @param value     Value to serialize.
     * @param reference Should field value be threatened as reference (CSP pointer).
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
     *
     * @param value     Value to serialize.
     * @param reference Should field value be threatened as reference (CSP pointer).
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
     *
     * @param value     Value to serialize.
     * @param reference Should field value be threatened as reference (CSP pointer).
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
     * Please, note that using Java char is not recommended in CSP in most cases. Use it only when you really
     * need utf-16 characters that are outside the internals of the {@link String} string and this is explicitly
     * specified in the CSP Interface. However, it seems highly unlikely that this would be the case.
     *
     * @param value     Value to serialize.
     * @param reference Should field value be threatened as reference (CSP pointer).
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
     *
     * @param value     Value to serialize.
     * @param reference Should field value be threatened as reference (CSP pointer).
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
     *
     * @param value     Value to serialize.
     * @param reference Should field value be threatened as reference (CSP pointer).
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
     *
     * @param value     Object to serialize.
     * @param reference Should field value be threatened as reference (CSP pointer).
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
     * @param clazz The class that will be serialized. It is for choice which {@link ICspClassSerializationProcessor}
     *              shall be used, as long as value can implement different interfaces and inherits different classes
     *              (and some of them may be not part of CSP interface),
     * @param <T>   Type of object to serialize.
     * @throws CspRuntimeException if some serialized object fields or their nested fields should be serialized
     *                             as references when {@link CspDataFlag#ALLOW_UNMANAGED_POINTERS} not set and status
     *                             will be {@link CspStatus#POINTER_WHEN_NO_ALLOW_UNMANAGED_POINTERS_SET}.
     *                             if there is no class processor for clazz to handle serialization and status will be
     *                             {@link CspStatus#NO_SUCH_HANDLER}.
     */
    <T> void serialize(T value, Class<?> clazz);

    /**
     * Serializes Object.
     * <p>
     * Object should not be any of: array, {@link String}, {@link Collection}, {@link Map}.
     *
     * @param value     Object to serialize.
     * @param reference Should field value be threatened as reference (CSP pointer).
     * @param clazz     The class that will be serialized. It is for choice which {@link ICspClassSerializationProcessor}
     *                  shall be used, as long as value can implement different interfaces and inherits different classes
     *                  (and some of them may be not part of CSP interface),
     * @param <T>       Type of object to serialize.
     * @throws CspRuntimeException if some serialized object fields or their nested fields should be serialized
     *                             as references when {@link CspDataFlag#ALLOW_UNMANAGED_POINTERS} not set and status
     *                             will be {@link CspStatus#POINTER_WHEN_NO_ALLOW_UNMANAGED_POINTERS_SET}.
     *                             if there is no class processor for clazz to handle serialization and status will be
     *                             {@link CspStatus#NO_SUCH_HANDLER}.
     */
    <T> void serialize(@Nullable T value, boolean reference, Class<?> clazz);

    <T> void serialize(T[] value, Class<?> itemClazz);

    <T> void serialize(@Nullable T @Nullable [] value, boolean reference, boolean fixedSize, Class<?> itemClazz,
        boolean itemReference);

    void serialize(String[] value, Charset charset);

    void serialize(@Nullable String @Nullable [] value, boolean reference, boolean fixedSize, boolean itemReference,
        Charset charset);

    <T> void serialize(Collection<T> value, Class<?> itemClazz);

    <T> void serialize(@Nullable Collection<@Nullable T> value, boolean reference, Class<?> itemClazz,
        boolean itemReference);

    void serialize(Collection<String> value, Charset charset);

    void serialize(@Nullable Collection<@Nullable String> value, boolean reference, boolean itemReference,
        Charset charset);

    <K, V> void serialize(Map<K, V> value, Class<?> keyClazz, Class<?> valueClazz);

    <K, V> void serialize(@Nullable Map<@Nullable K, @Nullable V> value, boolean reference, Class<?> keyClazz,
        boolean keyReference, Class<?> valueClazz, boolean valueRefence);

    <K> void serialize(Map<K, String> value, Class<?> keyClazz, Charset valueCharset);

    <K> void serialize(@Nullable Map<@Nullable K, @Nullable String> value, boolean reference, Class<?> keyClazz,
        boolean keyReference, boolean valueRefence, Charset valueCharset);

    <V> void serialize(Map<String, V> value, Charset keyCharset, Class<?> valueClazz);

    <V> void serialize(@Nullable Map<@Nullable String, @Nullable V> value, boolean reference, boolean keyReference,
        Charset keyCharset, Class<?> valueClazz, boolean valueRefence);

    void serialize(Map<String, String> value, Charset keyCharset, Charset valueCharset);

    void serialize(@Nullable Map<@Nullable String, @Nullable String> value, boolean reference, boolean keyReference,
        Charset keyCharset, boolean valueRefence, Charset valueCharset);

    <T> void serialize(@Nullable T value, CspTypeToken<T> cspTypeToken);
}
