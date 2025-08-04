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
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class TossPaymentClient {

	private final ObjectMapper objectMapper;

	@Value("${payment.toss.secret-key}")
	private String secretKey;

	@Value("${payment.toss.client-key}")
	private String clientKey;

	@Value("${payment.toss.api-url}")
	private String baseurl;

	public CancelPaymentRes requestCancelPayment(CancelPaymentReq req) throws IOException {
		URL url = new URL(baseurl + "/v1/payments/" + req.getPaymentKey() + "/cancel");
		HttpURLConnection conn = createConnection(url);

		JSONObject body = new JSONObject();
		body.put("paymentKey", req.getPaymentKey());
		body.put("cancelReason", req.getCancelReason());
		body.put("cancelAmount", req.getCancelAmount());

		writeBody(conn, body);

		String response = readResponse(conn);
		return objectMapper.readValue(response, CancelPaymentRes.class);
	}

	public CheckoutPaymentRes requestCheckoutPayment(CheckoutPaymentReq req) throws IOException {
		CheckoutPaymentRes result = new CheckoutPaymentRes();
		result.setOrderId(req.getOrderId());
		result.setAmount(req.getAmount());
		result.setOrderName(req.getOrderName());
		result.setMessage("결제 준비 완료. 클라이언트에서 TossPayments SDK로 결제 진행하세요.");

		return result;
	}

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

	private HttpURLConnection createConnection(URL url) throws IOException {
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);

		String authHeader = "Basic " + encodeBase64(secretKey + ":");
		conn.setRequestProperty("Authorization", authHeader);
		conn.setRequestProperty("Content-Type", "application/json");

		return conn;
	}

	private void writeBody(HttpURLConnection conn, JSONObject body) throws IOException {
		try (OutputStream os = conn.getOutputStream()) {
			os.write(body.toJSONString().getBytes(StandardCharsets.UTF_8));
		}
	}

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
		return response;
	}

	private String encodeBase64(String value) {
		return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
	}
}

