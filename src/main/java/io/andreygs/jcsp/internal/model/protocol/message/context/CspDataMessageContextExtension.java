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

package io.andreygs.jcsp.internal.model.protocol.message.context;

import io.andreygs.jcsp.api.model.protocol.ICspInterfaceVersion;
import io.andreygs.jcsp.api.model.protocol.message.context.ICspDataMessageContextExtension;

import java.util.Objects;

/**
 * TODO: place description here
 */
public final class CspDataMessageContextExtension
    implements ICspDataMessageContextExtension
{
    private final Class<?> structClazz;
    private final ICspInterfaceVersion cspInterfaceVersion;
    private final boolean alignmentMayBeNotEqual;
    private final boolean sizeOfIntegersMayBeNotEqual;
    private final boolean allowUnmanagedPointers;
    private final boolean checkRecursivePointers;
    private final boolean simplyAssignableTagsOptimizationsAreTurnedOff;
    private final boolean checkRecursivePointersWhileMaintainingLinkStructure;

    public CspDataMessageContextExtension(Class<?> structClazz, ICspInterfaceVersion cspInterfaceVersion,
        boolean alignmentMayBeNotEqual, boolean sizeOfIntegersMayBeNotEqual, boolean allowUnmanagedPointers,
        boolean checkRecursivePointers, boolean simplyAssignableTagsOptimizationsAreTurnedOff,
        boolean checkRecursivePointersWhileMaintainingLinkStructure)
    {
        this.structClazz = Objects.requireNonNull(structClazz);
        this.cspInterfaceVersion = Objects.requireNonNull(cspInterfaceVersion);
        this.alignmentMayBeNotEqual = alignmentMayBeNotEqual;
        this.sizeOfIntegersMayBeNotEqual = sizeOfIntegersMayBeNotEqual;
        this.allowUnmanagedPointers = allowUnmanagedPointers;
        this.checkRecursivePointers = checkRecursivePointers;
        this.simplyAssignableTagsOptimizationsAreTurnedOff = simplyAssignableTagsOptimizationsAreTurnedOff;
        this.checkRecursivePointersWhileMaintainingLinkStructure = checkRecursivePointersWhileMaintainingLinkStructure;
    }

    @Override
    public Class<?> getStructClazz()
    {
        return structClazz;
    }

    @Override
    public ICspInterfaceVersion getInterfaceVersion()
    {
        return cspInterfaceVersion;
    }

    @Override
    public boolean isAlignmentMayBeNotEqual()
    {
        return alignmentMayBeNotEqual;
    }

    @Override
    public boolean isSizeOfIntegersMayBeNotEqual()
    {
        return sizeOfIntegersMayBeNotEqual;
    }

    @Override
    public boolean isAllowUnmanagedPointers()
    {
        return allowUnmanagedPointers;
    }

    @Override
    public boolean isCheckRecursivePointers()
    {
        return checkRecursivePointers;
    }

    @Override
    public boolean isSimplyAssignableTagsOptimizationsAreTurnedOff()
    {
        return simplyAssignableTagsOptimizationsAreTurnedOff;
    }

    @Override
    public boolean isCheckRecursivePointersWhileMaintainingLinkStructure()
    {
        return checkRecursivePointersWhileMaintainingLinkStructure;
    }
}
