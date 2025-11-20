package com.sparta.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Repository{

	@JsonProperty("homepageUrl")
	private String homepageUrl;

	@JsonProperty("visibility")
	private String visibility;

	@JsonProperty("name")
	private String name;

	@JsonProperty("description")
	private String description;

	@JsonProperty("id")
	private String id;

	@JsonProperty("updatedAt")
	private String updatedAt;

	public String getHomepageUrl(){
		return homepageUrl;
	}

	public String getVisibility(){
		return visibility;
	}

	public String getName(){
		return name;
	}

	public String getDescription(){
		return description;
	}

	public String getId(){
		return id;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}
}