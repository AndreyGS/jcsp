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

package io.andreygs.jcsp.base.types;

import io.andreygs.jcsp.base.utils.ResourceMessagesLoader;

/**
 * TODO: place description here
 */
final class Messages
{
    public static String CspCommonFlags_BigEndian;
    public static String CspCommonFlags_Bitness32;
    public static String CspCommonFlags_Bitness64;
    public static String CspCommonFlags_EndiannessDifference;
    public static String CspCommonFlags_LittleEndian;
    public static String CspCommonFlags_No_EndiannessDifference;
    public static String CspCommonFlags_Type;
    public static String CspDataFlags_AlignmentMayBeNotEqual;
    public static String CspDataFlags_AlignmentsAreEqual;
    public static String CspDataFlags_AllowUnmanagedPointers;
    public static String CspDataFlags_CheckRecursivePointers;
    public static String CspDataFlags_DoNotAllowUnmanagedPointers;
    public static String CspDataFlags_DoNotCheckRecursivePointers;
    public static String CspDataFlags_SimplyAssignableTagsOptimizationsAreAvailable;
    public static String CspDataFlags_SimplyAssignableTagsOptimizationsAreOff;
    public static String CspDataFlags_SizeOfIntegersMayBeNotEqual;
    public static String CspDataFlags_SizeOfIntegersAreEqual;
    public static String CspDataFlags_Type;
    public static String CspMessageType_Data;
    public static String CspMessageType_GetSettings;
    public static String CspMessageType_Status;
    public static String CspMessageType_Type;
    public static String CspProtocolVersion_1;
    public static String CspProtocolVersion_2;
    public static String CspProtocolVersion_Type;
    public static String CspStatus_No_Error;
    public static String CspStatus_No_Memory;
    public static String CspStatus_Type;
    public static String CspStatus_No_Such_Handler;

    static
    {
        ResourceMessagesLoader.loadMessages(Messages.class);
    }
}
