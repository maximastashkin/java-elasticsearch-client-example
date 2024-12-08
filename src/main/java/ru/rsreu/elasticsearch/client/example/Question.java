package ru.rsreu.elasticsearch.client.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Question {
    @JsonProperty("question_id")
    private String questionId;

    private String title;

    private String body;

    private Integer views;

    @JsonProperty("posted_date")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate postedDate;

    private String status;

    private List<String> tags;

    private Double relevanceScore;

    @Override
    public String toString() {
        String tagsStringValue = tags != null ? String.join(",", tags) : "null";
        return String.format(
                "| %-36s | %-20s | %-20s | %-10s | %-12s | %-10s | %-20s | %-15s |%n",
                questionId,
                title != null && title.length() > 20 ? title.substring(0, 17) + "..." : title,
                body != null && body.length() > 20 ? body.substring(0, 17) + "..." : body,
                views,
                postedDate,
                status,
                tagsStringValue.length() > 20 ? tagsStringValue.substring(0, 17) + "..." : tagsStringValue,
                relevanceScore
        );
    }
}

