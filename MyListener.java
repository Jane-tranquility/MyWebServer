import java.io.*;                 //import everything in package java.io
import java.net.*;                //import everything in package java.net


public class MyListener {

	public static void main(String[] args) throws IOException {
		
		int q_len=6;       //the number of queues can get to wait in operating system
		int port=2540;     //the port number is 2540
		Socket sock;
		ServerSocket servsock=new ServerSocket(port, q_len);   
		System.out.println("Jing Li's server starts to listen at port 2540.\n");
		while (true){
			sock=servsock.accept();                    
			new Worker(sock).start();                   
		}

	}

}


class Worker extends Thread{   
	Socket sock;               
	Worker (Socket s){sock=s;} 
	
  
	public void run(){
		PrintStream out=null;        
		BufferedReader in=null;       
		try{
			in=new BufferedReader(new InputStreamReader(sock.getInputStream()));  
			out=new PrintStream(sock.getOutputStream());  
			String name;
			name=in.readLine();                         
			while(name!=null&&!name.isEmpty()){
				try{
					System.out.println(name); 
					name=in.readLine();                          
				//printRemoteAddress(name, out);             
				}catch(IOException x){
					System.out.println("Server read error");     
					x.printStackTrace();
				}
			}
			out.println("Successfully got your request!");
			sock.close();         
		}catch(IOException ioe){
			System.out.println(ioe);              
		}
	}
    
    /*
     The method printRemoteAddress is used to format the information you want to print out:
     - a line to see what the name is
     - the hose name of the name
     - the IP address of the name
     If fialed to do so, also print out an error message
	*/
	/*static void printRemoteAddress(String name, PrintStream out){
		try{
			out.println("Looking up "+name+"...");                     //print out the name wanted to be printed to the client
			InetAddress machine=InetAddress.getByName(name);           //get the Internet Protocol(IP) address by the name
			out.println("Host name: "+machine.getHostName());          //print out the Host name according to the IP address to the client
			out.println("Host IP: "+toText (machine.getAddress()));    //print out the IP address using toText function defined below, which is portable for 128 bit format to the client
		}catch(UnknownHostException ex){
			out.println("Failed in attempt to look up"+name);          //If we failed to find the IP address for the name variable, then print out the error message
		}
	}*/
	
    /*
     A method toText is used to make the IP address portable for 128 bit format
    */
	/*static String toText(byte ip[]){
		StringBuffer result=new StringBuffer();
		for (int i=0; i<ip.length;i++){
			if (i>0){
				result.append(".");
			}
			result.append(0xff&ip[i]);	
		}
		return result.toString();
	}*/
	
}