package com.apexglobal.test.orderworker.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ApiResponseErrorDto {
	
	private int code;
	private String message;
	private List<String> errors;

}

