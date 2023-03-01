package com.andre.nfcontrol.config;

import java.util.Base64;
import java.util.regex.Pattern;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.andre.nfcontrol.utils.EncryptString;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomPasswordEncoder implements PasswordEncoder {

	private static final Integer PREFIX_ENCODING_LENGTH = 8;

	private Pattern BCRYPT_PATTERN = Pattern.compile("\\A\\$2a?\\$\\d\\d\\$[./0-9A-Za-z]{53}");
	private static final PasswordEncoder ENCODER = PasswordEncoderFactories.createDelegatingPasswordEncoder();

	private static final String KEY_PASSWORD = "nf_contr0l";

	@Override
	public String encode(CharSequence rawPassword) {
		return ENCODER.encode(rawPassword);
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		if (encodedPassword == null || encodedPassword.length() == 0) {
			log.warn("Empty encoded password");
			return false;
		}

		String password = rawPassword.toString();
		encodedPassword = encodedPassword.substring(PREFIX_ENCODING_LENGTH, encodedPassword.length());

		try {
			//Front-end: decoder e descript Password
			byte[] urlEncodeByte = Base64.getUrlDecoder().decode(password.getBytes());

			String urlEncode = new String(urlEncodeByte);
			log.debug("URL Encode: " + urlEncode);

			if (urlEncode.split("\\|").length == 2) {
				String passwordEncrypted = urlEncode.split("\\|")[0];
				String keyEncrypted = urlEncode.split("\\|")[1];

				String keyVerify = EncryptString.descriptAES(keyEncrypted);

				if (keyVerify.equals(KEY_PASSWORD))
					password = EncryptString.descriptAES(passwordEncrypted);
			}
		} catch (IllegalArgumentException e) {
			log.error(e.getMessage());
		}

		if (!BCRYPT_PATTERN.matcher(encodedPassword).matches()) {
			log.warn("Encoded password does not look like BCrypt");
			return false;
		}

		return BCrypt.checkpw(password, encodedPassword);
	}

}
