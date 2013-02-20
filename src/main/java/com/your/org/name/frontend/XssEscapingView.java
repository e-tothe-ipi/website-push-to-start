package com.your.org.name.frontend;

import org.springframework.beans.BeanUtils;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyDescriptor;
import java.util.*;

/*
 *   From github.com/e-tothe-ipi
 *   Copyright (c) 2013, Benjamin Bercovitz
 *   All rights reserved.
 *
 *   Redistribution and use in source and binary forms, with or without
 *   modification, are permitted provided that the following conditions are met:
 *
 *       Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *       Redistributions in binary form must reproduce the above copyright notice,
 *       this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *
 *   THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 *   AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 *   IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 *   ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 *   LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *   CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 *   SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 *   INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 *   CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 *   ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 *   THE POSSIBILITY OF SUCH DAMAGE.
 */

public class XssEscapingView extends JstlView {


    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        for (String key : new HashSet<>(model.keySet())) {
            model.put(key, escapeHelper(model.get(key), new HashSet<Object>()));
        }
        super.renderMergedOutputModel(model, request, response);
    }

    protected String escape(String input) {
        return HtmlUtils.htmlEscape(input);
    }

    protected Object escapeHelper(Object input, Set<Object> seen) throws Exception {
        if (input == null) {
            return null;
        }
        if (seen.contains(input)) {
            return input;
        }
        seen.add(input);
        if (input instanceof String) {
            return escape((String) input);
        } else if (input instanceof Collection) {
            Collection<?> collection = (Collection<?>) input;
            Collection<Object> out = new ArrayList<>();
            for (Object item : collection) {
                out.add(escapeHelper(item, seen));
            }
            return out;
        } else if (input instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) input;
            Map<Object, Object> output = new HashMap<>();
            for (Map.Entry<?, ?> e : map.entrySet()) {
                output.put(e.getKey(), escapeHelper(e.getValue(), seen));
            }
            return output;
        } else if (input.getClass().getName().startsWith(getNamePrefix())) {
            PropertyDescriptor[] properties = BeanUtils.getPropertyDescriptors(input.getClass());
            Object output = input.getClass().newInstance();
            for (PropertyDescriptor property : properties) {
                if (property.getWriteMethod() != null && property.getReadMethod() != null) {
                    property.getWriteMethod().invoke(output, escapeHelper(property.getReadMethod().invoke(input), seen));
                } else if (property.getReadMethod() != null && !property.getName().equals("class") && !property.getPropertyType().isPrimitive()) {
                    System.err.println("WARNING: " + input.getClass().getName() + " " + property.getName() + " does not have a setter. Content will not be escaped.");
                }
            }
            return output;
        } else {
            return input;
        }
    }

    public String getNamePrefix() {
        return "com.your.org.name";
    }


}
