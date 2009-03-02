import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

public class Simulation {
  // Parameters
  public int N, M, G, L;
  public double lambda;
  public Distribution dist;
  
  // Variables
  public int nt, k, g;
  public double clock;
  
  // Metrics
  double[] f_response_times;
  int[] f_response_counts;
  double i_response_times;
  int i_response_count;
  
  // Sets
  public PriorityQueue<Event> event_set;
  public Queue<Request> i_item_request_set;
  public ArrayList<Request> request_set;
  
  public Simulation(int N, int M, int G, double lambda, Distribution dist, int L) {
    // Parameters
    this.N = N;  // Number of users
    this.M = M;  // Number of f-items
    this.G = G;  // Frequency of i-item checks
    this.lambda = lambda;  // Arrival rate of i-requests
    this.dist = dist;  // f-item request distribution
    this.L = L;  // Running length of simulation
  }
  
  public void run() {
    // Metrics
    f_response_times = new double[M+1];
    f_response_counts = new int[M+1];
    for(int m = 1; m <= M; m++) {  
      f_response_times[m] = 0;
      f_response_counts[m] = 0;
    }
    i_response_times = 0;
    i_response_count = 0;
    
    // Initialization
    clock = 0;
    nt = N;
    event_set = new PriorityQueue<Event>();
    i_item_request_set = new LinkedList<Request>();
    request_set = new ArrayList<Request>();
    for(int n = 1; n <= N; n++) {
      double think_t = ExponentialDistribution.getValue(5);
      event_set.add(new ArrivalEvent(this, clock + think_t));
    }
    k = 1;
    g = 1;
    event_set.add(new StartTransmitEvent(this, clock, k));
    event_set.add(new EndSimulationEvent(this, L));
    
    // Initialization for i-items
    double interarrival_t = ExponentialDistribution.getValue(1.0/lambda);
    event_set.add(new IArrivalEvent(this, clock + interarrival_t));
    
    // Event loop
    while(!(event_set.peek() instanceof EndSimulationEvent)) {
      // Update the clock
      Event e = event_set.poll();
      clock = e.time;
      // Handle the event
      // See each event class for their specific implementation
      e.handle();
    }
    
    // At the end of the simulation print out all the metrics we gathered
    System.out.println("Simulation Over");
    System.out.println("Metrics:");
    System.out.println("  F-items:");
    int f_response_count_total = 0;
    double f_response_time_total = 0;
    for(int m=1; m <= M; m++) {
      f_response_count_total += f_response_counts[m];
      f_response_time_total += f_response_times[m];
      System.out.println("    Item " + m + ":");
      System.out.println("      count=" + f_response_counts[m]);
      System.out.println("      mean =" + f_response_times[m] / f_response_counts[m]);
    }
    System.out.println("    Total:");
    System.out.println("      count=" + f_response_count_total);
    System.out.println("      mean =" + f_response_time_total / f_response_count_total);
    System.out.println("  I-items:");
    System.out.println("    count=" + i_response_count);
    System.out.println("    mean =" + i_response_times / i_response_count);
    System.out.println("TOTAL:");
    System.out.println("  Count = " + i_response_count + f_response_count_total);
    System.out.println("  Mean  = " + (i_response_times + f_response_time_total)/(i_response_count + f_response_count_total));
  }
  
  // Private methods used from getting parameters as input from the user for testing
  private static int getIntFromUser(Scanner s, String prompt, int min, int max) {
    int ret;
    while(true) {
      System.out.println(prompt);
      try {
        ret = s.nextInt();
        if (ret < min || ret > max) {
          System.out.println("Input out of range");
          continue;
        }
        break;
      } catch (InputMismatchException e){
        s.nextLine();
        continue;
      }
    }
    return ret;
  }
  
  private static double getDoubleFromUser(Scanner s, String prompt, double min, double max) {
    double ret;
    while(true) {
      System.out.println(prompt);
      try {
        ret = s.nextDouble();
        if (ret < min || ret > max) {
          System.out.println("Input out of range");
          continue;
        }
        break;
      } catch (InputMismatchException e){
        s.nextLine();
        continue;
      }
    }
    return ret;
  }
  
  // Main method
  public static void main(String[] args) {
    // Parameters
    int N, M, G;
    double lambda;
    Distribution dist;
    Scanner s = new Scanner(System.in);
    
    // Get input from user
    N = getIntFromUser(s, "Please enter number of users, N", 1, Integer.MAX_VALUE);
    lambda = getDoubleFromUser(s, "Please enter arrival rate of i-requests, Lambda", Double.MIN_VALUE, Double.MAX_VALUE);
    M = getIntFromUser(s, "Please enter number f-items, M", 1, Integer.MAX_VALUE);
    G = getIntFromUser(s, "Please enter frequency of checking i-items, G", 1, Integer.MAX_VALUE);
    int dist_id = getIntFromUser(s, "Please enter the probability distribution for p_m\n  1 - Uniform\n  2 - Zipf", 1, 2);
    if(dist_id == 1) dist = new UniformDistribution();
    else dist = new ZipfDistribution();

    // Run the simulation
    Simulation sim = new Simulation(N, M, G, lambda, dist, 100000);
    sim.run();
  }
}