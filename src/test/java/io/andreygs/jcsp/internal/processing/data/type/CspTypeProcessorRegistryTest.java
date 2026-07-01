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

package io.andreygs.jcsp.internal.processing.data.type;

import io.andreygs.jcsp.api.processing.data.type.CspTypeToken;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.AnnotatedType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.mockito.Mockito.mock;

/**
 * Unit-tests for {@link CspTypeProcessorRegistry}.
 */
@ExtendWith(MockitoExtension.class)
public class CspTypeProcessorRegistryTest
{
    @Mock
    private ICspTypeSerializationProcessor typeSerializationProcessor;
    private final CspTypeProcessorRegistry<ICspTypeSerializationProcessor> registry = new CspTypeProcessorRegistry<>();
    private final AnnotatedType annotatedType = new CspTypeToken<TestClass<String>>(){}.getAnnotatedType();

    @Test
    public void testRegister()
    {
        assertThat(registry.find(annotatedType)).isEmpty();
        registry.register(annotatedType, typeSerializationProcessor);
        assertThat(registry.find(annotatedType)).contains(typeSerializationProcessor);
    }

    @Test
    public void testRegisterUpdate()
    {
        registry.register(annotatedType, typeSerializationProcessor);
        ICspTypeSerializationProcessor typeSerializationProcessor2 = mock(ICspTypeSerializationProcessor.class);
        registry.register(annotatedType, typeSerializationProcessor2);
        assertThat(registry.find(annotatedType)).contains(typeSerializationProcessor2);
    }

    @Test
    @SuppressWarnings("DataFlowIssue" /* Intentional contract nullability violation for test */)
    public void testRegisterNullAnnotatedType()
    {
        assertThatNullPointerException().isThrownBy(() -> registry.register(null, typeSerializationProcessor));
    }

    @Test
    @SuppressWarnings("DataFlowIssue" /* Intentional contract nullability violation for test */)
    public void testRegisterNullProcessor()
    {
        assertThatNullPointerException().isThrownBy(() -> registry.register(annotatedType, null));
    }

    @Test
    public void testFind()
    {
        testRegister();
    }

    @Test
    @SuppressWarnings("DataFlowIssue" /* Intentional contract nullability violation for test */)
    public void testFindNullAnnotatedType()
    {
        assertThatNullPointerException().isThrownBy(() -> registry.find(null));
    }

    @Test
    public void testUnregister()
    {
        registry.register(annotatedType, typeSerializationProcessor);
        registry.unregister(annotatedType);
        assertThat(registry.find(annotatedType)).isEmpty();
    }

    @Test
    @SuppressWarnings("DataFlowIssue" /* Intentional contract nullability violation for test */)
    public void testUnregisterNullAnnotatedType()
    {
        assertThatNullPointerException().isThrownBy(() -> registry.unregister(null));
    }

    @SuppressWarnings("unused" /* Parameters are need for tests of work with generic classes */)
    private static class TestClass<T>
    {
    }
}
