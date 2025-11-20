package com.sparta.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateRepoResponse{

	@JsonProperty("data")
	private Data data;

	public Data getData(){
		return data;
	}
}