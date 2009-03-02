import java.util.Iterator;

public class StartTransmitEvent extends Event {
  public int item;
  
  public StartTransmitEvent(Simulation sim, double time, int item) {
    super(sim, time);
    this.item = item;
  }
  void handle() {
    System.out.println("Transmit at " + time);
    if (sim.g == sim.G) {
      sim.g = 0;
      if(sim.i_item_request_set.size() != 0) {
        double transmit_t = ExponentialDistribution.getValue(1.0);
        Request r = sim.i_item_request_set.poll();
        sim.event_set.add(new DepartureEvent(sim, sim.clock + transmit_t, r.t_arrival));
        sim.event_set.add(new StartTransmitEvent(sim, sim.clock + transmit_t, sim.k));
        return;
      }
    }
    double transmit_t = ExponentialDistribution.getValue(1.0);
    for (Iterator<Request> iter = sim.request_set.iterator(); iter.hasNext();) {
      Request r = iter.next();
      if (r.item == this.item) {
        iter.remove();
        sim.event_set.add(new DepartureEvent(sim, sim.clock + transmit_t, r.t_arrival));
      }
    }
    sim.k++;
    sim.g++;
    if (sim.k > sim.M) sim.k = 1;
    sim.event_set.add(new StartTransmitEvent(sim, sim.clock + transmit_t, sim.k));
  }
}
