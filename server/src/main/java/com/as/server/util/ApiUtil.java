package com.as.server.util;

import org.springframework.web.context.request.NativeWebRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ApiUtil {

    /**
     * Writes an example response body to the HTTP response.
     * <p>
     * Typically used in OpenAPI or Swagger-generated controllers to return example responses.
     *
     * @param request        the {@link NativeWebRequest}, from which the {@link HttpServletResponse} is obtained
     * @param contentType    the MIME type of the response (e.g., "application/json")
     * @param exampleString  the example response content to write into the response body
     * @throws RuntimeException if writing to the response fails or response is not available
     */
    public static void setExampleResponse(NativeWebRequest request, String contentType, String exampleString) {
        try {
            HttpServletResponse response = request.getNativeResponse(HttpServletResponse.class);
            if (response == null) {
                throw new IllegalStateException("HttpServletResponse is null");
            }
            if (!response.isCommitted()) {
                response.getWriter().write(exampleString);
            }
            response.setContentType(contentType);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(exampleString);
        } catch (IOException e) {
            throw new RuntimeException("Failed to set example response", e);
        }
    }
}