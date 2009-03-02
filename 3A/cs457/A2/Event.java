public abstract class Event implements Comparable<Event> {
  Simulation sim;
  double time;

  Event(Simulation sim, double time) {
    this.sim = sim;
    this.time = time;
  }

  public int compareTo(Event e) {
    if (this.time < e.time) {
      return -1;
    } else if (this.time > e.time) {
      return 1;
    }
    return 0;
  }

  abstract void handle();
}