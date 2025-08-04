package com.example.demo.store.dto;

import com.example.demo.store.entity.StoreStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StoreStatusChangeRequestDto {

	@Schema(description = "변경할 가게 상태", example = "OPEN")
	@NotNull(message = "변경할 상태는 필수입니다.")
	private StoreStatus newStatus;
}
