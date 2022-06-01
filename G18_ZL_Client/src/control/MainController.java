package control;

import java.net.InetAddress;

import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import boundary.ClientView;
 

public class MainController {

	// main
	public static void main(String[] args) {
		myClient =new ClientController(getIpAddress(), ClientController.DEFAULT_PORT);
		ClientView.launchApplication(args);
	}

	// Class Variables
	
	/**
	 * instance of the client
	 */
	private static ClientController myClient;

	/**
	 * default ip address to be added to the connect page's textfield
	 */
	private static String ipAddress;

	//Getters
	/**
	 * 
	 * @return instance of my client
	 */
	public static ClientController getMyClient() {
		return myClient;
	}
	/**
	 * pulls the ip of its own pc to be used as the server's ip to connect to
	 * @return <String> ip address of the this pc
	 */
	public static String getIpAddress() {
		try {
			ipAddress = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			ipAddress = "10.10.8.135";
			e.printStackTrace();
		}
		return ipAddress;
	}
	
	// Helpful Generic Methods
	
	public static String currentTime() {
		//current time is formatted into an appropriate datetime for mysql
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  //formats date and time to be suitable for sql
		Date date = new Date(System.currentTimeMillis());
		return formatter.format(date);
	}
	
	public static long timeDiffSecond(String start_date, String end_date,String ReturnType) {
		// formats date and time  -> "yyyy-MM-dd HH:mm:ss"
		SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
     System.out.println(start_date);
     System.out.println(end_date);
			  
            // parse method is used to parse
            // the text from a string to
            // produce the date
            Date d1 = null ,d2 = null;
			try {
				 d1 = sdf.parse(start_date);
				 d2 = sdf.parse(end_date);
			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
  
             long diff = d1.getTime()-d2.getTime();//as given
             
           if(ReturnType.equals("hour"))
        	   return TimeUnit.MILLISECONDS.toHours(diff) ;
           if(ReturnType.equals("second"))
        	   return TimeUnit.MILLISECONDS.toSeconds(diff);
           if(ReturnType.equals("min"))
        	   return TimeUnit.MILLISECONDS.toSeconds(diff);
		
		return TimeUnit.MILLISECONDS.toSeconds(diff);
	}
	
	
	
}
