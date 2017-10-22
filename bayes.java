/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import java.util.*;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 *
 * @author Monjura
 */
public class bayes {
    int traindocnum=0;
    int testdocnum=0;
    int numwords=0;//number of different words in traindoc
    int totaldoc=0;//number of documents in traindata
    double accuracy=0;
    Hashtable topiclist=new Hashtable();
    Hashtable trainwords=new Hashtable();
    String wk="",wb="";
    //ArrayList<String> test=new ArrayList<String>();
    //ArrayList<String> test2=new ArrayList<String>();
    ArrayList<Double> nb=new ArrayList<Double>();
    ArrayList<Double> knn=new ArrayList<Double>();
    //ArrayList<Double> test=new ArrayList<Double>();
    ArrayList<Probability> probTopic=new ArrayList<Probability>();
    ArrayList<Document> trainDocList = new ArrayList<Document>();
	//ArrayList<Document> testDocList = new ArrayList<Document>();
    ArrayList<Document> AllDocList = new ArrayList<Document>();
	String noiseList[]={"a","an","and","the","in","on","or","am","is",
        "are","for","but","it","this","has","have","will","shall","be",
        "been","would","should","that","to","of","off","at","however",
        "was","were","can","could","from","if","at","with","do","does",
        "did","done","who","where","how","what","when","which","then","there",
        "about","into","by","its",".",",","-",";","\"","(",")","\\",
        "out","up","with","through","about","above","below","before","after","reuter","reuters"};
   	ArrayList<String>noiseArrList=new ArrayList<String>(Arrays.asList(noiseList));
     //ArrayList<String>noiseArrList=new ArrayList<String>();
    
    public static void main(String[] args) {
        bayes ob = new bayes();
    	//ob.readTopics();
        //ob.readAllData();
        //ob.readTestData();
        //ob.train();
        //ob.testAccuracy();
        ob.comparison();
    }

    public void comparison()
    {
        readnb();
        readknn();        
        double mean_knn=0;
        double std_knn=0;
        double mean_bayes=0;
        double std_bayes=0;
        for(int i=0;i<nb.size();i++)
        {            
            double n=(double )nb.get(i);
            mean_bayes+=n;            
        }
        mean_bayes/=nb.size();
        System.out.println("mean bayes "+mean_bayes);        
        for(int i=0;i<nb.size();i++)
        {            
            double n=(double )nb.get(i);            
            std_bayes+=Math.pow(n-mean_bayes,2);// n-mean_bayes            
        }        
        std_bayes/=nb.size();
        std_bayes=Math.sqrt(std_bayes);
        System.out.println("std bayes "+std_bayes);
        for(int i=0;i<knn.size();i++)
        {
            double k=(double)knn.get(i);
            mean_knn+=k;
        }
        mean_knn/=knn.size();
        System.out.println("mean knn "+mean_knn);
        for(int i=0;i<knn.size();i++)
        {
            double k=(double)knn.get(i);
            std_knn+=Math.pow(k-mean_knn,2);            
        }
        std_knn/=knn.size();
        std_knn=Math.sqrt(std_knn);
        System.out.println("std knn "+std_knn);
        t_test();
    }

    public void t_test()
    {
        int size=nb.size();
        //System.out.println(size);
        double p_avg=0;
        for(int i=0;i<size;i++)
        {
            double p=((double)nb.get(i)-(double)knn.get(i))*100;
            //System.out.println(p);
            //test.add(p);
            p_avg+=p;
        }
        //System.out.println(p_avg);
        p_avg/=size;
        System.out.println(p_avg);
        double avg=0;
        for(int i=0;i<size;i++)
        {
            double p=((double)nb.get(i)-(double)knn.get(i))*100;
            avg+=Math.pow(p-p_avg,2);
            //System.out.println(Math.pow(p-p_avg,2));
            //p_avg+=p;
        }
        avg/=(size-1);
        //System.out.println(avg);
        double t=p_avg*Math.sqrt(size)/Math.sqrt(avg);
        System.out.println("t_test "+t);
    }

