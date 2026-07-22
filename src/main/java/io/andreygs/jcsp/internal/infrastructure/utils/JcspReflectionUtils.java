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

package io.andreygs.jcsp.internal.infrastructure.utils;

import io.andreygs.jcsp.api.exception.JcspRuntimeException;
import io.andreygs.jcsp.internal.annotation.JcspInject;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Optional;

/**
 * TODO: place description here
 */
public class JcspReflectionUtils
{
    public static Class<?> requireClass(String className)
    {
        try
        {
            return Class.forName(className);
        }
        catch (ClassNotFoundException e)
        {
            throw JcspRuntimeException.forClassError("Class \"" + className + "\" not found", null);
        }
    }

    public static Constructor<?> requireDefaultConstructor(Class<?> clazz)
    {
        Constructor<?> constructor;
        try
        {
            constructor = clazz.getDeclaredConstructor();
        }
        catch (NoSuchMethodException e)
        {
            throw JcspRuntimeException.forClassError(
                "Class \"" + clazz.getName() + "\" has no default constructor", null);
        }
        if (!Modifier.isPublic(constructor.getModifiers()))
        {
            throw JcspRuntimeException.forClassError(
                "Class \"" + clazz.getName() + "\" default constructor is not public", null);
        }
        return constructor;
    }

    public static Method requireMethod(Class<?> clazz, String methodName, Class<?> returnType,
        Class<?>... parameterTypes)
    {
        Method method;
        try
        {
            method = clazz.getDeclaredMethod(methodName, parameterTypes);
        }
        catch (NoSuchMethodException e)
        {
            throw JcspRuntimeException.forClassError(
                "Class \"" + clazz.getName() + "\" has no method \"" + methodName +
                    "(" + JcspArrayUtils.toString(parameterTypes, ",") + ")\"", null);
        }
        if (!returnType.isAssignableFrom(method.getReturnType()))
        {
            throw JcspRuntimeException.forClassError(
                "Class \"" + returnType.getName() + "\" is not assignable from \"" +
                    method.getReturnType() + "\" return type", null);
        }
        return method;
    }

    private JcspReflectionUtils()
    {
    }
}
