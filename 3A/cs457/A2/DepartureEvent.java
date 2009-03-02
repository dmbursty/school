public class DepartureEvent extends Event {
  double t_arrival;
  int item;
  String type;
  public DepartureEvent(Simulation sim, double time, double t_arrival, int item, String type) {
    super(sim, time);
    this.t_arrival = t_arrival;
    this.item = item;
    this.type = type;
  }
  void handle() {
    // Depending on the type of departure, record our metrics
    if(type.equals("i-item")){
      sim.i_response_count++;
      sim.i_response_times += (time - t_arrival);
    } else if (type.equals("f-item")) {
      sim.f_response_counts[item]++;
      sim.f_response_times[item] += (time - t_arrival);
      // This handling is the same as notes 5
      sim.nt++;
      double think_t = ExponentialDistribution.getValue(5);
      sim.event_set.add(new ArrivalEvent(sim, sim.clock + think_t));
    }
  }
}
