package io.penguinstats.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.penguinstats.enums.Server;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "item")
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "The model of an item.")
public class Item implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@JsonIgnore
	private ObjectId id;
	@Indexed
	private String itemId;
	private String name;
	@JsonProperty("name_i18n")
	private Map<String, String> nameMap;
	private Integer sortId;
	private Integer rarity;
	@ApiModelProperty(notes = "The existence of the item in each server.")
	private Map<Server, Existence> existence;
	private String itemType;
	private Integer addTimePoint;
	@ApiModelProperty(notes = "The position in the sprite image.")
	private List<Integer> spriteCoord;
	private String groupID;
	@JsonProperty("alias")
	private Map<String, List<String>> aliasMap;
	@JsonProperty("pron")
	private Map<String, List<String>> pronMap;

	@JsonIgnore
	public Item toNonI18nView() {
		this.nameMap = null;
		return this;
	}
}
