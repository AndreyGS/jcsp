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

package io.andreygs.jcsp.internal.processing.data.clazz;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * Registry for class processors.
 * <p>
 * It registers class processors in form of {@link ICspClassProcessorDescriptor}.
 *
 * @apiNote
 * Thread-safe.
 *
 * @param <P> type of class processor:
 * {@link io.andreygs.jcsp.api.processing.data.clazz.ICspClassSerializationProcessor} or
 * {@link io.andreygs.jcsp.api.processing.data.clazz.ICspClassDeserializationProcessor}
 */
public interface ICspClassProcessorRegistry<P>
{
    /**
     * Registers processor for ordinary and generic (non-primitive, non-array) type for later use.
     * <p>
     * If class processor already have been registered, then new processor will override previous registration.
     * But if you want to have both processors (by one for different cases, for example) you should create new registry,
     * fill it with all necessary processors including current one and use different registries in requisite scenarios.
     * <p>
     * Don't allowed to register processor for primitive, array, {@link String}, {@link Collection} and {@link Map}
     * classes - they are all processed by internal mechanisms.
     *
     * @param clazz Class that processor should handle.
     * @param classProcessor Class processor that will be used in serialization or deserialization process.
     * @throws IllegalArgumentException if class processor cannot be registered.
     */
    void register(Class<?> clazz, P classProcessor);

    /**
     * Resolves processor descriptor for chosen class.
     *
     * @param clazz Class which processor descriptor need to be resolved.
     * @return non-empty optional class processor descriptor if registered and empty if not.
     */
    Optional<ICspClassProcessorDescriptor<P>> resolveClassProcessorDescriptor(Class<?> clazz);

    /**
     * Unregisters class processor of provided class.
     *
     * @param clazz Processor class.
     */
    void unregister(Class<?> clazz);
}
