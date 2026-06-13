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

package io.andreygs.jcsp.internal.processing.data.clazz;

import io.andreygs.jcsp.api.processing.data.clazz.ICspClassSerializationProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

/**
 * Unit-tests for {@link CspClassProcessorRegistry}.
 */
@ExtendWith(MockitoExtension.class)
public class CspClassProcessorRegistryTests
{
    @Mock
    private ICspClassProcessorDescriptorGenerator cspClassProcessorDescriptorGenerator;
    @Mock
    private ICspClassProcessorDescriptor<ICspClassSerializationProcessor<?>> classProcessorDescriptor;
    @Mock
    private ICspClassSerializationProcessor<TestClass> classProcessor;

    private ICspClassProcessorRegistry<ICspClassSerializationProcessor<?>> registry;

    @BeforeEach
    public void setUp()
    {
        registry = new CspClassProcessorRegistry<>(cspClassProcessorDescriptorGenerator);
    }

    @Test
    @SuppressWarnings("DataFlowIssue" /* "Intentional contract nullability violation for test" */)
    public void testConstructorNullDescriptorGenerator()
    {
        assertThatThrownBy(() -> new CspClassProcessorRegistry<ICspClassSerializationProcessor<?>>(null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    public void testRegisterClassProcessor()
    {
        assertThat(registry.resolveClassProcessorDescriptor(TestClass.class)).isEmpty();

        when(cspClassProcessorDescriptorGenerator.<ICspClassSerializationProcessor<?>> generate(classProcessor, TestClass.class))
            .thenReturn(classProcessorDescriptor);
        registry.register(TestClass.class, classProcessor);

        assertThat(registry.resolveClassProcessorDescriptor(TestClass.class)).contains(classProcessorDescriptor);
    }

    @Test
    @SuppressWarnings("DataFlowIssue" /* "Intentional contract nullability violation for test" */)
    public void testRegisterClassProcessorNullClassProcessor()
    {
        assertThatThrownBy(() -> registry.register(TestClass.class, null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    @SuppressWarnings("DataFlowIssue" /* "Intentional contract nullability violation for test" */)
    public void testRegisterClassProcessorNullClass()
    {
        assertThatThrownBy(() -> registry.register(null, classProcessor))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    public void testRegisterClassProcessorNotAllowedClass()
    {
        assertThatThrownBy(() -> registry.register(int.class, classProcessor))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> registry.register(Test[].class, classProcessor))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> registry.register(String.class, classProcessor))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> registry.register(Collection.class, classProcessor))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> registry.register(Map.class, classProcessor))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testResolveClassProcessorDescriptor()
    {
        testRegisterClassProcessor();
    }

    @Test
    @SuppressWarnings("DataFlowIssue" /* "Intentional contract nullability violation for test" */)
    public void testResolveClassProcessorDescriptorNull()
    {
        assertThatThrownBy(() -> registry.resolveClassProcessorDescriptor(null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    public void testUnregister()
    {
        when(cspClassProcessorDescriptorGenerator.<ICspClassSerializationProcessor<?>> generate(classProcessor, TestClass.class))
            .thenReturn(classProcessorDescriptor);
        registry.register(TestClass.class, classProcessor);
        registry.unregister(TestClass.class);

        assertThat(registry.resolveClassProcessorDescriptor(TestClass.class)).isEmpty();
    }

    @Test
    @SuppressWarnings("DataFlowIssue" /* "Intentional contract nullability violation for test" */)
    public void testUnregisterNullClass()
    {
        assertThatThrownBy(() -> registry.unregister(null))
            .isInstanceOf(NullPointerException.class);
    }

    private static class TestClass
    {
    }
}
