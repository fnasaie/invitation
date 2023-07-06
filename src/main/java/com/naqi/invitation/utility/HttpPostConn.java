package com.naqi.invitation.utility;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.naqi.invitation.InvitationApplication;


public class HttpPostConn {

    public static HttpResult SendHttpsRequest(String httpMethod, String targetUrl, HashMap httpHeader, String requestBody, int connectTimeout, int waitTimeout, String transctionId) {
        HttpResult response = new HttpResult();
        String logprefix = "HttpsConn";
        Logger.application.info(Logger.pattern, InvitationApplication.VERSION, logprefix, transctionId,
                "Send EBill Url" + targetUrl);
        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }

                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }

                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }};

            // Ignore differences between given hostname and certificate hostname
            HostnameVerifier hv = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            SSLContext sc = SSLContext.getInstance("TLSv1.2");
            sc.init(null, trustAllCerts, new SecureRandom());
            // Logger.application.info(Logger.pattern, WhatsappWrapperServiceApplication.VERSION, logprefix, "Sending Request to :" + targetUrl, "");
            URL url = new URL(targetUrl);
            url.openConnection();
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            //for https uncomment below
            // HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            // con.setSSLSocketFactory(sc.getSocketFactory());
            // con.setHostnameVerifier(hv);
            con.setConnectTimeout(connectTimeout);
            con.setReadTimeout(waitTimeout);
            con.setRequestMethod(httpMethod);
            con.setDoOutput(true);


            //Set HTTP Headers
            Iterator it = httpHeader.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                con.setRequestProperty((String) pair.getKey(), (String) pair.getValue());
                it.remove(); // avoids a ConcurrentModificationException
            }

            con.connect();
            Logger.application.info(Logger.pattern, InvitationApplication.VERSION, logprefix, transctionId,
                    "Body: " + requestBody.toString());

            if (requestBody != null) {
                //for post paramters in JSON Format                
                OutputStream os = con.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os, StandardCharsets.UTF_8);

                osw.write(requestBody);
                osw.flush();
                osw.close();
            }

            int responseCode = con.getResponseCode();
            response.httpResponseCode = responseCode;


            BufferedReader in;
            if (responseCode < HttpsURLConnection.HTTP_BAD_REQUEST) {
                in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            String inputLine;
            StringBuilder httpMsgResp = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {

                httpMsgResp.append(inputLine);
            }
            in.close();
            Logger.application.info(Logger.pattern, InvitationApplication.VERSION, logprefix, transctionId,
                    "Response" + response);

            String newString = httpMsgResp.toString();

            // JSONObject xmlJSONObj = XML.toJSONObject(inputLine);
            // xmlJSONObj.getString("ns2:billPresentmentResponse");
            // String jsonPrettyPrintString = xmlJSONObj.toString(4);

            response.resultCode = 0;
            response.responseBody = newString;

        } catch (SocketTimeoutException ex) {

            Logger.application.info(Logger.pattern, InvitationApplication.VERSION, logprefix, transctionId,
                    "SocketTimeoutException " + ex.getMessage());
            if (ex.getMessage().equals("Read timed out")) {
                response.resultCode = -1;
                response.errorMessage = ex.getMessage();
                response.httpResponseCode = 205;
            } else {
                response.resultCode = -1;
                response.errorMessage = ex.getMessage();
                response.httpResponseCode = 204;

            }
        } catch (Exception ex) {
            Logger.application.info(Logger.pattern, InvitationApplication.VERSION, logprefix, transctionId,
                    "Exception " + ex.getMessage());

            //exception occur
            response.resultCode = -1;
            response.errorMessage = ex.getMessage();
            response.httpResponseCode = 203;

        }

        return response;
    }
}
