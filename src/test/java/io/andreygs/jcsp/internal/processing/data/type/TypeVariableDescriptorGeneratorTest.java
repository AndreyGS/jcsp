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

import io.andreygs.jcsp.internal.processing.data.type.factory.ITypeVariableDescriptorFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.TypeVariable;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit-tests for {@link TypeVariableDescriptorGenerator}.
 */
@ExtendWith(MockitoExtension.class)
public class TypeVariableDescriptorGeneratorTest
{
    @Mock
    private ITypeVariableDescriptorFactory typeVariableDescriptorFactory;
    @Mock
    private ITypeBoundsDescriptorGenerator typeBoundsDescriptorGenerator;
    @Mock
    private ITypeVariableDescriptor descriptor;
    @InjectMocks
    private TypeVariableDescriptorGenerator generator;

    @Test
    @SuppressWarnings("DataFlowIssue" /* Intentional contract nullability violation for test */)
    public void testConstructorNullDescriptorFactory()
    {
        assertThatNullPointerException().isThrownBy(
            () -> new TypeVariableDescriptorGenerator(null, typeBoundsDescriptorGenerator));
    }

    @Test
    @SuppressWarnings("DataFlowIssue" /* Intentional contract nullability violation for test */)
    public void testConstructorNullBoundsDescriptorGenerator()
    {
        assertThatNullPointerException().isThrownBy(
            () -> new TypeVariableDescriptorGenerator(typeVariableDescriptorFactory, null));
    }

    @Test
    public void testGenerate()
    {
        TypeVariable<? extends Class<?>> typeVariable = TestClass.class.getTypeParameters()[0];
        when(typeBoundsDescriptorGenerator.generate(typeVariable)).thenReturn(Optional.empty());
        when(typeVariableDescriptorFactory.create("XXXX", null)).thenReturn(descriptor);

        assertThat(generator.generate(typeVariable)).isEqualTo(descriptor);

        ITypeBoundsDescriptor typeBoundsDescriptor = mock(ITypeBoundsDescriptor.class);
        when(typeBoundsDescriptorGenerator.generate(typeVariable)).thenReturn(Optional.of(typeBoundsDescriptor));
        when(typeVariableDescriptorFactory.create("XXXX", typeBoundsDescriptor)).thenReturn(descriptor);

        assertThat(generator.generate(typeVariable)).isEqualTo(descriptor);
    }

    @Test
    @SuppressWarnings("DataFlowIssue" /* Intentional contract nullability violation for test */)
    public void testGenerateNullTypeVariable()
    {
        assertThatNullPointerException().isThrownBy(() -> generator.generate(null));
    }

    @Test
    public void testGenerateTypeBoundsGeneratorThrowsIae()
    {
        Throwable cause = new IllegalArgumentException();
        TypeVariable<? extends Class<?>> typeVariable = TestClass.class.getTypeParameters()[0];
        when(typeBoundsDescriptorGenerator.generate(typeVariable)).thenThrow(cause);
        assertThatIllegalArgumentException().isThrownBy(() -> generator.generate(typeVariable))
            .withCause(cause)
            .withMessageContaining(typeVariable.getName());
    }

    @SuppressWarnings("unused" /* Params are need for tests of work with generic classes */)
    private static class TestClass<XXXX>
    {
    }
}
