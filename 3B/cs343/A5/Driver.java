
public class Driver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int numPhil = 5;
		int noodles = 30;
		
		if (args.length > 2) {
			System.out.println("Usage: ./phil [ P [ N ] ], P = # philosophers >= 2, N = # noodles >= 1");
			System.exit(1);
		}
		
		try {
			numPhil = Integer.parseInt(args[0]);
			noodles = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			System.out.println("Usage: ./phil [ P [ N ] ], P = # philosophers >= 2, N = # noodles >= 1");
			System.exit(1);
	    } catch (ArrayIndexOutOfBoundsException e) {
			// Continue to just use default values
		}
	    
	    if (numPhil <= 1 || noodles <= 0) {
			System.out.println("Usage: ./phil [ P [ N ] ], P = # philosophers >= 2, N = # noodles >= 1");
			System.exit(1);
	    }
		
		Printer prt = new Printer(numPhil);
		Table table = new Table(numPhil, prt);
		
		Philosopher[] philosophers = new Philosopher[numPhil];
		
		for (int i = 0; i < numPhil; i++) {
			philosophers[i] = new Philosopher(i, noodles, table, prt);
			philosophers[i].start();
		}
		
		for (int i = 0; i < numPhil; i++) {
			try {
				philosophers[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("***********************");
		System.out.println("Philosophers terminated");
	}
}
