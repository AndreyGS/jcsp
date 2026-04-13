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

package io.andreygs.jcsp.base.processing.composite.internal;

import io.andreygs.jcsp.base.processing.composite.ICspDataCompositeProcessorBuilder;

import org.jetbrains.annotations.Nullable;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Stack;

/**
 * TODO: place description here
 */
final class CspDataCompositeProcessorBuilder<P, H extends ICspDataCompositeSubProcessorHolder<P>>
    implements ICspDataCompositeProcessorBuilder<P>
{
    private final ICspDataCompositeProcessorFactory<P, H> processorFactory;
    private final Stack<H> subProcessHolders = new Stack<>();
    private @Nullable P rootCompositeProcessor;

    CspDataCompositeProcessorBuilder(ICspDataCompositeProcessorFactory<P, H> processorFactory)
    {
        this.processorFactory = processorFactory;
    }

    @Override
    public ICspDataCompositeProcessorBuilder<P> addCollection(boolean reference)
    {
        testStateNotDone();
        H newNode = processorFactory.createCollectionProcessor(reference);
        commitNode(newNode);
        return this;
    }

    @Override
    public ICspDataCompositeProcessorBuilder<P> addMap(boolean reference)
    {
        testStateNotDone();
        H newNode = processorFactory.createMapProcessor(reference);
        commitNode(newNode);
        return this;
    }

    @Override
    public ICspDataCompositeProcessorBuilder<P> addReference(boolean reference, Class<?> clazz)
    {
        if (clazz.getTypeParameters().length > 0)
        {
            throw new IllegalArgumentException("Generics should be added using addGeneric() method!");
        }
        if (clazz.isArray())
        {
            throw new IllegalArgumentException(
                clazz.getName() + " is an array and it should be added using addArray() or addMultiLevelPointer() " +
                    "method!");
        }
        if (clazz == String.class)
        {
            throw new IllegalArgumentException("String class should be added using addString() method!");
        }
        testStateNotDone();
        P newNode = processorFactory.createRandomObjectProcessor(reference, clazz);
        commitNode(newNode);
        return this;
    }

    @Override
    public ICspDataCompositeProcessorBuilder<P> addString(boolean reference, Charset charset)
    {
        testStateNotDone();
        P newNode = processorFactory.createStringProcessor(reference, charset);
        commitNode(newNode);
        return this;
    }

    @Override
    public ICspDataCompositeProcessorBuilder<P> addGeneric(boolean reference, Class<?> clazz)
    {
        if (clazz.getTypeParameters().length < 1)
        {
            throw new IllegalArgumentException(
                clazz.getName() + " is not generic and it should be added using another " + "methods!.");
        }
        H newNode = processorFactory.createGenericProcessor(reference, clazz, clazz.getTypeParameters().length);
        commitNode(newNode);
        return this;
    }

    @Override
    public ICspDataCompositeProcessorBuilder<P> addArray(Class<?> clazz, List<Boolean> dimensionReferenceFlags,
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
        Class<?> arrayType = clazz;
        for (int i = 0; arrayType.isArray(); arrayType = arrayType.getComponentType(), ++i)
        {
            if (arrayType.isPrimitive())
            {
                P newNode = processorFactory.createArrayWithPrimitiveComponentProcessor(
                        dimensionReferenceFlags.get(i),  dimensionFixedSizeFlags.get(i), arrayType);
                commitNode(newNode);
            }
            else
            {
                H newNode = processorFactory.createArrayProcessor(
                    dimensionReferenceFlags.get(i),  dimensionFixedSizeFlags.get(i));
                commitNode(newNode);
            }
        }
        return this;
    }

    @Override
    public ICspDataCompositeProcessorBuilder<P> addMultiLevelPointer(Class<?> clazz)
    {
        if (!clazz.isArray())
        {
            throw new IllegalArgumentException(
                clazz.getName() + " is not an array, but only arrays can substitute for " + "pointers!");
        }
        testStateNotDone();
        Class<?> arrayType = clazz;
        for (int i = 0; arrayType.isArray(); arrayType = arrayType.getComponentType(), ++i)
        {
            if (arrayType.isPrimitive())
            {
                P newNode = processorFactory.createArrayWithPrimitiveComponentProcessor(true,  true, arrayType);
                commitNode(newNode);
            }
            else
            {
                H newNode = processorFactory.createArrayProcessor(true,  true);
                commitNode(newNode);
            }
        }
        return this;
    }

    @Override
    public ICspDataCompositeProcessorBuilder<P> addExistingProcessor(P processor)
    {
        commitNode(processor);
        return this;
    }

    @Override
    public P build()
    {
        if (!subProcessHolders.empty() || rootCompositeProcessor == null)
        {
            throw new IllegalStateException("Cannot build csp type traits because current configuration is not valid!");
        }
        return rootCompositeProcessor;
    }

    private void commitNode(H newNode)
    {
        commitNode(newNode.getThisProcessor());
        subProcessHolders.push(newNode);
    }

    private void commitNode(P newNode)
    {
        if (rootCompositeProcessor == null)
        {
            rootCompositeProcessor = newNode;
        }
        if (!subProcessHolders.empty())
        {
            H subProcessorHolder = subProcessHolders.peek();
            if (subProcessorHolder.addSubProcessor(newNode))
            {
                subProcessHolders.pop();
            }
        }
    }

    private void testStateNotDone()
    {
        if (subProcessHolders.empty() && rootCompositeProcessor != null)
        {
            throw new IllegalStateException("Composite processor already in complete state and cannot be extended by "
                                                + "any part!");
        }
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
