package ibf2022.batch3.paf.server.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ibf2022.batch3.paf.server.models.Comment;
import ibf2022.batch3.paf.server.models.Restaurant;
import ibf2022.batch3.paf.server.services.RestaurantService;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.stream.JsonCollectors;

@RestController
@RequestMapping(path="/api", produces=MediaType.APPLICATION_JSON_VALUE)
public class RestaurantController {
	
	@Autowired
	private RestaurantService svc;

	
	// TODO: Task 2 - request handler
	@GetMapping(path="/cuisines")
	public ResponseEntity<String> getCuisines(){
		List<String> cuisines = svc.getCuisines();
		
		
		JsonArray array = cuisines.stream()
		.map(Json::createValue)
		.collect(JsonCollectors.toJsonArray());
		
	
		return ResponseEntity.status(HttpStatus.OK)
		.contentType(MediaType.APPLICATION_JSON)
		.body(array.toString());

	}

	// TODO: Task 3 - request handler
	@GetMapping(path="/restaurants/{cuisine}")
	public ResponseEntity<String> getRestaurants(@PathVariable String cuisine){
		List<Restaurant> restaurants = svc.getRestaurantsByCuisine(cuisine);
		
		System.out.println(restaurants);
		JsonArray array = restaurants.stream()
		.map(r -> r.toJSON())
		.collect(JsonCollectors.toJsonArray());
		
	
		return ResponseEntity.status(HttpStatus.OK)
		.contentType(MediaType.APPLICATION_JSON)
		.body(array.toString());
	}


	// TODO: Task 4 - request handler
	@GetMapping(path="/restaurant/{restaurant_id}")
	public ResponseEntity<String> getRestaurantById(@PathVariable String restaurant_id){

		Optional<Restaurant> res = svc.getRestaurantById(restaurant_id);
		System.out.println(res.get());
		if (res.isEmpty()){
			JsonObject error = Json.createObjectBuilder()
			.add("error", String.format("Missing %s", restaurant_id))
			.build();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error.toString());
		}

		JsonObject obj = res.get().toJSONALL();
		System.out.println(obj.toString());

		return ResponseEntity.status(HttpStatus.OK)
		.contentType(MediaType.APPLICATION_JSON)
		.body(obj.toString());

	}

	// TODO: Task 5 - request handler
	@PostMapping(path="/restaurant/comment", consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<String> postComment(@ModelAttribute Comment comment) throws RestaurantException{
      System.out.println(comment);
		JsonObject message = null;
	  JsonObject error = null;

		try {
			svc.postRestaurantComment(comment);
			message = Json.createObjectBuilder()
			.build();

		} catch (Exception e){
			e.getMessage();
			error = Json.createObjectBuilder()
			.add("error", e.getMessage())
			.build();
			return ResponseEntity.badRequest().body(error.toString());
		}

		return ResponseEntity
		.status(HttpStatus.CREATED)
		.contentType(MediaType.APPLICATION_JSON)
		.body(message.toString());
	}

}
