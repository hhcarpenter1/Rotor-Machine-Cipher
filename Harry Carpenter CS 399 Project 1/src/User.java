import java.util.Arrays;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import javax.swing.JFileChooser;

/**
 * The class in charge of inputing and user interaction
 * @author Harry
 *
 */
public class User {
	private static final String plainTextAlphabet = "abcdefghijklmnopqrstuvwxyz"; // plain text alphabet
	private static final String rotor1Alphabet = "ekmflgdqvzntowyhxuspaibrcj";
	private static final String rotor2Alphabet = "ajdksiruxblhwtmcqgznpyfvoe";
	private static final char turnoverNotchRotor1 = 'r';
	private String path;
	private static Scanner scan; 
	/**
	 * From http://www.homeandlearn.co.uk/java/read_a_textfile_in_java.html
	 * @param file_path
	 */
	public User(String file_path)
	{
		path = file_path;
	}

	public static void run() throws IOException {
		scan = new Scanner(System.in);
		System.out.println("Select 1 to encrypt. Select 2 to decrypt.");
		int input = scan.nextInt();
		if (input == 1)
		{
			encipher();
		}
		else if (input == 2)
		{
			decipher();
		}

		else {
			System.err.println("Thou shalt not put an invalid number!");
			System.exit(1);
		}

	}

	/**
	 * A class for enciphering messages
	 * @throws IOException
	 */
	public static void encipher() throws IOException
	{
		Boolean Decryption = false;
		String message = enterMessage(Decryption);
		System.out.println(message);
		System.out.println("----------End of Message----------");
		String permutation = User.enterPermutation();
		int[] orientations = User.enterOrientations();
		int orientation1 = orientations[0];
		int orientation2 = orientations[1];
		String encipheredMessage = Machine.runEncryption(message,
				permutation, plainTextAlphabet, rotor1Alphabet, rotor2Alphabet,
				orientation1, orientation2, turnoverNotchRotor1);
		System.out.println("----------Your Enciphered Message----------");
		System.out.println(encipheredMessage);
		System.out.println("----------End of Enciphered Mesage----------");
		System.out.println("Please type in a path to save your enciphered message (For example: C:/Users/Harry/Documents/myFile.txt):");
		Scanner scan = new Scanner(System.in);
		String path = scan.nextLine();
		User.writing(encipheredMessage, path, permutation, orientation1, orientation2);	
		scan.close();
	}


	/**
	 * A class for deciphering messages 
	 * @throws IOException
	 */
	public static void decipher() throws IOException
	{
		Boolean decryption = true;
		String message = enterMessage(decryption);
		System.out.println(message);
		System.out.println("----------End of Message----------");
		String permutation = User.enterPermutation();
		int[] orientations = User.enterOrientations();
		int orientation1 = orientations[0];
		int orientation2 = orientations[1];
		System.out.println("-----------Deciphered Message-----------");
		String decipheredMessage = Machine.runDecryption(message, permutation, plainTextAlphabet, rotor1Alphabet, rotor2Alphabet,
				orientation1, orientation2, turnoverNotchRotor1);
		System.out.println(decipheredMessage);
		System.out.println("-------------End of Deciphered Mesage-----------");

	}





	/**
	 * From http://www.homeandlearn.co.uk/java/read_a_textfile_in_java.html
	 * @return file
	 * @throws IOException
	 */
	public String [] OpenFile() throws IOException {
		FileReader fr = new FileReader(path);
		BufferedReader textReader = new BufferedReader(fr);
		int count = 0;
		String temp = "";
		String [] myMessage = new String[1];
		while ((temp = textReader.readLine()) != null) {
			temp += "\n";
			if (count == myMessage.length)
			{
				myMessage = Arrays.copyOf(myMessage, myMessage.length + 1);
			}
			myMessage[count] = temp;
			temp = "";
			count++;
		}
		textReader.close();
		return myMessage;
	}


	/**
	 * From http://www.homeandlearn.co.uk/java/read_a_textfile_in_java.html
	 * @return numberOfLines in file
	 * @throws IOException
	 */
	int readLines() throws IOException {
		FileReader file_to_read = new FileReader(path);
		BufferedReader bf = new BufferedReader(file_to_read);
		String aLine;
		int numberOfLines = 0;
		while ((aLine = bf.readLine()) != null) {
			numberOfLines++;
		}
		bf.close();
		return numberOfLines;

	}

