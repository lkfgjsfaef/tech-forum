import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FixCode2 {
    public static void main(String[] args) throws IOException {
        String basePath = "d:\\桌面\\spring boot\\paicoding";
        try (Stream<Path> paths = Files.walk(Paths.get(basePath))) {
            paths.filter(Files::isRegularFile)
                 .filter(p -> p.toString().endsWith(".java") || p.toString().endsWith(".xml"))
                 .forEach(FixCode2::fixFile);
        }
        System.out.println("FixCode2: Java/XML file fix completed.");
    }

    private static void fixFile(Path path) {
        try {
            byte[] bytes = Files.readAllBytes(path);
            String content;
            boolean changed = false;
            if (bytes.length >= 3 && bytes[0] == (byte) 0xEF && bytes[1] == (byte) 0xBB && bytes[2] == (byte) 0xBF) {
                content = new String(bytes, 3, bytes.length - 3, StandardCharsets.UTF_8);
                changed = true;
            } else {
                content = new String(bytes, StandardCharsets.UTF_8);
            }

            String original = content;

            // ChatController, ChatSessionController
            content = content.replace("\"新对册\"", "\"新对话\"");
            content = content.replace("心跳发送失败，连接可能已关册\"", "心跳发送失败，连接可能已关闭\"");
            content = content.replace("Tokens 消册\" Prompt={}, Completion={}, Total={}", "Tokens 消耗: Prompt={}, Completion={}, Total={}");
            content = content.replace("册\"SseEmitter 异常或完成时，主动取消底层大模型 API 的请求，释放连接", "当 SseEmitter 异常或完成时，主动取消底层大模型 API 的请求，释放连接");
            content = content.replace("封装册\"JSON 格式返回，方便前端统一解析", "封装为 JSON 格式返回，方便前端统一解析");
            content = content.replace("发册\"SSE 错误消息失败", "发送 SSE 错误消息失败");
            
            // ZhipuAuthUtil
            content = content.replace("智谱 AI V4 接口 JWT 生成工具册\"", "智谱 AI V4 接口 JWT 生成工具类");
            content = content.replace("Token 有效册\"1 小时", "Token 有效期 1 小时");
            content = content.replace("将智谱的 API Key (格式: id.secret) 转换册\"JWT Token", "将智谱的 API Key (格式: id.secret) 转换为 JWT Token");
            content = content.replace("降级返回册\"key", "降级返回原 key");
            
            // WeatherTool
            content = content.replace("城市名称，例如：北京、上海、广册\"", "城市名称，例如：北京、上海、广州\"");
            content = content.replace("错误：必须提册\"location 参数", "错误：必须提供 location 参数");
            content = content.replace("模拟返回天气数据，实际项目中可以调用第三方天册\"API，如高德地图天气、和风天气等", "模拟返回天气数据，实际项目中可以调用第三方天气 API，如高德地图天气、和风天气等");
            content = content.replace("册\"s】今天天气晴朗，气温 20册\"~ 26℃，微风，空气质量优册\"", "【%s】今天天气晴朗，气温 20℃ ~ 26℃，微风，空气质量优良\"");

            // ToolRegistry
            content = content.replace("已注册\"{} 册\"AI Agent 工具: {}", "已注册 {} 个 AI Agent 工具: {}");
            
            // ToolHandler
            content = content.replace("工具名称 (必须符合大模册\"Function Calling 的命名规范，如英文、数字、下划线)", "工具名称 (必须符合大模型 Function Calling 的命名规范，如英文、数字、下划线)");
            content = content.replace("工具描述 (供大模型理解该工具的用册\"", "工具描述 (供大模型理解该工具的用途)");
            content = content.replace("工具参数册\"JSON Schema 描述", "工具参数的 JSON Schema 描述");
            content = content.replace("@param params 大模型解析出的参册\"", "@param params 大模型解析出的参数");
            
            // TimeTool
            content = content.replace("yyyy年MM月dd册\"HH:mm:ss", "yyyy年MM月dd日 HH:mm:ss");

            // ChatAttachmentServiceImpl
            content = content.replace("覆盖重叠参数，虽然TokenTextSplitter构造器不直接支持overlap，但可以通过覆盖默认方法或调册\"", "覆盖重叠参数，虽然TokenTextSplitter构造器不直接支持overlap，但可以通过覆盖默认方法或调用");
            content = content.replace("注：Spring AI 册\"TokenTextSplitter 第二个参册\"`minChunkSizeChars` 有时也作为启发式的最小保留长册\"", "注：Spring AI 的 TokenTextSplitter 第二个参数 `minChunkSizeChars` 有时也作为启发式的最小保留长度");
            content = content.replace("为了更好适配 RAG，我们可以在这里先保册\"800 册\"400（最小字符数，间接提供上下文连贯性），并注释说明", "为了更好适配 RAG，我们可以在这里先保留 800 和 400（最小字符数，间接提供上下文连贯性），并注释说明");
            content = content.replace("无法解析或向量化该文档内册\"", "无法解析或向量化该文档内容\"");

            // TokenService
            content = content.replace("精确计算给定文本册\"Token 数量", "精确计算给定文本的 Token 数量");
            content = content.replace("基于滑动窗口策略，截断消息上下文使其不超过最册\"Token 限制", "基于滑动窗口策略，截断消息上下文使其不超过最大 Token 限制");
            content = content.replace("保留 system 消息，从最早的 user/assistant 消息开始移册\"", "保留 system 消息，从最早的 user/assistant 消息开始移除");

            // RerankService
            content = content.replace("对检索到的文档片段进行重册\"", "对检索到的文档片段进行重排");
            content = content.replace("@param documents 初筛（召回）得到的文档片段列册\"", "@param documents 初筛（召回）得到的文档片段列表");
            content = content.replace("@return 重排并截取后的文档片段列册\"", "@return 重排并截取后的文档片段列表");

            // UserServiceImpl
            content = content.replace("\"用户未登册\"", "\"用户未登录\"");
            content = content.replace("更新最后登录时间（user表没有last_login_ip列，跳过IP更新册\"", "更新最后登录时间（user表没有last_login_ip列，跳过IP更新）");
            content = content.replace("册\"user_info 表读取昵称作为显示名，没有则用用户名", "从 user_info 表读取昵称作为显示名，没有则用用户名");
            content = content.replace("文章册\"- 统计该作者已发布的文章数册\"", "文章数 - 统计该作者已发布的文章数量");
            content = content.replace("关注册\"", "关注数");
            content = content.replace("粉丝册\"", "粉丝数");
            content = content.replace("收到的点赞数 (别人点赞自己发布的文册\"", "收到的点赞数 (别人点赞自己发布的文章)");
            content = content.replace("评论册\"", "评论数");
            content = content.replace("收藏册\"", "收藏数");
            content = content.replace("更新 user_info 表（昵称、简介等册\"", "更新 user_info 表（昵称、简介等）");
            content = content.replace("册\"user_info 读取昵称作为显示册\"", "从 user_info 读取昵称作为显示名");

            // AsyncNotificationService
            content = content.replace("[异步通知] 用户{}评论了文章{}, 通知作册\"", "[异步通知] 用户{}评论了文章{}, 通知作者\"");

            // ArticlePublishEventListener
            content = content.replace("模拟通知{}的粉丝：发布了新文章「{}册\"", "模拟通知{}的粉丝：发布了新文章「{}」\"");

            // GlobalExceptionHandler
            content = content.replace("参数册\"", "参数「");
            content = content.replace("数据库异常，请稍后重册\"", "数据库异常，请稍后重试\"");
            content = content.replace("上传文件过大，请选择更小的文册\"", "上传文件过大，请选择更小的文件\"");
            content = content.replace("非法状册\"", "非法状态:");

            // LoginRestController
            content = content.replace("用户册\"密码方式的登册\"登出的入册\"", "用户和密码方式的登录/登出的入口");
            content = content.replace("发送验证码（到邮箱，验证码显示在控制台册\"", "发送验证码（到邮箱，验证码显示在控制台）");
            content = content.replace("验证码校验（仅验证验证码是否正确，不登录册\"", "验证码校验（仅验证验证码是否正确，不登录）");
            content = content.replace("验证码错误或已过册\"", "验证码错误或已过期\"");
            content = content.replace("用户名密码注册\"", "用户名密码注册\"");
            content = content.replace("密码长度不能少于6册\"", "密码长度不能少于6位\"");
            content = content.replace("该邮箱已被注册，请使用其他邮册\"", "该邮箱已被注册，请使用其他邮箱\"");

            // SecurityConfig
            content = content.replace("册\"JWT密钥配置正常，长度: {} 册\"", "JWT密钥配置正常，长度: {}");

            // CacheBuilderExample
            content = content.replace("创建一册\"CacheBuilder 对象", "创建一个 CacheBuilder 对象");
            content = content.replace("缓存项在指定时间内没有被访问就过册\"", "缓存项在指定时间内没有被访问就过期");
            content = content.replace("开启统计功册\"", "开启统计功能");
            content = content.replace("构建一册\"LoadingCache 对象", "构建一个 LoadingCache 对象");
            content = content.replace("value册\"", "value:");
            content = content.replace("从缓存中获取册\"", "从缓存中获取:");
            content = content.replace("put 册\"", "put:");

            // ReqRecordFilter
            content = content.replace("1. 请求参数日志输出过滤册\"", "1. 请求参数日志输出过滤");
            content = content.replace("返回给前端的traceId，用于日志追册\"", "返回给前端的traceId，用于日志追踪");
            content = content.replace("一个链路请求完毕，清空MDC相关的变册\"如GlobalTraceId，用户信息\"", "一个链路请求完毕，清空MDC相关的变量如GlobalTraceId，用户信息\"");
            content = content.replace("静态资源直接放册\"", "静态资源直接放行");

            // SeoInjectService
            content = content.replace("seo注入服务，下面加个页面使册\"", "seo注入服务，下面加个页面使用");
            content = content.replace("- 文章详情册\"", "- 文章详情页");
            content = content.replace("- 专栏内容详情册\"", "- 专栏内容详情页");
            content = content.replace("开放内容协册\"OGP", "开放内容协议 OGP");
            content = content.replace("技术派,开源社册\"java,springboot,IT,程序册\"开发册\"mysql,redis,Java基础,多线册\"JVM,虚拟册\"数据册\"MySQL,Spring,Redis,MyBatis,系统设计,分布册\"RPC,高可册\"高并册\"沉默王二", "技术派,开源社区,java,springboot,IT,程序员,开发者,mysql,redis,Java基础,多线程,JVM,虚拟机,数据库,MySQL,Spring,Redis,MyBatis,系统设计,分布式,RPC,高可用,高并发,沉默王二");
            
            // AdminController
            content = content.replace("校验管理员权册\"", "校验管理员权限");
            content = content.replace("分页获取用户列表册\"Token 消耗情册\"", "分页获取用户列表和 Token 消耗情况");
            content = content.replace("默认值\"Token 消耗降册\"", "默认按 Token 消耗降序");
            content = content.replace("获取全局 Token 和用户统计大盘数册\"", "获取全局 Token 和用户统计大盘数据");
            content = content.replace("2. 册\"Token 消册\"(这里简单通过全表求和，实际生产中应使册\"SQL SUM 或单独的统计册\"", "2. 总 Token 消耗 (这里简单通过全表求和，实际生产中应使用 SQL SUM 或单独的统计表)");
            content = content.replace("3. 模型可用状册\"", "3. 模型可用状态");
            content = content.replace("user 表加册\"status 字段", "user 表加入 status 字段");

            if (changed || !content.equals(original)) {
                Files.write(path, content.getBytes(StandardCharsets.UTF_8));
                System.out.println("FixCode2: Fixed -> " + path.getFileName());
            }
        } catch (Exception e) {
            System.err.println("FixCode2: Error -> " + path.getFileName() + " : " + e.getMessage());
        }
    }
}
