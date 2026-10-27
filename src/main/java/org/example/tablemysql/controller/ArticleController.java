package org.example.tablemysql.controller;

import com.github.pagehelper.PageInfo;
import org.example.tablemysql.dto.ArticleCreateRequest;
import org.example.tablemysql.entity.Article;
import org.example.tablemysql.service.ArticleService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/article")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    // POST 请求：保存数据
    @PostMapping
    public Map<String, Object> create(@RequestBody ArticleCreateRequest request) {
        articleService.create(request);
        return Map.of("code", 200, "message", "保存成功");
    }

    // GET 请求：分页查询数据
    @GetMapping
    public Map<String, Object> page(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {

        PageInfo<Article> pageInfo = articleService.page(page, size);

        // 把查询结果组装成前端想要的格式返回
        return Map.of(
                "code", 200,
                "data", pageInfo.getList(),       // 当前页的数据
                "total", pageInfo.getTotal(),     // 数据库里一共有多少条
                "pages", pageInfo.getPages()      // 一共分了多少页
        );
    }
}