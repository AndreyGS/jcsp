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

package io.andreygs.jcsp.api.controller;

import io.andreygs.jcsp.api.processing.buffer.ISerializationBufferConfig;
import io.andreygs.jcsp.api.processing.buffer.factory.ISerializationBufferConfigFactory;
import io.andreygs.jcsp.api.processing.data.ICspDataSerializationProcessor;
import io.andreygs.jcsp.api.processing.data.type.CspTypeToken;
import io.andreygs.jcsp.api.protocol.message.ICspDataMessage;
import io.andreygs.jcsp.api.protocol.message.config.ICspDataMessageConfigExtension;
import io.andreygs.jcsp.api.protocol.ICspVersionable;
import io.andreygs.jcsp.api.protocol.message.config.ICspMessageConfig;
import io.andreygs.jcsp.api.protocol.message.config.factory.ICspMessageConfigFactory;
import io.andreygs.jcsp.api.processing.data.clazz.ICspClassSerializationProcessor;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.AnnotatedType;
import java.util.Collection;
import java.util.Map;

/**
 * TODO: place description here
 *
 * ALl implementations of current interface must be thread-safe.
 */
public interface ICspSerializationSession
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
     * <p>
     * For generics there are also some exclusions: generic don't allowed to have any parameter with bound as
     * parametrized class.
     * <p>
     * Examples of allowed generics:
     * <ul>
     *     <li>{@code class Example<T, V>}</li>
     *     <li>{@code class Example<T extends Number>}</li>
     *     <li>{@code class Example<T, V extends T>}</li>
     *     <li>{@code class Example<T extends Number & Runnable>}</li>
     * </ul>
     * Examples of not supported generics:
     * <ul>
     *     <li>{@code class Example<T extends List>}</li>
     *     <li>{@code class Example<T, V extends List>}</li>
     *     <li>{@code class Example<T extends List<Integer>>}</li>
     *     <li>{@code class Example<T extends List<Integer> & Runnable}</li>
     *     <li>{@code class Example<T extends List<? extends Number>>}</li>
     * </ul>
     * <p>
     * For serialization of not supported generics some wrapping class with separate class processor should be used.
     * Example:
     * <pre>
     *     // file Example.java
     *     public class Example&ltT extends List&ltInteger>>
     *     {
     *         public T list;
     *     }
     *
     *     // file WrapperExample.java
     *     public class WrapperExample
     *     {
     *         public Example&ltArrayList&ltInteger>> example;
     *     }
     *
     *     // file WrapperExampleClassProcessor
     *     public class WrapperExampleClassProcessor
     *         implements ICspDataSerializationProcessor&ltWrapperExample>
     *     {
     *         public void serialize(WrapperExample&lt?> value, ICspSerializationProcessor processor)
     *         {
     *              // it can be serialized with using of {@link ICspDataSerializationProcessor#serialize(Collection, Class)}
     *              processor.serialize(value.example.list, Integer.class);
     *              // or it can be done like that:
     *              // processor.serialize(value.example.list, new CspTypeToken&ltArrayList&ltInteger>>);
     *              // but using of {@link CspTypeToken} is justified only when there is no other
     *              // {@link ICspDataSerializationProcessor} methods to handle field serialization
     *         }
     *     }
     * </pre>
     *
     * @param clazz Class that processor should handle.
     * @param classProcessor Class processor that will be used in serialization or deserialization process.
     * @throws IllegalArgumentException if not allowed to register class processor for this class. See the description.
     */
    <T> void registerClassProcessor(Class<T> clazz, ICspClassSerializationProcessor<T> classProcessor);

    void unregisterClassProcessor(Class<?> clazz);

    void unregisterTypeProcessor(AnnotatedType annotatedType);

    void setDefaultBufferConfig(ISerializationBufferConfig config);

    void setDefaultMessageConfig(ICspMessageConfig config);

    void setDefaultDataMessageConfigExtension(ICspDataMessageConfigExtension config);

    ISerializationBufferConfigFactory getSerializationBufferConfigFactory();

    ICspMessageConfigFactory getCspMessageConfigFactory();

    <T extends ICspVersionable> ICspDataMessage<T> serializeData(ICspVersionable struct, Class<T> clazz);

    <T extends ICspVersionable> ICspDataMessage<T> serializeData(ICspVersionable struct, Class<T> clazz,
        @Nullable ISerializationBufferConfig customBufferConfig, @Nullable ICspMessageConfig customMessageConfig,
        @Nullable ICspDataMessageConfigExtension customDataMessageConfigExtension);
}
