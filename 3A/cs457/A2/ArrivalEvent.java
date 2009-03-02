public class ArrivalEvent extends Event {
  public ArrivalEvent(Simulation sim, double time) {
    super(sim, time);
  }

  void handle() {
    // This is pretty much the same as the pseudocode from lectures
    sim.nt--;
    int item = sim.dist.getItem(sim.M);
    sim.request_set.add(new Request(sim.clock, item));
  }

}
