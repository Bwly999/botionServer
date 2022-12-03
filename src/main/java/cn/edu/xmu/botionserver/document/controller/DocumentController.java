package cn.edu.xmu.botionserver.document.controller;

import cn.edu.xmu.botionserver.common.ErrorNo;
import cn.edu.xmu.botionserver.common.Result;
import cn.edu.xmu.botionserver.document.dao.DocumentDao;
import cn.edu.xmu.botionserver.document.model.Document;
import cn.edu.xmu.botionserver.document.model.vo.DocumentBrief;
import cn.edu.xmu.botionserver.util.MongoUtils;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/botion/document", produces = "application/json;charset=UTF-8")
@Slf4j
public class DocumentController {
    private DocumentDao documentDao;
    private MongoTemplate mongoTemplate;

    private KafkaTemplate<Object, Object> kafkaTemplate;

    @Autowired
    public void setKafkaTemplate(KafkaTemplate<Object, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Autowired
    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Autowired
    public void setDocumentDao(DocumentDao documentDao) {
        this.documentDao = documentDao;
    }

    /**
     * 添加新文档
     * @param newDocument 文档
     * @return
     */
    @PostMapping("")
    public Result<Document> addDocument(@RequestBody Document newDocument) {
        Document document = Document.builder()
                .name(newDocument.getName())
                .blocks(newDocument.getBlocks())
                .build();
        Document insertResult = documentDao.insert(document);
        return Result.ok(insertResult);
    }

    @PutMapping("/{id}")
    public Result<String> saveDocument(@PathVariable("id") String id, @RequestBody Document newDocument) {
        newDocument.setId(id);
        log.info("{}", newDocument);
        this.kafkaTemplate.send("save", newDocument);
        return Result.ok("保存成功");
//        newDocument.setId(null);
//        Update update = MongoUtils.getUpdateByObj(newDocument);
//        UpdateResult updateResult = mongoTemplate.updateFirst(new Query(Criteria.where("id").is(id)), update, Document.class);
//        if (updateResult.getMatchedCount() == 0L) {
//            return Result.fail(ErrorNo.DOCUMENT_NOT_FOUND);
//        } else {
//            return Result.ok("保存成功");
//        }
    }

    /**
     * 获取指定id的文档
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Document> getDocumentById(@PathVariable("id") String id) {
        Optional<Document> documentOptional = documentDao.findById(id);
        return documentOptional.map(Result::ok).orElseGet(() -> Result.fail(ErrorNo.DOCUMENT_NOT_FOUND));
    }

    /**
     * 获取文档列表
     * @return
     */
    @GetMapping("/list")
    public Result<List<DocumentBrief>> listDocument() {
        List<Document> documents = documentDao.findAll();
        List<DocumentBrief> documentBriefs = documents.stream().map(document -> {
            DocumentBrief documentBrief = new DocumentBrief();
            BeanUtils.copyProperties(document, documentBrief);
            return documentBrief;
        }).collect(Collectors.toList());
        return Result.ok(documentBriefs);
    }

    @DeleteMapping("/{id}")
    public Result<String> deleteDocumentById(@PathVariable("id") String id) {
        documentDao.deleteById(id);
        return Result.ok("删除成功");
    }

}
