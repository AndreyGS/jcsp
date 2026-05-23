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

package io.andreygs.jcsp.api.model.buffer;

/**
 * The allocation type of the buffer.
 * <p>
 * You should consider that direct buffer may not have underlying array that can be retrieved. Non-direct (heap) buffer,
 * otherwise will always have underlying array, and it can be used in all cases safely, but it has at least one
 * additional copying when passed to any native function, including methods of sockets and streams.
 * <p>
 * So if you want to pass data by net buffer should be direct. But many cryptographic libraries, on the other hand, are
 * using byte[], and you must either use non-direct buffer or convert make additionally copy to byte[] from direct
 * buffer manually.
 */
public enum AllocationType
{
    /**
     * Buffer allocated in heap.
     */
    HEAP,

    /**
     * Buffer allocated in native memory.
     */
    DIRECT
}
