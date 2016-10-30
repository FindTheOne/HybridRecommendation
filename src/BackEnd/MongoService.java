package BackEnd;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import java.net.UnknownHostException;
import java.util.Arrays;
import org.codehaus.jettison.json.JSONObject;

import com.mongodb.*;

public class MongoService {
	public MongoClientURI mongoLab;
	public MongoClient client;
	public DB database;
	public DBCollection collection;
	public DBObject currentUser;
	public static String username = "amrish";
	
	//comment
	public MongoService(){
		try{
			mongoLab = new MongoClientURI(Constant.MONGOCLIENT);
			client = new MongoClient(mongoLab);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}	
		database = client.getDB(Constant.FINDTHEONEDB);
		currentUser = null;
	}
	
	// Collaborative Recommendation
	public  String getPersonalizedRecommendation(String username){
		String result = "";
		String friends = getFriends(username);
		String[] friendsList = parseList(friends);
		System.out.println("friends list : "+Arrays.toString(friendsList));

		String interests = getInterests(username);
		String[] interestsList = parseList(interests);
		System.out.println("interest list : "+Arrays.toString(interestsList));

		DBCollection meetingCollection = database.getCollection(Constant.meetingCollection);
		
		for (String friend : friendsList){
			for(String interest : interestsList){
				BasicDBObject query = new BasicDBObject();
				query.put(Constant.MeetingQuery.student, friend);
				query.put(Constant.MeetingQuery.subject, interest);
				System.out.println("query : "+query.toString());
				DBCursor cursor = meetingCollection.find(query);
				if(cursor.hasNext()){
					//A friend can learn a same subject from multiple people, 
					//Currently assuming, he learnt a subject from only one person.
					DBObject dbObject = cursor.next();
					String teacher = dbObject.get(Constant.MeetingQuery.teacher).toString();
					String location = dbObject.get(Constant.MeetingQuery.studyPlace).toString();
					System.out.println("Recommendation based on : ");
					System.out.println("1. Friend : "+friend);
					System.out.println("2. Subject : "+interest);
					System.out.println("3. Teacher : "+teacher);
					System.out.println("3. Location : "+location);
					
					// Nikita learnt CS50 from Meghana at San Jose State University.
					// Would you like to contact Meghana?
				}
			}
		}
		
		return result;
	}

	public String getInterests(String userName){
		if(currentUser == null){
			DBCollection collection = database.getCollection(Constant.userCollection);
			BasicDBObject query = new BasicDBObject();
			query.put(Constant.UserQuery.userName, userName);
			DBCursor cursor = collection.find(query);
			if(cursor.hasNext()){
				currentUser = cursor.next();
			}
		}else{
			String stringify = currentUser.get(Constant.UserQuery.interests).toString();
			stringify = stringify.replaceAll(Constant.UserQuery.replaceRegex, Constant.UserQuery.emptyString);
			return stringify;
		}
		return new String();
	}
	public String getFriends(String userName){
		if(currentUser == null){
			DBCollection collection = database.getCollection(Constant.userCollection);
			BasicDBObject query = new BasicDBObject();
			query.put(Constant.UserQuery.userName, userName);
			DBCursor cursor = collection.find(query);
			if(cursor.hasNext()){
				currentUser = cursor.next();
			}
			String stringify = currentUser.get(Constant.UserQuery.friends).toString();
			stringify = stringify.replaceAll(Constant.UserQuery.replaceRegex, Constant.UserQuery.emptyString);
			return stringify;
		}
		return new String();
	}

	public  String[] parseList(String combinedString){
		String[] list = combinedString.split(Constant.COMMA);
		if(list.length>0)
			return list;
		return new String[0];
	}
	public static String getGeneralRecommendation(){
		return "";
	}



	public static void main(String[] args) throws Exception{

		MongoService service = new MongoService();
		String personalRecommendation = service.getPersonalizedRecommendation(MongoService.username);
	}
}
