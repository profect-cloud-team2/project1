package com.example.demo.store.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class StoreEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String address1;
	private String address2;
	private String category;
	private String aiDescription;

	private boolean deleted = false;

	public String getAddress() {
		return address1 + " " + address2;
	}
}

