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

package io.andreygs.jcsp.base.test.types;

import io.andreygs.jcsp.base.types.CspCommonFlags;
import io.andreygs.jcsp.base.types.CspDataFlags;
import io.andreygs.jcsp.base.types.CspFlagUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Unit-tests for {@link CspFlagUtils}.
 */
public class CspFlagUtilsTests
{
    @Test
    public void calculateFlagMaskStreamTest()
    {
        int expected = CspDataFlags.ALLOW_UNMANAGED_POINTERS.getValue()
                           | CspDataFlags.ALIGNMENT_MAY_BE_NOT_EQUAL.getValue();
        int result = CspFlagUtils.calculateFlagMask(
            Stream.of(CspDataFlags.ALLOW_UNMANAGED_POINTERS, CspDataFlags.ALIGNMENT_MAY_BE_NOT_EQUAL));

        Assertions.assertEquals(expected, result, "Masks do not match!");
    }

    @Test
    public void calculateFlagMaskArrayTest()
    {
        int expected = CspDataFlags.ALLOW_UNMANAGED_POINTERS.getValue()
                           | CspDataFlags.ALIGNMENT_MAY_BE_NOT_EQUAL.getValue();
        CspDataFlags[] cspDataFlags = {CspDataFlags.ALLOW_UNMANAGED_POINTERS, CspDataFlags.ALIGNMENT_MAY_BE_NOT_EQUAL};
        int result = CspFlagUtils.calculateFlagMask(cspDataFlags);

        Assertions.assertEquals(expected, result, "Masks do not match!");
    }

    @Test
    public void calculateFlagMaskListTest()
    {
        int expected = CspDataFlags.ALLOW_UNMANAGED_POINTERS.getValue()
                           | CspDataFlags.ALIGNMENT_MAY_BE_NOT_EQUAL.getValue();
        int result = CspFlagUtils.calculateFlagMask(
            Set.of(CspDataFlags.ALLOW_UNMANAGED_POINTERS, CspDataFlags.ALIGNMENT_MAY_BE_NOT_EQUAL));

        Assertions.assertEquals(expected, result, "Masks do not match!");
    }

    @Test
    public void isFlagSetTest()
    {
        boolean expected = true;
        int flagMask = CspDataFlags.ALLOW_UNMANAGED_POINTERS.getValue()
                           | CspDataFlags.ALIGNMENT_MAY_BE_NOT_EQUAL.getValue();
        boolean result = CspFlagUtils.isFlagSet(flagMask, CspDataFlags.ALLOW_UNMANAGED_POINTERS);

        Assertions.assertEquals(expected, result, "Flag not set!");
    }

    @Test
    public void evaluateFlagsStringDescriptionDefaultWithFlagTest()
    {
        Set<CspCommonFlags> flags = Set.of(CspCommonFlags.BITNESS_32);
        StringBuilder descriptionBuilder = CspFlagUtils.evaluateFlagsStringDescription(CspCommonFlags.class, flags);

        Assertions.assertTrue(descriptionBuilder.toString().contains(CspCommonFlags.BITNESS_32.getNameWhenSet()), "No flag info in description!");
        Assertions.assertTrue(descriptionBuilder.toString().contains(CspCommonFlags.BITNESS_32.getFlagTypeName()), "No flag type info in description!");
    }

    @Test
    public void evaluateFlagsStringDescriptionDefaultWithoutFlagTest()
    {
        Set<CspCommonFlags> flags = Set.of();
        StringBuilder descriptionBuilder = CspFlagUtils.evaluateFlagsStringDescription(CspCommonFlags.class, flags);

        Assertions.assertFalse(descriptionBuilder.toString().contains(CspCommonFlags.BITNESS_32.getNameWhenSet()), "flag info in description!");
        Assertions.assertTrue(descriptionBuilder.toString().contains(CspCommonFlags.BITNESS_32.getFlagTypeName()), "No flag type info in description!");
    }

    @Test
    public void evaluateFlagsStringDescriptionDefaultWithAllFlagsTest()
    {
        Set<CspCommonFlags> flags = Set.of(CspCommonFlags.BITNESS_32);
        StringBuilder descriptionBuilder = CspFlagUtils.evaluateFlagsStringDescription(CspCommonFlags.class, flags, false, true);

        Assertions.assertTrue(descriptionBuilder.toString().contains(CspCommonFlags.BITNESS_32.getNameWhenSet()), "No flag info in description!");
        Assertions.assertTrue(descriptionBuilder.toString().contains(CspCommonFlags.BIG_ENDIAN.getNameWhenUnset()), "No flag info in description!");
        Assertions.assertTrue(descriptionBuilder.toString().contains(CspCommonFlags.ENDIANNESS_DIFFERENCE.getNameWhenUnset()), "No flag info in description!");
        Assertions.assertTrue(descriptionBuilder.toString().contains(CspCommonFlags.BITNESS_32.getFlagTypeName()), "No flag type info in description!");
    }

    @Test
    public void evaluateFlagsStringDescriptionWitNoFlagsAndNoHeaderTest()
    {
        Set<CspCommonFlags> flags = Set.of();
        StringBuilder descriptionBuilder = CspFlagUtils.evaluateFlagsStringDescription(CspCommonFlags.class, flags, true, false);

        Assertions.assertTrue(descriptionBuilder.isEmpty(), "Description is not empty!");
    }
}