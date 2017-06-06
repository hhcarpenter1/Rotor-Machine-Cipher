/**
 * A class that is in charge of the functionality of the machine
 * @author Harry
 *
 */

public class Machine {


	/**
	 * 
	 * @param alphabet
	 * @return inverse
	 */
	public static String getInverseAlphabet(String alphabet)
	{
		String inverse = "";
		for (int i = alphabet.length(); i > 0; i--)
		{
			inverse += alphabet;
		}
		return inverse;
	}

	/**
	 * A method that returns a message encrypted by the cipher rotor machine
	 * Goes through the equation: c = P(Inverse)F(Inverse)S(Inverse)RSFP
	 * @param message
	 * @param permutationAlphabet
	 * @param plainTextAlphabet
	 * @param rotor1Alphabet
	 * @param rotor2Alphabet
	 * @param oRotor1
	 * @param oRotor2
	 * @param turnoverNotch
	 * @return pInverseEncipherment
	 */
	public static String runEncryption(String message, String permutationAlphabet, String plainTextAlphabet, String rotor1Alphabet, String rotor2Alphabet, int oRotor1, int oRotor2, char turnoverNotch)
	{
		String pString = Machine.encipherP(message.toLowerCase(), permutationAlphabet, plainTextAlphabet); //P
		String firstRotorEncipherment = Machine.firstRotorEncryption(pString, oRotor1, permutationAlphabet, rotor1Alphabet); //F
		String secondRotorEncipherment = Machine.secondRotorEncryption(firstRotorEncipherment, oRotor1, oRotor2, rotor1Alphabet, rotor2Alphabet, turnoverNotch); //S
		String reflector = Machine.reflector(secondRotorEncipherment); //R
		String inverseSecondRotorAlphabet = getInverseAlphabet(rotor2Alphabet);
		String inverseFirstRotorAlphabet = getInverseAlphabet(rotor1Alphabet);
		String inversePermutationAlphabet = getInverseAlphabet(permutationAlphabet);
		String inverseSecondRotorEncipherment = Machine.secondRotorEncryption(reflector, oRotor1, oRotor2, plainTextAlphabet, inverseSecondRotorAlphabet, turnoverNotch); //S inverse
		String inverseFirstRotorEncipherment = Machine.firstRotorEncryption(inverseSecondRotorEncipherment, oRotor1, inverseSecondRotorAlphabet, inverseFirstRotorAlphabet); //F inverse
		String pInverseEncipherment = Machine.encipherP(inverseFirstRotorEncipherment, inversePermutationAlphabet, inverseFirstRotorAlphabet);


		return pInverseEncipherment;
	}

	/**
	 * A method that returns the decryption of a message
	 * Goes through the equation: d= P(Inverse)F(Inverse)S(Inverse)RSFP
	 * @param message
	 * @param permutationAlphabet
	 * @param plainTextAlphabet
	 * @param rotor1Alphabet
	 * @param rotor2Alphabet
	 * @param oRotor1
	 * @param oRotor2
	 * @param turnoverNotch
	 * @return orginial plainText
	 */
	public static String runDecryption(String message, String permutationAlphabet, String plainTextAlphabet, String rotor1Alphabet, String rotor2Alphabet, int oRotor1, int oRotor2, char turnoverNotch)
	{
		String inversePermutationAlphabet = getInverseAlphabet(permutationAlphabet);
		String inverseFirstRotorAlphabet = getInverseAlphabet(rotor1Alphabet);
		String inverseSecondRotorAlphabet = getInverseAlphabet(rotor2Alphabet);
		String pInverseEncipherment = Machine.decipherP(message, inverseFirstRotorAlphabet, inversePermutationAlphabet); //P inverse		
		String inverseFirstRotorDecipherment = Machine.firstRotorDecryption(pInverseEncipherment, oRotor1, inverseSecondRotorAlphabet, inverseFirstRotorAlphabet); //F inverse
		String inverseSecondRotorDecipherment = Machine.secondRotorDecryption(inverseFirstRotorDecipherment, oRotor1, oRotor2, plainTextAlphabet, inverseSecondRotorAlphabet, turnoverNotch); //S inverse
		String reflector = Machine.reflector(inverseSecondRotorDecipherment); //R
		String secondRotorDecipherment = Machine.secondRotorDecryption(reflector, oRotor1, oRotor2, rotor1Alphabet, rotor2Alphabet, turnoverNotch); //S
		String firstRotorDecipherment = Machine.firstRotorDecryption(secondRotorDecipherment, oRotor1, permutationAlphabet, rotor1Alphabet); //F
		String pStringD = Machine.decipherP(firstRotorDecipherment, plainTextAlphabet, permutationAlphabet); //P
		return pStringD;
	}

