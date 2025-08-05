package com.example.demo.review.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class OwnerReplyRequestDto {
	private String replyContent;

	public OwnerReplyRequestDto(String replyContent) {
		this.replyContent = replyContent;
	}
}
