package cn.edu.xmu.botionserver.document.kafka;

import cn.edu.xmu.botionserver.common.ErrorNo;
import cn.edu.xmu.botionserver.common.Result;
import cn.edu.xmu.botionserver.document.dao.DocumentDao;
import cn.edu.xmu.botionserver.document.model.Document;
import cn.edu.xmu.botionserver.util.MongoUtils;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class DocumentListener {
    private DocumentDao documentDao;
    private MongoTemplate mongoTemplate;

    private final TaskExecutor executor = new SimpleAsyncTaskExecutor("document-listener");

    @Autowired
    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Autowired
    public void setDocumentDao(DocumentDao documentDao) {
        this.documentDao = documentDao;
    }

    @KafkaListener(id = "document", topics = "save")
    public void saveDocument(Document newDocument) {
        executor.execute(() -> {
            Update update = MongoUtils.getUpdateByObj(newDocument);
            mongoTemplate.updateFirst(new Query(Criteria.where("id").is(newDocument.getId())), update, Document.class);
        });
    }
}
