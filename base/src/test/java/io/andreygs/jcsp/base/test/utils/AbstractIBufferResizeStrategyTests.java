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

package io.andreygs.jcsp.base.test.utils;

import io.andreygs.jcsp.base.utils.IBufferResizeStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Unit-tests for {@link IBufferResizeStrategy} contract.
 */
public abstract class AbstractIBufferResizeStrategyTests
{
    @Test
    public void calculateNewSizeTest()
    {
        int currentCapacity = 4;
        int minimumRequiredSize = 1000000000;

        IBufferResizeStrategy bufferResizeStrategy = createBufferResizeStrategy();
        int result = bufferResizeStrategy.calculateNewSize(currentCapacity, minimumRequiredSize);

        Assertions.assertTrue(minimumRequiredSize <= result, "Calculated buffer size is less than minimum required!");
    }

    @Test
    public void calculateNewSizeCurrentCapacityIsEqualMinimumRequiredSizeTest()
    {
        int currentCapacity = 4;
        int minimumRequiredSize = 4;

        IBufferResizeStrategy bufferResizeStrategy = createBufferResizeStrategy();
        int result = bufferResizeStrategy.calculateNewSize(currentCapacity, minimumRequiredSize);

        Assertions.assertEquals(currentCapacity, result,
                                "Calculated size is not equal to current capacity when currentCapacity == minimumRequiredSize!");
    }

    @Test
    public void calculateNewSizeMinimumRequiredSizeIsVeryBigTest()
    {
        int currentCapacity = 4;
        int minimumRequiredSize = Integer.MAX_VALUE - 2;

        IBufferResizeStrategy bufferResizeStrategy = createBufferResizeStrategy();
        int result = bufferResizeStrategy.calculateNewSize(currentCapacity, minimumRequiredSize);

        Assertions.assertTrue(minimumRequiredSize <= result, "Calculated buffer size is less than minimum required!");
    }

    @Test
    public void calculateNewSizeMinimumRequiredSizeIsMaxValueTest()
    {
        int currentCapacity = 4;
        int minimumRequiredSize = Integer.MAX_VALUE;

        IBufferResizeStrategy bufferResizeStrategy = createBufferResizeStrategy();
        int result = bufferResizeStrategy.calculateNewSize(currentCapacity, minimumRequiredSize);

        Assertions.assertEquals(Integer.MAX_VALUE, result, "Calculated buffer size is not equal to Integer.MAX_VALUE!");
    }


    @Test
    public void calculateNewSizeCapacityIsNegativeTest()
    {
        int currentCapacity = -1;
        int minimumRequiredSize = 1;

        IBufferResizeStrategy bufferResizeStrategy = createBufferResizeStrategy();
        Assertions.assertThrows(IllegalArgumentException.class,
                                () ->  bufferResizeStrategy.calculateNewSize(currentCapacity, minimumRequiredSize),
                                "Current capacity with negative number not triggered throwing of "
                                    + "IllegalArgumentException!");
    }

    @Test
    public void calculateNewSizeMinimumRequiredSizeIsLessThanCurrentCapacityTest()
    {
        int currentCapacity = 1;
        int minimumRequiredSize = 0;

        IBufferResizeStrategy bufferResizeStrategy = createBufferResizeStrategy();
        Assertions.assertThrows(IllegalArgumentException.class,
                                () ->  bufferResizeStrategy.calculateNewSize(currentCapacity, minimumRequiredSize),
                                "Minimum required size lesser than current capacity not triggered throwing of "
                                    + "IllegalArgumentException!");
    }

    protected abstract IBufferResizeStrategy createBufferResizeStrategy();
}
