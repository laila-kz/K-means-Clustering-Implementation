import java.util.*;
import java.io.File;


public class Kmeans {

    List<Record> data =new ArrayList<Record>();
    List<Cluster> clusters =new ArrayList<Cluster>();
    Map<Cluster,List<Record>> clusterRecords = new HashMap<Cluster,List<Record>>();

    public static void main(String[] args){
        if (args.length < 3) {
            System.out.println("Usage: java Kmeans <csv_path> <number_of_clusters> <max_iterations>");
            return;
        }

        String csvPath = args[0];
        int clusterNumber = Integer.parseInt(args[1]);
        int maxIterations = Integer.parseInt(args[2]);
        Kmeans demo = new Kmeans();
        demo.loadDataFromCSV(csvPath); // instead of generateRecord()
        // Check if the fourth argument is "elbow"
        if (args.length == 4 && args[3].equalsIgnoreCase("elbow")) {
            demo.runElbowMethod(10, maxIterations); // test K=1..10
            return; // exit after running elbow
        }
        demo.runKMeans(clusterNumber, maxIterations);
        demo.printRecordInformation();
        demo.printClusterInformation();

    }
    //now If centroids stop moving before maxIterations, the loop ends early.
    private void runKMeans(int clusterNumber, int maxIterations) {
        // Step 1: initialize clusters
        // Shuffle the data randomly
        Collections.shuffle(data);

// Initialize clusters with the first 'clusterNumber' records after shuffle
        for (int i = 0; i < clusterNumber; i++) {
            Record record = data.get(i);
            record.setClusterNumber(i + 1);
            initializeCluster(i + 1, record);
        }


        // Step 2: repeat assignment + centroid update
        for (int iter = 0; iter < maxIterations; iter++) {
            // Clear old assignments
            for (Cluster cluster : clusters) {
                clusterRecords.put(cluster, new ArrayList<>());
            }

            // Assign each record to nearest cluster
            for (Record record : data) {
                double minDistance = Double.MAX_VALUE;
                Cluster whichCluster = null;

                for (Cluster cluster : clusters) {
                    double distance = cluster.calculateDistance(record);
                    if (distance < minDistance) {
                        minDistance = distance;
                        whichCluster = cluster;
                    }
                }

                record.setClusterNumber(whichCluster.getClusterNumber());
                clusterRecords.get(whichCluster).add(record);
            }

            // ✅ Save old centroids before updating
            List<Cluster> oldClusters = new ArrayList<>();
            for (Cluster c : clusters) {
                oldClusters.add(new Cluster(
                        c.getClusterNumber(),
                        c.getAgeCentroid(),
                        c.getIncomeCentroid(),
                        c.getScoreCentroid()
                ));
            }

            // Update centroids
            for (Cluster cluster : clusters) {
                cluster.updateCentroid(clusterRecords.get(cluster));
            }

            // ✅ Check convergence
            boolean allConverged = true;
            for (int i = 0; i < clusters.size(); i++) {
                if (!clusters.get(i).hasConverged(oldClusters.get(i))) {
                    allConverged = false;
                    break;
                }
            }

            System.out.println("Iteration " + (iter + 1));
            for (Cluster cluster : clusters) {
                System.out.println(cluster);
            }

            if (allConverged) {
                System.out.println("✅ Converged at iteration " + (iter + 1));
                break;
            }
        }
    }

    //adding the elbow method which evaluates the sum of squared errors SSE for diffrent K values and helps pick the optimal cluster number
    public void runElbowMethod(int maxK, int maxIterations) {
        System.out.println("=== Elbow Method ===");
        for (int k = 1; k <= maxK; k++) {
            runKMeans(k, maxIterations);
            double sse = calculateSSE();
            System.out.println("K=" + k + " -> SSE=" + sse);
            // Clear clusters and assignments for the next iteration
            clusters.clear();
            clusterRecords.clear();
            for (Record r : data) {
                r.setClusterNumber(0);
            }
        }
    }

    private double calculateSSE() {
        double sse = 0.0;
        for (Cluster cluster : clusters) {
            List<Record> records = clusterRecords.get(cluster);
            if (records == null) continue;
            for (Record record : records) {
                double distance = cluster.calculateDistance(record);
                sse += distance * distance;
            }
        }
        return sse;
    }



    private void loadDataFromCSV(String filePath) {
        try (Scanner scanner = new Scanner(new File(filePath))) {
            scanner.nextLine(); // skip header if present
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(",");
                int id = Integer.parseInt(parts[0]);
                int age = Integer.parseInt(parts[1]);
                int income = Integer.parseInt(parts[2]);
                int score = Integer.parseInt(parts[3]);

                data.add(new Record(id, age, income, score));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initiateClusterAndCentroid(int clusterNumber) {
        int counter = 1;
        Iterator<Record> iterator = data.iterator();
        Record record = null;

        while (iterator.hasNext()) {
            record = iterator.next();

            if (counter <= clusterNumber) {
                // Initialize first points as centroids
                record.setClusterNumber(counter);
                initializeCluster(counter, record);
                counter++;
            } else {
                // Assign record to nearest cluster
                double minDistance = Double.MAX_VALUE;
                Cluster whichCluster = null;

                for (Cluster cluster : clusters) {
                    double distance = cluster.calculateDistance(record);
                    if (minDistance > distance) {
                        minDistance = distance;
                        whichCluster = cluster;
                    }
                }

                record.setClusterNumber(whichCluster.getClusterNumber());
                clusterRecords.get(whichCluster).add(record);
            }
        }

        // ✅ Recalculate all centroids after assignment
        for (Cluster cluster : clusters) {
            cluster.updateCentroid(clusterRecords.get(cluster));
        }

        System.out.println("** Cluster Information **");
        for (Cluster cluster : clusters) {
            System.out.println(cluster);
        }
        System.out.println("*********************");
    }

    private void initializeCluster(int clusterNumber , Record record){
        Cluster cluster = new Cluster(clusterNumber , record.getAge(), record.getIncome(), record.getScore());
        clusters.add(cluster);
        List<Record> clusterRecord = new ArrayList<Record>();
        clusterRecord.add(record);
        clusterRecords.put(cluster,clusterRecord);
    }

    private void printRecordInformation() {
        System.out.println("****** Each Record INFORMATIN *********");
        for(Record record : data) {
            System.out.println(record);
        }
    }

    private void printClusterInformation() {
        System.out.println("=== FINAL CLUSTERS ===");
        for (Map.Entry<Cluster, List<Record>> entry : clusterRecords.entrySet()) {
            System.out.println("Cluster " + entry.getKey().getClusterNumber() + " Centroid: "
                + "(" + entry.getKey().getAgeCentroid() + ", "
                + entry.getKey().getIncomeCentroid() + ", "
                + entry.getKey().getScoreCentroid() + ")");
            for (Record r : entry.getValue()) {
                System.out.println("   -> " + r);
            }
        }
    }


}
