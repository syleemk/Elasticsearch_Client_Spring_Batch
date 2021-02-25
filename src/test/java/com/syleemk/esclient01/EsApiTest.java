package com.syleemk.esclient01;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.syleemk.esclient01.dto.NaverDto;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

@SpringBootTest
public class EsApiTest {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    ObjectMapper objectMapper;

    @DisplayName("조회 API 테스트")
    @Test
    void getTest() throws IOException {
        GetRequest request = new GetRequest("movie_search", "_doc", "ejzJqmkBjjM-ebDb8PsR");
        GetResponse response = restHighLevelClient.get(request, RequestOptions.DEFAULT);

        if (response.isExists()) {
            long version = response.getVersion();
            Map<String, Object> sourceAsMap = response.getSourceAsMap();
            System.out.println(sourceAsMap.get("movieNm"));
        } else {
            System.out.println("결과가 존재하지 않습니다.");
        }

    }

    @DisplayName("ES에서 가져온 Object타입을 자바객체로 매핑 테스트")
    @Test
    void getObjectType() throws IOException {
        GetRequest request = new GetRequest("naver_test", "_doc", "1");
        GetResponse response = restHighLevelClient.get(request, RequestOptions.DEFAULT);

        if (response.isExists()) {
            long version = response.getVersion();
            Map<String, Object> sourceAsMap = response.getSourceAsMap();
            System.out.println(sourceAsMap.get("business"));

            String sourceAsString = response.getSourceAsString();
            NaverDto naverDto = objectMapper.readValue(sourceAsString, NaverDto.class);

            System.out.println("businessDto.getBusiness() = " + naverDto.getBusiness());
            System.out.println(naverDto.getBusiness().getName());

        } else {
            System.out.println("결과가 존재하지 않습니다.");
        }
    }

    @DisplayName("search query 테스트")
    @Test
    void searchApiTest() throws IOException {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        searchSourceBuilder.query(matchAllQuery());
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(5);
        searchSourceBuilder.sort(new FieldSortBuilder("prdtStatNm").order(SortOrder.DESC));

        SearchRequest searchRequest = new SearchRequest("movie_search");
        searchRequest.types("_doc");
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        for (SearchHit hit : hits) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            System.out.println(sourceAsMap.get("movieNm"));
        }
    }

    @DisplayName("search query 테스트 객체 매핑")
    @Test
    void searchApiTest2() throws IOException {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        searchSourceBuilder.fetchSource("business", null);
        searchSourceBuilder.query(matchAllQuery());
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(5);

        SearchRequest searchRequest = new SearchRequest("naver_test");
        searchRequest.types("_doc");
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        for (SearchHit hit : hits) {
            String sourceAsString = hit.getSourceAsString();
            NaverDto naverDto = objectMapper.readValue(sourceAsString, NaverDto.class);
            System.out.println();
        }
    }

    @DisplayName("Scroll Api 테스트")
    @Test
    void scrollApiTest() throws IOException {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(matchQuery("movieNm", "곤지암"));

        final Scroll scroll = new Scroll(TimeValue.timeValueMinutes(1L));

        SearchRequest searchRequest = new SearchRequest("movie_search");
        searchRequest.source(searchSourceBuilder);
        searchRequest.scroll(scroll);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        String scrollId = searchResponse.getScrollId();
        SearchHit[] hits = searchResponse.getHits().getHits();

        while (hits != null && hits.length > 0) {
            for (SearchHit hit : hits) {
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                System.out.println(sourceAsMap.get("movieNm"));
            }

            SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
            scrollRequest.scroll(scroll);

            searchResponse = restHighLevelClient.scroll(scrollRequest, RequestOptions.DEFAULT);
            scrollId = searchResponse.getScrollId();
            hits = searchResponse.getHits().getHits();

        }
    }
}
