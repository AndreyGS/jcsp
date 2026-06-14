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

package io.andreygs.jcsp.api.processing.data.type;

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
 *     // file AnotherClassExampleClassProcessor.java
 *     public class AnotherClassExampleClassProcessor
 *         implements ICspDataSerializationProcessor&ltAnotherClassExample>
 *     {
 *         private static final AnnotatedType LIST_ANNOTATED_TYPE =
 *             new CspTypeToken&ltList&lt@CspReference GenericExample&ltMap&lt@CspString("UTF-16BE") String, Integer>>>>
 *                 .getAnnotatedType();
 *
 *         public void serialize(AnotherClassExample value, ICspSerializationProcessor processor)
 *         {
 *              processor.serialize(value.list, LIST_ANNOTATED_TYPE);
 *         }
 *     }
 *
 *     // file GenericExampleClassProcessor.java
 *     public class GenericExampleClassProcessor&ltT>
 *         implements ICspDataSerializationProcessor&ltGenericExample&lt?>>
 *     {
 *         private static final AnnotatedTYpe SOME_OBJECT_ANNOTATED_TYPE = new CspTypeToken&ltT>().getAnnotatedType();
 *
 *         public void serialize(GenericExample&lt?> value, ICspSerializationProcessor processor)
 *         {
 *              processor.serialize(value.someInt);
 *              processor.serialize(value.someObject, SOME_OBJECT_ANNOTATED_TYPE);
 *         }
 *     }
 * </pre>
 * In provided example AnotherClassExampleClassProcessor is using to serialize AnotherClassExample and using
 * {@link CspTypeToken} to provide to processor {@link AnnotatedType} of "list" field type and
 * GenericExampleClassProcessor is using {@link CspTypeToken} to provide to processor {@link AnnotatedType} of
 * "someObject" field type.
 *
 * @param <T> Is a source of {@link AnnotatedType}, that is gets by {@link #getAnnotatedType()}. Also using for avoiding
 *            of unchecked cast of result when use as parameter of deserialization processor method.
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
     * @return annotated type first generic parameter of subclass.
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