	/**
	 * Method from:
	 * http://www.java2s.com/Code/Java/Swing-JFC/SelectadirectorywithaJFileChooser.htm
	 * @return selects the file in a String
	 */
	public static String selectFile() {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setDialogTitle("FileChooser");
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);

		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
			System.out.println("getSelectedFile() : " + chooser.getSelectedFile());
			return chooser.getSelectedFile().toString();
		} else {
			System.out.println("No Selection ");
			System.exit(1);
			return "No Selection";
		}

	}

	/**
	 * From http://www.homeandlearn.co.uk/java/read_a_textfile_in_java.html
	 * @param file_name
	 * @return
	 * @throws IOException
	 */
	public static String [] fileData(String file_name, boolean isDeciphering) throws IOException {
		String file_name2 = "";
		for (int i = 0; i < file_name.length(); i++)
		{
			if (file_name.charAt(i) == '\"')
			{
				char newChar = '/';
				file_name2 += newChar;	
			}
			else{
				file_name2 += file_name.charAt(i);
			}

		}

		try {
			User file = new User(file_name2);
			String [] aryLines = file.OpenFile();

			if (isDeciphering == true)
			{
				String [] message = new String [aryLines.length-1];
				for (int i = 0; i < aryLines.length-1; i++)
				{
					message[i] = aryLines[i];
				}
				return message;
			}
			else {
				String [] message = new String [aryLines.length];
				for (int i = 0; i < aryLines.length; i++)
				{
					message[i] = aryLines[i];
				}
				return message;
			}



		}
		catch (IOException e)
		{
			return null;
		}

	}


	/**
	 * 
	 * @return The user enters the message for encryption or decryption
	 * @throws IOException
	 */
	public static String enterMessage(boolean Decryption) throws IOException {


		System.out.println("Please enter the file name of the message you want to encrypt or decrypt. Please use the file chooser.");
		String path = selectFile();
		System.out.println("-------------------- YOUR MESSAGE --------------------");
		String [] message = fileData(path, Decryption);
		String message1 = ""; 
		for (int i = 0; i < message.length; i++)
		{
			message1 += message[i];
		}
		return message1;
	}
	/**
	 * 
	 * @return The user enters the permutation key
	 */
	public static String enterPermutation() {
		scan = new Scanner(System.in);
		System.out.println(
				"Please enter a 26 latin alphabet permutation. There must not be two similar letters. Your input:");
		String pKey = scan.nextLine();
		verifyPermutation(pKey);
		return pKey;
	}

	/**
	 * 
	 * @return The user enters the orientations of the system
	 */
	public static int[] enterOrientations() {
		scan = new Scanner(System.in);
		System.out.println(
				"Please enter an orientation for Rotor 1. The orientation must be between 0 and 25. You input:");
		int oR1 = scan.nextInt();
		System.out.println(
				"Please enter an orientation for Rotor 2. The orientation must be between 0 and 25. You input:");
		int oR2 = scan.nextInt();
		verifyOrientation(oR1, oR2);
		int[] orientations = { oR1, oR2 };
		return orientations;
	}

	/**
	 * A method that verifies the orientation input for correctness 
	 * @param inputRotor1
	 * @param inputRotor2
	 */
	public static void verifyOrientation(int inputRotor1, int inputRotor2) {
		if ((inputRotor1 < 0 || inputRotor1 > 25) && (inputRotor2 < 0 || inputRotor2 > 25)) {
			System.err.println("Thou shalt not enter an orientation input that is less than 0 or greater than 25!");
			System.exit(1);
		}
	}

	/**
	 * A method that verifies the permutation input for correctness
	 * 1) The alphabet must be exactly 26 letters long
	 * 2) The alphabet must user letters from the 26-letter English alphabet
	 * 3) The alphabet cannot use the same characters twice
	 * @param permutation
	 */
	public static void verifyPermutation(String permutation) {
		String p = permutation.toLowerCase();
		boolean correctChars = true;
		if (permutation.length() == 26) {
			int aCount = 0;
			int bCount = 0;
			int cCount = 0;
			int dCount = 0;
			int eCount = 0;
			int fCount = 0;
			int gCount = 0;
			int hCount = 0;
			int iCount = 0;
			int jCount = 0;
			int kCount = 0;
			int lCount = 0;
			int mCount = 0;
			int nCount = 0;
			int oCount = 0;
			int pCount = 0;
			int qCount = 0;
			int rCount = 0;
			int sCount = 0;
			int tCount = 0;
			int uCount = 0;
			int vCount = 0;
			int wCount = 0;
			int xCount = 0;
			int yCount = 0;
			int zCount = 0;

			for (int i = 0; i < p.length(); i++) {
				if (!(Machine.isAlphabetic(p.charAt(i)))) {
					correctChars = false;
				}
				if (p.charAt(i) == 'a') {
					aCount++;
				}
				if (p.charAt(i) == 'b') {
					bCount++;
				}

				if (p.charAt(i) == 'c') {
					cCount++;
				}
				if (p.charAt(i) == 'd') {
					dCount++;
				}

				if (p.charAt(i) == 'e') {
					eCount++;
				}
				if (p.charAt(i) == 'f') {
					fCount++;
				}

				if (p.charAt(i) == 'g') {
					gCount++;
				}
				if (p.charAt(i) == 'h') {
					hCount++;
				}

				if (p.charAt(i) == 'i') {
					iCount++;
				}
				if (p.charAt(i) == 'j') {
					jCount++;
				}

				if (p.charAt(i) == 'k') {
					kCount++;
				}
				if (p.charAt(i) == 'l') {
					lCount++;
				}

				if (p.charAt(i) == 'm') {
					mCount++;
				}
				if (p.charAt(i) == 'o') {
					oCount++;
				}

				if (p.charAt(i) == 'p') {
					pCount++;
				}
				if (p.charAt(i) == 'q') {
					qCount++;
				}

				if (p.charAt(i) == 'r') {
					rCount++;
				}
				if (p.charAt(i) == 's') {
					sCount++;
				}

				if (p.charAt(i) == 't') {
					tCount++;
				}
				if (p.charAt(i) == 'u') {
					uCount++;
				}

				if (p.charAt(i) == 'v') {
					vCount++;
				}
				if (p.charAt(i) == 'w') {
					wCount++;
				}

				if (p.charAt(i) == 'x') {
					xCount++;
				}
				if (p.charAt(i) == 'y') {
					yCount++;
				}

				if (p.charAt(i) == 'z') {
					zCount++;
				}

			}

			if (aCount > 1 || bCount > 1 || cCount > 1 || dCount > 1 || eCount > 1 || fCount > 1 || gCount > 1
					|| hCount > 1 || iCount > 1 || jCount > 1 || kCount > 1 || lCount > 1 || mCount > 1 || nCount > 1
					|| oCount > 1 || pCount > 1 || qCount > 1 || rCount > 1 || sCount > 1 || tCount > 1 || uCount > 1
					|| vCount > 1 || wCount > 1 || xCount > 1 || yCount > 1 || zCount > 1) {
				System.err.println("Thou shalt not input two or more similar letters into the permutation!");
				System.exit(1);
			}

			if (correctChars == false) {
				System.err.println("Thou shalt not include characters that are not in the latin alphabet!");
				System.exit(1);
			}

		} else {
			System.err.println("Thou shalt not input an alphabet that is greater or less than 26 letters long!");
			System.exit(1);

		}

	}

	/**
	 * From: http://stackoverflow.com/questions/2885173/how-do-i-create-a-file-and-write-to-a-file-in-java
	 * @param message
	 * @param path
	 * @param permutation
	 * @param orientation1
	 * @param orientation2
	 */
	public static void writing(String message, String path, String permutation, int orientation1, int orientation2) {
		try {
			//Whatever the file path is.
			File statText = new File(path);
			FileOutputStream is = new FileOutputStream(statText);
			OutputStreamWriter osw = new OutputStreamWriter(is);    
			Writer w = new BufferedWriter(osw);
			w.write(message);
			w.write("\n");
			w.write("Permutation: " + permutation + " ");
			w.write("Orientation 1: " + orientation1 + " ");
			w.write("Orientation 2: " + orientation2 + " ");
			System.out.println("Your message has been successfully saved! Keep it in a safe place!");
			System.out.println("The key to your encrypted message is at the bottom of the encrypted text file.");
			w.close();
		} catch (IOException e) {
			System.err.println("Problem writing to the file.");
		}
	}



}