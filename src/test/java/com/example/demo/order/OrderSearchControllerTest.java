// package com.example.demo.order;
//
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
// import java.util.Collections;
// import java.util.UUID;
//
// import org.junit.jupiter.api.Test;
// import org.mockito.Mockito;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageImpl;
// import org.springframework.data.domain.PageRequest;
// import org.springframework.data.domain.Pageable;
// import org.springframework.test.web.servlet.MockMvc;
//
// import com.example.demo.order.controller.OrderSearchController;
// import com.example.demo.order.entity.OrderEntity;
// import com.example.demo.order.service.OrderSearchService;
//
// @WebMvcTest(OrderSearchController.class)
// class OrderSearchControllerTest {
//
// 	@Autowired
// 	private MockMvc mockMvc;
//
// 	@MockBean
// 	private OrderSearchService orderSearchService;
//
// 	@Test
// 	void getOrdersByStore_NoOrders_ReturnsEmptyPageWithMessage() throws Exception {
// 		// given
// 		UUID storeId = UUID.randomUUID();
// 		Pageable pageable = PageRequest.of(0, 10);
// 		Page<OrderEntity> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
//
// 		Mockito.when(orderSearchService.getOrderByStore(storeId, pageable))
// 			.thenReturn(emptyPage);
//
// 		// when & then
// 		mockMvc.perform(get("/orders/store/{storeId}", storeId)
// 				.param("page", "0")
// 				.param("size", "10"))
// 			.andExpect(status().isOk());
// 	}
// }
