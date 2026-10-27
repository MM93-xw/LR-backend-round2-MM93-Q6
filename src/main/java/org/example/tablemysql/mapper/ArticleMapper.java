package org.example.tablemysql.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.tablemysql.entity.Article;
import java.util.List;

@Mapper
// 声明一个接口：
public interface ArticleMapper {
    // 声明一个抽象方法，插入一条数据
    int insert(Article article);

    // 查询所有数据（分页由插件拦截处理，这里不写 limit）
    List<Article> selectPage();

    // 根据标题查询数量（用于 Service 层查重）
    int countByTitle(@Param("title") String title);
}