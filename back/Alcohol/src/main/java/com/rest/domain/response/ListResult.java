package com.rest.domain.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ListResult<T> extends CommonResult{
	private List<T> list;
}
