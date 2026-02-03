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

package io.andreygs.jcsp.base.processing.typetraits;

import io.andreygs.jcsp.base.processing.typetraits.annotations.CspAsReference;
import io.andreygs.jcsp.base.processing.typetraits.annotations.CspStringCharset;
import io.andreygs.jcsp.base.processing.typetraits.annotations.CspFixedSizeArray;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * Builder of csp type traits.
 * <p>
 * Example of use:
 * <p>
 * Suppose we have such declared type {@link CspAsReference @CspAsReference} {@link List}<
 * {@link Map}<{@link CspStringCharset @CspStringCharset(CspStringCharset.CharsetType.UTF_8)}
 * {@link String}, {@link Map}&lt;BmpImage, byte[]>> [] {@link CspFixedSizeArray @CspVariableSizeArray} []>.
 * <p>
 * builder.addNode({@link ICspCollectionTypeTraits}.class).setReference();<br>
 * builder.addNode({@link ICspArrayTypeTraits}.class);<br>
 * builder.addNode({@link ICspArrayTypeTraits}.class).setFixedSize();<br>
 * builder.addNode({@link ICspMapTypeTraits}.class);<br>
 * builder.addNode({@link ICspStringTypeTraits}.class).setCharset(CspStringCharset.CharsetType.UTF_8);<br>
 * builder.addNode({@link ICspMapTypeTraits}.class)<br>
 * builder.addNode({@link ICspObjectTypeTraits}.class).setProcessorClazz(Image.class);<br>
 * builder.addNode({@link ICspArrayTypeTraits}.class)<br>
 * builder.addNode({@link ICspTypeTraits}.class)
 * ICspReferenceTypeTraits cspReferenceTypeTraits = builder.build();
 * <p>
 * First, we add node List, which should be serialized as reference.
 * Next, we add a List element type, which is an array.
 * Next, we add the array element type, which is also an array, and set it (last array node) fixed size (according to
 * CSP Interface).
 * Next, we add the array element type, which is a Map.
 * Next, we add the Map key type, which is String, and set its Charset to UTF-8 (according to CSP Interface).
 * Next, we add the Map key type, which is BmpImage and set its processor class to Image (it must be registered
 * in respective serialization/deserialization CSP processor registrar).
 * Next, we add the second Map value type, which is an array.
 * Next, we add element type of previously added array as simply {@link ICspTypeTraits} to mark it as primitive.
 * Finally, we build complete type traits which will help to correctly process type according to CSP Interface.
 * <p>
 * Note, that after adding node all properties excluding those implementing {@link ICspReferenceTypeTraits} must
 * be set before next call to {@link #addNode(Class)}. Next call will start recursively element or key, or
 * value type node (value always going after key).
 * <p>
 * So, if root Map key, for example, itself a Map, then, after processing root Map node, next add node call will add
 * another Map node (which will be attached as a key of root map). Then two following add node calls will add and
 * attach key and value to the nested Map. Last add node will add value type traits of the root node. Of course, all
 * nodes in this example, except this two maps, are not containers or arrays.
 * <p>
 * Setting not applicable properties including adding node when not applicable, to currently processing node is
 * considered as an error.
 */
public interface ICspReferenceTypeTraitsBuilder
{
    ICspReferenceTypeTraitsBuilder addNode(Class<? extends ICspTypeTraits> traitsClazz);

    ICspReferenceTypeTraitsBuilder setReference();

    ICspReferenceTypeTraitsBuilder setProcessorClazz(Class<?> processorClazz);

    ICspReferenceTypeTraitsBuilder setFixedSize();

    ICspReferenceTypeTraitsBuilder setCharset(Charset charset);

    ICspReferenceTypeTraits build();
}
