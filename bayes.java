package Naive_Bayes;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class bayes {
			
	private Hashtable<String, Integer> topiclist;
	private Hashtable<String, Integer> trainwords;	    		
	private ArrayList<document> trainDocList;	
	private ArrayList<document> testDocList;
    
	public bayes(){
		topiclist = new Hashtable<String, Integer>();
		trainwords = new Hashtable<String, Integer>();						
		trainDocList = new ArrayList<document>();
		testDocList = new ArrayList<document>();				
	}
	
	public String removeNoise(String str) {    
    	String noiseList[]={"a","an","and","the","in","on","or","am","is",
    	        "are","for","but","it","this","has","have","will","shall","be",
    	        "been","would","should","that","to","of","off","at","however",
    	        "was","were","can","could","from","if","at","with","do","does",
    	        "did","done","who","where","how","what","when","which","then","there",
    	        "about","into","by","its",".",",","-",";","\"","(",")","\\",
    	        "out","up","with","through","about","above","below","before","after","reuter","reuters"};
    	   	ArrayList<String>noiseArrList=new ArrayList<String>(Arrays.asList(noiseList));         	    
        str=str.toLowerCase();
        if(noiseArrList.contains(str))
            return null;
        str=str.replaceAll(",","");
        str=str.replaceAll("\\.","");
        str=str.replaceAll(";","");
        str=str.replaceAll("-","");
        str=str.replaceAll("\"","");
        str=str.replaceAll("\\(","");
        str=str.replaceAll("\\)","");
        str=str.replaceAll("'","");
        str=str.replaceAll(":","");
        if(str.isEmpty())
            return null;
        String expression = "^[-+]?[0-9]*\\.?[0-9]+$";
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(str);
        if(matcher.matches())
            return null;
        return str;
    }
	
	public void readTopics(String filename) {    
        topiclist.clear();       
        try {
    		BufferedReader bin = new BufferedReader(new FileReader(new File(filename)));
        	String input = "";
        	while(true) {        	
            	try {            	
                	input = bin.readLine();
            	}
            	catch(Exception ex) {            	
                	System.out.println("Exception in readline: "+ex);
            	}
            	if(input == null)
                	break;
            	if(input.equals(""))
                	continue;
            	StringTokenizer st = new StringTokenizer(input);
            	String str = st.nextToken();
            	str = removeNoise(str);
            	if(str == null)
                    continue;
            	topiclist.put(str, new Integer(0));
        	}            
        	System.out.println("Topic: " + topiclist.size());            
	        try {	        
	            bin.close();
	        }
	        catch(Exception ex) {	        
	            System.out.println("Exception to close: "+ex);
	        }
        }
    	catch(Exception rx) {    	
    		System.out.println("Exception to read topic: "+rx);
    	}
    }
	
	public void readTrainData(String filename) {            
        document doc = new document();
    	try {
    		BufferedReader bin = new BufferedReader(new FileReader(new File(filename)));
        	String input = "";
        	while(true) {        	
            	try {            	
                	input = bin.readLine();
            	}
            	catch(Exception ex) {            	
                	System.out.println("Exception in readline: "+ex);
            	}
            	if(input == null)
                	break;
            	if(input.equals(""))
                	continue;
            	StringTokenizer st = new StringTokenizer(input);
            	int x = st.countTokens();
            	if(x == 1) { // found a label            	
                	String str = st.nextToken();
                	str = removeNoise(str);
                	if(str == null)
                    	continue;
                    if(topiclist.containsKey(str)) {                    	                       
                		doc = new document(str);                		  
                		trainDocList.add(doc);
                	}
            	}
            	int wcount = 0;
	            while(st.hasMoreTokens()) {	            
	                String str=st.nextToken();
	                str = removeNoise(str);
	                if(str == null)
	                    continue;
                    doc.addWord(str);
                    wcount++;
                    if(!trainwords.containsKey(str))
	                	trainwords.put(str, new Integer(0));
	            }
	            doc.setWordCount(wcount);
        	}
        	System.out.println("Train: " + trainDocList.size());
	        try {	        
	            bin.close();
	        }
	        catch(Exception ex) {	        
	            System.out.println("Exception: "+ex);
	        }
    	}
    	catch(Exception rx) {    	
    		System.out.println("Exception: "+rx);
    	}    	
    }
	
	public void readTestData(String filename) {    
    	document doc = new document();    	
    	try {
    		BufferedReader bin = new BufferedReader(new FileReader(new File(filename)));
        	String input = "";    		    	           
        	while(true) {        	      
            	try {            	
                	input = bin.readLine();                	
            	} 
            	catch(Exception ex) {            	
                	System.out.println("Exception in readline: "+ex);
            	}    	    	    	                           
            	if(input == null)
                	break;
            	if(input.equals(""))
                	continue;                        
            	StringTokenizer st=new StringTokenizer(input);
            	int x = st.countTokens();                
            	if(x == 1) { // found a label
            	
                	String str = st.nextToken();                    	
                	str = removeNoise(str);
                	if(str == null)
                    	continue;                	
                    if(topiclist.containsKey(str)) {                	                                                    
                		doc = new document(str);                                                     
                        testDocList.add(doc);
                        topiclist.put(str, topiclist.get(str) + 1);
                	}
            	}
            	int wcount = 0;
	            while(st.hasMoreTokens()) {	            
	                String str=st.nextToken(); 
	                str=removeNoise(str);
	                if(str==null)
	                    continue;	                
	                doc.addWord(str);
                    wcount++;
	            }
	            doc.setWordCount(wcount);
        	}
        	System.out.println("Test: " + testDocList.size());
	        try {	        
	            bin.close();
	        } 
	        catch(Exception ex) {	        
	            System.out.println("Exception: "+ex);
	        }                
    	}
    	catch(Exception rx)
    	{
    		System.out.println("Exception: "+rx);
    	}                        
    }
	
	public void setTrainWords(){	
		Enumeration<String> allWords = this.trainwords.keys();
		while(allWords.hasMoreElements()){
			String word = allWords.nextElement();
			int count = 0;
			for(int i = 0; i < this.trainDocList.size(); i++){
				document d = (document)trainDocList.get(i);
				if(d.existWord(word))
					count++;
			}
			trainwords.put(word, count);
		}
		System.out.println("Train words counted");
	}	
	    
	public double findTopic() {         
        int tr = 0, fl = 0;
        ArrayList<probability> probTopic = new ArrayList<probability>();
        int totaldoc = trainDocList.size();        
        for(int i = 0; i < testDocList.size(); i++) {        
            document testdoc = (document)testDocList.get(i); 
            double p_td = 0;
            probTopic.clear();
            for(int j = 0; j < trainDocList.size(); j++) {            
                document traindoc = (document)trainDocList.get(j);
                String topic = traindoc.getLabel();                
                double p_topic = (double )topiclist.get(topic)/totaldoc;//probability of topic p(c)                   
                p_td = p_topic;                
                Enumeration<String> e = testdoc.getWords();                
                while(e.hasMoreElements()) {                    
                	String w = (String)e.nextElement();
                	int f_wt = 0;
                	if(traindoc.existWord(w))
                		f_wt = (Integer)traindoc.wCount(w);
                	int n_t = traindoc.getWordCount();
                	double p_wt = (double)(f_wt+1)/(n_t+trainwords.size()); // word probability f(w/c)
                	p_wt*= 1000;                       
                	int num = (Integer)testdoc.wCount(w);
                	p_wt = Math.pow(p_wt,num);
                	p_td*= p_wt;
                }                   
                probability pd = new probability(traindoc.getLabel(),p_td);
                probTopic.add(pd);                
            }
            Collections.sort(probTopic);            
            probability pdt = (probability)probTopic.get(0);                       
            if(pdt.getLabel().matches(testdoc.getLabel()))
                tr++;
            else
                fl++;            
        }
        double temp_accuracy = (double)tr/(tr+fl);        
        return temp_accuracy;        
    } 
    
    public void clearData(){
    	for(int i = 0; i < this.trainDocList.size(); i++){
    		this.trainDocList.get(i).wClear();
    	}
    	this.trainDocList.clear();
    	for(int i = 0; i < this.testDocList.size(); i++){
    		this.testDocList.get(i).wClear();
    	}
    	this.testDocList.clear();
	this.topiclist.clear();
    	this.trainwords.clear();
    }                      
}
 
