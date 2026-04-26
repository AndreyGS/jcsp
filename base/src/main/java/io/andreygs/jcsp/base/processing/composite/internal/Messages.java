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

package io.andreygs.jcsp.base.processing.composite.internal;

import io.andreygs.jcsp.base.common.internal.ResourceMessagesLoader;

/**
 * TODO: place description here
 */
final class Messages
{
    public static String CspStatus_Error_in_struct_format_Class__0__cannot_be_implementation_of_class__1;
    public static String CspStatus_Error_in_struct_format_Class__0__cannot_override_class__1;
    public static String CspStatus_Error_in_struct_format_Collection_has_invalid_type_arguments_number;
    public static String CspStatus_Error_in_struct_format_Map_has_invalid_type_arguments_number;
    public static String CspStatus_Error_in_struct_format_Primitive__0__is_not_supported;
    public static String CspStatus_Error_in_struct_format_Property__0__for_struct__1__not_set;
    public static String CspStatus_Error_in_struct_format_Unbound_wildcard_type_cannot_be_processed;
    public static String CspStatus_Error_in_struct_format_Unknown_type_category;
    public static String CspStatus_Error_in_struct_format_Wildcard_type_cannot_be_overridden_by_primitive;
    public static String CspStatus_Error_in_struct_format_Wildcard_type_cannot_be_overridden_by_this_type_using_annotation;

    static
    {
        ResourceMessagesLoader.loadMessages(Messages.class);
    }
}
