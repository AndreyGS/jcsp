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

package io.andreygs.jcsp.base.test.processing;

import io.andreygs.jcsp.base.processing.ICspDeserializationProcessor;
import io.andreygs.jcsp.base.processing.ICspProcessor;
import io.andreygs.jcsp.base.processing.ICspProcessorRegistrar;
import io.andreygs.jcsp.base.processing.ICspSerializationProcessor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

/**
 * Unit-tests for {@link ICspProcessorRegistrar} contract.
 */
@ExtendWith(MockitoExtension.class)
public abstract class AbstractICspProcessorRegistrarTests
{
    @Mock
    private ICspSerializationProcessor cspSerializationProcessor;
    @Mock
    private ICspDeserializationProcessor cspDeserializationProcessor;

    @Test
    public void registerProcessorTest()
    {
        ICspProcessorRegistrar<ICspSerializationProcessor> cspProcessorRegistrar = getCspProcessorRegistrar();
        Assertions.assertFalse(cspProcessorRegistrar.findProcessor(List.class).isPresent(),
                              "Processor registered for " + List.class.getName() + "!");

        cspProcessorRegistrar.registerProcessor(List.class, cspSerializationProcessor);

        Assertions.assertTrue(cspProcessorRegistrar.findProcessor(List.class).isPresent(),
                              "No processor registered for " + List.class.getName() + "!");
    }

    @Test
    public void registerProcessorReplaceTest()
    {
        ICspProcessorRegistrar<ICspSerializationProcessor> cspProcessorRegistrar = getCspProcessorRegistrar();
        cspProcessorRegistrar.registerProcessor(List.class, cspSerializationProcessor);

        ICspSerializationProcessor cspSerializationProcessorNew = Mockito.mock(
            ICspSerializationProcessor.class);
        cspProcessorRegistrar.registerProcessor(List.class, cspSerializationProcessorNew);

        Assertions.assertEquals(cspSerializationProcessorNew, cspProcessorRegistrar.findProcessor(List.class).orElse(null),
                                "Processor for " + List.class.getName() + " not replaced!");
    }

    @Test
    public void unregisterProcessorTest()
    {
        ICspProcessorRegistrar<ICspDeserializationProcessor> cspProcessorRegistrar = getCspProcessorRegistrar();
        cspProcessorRegistrar.registerProcessor(List.class, cspDeserializationProcessor);
        cspProcessorRegistrar.unregisterProcessor(List.class);

        Assertions.assertTrue(cspProcessorRegistrar.findProcessor(List.class).isEmpty(),
                              "Processor for " + List.class.getName() + "was not unregistered!");
    }

    @Test
    public void findProcessorTest()
    {
        registerProcessorTest();
        unregisterProcessorTest();
    }

    protected abstract <T extends ICspProcessor> ICspProcessorRegistrar<T> getCspProcessorRegistrar();
}
