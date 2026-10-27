package org.example.tablemysql.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.example.tablemysql.dto.ArticleCreateRequest;
import org.example.tablemysql.entity.Article;
import org.example.tablemysql.mapper.ArticleMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {

    private final ArticleMapper articleMapper;

    // 构造方法注入 Mapper
    public ArticleService(ArticleMapper articleMapper) {
        this.articleMapper = articleMapper;
    }

    // 保存逻辑
    public void create(ArticleCreateRequest req) {
        // 简单校验
        if (req.getTitle() == null || req.getTitle().isBlank()) {
            throw new RuntimeException("标题不能为空");
        }

        Article article = new Article();
        article.setTitle(req.getTitle());
        article.setAuthor(req.getAuthor());
        article.setContent(req.getContent());

        articleMapper.insert(article);
    }

    // 分页查询逻辑
    public PageInfo<Article> page(int page, int size) {
        if (page < 1 || size < 1) {
            throw new RuntimeException("页码和每页数量必须大于 0");
        }

        // 开启分页！这一行必须紧挨着 selectPage 执行
        PageHelper.startPage(page, size);

        // 去数据库查数据
        List<Article> list = articleMapper.selectPage();

        // 把查出来的数据包装成 PageInfo（里面包含了总数、总页数等信息）
        return new PageInfo<>(list);
    }
}