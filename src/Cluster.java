import java.util.List;

public class Cluster {
    private int ageCentroid;
    private int incomeCentroid;
    private int scoreCentroid;
    private int clusterNumber;

    public Cluster(int clusterNumber,int ageCentroid,int incomeCentroid,int scoreCentroid) {
        super();
        this.clusterNumber = clusterNumber;
        this.ageCentroid = ageCentroid;
        this.incomeCentroid = incomeCentroid;
        this.scoreCentroid = scoreCentroid;

    }

    public int getAgeCentroid() {
        return ageCentroid;
    }

    public void setAgeCentroid(int ageCentroid) {
        this.ageCentroid = ageCentroid;
    }

    public int getIncomeCentroid() {
        return incomeCentroid;
    }

    public void setIncomeCentroid(int incomeCentroid) {
        this.incomeCentroid = incomeCentroid;
    }

    public int getScoreCentroid() {
        return scoreCentroid;
    }

    public void setScoreCentroid(int scoreCentroid) {
        this.scoreCentroid = scoreCentroid;
    }

    public int getClusterNumber() {
        return clusterNumber;
    }

    public void setClusterNumber(int clusterNumber) {
        this.clusterNumber = clusterNumber;
    }

    @Override
    public String toString() {
        return "Cluster{" +
                "ageCentroid=" + ageCentroid +
                ", incomeCentroid=" + incomeCentroid +
                ", scoreCentroid=" + scoreCentroid +
                ", clusterNumber=" + clusterNumber +
                '}';
    }

    //euclidien distance calculations ==> square((x-y)^2)
    public double calculateDistance(Record record){
        return Math.sqrt(Math.pow(getAgeCentroid()- record.getAge(),2)+Math.pow(getIncomeCentroid()- record.getIncome(),2)+Math.pow(getScoreCentroid() - record.getScore(), 2));
    }

    //update the center of the cluster to the middle between the two point (first center and new added point to the cluster )

    public void updateCentroid(List<Record> records) {
        if (records.isEmpty()) return;

        int sumAge = 0, sumIncome = 0, sumScore = 0;

        for (Record r : records) {
            sumAge += r.getAge();
            sumIncome += r.getIncome();
            sumScore += r.getScore();
        }

        setAgeCentroid(sumAge / records.size());
        setIncomeCentroid(sumIncome / records.size());
        setScoreCentroid(sumScore / records.size());
    }

    //This checks if two clusters have the same centroid.
    public boolean hasConverged(Cluster other) {
        return this.ageCentroid == other.ageCentroid &&
                this.incomeCentroid == other.incomeCentroid &&
                this.scoreCentroid == other.scoreCentroid;
    }




}
