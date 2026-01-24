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

package io.andreygs.jcsp.base.types;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * Utils for common use in CSP Flag implementations.
 */
public class CspFlagUtils
{
    private CspFlagUtils()
    {
    }

    /**
     * Calculates mask of flags as integer.
     *
     * @param flags Array with flags.
     * @return calculated mask.
     * @param <T> enum implemented {@link ICspFlag}.
     */
    public static <T extends ICspFlag> int calculateFlagMask(T[] flags)
    {
        return calculateFlagMask(Arrays.stream(flags));
    }

    /**
     * Calculates mask of flags as integer.
     *
     * @param flags List of flags.
     * @return calculated mask.
     * @param <T> enum implemented {@link ICspFlag}.
     */
    public static <T extends ICspFlag> int calculateFlagMask(List<T> flags)
    {
        return calculateFlagMask(flags.stream());
    }

    /**
     * Calculates mask of flags as integer.
     *
     * @param flags Stream with flags.
     * @return calculated mask.
     * @param <T> enum implemented {@link ICspFlag}.
     */
    public static <T extends ICspFlag> int calculateFlagMask(Stream<T> flags)
    {
        int[] flagMask = new int[1];
        flags.forEach(flag -> flagMask[0] |= flag.getValue() );
        return flagMask[0];
    }

    /**
     * Checks state of flag in flag mask.
     *
     * @param flagMask Mask of flag that are set.
     * @param flag Flag which state needs to be checked.
     * @return state of flag.
     */
    public static boolean isFlagSet(int flagMask, ICspFlag flag)
    {
        return (flag.getValue() & flagMask) != 0;
    }

    /**
     * Evaluates user-friendly description of flags using {@link ICspFlag#getNameWhenSet()} and {@link ICspFlag#getFlagTypeName()}.
     * <p>
     * Default options are:
     * <ul>
     *     <li>only the flags from the list (set) should be included.</li>
     *     <li>include a header type of flags if no other info will go to description.</li>
     * </ul>
     *
     * @param flagsEnum The enumeration class to which the list items belong.
     * @param setFlags List of set flags.
     * @return description string,
     * @param <T> enum implemented {@link ICspFlag}.
     */
    public static <T extends ICspFlag> StringBuilder evaluateFlagsStringDescription(Class<? extends ICspFlag> flagsEnum,
                                                                             List<T> setFlags)
    {
        return evaluateFlagsStringDescription(flagsEnum, setFlags, true, true);
    }

    /**
     * Evaluates user-friendly description of flags using {@link ICspFlag#getNameWhenSet()},
     * {@link ICspFlag#getNameWhenUnset()} and {@link ICspFlag#getFlagTypeName()}.
     *
     * @param flagsEnum The enumeration class to which the list items belong.
     * @param setFlags List of set flags.
     * @param onlySetFlagsShouldBeIncluded The option that only the flags from the list (set) should be included.
     * @param includeEmptyHeader The option to include a header type of flags if no other info will go to description.
     * @return description string,
     * @param <T> enum implemented {@link ICspFlag}.
     */
    public static <T extends ICspFlag> StringBuilder evaluateFlagsStringDescription(Class<? extends ICspFlag> flagsEnum,
                                                                             List<T> setFlags,
                                                                             boolean onlySetFlagsShouldBeIncluded,
                                                                             boolean includeEmptyHeader)
    {
        int flagMask = calculateFlagMask(setFlags);
        StringBuilder descriptionBuilder = new StringBuilder();

        if (!setFlags.isEmpty() || !onlySetFlagsShouldBeIncluded || includeEmptyHeader)
        {
            ICspFlag[] allFlags = getFlagConstants(flagsEnum);
            descriptionBuilder.append(allFlags[0].getFlagTypeName());
            descriptionBuilder.append(":");

            boolean[] firstFlagArr = new boolean[1];
            firstFlagArr[0] = true;
            Arrays.stream(allFlags).forEach(flag -> {
                boolean flagIsSet = isFlagSet(flagMask, flag);
                if (flagIsSet || !onlySetFlagsShouldBeIncluded)
                {
                    descriptionBuilder.append(firstFlagArr[0] ? " " : ", ");
                    descriptionBuilder.append(flagIsSet ? flag.getNameWhenSet() : flag.getNameWhenUnset());
                    firstFlagArr[0] = false;
                }
            });
        }
        return descriptionBuilder;
    }

    /**
     * Gets all constants from enum implementing ICspFlag.
     *
     * @param flagsEnum The enumeration class.
     * @return array of constants
     * @param <T> enum implemented {@link ICspFlag}.
     */
    public static <T extends ICspFlag> ICspFlag[] getFlagConstants(Class<T> flagsEnum)
    {
        return flagsEnum.getEnumConstants();
    }
}
