package ibf2022.batch3.paf.server.repositories;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.aggregation.StringOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import ibf2022.batch3.paf.server.models.Comment;
import ibf2022.batch3.paf.server.models.Restaurant;

@Repository
public class RestaurantRepository {


	@Autowired
	private MongoTemplate mongoTemplate;
	
	// TODO: Task 2 
	// Do not change the method's signature
	// Write the MongoDB query for this method in the comments below
	
	/*db.restaurant.aggregate([ {$project: {_id:1, cuisine: 1, borough: 1}},  {$group: { _id: "$borough", cuisines: {$push: "$cuisine" }}},{ $sort: { cuisine: 1}}]); */
	public List<String> getCuisines() {
		List<String> array = new ArrayList<>();
		List<String> list = new ArrayList<>();
		List<String> newList = new ArrayList<>();
		
		// return id, cuisine, borough, group by borough
		ProjectionOperation project = Aggregation.project("_id", "cuisine", "borough");
		GroupOperation group = Aggregation.group("borough")
		.push("cuisine").as("cuisines");
		Aggregation pipeline = Aggregation.newAggregation(project, group);
		AggregationResults<Document> r = mongoTemplate.aggregate(pipeline, "restaurant", Document.class);

	

	      // iterate through each document
		 if (r.iterator().hasNext()){
			Document d = r.iterator().next();
			array =  d.getList("cuisines", String.class);
		 }
		 // get unique values in a list
		 list = array.stream().distinct().collect(Collectors.toList());
		 // sort the list in ascending order
		 Collections.sort(list);
		 // print the list
		 for (String l : list){
			l = l.replaceAll("\\s", "");
			l = l.replace("/", "_");
			l = l.trim();
			newList.add(l);
		 }
		 
		 return newList;
	}

	// TODO: Task 3 
	// Do not change the method's signature
	// Write the MongoDB query for this method in the comments below
	/* db.restaurant.aggregate([ {$match: { cuisine: "Chinese" }},{$project: {_id:0, restaurantId: "$restaurant_id", name:1}},{$sort: { name: 1}} ]);*/
	public List<Restaurant> getRestaurantsByCuisine(String cuisine) {
		List<Restaurant> restaurants = new ArrayList<>();

		MatchOperation match = Aggregation.match(Criteria.where("cuisine").is(cuisine));
		ProjectionOperation project = Aggregation.project("_id", "name")
		.and("restaurant_id").as("restaurantId");
		SortOperation sort = Aggregation.sort(Sort.by(Direction.ASC, "name"));

		Aggregation pipeline = Aggregation.newAggregation(match, project, sort);

		AggregationResults<Document> r = mongoTemplate.aggregate(pipeline, "restaurant", Document.class);
		System.out.println(r.getMappedResults());
		
		// save mongodb data in a list of documents, iterate through and convert each to POJO
		List<Document> list = r.getMappedResults();
		for (Document l : list){
			Restaurant res = Restaurant.create(l);
			restaurants.add(res);
		}
		return restaurants;

		
	}
	
	// TODO: Task 4 
	// Do not change the method's signature
	// Write the MongoDB query for this method in the comments below
	/*db.restaurant.aggregate([ { $match: {restaurant_id : "30191841" }},{$project:  { restaurant_id:1, name: 1, cuisine: 1n,name:1, rating:1, address: { $concat: [ "$address.building", " ", "$address.street", " ","$address.zipcode"," ", "$borough"]} }}, { $lookup: { from: "comments", foreignField: "restaurantId", localField: "restaurant_id", as : "comments" }} ]);*/
	public Optional<Restaurant> getRestaurantById(String id) {
		MatchOperation match = Aggregation.match(Criteria.where("restaurant_id").is(id));
		ProjectionOperation project = Aggregation.project(  "restaurant_id", "name", "cuisine", "comment", "address", "borough", "comments")
		.and(
			StringOperators.Concat.valueOf("address.building").concat(", ")
			.concatValueOf("address.street").concat(", ")
			.concatValueOf("address.zipcode").concat(", ")
			.concatValueOf("borough")
		).as("address");
		//.and("restaurant_id").as("restaurantId");
			LookupOperation lookup = Aggregation.lookup("comments", "restaurant_id", "restaurantId", "comments");
		
		Aggregation pipeline = Aggregation.newAggregation(match, project, lookup);

		AggregationResults<Document> r = mongoTemplate.aggregate(pipeline, "restaurant", Document.class);

		System.out.println(r.getMappedResults());

		if (!r.iterator().hasNext()){
			return Optional.empty();
		}
		Document d = r.iterator().next();
		System.out.println(d);
		Restaurant res = Restaurant.createAll(d);
		System.out.println(res.toString());
		return Optional.of(res);
		
		
		
	}

	// TODO: Task 5 
	// Do not change the method's signature
	// Write the MongoDB query for this method in the comments below
	/* db.comments.insert({ restaurantId : "40356018", name: "Qoo", date: Long("1682416471"), comment: "Great", rating : 6 });*/
	public void insertRestaurantComment(Comment comment) {
		Date d = new Date();
		System.out.println(d);
		long epoch = d.getTime();
		System.out.println(epoch);

		Comment c = new Comment();
		c.setRestaurantId(comment.getRestaurantId());
		c.setName(comment.getName());
		c.setComment(comment.getComment());
		c.setDate(epoch);
		c.setRating(comment.getRating());

		String json  = c.toJSON().toString();
		Document doc = Document.parse(json);

		if (!mongoTemplate.getCollectionNames().contains("comments")){
			mongoTemplate.createCollection("comments");
		}
		

		Document document = mongoTemplate.insert(doc, "comments");
		
		System.out.println(document);

	}
	
}
