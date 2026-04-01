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

import io.andreygs.jcsp.base.processing.ICspDataGeneralSerializationProcessor;
import io.andreygs.jcsp.base.processing.ICspDataProcessorRegistry;
import io.andreygs.jcsp.base.processing.ICspDataSerializationProcessor;
import io.andreygs.jcsp.base.processing.buffer.internal.ICspSerializationBuffer;
import io.andreygs.jcsp.base.processing.session.ICspDataSerializationSession;
import io.andreygs.jcsp.base.types.CspCommonFlag;
import io.andreygs.jcsp.base.types.CspDataFlag;
import io.andreygs.jcsp.base.types.CspProtocolVersion;
import io.andreygs.jcsp.base.types.ICspInterfaceVersion;
import io.andreygs.jcsp.base.types.ICspVersionable;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;

/**
 * Factory for creation CSP message serialization sessions.
 */
public interface ICspSerializationSessionFactory
{
    ICspDataSerializationSession createCspDataSerializationSession(
        ICspSerializationBuffer cspSerializationBuffer,
        CspProtocolVersion cspProtocolVersion,
        Set<CspCommonFlag> cspCommonFlags,
        ICspDataGeneralSerializationProcessor cspDataGeneralSerializationProcessor,
        ICspDataProcessorRegistry<ICspDataSerializationProcessor> cspSerializationProcessorRegistrar,
        @Nullable Map<Object, Integer> referenceMap,
        ICspVersionable struct,
        Class<?> structClazz,
        ICspInterfaceVersion cspInterfaceVersion,
        Set<CspDataFlag> cspDataFlags);
}
