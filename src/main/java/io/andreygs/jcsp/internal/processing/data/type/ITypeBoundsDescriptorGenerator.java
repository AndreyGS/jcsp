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

import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Optional;

/**
 * Generator of type variable bounds descriptor.
 *
 * @apiNote
 * Immutable. Thread-safe.
 *
 * @implSpec
 * Invariants must be enforced at construction time (either via validation or constant values).
 * <p>
 * <b>Implementations MUST adhere to the immutability and self-validation contract.</b>
 */
public interface ITypeBoundsDescriptorGenerator
{
    /**
     * Generates descriptor of type variable bounds.
     *
     * @param typeVariable Type variable which bounds need to be generated.
     * @return optional of type bounds descriptor or an empty optional if type variable is unbound (upper-bounded with
     * Object).
     * @throws IllegalArgumentException if bound type is not supported or if bound category is unknown.
     */
    Optional<ITypeBoundsDescriptor> generate(TypeVariable<? extends Class<?>> typeVariable);

    /**
     * Generates descriptor of wildcard bound.
     *
     * @param wildcardType Wildcard which bounds need to be generated.
     * @return optional of type bounds descriptor or an empty optional if type wildcard is unbound.
     * @throws IllegalArgumentException if bound type is not supported or if bound category is unknown.
     */
    Optional<ITypeBoundsDescriptor> generate(WildcardType wildcardType);
}