    public void readnb()
    {
        int k=0;
        nb.clear();
        try{
    		BufferedReader bin=new BufferedReader(new FileReader(new File("nb.data")));
        	String input="";
        	while(true)
        	{
            	try
            	{
                	input=bin.readLine();
            	}
            	catch(Exception ex)
            	{
                	System.out.println("Exception in readline: "+ex);
            	}
            	if(input==null)
                	break;            	
            	StringTokenizer st = new StringTokenizer(input);                
            	//String str = st.nextToken();
            	nb.add(Double.parseDouble(st.nextToken()));            	
        	}            
	        try
	        {
	            bin.close();
	        }
	        catch(Exception ex)
	        {
	            System.out.println("Exception to close: "+ex);
	        }
        }
    	catch(Exception rx)
    	{
    		System.out.println("Exception to read nb: "+rx);
    	}
    }

    public void readknn()
    {
        knn.clear();
        try{
    		BufferedReader bin=new BufferedReader(new FileReader(new File("knn.data")));
        	String input="";
        	while(true)
        	{
            	try
            	{
                	input=bin.readLine();
            	}
            	catch(Exception ex)
            	{
                	System.out.println("Exception in readline: "+ex);
            	}
            	if(input==null)
                	break;
            	StringTokenizer st = new StringTokenizer(input);
            	//String str = st.nextToken();
            	knn.add(Double.parseDouble(st.nextToken()));
        	}
	        try
	        {
	            bin.close();
	        }
	        catch(Exception ex)
	        {
	            System.out.println("Exception to close: "+ex);
	        }
        }
    	catch(Exception rx)
    	{
    		System.out.println("Exception to read knn: "+rx);
    	}
    }    

    public String removeNoise(String str)
    {
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
        //str=str.r
        if(str.isEmpty())
            return null;
        String expression="^[-+]?[0-9]*\\.?[0-9]+$";
        Pattern pattern=Pattern.compile(expression);
        Matcher matcher=pattern.matcher(str);
        if(matcher.matches())
            return null;
        return str;
    }

    public double findTopic()
    {       
        int tr=0,fl=0;
        for(int i=traindocnum;i<AllDocList.size();i++)
        {
            Document testdoc=(Document)AllDocList.get(i); 
            double p_td=0;
            probTopic.clear();
            for(int j=0;j<trainDocList.size();j++)
            {
                Document traindoc=(Document)trainDocList.get(j);
                {
                    double p_topic=(double )traindoc.getDocCount()/totaldoc;                   
                    p_td=p_topic;                
                    Enumeration e=testdoc.getWords().keys();                
                    while(e.hasMoreElements())
                    {
                        String w=(String)e.nextElement();
                        int f_wt=0;
                        if(traindoc.getWords().containsKey(w))
                            f_wt=(Integer)traindoc.getWords().get(w);
                        int n_t=traindoc.getWordCount();
                        double p_wt=(double)(f_wt+1)/(n_t+numwords);
                        p_wt*=1000;                       
                        int num=(Integer)testdoc.getWords().get(w);
                        p_wt=Math.pow(p_wt,num);
                        p_td*=p_wt;
                    }                    
                }
                Probability pd=new Probability(traindoc.getLabel(),p_td);
                probTopic.add(pd);
                
            }
            Collections.sort(probTopic);            
            Probability pdt=(Probability)probTopic.get(0);                       
            if(pdt.getLabel().matches(testdoc.getLabel()))
                tr++;
            else
                fl++;
        }
        double temp_accuracy=(double)tr/(tr+fl);        
        return temp_accuracy;        
    }    

