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

import io.andreygs.jcsp.internal.processing.data.type.dto.ITypeBoundsDescriptor;
import io.andreygs.jcsp.internal.processing.data.type.model.TypeBoundKind;
import io.andreygs.jcsp.internal.processing.data.type.dto.factory.ITypeBoundsDescriptorFactory;

import java.lang.reflect.AnnotatedWildcardType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

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
    public Optional<ITypeBoundsDescriptor> resolveTypeBoundsDescriptor(TypeVariable<? extends Class<?>> typeVariable)
    {
        Type[] types = typeVariable.getBounds();
        if (types.length == 0)
        {
            return Optional.empty();
        }
        TypeBoundKind typeBoundKind = TypeBoundKind.UPPER_BOUND;
        Optional<String> boundTypeName = resolveTypeVariableName(types[0]);
        if (boundTypeName.isPresent())
        {
            return
                Optional.of(typeBoundsDescriptorFactory.createTypeBoundsDescriptor(typeBoundKind, boundTypeName.get()));
        }
        Set<Class<?>> boundClasses = new HashSet<>();
        for (Type type : types)
        {
            Class<?> clazz = requireClass(type);
            boundClasses.add(clazz);
        }
        return Optional.of(typeBoundsDescriptorFactory.createTypeBoundsDescriptor(typeBoundKind, boundClasses));
    }

    @Override
    public Optional<ITypeBoundsDescriptor> resolveTypeBoundsDescriptor(AnnotatedWildcardType annotatedWildcardType)
    {
        return null;
    }

    private Optional<String> resolveTypeVariableName(Type type)
    {
        if (type instanceof TypeVariable<?> typeVariable)
        {
            return Optional.of(typeVariable.getName());
        }
        return Optional.empty();
    }

    private Class<?> requireClass(Type type)
    {
        if (type instanceof Class<?>)
        {
            return (Class<?>)type;
        }
        throw new IllegalArgumentException("");
    }
}
