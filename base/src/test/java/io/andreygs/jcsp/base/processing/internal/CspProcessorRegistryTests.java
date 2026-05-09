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

package io.andreygs.jcsp.base.processing.internal;

import io.andreygs.jcsp.base.processing.ICspDataDeserializationProcessor;
import io.andreygs.jcsp.base.processing.ICspDataGeneralSerializationProcessor;
import io.andreygs.jcsp.base.processing.ICspDataSerializationProcessor;
import io.andreygs.jcsp.base.processing.proxy.internal.ICspDataSerializationProxyProcessor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

/**
 * Unit-tests for {@link CspDataProcessorRegistry}.
 */
@ExtendWith(MockitoExtension.class)
public class CspProcessorRegistryTests
{
    @Mock
    private ICspDataSerializationProcessor<?> cspSerializationProcessor;
    @Mock
    private ICspDataDeserializationProcessor<?> cspDeserializationProcessor;
    @Mock
    private ICspDataSerializationProcessor<Instant> cspSerializationProcessorForInstant;

    @Test
    public void testRegisterProcessor()
    {
        ICspDataProcessorRegistry<ICspDataSerializationProcessor<?>, ICspDataSerializationProxyProcessor<?>>
            cspProcessorRegistry = new CspDataProcessorRegistry<>();

        Assertions.assertTrue(cspProcessorRegistry.findOrdinaryProcessor(List.class).isEmpty(),
                              "Processor registered for " + List.class.getName() + "!");

        cspProcessorRegistry.registerProcessor(List.class, cspSerializationProcessor);

        Assertions.assertTrue(cspProcessorRegistry.findOrdinaryProcessor(List.class).isPresent(),
                              "No processor registered for " + List.class.getName() + "!");
    }

    @Test
    @SuppressWarnings("DataFlowIssue")
    public void testRegisterProcessorNullClass()
    {
        ICspDataProcessorRegistry<ICspDataSerializationProcessor<?>, ICspDataSerializationProxyProcessor<?>>
            cspProcessorRegistry = new CspDataProcessorRegistry<>();

        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> cspProcessorRegistry.registerProcessor(null, cspSerializationProcessor));
    }

    @Test
    @SuppressWarnings("DataFlowIssue")
    public void testRegisterProcessorNullProcessor()
    {
        ICspDataProcessorRegistry<ICspDataSerializationProcessor<?>, ICspDataSerializationProxyProcessor<?>>
            cspProcessorRegistry = new CspDataProcessorRegistry<>();

        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> cspProcessorRegistry.registerProcessor(Instant.class, null));
    }

    @Test
    public void testRegisterProcessorReplace()
    {
        ICspDataProcessorRegistry<ICspDataSerializationProcessor<?>, ICspDataSerializationProxyProcessor<?>>
            cspProcessorRegistry = new CspDataProcessorRegistry<>();

        Class<?> clazz = Instant.class;
        cspProcessorRegistry.registerProcessor(clazz, (value, generalProcessor) -> {});
        cspProcessorRegistry.registerProcessor(clazz, cspSerializationProcessorForInstant);

        Assertions.assertEquals(cspSerializationProcessorForInstant, cspProcessorRegistry.findOrdinaryProcessor(clazz).orElse(null),
                                "Processor for " + clazz.getName() + " not replaced!");
    }

    @Test
    public void testUnregisterProcessor()
    {
        ICspDataProcessorRegistry<ICspDataDeserializationProcessor<?>, ICspDataSerializationProxyProcessor<?>>
            cspProcessorRegistry = new CspDataProcessorRegistry<>();
        cspProcessorRegistry.registerProcessor(Instant.class, cspDeserializationProcessor);
        cspProcessorRegistry.unregisterProcessor(Instant.class);

        Assertions.assertTrue(cspProcessorRegistry.findOrdinaryProcessor(Instant.class).isEmpty(),
                              "Processor for " + Instant.class.getName() + "was not unregistered!");
    }

    @Test
    @SuppressWarnings("DataFlowIssue")
    public void testUnregisterProcessorNullClass()
    {
        ICspDataProcessorRegistry<ICspDataSerializationProcessor<?>, ICspDataSerializationProxyProcessor<?>>
            cspProcessorRegistry = new CspDataProcessorRegistry<>();

        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> cspProcessorRegistry.unregisterProcessor(null));
    }

    @Test
    public void testfindProcessor()
    {
        testRegisterProcessor();
    }

    @Test
    @SuppressWarnings("DataFlowIssue")
    public void testfindProcessorNullClass()
    {
        ICspDataProcessorRegistry<ICspDataSerializationProcessor<?>, ICspDataSerializationProxyProcessor<?>>
            cspProcessorRegistry = new CspDataProcessorRegistry<>();

        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> cspProcessorRegistry.findOrdinaryProcessor(null));
    }
}
