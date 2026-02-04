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
    private final Stack<ICspReferenceTypeTraits> cspTypeTraitsStack =  new Stack<>();
    private @Nullable ICspReferenceTypeTraits rootCspReferenceTypeTraits;
    private boolean done = false;

    @Override
    public ICspReferenceTypeTraitsBuilder addNode(Class<? extends ICspTypeTraits> traitsClazz)
    {
        testStateNotDone();

        if (!cspTypeTraitsStack.empty())
        {
            createNextCspTypeTraits(traitsClazz);
        }
        else
        {
            if (ICspReferenceTypeTraits.class.isAssignableFrom(traitsClazz))
            {
                @SuppressWarnings("unchecked")
                Class<? extends ICspReferenceTypeTraits> referenceTraitsClazz =
                    (Class<? extends ICspReferenceTypeTraits>) traitsClazz;
                createRootCspReferenceTypeTraits(referenceTraitsClazz);
            }
            else
            {
                throw new IllegalArgumentException("Primitive cannot be root type of csp type traits!");
            }
        }

        return this;
    }

    @Override
    public ICspReferenceTypeTraitsBuilder addReference(Class<?> clazz, boolean reference)
    {
        return null;
    }

    @Override
    public ICspReferenceTypeTraitsBuilder addString(boolean reference, Charset charset)
    {
        return null;
    }

    @Override
    public ICspReferenceTypeTraitsBuilder addGeneric(Class<?> clazz, boolean reference, int genericsNumber)
    {
        return null;
    }

    @Override
    public ICspReferenceTypeTraitsBuilder addArray(boolean reference, boolean fixedSizeArray)
    {
        return null;
    }

    @Override
    public ICspReferenceTypeTraitsBuilder addArrayDimension(boolean reference, boolean fixedSizeArray)
    {
        return null;
    }

    @Override
    public ICspReferenceTypeTraits build()
    {
        testStateNotDone();

        if (rootCspReferenceTypeTraits == null)
        {
            throw new IllegalStateException("Build of root csp type traits cannot be null!");
        }

        commitNode();

        if (!done)
        {
            throw new IllegalStateException("Cannot build csp type traits because its was not built to the end!");
        }

        return rootCspReferenceTypeTraits;
    }

    private void commitNode()
    {
        if (cspTypeTraitsStack.peek() instanceof CspNotGenericTypeTraits cspProcessorObjectTypeTraits
                && cspProcessorObjectTypeTraits.getProcessorClazz() == Object.class)
        {
            throw new IllegalStateException("Its illegal to add new node until class of processor object is not set!");
        }

        while (!cspTypeTraitsStack.empty())
        {
            if (ICspGenericTypeTraits.class.isAssignableFrom(cspTypeTraitsStack.peek().getClass()))
            {
                CspGenericTypeTraits cspGenericTypeTraits = (CspGenericTypeTraits) cspTypeTraitsStack.peek();
                if (cspGenericTypeTraits.getGenericsNumber() == cspGenericTypeTraits.getGenericTypeCollectionTypeTraits().size())
                {
                    cspTypeTraitsStack.pop();
                }
                else
                {
                    break;
                }
            }
            else
            {
                cspTypeTraitsStack.pop();
            }
        }

        if (cspTypeTraitsStack.empty())
        {
            done = true;
        }
    }

    private void testStateNotDone()
    {
        if (done)
        {
            throw new IllegalStateException("Csp type traits already committed!");
        }
    }

    private void createRootCspReferenceTypeTraits(Class<? extends ICspReferenceTypeTraits> referenceTraitsClazz)
    {
        ICspTypeTraits cspTypeTraits = createCspTypeTraits(referenceTraitsClazz);
        if (cspTypeTraits instanceof ICspReferenceTypeTraits cspReferenceTypeTraits)
        {
            cspTypeTraitsStack.add(cspReferenceTypeTraits);
            rootCspReferenceTypeTraits = cspReferenceTypeTraits;
        }
        else
        {
            throw new AssertionError("createCspTypeTraits() returned primitive type traits on query that "
                                         + "could not lead to such a result");
        }
    }

    private void createNextCspTypeTraits(Class<? extends ICspTypeTraits> referenceTraitsClazz)
    {
        commitNode();

        ICspTypeTraits cspTypeTraits = createCspTypeTraits(referenceTraitsClazz);
        // Should always evaluate to true (look on commitNode()).
        if (ICspGenericTypeTraits.class.isAssignableFrom(cspTypeTraitsStack.peek().getClass()))
        {
            CspGenericTypeTraits cspGenericTypeTraits = (CspGenericTypeTraits) cspTypeTraitsStack.peek();
            if (!cspGenericTypeTraits.areGenericPrimitivesAllowed() && referenceTraitsClazz == ICspTypeTraits.class)
            {
                throw new IllegalArgumentException("Primitive cannot be as generic type anywhere except arrays!");
            }

            cspGenericTypeTraits.addGenericTypeTypeTraits(cspTypeTraits);
        }

        cspTypeTraitsStack.add(cspTypeTraits);
    }

    private ICspTypeTraits createCspTypeTraits(Class<? extends ICspTypeTraits> traitsClazz)
    {
        if (traitsClazz == CspReferenceTypeTraits.class)
        {
            return new CspReferenceTypeTraits();
        }
        else if (traitsClazz == ICspCollectionTypeTraits.class)
        {
            return new CspCollectionTypeTraits();
        }
        else if (traitsClazz == ICspMapTypeTraits.class)
        {
            return new CspMapTypeTraits();
        }
        else if (traitsClazz == ICspStringTypeTraits.class)
        {
            return new CspStringTypeTraits();
        }
        else if (traitsClazz == ICspArrayTypeTraits.class)
        {
            return new CspArrayTypeTraits();
        }
        else if (traitsClazz == ICspGenericTypeTraits.class)
        {
            return new CspGenericTypeTraits();
        }
        else
        {
            return new ICspTypeTraits(){};
        }
    }
}
