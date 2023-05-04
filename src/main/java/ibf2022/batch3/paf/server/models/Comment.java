package ibf2022.batch3.paf.server.models;

import org.bson.Document;

import jakarta.json.Json;
import jakarta.json.JsonObject;

// Do not change this file
public class Comment {

	private String restaurantId;
	private String name;
	private long date = 0l;
	private String comment;
	private int rating;

	public Comment() {
	}

	
	public Comment(String restaurantId, String name, long date, String comment, int rating) {
		this.restaurantId = restaurantId;
		this.name = name;
		this.date = date;
		this.comment = comment;
		this.rating = rating;
	}


	public void setRestaurantId(String restaurantId) { this.restaurantId = restaurantId; }
	public String getRestaurantId() { return this.restaurantId; }

	public void setName(String name) { this.name = name; }
	public String getName() { return this.name; }

	public void setComment(String comment) { this.comment = comment; }
	public String getComment() { return this.comment; }

	public void setDate(long date) { this.date = date; }
	public long getDate() { return this.date; }

	public void setRating(int rating) { this.rating = rating; }
	public int getRating() { return this.rating; }

	@Override
	public String toString() {
		return "Comment{restaurantId=%s, name=%s, date=%d, comment=%s, rating=%d"
				.formatted(restaurantId, name, date, comment, rating);
	}

	public JsonObject toJSON(){
		return Json.createObjectBuilder()
		.add("restaurantId", getRestaurantId())
		.add("name", getName())
		.add("date", getDate())
		.add("comment", getComment())
		.add("rating", getRating())
		.build();
	}

	public static Comment create(Document d){
		Comment c = new Comment();

		c.setRestaurantId(d.getString("restaurantId"));
		c.setName(d.getString("name"));
		c.setDate(d.getLong("date"));
		c.setComment(d.getString("comment"));
		c.setRating(d.getInteger("rating"));
		
		return c;
	}

}
