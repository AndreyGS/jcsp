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

import io.andreygs.jcsp.api.model.annotation.CspFixedSizeArray;
import io.andreygs.jcsp.api.model.annotation.CspImplementationClass;
import io.andreygs.jcsp.api.model.annotation.CspReference;
import io.andreygs.jcsp.api.model.annotation.CspString;
import io.andreygs.jcsp.api.model.protocol.utils.CspTypeToken;
import io.andreygs.jcsp.api.processing.data.ICspClassDeserializationProcessor;
import io.andreygs.jcsp.api.processing.data.ICspClassSerializationProcessor;
import io.andreygs.jcsp.internal.processing.data.model.IGenericClassProcessorHolder;
import io.andreygs.jcsp.internal.processing.data.type.ICspTypeDeserializationProcessor;
import io.andreygs.jcsp.internal.processing.data.type.ICspTypeSerializationProcessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.AnnotatedType;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit-tests for {@link CspProcessorRegistry}.
 */
@ExtendWith(MockitoExtension.class)
public class CspProcessorRegistryTests
{
    @Mock
    private ICspClassSerializationProcessor<?> classSerializationProcessor;
    @Mock
    private ICspClassDeserializationProcessor<?> classDeserializationProcessor;
    @Mock
    private ICspTypeDeserializationProcessor typeDeserializationProcessor;

    @Test
    public void testRegisterClassProcessorOrdinary()
    {
        ICspProcessorRegistry<ICspClassSerializationProcessor<?>, ICspTypeSerializationProcessor>
            cspProcessorRegistry = new CspProcessorRegistry<>();

        assertThat(cspProcessorRegistry.findOrdinaryClassProcessor(TestClass1.class)).isEmpty();
        assertThat(cspProcessorRegistry.findOrdinaryClassProcessor(TestClass2.class)).isEmpty();
        assertThat(cspProcessorRegistry.findOrdinaryClassProcessor(TestClass3.class)).isEmpty();
        assertThat(cspProcessorRegistry.findOrdinaryClassProcessor(TestClass4.class)).isEmpty();

        ICspClassSerializationProcessor<?> classProcessor2 = (value, processor) -> {};
        ICspClassSerializationProcessor<?> classProcessor3 = (value, processor) -> {};
        ICspClassSerializationProcessor<?> classProcessor4 = (value, processor) -> {};

        cspProcessorRegistry.registerClassProcessor(TestClass1.class, classSerializationProcessor);
        cspProcessorRegistry.registerClassProcessor(TestClass2.class, classProcessor2);
        cspProcessorRegistry.registerClassProcessor(TestClass3.class, classProcessor3);
        cspProcessorRegistry.registerClassProcessor(TestClass4.class, classProcessor4);

        assertThat(cspProcessorRegistry.findOrdinaryClassProcessor(TestClass1.class))
            .contains(classSerializationProcessor);
        assertThat(cspProcessorRegistry.findOrdinaryClassProcessor(TestClass2.class))
            .contains(classProcessor2);
        assertThat(cspProcessorRegistry.findOrdinaryClassProcessor(TestClass3.class))
            .contains(classProcessor3);
        assertThat(cspProcessorRegistry.findOrdinaryClassProcessor(TestClass4.class))
            .contains(classProcessor4);
    }

