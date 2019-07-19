package com.auth.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.auth.Application;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class RealNameControllerTest {

	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@Test
	public void postRealnameAuthTest() {
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		headers.add("userId", "2");
		headers.add("token", "x1");
		
		//application/x-www-form-urlencoded post传参需要MultiValueMap或者实体对象，不能写Map<>
		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("idcard", "1");
		body.add("name", "1");
		
		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);
		ResponseEntity<String> resp = testRestTemplate.postForEntity("/realname", request, String.class);
		System.out.println(resp.getStatusCodeValue());
		System.out.println(resp.getBody());
	}
}
