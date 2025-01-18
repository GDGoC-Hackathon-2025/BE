package gdg.baekya.hackathon.security.jwt.handler;

import com.nimbusds.jose.shaded.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(Map.of("error", "ACCESS DENIED"));

        response.setContentType("application/json");
        response.setStatus(HttpStatus.FORBIDDEN.value());

        PrintWriter printWriter = response.getWriter();
        printWriter.write(jsonStr);
        printWriter.close();

    }
}
