package ru.rsreu.elasticsearch.client.example;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ElasticSearchClientFactory {
    private final static ElasticSearchClientFactory INSTANCE = new ElasticSearchClientFactory();

    public static ElasticSearchClientFactory getInstance() {
        return INSTANCE;
    }

    public ElasticsearchClient openConnection(String serverUrl) {
        RestClient restClient = RestClient.builder(HttpHost.create(serverUrl))
                .build();
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        return new ElasticsearchClient(transport);
    }
}
