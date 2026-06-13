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

import io.andreygs.jcsp.internal.processing.data.type.model.TypeBoundKind;
import io.andreygs.jcsp.internal.processing.data.type.factory.ITypeBoundsDescriptorFactory;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Generator of generic parameter type bounds descriptor.
 * <p>
 * Cannot generate descriptors for bounds represented with generic classes.
 * <p>
 * Uses {@link ITypeBoundsDescriptorFactory} to create {@link ITypeBoundsDescriptor} instance.
 */
public class TypeBoundsDescriptorGenerator implements ITypeBoundsDescriptorGenerator
{
    private final ITypeBoundsDescriptorFactory typeBoundsDescriptorFactory;

    public TypeBoundsDescriptorGenerator(ITypeBoundsDescriptorFactory typeBoundsDescriptorFactory)
    {
        this.typeBoundsDescriptorFactory = Objects.requireNonNull(typeBoundsDescriptorFactory);
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException if bound type is generic class or if bound category is unknown.
     */
    @Override
    public Optional<ITypeBoundsDescriptor> generate(TypeVariable<? extends Class<?>> typeVariable)
    {
        return generateInternal(TypeBoundKind.UPPER_BOUND, typeVariable.getBounds());
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException if bound type is generic class or array or if bound category is unknown.
     */
    @Override
    public Optional<ITypeBoundsDescriptor> generate(WildcardType wildcardType)
    {
        Type[] lowerBounds = wildcardType.getLowerBounds();
        if (lowerBounds.length == 1)
        {
            return generateInternal(TypeBoundKind.LOWER_BOUND, lowerBounds);
        }
        Type[] upperBounds = wildcardType.getUpperBounds();
        if (upperBounds.length == 1)
        {
            return generateInternal(TypeBoundKind.UPPER_BOUND, upperBounds);
        }
        throw new IllegalArgumentException(Messages.TypeBoundsDescriptorGenerator_Unknown_bound_type_category);
    }

    private Optional<ITypeBoundsDescriptor> generateInternal(TypeBoundKind typeBoundKind, Type[] bounds)
    {
        Optional<String> boundTypeName = resolveTypeVariableName(bounds[0]);
        if (boundTypeName.isPresent())
        {
            return Optional.of(typeBoundsDescriptorFactory.create(typeBoundKind, boundTypeName.get()));
        }
        Set<Class<?>> boundClasses = new HashSet<>();
        for (Type bound : bounds)
        {
            Class<?> clazz = requireClass(bound);
            if (clazz != Object.class || typeBoundKind == TypeBoundKind.LOWER_BOUND)
            {
                boundClasses.add(clazz);
            }
        }
        if (boundClasses.isEmpty())
        {
            return Optional.empty();
        }
        return Optional.of(typeBoundsDescriptorFactory.create(typeBoundKind, boundClasses));
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
        if (type instanceof Class<?> clazz)
        {
            if (clazz.getTypeParameters().length > 0 || clazz.isArray())
            {
                throw new IllegalArgumentException(Messages.TypeBoundsDescriptorGenerator_Bound_not_supported);
            }
            return clazz;
        }
        throw new IllegalArgumentException(Messages.TypeBoundsDescriptorGenerator_Unknown_bound_type_category);
    }
}