	/**
	 * A method to encipher P and inverse P
	 * @param message
	 * @param cipherText
	 * @param normalAlphabet
	 * @return encryptedMessage
	 */
	public static String encipherP(String message, String cipherText, String normalAlphabet) //enciphers the message into the P	
	{
		String encryptedMessage = "";

		for (int i = 0; i < message.length(); i++)
		{
			encryptedMessage += encryptChars(message.charAt(i), cipherText, normalAlphabet);
		}
		return encryptedMessage;
	}

	/**
	 * A method to decipher P and inverse P
	 * @param message
	 * @param normalAlphabet
	 * @param cipherText
	 * @return encryptedMessage
	 */
	public static String decipherP(String message, String normalAlphabet, String cipherText)
	{
		String encryptedMessage = "";

		for (int i = 0; i < message.length(); i++)
		{
			encryptedMessage += encryptChars(message.charAt(i), normalAlphabet, cipherText);
		}
		return encryptedMessage;	
	}

	/**
	 * A method to shift the rotor
	 * @param alphabet
	 * @return alphabet of new shift
	 */
	public static String shiftRotor(String alphabet)
	{
		String newShift = "";
		for (int i = 0; i < alphabet.length()-1; i++)
		{


			newShift += alphabet.charAt(i+1);
		}
		newShift += alphabet.charAt(0);
		return newShift;
	}

	/**
	 * 
	 * A method that tells the machine to keep shifting until it has reached the orientation of the rotor
	 * @param orientation
	 * @param rotor1Alphabet
	 * @return orientation Alphabet
	 */
	public static String reachOrientation(int orientation, String rotor1Alphabet) {
		String newString = rotor1Alphabet;
		String tempString = "";
		for (int j = 0; j < orientation; j++)
		{
			tempString = shiftRotor(newString);
			newString = tempString;
		}
		return newString;
	}

	/**
	 * Decryption works similar to encryption except that the permutationAlphabet and rotor1Alphabet are in swapped positions
	 * @param message
	 * @param orientation
	 * @param permutationAlphabet
	 * @param rotor1Alphabet
	 * @return decryptedMessage of first rotor
	 */
	public static String firstRotorDecryption(String message, int orientation, String permutationAlphabet, String rotor1Alphabet)	
	{


		String rotor1NewCipherAlphabet = reachOrientation(orientation, rotor1Alphabet);
		String tempString1 = "";
		String encryptedMessage = "";
		char encryptedCharRotor1 = ' ';
		for (int i = 0; i < message.length()-1; i++) //Every time a character is deciphered, the rotor will shift
		{
			if (isAlphabetic(message.charAt(i)))
			{
				encryptedCharRotor1 = encryptChars(message.charAt(i), permutationAlphabet, rotor1NewCipherAlphabet); //The char encrypted for rotor 1
				encryptedMessage += encryptedCharRotor1;	
				if (isAlphabetic(message.charAt(i+1))) //We need to make sure the rotor doesn't shift if the character is non-alphabetic!
				{
					tempString1 = shiftRotor(rotor1NewCipherAlphabet); //Shift the rotor for every alphabetic character in the alphabet
					rotor1NewCipherAlphabet = tempString1; //The cipher alphabet for rotor 1
				}
				if (i == message.length()-2) //We have this if statement so we make sure the shifting is performed correctly
				{
					encryptedCharRotor1 = encryptChars(message.charAt(message.length()-1), permutationAlphabet, rotor1NewCipherAlphabet); //The char encrypted for rotor 1
					encryptedMessage += encryptedCharRotor1;
				}

			}
			else {
				encryptedMessage += message.charAt(i);
			}

		}
		return encryptedMessage;
	}

