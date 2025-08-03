package com.example.demo.cart.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.demo.cart.dto.CartItemAddReq;
import com.example.demo.cart.dto.CartItemRes;
import com.example.demo.cart.dto.CartRes;
import com.example.demo.cart.entity.CartEntity;
import com.example.demo.cart.entity.CartItemEntity;
import com.example.demo.cart.repository.CartItemRepository;
import com.example.demo.cart.repository.CartRepository;
import com.example.demo.menus.entity.MenuEntity;
import com.example.demo.menus.repository.MenuRepository;
import com.example.demo.order.entity.OrderEntity;
import com.example.demo.order.entity.OrderItemEntity;
import com.example.demo.order.entity.OrderStatus;
import com.example.demo.order.repository.OrderItemRepository;
import com.example.demo.order.repository.OrderRepository;
import com.example.demo.store.repository.StoreRepository;
import com.example.demo.user.entity.UserEntity;
import com.example.demo.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

	private final CartRepository cartRepository;
	private final MenuRepository menuRepository;
	private final UserRepository userRepository;
	private final CartItemRepository cartItemRepository;
	private final OrderRepository orderRepository;
	private final OrderItemRepository orderItemRepository;
	private final StoreRepository storeRepository;

	private UUID getCurrentUserId() {
		String userIdStr = SecurityContextHolder
			.getContext()
			.getAuthentication()
			.getName();
		
		if ("anonymousUser".equals(userIdStr) || userIdStr == null) {
			return null;
		}
		
		return UUID.fromString(userIdStr);
	}

	@Override
	@Transactional
	public void addItemToCart(CartItemAddReq req) {
		UUID userId = getCurrentUserId();

		if (userId == null) {
			System.out.println("JWT에서 추출한 userId가 null입니다");
		}

		UserEntity user = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

		CartEntity cart = cartRepository.findWithItemsByUserId(userId)
			.orElseGet(() -> {
				CartEntity newCart = new CartEntity();
				newCart.setUser(user);
				newCart.setCreatedAt(LocalDateTime.now());
				newCart.setCreatedBy(userId);
				newCart.setItems(new ArrayList<>());
				return cartRepository.save(newCart);
			});

		boolean hasStore = cart.getItems() != null && !cart.getItems().isEmpty();
		if (hasStore) {
			UUID currentStoreId = cart.getItems().get(0).getMenu().getStore().getStoreId();
			if (!currentStoreId.equals(req.getStoreId())) {
				throw new IllegalArgumentException("장바구니에는 하나의 가게 메뉴만 담을 수 있습니다. 기존 장바구니를 비워주세요.");
			}
		}

		for (CartItemAddReq.MenuItem item : req.getMenuItems()) {
			MenuEntity menu = menuRepository.findById(item.getMenuId())
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴입니다."));

			cartItemRepository.findByCartCartIdAndMenuMenuId(cart.getCartId(), item.getMenuId())
				.ifPresentOrElse(existing -> {
					existing.setQuantity(existing.getQuantity() + item.getQuantity());
					existing.setPrice(menu.getPrice()); // DB의 실제 가격 사용
					existing.setUpdatedAt(LocalDateTime.now());
					existing.setUpdatedBy(userId);
					cartItemRepository.save(existing);
				}, () -> {
					CartItemEntity newItem = new CartItemEntity();
					newItem.setCart(cart);
					newItem.setMenu(menu);
					newItem.setQuantity(item.getQuantity());
					newItem.setPrice(menu.getPrice()); // DB의 실제 가격 사용
					newItem.setCreatedAt(LocalDateTime.now());
					newItem.setCreatedBy(userId);
					cartItemRepository.save(newItem);
				});
		}
	}

	@Override
	@Transactional
	public List<CartRes> getMyCart() {
		UUID userId = getCurrentUserId();
		
		if (userId == null) {
			throw new IllegalArgumentException("로그인이 필요합니다.");
		}

		UserEntity user = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

		CartEntity cart = cartRepository.findWithItemsByUserId(userId)
			.orElseGet(() -> {
				CartEntity newCart = new CartEntity();
				newCart.setUser(user);
				newCart.setCreatedAt(LocalDateTime.now());
				newCart.setCreatedBy(userId);
				newCart.setItems(new ArrayList<>());
				return cartRepository.save(newCart);
			});

		CartRes res = new CartRes();
		res.setCartId(cart.getCartId());

		List<CartItemRes> itemRes = cart.getItems().stream().map(entity -> {
			CartItemRes dto = new CartItemRes();
			dto.setCartItemId(entity.getCartItemId());
			dto.setMenuId(entity.getMenu().getMenuId());
			dto.setMenuName(entity.getMenu().getName());
			dto.setQuantity(entity.getQuantity());
			dto.setPrice(entity.getPrice());
			return dto;
		}).toList();

		res.setItems(itemRes);
		return List.of(res);
	}

	@Override
	@Transactional
	public void updateQuantity(UUID cartItemId, int quantity) {
		CartItemEntity item = cartItemRepository.findById(cartItemId)
			.orElseThrow(() -> new IllegalArgumentException("장바구니에 항목이 존재하지 않습니다."));
		item.setQuantity(quantity);
		item.setUpdatedAt(LocalDateTime.now());
		item.setUpdatedBy(getCurrentUserId());
	}

	@Override
	@Transactional
	public void deleteItem(UUID cartItemId) {
		cartItemRepository.deleteById(cartItemId);
	}

	@Transactional
	public UUID createOrderFromCart(List<UUID> cartItemIds, UUID storeId, String requestMessage, UUID userId) {
		// 장바구니 아이템들 조회
		List<CartItemEntity> cartItems = cartItemRepository.findAllById(cartItemIds);
		
		// 총 금액 계산
		int totalPrice = cartItems.stream()
			.mapToInt(item -> item.getPrice() * item.getQuantity())
			.sum();

		// 주문 생성
		OrderEntity order = OrderEntity.builder()
			.orderId(UUID.randomUUID())
			.user(userRepository.findById(userId).orElseThrow())
			.store(storeRepository.findById(storeId).orElseThrow())
			.totalPrice(totalPrice)
			.requestMessage(requestMessage)
			.orderStatus(OrderStatus.주문접수)
			.createdAt(LocalDateTime.now())
			.createdBy(userId)
			.build();

		orderRepository.save(order);

		// 주문 아이템들 생성
		for (CartItemEntity cartItem : cartItems) {
			OrderItemEntity orderItem = OrderItemEntity.builder()
				.order(order)
				.menu(cartItem.getMenu())
				.quantity(cartItem.getQuantity())
				.price(cartItem.getPrice())
				.build();
			orderItemRepository.save(orderItem);
		}

		// 장바구니 아이템들 삭제
		cartItemRepository.deleteAll(cartItems);

		return order.getOrderId();
	}
}
