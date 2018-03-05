package Naive_Bayes;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		bayes nb = new bayes();
	    nb.readTopics("topics.data");
	    nb.readTrainData("training.data");;
	    nb.readTestData("test.data");	    
	    nb.setTrainWords();
	    double ans = nb.findTopic();
	    System.out.println(ans);
	    nb.clearData();
	}		
}
