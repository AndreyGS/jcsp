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

import java.util.List;

/**
 * CSP Common flags according to CSP reference.
 */
public enum CspCommonFlags implements ICspFlag
{
    /**
     * Bitness of system on which serialization is performed is 32 bit.
     * <p>
     * Because Java specification states that JVM is always 64-bit, this flag is always unset on serialization (but not
     * deserialization!) process.
     */
    BITNESS_32(0x1, Messages.CspCommonFlags_Bitness32, Messages.CspCommonFlags_Bitness64),

    /**
     * Indicates that serialization be doing (or done) with big-endian format.
     * <p>
     * Java specification states that JVM works natively with big-endian format, so this should be a preferable option
     * in most scenarios.
     */
    BIG_ENDIAN(0x2, Messages.CspCommonFlags_BigEndian, Messages.CspCommonFlags_LittleEndian),

    /**
     * There (is) was endianness difference of "using big endian format" flag and execution environment where
     * serialization is (or was) performed.
     * <p>
     * The state of this flag is set automatically during serialization and affects both
     * the serialization process itself and deserialization.
     */
    ENDIANNESS_DIFFERENCE(0x4, Messages.CspCommonFlags_EndiannessDifference,
                          Messages.CspCommonFlags_No_EndiannessDifference);

    public static final int VALID_FLAGS_MASK = CspFlagUtils.calculateFlagMask(values());

    private final int value;
    private final String name;
    private final String nameWhenSet;
    private final String nameWhenUnset;

    CspCommonFlags(int value, String nameWhenSet, String nameWhenUnset)
    {
        this.value = value;
        this.name = Messages.CspCommonFlags_Type + ": " + nameWhenSet;
        this.nameWhenSet = nameWhenSet;
        this.nameWhenUnset = nameWhenUnset;
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
    public String getFlagTypeName()
    {
        return Messages.CspCommonFlags_Type;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
