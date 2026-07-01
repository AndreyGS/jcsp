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

package io.andreygs.jcsp.internal.processing.data.type.utils;

import io.andreygs.jcsp.api.exception.CspRuntimeException;
import io.andreygs.jcsp.api.protocol.CspStatus;
import org.assertj.core.api.ThrowingConsumer;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Unit-tests for {@link CspTypeUtils}.
 */
public class CspTypeUtilsTest
{
    private static final String INTERNAL_ERROR_MESSAGE = "Internal error";

    private static final ThrowingConsumer<CspRuntimeException> TEST_FOR_ERROR_IN_STRUCT_FORMAT =
        e -> assertThat(e.getCspStatus()).isEqualTo(CspStatus.ERROR_IN_STRUCT_FORMAT);

    @Test
    public void testRequireStringCharset()
    {
        assertThatNoException().isThrownBy(() -> CspTypeUtils.requireStringCharset(StandardCharsets.UTF_16BE));
    }

    @Test
    @SuppressWarnings("DataFlowIssue" /* Intentional contract nullability violation for test */)
    public void testRequireStringCharsetNull()
    {
        assertThatExceptionOfType(CspRuntimeException.class).isThrownBy(() -> CspTypeUtils.requireStringCharset(null))
                                                            .satisfies(TEST_FOR_ERROR_IN_STRUCT_FORMAT);
    }

    @Test
    public void testRequireClassForCollectionProcessing()
    {
        assertThat(CspTypeUtils.requireClassForCollectionProcessing(null, List.class)).isEqualTo(Collection.class);
    }

    @Test
    public void testRequireClassForCollectionProcessingWithOverride()
    {
        assertThat(CspTypeUtils.requireClassForCollectionProcessing(Collection.class, Collection.class))
            .isEqualTo(Collection.class);
        assertThat(CspTypeUtils.requireClassForCollectionProcessing(TestCollection1.class, Collection.class))
            .isEqualTo(TestCollection1.class);
        assertThat(CspTypeUtils.requireClassForCollectionProcessing(TestCollection1.class, List.class))
            .isEqualTo(TestCollection1.class);
        assertThat(CspTypeUtils.requireClassForCollectionProcessing(TestCollection2.class, TestCollection2.class))
            .isEqualTo(TestCollection2.class);
        assertThat(CspTypeUtils.requireClassForCollectionProcessing(TestCollection3.class, TestCollection2.class))
            .isEqualTo(TestCollection3.class);
    }

    @Test
    public void testRequireClassForCollectionProcessingWithOverrideWithDifferentTypeParameterNumber()
    {
        assertThatExceptionOfType(CspRuntimeException.class)
            .isThrownBy(() -> CspTypeUtils.requireClassForCollectionProcessing(TestCollection2.class, Collection.class))
            .satisfies(TEST_FOR_ERROR_IN_STRUCT_FORMAT)
            .withMessageContaining(TestCollection2.class.getName())
            .withMessageContaining(Collection.class.getName());
    }

    @Test
    public void testRequireClassForCollectionProcessingWithOverrideWithNotInheritedClass()
    {
        assertThatExceptionOfType(CspRuntimeException.class)
            .isThrownBy(() -> CspTypeUtils.requireClassForCollectionProcessing(TestClass1.class, Collection.class))
            .satisfies(TEST_FOR_ERROR_IN_STRUCT_FORMAT)
            .withMessageContaining(TestClass1.class.getName())
            .withMessageContaining(Collection.class.getName());
    }

