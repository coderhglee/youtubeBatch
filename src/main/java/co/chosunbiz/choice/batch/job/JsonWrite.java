package co.chosunbiz.choice.batch.job;

import com.mongodb.BasicDBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

public class JsonWrite extends MongoItemWriter<BasicDBObject> {

    private static final Logger log = LoggerFactory.getLogger(JsonWrite.class);

    public JsonWrite(MongoTemplate template,String collection) {
        super();
        this.setTemplate(template);
        this.setCollection(collection);
    }

    @Override
    public void write(List<? extends BasicDBObject> items) throws Exception {
        log.info(items.size() + "");
        super.write(items);
    }
}
