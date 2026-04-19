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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Setting for overriding declared class in serialization and deserialization.
 * <p>
 * The most obvious case when it may be handy is for deserialization of collections and maps if they are not created
 * with owner instance creation.
 * <p>
 * Here an example of possible cases:
 * <pre>
 *     // file OverrideClassExample.java
 *     &#64;CspProcessorAutoGeneratable
 *     public class OverrideClassExample
 *     {
 *          &#64;CspSerializable(0)
 *          &#64;CspClassOverride(ArrayList.class)
 *          private List&lt;&#64;CspClassOverride(HashMap.class) Map&lt;String, Integer>> someListOfMaps;
 *
 *          &#64;CspSerializable(1)
 *          &#64;CspClassOverride(InnerClassExample.class)
 *          private IOverrideExample overrideExample;
 *     }
 *
 *     // file IOverrideExample.java
 *     public interface IOverrideExample
 *     {
 *         void f();
 *     }
 *
 *     // file AnotherOverrideClassExample.java
 *     public static class AnotherOverrideClassExample implements IOverrideExample
 *     {
 *         &#64;Override
 *         void f()
 *         {
 *              System.out.println("AnotherOverrideClassExample");
 *         }
 *     }
 * </pre>
 * Of course, you should have a very good reason to do this override on any type except {@link java.util.Collection}
 * or {@link java.util.Map} and their derivatives. And if you still need some of such, consider to make
 * {@link io.andreygs.jcsp.base.processing.ICspDataSerializationProcessor} and (or)
 * {@link io.andreygs.jcsp.base.processing.ICspDataDeserializationProcessor} for type owning objects with such
 * interfaces or maybe even classes, or for interfaces (classes) themselves. In such processors you can apply additional
 * logic to decide which class (or interface, if candidate interface has its own processor) is suitable for
 * deserialization (or sometimes, serialization too).
 * <p>
 * Also, if you had to deal with non-trivial generics you should also look at
 * {@link io.andreygs.jcsp.base.processing.composite.ICspDataCompositeSerializationProcessor} and
 * {@link io.andreygs.jcsp.base.processing.composite.ICspDataCompositeDeserializationProcessor}. If field of your
 * interest has such composite type and one or more of its components has interface types that need special handling
 * they are also can add to you desiring capability.
 * <p>
 * Note, that by default serialization of {@link java.util.Collection}, {@link java.util.Map} and their derivatives
 * is using standard algorithm that is independent of specific implementation. Deserialization of fields with the same
 * types also using standard algorithm, but only if field was initialized by instance when object owner was created.
 * For other cases you should use this annotation or custom processors mentioned above.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE_PARAMETER, ElementType.TYPE_USE})
public @interface CspClassOverride
{
    /**
     * Class which overrides original one in serialiation/deserialization process.
     * <p>
     * This is most useful when
     * @return effective clazz for serialization/deserialization.
     */
    Class<?> value();
}
