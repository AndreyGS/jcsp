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

package io.andreygs.jcsp.internal.processing.data.type;

import io.andreygs.jcsp.internal.processing.data.type.factory.ITypeBoundsDescriptorFactory;
import io.andreygs.jcsp.internal.processing.data.type.model.TypeBoundKind;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * Unit-tests for {@link TypeBoundsDescriptorGenerator}.
 */
@ExtendWith(MockitoExtension.class)
public class TypeBoundsDescriptorGeneratorTests
{
    @Mock
    private ITypeBoundsDescriptorFactory typeBoundsDescriptorFactory;
    @Mock
    private ITypeBoundsDescriptor typeBoundsDescriptor;

    private TypeBoundsDescriptorGenerator generator;

    @BeforeEach
    public void setUp()
    {
        generator = new TypeBoundsDescriptorGenerator(typeBoundsDescriptorFactory);
    }

    @Test
    @SuppressWarnings("DataFlowIssue" /* Intentional contract nullability violation for test */)
    public void testConstructorNullFactory()
    {
        assertThatThrownBy(() -> new TypeBoundsDescriptorGenerator(null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    public void testGenerateTypeVariableWithClassBounds()
    {
        TypeVariable<? extends Class<?>> typeVariable = TestGenericClass1.class.getTypeParameters()[0];
        when(typeBoundsDescriptorFactory.create(eq(TypeBoundKind.UPPER_BOUND), anySet()))
            .thenAnswer(invocation -> {
                Set<Class<?>> classes = invocation.getArgument(1);
                assertThat(classes).containsExactly(Number.class, Runnable.class);
                return typeBoundsDescriptor;
            });
        Optional<ITypeBoundsDescriptor> result = generator.generate(typeVariable);
        assertThat(result).contains(typeBoundsDescriptor);
    }

    @Test
    public void testGenerateTypeVariableWithTypeVariableBound()
    {
        TypeVariable<? extends Class<?>> typeVariable = TestGenericClass2.class.getTypeParameters()[1];
        when(typeBoundsDescriptorFactory.create(eq(TypeBoundKind.UPPER_BOUND), eq("T")))
            .thenReturn(typeBoundsDescriptor);
        Optional<ITypeBoundsDescriptor> result = generator.generate(typeVariable);
        assertThat(result).contains(typeBoundsDescriptor);
    }

    @Test
    public void testGenerateTypeVariableWithoutBounds()
    {
        TypeVariable<? extends Class<?>> typeVariable = TestGenericClass3.class.getTypeParameters()[0];
        assertThat(generator.generate(typeVariable)).isEmpty();
    }

    @Test
    public void testGenerateWildcardWithClassBound()
    {
        WildcardType wildcardType1 = requireWildcardType(TestGenericField.class, "list1");
        when(typeBoundsDescriptorFactory.create(eq(TypeBoundKind.UPPER_BOUND), anySet()))
            .thenAnswer(invocation -> {
                Set<Class<?>> classes = invocation.getArgument(1);
                assertThat(classes).containsExactly(Number.class);
                return typeBoundsDescriptor;
            });
        Optional<ITypeBoundsDescriptor> result1 = generator.generate(wildcardType1);
        assertThat(result1).contains(typeBoundsDescriptor);

        WildcardType wildcardType2 = requireWildcardType(TestGenericField.class, "list2");
        when(typeBoundsDescriptorFactory.create(eq(TypeBoundKind.LOWER_BOUND), anySet()))
            .thenAnswer(invocation -> {
                Set<Class<?>> classes = invocation.getArgument(1);
                assertThat(classes).containsExactly(Number.class);
                return typeBoundsDescriptor;
            });
        Optional<ITypeBoundsDescriptor> result2 = generator.generate(wildcardType2);
        assertThat(result2).contains(typeBoundsDescriptor);

        WildcardType wildcardType3 = requireWildcardType(TestGenericField.class, "list3");
        when(typeBoundsDescriptorFactory.create(eq(TypeBoundKind.LOWER_BOUND), anySet()))
            .thenAnswer(invocation -> {
                Set<Class<?>> classes = invocation.getArgument(1);
                assertThat(classes).containsExactly(Object.class);
                return typeBoundsDescriptor;
            });
        Optional<ITypeBoundsDescriptor> result3 = generator.generate(wildcardType3);
        assertThat(result3).contains(typeBoundsDescriptor);
    }

    @Test
    public void testGenerateWildcardWithTypeVariableBound()
    {
        WildcardType wildcardType1 = requireWildcardType(TestGenericField.class, "list4");
        when(typeBoundsDescriptorFactory.create(eq(TypeBoundKind.UPPER_BOUND), eq("T")))
            .thenReturn(typeBoundsDescriptor);
        Optional<ITypeBoundsDescriptor> result1 = generator.generate(wildcardType1);
        assertThat(result1).contains(typeBoundsDescriptor);

        WildcardType wildcardType2 = requireWildcardType(TestGenericField.class, "list5");
        when(typeBoundsDescriptorFactory.create(eq(TypeBoundKind.LOWER_BOUND), eq("T")))
            .thenReturn(typeBoundsDescriptor);
        Optional<ITypeBoundsDescriptor> result2 = generator.generate(wildcardType2);
        assertThat(result2).contains(typeBoundsDescriptor);
    }

    @Test
    public void testGenerateWildcardWithoutBounds()
    {
        WildcardType wildcardType = requireWildcardType(TestGenericField.class, "list6");
        assertThat(generator.generate(wildcardType)).isEmpty();
    }

    @Test
    public void testGenerateTypeVariableForbiddenTypeBounds()
    {
        TypeVariable<? extends Class<?>> typeVariable = TestForbiddenClass.class.getTypeParameters()[0];
        assertThatThrownBy(() -> generator.generate(typeVariable)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testGenerateWildcardForbiddenTypeBounds()
    {
        WildcardType wildcardType1 = requireWildcardType(TestForbiddenWildcard.class, "list1");
        assertThatThrownBy(() -> generator.generate(wildcardType1)).isInstanceOf(IllegalArgumentException.class);
        WildcardType wildcardType2 = requireWildcardType(TestForbiddenWildcard.class, "list2");
        assertThatThrownBy(() -> generator.generate(wildcardType2)).isInstanceOf(IllegalArgumentException.class);
        WildcardType wildcardType3 = requireWildcardType(TestForbiddenWildcard.class, "list3");
        assertThatThrownBy(() -> generator.generate(wildcardType3)).isInstanceOf(IllegalArgumentException.class);
        WildcardType wildcardType4 = requireWildcardType(TestForbiddenWildcard.class, "list4");
        assertThatThrownBy(() -> generator.generate(wildcardType4)).isInstanceOf(IllegalArgumentException.class);
    }

    private WildcardType requireWildcardType(Class<?> clazz, String fieldName)
    {
        Field field;
        try
        {
            field = clazz.getDeclaredField(fieldName);
        }
        catch (NoSuchFieldException e)
        {
            throw new RuntimeException(e);
        }
        ParameterizedType type = (ParameterizedType)field.getGenericType();
        Type[] typeArgs = type.getActualTypeArguments();
        return (WildcardType)typeArgs[0];
    }

    @SuppressWarnings("unused" /* Parameters are need for tests of work with generic classes */)
    private static class TestGenericClass1<T extends Number & Runnable>
    {
    }

    @SuppressWarnings("unused" /* Parameters are need for tests of work with generic classes */)
    private static class TestGenericClass2<T, V extends T>
    {
    }

    @SuppressWarnings("unused" /* Parameters are need for tests of work with generic classes */)
    private static class TestGenericClass3<T>
    {
    }

    @SuppressWarnings("unused" /* Parameters and fields are need for tests of work with generic classes */)
    private static class TestGenericField<T>
    {
        private List<? extends Number> list1;
        private List<? super Number> list2;
        private List<? super Object> list3;
        private List<? extends T> list4;
        private List<? super T> list5;
        private List<?> list6;
    }

    @SuppressWarnings({"unused" /* Params are need for tests of work with generic classes */,
        "rawtypes" /* List should be raw for test purposes */})
    private static class TestForbiddenClass<E extends List>
    {
    }

    @SuppressWarnings("unused" /* Parameters and fields are need for tests of work with generic classes */)
    private static class TestForbiddenWildcard<T>
    {
        @SuppressWarnings("rawtypes" /* List should be raw for test purposes */)
        private List<? extends List> list1;
        private List<? extends int[]> list2;
        private List<? extends Number[]> list3;
        private List<? extends T[]> list4;
    }
}
