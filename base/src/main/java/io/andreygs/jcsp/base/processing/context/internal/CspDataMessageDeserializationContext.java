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

package io.andreygs.jcsp.base.processing.context.internal;

import io.andreygs.jcsp.base.processing.ICspProcessorRegistrar;
import io.andreygs.jcsp.base.processing.ICspSerializationProcessor;
import io.andreygs.jcsp.base.processing.buffer.internal.ICspDeserializationBuffer;
import io.andreygs.jcsp.base.processing.context.ICspDataMessageDeserializationContext;
import io.andreygs.jcsp.base.types.CspCommonFlags;
import io.andreygs.jcsp.base.types.CspDataFlags;
import io.andreygs.jcsp.base.types.ICspInterfaceVersion;
import io.andreygs.jcsp.base.types.CspMessageType;
import io.andreygs.jcsp.base.types.CspProtocolVersion;

import java.util.HashSet;
import java.util.Set;

/**
 * TODO: place description here
 */
public final class CspDataMessageDeserializationContext extends AbstractCspMessageDeserializationContext
    implements ICspDataMessageDeserializationContext
{
    private final Class<?> structClazz;
    private final ICspInterfaceVersion cspInterfaceVersion;
    private final boolean alignmentMayBeNotEqual;
    private final boolean sizeOfIntegersMayBeNotEqual;
    private final boolean allowUnmanagedPointers;
    private final boolean checkRecursivePointers;
    private final boolean simplyAssignableTagsOptimizationsAreTurnedOff;
    private final boolean checkRecursivePointersWhileMaintainingLinkStructure;

    public CspDataMessageDeserializationContext(
        ICspProcessorRegistrar<ICspSerializationProcessor> cspDeserializationProcessorRegistrar,
        ICspDeserializationBuffer cspDeserializationBuffer,
        CspProtocolVersion cspProtocolVersion,
        Set<CspCommonFlags> cspCommonFlags,
        Class<?> structClazz, ICspInterfaceVersion cspInterfaceVersion,
        Set<CspDataFlags> cspDataFlags)
    {
        super(cspDeserializationProcessorRegistrar, cspDeserializationBuffer, cspProtocolVersion, cspCommonFlags);
        this.structClazz = structClazz;
        this.cspInterfaceVersion = cspInterfaceVersion;
        this.alignmentMayBeNotEqual = cspDataFlags.contains(CspDataFlags.ALIGNMENT_MAY_BE_NOT_EQUAL);
        this.sizeOfIntegersMayBeNotEqual = cspDataFlags.contains(CspDataFlags.SIZE_OF_INTEGERS_MAY_BE_NOT_EQUAL);
        this.allowUnmanagedPointers = cspDataFlags.contains(CspDataFlags.ALLOW_UNMANAGED_POINTERS);
        this.checkRecursivePointers = cspDataFlags.contains(CspDataFlags.CHECK_RECURSIVE_POINTERS);
        this.simplyAssignableTagsOptimizationsAreTurnedOff =
            cspDataFlags.contains(CspDataFlags.SIMPLY_ASSIGNABLE_TAGS_OPTIMIZATIONS_ARE_TURNED_OFF);
        this.checkRecursivePointersWhileMaintainingLinkStructure =
            cspDataFlags.contains(CspDataFlags.CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE);
    }

    @Override
    public CspMessageType getCspMessageType()
    {
        return CspMessageType.DATA;
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
    public Set<CspDataFlags> getCspDataFlags()
    {
        Set<CspDataFlags> cspDataFlags = new HashSet<>();

        if (alignmentMayBeNotEqual)
        {
            cspDataFlags.add(CspDataFlags.ALIGNMENT_MAY_BE_NOT_EQUAL);
        }
        if (sizeOfIntegersMayBeNotEqual)
        {
            cspDataFlags.add(CspDataFlags.SIZE_OF_INTEGERS_MAY_BE_NOT_EQUAL);
        }
        if (allowUnmanagedPointers)
        {
            cspDataFlags.add(CspDataFlags.ALLOW_UNMANAGED_POINTERS);
        }
        if (checkRecursivePointers)
        {
            cspDataFlags.add(CspDataFlags.CHECK_RECURSIVE_POINTERS);
        }
        if (simplyAssignableTagsOptimizationsAreTurnedOff)
        {
            cspDataFlags.add(CspDataFlags.SIMPLY_ASSIGNABLE_TAGS_OPTIMIZATIONS_ARE_TURNED_OFF);
        }
        if (checkRecursivePointersWhileMaintainingLinkStructure)
        {
            cspDataFlags.add(CspDataFlags.CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE);
        }

        return cspDataFlags;
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
