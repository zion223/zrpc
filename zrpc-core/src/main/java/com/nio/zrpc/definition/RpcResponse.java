package com.nio.zrpc.definition;

import java.io.Serializable;

public class RpcResponse implements Serializable{


	private static final long serialVersionUID = -2682217078412263710L;
	private String requestId;
    private Throwable error;
    private Object result;
	public RpcResponse(String requestId, Throwable error, Object result) {
		super();
		this.requestId = requestId;
		this.error = error;
		this.result = result;
	}
	public RpcResponse() {
		super();
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public Throwable getError() {
		return error;
	}
	public void setError(Throwable error) {
		this.error = error;
	}
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
}
