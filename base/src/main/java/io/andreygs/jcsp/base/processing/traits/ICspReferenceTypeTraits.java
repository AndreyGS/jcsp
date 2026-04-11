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

package io.andreygs.jcsp.base.processing.traits;

/**
 * CSP traits that can be applied to serialized/deserialized type with reference type.
 * <p>
 * These traits dictate how field should be serialized/deserialized.
 * Because {@link io.andreygs.jcsp.base.processing.ICspDataGeneralSerializationProcessor} already have methods with
 * all necessary traits as parameters for all non-generic and single dimension array types, use of this interface and
 * all {@code traits} facility is only need when you need to set custom traits to the very specific handling of field
 * seerialization/deserialization.
 * <p>
 * This is common interface for describing reference type (not primitive) properties.<br/>
 * Common properties are:
 * <ul>
 *     <li>{@link Class} type</li>
 *     <li>Should object be serialized/deserialized as reference or not</li>
 * </ul>
 *
 * @see ICspGenericTypeTraitsBuilder
 */
public interface ICspReferenceTypeTraits
{
    /**
     * Gets {@link Class} of object described by this type trait.
     *
     * @return {@link Class} of object.
     */
    Class<?> getClazz();

    /**
     * Returns the trait indicating that object should be serialized/deserialized as reference or embedded structure.
     *
     * @return true if it should be serialized/deserialized as reference and false otherwise.
     */
    boolean isReference();

    /**
     * Copying current instance with overridden reference trait.
     * <p>
     * This can be useful when processing generic object which itself has some generic field with at least one generic
     * type defined by class but wants to threat it always like reference or embedded object regardless of object type
     * trait.
     * <p>
     * Example of such class is:
     * <pre>
     *     class TrickyGeneric&lt;T>
     *     {
     *         T ownKey;
     *         List&lt;T> relatedKeys;
     *         ...
     *     }
     * </pre>
     * Here {@link ICspReferenceTypeTraits} for T type can have reference trait equal to false. But relatedKeys can
     * contain reference type elements, if they are refer to other keys, for instance. And if so, traits for this field
     * can be obtained using this method.
     * <p>
     * The given example is not a guide for action for every similar class processing, but only shows when and how you
     * can use this method if you ever need it.
     *
     * @return copy of current instance that can be safely used without changing of original one.
     */
    ICspReferenceTypeTraits copyWithOverriddenReferenceTrait(boolean reference);
}
