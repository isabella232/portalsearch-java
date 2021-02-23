package devecojava;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class PortalSearch {
	static String protocal = "https";
	static String domainhost = "ecosystem.egain.cloud";
	static String anonymousinit = "/system/ws/v15/ss/portal/{portalId}/authentication/anonymous";
	static String searchurl = "/system/ws/v11/ss/search?portalId={portalId}&q=eGain&rangestart=0&rangesize=5&usertype=customer&$attribute=all";
	static String portalID = "555500000001000";
	static String params = null;
	static String operation = null;
	static String sid = null;

	public String anonymousinit() {
	    params = anonymousinit.replace("{portalId}", portalID);
	    return connectREST("POST", null);
	}

	public String search() {
	    params = searchurl.replace("{portalId}", portalID);
	    return connectREST("GET", null);
	}

    private String connectREST(String method, JSONObject payload) {
        int responseCode = 0;
        StringBuffer result = new StringBuffer();

        try {
            URL url = new URL(protocal + "://" + domainhost + params);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Accept-Language", "en-US");
            if (method.equals("POST")) {
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                /*
                if (sid != null) urlConnection.setRequestProperty("X-egain-session",sid);
                if (payload != null) {
                    OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                    wr.write(payload.toString());
                    wr.flush();
                }
                */
            }
            else {
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("X-egain-session",sid);
                urlConnection.setRequestProperty("Accept", "application/json");
            }
            urlConnection.setRequestMethod(method);
            responseCode = urlConnection.getResponseCode();

            // Process Request response
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                result.append(inputLine);
            }
            in.close();
            if (urlConnection.getHeaderField("X-egain-session") != null) {
            	sid = urlConnection.getHeaderField("X-egain-session");
            }
        } catch (Exception e) {
        	System.out.println(e.toString());
        }
        return "Status:"+responseCode+"\n"+result.toString();
    }

    public static void main(String[] args)
    {
    	PortalSearch ks = new PortalSearch();
    	ks.anonymousinit();
    	System.out.print(ks.search());
    }
}
