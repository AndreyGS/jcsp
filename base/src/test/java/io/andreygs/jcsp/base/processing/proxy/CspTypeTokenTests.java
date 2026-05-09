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

package io.andreygs.jcsp.base.processing.proxy;

import io.andreygs.jcsp.base.processing.annotations.CspField;
import io.andreygs.jcsp.base.processing.annotations.CspFixedSizeArray;
import io.andreygs.jcsp.base.processing.annotations.CspImplementationClass;
import io.andreygs.jcsp.base.processing.annotations.CspReference;
import io.andreygs.jcsp.base.processing.annotations.CspString;
import org.junit.jupiter.api.Test;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit-tests for {@link CspTypeToken}.
 */
public class CspTypeTokenTests
{
    @Test
    public void testNotGenericSubType()
    {
        class TestClass extends CspTypeToken<Integer>
        {
        }
        class TestClass2 extends TestClass
        {
        }

        assertThatThrownBy(TestClass2::new)
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testCorrectness()
    {
        class TestClass
        {
            @CspField(0)
            public List<Map<@CspReference @CspString("UTF16-BE") String, @CspImplementationClass(ConcurrentHashMap.class) Map<int @CspFixedSizeArray(1) @CspReference [], Long>>> list;
        }
        AnnotatedType annotatedType =
            new CspTypeToken<List<Map<@CspReference @CspString("UTF16-BE") String, @CspImplementationClass(ConcurrentHashMap.class) Map<int @CspFixedSizeArray(1) @CspReference [], Long>>>>(){}.getAnnotatedType();

        Field field;
        try
        {
            field = TestClass.class.getDeclaredField("list");
        }
        catch (NoSuchFieldException e)
        {
            throw new RuntimeException("Test corrupted - no field with name 'list' was found in 'TestClass'!");
        }
        AnnotatedType referenceAnnotatedType = field.getAnnotatedType();

        assertThat(annotatedType)
            .isEqualTo(referenceAnnotatedType);
    }
}
