package com.example.demo.search;

import com.example.demo.review.repository.ReviewRepository;
import com.example.demo.search.dto.SearchResultDto;
import com.example.demo.search.service.SearchService;
import com.example.demo.store.entity.Category;
import com.example.demo.store.entity.StoreEntity;
import com.example.demo.store.repository.StoreRepository;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class SearchServiceTest {

	@Mock
	private ReviewRepository reviewRepository;

	@Mock
	private StoreRepository storeRepository;

	@InjectMocks
	private SearchService searchService;

	public SearchServiceTest() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void 키워드로_가게_검색_정상작동() {
		// given
		String keyword = "한솥";
		int page = 0;
		int size = 10;
		String sortBy = "created";

		UUID storeId = UUID.randomUUID();
		StoreEntity store = StoreEntity.builder()
			.storeId(storeId)
			.name("한솥도시락")
			.category(Category.KOREAN)
			.imgURL("http://image.com/abc.jpg")
			.build();

		Page<StoreEntity> storePage = new PageImpl<>(List.of(store));
		Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

		when(storeRepository.searchVisibleStoresByKeyword(keyword, pageable)).thenReturn(storePage);

		when(reviewRepository.calculateAverageRatingByStoreId(storeId)).thenReturn(Optional.of(4.3));

		Page<SearchResultDto> result = searchService.search(keyword, page, size, sortBy);

		assertThat(result.getContent()).hasSize(1);
		SearchResultDto dto = result.getContent().get(0);

		System.out.println("🔍 검색 결과:");
		System.out.println("ID: " + dto.getStoreId());
		System.out.println("이름: " + dto.getStoreName());
		System.out.println("카테고리: " + dto.getCategory());
		System.out.println("이미지 URL: " + dto.getImgURL());
		System.out.println("평점: " + dto.getAverageRating());

		assertThat(dto.getStoreName()).isEqualTo("한솥도시락");
		assertThat(dto.getCategory()).isEqualTo("한식");
		assertThat(dto.getImgURL()).isEqualTo("http://image.com/abc.jpg");
		assertThat(dto.getAverageRating()).isEqualTo(4.3);

		verify(storeRepository, times(1)).searchVisibleStoresByKeyword(keyword, pageable);
		verify(reviewRepository, times(1)).calculateAverageRatingByStoreId(storeId);
	}

	@Test
	void 검색결과_없을때_빈페이지_반환() {
		// given
		String keyword = "없는가게";
		int page = 0;
		int size = 10;
		String sortBy = "created";

		Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
		Page<StoreEntity> emptyPage = new PageImpl<>(List.of());  // 빈 결과

		when(storeRepository.searchVisibleStoresByKeyword(keyword, pageable)).thenReturn(emptyPage);

		Page<SearchResultDto> result = searchService.search(keyword, page, size, sortBy);

		System.out.println("🔍 검색 결과 없음 테스트:");
		System.out.println("검색 결과 수: " + result.getContent().size());

		assertThat(result.getContent()).isEmpty();
		assertThat(result.getTotalElements()).isEqualTo(0);

		verify(storeRepository, times(1)).searchVisibleStoresByKeyword(keyword, pageable);
	}
}
