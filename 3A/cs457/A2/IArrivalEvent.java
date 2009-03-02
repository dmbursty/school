// Arrival event for i-items
public class IArrivalEvent extends Event {
  public IArrivalEvent(Simulation sim, double time) {
    super(sim, time);
  }
  void handle() {
    // This is handled similarily to f-request arrivals, except modified for an infinite population model
    sim.i_item_request_set.add(new Request(time, 0));
    double interarrival_t = ExponentialDistribution.getValue(1.0/sim.lambda);
    sim.event_set.add(new IArrivalEvent(sim, sim.clock + interarrival_t));
  }

}
