package com.example.UTSAPlaceBackend.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.function.Predicate;

/**
 * Validator class defines open endpoints that do not need authentication filtering.
 * */
@Component
public class RouterValidator {

    public static final List<String> openEndpoints = List.of(
            "auth/register",
            "auth/login"
    );

    public Predicate<HttpServletRequest> isSecured = request ->
            openEndpoints.stream()
                    .noneMatch(uri -> request.getRequestURI().contains(uri));
}
