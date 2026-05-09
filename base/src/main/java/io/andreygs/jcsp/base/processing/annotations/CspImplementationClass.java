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

package io.andreygs.jcsp.base.processing.annotations;

/**
 * Defines class that should be created when class instance is deserialized
 * <p>
 * The most obvious case when it may be handy is for deserialization of collections and maps if they are not created
 * with owner instance creation. And you should note that if some instance already created by its framing object before
 * this processing starts, then this annotation would have no effect - that is, no new instance would be created.
 * This is so because java-class definition is primary and CSP setting should not contradict to it. But, if you really
 * need to set some special class as instance of some or another interface (or superclass), then you can register
 * separate processor(s) to handle whole framing class instance as you wish. However, such scenario should be extremely
 * rare.
 * <p>
 * Here an example of possible case:
 * <pre>
 *     &#64;CspProcessorAutoGeneratable
 *     public class CspImplementationClassExample
 *     {
 *          &#64;CspSerializable(0)
 *          &#64;CspImplementationClass(ArrayList.class)
 *          private List&lt;&#64;CspImplementationClass(HashMap.class) Map&lt;String, Integer>> someListOfMaps;
 *
 *          ...
 *     }
 * </pre>
 * This applicable only for deserialization of non-immutable types and as a consequence has no effect on
 * {@link java.lang.String}, {@link java.lang.Byte}, {@link java.lang.Short}, {@link java.lang.Integer},
 * {@link java.lang.Long}, {@link java.lang.Character}, {@link java.lang.Float}, {@link java.lang.Double},
 * {@link java.lang.Boolean} and arrays, excluding their non-array elements. Provided class should extend declared
 * one. And it not dictates what processor will be used in deserialization - by default it defines by declared type or
 * can be overridden by {@link CspOverrideProcessorClass}.
 * <p>
 * Has no effect on serialization process.
 */
public @interface CspImplementationClass
{
    /**
     * Class that should be created to deserialize instance.
     *
     * @return class that should be created to deserialize instance.
     */
    Class<?> value();
}
