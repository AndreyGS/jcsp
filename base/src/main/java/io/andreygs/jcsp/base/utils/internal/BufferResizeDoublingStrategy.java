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

package io.andreygs.jcsp.base.utils.internal;

import io.andreygs.jcsp.base.utils.IBufferResizeStrategy;

/**
 * Strategy that calculates new size of random buffer by at least doubling its current size.
 */
class BufferResizeDoublingStrategy
    implements IBufferResizeStrategy
{
    @Override
    public int calculateNewSize(int currentCapacity, int minimumRequiredSize)
    {
        if (currentCapacity < 0 || currentCapacity > minimumRequiredSize)
        {
            throw new IllegalArgumentException("Current capacity shall not be negative or bigger than minimum "
                                                   + "required size!");
        }

        int result = currentCapacity;
        while (result  < minimumRequiredSize)
        {
            try
            {
                result = Math.multiplyExact(result, 2);
            }
            catch (ArithmeticException e)
            {
                result = Integer.MAX_VALUE;
            }
        }

        return result;
    }
}
