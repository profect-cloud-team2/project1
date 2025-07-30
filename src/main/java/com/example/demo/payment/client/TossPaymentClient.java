package com.example.demo.payment.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;
import java.util.stream.Collectors;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.demo.payment.dto.CancelPaymentReq;
import com.example.demo.payment.dto.CancelPaymentRes;
import com.example.demo.payment.dto.CheckoutPaymentReq;
import com.example.demo.payment.dto.CheckoutPaymentRes;
import com.example.demo.payment.dto.ConfirmPaymentRes;
import com.example.demo.payment.dto.DirectPaymentReq;
import com.example.demo.payment.dto.DirectPaymentRes;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class TossPaymentClient {

	private final ObjectMapper objectMapper;

	@Value("${payment.toss.secret-key}")
	private String secretKey;

	@Value("${payment.toss.api-url}")
	private String baseurl;

	@PostConstruct
	public void init() {
		System.out.println("System.out: Secret Key = " + secretKey);
		System.out.println("System.out: API URL = " + baseurl);
		log.info("[Toss] Secret Key Loaded: {}", secretKey);
		log.info("[Toss] Base URL Loaded: {}", baseurl);
	}

	// 즉시결제
	public DirectPaymentRes requestDirectPayment(DirectPaymentReq req) throws IOException {
		URL url = new URL(baseurl + "/v1/payments/key-in");
		HttpURLConnection conn = createConnection(url);

		JSONObject body = new JSONObject();
		body.put("amount", req.getAmount());
		body.put("orderId", req.getOrderId().toString());
		body.put("orderName", req.getOrderName());
		body.put("cardNumber", req.getCardNumber());
		body.put("cardExpirationYear", req.getCardExpirationYear());
		body.put("cardExpirationMonth", req.getCardExpirationMonth());
		body.put("cvc", req.getCvc());
		body.put("customerIdentityNumber", req.getCustomerIdentityNumber());

		writeBody(conn, body);

		log.info("Toss 요청 바디: {}", body.toJSONString());
		String response = readResponse(conn);
		return objectMapper.readValue(response, DirectPaymentRes.class);
	}

	// 결제 취소
	public CancelPaymentRes requestCancelPayment(CancelPaymentReq req) throws IOException {
		URL url = new URL(baseurl + "/v1/payments/" + req.getPaymentKey() + "/cancel");
		HttpURLConnection conn = createConnection(url);

		JSONObject body = new JSONObject();
		body.put("paymentKey", req.getPaymentKey());
		body.put("cancelReason", req.getCancelReason());
		body.put("cancelAmount", req.getCancelAmount());

		writeBody(conn, body);

		log.info("Toss 요청 바디: {}", body.toJSONString());

		String response = readResponse(conn);
		return objectMapper.readValue(response, CancelPaymentRes.class);
	}

	// 인증결제
	public CheckoutPaymentRes requestCheckoutPayment(CheckoutPaymentReq req) throws IOException {
		URL url = new URL(baseurl + "/v1/payments");
		HttpURLConnection conn = createConnection(url);

		JSONObject body = new JSONObject();
		body.put("amount", req.getAmount());
		body.put("orderId", req.getOrderId().toString());
		body.put("orderName", req.getOrderName());
		body.put("successUrl", "http://localhost:8080/api/payment/success");
		body.put("failUrl", "http://localhost:8080/api/payment/fail");
		body.put("customerEmail", req.getCustomerEmail());

		log.info("[요청 URL]: {}", url);
		log.info("[요청 Body]: {}", body.toJSONString());

		writeBody(conn, body);

		String response = readResponse(conn);
		log.info("[응답]: {}", response);
		return objectMapper.readValue(response, CheckoutPaymentRes.class);
	}

	// 결제 승인
	public ConfirmPaymentRes confirmPayment(String paymentKey, UUID orderId, int amount) throws IOException {
		URL url = new URL(baseurl + "/v1/payments/confirm");
		HttpURLConnection conn = createConnection(url);

		JSONObject body = new JSONObject();
		body.put("paymentKey", paymentKey);
		body.put("orderId", orderId);
		body.put("amount", amount);

		writeBody(conn, body);

		String response = readResponse(conn);
		return objectMapper.readValue(response, ConfirmPaymentRes.class);
	}

	// 연결 생성
	private HttpURLConnection createConnection(URL url) throws IOException {
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);

		String authHeader = "Basic " + encodeBase64(secretKey + ":");
		conn.setRequestProperty("Authorization", authHeader);
		conn.setRequestProperty("Content-Type", "application/json");

		return conn;
	}

	// 요청 본문 쓰기
	private void writeBody(HttpURLConnection conn, JSONObject body) throws IOException {
		try (OutputStream os = conn.getOutputStream()) {
			os.write(body.toJSONString().getBytes(StandardCharsets.UTF_8));
		}
	}

	// 응답 읽기
	private String readResponse(HttpURLConnection conn) throws IOException {
		BufferedReader reader;
		if (conn.getResponseCode() >= 400) {
			reader = new BufferedReader(
				new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8)
			);
		} else {
			reader = new BufferedReader(
				new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8)
			);
		}
		String response = reader.lines().collect(Collectors.joining());
		log.info("[HTTP Status]: {}", conn.getResponseCode());
		log.info("[HTTP Response Body]: {}", response);
		return response;
	}

	//Base64 인코딩
	private String encodeBase64(String value) {
		return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
	}
}

