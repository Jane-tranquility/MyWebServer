import java.io.*;                 //import everything in package java.io
import java.net.*;                //import everything in package java.net
import java.util.*;

public class MyWebServer {

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
			String[] nameList;
			String fileName;
			String serverName;
			name=in.readLine();   
			String fileType;                      
			if (name!=null&&!name.isEmpty()&&name.startsWith("GET")){
				nameList=name.split(" ");
				System.out.println(nameList[1]);
				fileName=nameList[1].substring(1);
					
				if (fileName.endsWith(".html")){
					fileType="text/html";
				}else{
					fileType="text/plain";
				}
				name=in.readLine(); 
				if (name!=null&&!name.isEmpty()&&name.startsWith("Host:")){
					nameList=name.split(" ");
					serverName=nameList[1].split(":")[0];
					//System.out.println(serverName);
					try{
						File f=new File(fileName);
						InputStream file=new FileInputStream(f);
						out.print("HTTP/1.1 200 OK\r\n" +"Content-Type: " +fileType + "\r\n"+"Content-Length: " +f.length()+"\r\n"+"Date: " + new Date() + "\r\n\r\n" );
						
						try {
            				byte[] buffer = new byte[1000];
            				while (file.available()>0) 
               					 out.write(buffer, 0, file.read(buffer));
        				} catch (IOException e) { System.out.println(e); }
					}catch(FileNotFoundException e){
						out.println("File not found error!");
					}
				}else{
					out.println("Bad request, the browser sent a request this web server doesn't understand!");
				}
				
			}else{
				out.println("Bad request, the browser sent a request this web server doesn't understand!");
			}
			
			sock.close();         
		}catch(IOException ioe){
			System.out.println(ioe);              
		}
	}
}
