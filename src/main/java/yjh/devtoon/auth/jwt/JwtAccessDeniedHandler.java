package yjh.devtoon.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import yjh.devtoon.common.exception.ErrorData;
import yjh.devtoon.common.response.ApiResponse;
import yjh.devtoon.common.utils.DateFormatter;
import java.io.IOException;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

   private final ObjectMapper objectMapper;

   public JwtAccessDeniedHandler(final ObjectMapper objectMapper) {
      this.objectMapper = objectMapper;
   }

   @Override
   public void handle(
           final HttpServletRequest request,
           final HttpServletResponse response,
           final AccessDeniedException accessDeniedException
   ) throws IOException {
      //필요한 권한이 없이 접근하려 할때 403

      int statusCode = HttpStatus.FORBIDDEN.value();
      String currentTime = DateFormatter.getCurrentDateTime();
      String message = HttpStatus.FORBIDDEN.toString();
      String detailMessage = accessDeniedException.getMessage();
      ErrorData errorData = new ErrorData(statusCode, currentTime, message, detailMessage);

      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      response.setStatus(401);
      response.getWriter().write(objectMapper.writeValueAsString(ApiResponse.error(errorData)));
   }
}