	/**
	 * Encryption works similar to decryption except that the permutationAlphabet and rotor1Alphabet are in swapped positions
	 * I wanted to understand the code better, so that's why these methods look a bit repetitive.
	 * @param message
	 * @param orientation
	 * @param permutationAlphabet
	 * @param rotor1Alphabet
	 * @return encrypted message from first rotor
	 */

	public static String firstRotorEncryption(String message, int orientation, String permutationAlphabet, String rotor1Alphabet)	
	{
		//The method should reach the turnoverNotch once every 26 shifts

		String rotor1NewCipherAlphabet = reachOrientation(orientation, rotor1Alphabet);
		String tempString1 = "";
		String encryptedMessage = "";
		char encryptedCharRotor1 = ' ';
		for (int i = 0; i < message.length()-1; i++) 
		{
			if (isAlphabetic(message.charAt(i)))
			{
				encryptedCharRotor1 = encryptChars(message.charAt(i), rotor1NewCipherAlphabet, permutationAlphabet); //The char encrypted for rotor 1
				encryptedMessage += encryptedCharRotor1;	
				if (isAlphabetic(message.charAt(i+1))) //We need to make sure the rotor doesn't shift if the character is non-alphabetic!
				{
					tempString1 = shiftRotor(rotor1NewCipherAlphabet); //Shift the rotor for every alphabetic character in the alphabet
					rotor1NewCipherAlphabet = tempString1; //The cipher alphabet for rotor 1
				}
				if (i == message.length()-2) //We have this if statement so we make sure the shifting is performed correctly
				{
					encryptedCharRotor1 = encryptChars(message.charAt(message.length()-1), rotor1NewCipherAlphabet, permutationAlphabet); //The char encrypted for rotor 1
					encryptedMessage += encryptedCharRotor1;
				}

			}
			else {
				encryptedMessage += message.charAt(i);
			}

		}
		return encryptedMessage;
	}	


	/**
	 * Decryption works similar to Encryption except that the rotor1NewCipherAlphabet and rotor2NewCipherAlphabet are in swapped positions
	 * @param message
	 * @param orientationRotor1
	 * @param orientationRotor2
	 * @param rotor1Alphabet
	 * @param rotor2Alphabet
	 * @param turnoverNotch
	 * @return decrypted message from second rotor
	 */
	public static String secondRotorDecryption(String message, int orientationRotor1, int orientationRotor2, String rotor1Alphabet, String rotor2Alphabet, char turnoverNotch)
	{
		String rotor1NewCipherAlphabet = reachOrientation(orientationRotor1, rotor1Alphabet);
		String rotor2NewCipherAlphabet = reachOrientation(orientationRotor2, rotor2Alphabet);	
		String tempString1 = "";
		String encryptedMessage = "";
		String tempString2 = "";
		char encryptedCharRotor2 = ' ';
		int count = 0;
		for (int i = 0; i < message.length()-1; i++) //Every time a character is enciphered, the rotor will shift
		{


			if (isAlphabetic(message.charAt(i)))
			{
				encryptedCharRotor2 = encryptChars(message.charAt(i), rotor2NewCipherAlphabet, rotor1NewCipherAlphabet); //The char encrypted for rotor 1
				encryptedMessage += encryptedCharRotor2;


				if (isAlphabetic(message.charAt(i+1))) //We need to make sure the rotor doesn't shift if the character is non-alphabetic!
				{
					tempString1 = shiftRotor(rotor1NewCipherAlphabet); //Shift the rotor for every alphabetic character in the alphabet
					rotor1NewCipherAlphabet = tempString1; //The cipher alphabet for rotor 1
				}

				if (rotor1NewCipherAlphabet.charAt(count) == turnoverNotch) //If the orientation of Rotor 1 is at the turnoverNotch of R, Rotor 2 shifts
				{
					tempString2 = shiftRotor(rotor2NewCipherAlphabet);
					rotor2NewCipherAlphabet = tempString2;
				}

				if (i == message.length()-2) //We have this if statement so we make sure the shifting is performed correctly
				{
					encryptedCharRotor2 = encryptChars(message.charAt(message.length()-1), rotor2NewCipherAlphabet, rotor1NewCipherAlphabet); //The char encrypted for rotor 1
					encryptedMessage += encryptedCharRotor2;
				}


				count++; //The count is to determine the rotor position for Rotor 1
				if (count == 25) {
					count = 0;
				}

			}
			else {
				encryptedMessage += message.charAt(i);
			}

		}
		return encryptedMessage;

	}



