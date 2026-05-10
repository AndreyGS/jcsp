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

import io.andreygs.jcsp.base.processing.annotations.CspCreateProcessor;
import io.andreygs.jcsp.base.processing.annotations.internal.CspAnnotationUtils;
import io.andreygs.jcsp.base.types.CspRuntimeException;
import io.andreygs.jcsp.base.types.CspStatus;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO: place description here
 */
abstract class AbstractCspDataProcessorGenerator<P, PP>
    implements ICspDataProcessorGenerator<P>
{
    @Override
    public P generateProcessor(Class<?> structClazz)
    {
        if (!CspAnnotationUtils.isCspCreateProcessor(structClazz))
        {
            throw CspRuntimeException.createCspRuntimeException(CspStatus.NO_SUCH_HANDLER,
                structClazz.getName() + " is not annotated with " + CspCreateProcessor.class.getName());
        }
        List<PP> proxyProcessors = new ArrayList<>();
        return createProcessor(proxyProcessors);
    }

    protected abstract void addParentClass(Class<?> parentClazz, List<PP> proxyProcessors);

    protected abstract void addField(Field field, List<PP> proxyProcessors);

    protected abstract P createProcessor(List<PP> proxyProcessors);

    private void produceProxyProcessors(Class<?> clazz, List<PP> proxyProcessors)
    {
        Class<?> parentClazz = clazz.getSuperclass();
        if (CspAnnotationUtils.isCspCreateProcessor(parentClazz))
        {
            addParentClass(parentClazz, proxyProcessors);
        }
        /*
        Field[] fields = clazz.getDeclaredFields();
        Arrays.stream(fields)
              .filter(field -> field.getAnnotation(CspField.class) != null)
              .sorted(Comparator.comparingInt(field -> field.getAnnotation(CspField.class).order()))
              .forEach(field -> {
                  field.setAccessible(true);
                  addField(field, callbacks);
              });*/
    }
}
