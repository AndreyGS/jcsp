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

package io.andreygs.jcsp.base.processing.internal;

import io.andreygs.jcsp.base.processing.context.ICspDataMessageDeserializationContext;
import io.andreygs.jcsp.base.processing.context.ICspDataMessageSerializationContext;

import java.util.function.BiConsumer;

/**
 * Provider of specialized serialization and deserialization methods.
 * <p>
 * {@link io.andreygs.jcsp.base.processing.CspMessageBodyProcessor} has serialize and deserialize methods
 * overloads for some base types. All other classes are processed by using methods from this provider.
 * <p>
 * Thread-safe.
 */
public interface ICspProcessingMethodProvider
{
    /**
     * Provides specialized serialization method for selected class.
     *
     * @param clazz Class which need of specialized serialization method.
     * @return specialized serialization method for the selected class.
     *
     * @throws io.andreygs.jcsp.base.types.CspRuntimeException when there is no specialized serialization method.
     */
    BiConsumer<Object, ICspDataMessageSerializationContext> provideSerializationMethod(Class<?> clazz);

    /**
     * Provides specialized deserialization method for the selected class.
     *
     * @param clazz Class which need of specialized deserialization method.
     * @return specialized deserialization method for the selected class.
     *
     * @throws io.andreygs.jcsp.base.types.CspRuntimeException when there is no specialized deserialization  method.
     */
    BiConsumer<ICspDataMessageDeserializationContext, Object> provideDeserializationMethod(Class<?> clazz);
}
