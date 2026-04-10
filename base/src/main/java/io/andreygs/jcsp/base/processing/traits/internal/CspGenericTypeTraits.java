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

import io.andreygs.jcsp.base.processing.traits.ICspGenericTypeTraits;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * TODO: place description here
 */
class CspGenericTypeTraits extends CspReferenceTypeTraits
    implements ICspGenericTypeTraits, ICspGenericTypeTraitsParameterAdder
{
    private final List<ICspReferenceTypeTraits> genericTypeParametersTypeTraits;
    private final int typeParametersNumber;

    public CspGenericTypeTraits(Class<?> clazz, boolean reference, int typeParametersNumber)
    {
        super(clazz, reference);
        this.genericTypeParametersTypeTraits = new ArrayList<>(typeParametersNumber);
        this.typeParametersNumber = typeParametersNumber;
    }

    @Override
    public List<? extends ICspReferenceTypeTraits> getGenericTypeParametersFieldTraits()
    {
        return Collections.unmodifiableList(genericTypeParametersTypeTraits);
    }

    @Override
    public boolean addGenericTypeTraitsParameter(ICspReferenceTypeTraits cspReferenceTypeTraits)
    {
        if (typeParametersNumber == genericTypeParametersTypeTraits.size())
        {
            throw new IllegalStateException("All generic type parameters have been added earlier!");
        }
        genericTypeParametersTypeTraits.add(cspReferenceTypeTraits);
        return typeParametersNumber == genericTypeParametersTypeTraits.size();
    }
}
