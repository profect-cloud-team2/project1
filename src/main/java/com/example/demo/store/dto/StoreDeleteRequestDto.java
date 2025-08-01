package com.example.demo.store.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoreDeleteRequestDto {

	@Schema(description = "삭제 요청 사유", example = "허위 정보가 포함되어 있습니다.")
	@NotBlank(message = "삭제 사유는 필수입니다.")
	private String reason;
}
