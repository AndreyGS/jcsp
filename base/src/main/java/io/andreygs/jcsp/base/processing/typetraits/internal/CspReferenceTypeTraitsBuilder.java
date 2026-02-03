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
import io.andreygs.jcsp.base.processing.typetraits.ICspCollectionTypeTraits;
import io.andreygs.jcsp.base.processing.typetraits.ICspMapTypeTraits;
import io.andreygs.jcsp.base.processing.typetraits.ICspReferenceTypeTraits;
import io.andreygs.jcsp.base.processing.typetraits.ICspStringTypeTraits;
import io.andreygs.jcsp.base.processing.typetraits.ICspTypeTraits;
import io.andreygs.jcsp.base.processing.typetraits.ICspReferenceTypeTraitsBuilder;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.Charset;
import java.util.Stack;

/**
 * TODO: place description here
 */
class CspReferenceTypeTraitsBuilder implements ICspReferenceTypeTraitsBuilder
{
    private final Stack<State> states =  new Stack<>();

    private @Nullable ICspReferenceTypeTraits rootCspReferenceTypeTraits;

    private @Nullable ICspTypeTraits tempCspTypeTraits;

    private boolean fixedSize;
    private @Nullable Charset charset;

    CspReferenceTypeTraitsBuilder()
    {
        states.push(State.Ready);
    }

    @Override
    public ICspReferenceTypeTraitsBuilder addNode(Class<? extends ICspTypeTraits> traitsClazz)
    {
        testStateNotDone();

        if (states.peek() != State.Ready)
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
    public ICspReferenceTypeTraitsBuilder setReference()
    {
        testStateNotDone();
        return null;
    }

    @Override
    public ICspReferenceTypeTraitsBuilder setProcessorClazz(Class<?> processorClazz)
    {
        testStateNotDone();
        return null;
    }

    @Override
    public ICspReferenceTypeTraitsBuilder setFixedSize()
    {
        testStateNotDone();
        return null;
    }

    @Override
    public ICspReferenceTypeTraitsBuilder setCharset(Charset charset)
    {
        testStateNotDone();
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

        if (states.peek() != State.Done)
        {
            throw new IllegalStateException("Cannot build csp type traits because its was not built to the end!");
        }

        return rootCspReferenceTypeTraits;
    }

    private void commitNode()
    {
        switch (states.peek())
        {
        case Value:
            states.pop();
            states.pop();
            states.pop();
            break;
        case Element:
            states.pop();
            states.pop();
            break;
        case Reference:
            states.pop();
            states.push(State.Done);
            break;
        case Array, Collection, Map, Key:
            break;
        case Ready:
            states.push(State.Done);
            break;
        case Done:
            testStateNotDone();
            break;
        }
    }

    private void testStateNotDone()
    {
        if (states.peek() == State.Done)
        {
            throw new IllegalStateException("Csp type traits already committed!");
        }
    }

    private void createRootCspReferenceTypeTraits(Class<? extends ICspReferenceTypeTraits> referenceTraitsClazz)
    {
        if (rootCspReferenceTypeTraits != null)
        {
            throw new IllegalStateException("Root csp type traits already added!");
        }

        ICspTypeTraits cspTypeTraits = createCspTypeTraits(referenceTraitsClazz);
        if (cspTypeTraits instanceof ICspReferenceTypeTraits cspReferenceTypeTraits)
        {
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
        
    }

    private ICspTypeTraits createCspTypeTraits(Class<? extends ICspTypeTraits> traitsClazz)
    {
        if (traitsClazz == ICspReferenceTypeTraits.class)
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
        else
        {
            return new ICspTypeTraits(){};
        }
    }

    private enum State
    {
        Ready, Array, Collection, Map, Element, Key, Value, Reference, Done
    }

}
