package com.rest.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rest.domain.entity.ScheduleEntity;
import com.rest.domain.entity.UserEntity;
import com.rest.domain.response.CommonResult;
import com.rest.domain.response.ListResult;
import com.rest.domain.response.ScheduleResult;
import com.rest.domain.response.SingleResult;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Service
public class ResponseService {
	
	@AllArgsConstructor
	@Getter
	public enum CommonResponse{
		SUCCESS(0, "성공하셨습니다."),
		FAIL(-1, "실패하셨습니다.");
		
		int code;
		String msg;
	}
	
	private void setSuccessResult(CommonResult result) {
        result.setSuccess(true);
        result.setCode(CommonResponse.SUCCESS.getCode());
        result.setMsg(CommonResponse.SUCCESS.getMsg());
    }
	
	public<T> SingleResult<T> getSingleResult(T data){
		SingleResult<T> result = new SingleResult<T>();
		result.setData(data);
		setSuccessResult(result);
		return result;
	}
	
	public <T> ListResult<T> getListResult(List<T> list){
		ListResult<T> result = new ListResult<>();
		result.setList(list);
		setSuccessResult(result);
		return result;
	}

	public <T> ScheduleResult<T> getScheduleResult(List<T> list, UserEntity user) {
		ScheduleResult<T> result = new ScheduleResult<>();
		result.setList(list);
		result.setUser(user);
		setSuccessResult(result);
		return result;
	}
	
	
	public CommonResult getSuccessResult() {
        CommonResult result = new CommonResult();
        setSuccessResult(result);
        return result;
    }
	

	public CommonResult getFailResult(int code, String msg) {
        CommonResult result = new CommonResult();
        result.setSuccess(false);
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}
