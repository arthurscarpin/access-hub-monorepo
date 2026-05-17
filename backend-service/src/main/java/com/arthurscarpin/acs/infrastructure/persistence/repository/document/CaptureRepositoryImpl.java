package com.arthurscarpin.acs.infrastructure.persistence.repository.document;

import com.arthurscarpin.acs.infrastructure.persistence.entity.document.CaptureEntity;
import com.arthurscarpin.acs.infrastructure.persistence.entity.document.CaptureImageEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CaptureRepositoryImpl implements CustomCaptureRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public CaptureEntity incrementProcessedCountAndUpsertSingleImage(String id, String imageId, CaptureImageEntity image) {
        // Filtra pela captura E garante que essa imagem específica NÃO está COMPLETED
        Query query = new Query(
                Criteria.where("id").is(id)
                        .and("images").elemMatch(Criteria.where("id").is(imageId).and("status").ne("COMPLETED"))
        );

        // Atualiza atomicamente apenas os campos da imagem que mudou e soma 1 no contador global
        Update update = new Update()
                .set("images.$.ocr", image.getOcr())
                .set("images.$.status", "COMPLETED")
                .set("images.$.timestamp", image.getTimestamp())
                .inc("processedImagesCount", 1);

        // returnNew(true) garante que o retorno trará o contador já atualizado (ex: 10/10)
        return mongoTemplate.findAndModify(
                query,
                update,
                FindAndModifyOptions.options().returnNew(true),
                CaptureEntity.class
        );
    }
}
