package com.github.parfenovvs.networkexamplelevelup.entities;



public class ResponseContainer<T> {
	private String requestId;
	private long ts;
	private T payload;

	public String getRequestId() {
		return requestId;
	}

	public long getTs() {
		return ts;
	}

	public T getPayload() {
		return payload;
	}
}