	/**
	 * Encryption works similar to decryption except that the rotor1NewCipherAlphabet and rotor2NewCipherAlphabet are in swapped positions
	 * @param message
	 * @param orientationRotor1
	 * @param orientationRotor2
	 * @param rotor1Alphabet
	 * @param rotor2Alphabet
	 * @param turnoverNotch
	 * @return encryption from second rotor
	 */
	public static String secondRotorEncryption(String message, int orientationRotor1, int orientationRotor2, String rotor1Alphabet, String rotor2Alphabet, char turnoverNotch)
	{
		String rotor1NewCipherAlphabet = reachOrientation(orientationRotor1, rotor1Alphabet);
		String rotor2NewCipherAlphabet = reachOrientation(orientationRotor2, rotor2Alphabet);	
		String tempString1 = "";
		String encryptedMessage = "";
		String tempString2 = "";
		char encryptedCharRotor2 = ' ';
		int count = 0;
		for (int i = 0; i < message.length()-1; i++) //Every time a character is enciphered, the rotor will shift
		{
			if (isAlphabetic(message.charAt(i)))
			{
				encryptedCharRotor2 = encryptChars(message.charAt(i), rotor1NewCipherAlphabet, rotor2NewCipherAlphabet); //The char encrypted for rotor 1
				encryptedMessage += encryptedCharRotor2;


				if (isAlphabetic(message.charAt(i+1))) //We need to make sure the rotor doesn't shift if the character is non-alphabetic!
				{
					tempString1 = shiftRotor(rotor1NewCipherAlphabet); //Shift the rotor for every alphabetic character in the alphabet
					rotor1NewCipherAlphabet = tempString1; //The cipher alphabet for rotor 1
				}

				if (rotor1NewCipherAlphabet.charAt(count) == turnoverNotch)
				{
					tempString2 = shiftRotor(rotor2NewCipherAlphabet);
					rotor2NewCipherAlphabet = tempString2;
				}

				if (i == message.length()-2) //We have this if statement so we make sure the shifting is performed correctly
				{
					encryptedCharRotor2 = encryptChars(message.charAt(message.length()-1), rotor1NewCipherAlphabet, rotor2NewCipherAlphabet); //The char encrypted for rotor 1
					encryptedMessage += encryptedCharRotor2;
				}

				count++; //The count is to determine the rotor position for Rotor 1
				if (count == 25) {
					count = 0;
				}

			}
			else {
				encryptedMessage += message.charAt(i);
			}

		}
		return encryptedMessage;

	}


