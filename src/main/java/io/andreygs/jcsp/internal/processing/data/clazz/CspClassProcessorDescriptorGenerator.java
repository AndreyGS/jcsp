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

package io.andreygs.jcsp.internal.processing.data.clazz;

import io.andreygs.jcsp.internal.processing.data.clazz.factory.ICspClassProcessorDescriptorFactory;
import io.andreygs.jcsp.internal.processing.data.type.ITypeVariableDescriptorGenerator;
import io.andreygs.jcsp.internal.processing.data.type.ITypeVariableDescriptor;

import java.lang.reflect.TypeVariable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Generator of class processor descriptor.
 * <p>
 * Can generate descriptor for any class processor and class pairs except if class is an array.
 * <p>
 * Uses {@link ICspClassProcessorDescriptorFactory} to create {@link ICspClassProcessorDescriptor} instance.
 * Also uses {@link ITypeVariableDescriptorGenerator} to generate {@link ITypeVariableDescriptor} instances if class
 * is generic.
 */
public class CspClassProcessorDescriptorGenerator implements ICspClassProcessorDescriptorGenerator
{
    private final ICspClassProcessorDescriptorFactory cspClassProcessorDescriptorFactory;
    private final ITypeVariableDescriptorGenerator typeVariableDescriptorGenerator;

    public CspClassProcessorDescriptorGenerator(ICspClassProcessorDescriptorFactory cspClassProcessorDescriptorFactory,
        ITypeVariableDescriptorGenerator typeVariableDescriptorGenerator)
    {
        this.cspClassProcessorDescriptorFactory = Objects.requireNonNull(cspClassProcessorDescriptorFactory);
        this.typeVariableDescriptorGenerator = Objects.requireNonNull(typeVariableDescriptorGenerator);
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException if clazz is an array or if {@link ITypeVariableDescriptorGenerator} throws it.
     */
    @Override
    public <P> ICspClassProcessorDescriptor<P> generate(P classProcessor, Class<?> clazz)
    {
        if (clazz.isArray())
        {
            throw new IllegalArgumentException(Messages.CspClassProcessorDescriptorGenerator_Arrays_not_supported);
        }
        TypeVariable<? extends Class<?>>[] typeVariables = clazz.getTypeParameters();
        if (typeVariables.length == 0)
        {
            return cspClassProcessorDescriptorFactory.create(Objects.requireNonNull(classProcessor), Set.of());
        }
        Set<ITypeVariableDescriptor> typeVariableDescriptors = new HashSet<>();
        for (TypeVariable<? extends Class<?>> typeVariable : typeVariables)
        {
            ITypeVariableDescriptor typeVariableDescriptor =
                typeVariableDescriptorGenerator.generate(typeVariable);
            typeVariableDescriptors.add(typeVariableDescriptor);
        }
        return cspClassProcessorDescriptorFactory.create(Objects.requireNonNull(classProcessor),
            typeVariableDescriptors);
    }
}
