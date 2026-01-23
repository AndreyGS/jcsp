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

package io.andreygs.jcsp.base.types;

/**
 * Version of CSP compatible interface (CSP Interface).
 * <p>
 * As CSP states: it must be present in raw form of 4-octet length integer.
 * However, internal form can be any.
 * <p>
 * For example:
 * <ul>
 *     <li>1 octet - Major | 1 octet Minor | 2 octet Patch</li>
 *     <li>1 octet - Major | 1 octet Minor | 1 octet Patch | 1 octet Build</li>
 * </ul>
 */
public interface ICspInterfaceVersion
{
    /**
     * Gets raw version of CSP Interface value.
     *
     * @return raw version of CSP Interface value.
     */
    int getRawVersion();

    /**
     * Calculates positive value of octets from which CSP Version consists.
     * <p>
     * Because Java doesn't have unsigned integer CSP Version raw value can be negative,
     * but this is not what we want in some cases (in raw compare, for example).
     * So here we got CSP Version (in raw view) with positive, non-overflowed value.
     *
     * @param cspInterfaceVersion Instance which version needs to be processed.
     * @return CSP Version raw positive value.
     */
    static long calculatePositiveRawVersion(ICspInterfaceVersion cspInterfaceVersion)
    {
        return (long)cspInterfaceVersion.getRawVersion() & 0xffffffffL;
    }

    /**
     * Compares versions by their positive raw CSP Versions representations similar to compareTo() method.
     * <p>
     * It's only legal to compare instances of the same class.
     *
     * @param left Left comparing instance.
     * @param right Right comparing instance.
     * @return result of left value minus right value equation, where values presented as positive raw version representation.
     * If result > 0, left version is higher, if result == 0, versions are equal.
     * @throws IllegalArgumentException if instances has different classes.
     */
    static int compareVersions(ICspInterfaceVersion left, ICspInterfaceVersion right)
    {
        if (left.getClass() != right.getClass())
        {
            throw new IllegalArgumentException("Comparing ICspInterfaceVersion instances must have equal class!");
        }

        return (int)(calculatePositiveRawVersion(left) - calculatePositiveRawVersion(right));
    }
}
