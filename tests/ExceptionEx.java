package il.co.ilrd.tests;
import java.io.FileReader;
import java.io.IOException;

public class ExceptionEx{
	/*public static void openFile(){
		try {
			// constructor may throw FileNotFoundException
			FileReader reader = new FileReader("someFile");
			int i=0;
			while(i != -1){
				//reader.read() may throw IOException
				i = reader.read();
				System.out.println((char) i );
			}
			reader.close();
			System.out.println("--- File End ---");
		} catch (FileNotFoundException e) {
			//do something clever with the exception
			e.printStackTrace();
		} catch (IOException e) {
			//do something clever with the exception
		}
	}*/

	//Better example: try-catch-finally
	
    public static void openFile(){
        FileReader reader = null;
        try {
            reader = new FileReader("someFile");
            int i=0;
            while(i != -1){
                i = reader.read();
                System.out.println((char) i );
            }
        } catch (IOException e) {
            //do something clever with the exception
        } finally {
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    //do something clever with the exception
                }
            }
            System.out.println("--- File End ---");
        }
    }
	
	public static int divide(int numberToDivide, int numberToDivideBy) throws BadNumberException{
		if(numberToDivideBy == 0){
			throw new BadNumberException();
		}
		return (numberToDivide / numberToDivideBy);
	}

	public static void callDivide() throws BadNumberException{
		int result = divide(2,1);
		System.out.println(result);
		/* try {
	        int result = divide(2,1);
	        System.out.println(result);
	    } catch (BadNumberException e) {
	        //do something clever with the exception
	        System.out.println(e.getMessage());
	    }
	    System.out.println("Division attempt done");
		 */
	}

	public static void main(String[] args) throws BadNumberException{
		ExceptionEx.callDivide();
		ExceptionEx.openFile();
	}
}
