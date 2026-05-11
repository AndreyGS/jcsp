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

package io.andreygs.jcsp.internal.processing.data;

import io.andreygs.jcsp.api.model.annotation.CspCreateProcessor;
import io.andreygs.jcsp.internal.model.annotation.utils.CspAnnotationUtils;
import io.andreygs.jcsp.api.model.exception.CspRuntimeException;
import io.andreygs.jcsp.api.model.protocol.CspStatus;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO: place description here
 */
abstract class AbstractCspClassProcessorGenerator<P, TP>
    implements ICspClassProcessorGenerator<P>
{
    @Override
    public P generateProcessor(Class<?> structClazz)
    {
        if (!CspAnnotationUtils.isCspCreateProcessor(structClazz))
        {
            throw CspRuntimeException.createCspRuntimeException(CspStatus.NO_SUCH_HANDLER,
                structClazz.getName() + " is not annotated with " + CspCreateProcessor.class.getName());
        }
        List<TP> typeProcessors = new ArrayList<>();
        return createProcessor(typeProcessors);
    }

    protected abstract void addParentClass(Class<?> parentClazz, List<TP> typeProcessors);

    protected abstract void addField(Field field, List<TP> typeProcessors);

    protected abstract P createProcessor(List<TP> typeProcessors);

    private void produceProxyProcessors(Class<?> clazz, List<TP> typeProcessors)
    {
        Class<?> parentClazz = clazz.getSuperclass();
        if (CspAnnotationUtils.isCspCreateProcessor(parentClazz))
        {
            addParentClass(parentClazz, typeProcessors);
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
