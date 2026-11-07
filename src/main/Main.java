package main;

import java.util.Random;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws Exception {
		Scanner sc = new Scanner(System.in);
		Random rng = new Random();
		Codeword codeword;
		int[] errorword;

		if (args.length != 0) {
			codeword = new Codeword(args[0]);
		} else {
			String input = "10100101101";
			codeword = new Codeword(input);
		}

		System.out.print("Codeword: ");
		for (int i : codeword.getCodeword()) {
			System.out.print(i);
		}
		System.out.println();

		// Make a working copy so the original codeword stays pristine
		// for parityCheck to compare against
		errorword = new int[codeword.getCodeword().length];
		for (int i = 0; i < errorword.length; i++) {
			errorword[i] = codeword.getCodeword()[i];
		}

		System.out.print("Introduce error? (y/n)\n> ");
		while (true) {
			String userInput = sc.nextLine();

			if (userInput.equalsIgnoreCase("y")) {
				int rIndex = rng.nextInt(codeword.getCodeword().length);
				errorword[rIndex] ^= 1;   // XOR-flips the chosen bit 0↔1
				break;
			}
			else if (userInput.equalsIgnoreCase("n")) {
				break;
			}
			else {
				System.out.println("Invalid input!");
			}
		}

		System.out.print("Received: ");
		for (int i : errorword) {
			System.out.print(i);
		}
		System.out.println();

		System.out.println(codeword.parityCheck(errorword));
		sc.close();
	}

}
