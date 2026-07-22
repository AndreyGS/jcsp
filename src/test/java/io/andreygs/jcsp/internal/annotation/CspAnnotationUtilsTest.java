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

package io.andreygs.jcsp.internal.annotation;

import io.andreygs.jcsp.api.annotation.protocol.CspCreateProcessor;
import io.andreygs.jcsp.api.annotation.protocol.CspField;
import io.andreygs.jcsp.api.annotation.protocol.CspFixedSizeArray;
import io.andreygs.jcsp.api.annotation.protocol.CspImplementationClass;
import io.andreygs.jcsp.api.annotation.protocol.CspOverrideProcessorClass;
import io.andreygs.jcsp.api.annotation.protocol.CspReference;
import io.andreygs.jcsp.api.annotation.protocol.CspString;
import io.andreygs.jcsp.api.processing.data.type.CspTypeToken;
import io.andreygs.jcsp.internal.annotation.utils.CspAnnotationUtils;
import org.junit.jupiter.api.Test;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit-tests for {@link CspAnnotationUtils}.
 */
public class CspAnnotationUtilsTest
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
        @CspCreateProcessor
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

        Optional<Integer> sequenceField0 = CspAnnotationUtils.resolveCspFieldSequence(field0);
        Optional<Integer> sequenceField1 = CspAnnotationUtils.resolveCspFieldSequence(field1);
        Optional<Integer> sequenceNonCspField0 = CspAnnotationUtils.resolveCspFieldSequence(nonCspField0);
        Optional<Integer> sequenceField2 = CspAnnotationUtils.resolveCspFieldSequence(field2);

        assertThat(sequenceField0).contains(0);
        assertThat(sequenceField1).contains(1);
        assertThat(sequenceNonCspField0).isEmpty();
        assertThat(sequenceField2).contains(2);
    }

    @Test
    public void testResolveCspFixedArraySizeExists()
    {
        AnnotatedType annotatedType1 = new CspTypeToken<Integer @CspFixedSizeArray(5) []>(){}.getAnnotatedType();
        AnnotatedType annotatedType2 = new CspTypeToken<Integer @CspFixedSizeArray(-1) []>(){}.getAnnotatedType();
        AnnotatedType annotatedType3 = new CspTypeToken<Integer @CspFixedSizeArray(0) []>(){}.getAnnotatedType();

        Optional<Integer> fixedSize1 = CspAnnotationUtils.resolveCspFixedArraySize(annotatedType1);
        Optional<Integer> fixedSize2 = CspAnnotationUtils.resolveCspFixedArraySize(annotatedType2);
        Optional<Integer> fixedSize3 = CspAnnotationUtils.resolveCspFixedArraySize(annotatedType3);

        assertThat(fixedSize1).contains(5);
        assertThat(fixedSize2).contains(-1);
        assertThat(fixedSize3).contains(0);
    }

    @Test
    public void testResolveCspFixedArraySizeNotExists()
    {
        AnnotatedType annotatedType = new CspTypeToken<Integer []>(){}.getAnnotatedType();

        Optional<Integer> fixedSize = CspAnnotationUtils.resolveCspFixedArraySize(annotatedType);

        assertThat(fixedSize).isEmpty();
    }

    @Test
    public void testResolveCspFixedArraySizeNotExistsNotArray()
    {
        AnnotatedType annotatedType = new CspTypeToken<Integer>(){}.getAnnotatedType();

        Optional<Integer> fixedSize =  CspAnnotationUtils.resolveCspFixedArraySize(annotatedType);

        assertThat(fixedSize).isEmpty();
    }

    @Test
    public void testResolveCspImplementationClassExists()
    {
        AnnotatedType annotatedType =
            new CspTypeToken<@CspImplementationClass(Set.class) List<@CspString("UTF-8") String>>(){}.getAnnotatedType();

        Optional<Class<?>> implementationClass = CspAnnotationUtils.resolveCspImplementationClass(annotatedType);

        assertThat(implementationClass).contains(Set.class);
    }

    @Test
    public void testResolveCspImplementationClassNotExists()
    {
        AnnotatedType annotatedType = new CspTypeToken<Collection<String>>(){}.getAnnotatedType();

        Optional<Class<?>> implementationClass = CspAnnotationUtils.resolveCspImplementationClass(annotatedType);

        assertThat(implementationClass).isEmpty();
    }

    @Test
    public void testResolveCspOverrideClassExists()
    {
        @SuppressWarnings("unused" /* Field 'i' only for example */)
        class TestClassA
        {
            int i;
        }

        @SuppressWarnings("unused" /* Field 'j' only for example */)
        class TestClassB extends TestClassA
        {
            transient int j;
        }

        @CspCreateProcessor
        class TestClassC extends TestClassB
        {
            @CspField(0)
            public @CspOverrideProcessorClass(TestClassA.class) TestClassB b;
        }

        AnnotatedType annotatedType;
        try
        {
            annotatedType = TestClassC.class.getDeclaredField("b").getAnnotatedType();
        }
        catch (NoSuchFieldException e)
        {
            throw new RuntimeException("Test corrupted - fields 'b' in 'TestClassC' is absent!");
        }

        Optional<Class<?>> overrideProcessorClass = CspAnnotationUtils.resolveCspOverrideProcessorClass(annotatedType);

        assertThat(overrideProcessorClass).contains(TestClassA.class);
    }

    @Test
    public void testResolveCspOverrideClassNotExists()
    {
        AnnotatedType annotatedType = new CspTypeToken<Collection<Integer>>(){}.getAnnotatedType();

        Optional<Class<?>> overrideProcessorClass = CspAnnotationUtils.resolveCspOverrideProcessorClass(annotatedType);

        assertThat(overrideProcessorClass).isEmpty();
    }

    @Test
    public void testIsCspReferenceExists()
    {
        AnnotatedType annotatedType = new CspTypeToken<@CspReference Integer>(){}.getAnnotatedType();

        boolean cspReference = CspAnnotationUtils.isCspReference(annotatedType);

        assertThat(cspReference).isTrue();
    }

    @Test
    public void testIsCspReferenceNotExists()
    {
        AnnotatedType annotatedType = new CspTypeToken<Integer>(){}.getAnnotatedType();

        boolean cspReference = CspAnnotationUtils.isCspReference(annotatedType);

        assertThat(cspReference).isFalse();
    }

    @Test
    public void testResolveCspStringCharsetExists()
    {
        AnnotatedType annotatedType = new CspTypeToken<@CspString("UTF-16BE") String>(){}.getAnnotatedType();

        Optional<Charset> overrideProcessorClass = CspAnnotationUtils.resolveCspStringCharset(annotatedType);

        assertThat(overrideProcessorClass).contains(StandardCharsets.UTF_16BE);
    }

    @Test
    public void testResolveCspStringCharsetNotExists()
    {
        AnnotatedType annotatedType = new CspTypeToken<String>(){}.getAnnotatedType();

        Optional<Charset> overrideProcessorClass = CspAnnotationUtils.resolveCspStringCharset(annotatedType);

        assertThat(overrideProcessorClass).isEmpty();
    }

    @Test
    public void testResolveCspStringCharsetNotValid()
    {
        AnnotatedType annotatedType = new CspTypeToken<@CspString(";UTF-16BE") String>(){}.getAnnotatedType();

        assertThatThrownBy(() -> CspAnnotationUtils.resolveCspStringCharset(annotatedType))
            .isInstanceOf(IllegalCharsetNameException.class);
    }

    @Test
    public void testResolveCspStringCharsetUnsupported()
    {
        AnnotatedType annotatedType = new CspTypeToken<@CspString("UTF-16BEX") String>(){}.getAnnotatedType();

        assertThatThrownBy(() -> CspAnnotationUtils.resolveCspStringCharset(annotatedType))
            .isInstanceOf(UnsupportedCharsetException.class);
    }
}
