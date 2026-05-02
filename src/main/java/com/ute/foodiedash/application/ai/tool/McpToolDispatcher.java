package com.ute.foodiedash.application.ai.tool;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class McpToolDispatcher {
    private final Map<String, MCPTool> tools;

    public McpToolDispatcher(List<MCPTool> toolList) {
        this.tools = toolList.stream()
                .collect(Collectors.toMap(MCPTool::getName, t -> t));
    }

    public Object dispatch(
            String toolName,
            Map<String, Object> args
    ) {

        MCPTool tool = tools.get(toolName);

        if (tool == null) {
            throw new RuntimeException(
                    "Tool not found: " + toolName
            );
        }

        return tool.execute(args);
    }
}
