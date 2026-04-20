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

import io.andreygs.jcsp.base.message.ICspDataMessage;
import io.andreygs.jcsp.base.processing.ICspDataGeneralSerializationProcessor;
import io.andreygs.jcsp.base.processing.ICspDataProcessorRegistry;
import io.andreygs.jcsp.base.processing.ICspDataSerializationProcessor;
import io.andreygs.jcsp.base.processing.buffer.internal.ICspSerializationBuffer;
import io.andreygs.jcsp.base.processing.composite.ICspDataCompositeSerializationProcessor;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * The sole implementation of {@link ICspDataGeneralSerializationProcessor}.
 * <p>
 *
 */
final class CspDataGeneralSerializationProcessor implements ICspDataGeneralSerializationProcessor
{
    private final ICspSerializationBuffer cspSerializationBuffer;
    private final ICspDataProcessorRegistry<ICspDataSerializationProcessor<?>> cspProcessorRegistry;
    private final ICspDataProcessorGenerator<ICspDataSerializationProcessor> cspDataProcessorGenerator;
    private final ICspDataMessage cspDataMessage;
    private @Nullable Stack<List<ICspDataCompositeSerializationProcessor>> activeCompositeProcessors;
    private final @Nullable Map<Object, Integer> referenceMap;

    CspDataGeneralSerializationProcessor(
        ICspSerializationBuffer cspSerializationBuffer,
        ICspDataProcessorRegistry<ICspDataSerializationProcessor<?>> cspProcessorRegistry,
        ICspDataProcessorGenerator<ICspDataSerializationProcessor> cspDataProcessorGenerator,
        ICspDataMessage cspDataMessage)
    {
        this.cspSerializationBuffer = cspSerializationBuffer;
        this.cspProcessorRegistry = cspProcessorRegistry;
        this.cspDataProcessorGenerator = cspDataProcessorGenerator;
        this.cspDataMessage = cspDataMessage;
        referenceMap = cspDataMessage.isCheckRecursivePointers() ? new HashMap<Object, Integer>() : null;
    }

    @Override
    public void serialize(boolean value)
    {
    }

    @Override
    public void serializeByte(byte value)
    {

    }

    @Override
    public void serializeShort(short value)
    {

    }

    @Override
    public void serializeInt(int value)
    {

    }

    @Override
    public void serializeLong(long value)
    {

    }

    @Override
    public void serializeChar(char value)
    {

    }

    @Override
    public void serializeFloat(float value)
    {

    }

    @Override
    public void serializeDouble(double value)
    {

    }

    @Override
    public void serialize(boolean[] value)
    {

    }

    @Override
    public void serialize(boolean @Nullable [] value, boolean reference, boolean fixedSize)
    {

    }

    @Override
    public void serialize(byte[] value)
    {

    }

    @Override
    public void serialize(byte @Nullable [] value, boolean reference, boolean fixedSize)
    {

    }

    @Override
    public void serialize(short[] value)
    {

    }

    @Override
    public void serialize(short @Nullable [] value, boolean reference, boolean fixedSize)
    {

    }

    @Override
    public void serialize(int[] value)
    {

    }

    @Override
    public void serialize(int @Nullable [] value, boolean reference, boolean fixedSize)
    {

    }

    @Override
    public void serialize(long[] value)
    {

    }

    @Override
    public void serialize(long @Nullable [] value, boolean reference, boolean fixedSize)
    {

    }

    @Override
    public void serialize(char[] value)
    {

    }

    @Override
    public void serialize(char @Nullable [] value, boolean reference, boolean fixedSize)
    {

    }

    @Override
    public void serialize(float[] value)
    {

    }

    @Override
    public void serialize(float @Nullable [] value, boolean reference, boolean fixedSize)
    {

    }

    @Override
    public void serialize(double[] value)
    {

    }

    @Override
    public void serialize(double @Nullable [] value, boolean reference, boolean fixedSize)
    {

    }

    @Override
    public void serialize(String value, Charset charset)
    {

    }

