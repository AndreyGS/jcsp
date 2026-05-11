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

package io.andreygs.jcsp.internal.processing.data;

import io.andreygs.jcsp.api.processing.data.ICspClassDeserializationProcessor;
import io.andreygs.jcsp.api.processing.data.ICspClassSerializationProcessor;
import io.andreygs.jcsp.internal.processing.data.type.ICspTypeSerializationProcessor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit-tests for {@link CspProcessorRegistry}.
 */
@ExtendWith(MockitoExtension.class)
public class CspProcessorRegistryTests
{
    @Mock
    private ICspClassSerializationProcessor<?> cspSerializationProcessor;
    @Mock
    private ICspClassDeserializationProcessor<?> cspDeserializationProcessor;
    @Mock
    private ICspClassSerializationProcessor<Instant> cspSerializationProcessorForInstant;

    @Test
    public void testRegisterClassProcessor()
    {
        ICspProcessorRegistry<ICspClassSerializationProcessor<?>, ICspTypeSerializationProcessor>
            cspProcessorRegistry = new CspProcessorRegistry<>();

        assertThat(cspProcessorRegistry.findOrdinaryProcessor(List.class)).isEmpty();

        cspProcessorRegistry.registerClassProcessor(List.class, cspSerializationProcessor);

        assertThat(cspProcessorRegistry.findGenericProcessor(List.class)).isPresent();
    }

    @Test
    @SuppressWarnings("DataFlowIssue")
    public void testRegisterClassProcessorNullClass()
    {
        ICspProcessorRegistry<ICspClassSerializationProcessor<?>, ICspTypeSerializationProcessor>
            cspProcessorRegistry = new CspProcessorRegistry<>();

        assertThatThrownBy(() -> cspProcessorRegistry.registerClassProcessor(null, cspSerializationProcessor))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    @SuppressWarnings("DataFlowIssue")
    public void testRegisterProcessorNullClassProcessor()
    {
        ICspProcessorRegistry<ICspClassSerializationProcessor<?>, ICspTypeSerializationProcessor>
            cspProcessorRegistry = new CspProcessorRegistry<>();

        assertThatThrownBy(() -> cspProcessorRegistry.registerClassProcessor(Instant.class, null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    public void testRegisterClassProcessorReplace()
    {
        ICspProcessorRegistry<ICspClassSerializationProcessor<?>, ICspTypeSerializationProcessor>
            cspProcessorRegistry = new CspProcessorRegistry<>();

        Class<?> clazz = Instant.class;
        cspProcessorRegistry.registerClassProcessor(clazz, (value, processor) -> {});
        cspProcessorRegistry.registerClassProcessor(clazz, cspSerializationProcessorForInstant);

        assertThat(cspProcessorRegistry.findOrdinaryProcessor(clazz)).containsSame(cspSerializationProcessorForInstant);
    }

    @Test
    public void testUnregisterClassProcessor()
    {
        ICspProcessorRegistry<ICspClassDeserializationProcessor<?>, ICspTypeSerializationProcessor>
            cspProcessorRegistry = new CspProcessorRegistry<>();
        cspProcessorRegistry.registerClassProcessor(Instant.class, cspDeserializationProcessor);
        cspProcessorRegistry.unregisterClassProcessor(Instant.class);

        assertThat(cspProcessorRegistry.findOrdinaryProcessor(Instant.class)).isEmpty();
    }

    @Test
    @SuppressWarnings("DataFlowIssue")
    public void testUnregisterClassProcessorNullClass()
    {
        ICspProcessorRegistry<ICspClassSerializationProcessor<?>, ICspTypeSerializationProcessor>
            cspProcessorRegistry = new CspProcessorRegistry<>();

        assertThatThrownBy(() -> cspProcessorRegistry.unregisterClassProcessor(null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    public void testFindProcessor()
    {
        testRegisterClassProcessor();
    }

    @Test
    @SuppressWarnings("DataFlowIssue")
    public void testFindProcessorNullClass()
    {
        ICspProcessorRegistry<ICspClassSerializationProcessor<?>, ICspTypeSerializationProcessor>
            cspProcessorRegistry = new CspProcessorRegistry<>();

        assertThatThrownBy(() -> cspProcessorRegistry.findOrdinaryProcessor(null))
            .isInstanceOf(NullPointerException.class);
    }
}
