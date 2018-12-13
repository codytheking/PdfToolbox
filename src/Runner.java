/************************
 * 						*
 * @author Cody King	*
 *						*
 ************************/

import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Runner 
{
	public static void main(String[] args) 
	{
		Scanner in = new Scanner(System.in);
		System.out.print("Enter the file name: ");
		String fn1 = in.next();
		System.out.print("Enter the file name: ");
		String fn2 = in.next();
		System.out.print("Enter the new merged file name: ");
		String newFn = in.next();
		in.close();
		
		PDFMergerUtility ut = new PDFMergerUtility();
		String home = System.getProperty("user.home");
		try 
		{
			ut.addSource(new File(FilenameUtils.normalize(home + fn1)));
			ut.addSource(new File(FilenameUtils.normalize(home + fn2)));
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		
		ut.setDestinationFileName(home + newFn);
		try 
		{
			ut.mergeDocuments(null);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			return;
		}
		
		System.out.println("Success!\n");
	}
}
