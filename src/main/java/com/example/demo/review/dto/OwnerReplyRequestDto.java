package com.example.demo.review.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OwnerReplyRequestDto {
	private String replyContent;

	public OwnerReplyRequestDto(String replyContent) {
		this.replyContent = replyContent;
	}
}
