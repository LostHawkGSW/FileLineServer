package results;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BaseResult<T> {
	@Length(max = 3)
	private long code;
	private T data;
	
	public BaseResult() {}
	  
	public BaseResult(long code, T data) {
		this.code = code;
		this.data = data;
	}
	
	@JsonProperty
	public long getCode() {
		return code;
	}
	
	@JsonProperty
	public T getData() {
		return data;
	}	
}
