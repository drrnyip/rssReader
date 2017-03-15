import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SimpleServlet
 */
@WebServlet("/SimpleServlet")
public class SimpleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    static String [] titleArray = new String[50];
    static String [] descriptionArray = new String[50];
    static String [] linkArray = new String[50];
    static String [] pubDateArray = new String[50];
    static String [] rssArray = new String[9];
    

    static int itemCount = 0;

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SimpleServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		//Start HTML
		out.println("<html>");
		out.println("<head>");
		out.println("</head>");
		
		//Start body
		out.println("<body style='background-color: #ececec; padding: 50px;'>");
		
		//Start center
		out.println("<center>");
		out.println("<br />");
		
		out.println("<img src='/FinalProject/reuters.png' style='width: 100%; height: auto; display: inline-block; margin-right: auto; margin-left: auto; text-align: center;'>");
		out.println("<br />");
//		out.println("<img src='/FinalProject/rssorange.png' style='height: 75px; width: auto; display: inline-block; margin-right: 35px; vertical-align: middle;'></img><h1 style='display: inline-block;'>Search Reuters RSS Feeds</h1>");
		
		
		//Form
		out.println("<form name='searchNews' method='post' action='SimpleServlet'>");
		out.println("<input type='text' placeholder='News keyword' name='keyword' style='font-size: 1.25em; padding: 5px; border-radius: 5px;'/>");
		out.println("<input type='submit' value='Search' name='search' style='font-size: 1.25em; color: white; padding: 5px 15px 5px 15px; background-color: #ff8100;'/>");
		out.println("</form>");
		
		out.println("<br />");
		out.println("<h1 style='text-align: left; margin-left: 15px; font-family: \"Tahoma\"; letter-spacing: 2px;'>Top News</h1>");
		
		populatePage();
		
		//Populate Page
		//Iterate through item list
		for (int i=0; i< itemCount; i++){
			if (titleArray[i] != null){
				out.println("<div style='background-color: #fffefe; padding: 25px;'>");
				out.println("<a href='" + linkArray[i] + "'>");
				out.println("<h3 style='text-align:left;'>" + titleArray[i] + "</h3>");
				out.println("</a>");
				out.println("<p style='text-align:left;'>" + descriptionArray[i] + "</p>");
				out.println("<p style='text-align:left;'>" + pubDateArray[i] + "</p>");
				out.println("</div>");
				out.println("<br />");
			}
			else {
				break;
			}
		}
		
		
		
		//End center
		out.println("</center>");

		//End body
		out.println("</body>");
		
		//End html
		out.println("</html>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	    //Add links to RSS array
	    rssArray[0] = "http://feeds.reuters.com/reuters/healthNews";
	    rssArray[1] = "http://feeds.reuters.com/news/wealth";
	    rssArray[2] = "http://feeds.reuters.com/reuters/technologyNews";
	    rssArray[3] = "http://feeds.reuters.com/Reuters/worldNews";
	    rssArray[4] = "http://feeds.reuters.com/Reuters/PoliticsNews";
	    rssArray[5] = "http://feeds.reuters.com/reuters/scienceNews";
	    rssArray[6] = "http://feeds.reuters.com/Reuters/domesticNews";
	    rssArray[7] = "http://feeds.reuters.com/reuters/sportsNews";
	    rssArray[8] = "http://feeds.reuters.com/reuters/entertainment";


	    
		String keyword = request.getParameter("keyword");
		System.out.println("KEYWORD: " + keyword);
		String result = rssFeed(keyword);
		System.out.println(result);
		
		PrintWriter out = response.getWriter();
		
		//Start HTML
		out.println("<html>");
		out.println("<head>");
		
		//Set style
		out.println("<style>");

		out.println("</style>");

		out.println("</head>");
		
		//Start body
		out.println("<body style='padding: 25px; background-color: #ececec;'>");
		
		//Insert Reuters image
		out.println("<a href='/FinalProject/SimpleServlet'>");		
		out.println("<img src='/FinalProject/reuters2.png'>");
		out.println("</a>");
		
		//Start center
		out.println("<br />");
		out.println("<center>");
		

		//Iterate through item list
		for (int i=0; i< itemCount; i++){
			if (titleArray[i] != null){
				out.println("<div style='background-color: #fffefe; padding: 25px;'>");
				out.println("<a href='" + linkArray[i] + "'>");
				out.println("<h3 style='text-align:left;'>" + titleArray[i] + "</h3>");
				out.println("</a>");
				out.println("<p style='text-align:left;'>" + descriptionArray[i] + "</p>");
				out.println("<p style='text-align:left;'>" + pubDateArray[i] + "</p>");
				out.println("</div>");
				out.println("<br />");
			}
			else {
				break;
			}
		}
		
		for (int i=0; i<titleArray.length; i++){
			System.out.println(titleArray[i]);
		}
		
		//End center
		out.println("</center>");

		//End body
		out.println("</body>");
		
		//End html
		out.println("</html>");
		
	
		
	}
	
	public static String rssFeed(String keyword){
		
		//Empties arrays
		Arrays.fill(titleArray, null);
		Arrays.fill(descriptionArray, null);
		Arrays.fill(linkArray, null);
		Arrays.fill(pubDateArray, null);

		
		boolean newsMatch = false;
		itemCount = 0;
        String code = "";
        
        System.out.println("itemcount: " + itemCount);
        int count = 0;

		
		for (int k = 0; k < rssArray.length; k++){
	        try {
	            URL rssUrl = new URL(rssArray[k]);
	            BufferedReader br = new BufferedReader(new InputStreamReader(rssUrl.openStream()));
	            String line;

	            while ((line = br.readLine())!= null){
	                if (line.contains("<item>") == true){
	                	newsMatch = false;
	                    line = br.readLine(); //advance to next line after <item>
	                    //Read title
	                    int firstPos = line.indexOf("<title>");
	                    String temp = line.substring(firstPos);
	                    temp = temp.replace("<title>", "");

	                    int lastPos = line.indexOf("</title>");
	                    lastPos = lastPos - 2;
	                    temp  = temp.substring(0, lastPos);
	                    temp = temp.replace("</title", "");
	                    
	                    
	                    //Check if news match keyword
	                    if (temp.toLowerCase().contains(keyword.toLowerCase()) == true){

	                        newsMatch = true;
	                    }
	                    
	                    
	                    //Check if news match description
	                    
	                    //Advance 3 lines
	                    for (int i=0; i<3; i++){
	                    	line = br.readLine();
	                    }
	                    firstPos = line.indexOf("<description>");
	                    String temp2 = line.substring(firstPos);
	                    temp2 = temp2.replace("<description>", "");
	                    lastPos = line.indexOf(".&lt");
	                    lastPos = lastPos - 15;
	                    temp2 = temp2.substring(0, lastPos);
	                    temp2 = temp2.replace("</description>", "");
	                    
	                    //Check for news match
	                    if (temp2.toLowerCase().contains(keyword.toLowerCase()) == true){
	                    	newsMatch = true;
	                    }
	                    
	                    if (newsMatch == true){
	                        titleArray[count] = temp;
	                        descriptionArray[count] = temp2;
	                        code = code + temp + "\n";
	                        code = code + temp2 + "\n";
	                    	
	                      //Advance 3 lines
	                        for (int i=0; i<3; i++){
	                            line = br.readLine();
	                        }
	                        
	                            //Read link
	                            firstPos = line.indexOf("<link>");
	                            temp = line.substring(firstPos);
	                            temp = temp.replace("<link>","");
	                            lastPos = line.indexOf("</link>");
	                            lastPos = lastPos - 2;

	                            temp = temp.substring(0, lastPos);
	                            temp = temp.replace("</link", "");
	                            linkArray[count] = temp;
	                            code = code + temp + "\n";

	                            //Advance 4 lines
	                            for (int i = 0; i < 3; i++){
	                                line = br.readLine();
	                            }

	                            //Read pubDate
	                            firstPos = line.indexOf("<pubDate>");
	                            temp = line.substring(firstPos);
	                            temp = temp.replace("<pubDate>","");
	                            lastPos = line.indexOf("</pubDate>");
	                            lastPos = lastPos - 2;

	                            temp = temp.substring(0, lastPos);
	                            temp = temp.replace("</pubDate", "");
	                            pubDateArray[count] = temp;
	                            temp = "PubDate: " + temp;
	                            code = code + temp + "\n";

	                            //Add blank line between news items;
	                            code = code + "\n";

	                            //Increase count
	                            count = count + 1;
	                            itemCount = itemCount + 1;
	                        
	                    }
	                    else {
	                    	continue;
	                    }
	                }
	            }
	            br.close();
	        }catch(Exception e){e.printStackTrace();}
		}
        return code;
    }
	
	public String populatePage(){
		//Empties arrays
		Arrays.fill(titleArray, null);
		Arrays.fill(descriptionArray, null);
		Arrays.fill(linkArray, null);
		Arrays.fill(pubDateArray, null);

		
		boolean newsMatch = false;
		itemCount = 0;
        String code = "";
        
        System.out.println("itemcount: " + itemCount);
		
	        try {
	            URL rssUrl = new URL("http://feeds.reuters.com/reuters/topNews");
	            BufferedReader br = new BufferedReader(new InputStreamReader(rssUrl.openStream()));
	            String line;
	            int count = 0;

	            while ((line = br.readLine())!= null){
	                if (line.contains("<item>") == true){
	                	newsMatch = false;
	                    line = br.readLine(); //advance to next line after <item>
	                    //Read title
	                    int firstPos = line.indexOf("<title>");
	                    String temp = line.substring(firstPos);
	                    temp = temp.replace("<title>", "");

	                    int lastPos = line.indexOf("</title>");
	                    lastPos = lastPos - 2;
	                    temp  = temp.substring(0, lastPos);
	                    temp = temp.replace("</title", "");
	                    
	            
	                    
	                    
	                    
	                    //Advance 3 lines
	                    for (int i=0; i<3; i++){
	                    	line = br.readLine();
	                    }
	                    firstPos = line.indexOf("<description>");
	                    String temp2 = line.substring(firstPos);
	                    temp2 = temp2.replace("<description>", "");
	                    lastPos = line.indexOf(".&lt");
	                    lastPos = lastPos - 15;
	                    temp2 = temp2.substring(0, lastPos);
	                    temp2 = temp2.replace("</description>", "");
	                    
	                        titleArray[count] = temp;
	                        descriptionArray[count] = temp2;
	                        code = code + temp + "\n";
	                        code = code + temp2 + "\n";
	                    	
	                      //Advance 3 lines
	                        for (int i=0; i<3; i++){
	                            line = br.readLine();
	                        }
	                        
	                            //Read link
	                            firstPos = line.indexOf("<link>");
	                            temp = line.substring(firstPos);
	                            temp = temp.replace("<link>","");
	                            lastPos = line.indexOf("</link>");
	                            lastPos = lastPos - 2;

	                            temp = temp.substring(0, lastPos);
	                            temp = temp.replace("</link", "");
	                            linkArray[count] = temp;
	                            code = code + temp + "\n";

	                            //Advance 4 lines
	                            for (int i = 0; i < 3; i++){
	                                line = br.readLine();
	                            }

	                            //Read pubDate
	                            firstPos = line.indexOf("<pubDate>");
	                            temp = line.substring(firstPos);
	                            temp = temp.replace("<pubDate>","");
	                            lastPos = line.indexOf("</pubDate>");
	                            lastPos = lastPos - 2;

	                            temp = temp.substring(0, lastPos);
	                            temp = temp.replace("</pubDate", "");
	                            pubDateArray[count] = temp;
	                            temp = "PubDate: " + temp;
	                            code = code + temp + "\n";

	                            //Add blank line between news items;
	                            code = code + "\n";

	                            //Increase count
	                            count = count + 1;
	                            itemCount = itemCount + 1;
	                       
	                }
	            }
	            br.close();
	        }catch(Exception e){e.printStackTrace();}
        return code;
		
	}
}
