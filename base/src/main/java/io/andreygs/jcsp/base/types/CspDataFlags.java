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

/**
 * CSP Data flags according to CSP reference.
 */
public enum CspDataFlags implements ICspFlag
{
    /**
     * Alignment of structs that take a part in CSP Data Body in modules that serialize and deserialize CSP Message, may
     * be not equal. If not set, may enable class raw memory copy.
     * <p>
     * In Java CSP realization must always be set, because class object instance raw memory copy is not allowed.
     */
    ALIGNMENT_MAY_BE_NOT_EQUAL(0x1, Messages.CspDataFlags_AlignmentMayBeNotEqual,
                               Messages.CspDataFlags_AlignmentsAreEqual),

    /**
     * Indicated that there is possibility of that the compilers that built modules that take a part in CSP
     * Serialization may interpret with different size any of the integers fields.
     * This can be the case when dealing with native C/C++ structs, where long type is 32 bits long on Windows
     * and 64 bits long on Linux. If current structs are serializing care should be taken in determining
     * real size of struct field on either end of Serialization process.
     */
    SIZE_OF_INTEGERS_MAY_BE_NOT_EQUAL(0x2, Messages.CspDataFlags_SizeOfIntegersMayBeNotEqual,
                                      Messages.CspDataFlags_SizeOfIntegersAreEqual),

    /**
     * In C/C++ processing any pointer that is a part of some serialized struct must be managed by it and
     * Serialization process must include individual procedures to handle it. Any free pointer is threatened as
     * error and no serialization must be done.
     * <p>
     * This flag removes this restriction and allow Serialization of pointers on generic order, but adds some
     * memory and processing overhead.
     */
    ALLOW_UNMANAGED_POINTERS(0x4, Messages.CspDataFlags_AllowUnmanagedPointers,
                             Messages.CspDataFlags_DoNotAllowUnmanagedPointers),

    /**
     * Indicates whether there can be recursively pointed references and if so processing will include appropriate
     * precautions.
     */
    CHECK_RECURSIVE_POINTERS(0x8, Messages.CspDataFlags_CheckRecursivePointers,
                             Messages.CspDataFlags_DoNotCheckRecursivePointers),

    /**
     * Turns of all optimizations of "simply assignable" tags. See CSP reference.
     * <p>
     * In Java CSP realization must always be set.
     */
    SIMPLY_ASSIGNABLE_TAGS_OPTIMIZATIONS_ARE_TURNED_OFF(0x10,
                                                        Messages.CspDataFlags_SimplyAssignableTagsOptimizationsAreOff,
                                                        Messages.CspDataFlags_SimplyAssignableTagsOptimizationsAreAvailable),

    /**
     * A flag indicating that the structure should be processed taking into account the original cross-references
     * of the top structure and its components.
     * <p>
     * In Java there is no such problem of links structure, so using it leads to the same result as
     * using of {@link CspDataFlags#ALLOW_UNMANAGED_POINTERS}.
     */
    CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE(0x20,
                                                                 Messages.CspDataFlags_CheckRecursivePointersWithMaintainingLinkStructure,
                                                                 Messages.CspDataFlags_DoNotCheckRecursivePointersWithMaintainingLinkStructure);

    public static final int VALID_FLAGS_MASK = CspFlagUtils.calculateFlagMask(values());

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
        return Messages.CspDataFlags_Type;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
