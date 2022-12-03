package cn.edu.xmu.botionserver.document.dao;

import cn.edu.xmu.botionserver.document.model.Document;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DocumentDao extends MongoRepository<Document, String> {
}
