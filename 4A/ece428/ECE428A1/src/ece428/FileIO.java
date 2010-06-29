package ece428;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * This class includes utility methods to read/write players to the appropriate files.
 * 
 * @author yubin.kim
 *
 */
public class FileIO {
    
    /**
     * Write the names of the given players to outputFile
     */
    public static void writePlayersToFile(String country, List<Player> teamPlayers, File outputFile) throws IOException {
        PrintStream output = new PrintStream(new FileOutputStream(outputFile));

        try {
            if (teamPlayers.size() == 0) {
                output.println(country + " did not qualify to the world cup");
            } else {
                for (Player player : teamPlayers) {
                    output.println(player.getName());
                }
            }
        } finally {
            output.close();
        }
    }

    /**
     * Read the players from an inputFile
     */
    public static List<Player> readPlayersFromFile(File inputFile) throws IOException {
        List<Player> ret = new ArrayList<Player>();

        BufferedReader input = new BufferedReader(new FileReader(inputFile));
        try {

            String line;
            while ((line = input.readLine()) != null) {
                String[] splitLine = line.split("\\s"); // split on white space
                if (splitLine == null || splitLine.length != 2 || splitLine[0].isEmpty()
                        || splitLine[1].isEmpty()) {
                    break; // read until stuff dosn't look right
                }

                ret.add(new Player(splitLine[0], splitLine[1]));
            }
        } finally {
            input.close();
        }

        return ret;
    }
}