    @Test
    public void testRequireClassForCollectionProcessingDeclaredClassDoesNotExtendCollection()
    {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> CspTypeUtils.requireClassForCollectionProcessing(null, TestClass1.class))
            .withMessageContaining(TestClass1.class.getName())
            .withMessageContaining(Collection.class.getName());
    }

    @Test
    @SuppressWarnings("DataFlowIssue" /* Intentional contract nullability violation for test */)
    public void testRequireClassForCollectionProcessingDeclaredClassIsNull()
    {
        assertThatNullPointerException().isThrownBy(() -> CspTypeUtils.requireClassForCollectionProcessing(List.class, null));
    }

    @Test
    public void testRequireClassForMapProcessing()
    {
        assertThat(CspTypeUtils.requireClassForMapProcessing(null, HashMap.class)).isEqualTo(Map.class);
    }

    @Test
    public void testRequireClassForMapProcessingWithOverride()
    {
        assertThat(CspTypeUtils.requireClassForMapProcessing(Map.class, Map.class))
            .isEqualTo(Map.class);
        assertThat(CspTypeUtils.requireClassForMapProcessing(TestMap1.class, Map.class))
            .isEqualTo(TestMap1.class);
        assertThat(CspTypeUtils.requireClassForMapProcessing(TestMap1.class, HashMap.class))
            .isEqualTo(TestMap1.class);
        assertThat(CspTypeUtils.requireClassForMapProcessing(TestMap2.class, TestMap2.class))
            .isEqualTo(TestMap2.class);
        assertThat(CspTypeUtils.requireClassForMapProcessing(TestMap4.class, TestMap3.class))
            .isEqualTo(TestMap4.class);
    }

    @Test
    public void testRequireClassForMapProcessingWithOverrideWithDifferentTypeParameterNumber()
    {
        assertThatExceptionOfType(CspRuntimeException.class)
            .isThrownBy(() -> CspTypeUtils.requireClassForMapProcessing(TestMap2.class, Map.class))
            .satisfies(TEST_FOR_ERROR_IN_STRUCT_FORMAT)
            .withMessageContaining(TestMap2.class.getName())
            .withMessageContaining(Map.class.getName());

        assertThatExceptionOfType(CspRuntimeException.class)
            .isThrownBy(() -> CspTypeUtils.requireClassForMapProcessing(TestMap3.class, Map.class))
            .satisfies(TEST_FOR_ERROR_IN_STRUCT_FORMAT)
            .withMessageContaining(TestMap3.class.getName())
            .withMessageContaining(Map.class.getName());
    }

    @Test
    public void testRequireClassForMapProcessingWithOverrideWithNotInheritedClass()
    {
        assertThatExceptionOfType(CspRuntimeException.class)
            .isThrownBy(() -> CspTypeUtils.requireClassForMapProcessing(TestClass2.class, Map.class))
            .satisfies(TEST_FOR_ERROR_IN_STRUCT_FORMAT)
            .withMessageContaining(TestClass2.class.getName())
            .withMessageContaining(Map.class.getName());
    }

    @Test
    public void testRequireClassForCollectionProcessingDeclaredClassDoesNotExtendMap()
    {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> CspTypeUtils.requireClassForMapProcessing(null, TestClass2.class))
            .withMessageContaining(TestClass2.class.getName())
            .withMessageContaining(Map.class.getName());
    }

    @Test
    @SuppressWarnings("DataFlowIssue" /* Intentional contract nullability violation for test */)
    public void testRequireClassForMapProcessingDeclaredClassIsNull()
    {
        assertThatNullPointerException().isThrownBy(() -> CspTypeUtils.requireClassForMapProcessing(Map.class, null));
    }

    private static class TestCollection1<T> extends ArrayList<T>
    {
    }

    private static class TestCollection2 extends TestCollection1<String>
    {
    }

    private static class TestCollection3 extends TestCollection2
    {
    }

    @SuppressWarnings("unused" /* Parameters are need for tests of work with generic classes */)
    private static class TestClass1<T>
    {
    }

    private static class TestMap1<K, V> extends HashMap<K, V>
    {
    }

    private static class TestMap2<K> extends TestMap1<K, String>
    {
    }

    private static class TestMap3 extends TestMap2<String>
    {
    }

    private static class TestMap4 extends TestMap3
    {
    }

    @SuppressWarnings("unused" /* Parameters are need for tests of work with generic classes */)
    private static class TestClass2<K, V>
    {
    }
}
