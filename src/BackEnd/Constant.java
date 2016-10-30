package BackEnd;

public class Constant {

	//DB
	public static String FINDTHEONEDB = "findtheonedb";

	//URI
	public static String MONGOCLIENT = "mongodb://saumeel:111@ds047592.mlab.com:47592/findtheonedb";

	//Collection
	public static String meetingCollection = "meeting";
	public static String userCollection = "user";

	//MeetingQuery
	static class MeetingQuery{
		public static String student = "student";
		public static String teacher = "teacher";
		public static String subject = "subject";
		public static String studyPlace = "location";
	}
	
	//UserQuery
	static class UserQuery{
		public static String userName = "userName";
		public static String interests = "interests";
		public static String friends = "friends";
		
		public static String replaceRegex = "[\\[\\]\\s\"]";
		public static String emptyString = "";
	}
	
	
	//Extras
	public static String COMMA = ",";
}
