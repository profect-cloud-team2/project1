package com.example.demo.search.exception;

public class SearchException extends RuntimeException {

	public SearchException() {
		super("검색어를 입력해주세요.");
	}
}
