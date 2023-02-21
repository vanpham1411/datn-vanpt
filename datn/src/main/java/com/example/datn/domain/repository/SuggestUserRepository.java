package com.example.datn.domain.repository;

import com.example.datn.domain.entity.Product;
import com.example.datn.domain.utility.ListID;
import com.example.datn.domain.utility.ObjectMapperUntil;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class SuggestUserRepository {
    @Autowired
    private RestHighLevelClient client;

    private final String INDEX_NAME = "user-product";

    public List<Long> getByID(long userID) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("userID", userID);
        searchSourceBuilder.query(termQueryBuilder);
        SearchResponse response = null;
        SearchRequest searchRequest = new SearchRequest(INDEX_NAME);
        searchRequest.source(searchSourceBuilder);
        try {
            response = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        long count = response.getHits().getTotalHits().value;
        if (count == 0) return new ArrayList<>();
        SearchHit searchHit = response.getHits().getHits()[0];
        String value = searchHit.getSourceAsString();
        ListID listID = ObjectMapperUntil.deserialize(value, ListID.class);
        return listID.getListID();


    }

    public void updateDoc(long userID, List<Long> productList) {
        List<Long> oldList = getByID(userID);
        if(oldList.size() == 0) {
            ListID listID = new ListID(userID, productList);
            String document = ObjectMapperUntil.serialize(listID);
            String id = Long.toString(userID);
            IndexRequest indexRequest = new IndexRequest(INDEX_NAME)
                    .id(id)
                    .source(document, XContentType.JSON);
            log.info("document:{}", document);
            IndexResponse response = null;
            try {
                response = client.index(indexRequest, RequestOptions.DEFAULT);
            } catch (IOException e) {
                log.error(e.getMessage());
                throw new RuntimeException(e);
            }
            log.info("create list bought response: {}", response);
        }
        else  {
            for(long newID: productList) {
                if(!oldList.contains(newID)) oldList.add(newID);
            }
            ListID listID = new ListID(userID, oldList);
            String document = ObjectMapperUntil.serialize(listID);
            String id = Long.toString(userID);
            UpdateRequest updateRequest = new UpdateRequest(INDEX_NAME, id).doc(document, XContentType.JSON);
            UpdateResponse response;
            try {
                response = client.update(updateRequest, RequestOptions.DEFAULT);
            } catch (IOException e) {
                log.error(e.getMessage());
                throw new RuntimeException(e);
            }
            log.info("update list bought response: {}", response);
        }
    }

    public List<Long> suggestProduct(List<Long> listInputID) {

//        return new ArrayList<>();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        for(Long inputID : listInputID) {
            boolQueryBuilder.should(QueryBuilders.matchQuery("listID", inputID));
        }
        int count = listInputID.size() >=4 ? 4 : listInputID.size();
        boolQueryBuilder.minimumShouldMatch(count);
        searchSourceBuilder.query(boolQueryBuilder);
//        QueryBuilder queryBuilder = QueryBuilders.matchQuery("listID", productID);
//        searchSourceBuilder.query(queryBuilder);
        AggregationBuilder aggregationBuilder = AggregationBuilders.terms("productID").field("listID").minDocCount(1);
        searchSourceBuilder.aggregation(aggregationBuilder);
        SearchRequest searchRequest= new SearchRequest(INDEX_NAME);
        searchRequest.source(searchSourceBuilder);
        SearchResponse response = null;
        try {
            response = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        AggregationRe
        SearchHit[] searchHits = response.getHits().getHits();
        ParsedLongTerms aggregations = response.getAggregations().get("productID");
        List<Terms.Bucket> bucketList = (List<Terms.Bucket>) aggregations.getBuckets();
        List<Long> listProductID = new ArrayList<>();
        for(Terms.Bucket bucket : bucketList) {
            String name = bucket.getKeyAsString();
//            Number id = bucket.getKeyAsNumber();
            long id = (long) bucket.getKey();
            bucket.getDocCount();
            listProductID.add(id);
        }
        return listProductID;
    }


//    public List<Long> suggestProduct(long productID) {
//        List<Long> listInputID = new ArrayList<>();
//        listInputID.add(12818737659L);
//        listInputID.add(13522827723L);
////        return new ArrayList<>();
//        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//        for(Long inputID : listInputID) {
//            boolQueryBuilder.should(QueryBuilders.matchQuery("listID", inputID));
//        }
//        int count = listInputID.size() >=4 ? 4 : listInputID.size();
//        boolQueryBuilder.minimumShouldMatch(count);
//        searchSourceBuilder.query(boolQueryBuilder);
////        QueryBuilder queryBuilder = QueryBuilders.matchQuery("listID", productID);
////        searchSourceBuilder.query(queryBuilder);
//        AggregationBuilder aggregationBuilder = AggregationBuilders.terms("productID").field("listID").minDocCount(1);
//        searchSourceBuilder.aggregation(aggregationBuilder);
//        SearchRequest searchRequest= new SearchRequest(INDEX_NAME);
//        searchRequest.source(searchSourceBuilder);
//        SearchResponse response = null;
//        try {
//            response = client.search(searchRequest, RequestOptions.DEFAULT);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
////        AggregationRe
//        SearchHit[] searchHits = response.getHits().getHits();
//        ParsedStringTerms aggregations = response.getAggregations().get("productID");
//        List<Terms.Bucket> bucketList = (List<Terms.Bucket>) aggregations.getBuckets();
//        List<Long> listProductID = new ArrayList<>();
//        for(Terms.Bucket bucket : bucketList) {
//            String name = bucket.getKeyAsString();
//            Number id = bucket.getKeyAsNumber();
//            bucket.getDocCount();
//            listProductID.add((Long) id);
//            log.info("bucket: {}", bucket);
//        }
//        return listProductID;
//    }


}


