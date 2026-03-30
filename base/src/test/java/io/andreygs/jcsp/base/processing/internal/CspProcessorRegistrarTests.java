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
import io.andreygs.jcsp.base.processing.ICspDataProcessorRegistrar;
import io.andreygs.jcsp.base.processing.ICspDataSerializationProcessor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

/**
 * Unit-tests for {@link CspDataProcessorRegistrar}.
 */
@ExtendWith(MockitoExtension.class)
public class CspProcessorRegistrarTests
{
    @Mock
    private ICspDataSerializationProcessor cspSerializationProcessor;
    @Mock
    private ICspDataDeserializationProcessor cspDeserializationProcessor;

    @Test
    public void registerProcessorTest()
    {
        ICspDataProcessorRegistrar<ICspDataSerializationProcessor>
            cspProcessorRegistrar = new CspDataProcessorRegistrar<>();

        Assertions.assertTrue(cspProcessorRegistrar.findProcessor(List.class).isEmpty(),
                              "Processor registered for " + List.class.getName() + "!");

        cspProcessorRegistrar.registerProcessor(List.class, cspSerializationProcessor);

        Assertions.assertTrue(cspProcessorRegistrar.findProcessor(List.class).isPresent(),
                              "No processor registered for " + List.class.getName() + "!");
    }

    @Test
    @SuppressWarnings("DataFlowIssue")
    public void registerProcessorNullClassTest()
    {
        ICspDataProcessorRegistrar<ICspDataSerializationProcessor>
            cspProcessorRegistrar = new CspDataProcessorRegistrar<>();

        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> cspProcessorRegistrar.registerProcessor(null, cspSerializationProcessor));
    }

    @Test
    @SuppressWarnings("DataFlowIssue")
    public void registerProcessorNullProcessorTest()
    {
        ICspDataProcessorRegistrar<ICspDataSerializationProcessor>
            cspProcessorRegistrar = new CspDataProcessorRegistrar<>();

        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> cspProcessorRegistrar.registerProcessor(List.class, null));
    }

    @Test
    public void registerProcessorReplaceTest()
    {
        ICspDataProcessorRegistrar<ICspDataSerializationProcessor>
            cspProcessorRegistrar = new CspDataProcessorRegistrar<>();
        cspProcessorRegistrar.registerProcessor(List.class, cspSerializationProcessor);

        ICspDataSerializationProcessor cspSerializationProcessorNew = Mockito.mock(
            ICspDataSerializationProcessor.class);
        cspProcessorRegistrar.registerProcessor(List.class, cspSerializationProcessorNew);

        Assertions.assertEquals(cspSerializationProcessorNew, cspProcessorRegistrar.findProcessor(List.class).orElse(null),
                                "Processor for " + List.class.getName() + " not replaced!");
    }

    @Test
    public void unregisterProcessorTest()
    {
        ICspDataProcessorRegistrar<ICspDataDeserializationProcessor>
            cspProcessorRegistrar = new CspDataProcessorRegistrar<>();
        cspProcessorRegistrar.registerProcessor(List.class, cspDeserializationProcessor);
        cspProcessorRegistrar.unregisterProcessor(List.class);

        Assertions.assertTrue(cspProcessorRegistrar.findProcessor(List.class).isEmpty(),
                              "Processor for " + List.class.getName() + "was not unregistered!");
    }

    @Test
    @SuppressWarnings("DataFlowIssue")
    public void unregisterProcessorNullClassTest()
    {
        ICspDataProcessorRegistrar<ICspDataSerializationProcessor>
            cspProcessorRegistrar = new CspDataProcessorRegistrar<>();

        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> cspProcessorRegistrar.unregisterProcessor(null));
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
        ICspDataProcessorRegistrar<ICspDataSerializationProcessor>
            cspProcessorRegistrar = new CspDataProcessorRegistrar<>();

        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> cspProcessorRegistrar.findProcessor(null));
    }
}
