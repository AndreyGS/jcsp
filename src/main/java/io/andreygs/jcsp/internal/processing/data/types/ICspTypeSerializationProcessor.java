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

package io.andreygs.jcsp.internal.processing.data.types;

import io.andreygs.jcsp.api.processing.data.ICspClassSerializationProcessor;
import io.andreygs.jcsp.internal.processing.data.ICspExtendedSerializationProcessor;
import io.andreygs.jcsp.api.model.annotations.CspCreateProcessor;
import org.jetbrains.annotations.Nullable;

/**
 * Processor which encapsulate CSP type properties for call one of {@link ICspExtendedSerializationProcessor} methods.
 * <p>
 * The primary intents of encapsulate properties is for autogeneration of {@link ICspClassSerializationProcessor} for
 * type annotated with {@link CspCreateProcessor} and for proper serialization of java generic types.
 * <p>
 * First intent actually is only for internal use by jcsp library, but the latter is handy for use by clients that wants
 * for serialize their generics.
 */
public interface ICspTypeSerializationProcessor<T>
{
    /**
     * Serialize a value.
     *
     * @param value Value to be serialized.
     * @param cspExtendedSerializationProcessor Extended general-purpose CSP serialization processor for CSP Data
     *                                          Message Body.
     */
    void serialize(@Nullable T value, ICspExtendedSerializationProcessor cspExtendedSerializationProcessor);
}
