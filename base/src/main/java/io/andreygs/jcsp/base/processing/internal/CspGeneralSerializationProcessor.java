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

package io.andreygs.jcsp.base.processing.internal;

import io.andreygs.jcsp.base.processing.ICspGeneralSerializationProcessor;
import io.andreygs.jcsp.base.processing.state.internal.ICspDataSerializationState;
import io.andreygs.jcsp.base.processing.typetraits.ICspReferenceTypeTraits;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.Charset;

/**
 * The sole implementation of {@link ICspGeneralSerializationProcessor}.
 */
public final class CspGeneralSerializationProcessor implements ICspGeneralSerializationProcessor
{

    @Override
    public void serialize(boolean value, Object state)
    {

    }

    @Override
    public void serializeByte(byte value, Object state)
    {

    }

    @Override
    public void serializeShort(short value, Object state)
    {

    }

    @Override
    public void serializeInt(int value, Object state)
    {

    }

    @Override
    public void serializeLong(long value, Object state)
    {

    }

    @Override
    public void serializeChar(char value, Object state)
    {

    }

    @Override
    public void serializeFloat(float value, Object state)
    {

    }

    @Override
    public void serializeDouble(double value, Object state)
    {

    }

    @Override
    public void serialize(boolean[] value, Object state)
    {

    }

    @Override
    public void serialize(boolean @Nullable [] value, boolean asReference, boolean fixedSize, Object state)
    {

    }

    @Override
    public void serialize(byte[] value, Object state)
    {

    }

    @Override
    public void serialize(byte @Nullable [] value, boolean asReference, boolean fixedSize, Object state)
    {

    }

    @Override
    public void serialize(short[] value, Object state)
    {

    }

    @Override
    public void serialize(short @Nullable [] value, boolean asReference, boolean fixedSize, Object state)
    {

    }

    @Override
    public void serialize(int[] value, Object state)
    {

    }

    @Override
    public void serialize(int @Nullable [] value, boolean asReference, boolean fixedSize, Object state)
    {

    }

    @Override
    public void serialize(long[] value, Object state)
    {

    }

    @Override
    public void serialize(long @Nullable [] value, boolean asReference, boolean fixedSize, Object state)
    {

    }

    @Override
    public void serialize(char[] value, Object state)
    {

    }

    @Override
    public void serialize(char @Nullable [] value, boolean asReference, boolean fixedSize, Object state)
    {

    }

    @Override
    public void serialize(float[] value, Object state)
    {

    }

    @Override
    public void serialize(float @Nullable [] value, boolean asReference, boolean fixedSize, Object state)
    {

    }

    @Override
    public void serialize(double[] value, Object state)
    {

    }

    @Override
    public void serialize(double @Nullable [] value, boolean asReference, boolean fixedSize, Object state)
    {

    }

    @Override
    public void serialize(String value, Charset charset, Object state)
    {

    }

    @Override
    public void serialize(@Nullable String value, boolean asReference, Charset charset, Object state)
    {

    }

    @Override
    public void serialize(Object value, Class<?> clazz, Object state)
    {

    }

    @Override
    public void serialize(@Nullable Object value, ICspReferenceTypeTraits cspObjectTypeTraits, Object state)
    {

    }
}
