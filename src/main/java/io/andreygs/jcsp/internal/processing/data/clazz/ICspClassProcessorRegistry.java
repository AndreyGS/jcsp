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

import io.andreygs.jcsp.api.processing.data.type.CspTypeToken;
import io.andreygs.jcsp.api.processing.data.ICspSerializationProcessor;
import io.andreygs.jcsp.internal.processing.data.clazz.dto.ICspClassProcessorDescriptor;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * TODO: place description here
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
     * Don't allowed to register processor for primitive, array, enum, or for {@link String}, {@link Collection} and
     * {@link Map} classes - they are all processed by internal mechanisms.
     * <p>
     * For generics there are also some exclusions: if generic has at least one type variable which has bounds with type
     * {@link GenericArrayType} or {@link ParameterizedType}. That is - only {@link Class} or {@link TypeVariable} are
     * allowed as generic type variable bound.
     * <p>
     * Examples of allowed generics:
     * <ul>
     *     <li>{@code class Example<T, V>}</li>
     *     <li>{@code class Example<T extends Number>}</li>
     *     <li>{@code class Example<T, V extends T>}</li>
     *     <li>{@code class Example<T extends int[]>}</li>
     *     <li>{@code class Example<T extends Number & Runnable>}</li>
     * </ul>
     * Examples of not supported generics:
     * <ul>
     *     <li>{@code class Example<T extends List>}</li>
     *     <li>{@code class Example<T extends List<Integer>>}</li>
     *     <li>{@code class Example<T, V extends T[]>}</li>
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
     *              // it can be serialized with using of {@link ICspSerializationProcessor#serialize(Collection, Class)}
     *              processor.serialize(value.example.list, Integer.class);
     *              // or it can be done like that:
     *              // processor.serialize(value.example.list, new CspTypeToken&ltArrayList&ltInteger>>);
     *              // but using of {@link CspTypeToken} is justified only when there is no other
     *              // {@link ICspSerializationProcessor} methods to handle field serialization
     *         }
     *     }
     * </pre>
     *
     * @param clazz Class that processor should handle.
     * @param classProcessor Class processor that will be used in serialization or deserialization process.
     * @throws IllegalArgumentException if not allowed to register class processor for this class. See the description.
     */
    void registerClassProcessor(Class<?> clazz, P classProcessor);

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
    void unregisterClassProcessor(Class<?> clazz);
}
