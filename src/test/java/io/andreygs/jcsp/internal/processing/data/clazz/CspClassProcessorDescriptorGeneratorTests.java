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
import io.andreygs.jcsp.internal.processing.data.clazz.factory.ICspClassProcessorDescriptorFactory;
import io.andreygs.jcsp.internal.processing.data.type.ITypeVariableDescriptorGenerator;
import io.andreygs.jcsp.internal.processing.data.type.ITypeVariableDescriptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.TypeVariable;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit-tests for {@link CspClassProcessorDescriptorGenerator}.
 */
@ExtendWith(MockitoExtension.class)
public class CspClassProcessorDescriptorGeneratorTests
{
    @Mock
    private ICspClassProcessorDescriptorFactory cspClassProcessorDescriptorFactory;
    @Mock
    private ITypeVariableDescriptorGenerator typeVariableDescriptorGenerator;
    @Mock
    private ICspClassSerializationProcessor<TestClass> classProcessor;
    @Mock
    private ICspClassProcessorDescriptor<ICspClassSerializationProcessor<TestClass>> classProcessorDescriptor;
    @Mock
    private ITypeVariableDescriptor typeVariableDescriptor;
    @InjectMocks
    private CspClassProcessorDescriptorGenerator generator;

    @Test
    @SuppressWarnings("DataFlowIssue" /* Intentional contract nullability violation for test */)
    public void testConstructorNullClassDescriptorFactory()
    {
        assertThatThrownBy(() -> new CspClassProcessorDescriptorGenerator(null, typeVariableDescriptorGenerator))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    @SuppressWarnings("DataFlowIssue" /* Intentional contract nullability violation for test */)
    public void testConstructorNullTypeVariableDescriptorGenerator()
    {
        assertThatThrownBy(() -> new CspClassProcessorDescriptorGenerator(cspClassProcessorDescriptorFactory, null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    public void testGenerateNonGenericClassDescriptor()
    {
        when(cspClassProcessorDescriptorFactory.create(classProcessor, Map.of())).thenReturn(classProcessorDescriptor);
        ICspClassProcessorDescriptor<ICspClassSerializationProcessor<TestClass>> result =
            generator.generate(classProcessor, Test.class);
        assertThat(result).isEqualTo(classProcessorDescriptor);
    }

    @Test
    public void testGenerateGenericClassDescriptor()
    {
        TypeVariable<? extends Class<?>>[] typeVariables = TestGenericClass.class.getTypeParameters();

        when(cspClassProcessorDescriptorFactory.create(eq(classProcessor), anyMap()))
            .thenReturn(classProcessorDescriptor);
        when(typeVariableDescriptorGenerator.generate(typeVariables[0])).thenReturn(typeVariableDescriptor);
        when(typeVariableDescriptorGenerator.generate(typeVariables[1])).thenReturn(typeVariableDescriptor);

        ICspClassProcessorDescriptor<ICspClassSerializationProcessor<TestClass>> result =
            generator.generate(classProcessor, TestGenericClass.class);
        verify(typeVariableDescriptorGenerator).generate(typeVariables[0]);
        verify(typeVariableDescriptorGenerator).generate(typeVariables[1]);
        assertThat(result).isEqualTo(classProcessorDescriptor);
    }

    @Test
    @SuppressWarnings("DataFlowIssue" /* Intentional contract nullability violation for test */)
    public void testGenerateNullProcessor()
    {
        assertThatThrownBy(() -> generator.generate(null, TestClass.class))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    @SuppressWarnings("DataFlowIssue" /* Intentional contract nullability violation for test */)
    public void testGenerateNullClass()
    {
        assertThatThrownBy(() -> generator.generate(classProcessor, null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    public void testGenerateForbiddenClass()
    {
        assertThatThrownBy(() -> generator.generate(classProcessor, Test[].class ))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testGenerateTypeVariableDescriptorThrowsIae()
    {
        Throwable cause = new IllegalArgumentException();
        when(typeVariableDescriptorGenerator.generate(any())).thenThrow(cause);
        Throwable throwable = catchThrowable(() -> generator.generate(classProcessor, TestGenericClass.class));
        assertThat(throwable.getCause()).isEqualTo(cause);
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
        assertThat(throwable.getMessage()).contains(TestGenericClass.class.getName());
    }

    private static class TestClass
    {
    }

    @SuppressWarnings("unused" /* Parameters are need for tests of work with generic classes */)
    private static class TestGenericClass<W, E>
    {
    }
}
