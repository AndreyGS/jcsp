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

import org.jetbrains.annotations.Nullable;

/**
 * TODO: place description here
 */
public class CspException extends Exception
{
    private final CspStatus cspStatus;

    private CspException(CspStatus cspStatus, @Nullable Throwable cause)
    {
        super(cspStatus.toString(), cause);
        this.cspStatus = cspStatus;
    }

    private CspException(CspStatus cspStatus, String additionalInfo, @Nullable Throwable cause)
    {
        super(cspStatus.toString() + ": " + additionalInfo, cause);
        this.cspStatus = cspStatus;
    }

    public CspStatus getCspStatus()
    {
        return cspStatus;
    }

    /**
     * Creates an exception.
     *
     * @param status {@link  CspStatus#isErrorStatus() Error CSP status}.
     * @return created exception.
     * @throws IllegalArgumentException when status passed is not error status.
     */
    public static CspException createCspException(CspStatus status)
    {
        if (!status.isErrorStatus())
        {
            throw new IllegalArgumentException("CSP Status is not error status");
        }
        return new CspException(status, null);
    }

    /**
     * Creates an exception.
     *
     * @param status {@link  CspStatus#isErrorStatus() Error CSP status}.
     * @param additionalInfo Additional info about an error.
     * @return created exception.
     * @throws IllegalArgumentException when status passed is not error status.
     */
    public static CspException createCspException(CspStatus status, String additionalInfo)
    {
        if (!status.isErrorStatus())
        {
            throw new IllegalArgumentException("CSP Status is not error status");
        }
        return new CspException(status, additionalInfo, null);
    }

    /**
     * Creates an exception.
     *
     * @param status {@link  CspStatus#isErrorStatus() Error CSP status}.
     * @param additionalInfo Additional info about an error.
     * @param cause Exception that was cause of an error.
     * @return created exception.
     * @throws IllegalArgumentException when status passed is not error status.
     */
    public static CspException createCspException(CspStatus status, String additionalInfo, Throwable cause)
    {
        if (!status.isErrorStatus())
        {
            throw new IllegalArgumentException("CSP Status is not error status");
        }
        return new CspException(status, additionalInfo, cause);
    }
}
