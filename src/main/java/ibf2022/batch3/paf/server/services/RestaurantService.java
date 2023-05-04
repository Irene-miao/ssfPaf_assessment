package ibf2022.batch3.paf.server.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ibf2022.batch3.paf.server.models.Comment;
import ibf2022.batch3.paf.server.models.Restaurant;
import ibf2022.batch3.paf.server.repositories.RestaurantRepository;

@Service
public class RestaurantService {

	
	@Autowired
	private RestaurantRepository repo;

	
	// TODO: Task 2
	// Do not change the method's signature
	public List<String> getCuisines() {
		return repo.getCuisines();
	}

	// TODO: Task 3 
	// Do not change the method's signature
	public List<Restaurant> getRestaurantsByCuisine(String cuisine) {
		return repo.getRestaurantsByCuisine(cuisine);
	}

			

	// TODO: Task 4 
	// Do not change the method's signature
	public Optional<Restaurant> getRestaurantById(String id) {
		return repo.getRestaurantById(id);
	}

	// TODO: Task 5 
	// Do not change the method's signature
	public void postRestaurantComment(Comment comment) {
		repo.insertRestaurantComment(comment);
	}
}
