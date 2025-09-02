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

import io.andreygs.jcsp_base.types.internal.CspFlagsMethods;

import java.util.List;

/**
 * TODO: place description here
 */
public enum CspCommonFlags
    implements ICspFlag
{
    BIG_ENDIAN(1, Messages.CspCommonFlags_BigEndian, Messages.CspCommonFlags_LittleEndian),
    BITNESS_32(2, Messages.CspCommonFlags_Bitness32, Messages.CspCommonFlags_Bitness64),
    ENDIANNESS_DIFFERENCE(4, Messages.CspCommonFlags_EndiannessDifference, Messages.CspCommonFlags_No_EndiannessDifference);

    private final int value;
    private final String nameWhenSet;
    private final String nameWhenUnset;

    CspCommonFlags(int value, String nameWhenSet, String nameWhenUnset)
    {
        this.value = value;
        this.nameWhenSet = nameWhenSet;
        this.nameWhenUnset = nameWhenUnset;
    }

    public static String stringDescription(List<CspCommonFlags> setFlags, boolean onlySetFlagsShouldBePrinted
        , boolean printEmptyHeaderIfFlagsPrinted)
    {
        if (setFlags.isEmpty())
        {
            return CspFlagsMethods.stringDescription(CspCommonFlags.class, onlySetFlagsShouldBePrinted
                , printEmptyHeaderIfFlagsPrinted);
        }
        else
        {
            return CspFlagsMethods.stringDescription(setFlags, onlySetFlagsShouldBePrinted
                , printEmptyHeaderIfFlagsPrinted);
        }
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
    public String flagGroupName()
    {
        return Messages.CspCommonFlags_Type;
    }

    @Override
    public int validFlagsMask()
    {
        return 7;
    }

    @Override
    public ICspFlag[] groupValues()
    {
        return values();
    }
}
