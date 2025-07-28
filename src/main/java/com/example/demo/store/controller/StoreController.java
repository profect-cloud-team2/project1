package com.example.demo.store.controller;

//
// import com.example.demo.store.dto.StoreResponseDto;
// import com.example.demo.store.entity.StoreEntity;
// import com.example.demo.store.service.StoreService;
//
// import lombok.RequiredArgsConstructor;
//
// @RestController
// @RequestMapping("/api/store")
// @RequiredArgsConstructor
// public class StoreController {
//
// 	private final StoreService storeService;
//
// 	@PostMapping
// 	public ResponseEntity<?> registerStore(@RequestBody StoreEntity store) {
// 		try {
// 			StoreEntity saved = storeService.createStore(store);
//
// 			StoreResponseDto response = new StoreResponseDto(
// 				saved.getName(),
// 				saved.getCategory(),
// 				saved.getAddress(),        // address1 + address2 조합
// 				saved.getAiDescription()
// 			);
//
// 			return ResponseEntity.status(201).body(response);
// 		} catch (IllegalArgumentException e) {
// 			return ResponseEntity.status(409).body(e.getMessage());
// 		}
// 	}
// }
