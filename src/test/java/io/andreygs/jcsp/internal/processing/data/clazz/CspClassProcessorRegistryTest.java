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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.mockito.Mockito.when;

/**
 * Unit-tests for {@link CspClassProcessorRegistry}.
 */
@ExtendWith(MockitoExtension.class)
public class CspClassProcessorRegistryTest
{
    @Mock
    private ICspClassProcessorDescriptorGenerator cspClassProcessorDescriptorGenerator;
    @Mock
    private ICspClassProcessorDescriptor<ICspClassSerializationProcessor<?>> classProcessorDescriptor;
    @Mock
    private ICspClassSerializationProcessor<TestClass> classProcessor;
    @InjectMocks
    private CspClassProcessorRegistry<ICspClassSerializationProcessor<?>> registry;

    @Test
    @SuppressWarnings("DataFlowIssue" /* Intentional contract nullability violation for test */)
    public void testConstructorNullDescriptorGenerator()
    {
        assertThatNullPointerException()
            .isThrownBy(() -> new CspClassProcessorRegistry<ICspClassSerializationProcessor<?>>(null));
    }

    @Test
    public void testRegister()
    {
        assertThat(registry.findClassProcessorDescriptor(TestClass.class)).isEmpty();

        when(cspClassProcessorDescriptorGenerator.<ICspClassSerializationProcessor<?>> generate(classProcessor, TestClass.class))
            .thenReturn(classProcessorDescriptor);
        registry.register(TestClass.class, classProcessor);

        assertThat(registry.findClassProcessorDescriptor(TestClass.class)).contains(classProcessorDescriptor);
    }

    @Test
    @SuppressWarnings("DataFlowIssue" /* Intentional contract nullability violation for test */)
    public void testRegisterNullClassProcessor()
    {
        assertThatNullPointerException().isThrownBy(() -> registry.register(TestClass.class, null));
    }

    @Test
    @SuppressWarnings("DataFlowIssue" /* Intentional contract nullability violation for test */)
    public void testRegisterNullClass()
    {
        assertThatNullPointerException().isThrownBy(() -> registry.register(null, classProcessor));
    }

    @Test
    public void testRegisterNotAllowedClass()
    {
        assertThatIllegalArgumentException().isThrownBy(() -> registry.register(int.class, classProcessor))
                                            .withMessageContaining(int.class.getName());
        assertThatIllegalArgumentException().isThrownBy(() -> registry.register(Test[].class, classProcessor))
                                            .withMessageContaining(Test[].class.getName());
        assertThatIllegalArgumentException().isThrownBy(() -> registry.register(String.class, classProcessor))
                                            .withMessageContaining(String.class.getName());
        assertThatIllegalArgumentException().isThrownBy(() -> registry.register(Collection.class, classProcessor))
                                            .withMessageContaining(Collection.class.getName());
        assertThatIllegalArgumentException().isThrownBy(() -> registry.register(Map.class, classProcessor))
                                            .withMessageContaining(Map.class.getName());
    }

    @Test
    public void testRegisterDescriptorGeneratorThrows()
    {
        when(cspClassProcessorDescriptorGenerator.<ICspClassSerializationProcessor<?>> generate(classProcessor, TestClass.class))
            .thenThrow(new IllegalArgumentException());
        assertThatIllegalArgumentException().isThrownBy(() -> registry.register(TestClass.class, classProcessor));
    }

    @Test
    public void testFindClassProcessorDescriptor()
    {
        testRegister();
    }

    @Test
    @SuppressWarnings("DataFlowIssue" /* Intentional contract nullability violation for test */)
    public void testFindClassProcessorDescriptorNull()
    {
        assertThatNullPointerException().isThrownBy(() -> registry.findClassProcessorDescriptor(null));
    }

    @Test
    public void testUnregister()
    {
        when(cspClassProcessorDescriptorGenerator.<ICspClassSerializationProcessor<?>> generate(classProcessor, TestClass.class))
            .thenReturn(classProcessorDescriptor);
        registry.register(TestClass.class, classProcessor);
        registry.unregister(TestClass.class);

        assertThat(registry.findClassProcessorDescriptor(TestClass.class)).isEmpty();
    }

    @Test
    @SuppressWarnings("DataFlowIssue" /* Intentional contract nullability violation for test */)
    public void testUnregisterNullClass()
    {
        assertThatNullPointerException().isThrownBy(() -> registry.unregister(null));
    }

    private static class TestClass
    {
    }
}
