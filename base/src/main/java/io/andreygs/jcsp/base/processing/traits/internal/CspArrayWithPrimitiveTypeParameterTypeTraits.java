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

package io.andreygs.jcsp.base.processing.traits.internal;

import io.andreygs.jcsp.base.processing.traits.ICspReferenceTypeTraits;

import java.util.List;

/**
 * TODO: place description here
 */
final class CspArrayWithPrimitiveTypeParameterTypeTraits extends CspReferenceTypeTraits
    implements ICspArrayTypeTraits
{
    private final List<Boolean> dimensionReferenceFlags;
    private final List<Boolean> dimensionFixedSizeFlags;

    CspArrayWithPrimitiveTypeParameterTypeTraits(Class<?> arrayClazz,
        List<Boolean> dimensionReferenceFlags, List<Boolean> dimensionFixedSizeFlags)
    {
        super(arrayClazz, dimensionReferenceFlags.get(0));
        this.dimensionReferenceFlags = List.copyOf(dimensionReferenceFlags);
        this.dimensionFixedSizeFlags = List.copyOf(dimensionFixedSizeFlags);
    }

    private CspArrayWithPrimitiveTypeParameterTypeTraits(Class<?> arrayClazz,
        List<Boolean> dimensionReferenceFlags, List<Boolean> dimensionFixedSizeFlags, boolean noCopyTag)
    {
        super(arrayClazz, dimensionReferenceFlags.get(0));
        this.dimensionReferenceFlags = dimensionReferenceFlags;
        this.dimensionFixedSizeFlags = dimensionFixedSizeFlags;
    }

    @Override
    public ICspReferenceTypeTraits obtainInstanceWithOverriddenReferenceTrait(boolean reference)
    {
        if (isReference() != reference)
        {
            return new CspArrayWithPrimitiveTypeParameterTypeTraits(getClazz(), dimensionReferenceFlags,
                dimensionFixedSizeFlags, true);
        }
        else
        {
            return this;
        }
    }

    @Override
    public boolean isDimensionReference(int dimension)
    {
        return dimensionReferenceFlags.get(dimension);
    }

    @Override
    public boolean isDimensionFixedSize(int dimension)
    {
        return dimensionFixedSizeFlags.get(dimension);
    }
}
