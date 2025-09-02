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

package io.andreygs.jcsp_base.types.internal;

import io.andreygs.jcsp_base.types.api.ICspFlag;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

/**
 * TODO: place description here
 */
public class CspFlagsMethods
{
    public static <T extends ICspFlag> String stringDescription(List<T> setFlags, boolean printEmptyHeaderIfFlagsPrinted
        , boolean printEmptyHeaderIfNoFlagIsSet)
    {
        return stringDescriptionInternal(setFlags.get(0).groupValues(), setFlags, printEmptyHeaderIfFlagsPrinted
            , printEmptyHeaderIfNoFlagIsSet);
    }

    public static String stringDescription(Class<? extends ICspFlag> clazz, boolean printEmptyHeaderIfFlagsPrinted
        , boolean printEmptyHeaderIfNoFlagIsSet)
    {
        try
        {
            ICspFlag[] flags = (ICspFlag[])clazz.getDeclaredMethod("values").invoke(null);
            return stringDescriptionInternal(flags, null, printEmptyHeaderIfFlagsPrinted, printEmptyHeaderIfNoFlagIsSet);
        }
        catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static <T extends ICspFlag> int calcFlagMask(List<T> setFlags)
    {
        int[] flagsSum = new int[1];
        flagsSum[0] = 0;
        setFlags.stream().forEach(flag -> flagsSum[0] |= flag.getValue() );
        return flagsSum[0];
    }

    public static boolean flagISet(int flagMask, ICspFlag flag)
    {
        return (flag.getValue() & flagMask) != 0;
    }

    private static <T extends ICspFlag> String stringDescriptionInternal(ICspFlag[] allFlags, @Nullable List<T> setFlags
        , boolean onlySetFlagsShouldBePrinted, boolean printEmptyHeaderIfFlagsPrinted)
    {
        int flagMask = setFlags != null ? calcFlagMask(setFlags) : 0;
        String[] messageArr = new String[1];
        messageArr[0] = "";

        if (setFlags != null && !setFlags.isEmpty() || !onlySetFlagsShouldBePrinted || printEmptyHeaderIfFlagsPrinted)
        {
            messageArr[0] = allFlags[0].flagGroupName() + ": ";

            boolean[] firstFlagArr = new boolean[1];
            firstFlagArr[0] = true;
            Arrays.stream(allFlags).forEach(flag -> {
                boolean flagIsSet = flagISet(flagMask, flag);
                if (flagIsSet || !onlySetFlagsShouldBePrinted)
                {
                    messageArr[0] += (firstFlagArr[0] ? "" : ", ")
                                         + (flagIsSet ? flag.getNameWhenSet() : flag.getNameWhenUnset());
                    firstFlagArr[0] = false;
                }
            });
        }
        return messageArr[0];
    }
}
