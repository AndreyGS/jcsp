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

package io.andreygs.jcsp.internal.processing.data.type.dto;

import io.andreygs.jcsp.internal.processing.data.type.model.TypeBoundKind;
import io.andreygs.jcsp.internal.processing.data.type.model.TypeIdKind;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * TODO: place description here
 */
public class TypeBoundsDescriptor
    implements ITypeBoundsDescriptor
{
    private final TypeBoundKind boundTypeKind;
    private final TypeIdKind typeIdKind;
    private final Set<Class<?>> boundClasses;
    private final @Nullable String boundTypeVariableName;

    public TypeBoundsDescriptor(TypeBoundKind boundTypeKind, Set<Class<?>> boundClasses)
    {
        this.boundTypeKind = Objects.requireNonNull(boundTypeKind);
        this.typeIdKind = TypeIdKind.CLASS;
        this.boundClasses = Set.copyOf(boundClasses);
        this.boundTypeVariableName = null;
        if (this.boundClasses.isEmpty())
        {

        }
    }

    public TypeBoundsDescriptor(TypeBoundKind boundTypeKind, String boundTypeVariableName)
    {
        this.boundTypeKind = Objects.requireNonNull(boundTypeKind);
        this.typeIdKind = TypeIdKind.TYPE_VARIABLE_NAME;
        this.boundClasses = Set.of();
        this.boundTypeVariableName = boundTypeVariableName;
        if (this.boundTypeVariableName.isEmpty())
        {

        }
    }

    @Override
    public TypeBoundKind getTypeBoundKind()
    {
        return boundTypeKind;
    }

    @Override
    public TypeIdKind getTypeIdKind()
    {
        return typeIdKind;
    }

    @Override
    public Set<Class<?>> getBoundClasses()
    {
        return boundClasses;
    }

    @Override
    public Optional<String> getBoundTypeVariableName()
    {
        return Optional.ofNullable(boundTypeVariableName);
    }
}
