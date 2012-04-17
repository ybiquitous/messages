package org.ybiquitous.messages.sample;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.ybiquitous.messages.ThreadLocalLocaleHolder;

public class AppFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
            FilterChain chain) throws IOException, ServletException {

        req.setCharacterEncoding("UTF-8");
        res.setCharacterEncoding("UTF-8");

        HttpServletRequest httpReq = (HttpServletRequest) req;
        String lang = httpReq.getParameter("lang");
        if (lang != null && !lang.isEmpty()) {
            ThreadLocalLocaleHolder.set(new Locale(lang));
        } else if ((lang = (String) httpReq.getSession().getAttribute("lang")) != null) {
            ThreadLocalLocaleHolder.set(new Locale(lang));
        }
        httpReq.getSession().setAttribute("lang", ThreadLocalLocaleHolder.get().getLanguage());

        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {
        // pass
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
        // pass
    }

}