    public double calTf_IDF()
    {
    	int ct=0,cf=0;
    	for(int i=traindocnum;i<AllDocList.size();i++)
    	{
    		Document ds=(Document)AllDocList.get(i);
    		int sz=ds.getWords().size();
    		Enumeration es=ds.getWords().keys();
    		while(es.hasMoreElements())
    		{
    			String s=(String)es.nextElement();
    			int nw=(Integer)ds.getWords().get(s);
    			double tf=(double)nw/sz;
    			double idf=0;
    			if(trainwords.containsKey(s))
    				idf=(Double)trainwords.get(s);
    			double tf_idf=tf*idf;
    			ds.setTfSum(ds.getTfSum()+Math.pow(tf_idf,2));
    			
    			ds.getTfVal().put(s,tf_idf);
    		}
    		ds.setTfSum(Math.sqrt(ds.getTfSum()));
    		
    		for(int j=0;j<traindocnum;j++)
    		{
    			Document dr=(Document)AllDocList.get(j);
    			Enumeration test=ds.getTfVal().keys();
		    	double cosine=0;
		    	while(test.hasMoreElements())
		    	{
		    		String sr=(String)test.nextElement();
		    		if(dr.getTfVal().containsKey(sr))
		    		{
		    			cosine+=(Double)ds.getTfVal().get(sr)*(Double)dr.getTfVal().get(sr);
		    		}
		    	}
		    	cosine=cosine/(ds.getTfSum()*dr.getTfSum());
                Probability pd=new Probability(dr.getLabel(),cosine);
                probTopic.add(pd);
    		}
    		Collections.sort(probTopic);
    		Hashtable predict=new Hashtable();
    		String sp="";
    		predict.clear();
            for(int k=0;k<5;k++)
            {
                Probability sm=(Probability)probTopic.get(k);
                String label=sm.getLabel();
                if(predict.containsKey(label))
                {
                    Integer count=(Integer)predict.get(label);
                    predict.put(label,new Integer(count+1));
                }
                else
                    predict.put(label,new Integer(1));
            }
            int max=0;
            Enumeration p=predict.keys();
            while(p.hasMoreElements())
            {
                String s=(String)p.nextElement();
            	int m=(Integer)predict.get(s);
            	if(m>max)
            	{
                    max=m;
            		sp=s;
            	}
            }
            if(sp.matches(ds.getLabel()))
                ct++;
    		else
    			cf++;
    		probTopic.clear();
            ds.getTfVal().clear();
    	}
    	double temp_accuracy=(double)ct/(ct+cf);
    	//System.out.println("TF_IDF accuracy "+temp_accuracy);
        return temp_accuracy;
    }

    public void calTf()
    {
    	for(int i=0;i<traindocnum;i++)
    	{
    		Document d=(Document)AllDocList.get(i);
            d.getTfVal().clear();
    		int sz=d.getWords().size();
    		Enumeration e=d.getWords().keys();
    		while(e.hasMoreElements())
    		{
    			String s=(String)e.nextElement();
    			int nw=(Integer)d.getWords().get(s);
    			double tf=(double)nw/sz;
    			double idf=(Double)trainwords.get(s);
    			double tf_idf=tf*idf;    		
                d.getTfVal().put(s,tf_idf);
                d.setTfSum(d.getTfSum()+Math.pow(tf_idf,2));    			
    		}    		
    		d.setTfSum(Math.sqrt(d.getTfSum()));
    	}
    	//System.out.println("tf done");
    }

    private void calIDF()
    {
    	int docnum=traindocnum;///D
        Enumeration e=trainwords.keys();
        while(e.hasMoreElements())
        {
        	String str=(String)e.nextElement();
            int count=0;
            for(int k=0;k<traindocnum;k++)
            {
            	Document d=(Document)AllDocList.get(k);
            	if(d.getWords().containsKey(str))
            		count++;
            }
            double d_cw=(double)docnum/count;//D/C(w)
            double idf= Math.log(d_cw);
            trainwords.put(str,(Double)idf);
        }
        //System.out.println("idf done");
    }

