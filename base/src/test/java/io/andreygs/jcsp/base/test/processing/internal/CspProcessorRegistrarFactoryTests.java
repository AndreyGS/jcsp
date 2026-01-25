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

package io.andreygs.jcsp.base.test.processing.internal;

import io.andreygs.jcsp.base.processing.ICspProcessor;
import io.andreygs.jcsp.base.processing.ICspProcessorRegistrar;
import io.andreygs.jcsp.base.processing.ICspProcessorRegistrarFactory;
import io.andreygs.jcsp.base.processing.internal.CspProcessorRegistrarFactory;
import io.andreygs.jcsp.base.test.processing.AbstractICspProcessorRegistrarFactory;
import io.andreygs.jcsp.base.test.processing.AbstractICspProcessorRegistrarTests;
import org.junit.jupiter.api.Nested;

/**
 *  Unit-tests for {@link CspProcessorRegistrarFactory}.
 */
public class CspProcessorRegistrarFactoryTests extends AbstractICspProcessorRegistrarFactory
{
    @Override
    protected ICspProcessorRegistrarFactory produceCspProcessorRegistrarFactory()
    {
        return new CspProcessorRegistrarFactory();
    }

    @Nested
    public class CreateProcessorRegistrarTests extends AbstractICspProcessorRegistrarTests
    {
        @Override
        protected <T extends ICspProcessor> ICspProcessorRegistrar<T> getCspProcessorRegistrar()
        {
            return produceCspProcessorRegistrarFactory().createProcessorRegistrar();
        }
    }
}
