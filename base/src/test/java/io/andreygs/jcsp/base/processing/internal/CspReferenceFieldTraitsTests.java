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

import io.andreygs.jcsp.base.processing.annotations.CspImplementationClass;
import io.andreygs.jcsp.base.processing.annotations.CspReference;
import io.andreygs.jcsp.base.processing.annotations.CspStringCharset;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: place description here
 */
public class CspReferenceFieldTraitsTests
{
    @Test
    public void testReferencedType()
    {
        Map<Class<?>, IGen<?>> map1 = new HashMap<>();

        TypeInfo typeInfo
            = new AnnotatedTypeInfoExtractor<@CspImplementationClass(HashMap.class) Map<@CspReference @CspStringCharset("UTF-16BE") String, List<Integer>>>()
        {
        }.getTypeInfo();
        int i = 1;
        Map<String, List<Integer>> map = TestCast.testCast(
            new AnnotatedTypeInfoExtractor<@CspImplementationClass(HashMap.class) Map<@CspReference @CspStringCharset("UTF-16BE") String, List<Integer>>>()
            {
            });
        A<String> as = null;
        new TestGeneric<String>().getT(as, typeInfo.genericArguments.get(0).genericArguments.get(0));
        Assertions.assertTrue(true);
    }
}
/*
    public <T> T applyFunction(Function<?, T> f, Object input) {
        return f.apply(input);
    }
}
*/


interface IGen<T>
{
    T getT(T t, TypeInfo... typeInfo);
}

class A<T>
{
    Map<T, String> list;
}

class TestGeneric<T> implements IGen<A<T>>
{
    @Override
    public A<T> getT(A<T> t, TypeInfo... typeInfo)
    {
        var test = new AnnotatedTypeInfoExtractor<@CspReference Map<@CspReference T,
                                                                   @CspStringCharset("UTF-16BE") String>>(typeInfo){};
        test.getTypeInfo();
        t.list = TestCast.testCast(new AnnotatedTypeInfoExtractor<@CspReference Map<@CspReference T, @CspStringCharset("UTF-16BE") String>>(typeInfo){});
        return null;
    }
}

class TestCast
{
    static <T> T testCast(AnnotatedTypeInfoExtractor<T> t)
    {
        return null;
    }
}

class GenericParam
{
    public int i;

    GenericParam(int i)
        {
        this.i = i;
        }
}

class TypeInfo {
    Type type;
    Annotation[] annotations;
    List<TypeInfo> genericArguments = new ArrayList<>();

    public TypeInfo(Type type, Annotation[] annotations) {
        this.type = type;
        this.annotations = annotations;
    }
}

abstract class AnnotatedTypeInfoExtractor<T> {
    TypeInfo[] baseTypeInfos;

    protected AnnotatedTypeInfoExtractor()
    {

    }

    protected AnnotatedTypeInfoExtractor(TypeInfo[] typeInfo)
    {
        baseTypeInfos = typeInfo;
    }

    protected AnnotatedType extractAnnotatedType() {
        AnnotatedType type = getClass().getAnnotatedSuperclass();
        if (type instanceof java.lang.reflect.AnnotatedParameterizedType) {
            return type;
        }
        return null;
    }

    public TypeInfo getTypeInfo() {
        AnnotatedType at = extractAnnotatedType();
        if (at == null) {
            throw new IllegalStateException("Cannot extract AnnotatedType");
        }
        return processType(at);
    }

    private TypeInfo processType(AnnotatedType at) {
        // Получаем базовый тип и аннотации
        Type rawType = at.getType();
        Annotation[] anns = at.getAnnotations();

        TypeInfo info = new TypeInfo(rawType, anns);

        // Обрабатываем параметры, если есть
        if (at instanceof java.lang.reflect.AnnotatedParameterizedType) {
            java.lang.reflect.AnnotatedParameterizedType apt = (java.lang.reflect.AnnotatedParameterizedType) at;
            for (AnnotatedType argType : apt.getAnnotatedActualTypeArguments()) {
                info.genericArguments.add(processType(argType));
            }
        }

        return info;
    }
}
/*
abstract class AnnotatedTypeInfoExtractor<T>
{
    protected AnnotatedType extractAnnotatedType()
    {
        AnnotatedType type = getClass().getAnnotatedSuperclass();
        return ((AnnotatedParameterizedType)type).getAnnotatedActualTypeArguments()[0];
    }
}
*//*
abstract class RefTest<T> extends AnnotatedTypeInfoExtractor<T>
{
    private AnnotatedType annotated;
    public RefTest()
    {
        this.annotated = extractAnnotatedType();
    }
}*/
