package org.ybiquitous.messages.sample;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.ybiquitous.messages.MessageLocaleHolder;

public class Filter implements javax.servlet.Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
            FilterChain chain) throws IOException, ServletException {

        req.setCharacterEncoding("UTF-8");
        res.setCharacterEncoding("UTF-8");

        HttpServletRequest httpReq = (HttpServletRequest) req;
        String lang = httpReq.getParameter("lang");
        if (lang != null && !lang.isEmpty()) {
            MessageLocaleHolder.set(new Locale(lang));
        }
        httpReq.getSession().setAttribute("lang", MessageLocaleHolder.get().getLanguage());

        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        // TODO Auto-generated method stub

    }

}
