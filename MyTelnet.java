

import java.io.*;             
import java.net.*;             


public class MyTelnet {
	public static void main(String[] args){
		String serverName;                         
		if (args.length<1) {
			serverName="localhost";                
		}else {serverName=args[0];}
		
		System.out.println("Jing Li's MyTelnet, 1.8.\n");                
		System.out.println("Using server: "+serverName+", port 80");
		BufferedReader in=new BufferedReader(new InputStreamReader(System.in)); 
		
		try{
			String name;
			do { 
				Socket sock;
				BufferedReader fromServer;
				PrintStream toServer;
				String textFromServer; 
				int count=0;
				
				sock=new Socket(serverName, 80);        
				name=in.readLine(); 
				fromServer=new BufferedReader(new InputStreamReader(sock.getInputStream()));   //define the input stream from the server socket
				toServer=new PrintStream(sock.getOutputStream());  
				while(name!=null&&!name.isEmpty()&&count<3){ 
					if(name.isEmpty()){
						count+=1;
					}else{
						toServer.println(name);      //if the content is not empty, print out the message on the console
                		name=in.readLine();
                	}
		    	} 
		    	toServer.flush();
		    	textFromServer=fromServer.readLine(); 
		    	while(textFromServer!=null){                       
               		System.out.println(textFromServer);      //if the content is not empty, print out the message on the console
                	textFromServer=fromServer.readLine(); 
		    	}
		    	sock.close();
				//sendInfo(name, serverName);   
				
			}while(name.indexOf("quit")<0);
			System.out.println("Cancelled by user request."); 
			  
		}catch(IOException e){
			e.printStackTrace();                           
		}
		
	}
	
  /*
	static void sendInfo(String name, String serverName){   //the method having two arguments: name and serverName
		Socket sock;
		BufferedReader fromServer;
		PrintStream toServer;
		String textFromServer;
		
		try{
			sock=new Socket(serverName, 80);                  //create a new sock which is the server socket
			fromServer=new BufferedReader(new InputStreamReader(sock.getInputStream()));   //define the input stream from the server socket
			toServer=new PrintStream(sock.getOutputStream());   //define an outstream to the server socket
			toServer.println(name);         //sent the name as an output to the server socket
			toServer.flush();               //flush the stream
			textFromServer=fromServer.readLine(); 
		    while(textFromServer!=null&&!textFromServer.isEmpty()){                       
                System.out.println(textFromServer);      //if the content is not empty, print out the message on the console
                textFromServer=fromServer.readLine(); 
		    }
		    sock.close();                                  
		}catch(IOException e){
			System.out.println("Socket Error!");            //IOException, prints an error message
			e.printStackTrace();
		}
	}*/
    
    
    /*
     A method toText is used to make the IP address portable for 128 bit format
     */
    /*static String toText(byte ip[]){
        StringBuffer result=new StringBuffer();
        for (int i=0; i<ip.length; i++){
            if (i>0){
                result.append(".");
            }
            result.append(0xff&ip[i]);
        }
        return result.toString();
    }*/
}