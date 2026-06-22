package com.example.forum.service.agent.service;

import java.util.List;

public interface RerankService {
    
    /**
     * 对检索到的文档片段进行重排     * @param query 用户原始查询问题
     * @param documents 初筛（召回）得到的文档片段列表     * @param topK 需要保留的 Top K 数量
     * @return 重排并截取后的文档片段列表     */
    List<String> rerank(String query, List<String> documents, int topK);
}

