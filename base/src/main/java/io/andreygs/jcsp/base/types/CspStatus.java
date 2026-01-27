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
 * CSP Status according to CSP reference.
 */
public enum CspStatus
{
    /**
     * Operation is successes.
     */
    NO_ERROR(0, Messages.CspStatus_No_Error),

    /**
     * There was not enough memory to complete the operation.
     */
    NO_MEMORY(-1, Messages.CspStatus_No_Memory),

    /**
     * There is no procedure or server that can handle the request.
     */
    NO_SUCH_HANDLER(-10, Messages.CspStatus_No_Such_Handler),

    /**
     * Error in attempting to serialize a pointer when the CSP Data flag "Allow unmanaged pointers" is not set.
     */
    POINTER_WHEN_NO_ALLOW_UNMANAGED_POINTERS_SET(-23, Messages.CspStatus_Pointer_When_No_Allow_Unmanaged_Pointers_Set);

    private final int code;
    private final String message;

    CspStatus(int code, String message)
    {
        this.code = code;
        this.message = Messages.CspStatus_Type + ": " + message + " (" + code + ")";
    }

    public int getValue()
    {
        return code;
    }

    @Override
    public String toString()
    {
        return message;
    }

    public boolean isErrorStatus()
    {
        return code < 0;
    }
}
