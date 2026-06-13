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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.AnnotatedType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

/**
 * Unit-tests for {@link CspTypeProcessorRegistry}.
 */
@ExtendWith(MockitoExtension.class)
public class CspTypeProcessorRegistryTests
{
    @Mock
    private ICspTypeSerializationProcessor typeSerializationProcessor;

    private ICspTypeProcessorRegistry<ICspTypeSerializationProcessor> registry;
    private AnnotatedType annotatedType;

    @BeforeEach
    public void setUp()
    {
        registry = new CspTypeProcessorRegistry<>();
        annotatedType = new CspTypeToken<TestClass<String>>(){}.getAnnotatedType();
    }

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
        assertThatThrownBy(() -> registry.register(null, typeSerializationProcessor))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    @SuppressWarnings("DataFlowIssue" /* Intentional contract nullability violation for test */)
    public void testRegisterNullProcessor()
    {
        assertThatThrownBy(() -> registry.register(annotatedType, null))
            .isInstanceOf(NullPointerException.class);
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
        assertThatThrownBy(() -> registry.find(null)).isInstanceOf(NullPointerException.class);
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
        assertThatThrownBy(() -> registry.unregister(null)).isInstanceOf(NullPointerException.class);
    }

    private static class TestClass<T>
    {
    }
}
