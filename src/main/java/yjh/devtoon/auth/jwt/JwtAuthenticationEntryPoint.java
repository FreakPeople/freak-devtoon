package yjh.devtoon.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.ErrorResponse;
import yjh.devtoon.common.exception.ErrorData;
import yjh.devtoon.common.response.ApiResponse;
import yjh.devtoon.common.utils.DateFormatter;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

   private final ObjectMapper objectMapper;

   public JwtAuthenticationEntryPoint(final ObjectMapper objectMapper) {
      this.objectMapper = objectMapper;
   }

   @Override
   public void commence(final HttpServletRequest request,
                        final HttpServletResponse response,
                        final AuthenticationException authException
   ) throws IOException {
      // 유효한 자격증명을 제공하지 않고 접근하려 할때 401

      int statusCode = HttpStatus.UNAUTHORIZED.value();
      String currentTime = DateFormatter.getCurrentDateTime();
      String message = HttpStatus.UNAUTHORIZED.toString();
      String detailMessage = authException.getMessage();
      ErrorData errorData = new ErrorData(statusCode, currentTime, message, detailMessage);

      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      response.setStatus(401);
      response.getWriter().write(objectMapper.writeValueAsString(ApiResponse.error(errorData)));
   }
}
