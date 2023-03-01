package com.andre.nfcontrol.service;

import java.util.Map;

public interface MailService {
	
	void send(String to, String subject, String htmlBody, Map<String, String> imgs);
	
	String getTextHtml(String fileSrc);
	
}
