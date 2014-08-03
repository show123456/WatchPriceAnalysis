package analysis;

import java.sql.ResultSet;
import java.util.Set;

import newscrawler.Globals;
import newscrawler.Helper;
import dbconnection.MySqlConnection;

public class TopicAnalysis {
	private MySqlConnection mysqlConnection = null;

	public TopicAnalysis() {
		this.mysqlConnection = new MySqlConnection();
		this.mysqlConnection.getIdTopicMap();
	}

	// Go through existings article, try to identify topic of them
	public void populateArticleTopic() {
		int lowerBound = 0;
		int maxNumResult = 2000;
		int articleCount = lowerBound;
		
		// Get 2000 articles at a time, until exhaust all the articles
		while (true) {
			ResultSet resultSet = this.mysqlConnection.getArticleInfo(lowerBound, maxNumResult);
			if (resultSet == null)
				break;

			try {
				int count = 0;
				// Iterate through the result set to populate the information
				while (resultSet.next()) {
					count++;
					articleCount++;
					
					int articleId = resultSet.getInt(1);
					String articleName = resultSet.getString(6).trim();
					if (Globals.DEBUG)
						System.out.println("("+articleCount+") Article id " + articleId + ": "
								+ articleName);

					Set<String> topicsOfName = Helper.identifyTopicOfName(
							articleName, Globals.HOROLOGYTOPICS);
					String[] topics = new String[topicsOfName.size()];

					int index = 0;
					for (String topic : topicsOfName) {
						topics[index] = topic;
						index++;
					}

					Integer[] topicsId = this.mysqlConnection
							.convertTopicToTopicId(topics);

					if (Globals.DEBUG)
						System.out.println("Topics " + topics.toString() + ": "
								+ topicsId.toString());

					// Insert into article_topic_table table
					for (int topicId : topicsId) {
						this.mysqlConnection.addArticleTopicRelationship(
								articleId, topicId);
					}
				}

				if (count == 0)
					break;
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
			
			lowerBound += maxNumResult;
		}
	}

	public static void main(String[] args) {
//		TopicAnalysis topicAnalysis = new TopicAnalysis();
//		topicAnalysis.populateArticleTopic();
	}
}