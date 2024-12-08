package ru.rsreu.elasticsearch.client.example;

import co.elastic.clients.elasticsearch._types.aggregations.RangeBucket;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;

import java.util.List;

public class ApplicationLauncher {
    private static final String SERVER_URL = "http://localhost:9200";

    public static void main(String[] args) {
        try (ElasticSearchDao dao = ElasticSearchDao.getInstance(SERVER_URL)) {
            SearchResponse<Question> searchResponse = dao.getQuestionsByMatchBoolPrefixBody(
                    "python difference list java",
                    2
            );
            List<Question> tableResult = searchResponse.hits().hits().stream().map(Hit::source).toList();
            printQuestionsTables(tableResult);

            List<RangeBucket> relevanceScoreBuckets = dao.getQuestionsRelevanceScoreRangeBuckets();
            printRelevanceScoreRanges(relevanceScoreBuckets);
        }
    }


    private static void printQuestionsTables(List<Question> questions) {
        if (questions == null || questions.isEmpty()) {
            System.out.println("No data to display.");
            return;
        }

        System.out.println("++++++++++ StackOverflow Questions ++++++++++");
        System.out.printf("| %-36s | %-20s | %-20s | %-10s | %-12s | %-10s | %-20s | %-15s |%n",
                "Question ID",
                "Title",
                "Body",
                "Views",
                "PostedDate",
                "Status",
                "Tags",
                "Relevance Score");

        for (Question doc : questions) {
            System.out.println(doc);
        }
    }

    private static void printRelevanceScoreRanges(List<RangeBucket> buckets) {
        System.out.println("++++++++++ Questions relevance scores ranges ++++++++++");
        System.out.printf("| %-10s | %-10s | %-20s |%n", "from", "to", "Questions count");
        for (RangeBucket bucket : buckets) {
            System.out.printf(
                    "| %-10s | %-10s | %-20s |%n",
                    bucket.from() == null ? "-inf" : bucket.from(),
                    bucket.to() == null ? "+inf" : bucket.to(),
                    bucket.docCount()
            );
        }
    }
}
