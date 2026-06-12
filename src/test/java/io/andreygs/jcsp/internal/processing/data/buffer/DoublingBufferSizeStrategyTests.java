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

package io.andreygs.jcsp.internal.processing.data.buffer;

import io.andreygs.jcsp.api.processing.buffer.IBufferResizeStrategy;
import io.andreygs.jcsp.internal.processing.buffer.DoublingBufferSizeStrategy;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit-tests for {@link DoublingBufferSizeStrategy}.
 */
public class DoublingBufferSizeStrategyTests
{
    private final IBufferResizeStrategy strategy = new DoublingBufferSizeStrategy();

    @Test
    public void testCalculateNewSize()
    {
        int currentCapacity = 4;
        int minimumRequiredSize = 1000000000;
        int result = strategy.calculateNewSize(currentCapacity, minimumRequiredSize);
        assertThat(result).isGreaterThanOrEqualTo(minimumRequiredSize);
    }

    @Test
    public void testCalculateNewSizeCurrentCapacityIsEqualMinimumRequiredSize()
    {
        int currentCapacity = 4;
        int minimumRequiredSize = 4;
        int result = strategy.calculateNewSize(currentCapacity, minimumRequiredSize);
        assertThat(result).isEqualTo(minimumRequiredSize);
    }

    @Test
    public void testCalculateNewSizeMinimumRequiredSizeIsVeryBig()
    {
        int currentCapacity = 4;
        int minimumRequiredSize = Integer.MAX_VALUE - 2;
        int result = strategy.calculateNewSize(currentCapacity, minimumRequiredSize);
        assertThat(result).isGreaterThanOrEqualTo(minimumRequiredSize);
    }

    @Test
    public void testCalculateNewSizeMinimumRequiredSizeIsMaxValue()
    {
        int currentCapacity = 4;
        int minimumRequiredSize = Integer.MAX_VALUE;
        int result = strategy.calculateNewSize(currentCapacity, minimumRequiredSize);
        assertThat(result).isEqualTo(minimumRequiredSize);
    }

    @Test
    public void testCalculateNewSizeCapacityIsNegative()
    {
        int currentCapacity = -1;
        int minimumRequiredSize = 1;
        assertThatThrownBy(() ->  strategy.calculateNewSize(currentCapacity, minimumRequiredSize))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testCalculateNewSizeMinimumRequiredSizeIsLessThanCurrentCapacity()
    {
        int currentCapacity = 1;
        int minimumRequiredSize = 0;
        assertThatThrownBy(() ->  strategy.calculateNewSize(currentCapacity, minimumRequiredSize))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testCalculateNewSizeWhenCurrentIsZero()
    {
        int currentCapacity = 0;
        int minimumRequiredSize = 7;
        int result = strategy.calculateNewSize(currentCapacity, minimumRequiredSize);
        assertThat(result).isEqualTo(minimumRequiredSize);
    }
}
