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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.AnnotatedType;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit-tests for {@link CspTypeProcessorProvider}.
 */
@ExtendWith(MockitoExtension.class)
public class CspTypeProcessorProviderTests
{
    @Mock
    private ICspTypeProcessorRegistry<ICspTypeSerializationProcessor> registry;
    @Mock
    private ICspTypeProcessorGenerator<ICspTypeSerializationProcessor> generator;
    @Mock
    private ICspTypeSerializationProcessor typeProcessor;
    @InjectMocks
    private CspTypeProcessorProvider<ICspTypeSerializationProcessor> provider;
    private final AnnotatedType annotatedType = new CspTypeToken<TestClass<String>>(){}.getAnnotatedType();

    @Test
    @SuppressWarnings("DataFlowIssue" /* Intentional contract nullability violation for test */)
    public void testConstructorNullRegistry()
    {
        assertThatThrownBy(() -> new CspTypeProcessorProvider<>(null, generator))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    @SuppressWarnings("DataFlowIssue" /* Intentional contract nullability violation for test */)
    public void testConstructorNullGenerator()
    {
        assertThatThrownBy(() -> new CspTypeProcessorProvider<>(registry, null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    public void testProvideIfAlreadyRegistered()
    {
        when(registry.find(annotatedType)).thenReturn(Optional.of(typeProcessor));
        assertThat(provider.provide(annotatedType)).isEqualTo(typeProcessor);
    }

    @Test
    public void testProvideIfNotAlreadyRegistered()
    {
        when(registry.find(annotatedType)).thenReturn(Optional.empty());
        when(generator.generate(annotatedType)).thenReturn(typeProcessor);

        assertThat(provider.provide(annotatedType)).isEqualTo(typeProcessor);
        verify(registry).register(annotatedType, typeProcessor);
    }

    @Test
    public void testProvideGenerateThrows()
    {
        when(registry.find(annotatedType)).thenReturn(Optional.empty());
        when(generator.generate(annotatedType)).thenThrow(new IllegalArgumentException());
        assertThatThrownBy(() -> provider.provide(annotatedType)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @SuppressWarnings("DataFlowIssue" /* Intentional contract nullability violation for test */)
    public void testProvideNullAnnotatedType()
    {
        assertThatThrownBy(() -> provider.provide(null)).isInstanceOf(NullPointerException.class);
    }

    @SuppressWarnings("unused" /* Parameters are need for tests of work with generic classes */)
    private static class TestClass<T>
    {
    }
}