    public void train()
    {
        traindocnum=4000;
        for(int i=0;i<50;i++)
        {
            Collections.shuffle(AllDocList);
            knntest();
        }
        writeResult(wb,"bayes");
        writeResult(wk,"knn");
    }

    public void knntest()
    {        
        readTrainingData();
        double bayesaccuracy=findTopic();
        //System.out.println();
        trainDocList.clear();       
        wb+=bayesaccuracy+"\r\n";
        calIDF();
        calTf();
        double knnaccuracy=calTf_IDF();
        wk+= knnaccuracy+"\r\n";
        System.out.println("bayes "+bayesaccuracy+"knn "+knnaccuracy);
        for(int i=0;i<traindocnum;i++)
        {
            Document d=(Document)AllDocList.get(i);
            d.getTfVal().clear();
        }
        trainwords.clear();
    }

    public void readTrainingData()
    {
        trainDocList.clear();
        Enumeration et=topiclist.keys();
        while(et.hasMoreElements())
        {
            String s=(String)et.nextElement();
            Document doc=new Document(s);
            trainDocList.add(doc);
        }
        
        int index=-1;
        for(int i=0;i<traindocnum;i++)
        {
            Document d=(Document)AllDocList.get(i);
            index=(Integer)topiclist.get(d.getLabel());
            Document doc=(Document)trainDocList.get(index);
            doc.setDocCount(doc.getDocCount()+1);            
            totaldoc++;
            Enumeration e=d.getWords().keys();
            while(e.hasMoreElements())
            {
                String s=(String)e.nextElement();
                Integer num=(Integer)d.getWords().get(s);
                if(!doc.getWords().containsKey(s))
                {
                    doc.getWords().put(s,new Integer(num));
                }
                else
                {
                    Integer count=(Integer)doc.getWords().get(s);
                    doc.getWords().put(s,new Integer(count+num));
                }
                doc.setWordCount(doc.getWordCount()+num);                
                if(!trainwords.containsKey(s))
	                trainwords.put(s, new Integer(1));
            }
        }
        numwords=trainwords.size();
        //trainwords.clear();
        for(int i=0;i<trainDocList.size();i++)
        {
            Document d=(Document)trainDocList.get(i);
            //System.out.println(d.wordcount);
            if(d.getDocCount()==0 && d.getWordCount()==0)
                trainDocList.remove(d);
        }        
    }

    public void readTestData()
    {
        int i=0;
    	Document doc=new Document();    	
    	try{
    		BufferedReader bin=new BufferedReader(new FileReader(new File("test.data")));
        	String input="";    		    	           
        	while(true) 
        	{        
            	try 
            	{
                	input=bin.readLine();                	
            	} 
            	catch(Exception ex)
            	{
                	System.out.println("Exception in readline: "+ex);
            	}    	    	    	                           
            	if(input==null)
                	break;
            	if(input.equals(""))
                	continue;                        
            	StringTokenizer st=new StringTokenizer(input);
            	int x=st.countTokens();                
            	if(x==1) // found a label
            	{
                	String str=st.nextToken();                    	
                	str=removeNoise(str);
                	if(str==null)
                    	continue;                	
                    if(topiclist.containsKey(str))
                	{
                        if(i==2000)
                            break;
                		doc=new Document(str);                        
                        AllDocList.add(doc);
                        i++;
                	}
            	}				            
	            while(st.hasMoreTokens()) 
	            {
	                String str=st.nextToken(); 
	                str=removeNoise(str);
	                if(str==null)
	                    continue;	                
	                if(!doc.getWords().containsKey(str))
	                	doc.getWords().put(str,new Integer(1));
	                else
	                {
	                	Integer count=(Integer)doc.getWords().get(str);	                	
	                	doc.getWords().put(str,new Integer(count+1));
	                }
	                doc.setWordCount(doc.getWordCount()+1);                    
	            }	                    	                      	        
        	}        	
	        try 
	        {
	            bin.close();
	        } 
	        catch(Exception ex) 
	        {
	            System.out.println("Exception: "+ex);
	        }                
    	}
    	catch(Exception rx)
    	{
    		System.out.println("Exception: "+rx);
    	}
        System.out.println("doc number "+AllDocList.size());
        noiseArrList.clear();        
    }
   
