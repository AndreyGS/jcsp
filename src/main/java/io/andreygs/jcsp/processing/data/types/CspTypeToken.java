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

package io.andreygs.jcsp.processing.data.types;

import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;

/**
 * Type token to get annotated type and use it in CSP serialization or deserialization.
 * <p>
 * Example of use:
 * <pre>
 *     // file GenericExample.java
 *     public class GenericExample<T>
 *     {
 *         public int someInt;
 *         public T someObject;
 *     }
 *
 *     // file AnotherClassExample.java
 *     public class AnotherClassExample
 *     {
 *         public List&lt@CspReference GenericExample&ltMap&lt@CspString("UTF-16BE") String, Integer>>> list;
 *     }
 *
 *     // file AnotherClassExampleProcessor.java
 *     public class AnotherClassExampleProcessor
 *         implements ICspDataSerializationProcessor&ltAnotherClassExampleProcessor>
 *     {
 *         public void serialize(AnotherClassExample value, ICspDataGeneralSerializationProcessor generalProcessor)
 *         {
 *              generalProcessor.serialize(value.list, new CspTypeToken&ltList&lt@CspReference GenericExample&ltMap&lt@CspString("UTF-16BE") String, Integer>>>>);
 *         }
 *     }
 *
 *     // file GenericExampleProcessor.java
 *     public class GenericExampleProcessor&ltT>
 *         implements ICspDataSerializationProcessor&ltGenericExample&lt?>>
 *     {
 *         public void serialize(GenericExample&lt?> value, ICspDataGeneralSerializationProcessor generalProcessor)
 *         {
 *              generalProcessor.serialize(value.someInt);
 *              generalProcessor.serialize(value.someObject, new CspTypeToken&ltT>);
 *         }
 *     }
 * </pre>
 * In provided example AnotherClassExampleProcessor is using to serialize AnotherClassExample and using
 * {@link CspTypeToken} to provide to generalProcessor definition of "list" field type and GenericExampleProcessor
 * is using {@link CspTypeToken} to provide to  generalProcessor definition os "someObject" field type.
 */
public abstract class CspTypeToken<T>
{
    private final AnnotatedType annotatedType;

    /**
     * Constructs an instance.
     *
     * @throws IllegalArgumentException if supertype of class that extends {@link CspTypeToken} is not generic.
     * For example class X extends CspTypeToken&ltString> and class Y extends X. Then for new Y() this exception will
     * be thrown.
     */
    protected CspTypeToken()
    {
        annotatedType = extractAnnotatedType();
    }

    /**
     * Gets annotated type of argument of first generic parameter of superclass of specific class that extends
     * {@link CspTypeToken}.
     * <p>
     * For example: for {@code new CspTypeToken&lt@CspReference Integer>()} it will return annotated type with
     * {@code @CspReference Integer}
     *
     * @return annotated type first generic parameter of subclass..
     */
    public AnnotatedType getAnnotatedType()
    {
        return annotatedType;
    }

    /**
     * Extracts annotated type of first generic class argument of supertype of class that extends {@link CspTypeToken}.
     *
     * @return annotated type from subclass first generic class argument
     * @throws IllegalArgumentException if supertype of class that extends {@link CspTypeToken} is not generic.
     * For example class X extends CspTypeToken&ltString> and class Y extends X. Then for new Y() this exception will
     * be thrown.
     */
    private AnnotatedType extractAnnotatedType()
    {
        AnnotatedType annotatedType = getClass().getAnnotatedSuperclass();
        if (!(annotatedType instanceof AnnotatedParameterizedType annotatedParameterizedType))
        {
            throw new IllegalArgumentException(Messages.CspTypeToken_Specific_token_class_must_be_generic);
        }
        return annotatedParameterizedType.getAnnotatedActualTypeArguments()[0];
    }
}
