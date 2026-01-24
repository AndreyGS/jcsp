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
import io.andreygs.jcsp.base.processing.ICspProcessorProvider;
import io.andreygs.jcsp.base.processing.ICspProcessor;
import io.andreygs.jcsp.base.processing.ICspProcessorRegistrar;
import io.andreygs.jcsp.base.processing.ICspSerializationProcessor;
import io.andreygs.jcsp.base.types.CspRuntimeException;
import io.andreygs.jcsp.base.types.CspStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

/**
 * Unit-tests for {@link ICspProcessorProvider} contract.
 */
@ExtendWith(MockitoExtension.class)
public abstract class AbstractICspProcessorProviderTests
{
    @Mock
    private ICspProcessorRegistrar<ICspSerializationProcessor> cspProcessorRegistrarMock;

    @Test
    public void provideProcessorTest()
    {
        ICspSerializationProcessor cspSerializationProcessor = Mockito.mock(ICspSerializationProcessor.class);
        Class<?> processorClass = List.class;

        Mockito.when(cspProcessorRegistrarMock.findProcessor(processorClass)).thenReturn(Optional.of(cspSerializationProcessor));
        ICspProcessorProvider<ICspSerializationProcessor> cspProcessorProvider =
            createICspProcessorProvider(cspProcessorRegistrarMock);

        ICspSerializationProcessor cspProcessor = cspProcessorProvider.provideProcessor(processorClass);

        Assertions.assertNotNull(cspProcessor);
        Mockito.verify(cspProcessorRegistrarMock).findProcessor(processorClass);
    }

    @Test
    public void provideProcessorWhenAbsentTest()
    {
        Class<?> processorClass = List.class;

        Mockito.when(cspProcessorRegistrarMock.findProcessor(processorClass)).thenReturn(Optional.empty());
        ICspProcessorProvider<ICspSerializationProcessor> cspProcessorProvider =
            createICspProcessorProvider(cspProcessorRegistrarMock);

        CspRuntimeException cspRuntimeException = Assertions.assertThrows(CspRuntimeException.class,
                                () -> cspProcessorProvider.provideProcessor(processorClass),
                                "There was no CspRuntimeException on absent processor!");

        Assertions.assertEquals(CspStatus.NO_SUCH_HANDLER, cspRuntimeException.getCspStatus(),
                                "Invalid status on absent processor!");
    }

    @Test
    public void getCspProcessorRegistrarTest()
    {
        ICspProcessorRegistrar<ICspDeserializationProcessor> expected = Mockito.mock();
        ICspProcessorProvider<ICspDeserializationProcessor> cspProcessorProvider =
            createICspProcessorProvider(expected);

        ICspProcessorRegistrar<ICspDeserializationProcessor> cspProcessorRegistrar =
            cspProcessorProvider.getCspProcessorRegistrar();
        Assertions.assertEquals(expected, cspProcessorRegistrar,
                                "ICspProcessorRegistrar is not equal to the one provided!");
    }

    protected abstract <T extends ICspProcessor> ICspProcessorProvider<T> createICspProcessorProvider(
        ICspProcessorRegistrar<T> cspProcessorRegistrar);
}
