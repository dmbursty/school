public class Table {
	private int noOfPhil;
	private Printer prt;
	private boolean[] available;
	
	public Table(int noOfPhil, Printer prt) {
		this.noOfPhil = noOfPhil;
		this.prt = prt;
		this.available = new boolean[noOfPhil];
		for (int i = 0; i < noOfPhil; i++) {
			this.available[i] = true;
		}
	}
	
	public synchronized void pickup(int id) {
		if (!available[id] || !available[(id+1)%noOfPhil]) {
			prt.print(id, Philosopher.States.WAITING);
			while (!available[id] || !available[(id+1)%noOfPhil]) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		available[id] = false;
		available[(id+1)%noOfPhil] = false;
	}
	
	public synchronized void putdown(int id) {
		available[id] = true;
		available[(id+1)%noOfPhil] = true;
		// Print thinking before leaving monitor
		prt.print(id, Philosopher.States.THINKING);
		this.notifyAll();
	}
}