    @Override
    public void serialize(@Nullable String value, boolean reference, Charset charset)
    {

    }

    @Override
    public <T> void serialize(T value, Class<?> clazz)
    {

    }

    @Override
    public <T> void serialize(T value, boolean reference, Class<?> clazz)
    {

    }

    @Override
    public <T> void serialize(T[] value, Class<?> elementClazz)
    {

    }

    @Override
    public <T> void serialize(@Nullable T @Nullable [] value, boolean reference, boolean fixedSize,
        boolean elementAsReference, Class<?> elementClazz)
    {

    }

    @Override
    public void serialize(String[] value, Charset charset)
    {

    }

    @Override
    public void serialize(@Nullable String @Nullable [] value, boolean reference, boolean fixedSize,
        boolean elementAsReference, Charset charset)
    {

    }

    @Override
    public <T> void serialize(Collection<T> value, Class<?> elementClazz)
    {

    }

    @Override
    public <T> void serialize(@Nullable Collection<@Nullable T> value, boolean reference, boolean valueAsReference, Class<?> elementClazz)
    {

    }

    @Override
    public void serialize(Collection<String> value, Charset charset)
    {

    }

    @Override
    public void serialize(@Nullable Collection<@Nullable String> value, boolean reference, boolean valueAsReference, Charset charset)
    {

    }

    @Override
    public <K, V> void serialize(Map<K, V> value, Class<?> keyClazz, Class<?> valueClazz)
    {

    }

    @Override
    public <K, V> void serialize(@Nullable Map<@Nullable K,@Nullable V> value, boolean reference, boolean keyAsReference, boolean valueAsRefence,
                                 Class<?> keyClazz, Class<?> valueClazz)
    {

    }

    @Override
    public <K> void serialize(Map<K, String> value, Class<?> keyClazz, Charset valueCharset)
    {

    }

    @Override
    public <K> void serialize(@Nullable Map<@Nullable K, @Nullable String> value, boolean reference, boolean keyAsReference, boolean valueAsRefence,
                              Class<?> keyClazz, Charset valueCharset)
    {

    }

    @Override
    public <V> void serialize(Map<String, V> value, Charset keyCharset, Class<?> valueClazz)
    {

    }

    @Override
    public <V> void serialize(@Nullable Map<@Nullable String, @Nullable V> value, boolean reference, boolean keyAsReference, boolean valueAsRefence,
                              Charset keyCharset, Class<?> valueClazz)
    {

    }

    @Override
    public void serialize(Map<String, String> value, Charset keyCharset, Charset valueCharset)
    {

    }

    @Override
    public void serialize(@Nullable Map<@Nullable String, @Nullable String> value, boolean reference,
        boolean keyAsReference, boolean valueAsRefence, Charset keyCharset, Charset valueCharset)
    {

    }

    @Override
    public void serializeComposite(@Nullable Object value, boolean reference, Class<?> clazz,
        List<? extends ICspDataCompositeSerializationProcessor> genericTypeParameterProcessors)
    {

    }

    @Override
    public <T> void serializeComposite(T @Nullable [] value, boolean reference, boolean fixedSize,
        ICspDataCompositeSerializationProcessor componentProcessor)
    {

    }

    @Override
    public <T> void serializeComposite(@Nullable Collection<T> value, boolean reference,
        ICspDataCompositeSerializationProcessor elementProcessor)
    {

    }

    @Override
    public <K, V> void serializeComposite(@Nullable Map<K, V> value, boolean reference,
        ICspDataCompositeSerializationProcessor keyProcessor, ICspDataCompositeSerializationProcessor valueProcessor)
    {

    }

    @Override
    public List<ICspDataCompositeSerializationProcessor> peekCompositeProcessors()
    {
        if (activeCompositeProcessors == null || activeCompositeProcessors.empty())
        {
            throw new IllegalStateException("typeTraits are not present when they should be!");
        }
        return Collections.unmodifiableList(activeCompositeProcessors.peek());
    }
}
