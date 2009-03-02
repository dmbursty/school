import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class Simulation {
  // Constants
  public int N, M, G;
  public double lambda;
  public Distribution dist;
  
  // Variables
  public int nt, k, g;
  public double clock;
  
  // Sets
  public PriorityQueue<Event> event_set;
  public Queue<Request> i_item_request_set;
  public ArrayList<Request> request_set;
  
  public Simulation(int N, int M, int G, double lambda, Distribution dist) {
    // Constants
    this.N = N;
    this.M = M;
    this.G = G;
    this.lambda = lambda;
    this.dist = dist;
  }
  
  public void run() {
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
    event_set.add(new EndSimulationEvent(this, 100));
    
    double interarrival_t = ExponentialDistribution.getValue(1.0/lambda);
    event_set.add(new IArrivalEvent(this, clock + interarrival_t));
    
    // Event loop
    while(!(event_set.peek() instanceof EndSimulationEvent)) {
      Event e = event_set.poll();
      clock = e.time;
      e.handle();
    }
    System.out.println("Simulation Over");
  }

  public static void main(String[] args) {
    Simulation sim = new Simulation(35, 20, 2, 0.2, new UniformDistribution());
    sim.run();
  }
}