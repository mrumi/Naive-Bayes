package test;

public class Probability implements Comparable
{
    private String label;
    private double prob;
    public Probability(String l, double p)
    {
        this.label=l;
        this.prob=p;
    }

    public String getLabel()
    {
        return this.label;
    }

    public double getProb()
    {
        return this.prob;
    }

    public int compareTo(Object ob){
        Probability temp=(Probability)ob;

        if(this.getProb()<temp.getProb())
        {
            return 1;
        }
        else if(this.getProb()>temp.getProb())
        {
            return -1;
        }
        else
        {
            return 0;
        }
    }
}
