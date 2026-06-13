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

import io.andreygs.jcsp.internal.processing.data.type.model.TypeBoundKind;
import io.andreygs.jcsp.internal.processing.data.type.model.TypeIdKind;

import java.util.Optional;
import java.util.Set;

/**
 * Generic parameter type bounds descriptor.
 *
 * @apiNote
 * Immutable. Thread-safe.
 *
 * @implSpec
 * Invariants must be enforced at construction time (either via validation or constant values).
 * <p>
 * <b>Implementations MUST adhere to the immutability and self-validation contract.</b>
 */
public interface ITypeBoundsDescriptor
{
    /**
     * Gets kind of type bounds.
     *
     * @return kind of type bounds.
     */
    TypeBoundKind getTypeBoundKind();

    /**
     * Gets kind of type identifier.
     *
     * @return {@link TypeIdKind#TYPE_VARIABLE_NAME} if bound is a type variable and {@link TypeIdKind#CLASS} otherwise.
     */
    TypeIdKind getTypeIdKind();

    /**
     * Gets classes of bounds.
     *
     * @return non-empty {@link Set} if {@link #getTypeIdKind()} returns {@link TypeIdKind#CLASS} and empty set
     * otherwise.
     */
    Set<Class<?>> getBoundClasses();

    /**
     * Gets name of bound type variable (can be only one bound type variable according to Java specification).
     *
     * @return non-empty {@link Optional} with non-empty {@link String} if {@link #getTypeIdKind()} returns
     * {@link TypeIdKind#TYPE_VARIABLE_NAME} and empty {@link Optional} otherwise.
     */
    Optional<String> getBoundTypeVariableName();
}
