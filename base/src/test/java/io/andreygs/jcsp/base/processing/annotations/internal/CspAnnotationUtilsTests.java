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

package io.andreygs.jcsp.base.processing.annotations.internal;

import io.andreygs.jcsp.base.processing.annotations.CspCreateProcessor;
import io.andreygs.jcsp.base.processing.annotations.CspField;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit-tests for {@link CspAnnotationUtils}.
 */
public class CspAnnotationUtilsTests
{
    @Test
    public void testIsCspCreateProcessorAnnotationExists()
    {
        @CspCreateProcessor
        class TestClass
        {
        }

        boolean result = CspAnnotationUtils.isCspCreateProcessor(TestClass.class);
        assertThat(result).isTrue();
    }

    @Test
    public void testIsCspCreateProcessorAnnotationNotExists()
    {
        class TestClass
        {
        }

        boolean result = CspAnnotationUtils.isCspCreateProcessor(TestClass.class);
        assertThat(result).isFalse();
    }

    @Test
    public void testResolveCspFieldSequence()
    {
        class TestClass
        {
            @CspField(0)
            public int field0;
            @CspField(1)
            public int field1;
            public int nonCspField0;
            @CspField(2)
            public int field2;
        }

        Field field0;
        Field field1;
        Field nonCspField0;
        Field field2;
        try
        {
            field0 = TestClass.class.getDeclaredField("field0");
            field1 = TestClass.class.getDeclaredField("field1");
            nonCspField0 = TestClass.class.getDeclaredField("nonCspField0");
            field2 = TestClass.class.getDeclaredField("field2");
        }
        catch (NoSuchFieldException e)
        {
            throw new RuntimeException("Test corrupted - one of the fields in 'TestClass' are absent!");
        }

        Optional<Integer> orderField0 = CspAnnotationUtils.resolveCspFieldSequence(field0);
        Optional<Integer> orderField1 = CspAnnotationUtils.resolveCspFieldSequence(field1);
        Optional<Integer> orderNonCspField0 = CspAnnotationUtils.resolveCspFieldSequence(nonCspField0);
        Optional<Integer> orderField2 = CspAnnotationUtils.resolveCspFieldSequence(field2);

        assertThat(orderField0).contains(0);
        assertThat(orderField1).contains(1);
        assertThat(orderNonCspField0).isEmpty();
        assertThat(orderField2).contains(2);
    }
}
