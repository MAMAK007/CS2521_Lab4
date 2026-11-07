package main;

public class Codeword {

	private int[] outputVals;

	private static int parityBitsNeeded(int input) {
		int r = 0;
		while (Math.pow(2, r) < (input + r + 1)) {
			r++;
		}
		return r;
	}

	// Checks a codeword for 1-bit errors using the Hamming syndrome.
	// Mutates cw2[] in place if a correction is made; returns a status message.
	public String parityCheck(int[] cw2) throws Exception {
		int[] cw1 = this.outputVals;

		// Number of parity checks = powers of two that fit in the codeword
		// (7 bits -> 3 checks at positions 1, 2, 4; 15 -> 4; 31 -> 5)
		int pbn = 0;
		while ((1 << pbn) <= cw1.length) {
			pbn++;
		}

		if (cw2.length != cw1.length) {
			throw new Exception("Codeword lengths do not match!");
		}

		// Syndrome: for each parity bit p, XOR every position 
		// (including the parity slot itself) whose 1-based index has bit p set. 
		// In a clean codeword every check XORs to 0; each failing check contributes 2^p.
		int syndrome = 0;
		for (int p = 0; p < pbn; p++) {
			int check = 0;
			for (int pos = 0; pos < cw2.length; pos++) {
				if ((((pos + 1) >> p) & 1) == 1) {
					check ^= cw2[pos];
				}
			}
			if (check == 1) {
				syndrome += (1 << p);   // parity p failed — add its weight
			}
		}

		if (syndrome == 0) {
			// Syndrome clean — but a 2-bit error can also yield a clean
			// syndrome, so diff against cw1 to be sure
			for (int i = 0; i < cw1.length; i++) {
				if (cw1[i] != cw2[i]) {
					return "Syndrome clean but codewords differ — 2+ bit error";
				}
			}
			return "It's a match!";
		}
		if (syndrome > cw2.length) {
			return "Inconsistent codeword — more than 1 bit is wrong";
		}

		// The syndrome IS the 1-based position of the flipped bit.
		cw2[syndrome - 1] ^= 1;   // XOR-flips 0↔1
		
		String corrected = "";
		for (int i : cw2) {
			corrected += i;
		}
		
		return "Bit at position " + syndrome + " was flipped.\nCorrected to " + corrected;
	}

	public Codeword(String val) throws Exception {
		int[] workingArray = new int[val.length()];
		int ln = workingArray.length;
		int[] codeword = null;

		// Assign to workingArray
		int pos1 = 0;
		for (char i : val.toCharArray()) {
			// Check for 1 or 0
			if (i == '0') {
				workingArray[pos1] = 0;
			}
			else if (i == '1') {
				workingArray[pos1] = 1;
			}
			// Invalid characters, throw exception
			else {
				throw new Exception("Invalid characters found in input!");
			}
			pos1++;
		}

		// 4, 11, or 26 bits entered
		// Calculate and add parity bits and return 7, 15, or 31 bit array
		if (
			workingArray.length == 4 ||
			workingArray.length == 11 ||
			workingArray.length == 26
			) {

			int pbn = parityBitsNeeded(ln);
			codeword = new int[(ln + pbn)];
			int index = 0;

			// Pass 1: place data bits; parity slots stay 0 for now
			for (int pos2 = 0; pos2 < codeword.length; pos2++) {
				if ((pos2 & (pos2 + 1)) != 0) {
					codeword[pos2] = workingArray[index];
					index++;
				}
			}

			// Pass 2: parity bit at 1-based position 2^p covers
			// every position whose bit p is set
			for (int p = 0; p < pbn; p++) {
				int parityPos = (1 << p) - 1;
				int paritySum = 0;
				for (int pos2 = 0; pos2 < codeword.length; pos2++) {
					if (pos2 != parityPos && (((pos2 + 1) >> p) & 1) == 1) {
						paritySum ^= codeword[pos2];
					}
				}
				codeword[parityPos] = paritySum;
			}
		}
		// 7, 15, or 31 bits entered
		// Return as typed
		else if (
				workingArray.length == 7 ||
				workingArray.length == 15 ||
				workingArray.length == 31
				) {
			codeword = new int[ln];
			for (int i = 0; i < workingArray.length; i++) {
				codeword[i] = workingArray[i];
			}
		}
		else {
			throw new Exception("Invalid number of bits!");
		}
		// Final value for object
		this.outputVals = codeword;
	}

	public int[] getCodeword() {
		return this.outputVals;
	}

	public void setValInCodeword(int index, int newVal) {
		this.outputVals[index] = newVal;
	}

}
