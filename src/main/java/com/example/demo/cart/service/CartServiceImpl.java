package com.example.demo.cart.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.demo.cart.dto.CartItemAddReq;
import com.example.demo.cart.dto.CartItemRes;
import com.example.demo.cart.dto.CartRes;
import com.example.demo.cart.entity.CartEntity;
import com.example.demo.cart.entity.CartItemEntity;
import com.example.demo.cart.exception.CartItemNotFoundException;
import com.example.demo.cart.exception.CartNotFoundException;
import com.example.demo.cart.exception.InvalidQuantityException;
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

	@Override
	@Transactional
	public void addItemToCart(CartItemAddReq req, UUID userId) {

		UserEntity user = userRepository.findById(userId)
			.orElseThrow(() -> new CartNotFoundException());

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
				throw new InvalidQuantityException();
			}
		}

		for (CartItemAddReq.MenuItem item : req.getMenuItems()) {
			MenuEntity menu = menuRepository.findById(item.getMenuId())
				.orElseThrow(() -> new CartItemNotFoundException());

			cartItemRepository.findByCartCartIdAndMenuMenuId(cart.getCartId(), item.getMenuId())
				.ifPresentOrElse(existing -> {
					existing.setQuantity(existing.getQuantity() + item.getQuantity());
					existing.setPrice(menu.getPrice());
					existing.setUpdatedAt(LocalDateTime.now());
					existing.setUpdatedBy(userId);
					cartItemRepository.save(existing);
				}, () -> {
					CartItemEntity newItem = new CartItemEntity();
					newItem.setCart(cart);
					newItem.setMenu(menu);
					newItem.setQuantity(item.getQuantity());
					newItem.setPrice(menu.getPrice());
					newItem.setCreatedAt(LocalDateTime.now());
					newItem.setCreatedBy(userId);
					cartItemRepository.save(newItem);
				});
		}
	}

	@Override
	@Transactional
	public List<CartRes> getMyCart(UUID userId) {
		if (userId == null) {
			throw new CartNotFoundException();
		}

		UserEntity user = userRepository.findById(userId)
			.orElseThrow(() -> new CartNotFoundException());

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
	public void updateQuantity(UUID cartItemId, int quantity, UUID userId) {
		if (quantity <= 0) {
			throw new InvalidQuantityException();
		}
		CartItemEntity item = cartItemRepository.findById(cartItemId)
			.orElseThrow(() -> new CartItemNotFoundException());
		item.setQuantity(quantity);
		item.setUpdatedAt(LocalDateTime.now());
		item.setUpdatedBy(userId);
	}

	@Override
	@Transactional
	public void deleteItem(UUID cartItemId, UUID userId) {
		CartItemEntity item = cartItemRepository.findById(cartItemId)
			.orElseThrow(() -> new CartItemNotFoundException());
		cartItemRepository.deleteById(cartItemId);
	}

	@Transactional
	public UUID createOrderFromCart(List<UUID> cartItemIds, UUID storeId, String requestMessage, UUID userId) {

		List<CartItemEntity> cartItems = cartItemRepository.findAllById(cartItemIds);

		int totalPrice = cartItems.stream()
			.mapToInt(item -> item.getPrice() * item.getQuantity())
			.sum();

		OrderEntity order = OrderEntity.builder()
			.orderId(UUID.randomUUID())
			.user(userRepository.findById(userId).orElseThrow())
			.store(storeRepository.findById(storeId).orElseThrow())
			.totalPrice(totalPrice)
			.requestMessage(requestMessage)
			.orderStatus(OrderStatus.RECEIVED)
			.createdAt(LocalDateTime.now())
			.createdBy(userId)
			.build();

		orderRepository.save(order);

		for (CartItemEntity cartItem : cartItems) {
			OrderItemEntity orderItem = OrderItemEntity.builder()
				.order(order)
				.menu(cartItem.getMenu())
				.quantity(cartItem.getQuantity())
				.price(cartItem.getPrice())
				.build();
			orderItemRepository.save(orderItem);
		}

		cartItemRepository.deleteAll(cartItems);

		return order.getOrderId();
	}
}
