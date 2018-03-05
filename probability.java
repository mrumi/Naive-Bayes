package Naive_Bayes;

public class probability implements Comparable<probability> {
	
	private String label;
    private double prob;
    public probability(String l, double p) {    
        this.label = l;
        this.prob = p;
    }

    public String getLabel() {    
        return this.label;
    }

    public double getProb() {    
        return this.prob;
    }

    public int compareTo( probability ob){        
        if(this.getProb() < ob.getProb()) {        
            return 1;
        }
        else if(this.getProb() > ob.getProb()) {        
            return -1;
        }
        else {
            return 0;
        }
    }
}
