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

package io.andreygs.jcsp.base.processing.composite.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * TODO: place description here
 */
abstract class AbstractCspDataCompositeSubProcessorHolder<P> extends AbstractCspDataCompositeObjectProcessor<P>
    implements ICspDataCompositeSubProcessorHolder<P>
{
    private final List<P> subProcessors;
    private final int subProcessorNumber;

    AbstractCspDataCompositeSubProcessorHolder(boolean reference, int subProcessorNumber)
    {
        super(reference);
        this.subProcessors = new ArrayList<>(subProcessorNumber);
        this.subProcessorNumber = subProcessorNumber;
    }

    protected AbstractCspDataCompositeSubProcessorHolder(AbstractCspDataCompositeSubProcessorHolder<P> processor,
        boolean reference)
    {
        super(reference);
        this.subProcessors = processor.subProcessors;
        this.subProcessorNumber = processor.subProcessorNumber;
    }

    @Override
    public final boolean addSubProcessor(P subProcessor)
    {
        if (subProcessorNumber == subProcessors.size())
        {
            throw new IllegalStateException("All generic type parameters have been added earlier!");
        }
        subProcessors.add(subProcessor);
        return subProcessorNumber == subProcessors.size();
    }

    protected final List<P> getSubProcessors()
    {
        return Collections.unmodifiableList(subProcessors);
    }

    protected final P getSubProcessor(int processorIndex)
    {
        return subProcessors.get(processorIndex);
    }

    protected final int getSubProcessorNumber()
    {
        return subProcessorNumber;
    }
}
