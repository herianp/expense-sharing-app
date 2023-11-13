package com.herian.expensesharingapp.config.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class CsrfCookieFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        //  Tento řádek kódu získává CsrfToken objekt z aktuálního HTTP požadavku
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        if(null != csrfToken.getHeaderName()){
            // nastaví hlavičku odpovědi s názvem hlavičky CSRF tokenu a hodnotou tokenu.
            response.setHeader(csrfToken.getHeaderName(), csrfToken.getToken());
        }
        // pošle řetězec filtrů dál, aby mohl být požadavek zpracován dalšími filtry
        filterChain.doFilter(request, response);
    }

}