	/**
	 * A method for the reflector. As shown in the assignment, the mappings are:
	 * (AY) (BR) (CU) (DH) (EQ) (FS) (GL) (IP) (JX) (KN) (MO) (TZ) (VW). In other words, A maps to Y or Y maps to A, and so on... 
	 * I'd imagine that there might be a more concise way to write this method, but I
	 * am not aware of any methods that may simplify this.
	 * 
	 * @param message
	 * @return reflectorMessage
	 */
	public static String reflector(String message)
	{
		String encryptedMessage = "";
		for (int i = 0; i < message.length(); i++)
		{
			if (message.charAt(i) == 'y')
			{
				encryptedMessage += 'a';
			}
			else if (message.charAt(i) == 'a')
			{
				encryptedMessage += 'y';
			}
			else if (message.charAt(i) == 'b')
			{
				encryptedMessage += 'r';
			}
			else if (message.charAt(i) == 'r')
			{
				encryptedMessage += 'b';
			}
			else if (message.charAt(i) == 'c')
			{
				encryptedMessage += 'u';
			}
			else if (message.charAt(i) == 'u')
			{
				encryptedMessage += 'c';
			}
			else if (message.charAt(i) == 'd')
			{
				encryptedMessage += 'h';
			}
			else if (message.charAt(i) == 'h')
			{
				encryptedMessage += 'd';
			}
			else if (message.charAt(i) == 'e')
			{
				encryptedMessage += 'q';
			}
			else if (message.charAt(i) == 'q')
			{
				encryptedMessage += 'e';
			}
			else if (message.charAt(i) == 'f')
			{
				encryptedMessage += 's';
			}
			else if (message.charAt(i) == 's')
			{
				encryptedMessage += 'f';
			}
			else if (message.charAt(i) == 'g')
			{
				encryptedMessage += 'l';
			}
			else if (message.charAt(i) == 'l')
			{
				encryptedMessage += 'g';
			}
			else if (message.charAt(i) == 'i')
			{
				encryptedMessage += 'p';
			}
			else if (message.charAt(i) == 'p')
			{
				encryptedMessage += 'i';
			}
			else if (message.charAt(i) == 'j')
			{
				encryptedMessage += 'x';
			}
			else if (message.charAt(i) == 'x')
			{
				encryptedMessage += 'j';
			}
			else if (message.charAt(i) == 'k')
			{
				encryptedMessage += 'n';
			}
			else if (message.charAt(i) == 'n')
			{
				encryptedMessage += 'k';
			}
			else if (message.charAt(i) == 'm')
			{
				encryptedMessage += 'o';
			}
			else if (message.charAt(i) == 'o')
			{
				encryptedMessage += 'm';
			}
			else if (message.charAt(i) == 't')
			{
				encryptedMessage += 'z';
			}
			else if (message.charAt(i) == 'z')
			{
				encryptedMessage += 't';
			}
			else if (message.charAt(i) == 'v')
			{
				encryptedMessage += 'w';
			}
			else if (message.charAt(i) == 'w')
			{
				encryptedMessage += 'v';
			}
			else
			{
				encryptedMessage += message.charAt(i);
			}
		}
		return encryptedMessage;
	}


