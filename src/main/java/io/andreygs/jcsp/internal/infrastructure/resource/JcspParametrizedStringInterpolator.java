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

package io.andreygs.jcsp.internal.infrastructure.resource;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TODO: place description here
 */
public class JcspParametrizedStringInterpolator
    implements IJcspParametrizedStringInterpolator
{
    private final List<IJcspTemplateVariableValueProvider> valueProviders;

    public JcspParametrizedStringInterpolator(List<IJcspTemplateVariableValueProvider> valueProviders)
    {
        this.valueProviders = List.copyOf(valueProviders);
    }

    @Override
    public String interpolate(String sourceString)
    {
        if (!sourceString.contains("${"))
        {
            return sourceString;
        }
        String updatedString = sourceString;
        String regex = "\\$\\{([^}]+)}";
        Pattern pattern = Pattern.compile(regex);
nextTemplateVariable:
        while (updatedString.contains("${"))
        {
            Matcher matcher = pattern.matcher(updatedString);
            if (!matcher.find())
            {
                throw new IllegalArgumentException();
            }
            String templateVariable = matcher.group(1);
            for (IJcspTemplateVariableValueProvider valueProvider : valueProviders)
            {
                Optional<Object> templateVariableValue = valueProvider.provide(templateVariable);
                if (templateVariableValue.isEmpty())
                {
                    continue;
                }
                updatedString = updatedString.replaceAll("\\$\\{" + templateVariable + "}",
                    templateVariableValue.get().toString());
                continue nextTemplateVariable;
            }
            throw new IllegalArgumentException();
        }
        return updatedString;
    }
}
