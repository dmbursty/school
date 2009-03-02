import java.util.Iterator;

public class StartTransmitEvent extends Event {
  public int item;
  
  public StartTransmitEvent(Simulation sim, double time, int item) {
    super(sim, time);
    this.item = item;
  }
  void handle() {
    // Check if it's time to serve an i-request
    if (sim.g == sim.G) {
      // Reset the i-request counter
      sim.g = 0;
      // Are there any i-requests in the queue?
      if(sim.i_item_request_set.size() != 0) {
        // Schedule departure and transmit events
        double transmit_t = ExponentialDistribution.getValue(1.0);
        Request r = sim.i_item_request_set.poll();
        sim.event_set.add(new DepartureEvent(sim, sim.clock + transmit_t, r.t_arrival, r.item,"i-item"));
        sim.event_set.add(new StartTransmitEvent(sim, sim.clock + transmit_t, sim.k));
        return;
      }
    }
    // Handle an f-request
    // This is mostly the same as the slide pseudocode
    double transmit_t = ExponentialDistribution.getValue(1.0);
    for (Iterator<Request> iter = sim.request_set.iterator(); iter.hasNext();) {
      Request r = iter.next();
      if (r.item == this.item) {
        iter.remove();
        sim.event_set.add(new DepartureEvent(sim, sim.clock + transmit_t, r.t_arrival, r.item, "f-item"));
      }
    }
    sim.k++;
    // This counter keeps track of when we need to check i-requests
    sim.g++;
    if (sim.k > sim.M) sim.k = 1;
    sim.event_set.add(new StartTransmitEvent(sim, sim.clock + transmit_t, sim.k));
  }
}
