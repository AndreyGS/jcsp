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
import io.andreygs.jcsp.internal.processing.data.clazz.dto.ICspClassProcessorDescriptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

/**
 * TODO: place description here
 */
@ExtendWith(MockitoExtension.class)
public class CspClassProcessorRegistryTests
{
    @Mock
    private ICspClassProcessorDescriptorGenerator cspClassProcessorDescriptorGenerator;

    @Mock
    private ICspClassProcessorDescriptor<ICspClassSerializationProcessor<?>> classProcessorDescriptor;

    @Mock
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

        ICspClassSerializationProcessor<TestClass> classProcessor =  (value, dataProcessor) -> {};
        when(cspClassProcessorDescriptorGenerator.<ICspClassSerializationProcessor<?>> generate(classProcessor, TestClass.class))
            .thenReturn(classProcessorDescriptor);
        registry.registerClassProcessor(TestClass.class, classProcessor);

        assertThat(registry.resolveClassProcessorDescriptor(TestClass.class))
            .isPresent()
            .flatMap(descriptor -> Optional.of(descriptor.getClassProcessor()))
            .get().isEqualTo(classProcessor);
    }

    private static class TestClass
    {
    }
}