    public void readAllData()
    {
        int i=0;
        Document doc=new Document();
    	try{
    		BufferedReader bin=new BufferedReader(new FileReader(new File("training.data")));
        	String input="";
        	while(true)
        	{
            	try
            	{
                	input=bin.readLine();
            	}
            	catch(Exception ex)
            	{
                	System.out.println("Exception in readline: "+ex);
            	}
            	if(input==null)
                	break;
            	if(input.equals(""))
                	continue;
            	StringTokenizer st=new StringTokenizer(input);
            	int x=st.countTokens();
            	if(x==1) // found a label
            	{
                	String str=st.nextToken();
                	str=removeNoise(str);
                	if(str==null)
                    	continue;
                    if(topiclist.containsKey(str))                	
                	{
                        if(i==4000)
                            break;
                		doc=new Document(str);
                		AllDocList.add(doc);
                        i++;
                	}
            	}
	            while(st.hasMoreTokens())
	            {
	                String str=st.nextToken();
	                str=removeNoise(str);
	                if(str==null)
	                    continue;
                    if(!doc.getWords().containsKey(str))
                    {
                    	doc.getWords().put(str,new Integer(1));
                    }
                    else
                    {
                    	Integer count=(Integer)doc.getWords().get(str);
                    	doc.getWords().put(str,new Integer(count+1));
                    }
                    doc.setWordCount(doc.getWordCount()+1);                    
	            }
        	}
	        try
	        {
	            bin.close();
	        }
	        catch(Exception ex)
	        {
	            System.out.println("Exception: "+ex);
	        }
    	}
    	catch(Exception rx)
    	{
    		System.out.println("Exception: "+rx);
    	}    	
    }

    public void readTopics()
    {
        topiclist.clear();       
        //
        try{
    		BufferedReader bin=new BufferedReader(new FileReader(new File("topics.data")));
        	String input="";
        	while(true)
        	{
            	try
            	{
                	input=bin.readLine();
            	}
            	catch(Exception ex)
            	{
                	System.out.println("Exception in readline: "+ex);
            	}
            	if(input==null)
                	break;
            	if(input.equals(""))
                	continue;
            	StringTokenizer st = new StringTokenizer(input);
            	String str = st.nextToken();
            	str=removeNoise(str);
            	if(str==null)
                    continue;
            	topiclist.put(str, new Integer(0));
        	}
            Enumeration et=topiclist.keys();
            int i=0;
        	while(et.hasMoreElements())
        	{
        		String s=(String)et.nextElement();
                topiclist.put(s, new Integer(i));                
                i++;        		
        	}
           
	        System.out.println("Topic: " + topiclist.size());
            //System.out.println("Topic: " + test2.size());
	        try
	        {
	            bin.close();
	        }
	        catch(Exception ex)
	        {
	            System.out.println("Exception to close: "+ex);
	        }
        }
    	catch(Exception rx)
    	{
    		System.out.println("Exception to read topic: "+rx);
    	}
    }

    public void writeResult(String s, String name)
    {
        String filename;
         if(name.matches("knn"))
             filename="knn.data";
         else
             filename="nb.data";
        char buffer[]=new char[s.length()];
        s.getChars(0, s.length(), buffer, 0);
        FileWriter fw = null;
        try
        {
            fw = new FileWriter(filename,false);
        }
        catch (IOException ex)
        {
            System.out.println("Exception in write file: "+ex);
        }
        try
        {
            fw.write(buffer);
        }
        catch (IOException ex)
        {
            System.out.println("Exception in write file: "+ex);
        }
        try
        {
            fw.close();
        }
        catch (IOException ex)
        {
            System.out.println("Exception: "+ex);
        }
    } 
}
