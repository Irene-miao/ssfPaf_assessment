package ibf2022.batch3.paf.server.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import ibf2022.batch3.paf.server.models.Comment;
import ibf2022.batch3.paf.server.models.Restaurant;

@Repository
public class RestaurantRepository {

	// TODO: Task 2 
	// Do not change the method's signature
	// Write the MongoDB query for this method in the comments below
	//
	public List<String> getCuisines() {
		return null;
	}

	// TODO: Task 3 
	// Do not change the method's signature
	// Write the MongoDB query for this method in the comments below
	//
	public List<Restaurant> getRestaurantsByCuisine(String cuisine) {
		return null;
	}
	
	// TODO: Task 4 
	// Do not change the method's signature
	// Write the MongoDB query for this method in the comments below
	//
	public Optional<Restaurant> getRestaurantById(String id) {
		return null;
	}

	// TODO: Task 5 
	// Do not change the method's signature
	// Write the MongoDB query for this method in the comments below
	//
	public void insertRestaurantComment(Comment comment) {
	}
}
