package co.chosunbiz.choice.batch.job;

import com.mongodb.BasicDBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class JsonProcessor implements ItemProcessor<BasicDBObject, BasicDBObject> {

    private static final Logger log = LoggerFactory.getLogger(JsonProcessor.class);

    @Override
    public BasicDBObject process(BasicDBObject item) throws Exception {
        item.append("collectionTime",System.currentTimeMillis());
        log.info(item.toString());
        return item;
    }
}
