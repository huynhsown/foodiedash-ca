package com.ute.foodiedash.application.ai.port;

import java.util.List;

public interface EmbeddingPort {
    List<Float> embed(String text);
}
