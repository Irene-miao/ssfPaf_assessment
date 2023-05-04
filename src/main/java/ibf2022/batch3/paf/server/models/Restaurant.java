package ibf2022.batch3.paf.server.models;

import java.util.LinkedList;
import java.util.List;

import org.bson.Document;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

// Do not change this file
public class Restaurant {

	private String restaurantId;
	private String name;
	private String address;
	private String cuisine;
	private String borough;
	private List<Comment> comments = new LinkedList<>();

	public void setRestaurantId(String restaurantId) { this.restaurantId = restaurantId; }
	public String getRestaurantId() { return this.restaurantId; }

	public void setName(String name) { this.name = name; }
	public String getName() { return this.name; }

	public void setAddress(String address) { this.address = address; }
	public String getAddress() { return this.address; }

	public void setCuisine(String cuisine) { this.cuisine = cuisine; }
	public String getCuisine() { return this.cuisine; }

	public void setComments(List<Comment> comments) { this.comments = comments; }
	public List<Comment> getComments() { return this.comments; }
	public void addComment(Comment comment) { this.comments.add(comment); }

	public Restaurant() {
	}

	
	public Restaurant(String restaurantId, String name) {
		this.restaurantId = restaurantId;
		this.name = name;
	}


	public Restaurant(String restaurantId, String name, String address, String cuisine, List<Comment> comments) {
		this.restaurantId = restaurantId;
		this.name = name;
		this.address = address;
		this.cuisine = cuisine;
		this.comments = comments;
	}

	
	@Override
	public String toString() {
		return "Restaurant{restaurantId=%s, name=%s, address=%s, cuisine=%s, comments=%s"
				.formatted(restaurantId, name, address, cuisine, comments);
	}
	public String getBorough() {
		return borough;
	}
	public void setBorough(String borough) {
		this.borough = borough;
	}

	// create POJO from mongo document
	public static Restaurant create(Document d){
		Restaurant r = new Restaurant();
		r.setRestaurantId(d.getString("restaurantId"));
		r.setName(d.getString("name"));
		
		return r;
	}

	// convert POJO to json string
	public JsonObject toJSON(){
		return Json.createObjectBuilder()
		.add("restaurantId", getRestaurantId())
		.add("name", getName())
		.build();
		
	}

	// create POJO from mongo document
	public static Restaurant createAll(Document d){

		Restaurant r = new Restaurant();
		List<Comment> comments = new LinkedList<>();
		List<Document> array  = d.getList("comments", Document.class);
		if (array != null) {
			for (Document a : array){
				comments.add(Comment.create(a));
			}
		}
		String cuisine = d.getString("cuisine");
		cuisine = cuisine.replaceAll("\\s", "");
		cuisine = cuisine.replace("/", "_");
		cuisine = cuisine.trim();
		System.out.println(cuisine);

		r.setRestaurantId(d.getString("restaurant_id"));
		r.setName(d.getString("name"));
		r.setCuisine(cuisine);
		r.setAddress(d.getString("address"));
		r.setComments(comments);
		
		return r;
	}

	// convert POJO to json string
	public JsonObject toJSONALL(){

		// convert List<Comment> into JsonArray
		JsonArray result = null;
		JsonArrayBuilder builder = Json.createArrayBuilder();

		if (this.getComments() != null){
			for (Comment c : this.getComments()){
				JsonObject obj = Json.createObjectBuilder()
				.add("restaurantId", c.getRestaurantId())
				.add("name", c.getName())
				.add("date", c.getDate())
				.add("comment", c.getComment())
				.add("rating", c.getRating())
				.build();
				builder.add(obj);
			}
			result = builder.build();

			// convert Restaurant POJO into jsonObject
		return Json.createObjectBuilder()
		.add("restaurant_id", getRestaurantId())
		.add("name", getName())
		.add("cuisine", getCuisine())
		.add("address", getAddress())
		.add("comments", result)
		.build();
		}
			
			// convert Restaurant POJO into jsonObject
			return Json.createObjectBuilder()
			.add("restaurant_id", getRestaurantId())
			.add("name", getName())
			.add("cuisine", getCuisine())
			.add("address", getAddress())
			.add("comments", Json.createArrayBuilder().build())
			.build();
	}
}


