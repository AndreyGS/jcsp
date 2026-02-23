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

package io.andreygs.jcsp.base.processing.typetraits.internal;

import io.andreygs.jcsp.base.processing.typetraits.ICspGenericTypeTraits;
import io.andreygs.jcsp.base.processing.typetraits.ICspReferenceTypeTraits;

import java.util.ArrayList;
import java.util.Collection;

/**
 * TODO: place description here
 */
public class CspGenericTypeTraits extends CspReferenceTypeTraits
    implements ICspGenericTypeTraits, ICspGenericTypeTraitsParameterAdder
{
    private final Collection<ICspReferenceTypeTraits> genericTypeParametersTypeTraits;
    private final int typeParametersNumber;

    public CspGenericTypeTraits(Class<?> typeClazz, boolean reference, int typeParametersNumber)
    {
        super(typeClazz, reference);
        this.genericTypeParametersTypeTraits = new ArrayList<>(typeParametersNumber);
        this.typeParametersNumber = typeParametersNumber;
    }

    @Override
    public Collection<? extends ICspReferenceTypeTraits> getGenericTypeParametersTypeTraits()
    {
        return genericTypeParametersTypeTraits;
    }

    @Override
    public boolean addGenericTypeParametersTypeTraits(ICspReferenceTypeTraits cspReferenceTypeTraits)
    {
        if (typeParametersNumber == genericTypeParametersTypeTraits.size())
        {
            throw new IllegalStateException("All generic type parameters have been added earlier!");
        }

        genericTypeParametersTypeTraits.add(cspReferenceTypeTraits);
        return typeParametersNumber == genericTypeParametersTypeTraits.size();
    }

    protected int getTypeParametersNumberToAdd()
    {
        return typeParametersNumber - genericTypeParametersTypeTraits.size();
    }
}
