package org.example.tablemysql.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.example.tablemysql.dto.ArticleCreateRequest;
import org.example.tablemysql.entity.Article;
import org.example.tablemysql.exception.BadRequestException;
import org.example.tablemysql.exception.BusinessException;
import org.example.tablemysql.mapper.ArticleMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ArticleService {

    private final ArticleMapper articleMapper;

    // 构造方法注入 Mapper
    public ArticleService(ArticleMapper articleMapper) {
        this.articleMapper = articleMapper;
    }

    @Transactional //  开启事务
    public void create(ArticleCreateRequest req) {
        // 1. 参数校验（抛出自定义 400 异常）
        if (req.getTitle() == null || req.getTitle().isBlank()) {
            throw new BadRequestException("标题不能为空");
        }
        if (req.getAuthor() == null || req.getAuthor().isBlank()) {
            throw new BadRequestException("作者不能为空");
        }

        // 2. 第一次查重（业务层面快速拦截）
        if (articleMapper.countByTitle(req.getTitle()) > 0) {
            throw new BusinessException("该标题已存在");
        }

        // 3. 组装并插入
        Article article = new Article();
        article.setTitle(req.getTitle());
        article.setAuthor(req.getAuthor());
        article.setContent(req.getContent());

        // 4. 数据库插入（如果并发极高，两人同时越过第2步，数据库的唯一索引会抛出 DuplicateKeyException，由全局异常拦截器接住）
        articleMapper.insert(article);
    }

    // 分页查询逻辑
    public PageInfo<Article> page(int page, int size) {
        if (page < 1 || size < 1) {
            throw new BadRequestException("页码和每页数量必须大于 0"); // 替换为自定义异常
        }

        // 开启分页！这一行必须紧挨着 selectPage 执行
        PageHelper.startPage(page, size);

        // 去数据库查数据
        List<Article> list = articleMapper.selectPage();

        // 把查出来的数据包装成 PageInfo（里面包含了总数、总页数等信息）
        return new PageInfo<>(list);
    }
}