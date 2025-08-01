package com.example.demo.payment;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.payment.client.TossPaymentClient;
import com.example.demo.payment.controller.PaymentController;
import com.example.demo.payment.dto.CancelPaymentReq;
import com.example.demo.payment.dto.DirectPaymentReq;
import com.example.demo.payment.service.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;

// @WebMvcTest(value = PaymentController.class, excludeAutoConfiguration = {
// 	org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
// 	org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration.class
// })
// class PaymentControllerTest {
//
// 	@Autowired
// 	private MockMvc mockMvc;
//
// 	@MockitoBean
// 	private PaymentService paymentService;
//
// 	@MockitoBean
// 	private TossPaymentClient tossPaymentClient;
//
// 	@MockitoBean
// 	private com.example.demo.global.security.JwtUtil jwtUtil;
//
// 	@MockitoBean
// 	private com.example.demo.global.security.JwtAuthenticationFilter jwtAuthenticationFilter;
//
// 	@Autowired
// 	private ObjectMapper objectMapper;
//
// 	@Test
// 	void 즉시결제_성공_응답_검증() throws Exception {
// 		// given
// 		UUID testOrderId = UUID.fromString("f05c4c24-bc93-45a9-9f59-3c3e8b912345");
// 		DirectPaymentReq request = new DirectPaymentReq();
// 		request.setAmount(10000);
// 		request.setOrderId(testOrderId);
// 		request.setOrderName("아메리카노 1잔");
// 		request.setCardNumber("1234567812345678");
// 		request.setCardExpirationYear("25");
// 		request.setCardExpirationMonth("12");
// 		request.setCvc("123");
// 		request.setCustomerIdentityNumber("880101");
//
// 		System.out.println("📝 [요청 데이터]: " + objectMapper.writeValueAsString(request));
//
// 		// when & then - 결제 완료 응답 검증
// 		mockMvc.perform(post("/api/payment/direct")
// 				.contentType(MediaType.APPLICATION_JSON)
// 				.content(objectMapper.writeValueAsString(request)))
// 			.andDo(print())
// 			.andExpect(status().isOk())
// 			// 기대하는 JSON 구조 검증
// 			.andExpect(jsonPath("$.paymentKey").exists())
// 			.andExpect(jsonPath("$.paymentKey").isString())
// 			.andExpect(jsonPath("$.orderId").value(testOrderId.toString()))
// 			.andExpect(jsonPath("$.amount").isNumber())
// 			.andExpect(jsonPath("$.method").value("카드"))
// 			.andExpect(jsonPath("$.approvedAt").exists())
// 			.andExpect(jsonPath("$.approvedAt").isString())
// 			// cardCompany는 null일 수 있음
// 			.andExpect(jsonPath("$.cardCompany").exists())
// 			.andDo(result -> {
// 				String response = result.getResponse().getContentAsString();
// 				System.out.println("✅ [결제 완료 응답]: " + response);
//
// 				// JSON 파싱하여 세부 내용 출력
// 				try {
// 					com.fasterxml.jackson.databind.JsonNode jsonNode = objectMapper.readTree(response);
// 					System.out.println("💳 paymentKey: " + jsonNode.get("paymentKey").asText());
// 					System.out.println("📝 orderId: " + jsonNode.get("orderId").asText());
// 					System.out.println("💰 amount: " + jsonNode.get("amount").asInt());
// 					System.out.println("💳 method: " + jsonNode.get("method").asText());
// 					System.out.println("🏦 cardCompany: " + (jsonNode.get("cardCompany").isNull() ? "null" : jsonNode.get("cardCompany").asText()));
// 					System.out.println("⏰ approvedAt: " + jsonNode.get("approvedAt").asText());
// 				} catch (Exception e) {
// 					System.out.println("❌ JSON 파싱 실패: " + e.getMessage());
// 				}
// 			});
// 	}
//
// 	@Test
// 	void 결제취소_성공_응답_검증() throws Exception {
// 		// given
// 		String paymentKey = "tviva20250730100356lszd5";
// 		CancelPaymentReq request = new CancelPaymentReq(paymentKey, "고객 요청");
// 		request.setCancelAmount(10000);
//
// 		System.out.println("📝 [취소 요청]: " + objectMapper.writeValueAsString(request));
//
// 		// when & then - 취소 완료 응답 검증
// 		mockMvc.perform(post("/api/payment/" + paymentKey + "/cancel")
// 				.contentType(MediaType.APPLICATION_JSON)
// 				.content(objectMapper.writeValueAsString(request)))
// 			.andDo(print())
// 			.andExpect(status().isOk())
// 			.andDo(result -> {
// 				String response = result.getResponse().getContentAsString();
// 				System.out.println("✅ [취소 완료 응답]: " + response);
// 				System.out.println("📝 [응답 길이]: " + response.length());
//
// 				// 취소 응답이 JSON인지 문자열인지 확인
// 				if (response.startsWith("{")) {
// 					System.out.println("📝 [JSON 형태 취소 응답]");
// 				} else {
// 					System.out.println("📝 [문자열 형태 취소 응답]: " + response);
// 				}
// 			});
// 	}
//}
