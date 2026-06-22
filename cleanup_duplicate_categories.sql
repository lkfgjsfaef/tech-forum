-- ============================================================
-- 清理category表中的重复数据
-- 创建时间: 2026-04-10
-- 说明: 删除重复的分类，只保留最新的一批（ID 39-57）
-- ============================================================

SET NAMES utf8mb4;

-- 1. 先查看当前的重复情况
SELECT 
    category_name,
    COUNT(*) as repeat_count,
    GROUP_CONCAT(id ORDER BY id) as ids
FROM category 
WHERE deleted = 0
GROUP BY category_name
HAVING COUNT(*) > 1
ORDER BY repeat_count DESC;

-- 2. 备份：先查看要删除的数据
-- （保留ID >= 39 的记录，删除ID < 39的旧记录）
SELECT id, category_name, create_time 
FROM category 
WHERE deleted = 0 
  AND id < 39
ORDER BY id;

-- 3. 执行删除操作（软删除旧的重复记录）
UPDATE category 
SET deleted = 1,
    update_time = NOW()
WHERE deleted = 0 
  AND id < 39;

-- 4. 验证清理结果
SELECT 
    id,
    category_name,
    status,
    `rank`,
    create_time
FROM category 
WHERE deleted = 0 
ORDER BY `rank` ASC, id ASC;

-- 5. 统计剩余的有效分类数量
SELECT COUNT(*) as total_categories FROM category WHERE deleted = 0;

-- 6. 如果有文章引用了被删除的分类ID，需要更新它们
-- 查找是否有文章使用了被删除的分类ID
SELECT 
    a.id as article_id,
    a.title,
    a.category_id,
    c.category_name
FROM article a
LEFT JOIN category c ON a.category_id = c.id AND c.deleted = 0
WHERE a.category_id IN (SELECT id FROM category WHERE deleted = 1)
  AND a.deleted = 0;

-- 7. 如果第6步有结果，执行以下更新（将文章分类更新到新的分类ID）
-- 根据category_name匹配新的分类ID
UPDATE article a
INNER JOIN (
    SELECT old.id as old_id, new.id as new_id
    FROM category old
    INNER JOIN category new ON old.category_name = new.category_name 
        AND old.deleted = 1 AND new.deleted = 0
) mapping ON a.category_id = mapping.old_id
SET a.category_id = mapping.new_id,
    a.update_time = NOW()
WHERE a.deleted = 0;

-- 8. 最终验证
SELECT '=== 清理完成 ===' as status;
SELECT CONCAT('有效分类数: ', COUNT(*)) as info FROM category WHERE deleted = 0;
SELECT CONCAT('待删除分类数: ', COUNT(*)) as info FROM category WHERE deleted = 1;
