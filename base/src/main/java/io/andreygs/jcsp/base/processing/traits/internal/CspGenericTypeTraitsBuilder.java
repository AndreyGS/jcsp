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
import io.andreygs.jcsp.base.processing.traits.ICspGenericTypeTraitsBuilder;
import io.andreygs.jcsp.base.processing.traits.ICspReferenceTypeTraits;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Stack;

/**
 * TODO: place description here
 */
final class CspGenericTypeTraitsBuilder implements ICspGenericTypeTraitsBuilder
{
    private final Stack<ICspGenericTypeTraitsParameterAdder> cspGenericTypeTraitsParameterAdderStack = new Stack<>();
    private @Nullable ICspGenericTypeTraits rootCspGenericTypeTraits;

    @Override
    public ICspGenericTypeTraitsBuilder addReference(Class<?> clazz, boolean reference)
    {
        if (clazz.getTypeParameters().length > 0)
        {
            throw new IllegalArgumentException("Generics should be added using addGeneric() method!");
        }
        if (clazz.isArray())
        {
            throw new IllegalArgumentException(
                clazz.getName() + " is an array and it should be added using addArray() " + "method!");
        }
        if (clazz == String.class)
        {
            throw new IllegalArgumentException("String class should be added using addString() method!");
        }
        testRootNodeIsSet();
        testStateNotDone();
        ICspReferenceTypeTraits newNode = new CspReferenceTypeTraits(clazz, reference);
        commitNode(newNode);
        return this;
    }

    @Override
    public ICspGenericTypeTraitsBuilder addString(boolean reference, Charset charset)
    {
        testRootNodeIsSet();
        testStateNotDone();
        ICspStringTypeTraits newNode = new CspStringTypeTraits(reference, charset);
        commitNode(newNode);
        return this;
    }

    @Override
    public ICspGenericTypeTraitsBuilder addGeneric(Class<?> clazz, boolean reference)
    {
        if (clazz.getTypeParameters().length < 1)
        {
            throw new IllegalArgumentException(
                clazz.getName() + " is not generic and it should be added using another " + "methods!.");
        }
        testStateNotDone();
        ICspGenericTypeTraitsParameterAdder newNode =
            new CspGenericTypeTraits(clazz, reference, clazz.getTypeParameters().length);
        commitGenericTypeNode(newNode);
        return this;
    }

    @Override
    public ICspGenericTypeTraitsBuilder addArray(Class<?> clazz, List<Boolean> dimensionReferenceFlags,
        List<Boolean> dimensionFixedSizeFlags)
    {
        if (!clazz.isArray())
        {
            throw new IllegalArgumentException(
                clazz.getName() + " is not an array and it should be added using another " + "methods!");
        }
        testStateNotDone();
        int arrayDimensionsNumber = evalArrayDimensionsNumber(clazz);
        if (arrayDimensionsNumber != dimensionReferenceFlags.size())
        {
            throw new IllegalArgumentException(
                clazz.getName() + " dimensions number differs from " + " dimensionReferenceFlags.size()!");
        }
        if (arrayDimensionsNumber != dimensionFixedSizeFlags.size())
        {
            throw new IllegalArgumentException(
                clazz.getName() + " dimensions number differs from " + " dimensionFixedSizeFlags.size()!");
        }
        boolean arrayHasPrimitiveTypeParameter = evalArrayHasPrimitiveTypeParameter(clazz);
        if (arrayHasPrimitiveTypeParameter)
        {
            testRootNodeIsSet();
            ICspReferenceTypeTraits newNode = new CspArrayWithPrimitiveTypeParameterTypeTraits(clazz,
                dimensionReferenceFlags, dimensionFixedSizeFlags);
            commitNode(newNode);
        }
        else
        {
            ICspGenericTypeTraitsParameterAdder newNode = new CspArrayWithGenericTypeParameterTypeTraits(clazz,
                dimensionReferenceFlags, dimensionFixedSizeFlags);
            commitGenericTypeNode(newNode);
        }
        return this;
    }

    @Override
    public ICspGenericTypeTraitsBuilder addMultiLevelPointer(Class<?> clazz)
    {
        if (!clazz.isArray())
        {
            throw new IllegalArgumentException(
                clazz.getName() + " is not an array, but only arrays can substitute for " + "pointers!");
        }
        testStateNotDone();
        boolean arrayHasPrimitiveTypeParameter = evalArrayHasPrimitiveTypeParameter(clazz);
        int arrayDimensionsNumber = evalArrayDimensionsNumber(clazz);
        if (arrayHasPrimitiveTypeParameter)
        {
            testRootNodeIsSet();
            ICspReferenceTypeTraits newNode = new CspMultiLevelPointerWithPrimitiveTypeParameterTypeTraits(clazz,
                arrayDimensionsNumber);
            commitNode(newNode);
        }
        else
        {
            ICspGenericTypeTraitsParameterAdder newNode
                = new CspMultiLevelPointerWithGenericTypeParameterTypeTraits(clazz, arrayDimensionsNumber);
            commitGenericTypeNode(newNode);
        }
        return this;
    }

    @Override
    public ICspGenericTypeTraits build()
    {
        if (!cspGenericTypeTraitsParameterAdderStack.empty() || rootCspGenericTypeTraits == null)
        {
            throw new IllegalStateException("Cannot build csp type traits because current configuration is not valid!");
        }
        return rootCspGenericTypeTraits;
    }

    private void setRootNodeIfNotSet(ICspGenericTypeTraits newNode)
    {
        if (rootCspGenericTypeTraits == null)
        {
            rootCspGenericTypeTraits = newNode;
        }
    }

    private void commitGenericTypeNode(ICspGenericTypeTraitsParameterAdder newNode)
    {
        setRootNodeIfNotSet(newNode);
        commitNode(newNode);
        cspGenericTypeTraitsParameterAdderStack.push(newNode);
    }

    private void commitNode(ICspReferenceTypeTraits newNode)
    {
        if (!cspGenericTypeTraitsParameterAdderStack.empty())
        {
            ICspGenericTypeTraitsParameterAdder cspGenericTypeTraitsParameterAdder =
                cspGenericTypeTraitsParameterAdderStack.peek();
            if (cspGenericTypeTraitsParameterAdder.addGenericTypeTraitsParameter(newNode))
            {
                cspGenericTypeTraitsParameterAdderStack.pop();
            }
        }
    }

    private void testStateNotDone()
    {
        if (cspGenericTypeTraitsParameterAdderStack.empty() && rootCspGenericTypeTraits != null)
        {
            throw new IllegalStateException("Csp type traits already committed!");
        }
    }

    private void testRootNodeIsSet()
    {
        if (rootCspGenericTypeTraits == null)
        {
            throw new IllegalArgumentException("No root node added! Only generic type or array with reference element "
                                                   + "type can be root node!");
        }
    }

    private static boolean evalArrayHasPrimitiveTypeParameter(Class<?> clazz)
    {
        Class<?> arrayType = clazz;
        do
        {
            arrayType = arrayType.getComponentType();
        } while (arrayType.isArray());
        return arrayType.isPrimitive();
    }

    private static int evalArrayDimensionsNumber(Class<?> arrayClazz)
    {
        Class<?> arrayType = arrayClazz;
        int dimensions = 0;
        do
        {
            ++dimensions;
            arrayType = arrayType.getComponentType();
        } while (arrayType.isArray());
        return dimensions;
    }
}
