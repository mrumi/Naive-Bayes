package Naive_Bayes;

import java.util.Enumeration;
import java.util.Hashtable;
public class document {
	
	private String label;    
	private Hashtable<String, Integer> words;	
	private int wordcount;//total word in this topic	    
    
    public document() {

    }
    public document(String l) {    	
    	label = l;
    	words = new Hashtable<String, Integer>();        
        wordcount = 0;                
    }
    
    public void setLabel(String l){
    	this.label = l;
    }
    
    public String getLabel(){
    	return this.label;
    }                
    
    public void setWordCount(int wc){
    	this.wordcount = wc;
    }
    
    public int getWordCount(){
    	return this.wordcount;
    }               
    
    public void addWord(String word){
    	if(!words.containsKey(word)) {        
        	words.put(word,new Integer(1));
        }
        else {        
        	Integer count=(Integer)words.get(word);
        	words.put(word,new Integer(count+1));
        }
    }
     public boolean existWord(String word){
    	 if(words.containsKey(word))
    		 return true;
    	 return false;
     }
     
     public Enumeration<String> getWords(){
     	return words.keys();
     }
     
     public int wCount(String word){
    	 return words.get(word);
     }
    
    public void wClear(){
    	this.words.clear();
    }
        
}
