public class ArrivalEvent extends Event {
  public ArrivalEvent(Simulation sim, double time) {
    super(sim, time);
  }

  void handle() {
    System.out.println("Arrival at " + time);
    sim.nt--;
    int item = sim.dist.getItem(sim.M);
    sim.request_set.add(new Request(sim.clock, item));
  }

}
