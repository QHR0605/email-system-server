package com.example.server;

import com.example.server.config.SpringContextConfig;
import com.example.server.entity.Email;
import com.example.server.mapper.MailMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ServerApplicationTests {

	@Test
	void contextLoads() {
		MailMapper mailMapper = SpringContextConfig.getBean(MailMapper.class);
		List<Email> emails = mailMapper.findMailsByRcpt("qhr666@lyq.com");
		System.out.println(emails.toString());
	}

}
