package com.hannah.AI_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PreviousQuestionListResponse<T> {
    @Builder.Default
    private List<T> data = Collections.emptyList();
}
