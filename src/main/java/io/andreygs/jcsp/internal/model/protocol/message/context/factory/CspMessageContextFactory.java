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

package io.andreygs.jcsp.internal.model.protocol.message.context.factory;

import io.andreygs.jcsp.api.model.protocol.CspProtocolVersion;
import io.andreygs.jcsp.api.model.protocol.ICspInterfaceVersion;
import io.andreygs.jcsp.api.model.protocol.message.context.ICspDataMessageContextExtension;
import io.andreygs.jcsp.api.model.protocol.message.context.ICspMessageContext;
import io.andreygs.jcsp.api.model.protocol.message.context.factory.ICspMessageContextFactory;
import io.andreygs.jcsp.internal.model.protocol.message.context.CspDataMessageContextExtension;
import io.andreygs.jcsp.internal.model.protocol.message.context.CspMessageContext;

/**
 * TODO: place description here
 */
public final class CspMessageContextFactory
    implements ICspMessageContextFactory
{
    @Override
    public ICspMessageContext createCspMessageContext(CspProtocolVersion cspProtocolVersion, boolean bitness32,
        boolean bigEndian, boolean endiannessDifference)
    {
        return new CspMessageContext(cspProtocolVersion, bitness32, bigEndian, endiannessDifference);
    }

    @Override
    public ICspDataMessageContextExtension createCspDataMessageContextExtension(Class<?> structClazz,
        ICspInterfaceVersion cspInterfaceVersion, boolean alignmentMayBeNotEqual, boolean sizeOfIntegersMayBeNotEqual,
        boolean allowUnmanagedPointers, boolean checkRecursivePointers,
        boolean simplyAssignableTagsOptimizationsAreTurnedOff,
        boolean checkRecursivePointersWhileMaintainingLinkStructure)
    {
        return new CspDataMessageContextExtension(structClazz, cspInterfaceVersion, alignmentMayBeNotEqual,
            sizeOfIntegersMayBeNotEqual, allowUnmanagedPointers, checkRecursivePointers,
            simplyAssignableTagsOptimizationsAreTurnedOff, checkRecursivePointersWhileMaintainingLinkStructure);
    }
}
