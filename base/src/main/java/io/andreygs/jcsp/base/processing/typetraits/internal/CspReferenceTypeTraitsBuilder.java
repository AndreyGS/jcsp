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

import io.andreygs.jcsp.base.processing.typetraits.ICspArrayDimensionTypeTraits;
import io.andreygs.jcsp.base.processing.typetraits.ICspArrayTypeTraits;
import io.andreygs.jcsp.base.processing.typetraits.ICspGenericTypeTraits;
import io.andreygs.jcsp.base.processing.typetraits.ICspReferenceTypeTraits;
import io.andreygs.jcsp.base.processing.typetraits.ICspStringTypeTraits;
import io.andreygs.jcsp.base.processing.typetraits.ICspReferenceTypeTraitsBuilder;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.Charset;
import java.util.Stack;

/**
 * TODO: place description here
 */
class CspReferenceTypeTraitsBuilder implements ICspReferenceTypeTraitsBuilder
{
    private final Stack<ICspGenericTypeTraitsParameterAdder> cspGenericTypeTraitsParameterAdderStack =  new Stack<>();
    private @Nullable ICspReferenceTypeTraits rootCspReferenceTypeTraits;

    @Override
    public ICspReferenceTypeTraitsBuilder addReference(Class<?> clazz, boolean reference)
    {
        testStateNotDone();

        if (clazz.getTypeParameters().length > 0)
        {
            throw new IllegalArgumentException("Class is generic and it should be added using another methods!");
        }

        ICspReferenceTypeTraits newNode = new CspReferenceTypeTraits(clazz, reference);
        commitNode(newNode);

        return this;
    }

    @Override
    public ICspReferenceTypeTraitsBuilder addString(boolean reference, Charset charset)
    {
        testStateNotDone();

        ICspStringTypeTraits newNode = new CspStringTypeTraits(reference, charset);
        commitNode(newNode);

        return this;
    }

    @Override
    public ICspReferenceTypeTraitsBuilder addGeneric(Class<?> clazz, boolean reference)
    {
        testStateNotDone();

        if (clazz.getTypeParameters().length < 1)
        {
            throw new IllegalArgumentException("Class is not generic and it should be added using another methods!.");
        }

        ICspGenericTypeTraits newNode = new CspGenericTypeTraits(clazz, reference, clazz.getTypeParameters().length);
        commitNode(newNode);

        return this;
    }

    @Override
    public ICspReferenceTypeTraitsBuilder addArray(Class<?> clazz, boolean reference, boolean fixedSizeArray)
    {
        testStateNotDone();

        if (!clazz.isArray())
        {
            throw new IllegalArgumentException(clazz.getName() + " is not an array");
        }

        ICspArrayTypeTraits newNode = new CspArrayTypeTraits(clazz, reference, fixedSizeArray);
        commitNode(newNode);

        return this;
    }

    @Override
    public ICspReferenceTypeTraitsBuilder addArrayDimension(boolean reference, boolean fixedSizeArray)
    {
        testStateNotDone();

        ICspArrayDimensionTypeTraits newNode = new CspArrayDimensionTypeTraits(reference, fixedSizeArray);
        commitNode(newNode);

        return this;
    }

    @Override
    public ICspReferenceTypeTraits build()
    {
        if (!cspGenericTypeTraitsParameterAdderStack.empty() || rootCspReferenceTypeTraits == null)
        {
            throw new IllegalStateException("Cannot build csp type traits because its was not built to the end!");
        }

        return rootCspReferenceTypeTraits;
    }

    private void commitNode(ICspReferenceTypeTraits newNode)
    {
        if (rootCspReferenceTypeTraits == null)
        {
            rootCspReferenceTypeTraits = newNode;
        }

        if (!cspGenericTypeTraitsParameterAdderStack.empty())
        {
            ICspGenericTypeTraitsParameterAdder cspGenericTypeTraitsParameterAdder =
                cspGenericTypeTraitsParameterAdderStack.peek();

            if (cspGenericTypeTraitsParameterAdder.addGenericTypeParametersTypeTraits(newNode))
            {
                cspGenericTypeTraitsParameterAdderStack.pop();
            }
        }

        if (newNode instanceof ICspGenericTypeTraitsParameterAdder cspGenericTypeTraitsParameterAdder)
        {
            cspGenericTypeTraitsParameterAdderStack.push(cspGenericTypeTraitsParameterAdder);
        }
    }

    private void testStateNotDone()
    {
        if (cspGenericTypeTraitsParameterAdderStack.empty() && rootCspReferenceTypeTraits != null)
        {
            throw new IllegalStateException("Csp type traits already committed!");
        }
    }
}
