package com.andre.nfcontrol.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

@Component
public class LogRequestFilter extends GenericFilterBean {
	
    private static final Logger log = LogManager.getLogger(LogRequestFilter.class);

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		String path = req.getRequestURI().substring(req.getContextPath().length());
	    String contentType = request.getContentType();
	    log.debug("Request URL path: {}, Content type: {}", path, contentType);
	    log.debug("Cors Origin {}", req.getHeader("origin"));
	    
	    res.setHeader("Access-Control-Allow-Origin", req.getHeader("origin"));
	    res.setHeader("Access-Control-Allow-Credentials", "true");
	    res.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, PATCH, DELETE");
	    res.setHeader("Access-Control-Allow-Headers", "Access-Control-Allow-Headers, Origin,Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers,X-Access-Token,XKey,Authorization");

	    
	    if("OPTIONS".equalsIgnoreCase(req.getMethod()))
	    	res.setStatus(HttpServletResponse.SC_NO_CONTENT);
	    
	    log.debug("Status {} - Method {}", res.getStatus(), req.getMethod());
	    
	    filterChain.doFilter(request, response);
	    
	}
}
