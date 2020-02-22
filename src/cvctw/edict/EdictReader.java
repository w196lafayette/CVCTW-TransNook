/**
 * 
 */
package cvctw.edict;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * @author Fred Minger
 *
 */
public class EdictReader {

	private BufferedReader bR = null;
	private ArrayList<String> contents = new ArrayList<String>();
//	private ArrayList<ArrayList<Integer>> iContents = new ArrayList<ArrayList<Integer>>();
//	private ArrayList<Integer> tContents = new ArrayList<Integer>();

	@SuppressWarnings("unused")
	private EdictReader() {
		// disallow
	}

	public EdictReader(String filePath) throws IOException {
		System.out.println("Opening EDICT file " + filePath);
		BufferedReader bR = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "EUC-JP"));
		String line;
		try {
			line = bR.readLine();
			while (line != null) {
				contents.add(line);
				line = bR.readLine();
			}
		} catch (IOException e) {
			bR.close();
			e.printStackTrace();
			throw (e);
		}
		bR.close();
		// remove first line, which is comments
		contents.remove(0);

//		String newLine = "\n";
//		BufferedReader fI = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "EUC-JP"));
//		Integer iByte = fI.read();
//		while (! iByte.equals(-1)) {
//			tContents.add(iByte);
//			if (iByte.toString().equals(newLine)) {
//				iContents.add(new ArrayList<Integer>(tContents));
//				tContents.clear();
//			}
//		}
//		fI.close();
//		for (ArrayList<Integer> a : iContents) {
//			for (Integer ii : a) {
//				System.out.print(ii + " ");
//			}
//			System.out.println();
//		}
	}

	public String readLine() throws IOException {
		return bR.readLine();
	}

	public ArrayList<String> getContents() {
		return contents;
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		if (args.length < 1) {
			System.out.println("Missing argument: filePath");
			System.exit(-1);
		}
		try {
			EdictReader e = new EdictReader(args[0]);
			ArrayList<String> c = e.getContents();
			for (String l : c) {
				System.out.println(l);
				byte[] jb = l.getBytes("EUC-JP");
				for (int i = 0; /* i < 20 && */ i < jb.length; i++) {
					System.out.print(Byte.toUnsignedInt(jb[i]) + " ");
				}
				System.out.println("\n");
//				char[] cb = l.toCharArray();
//				for (int i = 0; i < 20 && i < cb.length; i++) {
//					System.out.print(Character.getNumericValue(cb[i]) + " ");
//				}
//				System.out.println("\n-------");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println(e);
			System.exit(-1);
		}
	}

}
