//**REFERENCE: http://www.java2s.com/Code/Java/Network-Protocol/ASimpleWebServer.htm

import java.net.*;
import java.io.*;

public class HTTPHeaders{
  public static void main(String args[]) {
		HTTPHeaders http = new HTTPHeaders();
		http.start();
	}

  public final static int port = 8080; //port for http (**for transferring documents. Forms the foundation of the Web.)

  protected void start(){
    ServerSocket s;

    System.out.println("Webserver starting up on port " + port);
    try{
		  s = new ServerSocket(port); // create the main server socket
      System.out.println("The server has started!");
	  }catch(Exception e){
		  System.out.println("Error: " + e);
		  return;
    }

    System.out.println("Waiting for connection...");

    int ctr = 0;
    String filename = "";
    for(;;){
      try{
        Socket remote = s.accept(); //socket is connected
        System.out.println("Connection, sending data...");
        BufferedReader in = new BufferedReader(new InputStreamReader(remote.getInputStream()));
        PrintWriter out = new PrintWriter(remote.getOutputStream());
        PrintWriter out2;

        String[] data = new String[100];
        String str = "-";
        int i = 0;
        while (!str.equals("")){
          str = in.readLine();
          data[i] = str+" ";
          i++;
        }
        String httpbody = "<html>\n<head>\n\t<title> PROJECT 2 </title>\n</head>\n<body>\n\t<h3>HTTP HEADERS</h3> \n\t<table border=\"1\">\n\t\t<tr> \n\t\t\t<th>Key</th> \n\t\t\t<th>Value</th> \n\t\t</tr>";

        for(int j=0;j<i-1;j++){
          String[] tokens = data[j].split(" ");
          String content = "";

          httpbody += "\n\t\t<tr>\n\t\t\t<td>" + tokens[0] + "</td>";

          for(int k=1;k<tokens.length;k++){
            content += tokens[k] + " ";
          }

          httpbody += "\t\t\t<td>" + content + "</td>\n\t\t</tr>\n";

          if(j==0 && ctr == 0) filename = tokens[1].substring(1, tokens[1].length());
        }


        httpbody += "\n\t</table>\n</body>\n";

        File file = new File(filename);

        if(file.exists()){ //check if the requested file exists
          out.println("HTTP/1.0 200 OK");
	        out.println("Content-Type: text/html");
	        out.println("Server: Bot");
	        out.println("");

          BufferedReader br = new BufferedReader(new FileReader(file));

          String lines;

          if(filename.contains(".css")){
            httpbody += "<style>";
          }else if(filename.contains(".js")){
            httpbody += "<script>";
          }

          while((lines = br.readLine()) != null){
				  	httpbody += lines;
				  }

          if(filename.contains(".css")){
            httpbody += "</style>";
          }else if(filename.contains(".js")){
            httpbody += "</script>";
          }
          br.close();
          out.println(httpbody);
        }else{
          out2 = new PrintWriter(filename);

          out.println("HTTP/1.0 404 Not Found");
          out.println("Content-Type: text/html");
        	out.println("Server: Bot");
        	out.println("");
          out.println("<h1>HTTP 404 File Not Found</h1>"+httpbody);

          out2.flush();
	      }
        out.println("</html>");
        out.flush();
        remote.close();
        ctr++;
      }catch(Exception e) {
        System.out.println("Error: " + e);
      }
    }
  }
}
