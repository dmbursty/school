
public class Printer {
	private int noOfPhil;
	private int last_flush = -1;
	private int last_id = -1;
	private int soloing = -1;
	private BufferSlot[] buffer;

	private class BufferSlot {
		public Philosopher.States state;
		public boolean set = false;
		public int bite;
		public int noodles;
	}

	public Printer(int noOfPhil) {
		this.noOfPhil = noOfPhil;
		this.buffer = new BufferSlot[noOfPhil];
		for (int i = 0; i < noOfPhil; i++) {
			this.buffer[i] = new BufferSlot();
			System.out.print("Phil" + i + "\t");
		}
		System.out.println("");
		for (int i = 0; i < noOfPhil; i++) {
			System.out.print("******\t");
		}
		System.out.println("");
	}

	public synchronized void print(int id, Philosopher.States state) {
		print(id, state, -1, -1);
	}

	public synchronized void print(int id, Philosopher.States state, int bite, int noodles) {
		// We will get a thinking before finishing, so ignore it
		if (state == Philosopher.States.THINKING && buffer[id].noodles == 0) {
			return;
		}
		// Validate that current state is valid
		errorCheck();

		// Special print on a finish
		if (state == Philosopher.States.FINISHED) {
			if (last_flush != id || last_id != id) {
				flush(id);
			}
			finish(id);
			buffer[id].state = state;
			buffer[id].set = false;
			last_flush = id;
			last_id = id;
			return;
		}

		if (buffer[id].set) {
			if (id == last_flush && id == last_id) {
				if (id != soloing) {
					printOnlyMe(id, buffer[id].state, buffer[id].bite, buffer[id].noodles);
					soloing = id;
				}
				printOnlyMe(id, state, bite, noodles);
			} else {
				flush(id);
				soloing = -1;
				last_flush = id;
			}
		}
		buffer[id].set = true;
		buffer[id].state = state;
		buffer[id].bite = bite;
		buffer[id].noodles = noodles;
		last_id = id;
	}

	private void errorCheck() {
		for (int i = 0; i < noOfPhil; i++) {
			int left = (i == 0 ? noOfPhil - 1 : i - 1);
			int right = (i+1) % noOfPhil;
			if (buffer[i].set) {
				switch (buffer[i].state) {
				case HUNGRY:
				case THINKING:
					break;  // No restrictions
				case EATING:
					assert(buffer[left].state != Philosopher.States.EATING);
					assert(buffer[right].state != Philosopher.States.EATING);
					break;
				case WAITING:
					break;
				case FINISHED:
					assert(!buffer[i].set);
					break;
				}
			}
		}
	}

	private void flush(int id) {
		for (int i = 0; i < noOfPhil; i++) {
			if (!buffer[i].set) {
				System.out.print("\t");
				continue;
			}
			// Print State
			switch (buffer[i].state) {
			case HUNGRY:
				System.out.print("H"); break;
			case THINKING:
				System.out.print("T"); break;
			case EATING:
				System.out.print("E" + buffer[i].bite + "," + buffer[i].noodles); break;
			case WAITING:
				System.out.print("W" + i + "," + ((i+1)%noOfPhil)); break;
			case FINISHED:
				break;  // Should never happen
			}
			if (i == id) {
				System.out.print("*");
			}
			System.out.print("\t");
		}
		System.out.println("");
	}

	private void finish(int id) {
		for (int i = 0; i < noOfPhil; i++) {
			System.out.print(i == id ? "F\t" : "...\t");
		}
		System.out.println("");
	}

	private void printOnlyMe(int id, Philosopher.States state, int bite, int noodles) {
		for (int i = 0; i < noOfPhil; i++) {
			if (id == i) {
				// Print State
				switch (state) {
				case HUNGRY:
					System.out.print("H"); break;
				case THINKING:
					System.out.print("T"); break;
				case EATING:
					System.out.print("E" + bite + "," + noodles); break;
				case WAITING:
					System.out.print("W" + i + "," + ((i+1)%noOfPhil)); break;
				case FINISHED:
					break;  // Should never happen
				}
				System.out.print("\t");
			} else {
				System.out.print("\t");
			}
		}
		System.out.println("");
	}
}
