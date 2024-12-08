package ru.rsreu.elasticsearch.client.example;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortMode;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.AggregationRange;
import co.elastic.clients.elasticsearch._types.aggregations.RangeBucket;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ElasticSearchDao implements AutoCloseable {
    public static final String RELEVANCE_SCORE_RANGES = "relevance_score_ranges";
    private static volatile ElasticSearchDao instance;
    private final ElasticsearchClient elasticsearchClient;

    public static ElasticSearchDao getInstance(String serverUrl) {
        if (instance == null) {
            synchronized (ElasticSearchDao.class) {
                if (instance == null) {
                    ElasticsearchClient client = ElasticSearchClientFactory.getInstance().openConnection(serverUrl);
                    instance = new ElasticSearchDao(client);
                }
            }
        }
        return instance;
    }

    @SneakyThrows
    public SearchResponse<Question> getQuestionsByMatchBoolPrefixBody(String bodyQuery,
                                                                      int minimumShouldMatch) {
        return elasticsearchClient.search(searchBuilder ->
                        searchBuilder.index("e08_03_astashkin")
                                .query(queryBuilder -> queryBuilder.matchBoolPrefix(
                                        matchBoolPrefixBuilder -> matchBoolPrefixBuilder
                                                .field("body")
                                                .query(bodyQuery)
                                                .minimumShouldMatch(String.valueOf(minimumShouldMatch))))
                                .sort(sortBuilder -> sortBuilder.field(
                                        fieldSortBuilder -> fieldSortBuilder.field("body")
                                                .order(SortOrder.Desc)
                                                .mode(SortMode.Min)
                                )),
                Question.class
        );
    }

    @SneakyThrows
    public List<RangeBucket> getQuestionsRelevanceScoreRangeBuckets() {
        SearchResponse<Void> searchResponse = elasticsearchClient.search(searchBuilder -> searchBuilder.size(0)
                        .aggregations(RELEVANCE_SCORE_RANGES, aggregationBuilder -> aggregationBuilder.range(
                                rangeAggregationBuilder -> rangeAggregationBuilder
                                        .field("relevance_score")
                                        .ranges(AggregationRange.of(rangeBuilder -> rangeBuilder.to(25d)),
                                                AggregationRange.of(rangeBuilder -> rangeBuilder.from(25d).to(50d)),
                                                AggregationRange.of(rangeBuilder -> rangeBuilder.from(50d).to(70d)),
                                                AggregationRange.of(rangeBuilder -> rangeBuilder.from(70d)))
                        )),
                Void.class
        );
        return searchResponse.aggregations().get(RELEVANCE_SCORE_RANGES).range().buckets().array();
    }

    @Override
    @SneakyThrows
    public void close() {
        elasticsearchClient.close();
    }
}
