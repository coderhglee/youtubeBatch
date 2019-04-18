package co.chosunbiz.choice.batch.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;


public class BatchUtil {
    private static final Logger log = LoggerFactory.getLogger(BatchUtil.class);

    public static String readUrl(String urlString) throws Exception {
        log.info(urlString);
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream(),"utf-8"));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);

            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }
    }


//    public static void main(String[] arg){
//
//        try {
////            log.info(readUrl().toString());
//
//            String searchJson = BatchUtil.readUrl("https://www.googleapis.com/youtube/v3/search?part=snippet&channelId=UCWlV3Lz_55UaX4JsMj-z__Q&maxResults=10&publishedAfter=2019-03-11T00:00:00Z&publishedBefore=2019-03-11T23:59:59Z&key=AIzaSyCO7KVJbOD_xpWoyDFCboZMszFBGBCsln4");
//
//
//            JsonParser parser = new JsonParser();
//            JsonElement element = parser.parse(searchJson);
//            JsonArray items = element.getAsJsonObject().get("items").getAsJsonArray();
//
//            JsonArray newItems = new JsonArray();
//            for(JsonElement item : items){
//                JsonObject itemObject = new JsonObject();
//                String videoId = item.getAsJsonObject().get("id").getAsJsonObject().get("videoId").toString().replaceAll("\"","");
//                String videoJson = BatchUtil.readUrl("https://www.googleapis.com/youtube/v3/videos?part=statistics&id="+videoId+"&key=AIzaSyBU4L3Da6LjQks_Ln7Z_aPdukzmnEGiNtM");
//
//                JsonElement vElement = parser.parse(videoJson);
//                JsonArray vItems = vElement.getAsJsonObject().get("items").getAsJsonArray();
//
//                itemObject.add("snippet",item);
//                itemObject.add("statistics",vItems.get(0).getAsJsonObject().get("statistics"));
//                newItems.add(itemObject);
//            }
//            JsonObject newObject = new JsonObject();
//            newObject.addProperty("channelId","UCWlV3Lz_55UaX4JsMj");
//            newObject.add("items",newItems);
//            log.info(newObject.toString());
//        }catch (Exception e){
//
//        }
//    }

}