    @SuppressWarnings("CommentedOutCode" /* Comment include example of code that highlights by IDEA (but compiling
    and successfully running) - possibly IDE bug */)
    @Test
    public void testRegisterClassProcessorGeneric()
    {
        ICspProcessorRegistry<ICspClassSerializationProcessor<?>, ICspTypeSerializationProcessor>
            cspProcessorRegistry = new CspProcessorRegistry<>();

        assertThat(cspProcessorRegistry.findOrdinaryClassProcessor(TestGenericClass1.class)).isEmpty();
        assertThat(cspProcessorRegistry.findOrdinaryClassProcessor(TestGenericClass2.class)).isEmpty();
        assertThat(cspProcessorRegistry.findOrdinaryClassProcessor(TestGenericClass3.class)).isEmpty();
        assertThat(cspProcessorRegistry.findOrdinaryClassProcessor(TestGenericClass4.class)).isEmpty();

        ICspClassSerializationProcessor<?> classProcessor2 = (value, processor) -> {};
        ICspClassSerializationProcessor<?> classProcessor3 = (value, processor) -> {};
        ICspClassSerializationProcessor<?> classProcessor4 = (value, processor) -> {};

        cspProcessorRegistry.registerClassProcessor(TestGenericClass1.class, classSerializationProcessor);
        cspProcessorRegistry.registerClassProcessor(TestGenericClass2.class, classProcessor2);
        cspProcessorRegistry.registerClassProcessor(TestGenericClass3.class, classProcessor3);
        cspProcessorRegistry.registerClassProcessor(TestGenericClass4.class, classProcessor4);

        Optional<IGenericClassProcessorHolder<ICspClassSerializationProcessor<?>>> classProcessorHolder1 =
            cspProcessorRegistry.findGenericClassProcessor(TestGenericClass1.class);
        Optional<IGenericClassProcessorHolder<ICspClassSerializationProcessor<?>>> classProcessorHolder2 =
            cspProcessorRegistry.findGenericClassProcessor(TestGenericClass2.class);
        Optional<IGenericClassProcessorHolder<ICspClassSerializationProcessor<?>>> classProcessorHolder3 =
            cspProcessorRegistry.findGenericClassProcessor(TestGenericClass3.class);
        Optional<IGenericClassProcessorHolder<ICspClassSerializationProcessor<?>>> classProcessorHolder4 =
            cspProcessorRegistry.findGenericClassProcessor(TestGenericClass4.class);

        assertThat(classProcessorHolder1).isPresent();
        assertThat(classProcessorHolder2).isPresent();
        assertThat(classProcessorHolder3).isPresent();
        assertThat(classProcessorHolder4).isPresent();

        ICspClassSerializationProcessor<?> classProcessor1Result = classProcessorHolder1.get().getClassProcessor();
        ICspClassSerializationProcessor<?> classProcessor2Result = classProcessorHolder2.get().getClassProcessor();
        ICspClassSerializationProcessor<?> classProcessor3Result = classProcessorHolder3.get().getClassProcessor();
        ICspClassSerializationProcessor<?> classProcessor4Result = classProcessorHolder4.get().getClassProcessor();

        assertThat(classProcessor1Result).isEqualTo(classSerializationProcessor);
        assertThat(classProcessorHolder1).map(IGenericClassProcessorHolder::getTypeVariableNames)
                                         .contains(Set.of("X", "V"));
        assertThat(classProcessor2Result).isEqualTo(classProcessor2);
        assertThat(classProcessorHolder2).map(IGenericClassProcessorHolder::getTypeVariableNames)
                                         .contains(Set.of("W", "E"));
        assertThat(classProcessor3Result).isEqualTo(classProcessor3);
        assertThat(classProcessorHolder3).map(IGenericClassProcessorHolder::getTypeVariableNames)
                                         .contains(Set.of("K"));
        assertThat(classProcessor4Result).isEqualTo(classProcessor4);
        assertThat(classProcessorHolder4).map(IGenericClassProcessorHolder::getTypeVariableNames)
                                         .contains(Set.of("P", "M"));

        // Next alternative triggers IDEA statical analyzer on .contains(...) line.
        // Because of this it replaced with current code.
        // assertThat(cspProcessorRegistry.findGenericClassProcessor(clazz))
        //            .isPresent()
        //            .map(IGenericClassProcessorHolder::getClassProcessor)
        //            .contains(classSerializationProcessor);
    }

    @Test
    public void testRegisterClassProcessorGenericModifyTypeVariableNamesSet()
    {
        ICspProcessorRegistry<ICspClassSerializationProcessor<?>, ICspTypeSerializationProcessor> cspProcessorRegistry = new CspProcessorRegistry<>();

        cspProcessorRegistry.registerClassProcessor(TestGenericClass1.class, classSerializationProcessor);

        Optional<IGenericClassProcessorHolder<ICspClassSerializationProcessor<?>>> classProcessorHolder1
            = cspProcessorRegistry.findGenericClassProcessor(TestGenericClass1.class);

        assertThat(classProcessorHolder1).isPresent();

        Set<String> typeVariableNames = classProcessorHolder1.get().getTypeVariableNames();

        assertThatThrownBy(() -> typeVariableNames.add("test")).isInstanceOf(Throwable.class);
    }

