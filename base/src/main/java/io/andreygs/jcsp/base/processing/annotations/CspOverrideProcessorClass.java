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
 * Setting for overriding processor that should be used in serialization and deserialization of class instance.
 * <p>
 * Its primary use is for processing of fields with interface types, that have no separate processors, but has one
 * and only one branch of hierarchy up to provided class or only one implementor which object can be present in the
 * specific field. This sounds not very reliable and it is. You should have a very good reason to use this setting.
 * But, there are two special cases when its completely acceptable: when declared type implements or extends
 * Collection or(and) Map. By default, it will be processed as Collection or Map. But if implementor not just a
 * collection but has some additional data to transform and there is specific processor to do that, then this
 * annotation shall be used.
 * <p>
 * Here an example of possible case:
 * <pre>
 *     // file CspOverrideProcessorFramingClassExample.java
 *     &#64;CspProcessorAutoGeneratable
 *     public class CspImplementationClassExample
 *     {
 *          &#64;CspSerializable(0)
 *          &#64;CspOverrideProcessor(CspOverrideProcessorExample.class)
 *          private ICspImplementationClassExample implementationExample;
 *
 *          ...
 *     }
 *
 *     // file ICspOverrideProcessorExample.java
 *     public interface ICspOverrideProcessorExample
 *     {
 *         void f();
 *     }
 *
 *     // file CspOverrideProcessorExample.java
 *     public static class CspOverrideProcessorExample implements ICspOverrideProcessorExample
 *     {
 *         &#64;Override
 *         void f()
 *         {
 *              System.out.println("CspOverrideProcessorExample");
 *         }
 *     }
 * </pre>
 * Provided class should extend declared one.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE_PARAMETER, ElementType.TYPE_USE})
public @interface CspOverrideProcessorClass
{
    /**
     * Class which overrides original one in serialiation/deserialization process.
     *
     * @return effective clazz for serialization/deserialization.
     */
    Class<?> value();
}
