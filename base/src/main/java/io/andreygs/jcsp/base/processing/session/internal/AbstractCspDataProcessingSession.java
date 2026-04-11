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

package io.andreygs.jcsp.base.processing.session.internal;

import io.andreygs.jcsp.base.processing.ICspDataProcessorRegistry;
import io.andreygs.jcsp.base.processing.buffer.internal.ICspBuffer;
import io.andreygs.jcsp.base.processing.traits.ICspReferenceTypeTraits;
import io.andreygs.jcsp.base.types.CspCommonFlag;
import io.andreygs.jcsp.base.types.CspDataFlag;
import io.andreygs.jcsp.base.types.CspMessageType;
import io.andreygs.jcsp.base.types.CspProtocolVersion;
import io.andreygs.jcsp.base.types.ICspInterfaceVersion;
import io.andreygs.jcsp.base.types.ICspVersionable;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * TODO: place description here
 */
abstract class AbstractCspDataProcessingSession<B extends ICspBuffer, G, P, K, V>
    extends AbstractCspCommonProcessingSession<B>
    implements ICspDataProcessingSession<B, G, P, K, V>
{
    private final G cspGeneralProcessor;
    private final ICspDataProcessorRegistry<P> cspProcessorRegistrar;
    private @Nullable Stack<List<ICspReferenceTypeTraits>> typeTraits;
    private final @Nullable Map<K, V> referenceMap;
    private final ICspVersionable struct;
    private final Class<?> structClazz;
    private final ICspInterfaceVersion cspInterfaceVersion;
    private final boolean alignmentMayBeNotEqual;
    private final boolean sizeOfIntegersMayBeNotEqual;
    private final boolean allowUnmanagedPointers;
    private final boolean checkRecursivePointers;
    private final boolean simplyAssignableTagsOptimizationsAreTurnedOff;
    private final boolean checkRecursivePointersWhileMaintainingLinkStructure;

    public AbstractCspDataProcessingSession(B cspBuffer,
                                            CspProtocolVersion cspProtocolVersion,
                                            Set<CspCommonFlag> cspCommonFlags,
                                            G cspGeneralProcessor,
                                            ICspDataProcessorRegistry<P> cspProcessorRegistrar,
                                            @Nullable Map<K, V> referenceMap,
                                            ICspVersionable struct,
                                            Class<?> structClazz,
                                            ICspInterfaceVersion cspInterfaceVersion,
                                            Set<CspDataFlag> cspDataFlags)
    {
        super(cspBuffer, cspProtocolVersion, cspCommonFlags);
        this.cspGeneralProcessor = cspGeneralProcessor;
        this.cspProcessorRegistrar = cspProcessorRegistrar;
        this.referenceMap = referenceMap;
        this.struct = struct;
        this.structClazz = structClazz;
        this.cspInterfaceVersion = cspInterfaceVersion;
        this.alignmentMayBeNotEqual = cspDataFlags.contains(CspDataFlag.ALIGNMENT_MAY_BE_NOT_EQUAL);
        this.sizeOfIntegersMayBeNotEqual = cspDataFlags.contains(CspDataFlag.SIZE_OF_INTEGERS_MAY_BE_NOT_EQUAL);
        this.allowUnmanagedPointers = cspDataFlags.contains(CspDataFlag.ALLOW_UNMANAGED_POINTERS);
        this.checkRecursivePointers = cspDataFlags.contains(CspDataFlag.CHECK_RECURSIVE_POINTERS);
        this.simplyAssignableTagsOptimizationsAreTurnedOff =
            cspDataFlags.contains(CspDataFlag.SIMPLY_ASSIGNABLE_TAGS_OPTIMIZATIONS_ARE_TURNED_OFF);
        this.checkRecursivePointersWhileMaintainingLinkStructure =
            cspDataFlags.contains(CspDataFlag.CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE);
    }

    @Override
    public G getCspDataGeneralProcessor()
    {
        return cspGeneralProcessor;
    }

    @Override
    public CspMessageType getCspMessageType()
    {
        return CspMessageType.DATA;
    }

    @Override
    public ICspDataProcessorRegistry<P> getCspProcessorRegistrar()
    {
        return cspProcessorRegistrar;
    }

    @Override
    public List<ICspReferenceTypeTraits> peekGenericTypeParameterTraits()
    {
        if (typeTraits == null)
        {
            throw new IllegalStateException("typeTraits are not initialized when they should already be!");
        }
        return typeTraits.peek();
    }

    @Override
    public Map<K, V> getReferenceMap()
    {
        if (referenceMap == null)
        {
            throw new IllegalStateException("getReferenceMap is called when it shouldn't be called or "
                                                + "reference map is not initialized when it should be initialized!");
        }
        return referenceMap;
    }

    @Override
    public ICspVersionable getStruct()
    {
        return struct;
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
    public Set<CspDataFlag> getCspDataFlags()
    {
        Set<CspDataFlag> cspDataFlags = EnumSet.noneOf(CspDataFlag.class);
        if (alignmentMayBeNotEqual)
        {
            cspDataFlags.add(CspDataFlag.ALIGNMENT_MAY_BE_NOT_EQUAL);
        }
        if (sizeOfIntegersMayBeNotEqual)
        {
            cspDataFlags.add(CspDataFlag.SIZE_OF_INTEGERS_MAY_BE_NOT_EQUAL);
        }
        if (allowUnmanagedPointers)
        {
            cspDataFlags.add(CspDataFlag.ALLOW_UNMANAGED_POINTERS);
        }
        if (checkRecursivePointers)
        {
            cspDataFlags.add(CspDataFlag.CHECK_RECURSIVE_POINTERS);
        }
        if (simplyAssignableTagsOptimizationsAreTurnedOff)
        {
            cspDataFlags.add(CspDataFlag.SIMPLY_ASSIGNABLE_TAGS_OPTIMIZATIONS_ARE_TURNED_OFF);
        }
        if (checkRecursivePointersWhileMaintainingLinkStructure)
        {
            cspDataFlags.add(CspDataFlag.CHECK_OF_RECURSIVE_POINTERS_WHILE_MAINTAINING_LINK_STRUCTURE);
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
