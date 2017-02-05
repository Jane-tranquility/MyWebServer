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
				//fileName=nameList[1].substring(1);
				//System.out.println(fileName);	
				String addr=nameList[1];
				if (addr.equals("/")){
					fileType="text/html";
					out.print("HTTP/1.1 200 OK\r\n" +"Content-Type: " +fileType +"\r\n"+"Content-Length: " +"2000"+"\r\n"+"Date: " + new Date() + "\r\n\r\n" );
					out.print("<html><head>\r\n</head>\r\n<body>\r\n");
					out.print("<h1>Index of my directory</h1>\r\n");
					//System.out.println("this is a test");
					File dir=new File("./");
					File[] listOfFiles=dir.listFiles();
					for ( int i = 0 ; i < listOfFiles.length ; i ++ ) {
						String linkName;
						
      					if ( listOfFiles[i].isDirectory ( ) ) {
      						linkName=listOfFiles[i].getPath()+"/";
							out.print ( "<a href= "+linkName+">"+listOfFiles[i].getPath().substring(2)+"</a><br>\r\n") ;
      					}else if ( listOfFiles[i].isFile ( ) ){
      						linkName=listOfFiles[i].getPath();
							out.print (  "<a href="+linkName+">"+listOfFiles[i].getPath().substring(2)+"</a><br>\r\n") ;
   						}
					}
				}else if(addr.endsWith("/")){
					addr=addr.substring(1);
					fileType="text/html";
					out.print("HTTP/1.1 200 OK\r\n" +"Content-Type: " +fileType +"\r\n"+"Content-Length: " +"2000"+"\r\n"+"Date: " + new Date() + "\r\n\r\n" );
					out.print("<html><head>\r\n</head>\r\n<body>\r\n");
					File dir=new File(addr);
					int len=addr.length();
					out.print("<h1>Index of my "+addr+" directory</h1>\r\n");
					File[] listOfFiles=dir.listFiles();
					for ( int i = 0 ; i < listOfFiles.length ; i ++ ) {
						String linkName;
						
      					if ( listOfFiles[i].isDirectory ( ) ) {
      						linkName=listOfFiles[i].getPath().substring(len)+"/";
							out.print ( "<a href= "+linkName+">"+listOfFiles[i].getPath().substring(len)+"</a><br>\r\n") ;
      					}else if ( listOfFiles[i].isFile ( ) ){
      						linkName=listOfFiles[i].getPath().substring(len);
							out.print (  "<a href="+linkName+">"+listOfFiles[i].getPath().substring(len)+"</a><br>\r\n") ;
   						}
					}

				}else{
					fileName=addr.substring(1);
					if (fileName.endsWith(".html")){
						fileType="text/html";
					}else{
						fileType="text/plain";
					}
				
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