    @Test
    @SuppressWarnings("DataFlowIssue" /* "Intentional contract nullability violation for test" */)
    public void testRegisterClassProcessorNullClass()
    {
        ICspProcessorRegistry<ICspClassSerializationProcessor<?>, ICspTypeSerializationProcessor>
            cspProcessorRegistry = new CspProcessorRegistry<>();

        assertThatThrownBy(() -> cspProcessorRegistry.registerClassProcessor(null, classSerializationProcessor))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    @SuppressWarnings("DataFlowIssue" /* "Intentional contract nullability violation for test" */)
    public void testRegisterProcessorNullClassProcessor()
    {
        ICspProcessorRegistry<ICspClassSerializationProcessor<?>, ICspTypeSerializationProcessor>
            cspProcessorRegistry = new CspProcessorRegistry<>();

        assertThatThrownBy(() -> cspProcessorRegistry.registerClassProcessor(TestClass1.class, null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    public void testRegisterClassProcessorForbiddenClassGroup()
    {
        ICspProcessorRegistry<ICspClassSerializationProcessor<?>, ICspTypeSerializationProcessor>
            cspProcessorRegistry = new CspProcessorRegistry<>();

        assertThatThrownBy(() -> cspProcessorRegistry.registerClassProcessor(int.class, classSerializationProcessor))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> cspProcessorRegistry.registerClassProcessor(TestGenericClass1[].class, classSerializationProcessor))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testRegisterClassProcessorForbiddenClass()
    {
        ICspProcessorRegistry<ICspClassDeserializationProcessor<?>, ICspTypeDeserializationProcessor>
            cspProcessorRegistry = new CspProcessorRegistry<>();

        assertThatThrownBy(() -> cspProcessorRegistry.registerClassProcessor(String.class,
            classDeserializationProcessor))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> cspProcessorRegistry.registerClassProcessor(Collection.class,
            classDeserializationProcessor))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> cspProcessorRegistry.registerClassProcessor(Map.class, classDeserializationProcessor))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testRegisterClassProcessorReplaceOrdinary()
    {
        ICspProcessorRegistry<ICspClassSerializationProcessor<?>, ICspTypeSerializationProcessor>
            cspProcessorRegistry = new CspProcessorRegistry<>();

        cspProcessorRegistry.registerClassProcessor(TestClass1.class, (value, processor) -> {});
        cspProcessorRegistry.registerClassProcessor(TestClass1.class, classSerializationProcessor);

        assertThat(cspProcessorRegistry.findOrdinaryClassProcessor(TestClass1.class)).containsSame(classSerializationProcessor);
    }

    @SuppressWarnings("CommentedOutCode" /* Comment include example of code that highlights by IDEA (but compiling
    and successfully running) - possibly IDE bug */)
    @Test
    public void testRegisterClassProcessorReplaceGeneric()
    {
        ICspProcessorRegistry<ICspClassSerializationProcessor<?>, ICspTypeSerializationProcessor>
            cspProcessorRegistry = new CspProcessorRegistry<>();

        cspProcessorRegistry.registerClassProcessor(TestGenericClass1.class, (value, processor) -> {});
        cspProcessorRegistry.registerClassProcessor(TestGenericClass1.class, classSerializationProcessor);

        Optional<IGenericClassProcessorHolder<ICspClassSerializationProcessor<?>>> classProcessorHolder =
            cspProcessorRegistry.findGenericClassProcessor(TestGenericClass1.class);

        assertThat(classProcessorHolder).isPresent();

        ICspClassSerializationProcessor<?> classProcessor = classProcessorHolder.get().getClassProcessor();

        assertThat(classProcessor).isEqualTo(classSerializationProcessor);

        // Next alternative triggers IDEA statical analyzer on .contains(...) line.
        // Because of this it replaced with current code.
        // assertThat(cspProcessorRegistry.findGenericClassProcessor(clazz))
        //            .isPresent()
        //            .map(IGenericClassProcessorHolder::getClassProcessor)
        //            .contains(classSerializationProcessor);
    }

    @Test
    public void testRegisterTypeProcessor()
    {
        ICspProcessorRegistry<ICspClassDeserializationProcessor<?>, ICspTypeDeserializationProcessor>
            cspProcessorRegistry = new CspProcessorRegistry<>();

        AnnotatedType annotatedType1 =
            new CspTypeToken<List<Map<@CspReference @CspString("UTF16-BE") String, @CspImplementationClass(ConcurrentHashMap.class) Map<int @CspFixedSizeArray(1) @CspReference [], Long>>>>(){}.getAnnotatedType();
        AnnotatedType annotatedType2 =
            new CspTypeToken<List<Map<@CspString("UTF16-BE") String, @CspImplementationClass(ConcurrentHashMap.class) Map<int @CspFixedSizeArray(1) @CspReference [], Long>>>>(){}.getAnnotatedType();
        AnnotatedType annotatedType3 =
            new CspTypeToken<List<Long>>(){}.getAnnotatedType();
        AnnotatedType annotatedType4 =
            new CspTypeToken<@CspReference @CspString("UTF16-BE") String>(){}.getAnnotatedType();

        assertThat(cspProcessorRegistry.findTypeProcessor(annotatedType1)).isEmpty();
        assertThat(cspProcessorRegistry.findTypeProcessor(annotatedType2)).isEmpty();
        assertThat(cspProcessorRegistry.findTypeProcessor(annotatedType3)).isEmpty();
        assertThat(cspProcessorRegistry.findTypeProcessor(annotatedType4)).isEmpty();

        ICspTypeDeserializationProcessor typeProcessor2 = (value, extendedProcessor) -> null;
        ICspTypeDeserializationProcessor typeProcessor3 = (value, extendedProcessor) -> null;
        ICspTypeDeserializationProcessor typeProcessor4 = (value, extendedProcessor) -> null;

        cspProcessorRegistry.registerTypeProcessor(annotatedType1, typeDeserializationProcessor);
        cspProcessorRegistry.registerTypeProcessor(annotatedType2, typeProcessor2);
        cspProcessorRegistry.registerTypeProcessor(annotatedType3, typeProcessor3);
        cspProcessorRegistry.registerTypeProcessor(annotatedType4, typeProcessor4);

        assertThat(cspProcessorRegistry.findTypeProcessor(annotatedType1)).containsSame(typeDeserializationProcessor);
        assertThat(cspProcessorRegistry.findTypeProcessor(annotatedType2)).containsSame(typeProcessor2);
        assertThat(cspProcessorRegistry.findTypeProcessor(annotatedType3)).containsSame(typeProcessor3);
        assertThat(cspProcessorRegistry.findTypeProcessor(annotatedType4)).containsSame(typeProcessor4);
    }

    @Test
    @SuppressWarnings("DataFlowIssue" /* "Intentional contract nullability violation for test" */)
    public void testRegisterTypeProcessorNullType()
    {
        ICspProcessorRegistry<ICspClassDeserializationProcessor<?>, ICspTypeDeserializationProcessor>
            cspProcessorRegistry = new CspProcessorRegistry<>();

        assertThatThrownBy(() -> cspProcessorRegistry.registerTypeProcessor(null, typeDeserializationProcessor))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    @SuppressWarnings("DataFlowIssue" /* "Intentional contract nullability violation for test" */)
    public void testRegisterTypeProcessorNullProcessor()
    {
        ICspProcessorRegistry<ICspClassDeserializationProcessor<?>, ICspTypeDeserializationProcessor>
            cspProcessorRegistry = new CspProcessorRegistry<>();

        AnnotatedType annotatedType =
            new CspTypeToken<@CspReference @CspString("UTF16-BE") String>(){}.getAnnotatedType();

        assertThatThrownBy(() -> cspProcessorRegistry.registerTypeProcessor(annotatedType, null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    public void testRegisterTypeProcessorReplace()
    {
        ICspProcessorRegistry<ICspClassDeserializationProcessor<?>, ICspTypeDeserializationProcessor>
            cspProcessorRegistry = new CspProcessorRegistry<>();

        AnnotatedType annotatedType =
            new CspTypeToken<@CspReference @CspString("UTF16-BE") String>(){}.getAnnotatedType();

        cspProcessorRegistry.registerTypeProcessor(annotatedType, (value, extendedProcessor) -> null);
        cspProcessorRegistry.registerTypeProcessor(annotatedType, typeDeserializationProcessor);

        assertThat(cspProcessorRegistry.findTypeProcessor(annotatedType)).containsSame(typeDeserializationProcessor);
    }

    @Test
    public void testFindOrdinaryClassProcessor()
    {
        testRegisterClassProcessorOrdinary();
    }

    @Test
    @SuppressWarnings("DataFlowIssue" /* "Intentional contract nullability violation for test" */)
    public void testFindOrdinaryClassProcessorNullClass()
    {
        ICspProcessorRegistry<ICspClassSerializationProcessor<?>, ICspTypeSerializationProcessor>
            cspProcessorRegistry = new CspProcessorRegistry<>();

        assertThatThrownBy(() -> cspProcessorRegistry.findOrdinaryClassProcessor(null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    public void testFindGenericClassProcessor()
    {
        testRegisterClassProcessorGeneric();
    }

    @Test
    @SuppressWarnings("DataFlowIssue" /* "Intentional contract nullability violation for test" */)
    public void testFindGenericClassProcessorNullClass()
    {
        ICspProcessorRegistry<ICspClassSerializationProcessor<?>, ICspTypeSerializationProcessor>
            cspProcessorRegistry = new CspProcessorRegistry<>();

        assertThatThrownBy(() -> cspProcessorRegistry.findGenericClassProcessor(null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    public void testFindTypeProcessor()
    {
        testRegisterTypeProcessor();
    }

    @Test
    @SuppressWarnings("DataFlowIssue" /* "Intentional contract nullability violation for test" */)
    public void testFindTypeProcessorNullType()
    {
        ICspProcessorRegistry<ICspClassSerializationProcessor<?>, ICspTypeSerializationProcessor>
            cspProcessorRegistry = new CspProcessorRegistry<>();

        assertThatThrownBy(() -> cspProcessorRegistry.findTypeProcessor(null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    public void testUnregisterClassProcessorOrdinary()
    {
        ICspProcessorRegistry<ICspClassDeserializationProcessor<?>, ICspTypeDeserializationProcessor>
            cspProcessorRegistry = new CspProcessorRegistry<>();
        cspProcessorRegistry.registerClassProcessor(TestClass1.class, classDeserializationProcessor);
        cspProcessorRegistry.unregisterClassProcessor(TestClass1.class);

        assertThat(cspProcessorRegistry.findOrdinaryClassProcessor(TestClass1.class)).isEmpty();
    }

    @Test
    public void testUnregisterClassProcessorGeneric()
    {
        ICspProcessorRegistry<ICspClassDeserializationProcessor<?>, ICspTypeDeserializationProcessor>
            cspProcessorRegistry = new CspProcessorRegistry<>();
        cspProcessorRegistry.registerClassProcessor(TestGenericClass1.class, classDeserializationProcessor);
        cspProcessorRegistry.unregisterClassProcessor(TestGenericClass1.class);

        assertThat(cspProcessorRegistry.findOrdinaryClassProcessor(TestGenericClass1.class)).isEmpty();
    }

    @Test
    @SuppressWarnings("DataFlowIssue" /* "Intentional contract nullability violation for test" */)
    public void testUnregisterClassProcessorOrdinaryNullClass()
    {
        ICspProcessorRegistry<ICspClassSerializationProcessor<?>, ICspTypeSerializationProcessor>
            cspProcessorRegistry = new CspProcessorRegistry<>();

        assertThatThrownBy(() -> cspProcessorRegistry.unregisterClassProcessor(null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    public void testUnregisterTypeProcessor()
    {
        ICspProcessorRegistry<ICspClassDeserializationProcessor<?>, ICspTypeDeserializationProcessor>
            cspProcessorRegistry = new CspProcessorRegistry<>();

        AnnotatedType annotatedType =
            new CspTypeToken<@CspReference @CspString("UTF16-BE") String>(){}.getAnnotatedType();

        cspProcessorRegistry.registerTypeProcessor(annotatedType, typeDeserializationProcessor);
        cspProcessorRegistry.unregisterTypeProcessor(annotatedType);

        assertThat(cspProcessorRegistry.findTypeProcessor(annotatedType)).isEmpty();
    }

    @Test
    @SuppressWarnings("DataFlowIssue" /* "Contract nullability violation" */)
    public void testUnregisterTypeProcessorNullType()
    {
        ICspProcessorRegistry<ICspClassSerializationProcessor<?>, ICspTypeSerializationProcessor>
            cspProcessorRegistry = new CspProcessorRegistry<>();

        assertThatThrownBy(() -> cspProcessorRegistry.unregisterTypeProcessor(null))
            .isInstanceOf(NullPointerException.class);
    }

    static class TestClass1
    {
    }
    static class TestClass2
    {
    }
    static class TestClass3
    {
    }
    static class TestClass4
    {
    }

    @SuppressWarnings("unused" /* Params are need for tests of work with generic classes */)
    static class TestGenericClass1<X, V>
    {
    }

    @SuppressWarnings("unused" /* Params are need for tests of work with generic classes */)
    static class TestGenericClass2<W, E>
    {
    }

    @SuppressWarnings("unused" /* Params are need for tests of work with generic classes */)
    static class TestGenericClass3<K>
    {
    }

    @SuppressWarnings("unused" /* Params are need for tests of work with generic classes */)
    static class TestGenericClass4<P, M>
    {
    }
}
