# K-Means Clustering in Java

A Java implementation of the K-Means clustering algorithm built from scratch. This project demonstrates unsupervised machine learning techniques applied to customer segmentation dataset analysis (`customers.csv`).

---

## Features

- **Custom Algorithm Implementation:** Pure Java logic for distance calculation, centroid initialization, iterative updates, and cluster assignment.
- **Data Ingestion:** CSV reader to parse and extract numeric features from customer data.
- **Flexible Parameters:** Configurable cluster count ($k$), iteration limits, and distance metrics.

---

## Repository Structure

```text
k_means_clustering/
├── .idea/                 # IntelliJ IDEA configuration files
├── src/                   # Source code
├── .gitignore             # Git ignore rules
├── customers.csv          # Sample dataset for customer segmentation
└── k_means_clustering.iml # Project module settings

```

---

## Dataset Overview

The project uses `customers.csv` to perform vector quantization and customer grouping.

* **Target Variables:** Key numerical attributes (e.g., Annual Income, Spending Score, Age) used to group similar observations into distinct clusters.
* **Preprocessing:** Features are parsed and converted into coordinate vectors before clustering.

---

## Mathematical Formulation & Algorithm Lifecycle

K-Means is an iterative, non-deterministic unsupervised learning algorithm that partitions $N$ observations into $K$ distinct, non-overlapping clusters. Each observation $x_i \in \mathbb{R}^d$ is assigned to the cluster with the nearest mean (centroid).

### Objective Function

The optimization target minimizes the **Within-Cluster Sum of Squares (WCSS)**, also known as inertia:

$$J = \sum_{k=1}^{K} \sum_{x_i \in C_k} \Vert{} x_i - \mu_k \Vert{}^2$$

Where:

* $K$ is the number of clusters.
* $C_k$ is the set of data points assigned to cluster $k$.
* $\mu_k$ is the centroid vector (mean) of cluster $k$:

$$\mu_k = \frac{1}{\vert{}C_k\vert{}} \sum_{x_i \in C_k} x_i$$


* $\Vert{} x_i - \mu_k \Vert{}$ represents the Euclidean distance metric ($L_2$ norm):

$$d(p, q) = \sqrt{\sum_{j=1}^{d} (p_j - q_j)^2}$$



---

### Step-by-Step Algorithm Breakdown

1. **Initialization:**
Select $K$ initial cluster centroids $\{\mu_1, \mu_2, \dots, \mu_K\}$ randomly from the dataset or via specific initialization routines (e.g., K-Means++).
2. **Assignment Step (Expectation):**
Assign each point $x_i$ to its closest centroid $\mu_k$:

$$c^{(i)} := \arg\min_k \Vert{} x_i - \mu_k \Vert{}^2$$


3. **Update Step (Maximization):**
Recalculate each centroid $\mu_k$ as the mean coordinate of all points currently assigned to cluster $C_k$:

$$\mu_k := \frac{1}{\vert{}C_k\vert{}} \sum_{i \in C_k} x_i$$


4. **Convergence Check:**
Repeat steps 2 and 3 until the centroids stabilize ($\Delta \mu \le \epsilon$) or the maximum number of iterations is reached.

---

### Visualization of Convergence Process

```text
Iteration 1: Random Centroids       Iteration 5: Centroid Shift        Iteration 12: Convergence (Final)
      
    *    .   (X1)                       *   .   (X1)                       (  * . *  )
   .   *   .                           .  *   .                           (  Cluster 1  )
         .                                  .                              x1 = Centroid
      x2                                 x1
    .    .   *                         .    .   *                         (   . * .  )
   *   .   (X2)                       *   .   (X2)                        (  Cluster 2  )
                                                                           x2 = Centroid

```

---

## Getting Started

### Prerequisites

* **Java Development Kit (JDK):** Version 11 or higher recommended.
* **IDE (Optional):** IntelliJ IDEA, Eclipse, or VS Code.

### Building and Running

1. **Clone the repository:**
```bash
git clone [https://github.com/laila-kz/K-means-Clustering-Implementation.git](https://github.com/laila-kz/K-means-Clustering-Implementation.git)
cd k_means_clustering

```


2. **Compile the source files:**
```bash
javac -d bin src/**/*.java

```


3. **Run the application:**
```bash
java -cp bin Main

```



---

## Example Output

Upon running the program, the console logs iteration status and final vector centroids:

```text
Iterating... Converged at iteration 12.

Cluster 1 Centroid: [Annual Income: 55.4k, Spending Score: 49.2] -> 82 Customer(s)
Cluster 2 Centroid: [Annual Income: 86.5k, Spending Score: 82.1] -> 39 Customer(s)
Cluster 3 Centroid: [Annual Income: 26.3k, Spending Score: 20.9] -> 23 Customer(s)
...

```

```

```