	/**
	 * If the char is alphabet, then the first rotor will turn. For spaces and punctuation, there is no encipherment.
	 * 
	 * @param currChar
	 * @return isAlphabetic
	 */
	public static boolean isAlphabetic(char currChar) 
	{
		if (currChar == 'a')
		{
			return true;
		}
		else if (currChar == 'b')
		{
			return true;
		}
		else if (currChar == 'c')
		{
			return true;
		}
		else if (currChar == 'd')
		{
			return true;
		}
		else if (currChar == 'e')
		{
			return true;
		}
		else if (currChar == 'f')
		{
			return true;
		}
		else if (currChar == 'g')
		{
			return true;
		}
		else if (currChar == 'h')
		{
			return true;
		}
		else if (currChar == 'i')
		{
			return true;
		}
		else if (currChar == 'j')
		{
			return true;
		}
		else if (currChar == 'k')
		{
			return true;
		}
		else if (currChar == 'l')
		{
			return true;
		}
		else if (currChar == 'm')
		{
			return true;
		}
		else if (currChar == 'n')
		{
			return true;
		}
		else if (currChar == 'o')
		{
			return true;
		}
		else if (currChar == 'p')
		{
			return true;
		}
		else if (currChar == 'q')
		{
			return true;
		}
		else if (currChar == 'r')
		{
			return true;
		}
		else if (currChar == 's')
		{
			return true;
		}
		else if (currChar == 't')
		{
			return true;
		}
		else if (currChar == 'u')
		{
			return true;
		}
		else if (currChar == 'v')
		{
			return true;
		}
		else if (currChar == 'w')
		{
			return true;
		}
		else if (currChar == 'x')
		{
			return true;
		}
		else if (currChar == 'y')
		{
			return true;
		}
		else if (currChar == 'z')
		{
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * A message to encrypt the current char to the cipher alphabet
	 * @param messageChar
	 * @param cipherAlphabet
	 * @param messageAlphabet
	 * @return encryptedChar
	 */
	public static char encryptChars(char messageChar, String cipherAlphabet, String messageAlphabet)
	{
		if (messageChar == messageAlphabet.charAt(0))
		{
			return cipherAlphabet.charAt(0);
		}
		else if (messageChar == messageAlphabet.charAt(1))
		{
			return cipherAlphabet.charAt(1);
		}
		else if (messageChar == messageAlphabet.charAt(2))
		{
			return cipherAlphabet.charAt(2);
		}
		else if (messageChar == messageAlphabet.charAt(3))
		{
			return cipherAlphabet.charAt(3);
		}
		else if (messageChar == messageAlphabet.charAt(4))
		{
			return cipherAlphabet.charAt(4);
		}
		else if (messageChar == messageAlphabet.charAt(5))
		{
			return cipherAlphabet.charAt(5);
		}
		else if (messageChar == messageAlphabet.charAt(6))
		{
			return cipherAlphabet.charAt(6);
		}
		else if (messageChar == messageAlphabet.charAt(7))
		{
			return cipherAlphabet.charAt(7);
		}
		else if (messageChar == messageAlphabet.charAt(8))
		{
			return cipherAlphabet.charAt(8);
		}
		else if (messageChar == messageAlphabet.charAt(9))
		{
			return cipherAlphabet.charAt(9);
		}
		else if (messageChar == messageAlphabet.charAt(10))
		{
			return cipherAlphabet.charAt(10);
		}
		else if (messageChar == messageAlphabet.charAt(11))
		{
			return cipherAlphabet.charAt(11);
		}
		else if (messageChar == messageAlphabet.charAt(12))
		{
			return cipherAlphabet.charAt(12);
		}
		else if (messageChar == messageAlphabet.charAt(13))
		{
			return cipherAlphabet.charAt(13);
		}
		else if (messageChar == messageAlphabet.charAt(14))
		{
			return cipherAlphabet.charAt(14);
		}
		else if (messageChar == messageAlphabet.charAt(15))
		{
			return cipherAlphabet.charAt(15);
		}
		else if (messageChar == messageAlphabet.charAt(16))
		{
			return cipherAlphabet.charAt(16);
		}
		else if (messageChar == messageAlphabet.charAt(17))
		{
			return cipherAlphabet.charAt(17);
		}
		else if (messageChar == messageAlphabet.charAt(18))
		{
			return cipherAlphabet.charAt(18);
		}
		else if (messageChar == messageAlphabet.charAt(19))
		{
			return cipherAlphabet.charAt(19);
		}
		else if (messageChar == messageAlphabet.charAt(20))
		{
			return cipherAlphabet.charAt(20);
		}
		else if (messageChar == messageAlphabet.charAt(21))
		{
			return cipherAlphabet.charAt(21);
		}
		else if (messageChar == messageAlphabet.charAt(22))
		{
			return cipherAlphabet.charAt(22);
		}
		else if (messageChar == messageAlphabet.charAt(23))
		{
			return cipherAlphabet.charAt(23);
		}
		else if (messageChar == messageAlphabet.charAt(24))
		{
			return cipherAlphabet.charAt(24);
		}
		else if (messageChar == messageAlphabet.charAt(25))
		{
			return cipherAlphabet.charAt(25);
		}
		else
		{
			return messageChar;
		}
	}

}