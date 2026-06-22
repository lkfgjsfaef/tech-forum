import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FixCode {
    public static void main(String[] args) throws IOException {
        String basePath = "d:\\桌面\\spring boot\\paicoding";
        try (Stream<Path> paths = Files.walk(Paths.get(basePath))) {
            paths.filter(Files::isRegularFile)
                 .filter(p -> p.toString().endsWith(".java"))
                 .forEach(FixCode::fixFile);
        }
        System.out.println("Java 文件修复完成！");
    }

    private static void fixFile(Path path) {
        try {
            byte[] bytes = Files.readAllBytes(path);
            String content;
            if (bytes.length >= 3 && bytes[0] == (byte) 0xEF && bytes[1] == (byte) 0xBB && bytes[2] == (byte) 0xBF) {
                content = new String(bytes, 3, bytes.length - 3, StandardCharsets.UTF_8);
            } else {
                content = new String(bytes, StandardCharsets.UTF_8);
            }

            String original = content;

            // ZhipuAuthUtil
            content = content.replace("无效的智册\"API Key 格式", "无效的智谱 API Key 格式");

            // DbInitializer
            content = content.replace("检查或更新 user 册\"role 字段失败 (可能已存在\": {}", "检查或更新 user 的 role 字段失败 (可能已存在): {}");
            content = content.replace("检查或更新 user 的 role 字段失败 (可能已存在): {}", "检查或更新 user 的 role 字段失败 (可能已存在): {}");
            content = content.replace("成功册\"user 表添册\"role 字段", "成功为 user 表添加 role 字段");

            // MemoryServiceImpl
            content = content.replace("你是一个智能AI助手，请根据上下文和用户的问题给出准确、有用的回答。", "你是一个智能AI助手，请根据上下文和用户的问题给出准确、有用的回答。");
            content = content.replace("执行RAG混合检册\" query: {}", "执行RAG混合检索: query: {}");
            content = content.replace("降级：截取前 3 册\"", "降级：截取前 3 条");
            content = content.replace("RAG混合检索命册\"{} 个相关片册\" 重排后保册\"{} 册\"", "RAG混合检索命中 {} 个相关片段, 重排后保留 {} 个");
            content = content.replace("假设大模型的最大上下文 Token 限制册\"8000（预册\"1000 给生成的回答，所以输入限册\"7000册\"", "假设大模型的最大上下文 Token 限制为 8000（预留 1000 给生成的回答，所以输入限制 7000）");
            content = content.replace("触发自动摘要: sessionId={}, 消息册\"{}, 总长册\"{}", "触发自动摘要: sessionId={}, 消息数={}, 总长度={}");
            content = content.replace("你是一个对话摘要助手，请精简准确地总结对话内容。", "你是一个对话摘要助手，请精简准确地总结对话内容。");
            content = content.replace("摘要生成完成，长册\" {}", "摘要生成完成，长度: {}");

            // TokenServiceImpl
            content = content.replace("使用 CL100K_BASE 编码，适用册\"GPT-4, GPT-3.5, GLM-4 等主流大模型", "使用 CL100K_BASE 编码，适用于 GPT-4, GPT-3.5, GLM-4 等主流大模型");
            content = content.replace("Token 计算失败, 降级使用估算册\" {}", "Token 计算失败, 降级使用估算: {}");
            content = content.replace("计算总Token数，由于 List<Map> 中有的可能是字符串，有的册\"Object，我们需要详细累册\"", "计算总Token数，由于 List<Map> 中有的可能是字符串，有的是 Object，我们需要详细累加");
            content = content.replace("当前上下册\"Token 册\"[{}] 超过最大限册\"[{}]", "当前上下文 Token 数 [{}] 超过最大限制 [{}]");
            content = content.replace("计算必须保留的部册\"(system + tool) 册\"token", "计算必须保留的部分 (system + tool) 的 token");
            content = content.replace("如果册\"system prompt 都超了", "如果连 system prompt 都超了");
            content = content.replace("System/Tool 消息已经超过最大限册\" 无法保留历史对话!", "System/Tool 消息已经超过最大限制, 无法保留历史对话!");
            content = content.replace("从最新的历史消息往前推，直到达到可册\"token 上限", "从最新的历史消息往前推，直到达到可用 token 上限");
            content = content.replace("插入到头部保持顺册\"", "插入到头部保持顺序");
            content = content.replace("这里为了保证语义完整性，选择直接丢弃更早的消册\"", "这里为了保证语义完整性，选择直接丢弃更早的消息");
            content = content.replace("截断完成, 保留册\"{} 条消册\" 预计 Token 册\" {}", "截断完成, 保留了 {} 条消息, 预计 Token 数: {}");

            // ZhipuRerankServiceImpl
            content = content.replace("如果没有配置 ApiKey 册\"文档为空，直接返回原有的截断列表", "如果没有配置 ApiKey 或文档为空，直接返回原有的截断列表");
            content = content.replace("智谱官方唯一的重排模型标册\"", "智谱官方唯一的重排模型标识");
            content = content.replace("防止 top_n 大于实际文档数导册\"400 错误", "防止 top_n 大于实际文档数导致 400 错误");
            content = content.replace("Rerank 完成, 册\"{} 篇文档中重排提取册\"{} 册\"", "Rerank 完成, 从 {} 篇文档中重排提取了 {} 篇\"");
            content = content.replace("降级：如册\"Rerank 失败，直接返回原始列表的册\"Top K", "降级：如果 Rerank 失败，直接返回原始列表的前 Top K");

            // AbstractOpenAiCompatibleModel
            content = content.replace("开始预热模册\"API 连接: {}", "开始预热模型 API 连接: {}");
            content = content.replace("工具调用次数超过最大限册\"(5册\"", "工具调用次数超过最大限制 (5次)");
            content = content.replace("AI请求册\" {}", "AI请求: {}");
            content = content.replace("处理普通文册\"", "处理普通文本");
            content = content.replace("检测到 Tool Calls，开始执行本地工册\"..", "检测到 Tool Calls，开始执行本地工具...");
            content = content.replace("执行工具并构册\"tool 消息", "执行工具并构造 tool 消息");
            content = content.replace("继续使用册\"Map 或尝试执行，或者在 result 中返回解析错误让大模型重册\"", "继续使用空 Map 或尝试执行，或者在 result 中返回解析错误让大模型重试");
            content = content.replace("未找到工册\" {}", "未找到工具: {}");
            content = content.replace("递归调用大模型获取最终回答，并将流数据转发到当前册\"sink", "递归调用大模型获取最终回答，并将流数据转发到当前 sink");
            content = content.replace("工具执行完成，将结果发送给大模型继续生册\"..", "工具执行完成，将结果发送给大模型继续生成...");
            content = content.replace("SSE连接已释册\"", "SSE连接已释放");

            // UserServiceImpl
            content = content.replace("throw new RuntimeException(\"用户名已存在):);", "throw new RuntimeException(\"用户名已存在\");");
            content = content.replace("throw new RuntimeException(\"新密码长度不能少册\"册\");", "throw new RuntimeException(\"新密码长度不能少于6位\");");

            // UserController
            content = content.replace("获取当前用户信册\"", "获取当前用户信息");
            content = content.replace("更新用户信册\"", "更新用户信息");

            // TimeTool
            content = content.replace("获取当前时间失册\"{}", "获取当前时间失败: {}");
            content = content.replace("获取当前时间失册\"", "获取当前时间失败\"");

            // ToolRegistry
            content = content.replace("已注册工具册\"{}", "已注册工具: {}");
            content = content.replace("已注\\w+工具册\"{}", "已注册工具: {}");

            // WeatherTool
            content = content.replace("未找到该城市的天气信册\"", "未找到该城市的天气信息");
            content = content.replace("天气查询失册\"{}", "天气查询失败: {}");
            content = content.replace("查询天气异册\"{}", "查询天气异常: {}");

            // ArticlePublishEventListener
            content = content.replace("开始处理文章发布事册\"{}", "开始处理文章发布事件: {}");
            content = content.replace("文章发布后处理完册\"{}", "文章发布后处理完成: {}");

            // AsyncNotificationService
            content = content.replace("异步发送通知成册\"{}", "异步发送通知成功: {}");
            content = content.replace("发送邮件通册\"{}", "发送邮件通知: {}");
            content = content.replace("发送站内信通册\"{}", "发送站内信通知: {}");
            content = content.replace("发送系统消册\"{}", "发送系统消息: {}");

            if (!content.equals(original)) {
                Files.write(path, content.getBytes(StandardCharsets.UTF_8));
                System.out.println("已修复: " + path.getFileName());
            }
        } catch (Exception e) {
            System.err.println("处理失败: " + path.getFileName() + " -> " + e.getMessage());
        }
    }
}