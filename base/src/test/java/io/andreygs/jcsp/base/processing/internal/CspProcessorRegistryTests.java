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
    @Mock
    private ICspDataGeneralSerializationProcessor cspDataGeneralSerializationProcessor;

    @Test
    public void registerProcessorTest()
    {
        ICspDataProcessorRegistry<ICspDataSerializationProcessor<?>>
            cspProcessorRegistry = new CspDataProcessorRegistry<>();

        Assertions.assertTrue(cspProcessorRegistry.findOrdinaryProcessor(List.class).isEmpty(),
                              "Processor registered for " + List.class.getName() + "!");

        cspProcessorRegistry.registerProcessor(List.class, cspSerializationProcessor);

        Assertions.assertTrue(cspProcessorRegistry.findOrdinaryProcessor(List.class).isPresent(),
                              "No processor registered for " + List.class.getName() + "!");
    }

    @Test
    @SuppressWarnings("DataFlowIssue")
    public void registerProcessorNullClassTest()
    {
        ICspDataProcessorRegistry<ICspDataSerializationProcessor<?>>
            cspProcessorRegistry = new CspDataProcessorRegistry<>();

        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> cspProcessorRegistry.registerProcessor(null, cspSerializationProcessor));
    }

    @Test
    @SuppressWarnings("DataFlowIssue")
    public void registerProcessorNullProcessorTest()
    {
        ICspDataProcessorRegistry<ICspDataSerializationProcessor<?>>
            cspProcessorRegistry = new CspDataProcessorRegistry<>();

        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> cspProcessorRegistry.registerProcessor(Instant.class, null));
    }

    @Test
    public void registerProcessorReplaceTest()
    {
        ICspDataProcessorRegistry<ICspDataSerializationProcessor<?>>
            cspProcessorRegistry = new CspDataProcessorRegistry<>();

        ICspDataSerializationProcessor<Instant> xxy = (value, generalSerializationProcessor) -> {
        int i =1;
    };
        Instant instant = Instant.now();
        Class<?> clazz = Instant.class;
        cspProcessorRegistry.registerProcessor(Instant.class, xxy);
        ICspDataSerializationProcessor<Instant> xxz =
            (ICspDataSerializationProcessor<Instant>)cspProcessorRegistry.findOrdinaryProcessor(Instant.class).get();
        xxz.serialize(instant, cspDataGeneralSerializationProcessor);
/*
        new XXX<String>().registerProcessorTestZ();
        new AbstractCspTypeDescriptorHolder<List<String>>(){};*/

        cspProcessorRegistry.registerProcessor(Instant.class, cspSerializationProcessorForInstant);

        Assertions.assertEquals(cspSerializationProcessorForInstant, cspProcessorRegistry.findOrdinaryProcessor(Instant.class).orElse(null),
                                "Processor for " + Instant.class.getName() + " not replaced!");
    }

    @Test
    public void unregisterProcessorTest()
    {
        ICspDataProcessorRegistry<ICspDataDeserializationProcessor<?>>
            cspProcessorRegistry = new CspDataProcessorRegistry<>();
        cspProcessorRegistry.registerProcessor(Instant.class, cspDeserializationProcessor);
        cspProcessorRegistry.unregisterProcessor(Instant.class);

        Assertions.assertTrue(cspProcessorRegistry.findOrdinaryProcessor(Instant.class).isEmpty(),
                              "Processor for " + Instant.class.getName() + "was not unregistered!");
    }

    @Test
    @SuppressWarnings("DataFlowIssue")
    public void unregisterProcessorNullClassTest()
    {
        ICspDataProcessorRegistry<ICspDataSerializationProcessor<?>>
            cspProcessorRegistry = new CspDataProcessorRegistry<>();

        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> cspProcessorRegistry.unregisterProcessor(null));
    }

    @Test
    public void findProcessorTest()
    {
        registerProcessorTest();
    }

    @Test
    @SuppressWarnings("DataFlowIssue")
    public void findProcessorNullClassTest()
    {
        ICspDataProcessorRegistry<ICspDataSerializationProcessor<?>>
            cspProcessorRegistry = new CspDataProcessorRegistry<>();

        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> cspProcessorRegistry.findOrdinaryProcessor(null));
    }
/*
    private static class XXX<T>
    {
        void registerProcessorTestZ()
        {
            TypeVariable<Class<XXX>>[] typeParameters = XXX.class.getTypeParameters();
            Map<String, AbstractCspTypeDescriptorHolder.CspTypeDescriptor> map = new HashMap<>();
            var test = new AbstractCspTypeDescriptorHolder<Map<String, @CspReference String>>(){};
            map.put(typeParameters[0].getName(), test.getCspTypeDescriptor());
            var holder = new AbstractCspTypeDescriptorHolder<@CspReference Map<@CspReference T, Map<int[],
                                                                            @CspReference Integer @CspFixedSizeArray [] @CspFixedSizeArray []>>>(map){};

            int i = 1;
        }
    }

    private static class ProcessorTestZ<T> extends AbstractCspTypeDescriptorHolder<T>
    {
        ProcessorTestZ(@Nullable Map<String, CspTypeDescriptor> genericParameterCspTypeDescriptors)
        {
            super(genericParameterCspTypeDescriptors);
        }
    }*/
}
