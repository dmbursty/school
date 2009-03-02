// Request is just a holder for an arrival time and an item
public class Request {
  public double t_arrival;
  public int item;
  public Request(double t_arrival, int item) {
     this.t_arrival = t_arrival;
     this.item = item;
  }
}
