package com.sparta.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Data{

	@JsonProperty("updateRepository")
	private UpdateRepository updateRepository;

	public UpdateRepository getUpdateRepository(){
		return updateRepository;
	}
}