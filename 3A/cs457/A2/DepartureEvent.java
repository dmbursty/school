public class DepartureEvent extends Event {
  double t_arrival;
  public DepartureEvent(Simulation sim, double time, double t_arrival) {
    super(sim, time);
    this.t_arrival = t_arrival;
  }
  void handle() {
    System.out.println("Departure at " + time);
    sim.nt++;
    double think_t = ExponentialDistribution.getValue(5);
    sim.event_set.add(new ArrivalEvent(sim, sim.clock + think_t));
  }
}
