package com.andre.nfcontrol.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.mail.internet.MimeMessage;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.andre.nfcontrol.exceptions.ProjectException;
import com.andre.nfcontrol.service.MailService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class MailServiceImpl implements MailService {

	@Value("${email_username}")
	private String username;

	@Value("${email_password}")
	private String password;

	private JavaMailSender mailSender;
	
	private JavaMailSender getMailSender() {
		JavaMailSenderImpl sender = new JavaMailSenderImpl();
		sender.setProtocol("smtp");
		sender.setHost("smtp.gmail.com");
		
		sender.setPort(587);
		sender.setUsername(username);
		sender.setPassword(password);

		Properties mailProps = new Properties();

		mailProps.put("mail.smtp.auth", "true");
		mailProps.put("mail.smtp.starttls.enable", "true");
		mailProps.put("mail.smtp.starttls.required", "false");
		mailProps.put("mail.smtp.ssl.enable", "false");

		sender.setJavaMailProperties(mailProps);

		return sender;
	}

	@Override
	public void send(String to, String subject, String htmlBody, Map<String, String> imgs) {
		try {
			mailSender = this.getMailSender();
			MimeMessage mail = mailSender.createMimeMessage();
			mail.setContentID("<logo>");
			
			MimeMessageHelper messageHelper = new MimeMessageHelper(mail, true, "utf-8");
			messageHelper.setFrom(username);
			messageHelper.setTo(to);
			messageHelper.setSubject(subject);
			messageHelper.setText(htmlBody, true);
			
			if (htmlBody.contains("img src=")) {
				
				for (Entry<String, String> img : imgs.entrySet()) {
					FileSystemResource res = this.getFileSystemResource(img.getValue());
					messageHelper.addInline(img.getKey(), res, "image/png");
				}
			}
			
			mailSender.send(mail);
		} catch (Exception e) {
			throw new ProjectException(e.getMessage(), e);
		}
	}
	
	private FileSystemResource getFileSystemResource(String fileSrc) throws IOException {
		File file = new File(fileSrc);
		byte[] bytes = FileUtils.readFileToByteArray(file);
		
		FileOutputStream fos = new FileOutputStream(file);
        fos.write(bytes);
        fos.close();
		
        FileSystemResource res = new FileSystemResource(file);
        
        return res;
	}

	public String getTextHtml(String fileSrc) {
		try {
			byte[] bytes = Files.readAllBytes(Paths.get(fileSrc));
			String text = new String(bytes, StandardCharsets.UTF_8);
			return text;
		} catch (IOException e) {
			throw new ProjectException(e.getMessage(), e);
		}
	}

}
