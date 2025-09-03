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

package io.andreygs.jcsp_base.types.api;

import io.andreygs.jcsp_base.types.internal.CspFlagUtils;

import java.util.Arrays;
import java.util.List;

/**
 * TODO: place description here
 */
public enum CspDataFlags implements ICspFlag
{
    ALIGNMENT_MAY_BE_NOT_EQUAL(0x1, Messages.CspDataFlags_AlignmentMayBeNotEqual,
                               Messages.CspDataFlags_AlignmentsAreEqual),
    SIZE_OF_INTEGERS_MAY_BE_NOT_EQUAL(0x2, Messages.CspDataFlags_SizeOfIntegersMayBeNotEqual,
                                      Messages.CspDataFlags_SizeOfIntegersAreEqual),
    ALLOW_UNMANAGED_POINTERS(0x4, Messages.CspDataFlags_AllowUnmanagedPointers,
                             Messages.CspDataFlags_DoNotAllowUnmanagedPointers),
    CHECK_RECURSIVE_POINTERS(0x8, Messages.CspDataFlags_CheckRecursivePointers,
                             Messages.CspDataFlags_DoNotCheckRecursivePointers),
    SIMPLY_ASSIGNABLE_TAGS_OPTIMIZATIONS_ARE_TURNED_OFF(0x10,
                                                        Messages.CspDataFlags_SimplyAssignableTagsOptimizationsAreOff,
                                                        Messages.CspDataFlags_SimplyAssignableTagsOptimizationsAreAvailable);

    private static int validFlagsMask = 0;

    static
    {
        Arrays.stream(values()).forEach(flag -> validFlagsMask |= flag.value);
    }

    private final int value;
    private final String name;
    private final String nameWhenSet;
    private final String nameWhenUnset;

    CspDataFlags(int value, String nameWhenSet, String nameWhenUnset)
    {
        this.value = value;
        this.name = Messages.CspDataFlags_Type + ": " + nameWhenSet;
        this.nameWhenSet = nameWhenSet;
        this.nameWhenUnset = nameWhenUnset;
    }

    public static int groupValidFlagsMask()
    {
        return validFlagsMask;
    }

    public static String stringDescription(List<CspDataFlags> setFlags)
    {
        return stringDescription(setFlags, true, true);
    }

    public static String stringDescription(List<CspDataFlags> setFlags, boolean onlySetFlagsShouldBePrinted,
                                           boolean printEmptyHeaderIfNoFlagIsSet)
    {
        return CspFlagUtils.stringDescription(values(), setFlags, Messages.CspDataFlags_Type,
                                              onlySetFlagsShouldBePrinted, printEmptyHeaderIfNoFlagIsSet);
    }

    @Override
    public int getValue()
    {
        return value;
    }

    @Override
    public String getNameWhenSet()
    {
        return nameWhenSet;
    }

    @Override
    public String getNameWhenUnset()
    {
        return nameWhenUnset;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
