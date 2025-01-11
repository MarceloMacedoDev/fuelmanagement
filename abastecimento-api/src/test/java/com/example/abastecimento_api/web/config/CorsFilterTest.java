package com.example.abastecimento_api.web.config;

import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CorsFilterTest {

    @InjectMocks
    private CorsFilter corsFilter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain chain;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDoFilterWithOriginHeader() throws IOException, ServletException {
        when(request.getHeader("Origin")).thenReturn("http://example.com");
        when(request.getMethod()).thenReturn("GET");

        corsFilter.doFilter(request, response, chain);

        verify(response).setHeader("Access-Control-Allow-Origin", "http://example.com");
        verify(response).setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        verify(response).setHeader("Access-Control-Max-Age", "3600");
        verify(response).setHeader("Access-Control-Allow-Headers",
                "x-requested-with, authorization, Content-Type, Authorization, credential, X-XSRF-TOKEN");
        verify(response).setHeader("Access-Control-Allow-Credentials", "true");
        verify(chain).doFilter(request, response);
    }

    @Test
    void testDoFilterWithoutOriginHeader() throws IOException, ServletException {
        when(request.getHeader("Origin")).thenReturn(null);
        when(request.getMethod()).thenReturn("GET");

        corsFilter.doFilter(request, response, chain);

        verify(response).setHeader("Access-Control-Allow-Origin", "*");
        verify(response).setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        verify(response).setHeader("Access-Control-Max-Age", "3600");
        verify(response).setHeader("Access-Control-Allow-Headers",
                "x-requested-with, authorization, Content-Type, Authorization, credential, X-XSRF-TOKEN");
        verify(response).setHeader("Access-Control-Allow-Credentials", "true");
        verify(chain).doFilter(request, response);
    }

    @Test
    void testDoFilterOptionsMethod() throws IOException, ServletException {
        when(request.getHeader("Origin")).thenReturn("http://example.com");
        when(request.getMethod()).thenReturn("OPTIONS");

        corsFilter.doFilter(request, response, chain);

        verify(response).setHeader("Access-Control-Allow-Origin", "http://example.com");
        verify(response).setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        verify(response).setHeader("Access-Control-Max-Age", "3600");
        verify(response).setHeader("Access-Control-Allow-Headers",
                "x-requested-with, authorization, Content-Type, Authorization, credential, X-XSRF-TOKEN");
        verify(response).setHeader("Access-Control-Allow-Credentials", "true");
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(chain, never()).doFilter(request, response);
    }
}
