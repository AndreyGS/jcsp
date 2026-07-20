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

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit-tests for {@link TypeBoundsDescriptor}.
 */
public class TypeBoundsDescriptorTest
{
    @Test
    public void testConstructorClassBounds()
    {
        ITypeBoundsDescriptor descriptor =
            new TypeBoundsDescriptor(TypeBoundKind.UPPER_BOUND, Set.of(String.class, Integer.class));
        assertThat(descriptor.getTypeBoundKind()).isEqualTo(TypeBoundKind.UPPER_BOUND);
        assertThat(descriptor.getTypeIdKind()).isEqualTo(TypeIdKind.CLASS);
        assertThat(descriptor.getBoundClasses()).containsExactlyInAnyOrder(String.class, Integer.class);
        assertThat(descriptor.getBoundTypeVariableName()).isEmpty();
    }

    @Test
    public void testConstructorClassBoundsEmpty()
    {
        assertThatIllegalArgumentException().isThrownBy(() -> new TypeBoundsDescriptor(TypeBoundKind.UPPER_BOUND, Set.of()));
    }

    @Test
    @SuppressWarnings("DataFlowIssue" /* Intentional contract nullability violation for test */)
    public void testConstructorClassBoundsNullTypeBoundKind()
    {
        assertThatNullPointerException().isThrownBy(() -> new TypeBoundsDescriptor(null, Set.of(String.class)));
    }

    @Test
    @SuppressWarnings("DataFlowIssue" /* Intentional contract nullability violation for test */)
    public void testConstructorClassBoundsNullClassSet()
    {
        assertThatNullPointerException()
            .isThrownBy(() -> new TypeBoundsDescriptor(TypeBoundKind.UPPER_BOUND, (Set<Class<?>>)null));
    }

    @Test
    public void testConstructorTypeVariableBounds()
    {
        ITypeBoundsDescriptor descriptor = new TypeBoundsDescriptor(TypeBoundKind.UPPER_BOUND, "T");
        assertThat(descriptor.getTypeBoundKind()).isEqualTo(TypeBoundKind.UPPER_BOUND);
        assertThat(descriptor.getTypeIdKind()).isEqualTo(TypeIdKind.TYPE_VARIABLE_NAME);
        assertThat(descriptor.getBoundClasses()).isEmpty();
        assertThat(descriptor.getBoundTypeVariableName()).contains("T");
    }

    @Test
    public void testConstructorTypeVariableBoundsEmpty()
    {
        assertThatIllegalArgumentException().isThrownBy(() -> new TypeBoundsDescriptor(TypeBoundKind.UPPER_BOUND, ""));
    }

    @Test
    @SuppressWarnings("DataFlowIssue" /* Intentional contract nullability violation for test */)
    public void testConstructorTypeVariableBoundsNullTypeBoundKind()
    {
        assertThatNullPointerException().isThrownBy(() -> new TypeBoundsDescriptor(null, "T"));
    }

    @Test
    @SuppressWarnings("DataFlowIssue" /* Intentional contract nullability violation for test */)
    public void testConstructorTypeVariableBoundsNullTypeVariableName()
    {
        assertThatNullPointerException().isThrownBy(() -> new TypeBoundsDescriptor(TypeBoundKind.UPPER_BOUND, (String)null));
    }

    @Test
    public void testGetTypeBoundKind()
    {
        ITypeBoundsDescriptor descriptor1 = new TypeBoundsDescriptor(TypeBoundKind.UPPER_BOUND, Set.of(String.class));
        assertThat(descriptor1.getTypeBoundKind()).isEqualTo(TypeBoundKind.UPPER_BOUND);
        ITypeBoundsDescriptor descriptor2 = new TypeBoundsDescriptor(TypeBoundKind.LOWER_BOUND, Set.of(String.class));
        assertThat(descriptor2.getTypeBoundKind()).isEqualTo(TypeBoundKind.LOWER_BOUND);

        ITypeBoundsDescriptor descriptor3 = new TypeBoundsDescriptor(TypeBoundKind.UPPER_BOUND, "T");
        assertThat(descriptor3.getTypeBoundKind()).isEqualTo(TypeBoundKind.UPPER_BOUND);
        ITypeBoundsDescriptor descriptor4 = new TypeBoundsDescriptor(TypeBoundKind.LOWER_BOUND, "T");
        assertThat(descriptor4.getTypeBoundKind()).isEqualTo(TypeBoundKind.LOWER_BOUND);
    }

    @Test
    public void testGetTypeIdKind()
    {
        ITypeBoundsDescriptor descriptor1 = new TypeBoundsDescriptor(TypeBoundKind.UPPER_BOUND,Set.of(String.class));
        assertThat(descriptor1.getTypeIdKind()).isEqualTo(TypeIdKind.CLASS);

        ITypeBoundsDescriptor descriptor2 = new TypeBoundsDescriptor(TypeBoundKind.UPPER_BOUND, "T");
        assertThat(descriptor2.getTypeIdKind()).isEqualTo(TypeIdKind.TYPE_VARIABLE_NAME);
    }

    @Test
    @SuppressWarnings("DataFlowIssue" /* Intentional contract immutability violation for test */)
    public void testGetBoundClassesImmutability()
    {
        ITypeBoundsDescriptor descriptor = new TypeBoundsDescriptor(TypeBoundKind.UPPER_BOUND,Set.of(String.class));
        assertThatThrownBy(() -> descriptor.getBoundClasses().add(Integer.class));
    }
}
