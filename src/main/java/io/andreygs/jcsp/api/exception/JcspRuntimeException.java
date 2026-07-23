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

package io.andreygs.jcsp.api.exception;

import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.util.Objects;

/**
 * Runtime exception of JCSP Library.
 */
public class JcspRuntimeException extends RuntimeException
{
    @Serial
    private static final long serialVersionUID = 6783560662387180450L;

    private final JcspExceptionStatus status;

    public static JcspRuntimeException forXmlLoadingError(String message, @Nullable Throwable cause)
    {
        return new JcspRuntimeException(JcspExceptionStatus.XML_LOADING_ERROR, message, cause);
    }

    public static JcspRuntimeException forXmlLoadingError(Throwable cause)
    {
        return new JcspRuntimeException(JcspExceptionStatus.XML_LOADING_ERROR, cause);
    }

    public static JcspRuntimeException forClassError(String message, @Nullable Throwable cause)
    {
        return new JcspRuntimeException(JcspExceptionStatus.CLASS_ERROR, message, cause);
    }

    public static JcspRuntimeException forClassError(Throwable cause)
    {
        return new JcspRuntimeException(JcspExceptionStatus.CLASS_ERROR, cause);
    }

    private JcspRuntimeException(JcspExceptionStatus status, String message, @Nullable Throwable cause)
    {
        super(message, cause);
        this.status = Objects.requireNonNull(status);
    }

    private JcspRuntimeException(JcspExceptionStatus status, @Nullable Throwable cause)
    {
        super(cause);
        this.status = Objects.requireNonNull(status);
    }


    public JcspExceptionStatus getStatus()
    {
        return status;
    }
}
