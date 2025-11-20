package com.sparta.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateRepository{

	@JsonProperty("repository")
	private Repository repository;

	public Repository getRepository(){
		return repository;
	}
}