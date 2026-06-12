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

package io.andreygs.jcsp.api.protocol.utils;

import io.andreygs.jcsp.api.protocol.CspCommonFlag;
import io.andreygs.jcsp.api.protocol.CspDataFlag;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit-tests for {@link CspFlagUtils}.
 */
public class CspFlagUtilsTests
{
    @Test
    public void testCalculateFlagMaskIterable()
    {
        int expected = CspDataFlag.ALLOW_UNMANAGED_POINTERS.getValue()
                           | CspDataFlag.ALIGNMENT_MAY_BE_NOT_EQUAL.getValue();
        int result = CspFlagUtils.calculateFlagMask(
            Set.of(CspDataFlag.ALLOW_UNMANAGED_POINTERS, CspDataFlag.ALIGNMENT_MAY_BE_NOT_EQUAL));

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testCalculateFlagMaskArray()
    {
        int expected = CspDataFlag.ALLOW_UNMANAGED_POINTERS.getValue()
                           | CspDataFlag.ALIGNMENT_MAY_BE_NOT_EQUAL.getValue();
        CspDataFlag[] cspDataFlags = {CspDataFlag.ALLOW_UNMANAGED_POINTERS, CspDataFlag.ALIGNMENT_MAY_BE_NOT_EQUAL};
        int result = CspFlagUtils.calculateFlagMask(cspDataFlags);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testIsFlagSet()
    {
        boolean expected = true;
        int flagMask = CspDataFlag.ALLOW_UNMANAGED_POINTERS.getValue()
                           | CspDataFlag.ALIGNMENT_MAY_BE_NOT_EQUAL.getValue();
        boolean result = CspFlagUtils.isFlagSet(flagMask, CspDataFlag.ALLOW_UNMANAGED_POINTERS);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testEvaluateFlagsStringDescriptionDefaultWithFlag()
    {
        Set<CspCommonFlag> flags = Set.of(CspCommonFlag.BITNESS_32);
        StringBuilder descriptionBuilder = CspFlagUtils.evaluateFlagsStringDescription(CspCommonFlag.class, flags);

        assertThat(descriptionBuilder.toString().contains(CspCommonFlag.BITNESS_32.getNameWhenSet())).isTrue();
        assertThat(descriptionBuilder.toString().contains(CspCommonFlag.BITNESS_32.getFlagTypeName())).isTrue();
    }

    @Test
    public void testEvaluateFlagsStringDescriptionDefaultWithoutFlag()
    {
        Set<CspCommonFlag> flags = Set.of();
        StringBuilder descriptionBuilder = CspFlagUtils.evaluateFlagsStringDescription(CspCommonFlag.class, flags);

        assertThat(descriptionBuilder.toString().contains(CspCommonFlag.BITNESS_32.getNameWhenSet())).isFalse();
        assertThat(descriptionBuilder.toString().contains(CspCommonFlag.BITNESS_32.getFlagTypeName())).isTrue();
    }

    @Test
    public void testEvaluateFlagsStringDescriptionDefaultWithAllFlags()
    {
        Set<CspCommonFlag> flags = Set.of(CspCommonFlag.BITNESS_32);
        StringBuilder descriptionBuilder = CspFlagUtils.evaluateFlagsStringDescription(CspCommonFlag.class, flags, false, true);

        assertThat(descriptionBuilder.toString().contains(CspCommonFlag.BITNESS_32.getNameWhenSet())).isTrue();
        assertThat(descriptionBuilder.toString().contains(CspCommonFlag.BIG_ENDIAN.getNameWhenUnset())).isTrue();
        assertThat(descriptionBuilder.toString().contains(CspCommonFlag.ENDIANNESS_DIFFERENCE.getNameWhenUnset())).isTrue();
        assertThat(descriptionBuilder.toString().contains(CspCommonFlag.BITNESS_32.getFlagTypeName())).isTrue();
    }

    @Test
    public void testEvaluateFlagsStringDescriptionWitNoFlagsAndNoHeader()
    {
        Set<CspCommonFlag> flags = Set.of();
        StringBuilder descriptionBuilder = CspFlagUtils.evaluateFlagsStringDescription(CspCommonFlag.class, flags, true, false);

        assertThat(descriptionBuilder.isEmpty()).isTrue();
    }
}