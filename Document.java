/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.*;

public class Document {
    private String label;    
    private Hashtable words;
    private Hashtable tfval;
    private int wordcount;//total word in this topic
    private int doccount;//number of document about this topic
    private double tfsum;
    
    public Document()
    {

    }
    
    public Document(String l)
    {    	
    	label=l;
    	words=new Hashtable();
        tfval=new Hashtable();
        wordcount=0;
        doccount=0;
        tfsum=0;
    } 
    
    public void setLabel(String l){
    	this.label = l;
    }
    
    public String getLabel(){
    	return this.label;
    }        
    
    public Hashtable getWords(){
    	return this.words;
    }   
    
    public Hashtable getTfVal(){
    	return this.tfval;
    }
    
    public void setWordCount(int wc){
    	this.wordcount = wc;
    }
    
    public int getWordCount(){
    	return this.wordcount;
    }
    
    public void setDocCount(int dc){
    	this.doccount = dc;
    }
    
    public int getDocCount(){
    	return this.doccount;
    }
    
    public void setTfSum(double sum){
    	this.tfsum = sum;
    }
    
    public double getTfSum(){
    	return this.tfsum;
    }
}
