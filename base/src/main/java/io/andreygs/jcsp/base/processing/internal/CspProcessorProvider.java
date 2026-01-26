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

import io.andreygs.jcsp.base.processing.ICspProcessor;
import io.andreygs.jcsp.base.processing.ICspProcessorProvider;
import io.andreygs.jcsp.base.processing.ICspProcessorRegistrar;
import io.andreygs.jcsp.base.types.CspRuntimeException;
import io.andreygs.jcsp.base.types.CspStatus;

import java.text.MessageFormat;
import java.util.Optional;

/**
 * Internal implementation of {@link ICspProcessorProvider}.
 */
final class CspProcessorProvider<T extends ICspProcessor>
    implements ICspProcessorProvider<T>
{
    private final ICspProcessorRegistrar<T> cspProcessorRegistrar;

    public CspProcessorProvider(ICspProcessorRegistrar<T> cspProcessorRegistrar)
    {
        this.cspProcessorRegistrar = cspProcessorRegistrar;
    }

    @Override
    public T provideProcessor(Class<?> clazz)
    {
        Optional<T> processor = cspProcessorRegistrar.findProcessor(clazz);
        if (processor.isEmpty())
        {
            throw CspRuntimeException.createCspRuntimeException(CspStatus.NO_SUCH_HANDLER,
                                                                MessageFormat.format(
                                                                    Messages.CspStatus_No_Such_Handler_ext_No_specialized_processor_for__0__,
                                                                    clazz));
        }

        return processor.get();
    }

    @Override
    public ICspProcessorRegistrar<T> getCspProcessorRegistrar()
    {
        return cspProcessorRegistrar;
    }
}
