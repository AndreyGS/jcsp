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

package io.andreygs.jcsp.base.processing;

import java.nio.charset.Charset;

/**
 * TODO: place description here
 */
public interface ICspGeneralDeserializationProcessor
{
    boolean deserialize(Object state, boolean value);

    byte deserialize(Object state, byte value);

    short deserialize(Object state, short value);

    int deserialize(Object state, int value);

    long deserialize(Object state, long value);

    char deserialize(Object state, char value);

    float deserialize(Object state, float value);

    double deserialize(Object state, double value);

    void deserialize(Object state, byte[] value);

    void deserialize(Object state, short[] value);

    void deserialize(Object state, int[] value);

    void deserialize(Object state, long[] value);

    void deserialize(Object state, char[] value);

    void deserialize(Object state, float[] value);

    void deserialize(Object state, double[] value);

    void deserialize(Object state, Charset charset, String value);

    void deserialize(Object state, Object value);
}
