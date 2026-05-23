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

package io.andreygs.jcsp.internal.processing.data.type;

import io.andreygs.jcsp.internal.processing.data.type.model.ITypeBoundsDescriptor;
import io.andreygs.jcsp.internal.processing.data.type.model.factory.ITypeBoundsDescriptorFactory;

import java.lang.reflect.AnnotatedWildcardType;
import java.lang.reflect.Type;
import java.util.Objects;

/**
 * TODO: place description here
 */
public class TypeBoundsDescriptorGenerator implements ITypeBoundsDescriptorGenerator
{
    private final ITypeBoundsDescriptorFactory typeBoundsDescriptorFactory;

    public TypeBoundsDescriptorGenerator(ITypeBoundsDescriptorFactory typeBoundsDescriptorFactory)
    {
        this.typeBoundsDescriptorFactory = Objects.requireNonNull(typeBoundsDescriptorFactory);
    }

    @Override
    public ITypeBoundsDescriptor getTypeBoundsDescriptor(Type[] typeBounds)
    {
        return null;
    }

    @Override
    public ITypeBoundsDescriptor getTypeBoundsDescriptor(AnnotatedWildcardType annotatedWildcardType)
    {
        return null;
    }
}
