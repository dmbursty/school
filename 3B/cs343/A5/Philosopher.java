
public class Philosopher extends Thread {
	public enum States {THINKING, HUNGRY, EATING, WAITING, FINISHED};

	private int id;
	private int noodles;
	private Table table;
	private Printer prt;

	@Override
	public void run() {
		while (true) {
			// Start Hungry
			prt.print(id, States.HUNGRY);
			int time = (int)(Math.random() * 5);
			for (int i = 0; i < time; i++) {
				yield();
			}

			// Acquire Forks
			table.pickup(id);

			// Eat
			int bite = (int)(Math.random() * 5) + 1;
			if (bite > noodles) {
				bite = noodles;
			}
			noodles -= bite;
			prt.print(id, States.EATING, bite, noodles);
			time = (int)(Math.random() * 5);
			for (int i = 0; i < time; i++) {
				yield();
			}

			// Put down forks
			table.putdown(id);

			// Check if done noodles
			if (noodles <= 0) {
				break;
			}

			// Think
			time = (int)(Math.random() * 20);
			for (int i = 0; i < time; i++) {
				yield();
			}
		}
		
		// Terminate
		prt.print(id, States.FINISHED);
	}

	public Philosopher(int id, int noodles, Table table, Printer prt) {
		this.id = id;
		this.noodles = noodles;
		this.table = table;
		this.prt = prt;
	}
}
