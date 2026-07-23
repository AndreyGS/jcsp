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

package io.andreygs.jcsp.internal.infrastructure.service;

import java.util.List;

/**
 * Provider for all JCSP library IoC services.
 * <p>
 * Provides all available JCSP services including factories, registries and so on.
 *
 * @apiNote
 * Thread-safe
 */
public interface IJcspServiceProvider
{
    /**
     * Provides service for serviceClass.
     *
     * @param serviceClass Class (probably an interface) of service.
     * @return service.
     * @param <S> Type of service.
     */
    <S> S provide(Class<S> serviceClass);

    /**
     * Provides service for serviceClass with additional distinguishing features if any.
     *
     * @param serviceClass Class (probably an interface) of service.
     * @param genericTypeVariableClasses Generic type variable arguments if serviceClass is generic and has different
     *                                   services for different type variable arguments. If list is empty, then no
     *                                   matter is serviceClass generic or not, but this feature is ignored. If
     *                                   genericTypeVariableClasses is not empty and provider has no service with such
     *                                   types, then service without prescribed generic type variable arguments will
     *                                   try to be found.
     * @param serviceName Unique service name for additional distinguishing of specific service. If it equals to empty
     *                    string, then service considered unnamed and this feature is ignored.
     * @return service.
     * @param <S> Type (class) of service.
     */
    <S> S provide(Class<S> serviceClass, List<Class<?>> genericTypeVariableClasses, String serviceName);
}
