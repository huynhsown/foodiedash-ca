package com.ute.foodiedash.application.ai.tool;

import java.util.Map;

public interface MCPTool {
    String getName();
    Object execute(Map<String, Object> args);
}
