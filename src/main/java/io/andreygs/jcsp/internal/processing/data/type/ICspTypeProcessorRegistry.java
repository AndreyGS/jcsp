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

package io.andreygs.jcsp.internal.processing.data.type;

import java.lang.reflect.AnnotatedType;
import java.util.Optional;

/**
 * TODO: place description here
 */
public interface ICspTypeProcessorRegistry<P>
{
    /**
     * Registers processor for type for later use.
     * <p>
     * If type processor already have been registered, then new processor will override previous registration.
     *
     * @param annotatedType Type which has non-trivial traits, like generic parameters and nested structure.
     *                      For example: List&ltMap&lt@CspReference String, Integer[]>
     * @param typeProcessor Type processor that will be used in serialization or deserialization process.
     */
    void registerTypeProcessor(AnnotatedType annotatedType, P typeProcessor);

    /**
     * Finds already registered processor for chosen type.
     *
     * @param annotatedType Type which processor need to be found.
     * @return optional proxy processor.
     */
    Optional<P> findTypeProcessor(AnnotatedType annotatedType);

    /**
     * Unregisters processor for provided {@link AnnotatedType}.
     *
     * @param annotatedType Type of processor.
     */
    void unregisterTypeProcessor(AnnotatedType annotatedType);
}
