package com.example.datn.domain.repository;

import com.example.datn.domain.entity.Product;
import com.example.datn.domain.utility.DataResponse;
import com.example.datn.domain.utility.ObjectMapperUntil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.ml.job.results.Bucket;
import org.elasticsearch.common.document.DocumentField;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class ProductESRepository {
    @Autowired
    private RestHighLevelClient client;

    private final String INDEX_NAME = "my-index-product";

    public void addProduct(Product product)  {
        String document = ObjectMapperUntil.serialize(product);
        String id = Long.toString(product.getProductID());
        IndexRequest indexRequest = new IndexRequest(INDEX_NAME)
                .id(id)
                .source(document, XContentType.JSON);
        log.info("document:{}", document);
        IndexResponse response = null;
        try {
            response = client.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("response: {}", response);

    }


    public void updateProduct(Product product) {
        String document = ObjectMapperUntil.serialize(product);
        String id = Long.toString(product.getProductID());
        UpdateRequest updateRequest = new UpdateRequest(INDEX_NAME, id).doc(document,XContentType.JSON);
        UpdateResponse response;
        try {
            response = client.update(updateRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("response: {}", response);
    }

    public List<Product> getRelated(Product product) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        QueryBuilders.rangeQuery("cost").lte(product.getCost() *2).gt(0);

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .mustNot(QueryBuilders.termQuery("productID",product.getProductID()))
                .must(QueryBuilders.termQuery("categoryID",product.getCategoryID()))
                .must(QueryBuilders.rangeQuery("cost").lte(product.getCost() *4/3).gt(product.getCost() *2/3))
                .must(QueryBuilders.termQuery("status",0));

        searchSourceBuilder.query(boolQueryBuilder);
        SearchRequest searchRequest= new SearchRequest(INDEX_NAME);
        searchRequest.source(searchSourceBuilder);
        SearchResponse response = null;
        try {
            response = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        SearchHit[] searchHits = response.getHits().getHits();
        List<Product> productList = new ArrayList<>();
        for(SearchHit searchHit : searchHits) {
            String value = searchHit.getSourceAsString();
            Product product1 = ObjectMapperUntil.deserialize(value, Product.class);
            productList.add(product1);
        }
        if(productList.contains(product)) productList.remove(product);
        return productList;
    }

    public void suggestByUser(long productID ) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        QueryBuilder queryBuilder = QueryBuilders.matchQuery("movies_liked.keyword", "Terminator");
        searchSourceBuilder.query(queryBuilder);
        AggregationBuilder aggregationBuilder = AggregationBuilders.terms("movies_liked.keyword").field("movies_liked.keyword").minDocCount(1);
        searchSourceBuilder.aggregation(aggregationBuilder);
//        searchSourceBuilder.aggregation(AggregationBuilders.terms("movies_liked.keyword").minDocCount(1));
//        searchSourceBuilder.aggregations(AggregationBuilders.terms("movies_liked.keyword").minDocCount(1));
        SearchRequest searchRequest= new SearchRequest("recs");
        searchRequest.source(searchSourceBuilder);
        SearchResponse response = null;
        try {
            response = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        AggregationRe
        SearchHit[] searchHits = response.getHits().getHits();
        ParsedStringTerms aggregations = response.getAggregations().get("movies_liked.keyword");
        List<Terms.Bucket> bucketList = (List<Terms.Bucket>) aggregations.getBuckets();
        for(Terms.Bucket bucket : bucketList) {
            String name = bucket.getKeyAsString();
            log.info("bucket: {}", bucket);
        }

        aggregations.getName();
        long count = response.getHits().getTotalHits().value;
        for(SearchHit searchHit : searchHits) {
            Map<String, Object> map = searchHit.getSourceAsMap();
            String value = searchHit.getSourceAsString();
            log.info("get map: ", value);
        }

    }

    public DataResponse search(String keyword, long categoryID, int offset, int limit) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        QueryBuilders.rangeQuery("cost").lte(product.getCost() *2).gt(0);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        if(keyword != null && !keyword.trim().equals("")) {
            if(keyword.startsWith("A-") || keyword.startsWith("Q-"))
            {
                boolQueryBuilder.must(QueryBuilders.termQuery("code.keyword",keyword));
            }
            else boolQueryBuilder.must(QueryBuilders.multiMatchQuery(keyword, "name", "description"));
        }
        if(categoryID != 0) {
            boolQueryBuilder.must(QueryBuilders.termQuery("categoryID", categoryID));
        }
         boolQueryBuilder.must(QueryBuilders.termQuery("status",0));
        searchSourceBuilder.size(limit);
        searchSourceBuilder.from(offset);
        searchSourceBuilder.sort(new FieldSortBuilder("updateTime").order(SortOrder.DESC));
        searchSourceBuilder.query(boolQueryBuilder);

        SearchRequest searchRequest= new SearchRequest(INDEX_NAME);
        searchRequest.source(searchSourceBuilder);

        SearchResponse response = null;
        try {
            response = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<Product> list = new ArrayList<>();
        SearchHit[] searchHits = response.getHits().getHits();
        long count = response.getHits().getTotalHits().value;
        for(SearchHit searchHit : searchHits) {
            String value = searchHit.getSourceAsString();
            Product product = ObjectMapperUntil.deserialize(value, Product.class);
            list.add(product);
        }
        int totalPage = (int) count/limit;
        if(count % limit >0) totalPage++;
        DataResponse dataResponse = new DataResponse(list,totalPage,(int) count);
        return dataResponse;
    }
}
