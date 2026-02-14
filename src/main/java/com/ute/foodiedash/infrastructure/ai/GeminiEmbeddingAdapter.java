package com.ute.foodiedash.infrastructure.ai;

import com.ute.foodiedash.application.ai.port.EmbeddingPort;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GeminiEmbeddingAdapter implements EmbeddingPort {

    private final EmbeddingModel embeddingModel;

    @Override
    public List<Float> embed(String text) {
        float[] embeddingResponse = this.embeddingModel.embed(text);
        List<Float> result = new ArrayList<>(embeddingResponse.length);

        for (float value : embeddingResponse) {
            result.add(value);
        }

        return result;
    }
}
