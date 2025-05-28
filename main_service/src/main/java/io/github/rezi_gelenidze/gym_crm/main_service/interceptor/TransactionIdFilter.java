package io.github.rezi_gelenidze.gym_crm.main_service.interceptor;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

@Component
public class TransactionIdFilter extends OncePerRequestFilter {

    public static final String TRANSACTION_ID_HEADER = "X-Transaction-Id";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String transactionId = request.getHeader(TRANSACTION_ID_HEADER);
        if (transactionId == null || transactionId.isEmpty())
            transactionId = UUID.randomUUID().toString();

        MDC.put("transactionId", transactionId);
        response.setHeader(TRANSACTION_ID_HEADER, transactionId);

        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove("transactionId");
        }
    }
}
