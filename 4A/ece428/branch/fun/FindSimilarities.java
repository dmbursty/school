import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.collections.bag.TreeBag;

public class FindSimilarities {

	public static void main(String[] args) throws Exception {
		findCommonStrings(System.in);
	}

	public static void findCommonStrings(InputStream in) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		TreeBag stringBag = new TreeBag();
		int character;
		StringBuilder sb = new StringBuilder();

		while ((character = br.read()) != -1) {
			if(character == '|') {
				stringBag.add(sb.toString().substring(0, 15));
				sb = new StringBuilder();
			} else {
				sb.append(new Character((char)character).toString());
			}
		}
		br.close();

		System.out.println(stringBag.uniqueSet());
		System.out.println("stringBag.size() : " + stringBag.size());
		System.out.println("stringBag.uniqueSet().size() : " + stringBag.uniqueSet().size());
	}
}
