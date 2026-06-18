package liyue.myblog.service.impl;

import liyue.myblog.dto.*;
import liyue.myblog.entity.Article;
import liyue.myblog.mapper.ArticleMapper;
import liyue.myblog.service.ArticleService;
import liyue.myblog.vo.ArticleVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Base64Util;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    ArticleMapper articleMapper;


    /**
     * 获取信息，根据articleId
     *
     * @param articleId
     * @return
     */
    @Override
    public ArticleDTO getByArticleId(String articleId) {

        //根据articleId查询文章
        ArticleDTO articleDTO = articleMapper.getByArticleId(articleId);


        return articleDTO;
    }

    /**
     * 插入文章，添加文章，返回文章ID
     */
    public int insertArticle(ArticleVO articleVO) {

        ArticleDTO articleDTO = new ArticleDTO();

        BeanUtils.copyProperties(articleVO, articleDTO);

        articleDTO.setFavorites(0);
        articleDTO.setLikes(0);
        articleDTO.setCreateTime(LocalDateTime.now());
        articleDTO.setChangeTime(LocalDateTime.now());

        log.info("articleDTO:-------{}", articleDTO);

        articleMapper.insertArticle(articleDTO);

        return articleDTO.getId();
    }

    /**
     * 分页查询，获取文章列表，根据用户id
     *
     * @param authorId
     * @return
     */
    public PageDTO getPageByArticleId(Integer authorId, Integer currentPage) {

        if (currentPage == null || currentPage < 1) currentPage = 1;

        int start = (currentPage - 1) * 10;
        int end = 10;

        //查询的列表
        List<ArticlePageDTO> articlePageDTOList = articleMapper.getPageByauthorId(authorId, start, end);

        //接受查询的数据
        PageDTO pageDTO = new PageDTO();

        //查询的列表
        pageDTO.setArticlePageDTOList(articlePageDTOList);
//        log.info("articlePageDTOList:{}",articlePageDTOList);

        //页面显示个数
        pageDTO.setPageSize(10);

        //当前页面
        pageDTO.setCurrentPage(currentPage);

        //总数量  整个列表的总数量
        int pageTotal = articleMapper.getTotalByauthorId(authorId);

        pageDTO.setTotal(pageTotal);
//        log.info("articlePageDTOList.size():{}",articlePageDTOList.size());

        return pageDTO;
    }

    /**
     * 分页查询，获取所有文章列表
     *
     * @param articleSign
     * @param currentPage
     * @param order
     * @return
     */
    public PageDTO getAllPage(Integer articleSign, Integer currentPage, Integer order) {

        if (articleSign == null) articleSign = 4;
        if (currentPage == null || currentPage < 1) currentPage = 1;
        if (order == null) order = 0;

        int start = (currentPage - 1) * 10;
        int num = 10;


        String sign;
        switch (articleSign) {
            case 0:
                sign = "日常";
                break;
            case 1:
                sign = "技术";
                break;
            case 2:
                sign = "杂谈";
                break;
            case 3:
                sign = "其他";
                break;
            case 4:
                sign = "全部";
                break;
            default:
                sign = "日常";
        }

        String orderC;
        if (order == 0) {
            orderC = "likes";
        } else {
            orderC = "favorites";
        }

        PageDTO pageDTO = new PageDTO();

        if(articleSign==4){
            List<ArticlePageDTO> articlePageDTOList = articleMapper.getPageByAllOrder(start, num, orderC);
            pageDTO.setArticlePageDTOList(articlePageDTOList);

        }
        else {
            List<ArticlePageDTO> articlePageDTOList = articleMapper.getPageBySign(sign, start, num, orderC);
            pageDTO.setArticlePageDTOList(articlePageDTOList);

        }

        int total;
        if(articleSign==4){
            total = articleMapper.getTotalAll();
        } else {
            total = articleMapper.getTotalBySign(sign);
        }

        pageDTO.setTotal(total);

        //获取当前页面
        pageDTO.setCurrentPage(currentPage);

        //获取每页显示数量
        pageDTO.setPageSize(10);

        log.info("pageDTO:{}", pageDTO);


        return pageDTO;
    }

    /**
     * 获取文章信息
     *
     * @param articleId
     * @return
     */
    public ArticleDTO getArticleByArticleId(Integer articleId) {

        // 获取  :  标题  标签  作者  文章内容  点赞数  收藏数  创建时间
        ArticleDTO articleDTO = articleMapper.getArticle(articleId);

        log.info("articleDTO:{}", articleDTO);

        return articleDTO;
    }

    /**
     * 获取 收藏夹 内的文章信息
     * @param authorId
     * @param currentPage
     * @return
     */
    public PageDTO getFavoritesByAuthorId(Integer authorId, Integer currentPage) {

        if (currentPage == null || currentPage < 1) currentPage = 1;

        int start = (currentPage - 1) * 10;
        int num = 10;

        List<ArticlePageDTO> articleDTOS = articleMapper.getArticleFavorites(authorId, start, num);

        log.info("getFavoritesByAuthorId------articleDTOS:{}   ",articleDTOS);

        int total = articleMapper.getTotalInAL(authorId);

        PageDTO pageDTO=new PageDTO();

        pageDTO.setArticlePageDTOList(articleDTOS);

        pageDTO.setTotal(total);

        pageDTO.setCurrentPage(currentPage);

        pageDTO.setPageSize(10);


        return pageDTO;
    }

    /**
     * 获取 点赞的 文章信息
     * @param authorId
     * @param currentPage
     * @return
     */
    public PageDTO getLikesByAuthorId(Integer authorId, Integer currentPage) {

        if (currentPage == null || currentPage < 1) currentPage = 1;

        int start = (currentPage - 1) * 10;
        int num = 10;

        List<ArticlePageDTO> articleDTOS = articleMapper.getArticleLikes(authorId, start, num);
        log.info("getLikesByAuthorId---------articleDTOS:{}   ",articleDTOS);

        int total = articleMapper.getTotalInAF(authorId);
        log.info("getLikesByAuthorId---------total:{}   ",total);

        PageDTO pageDTO=new PageDTO();

        pageDTO.setArticlePageDTOList(articleDTOS);

        pageDTO.setTotal(total);

        pageDTO.setCurrentPage(currentPage);

        pageDTO.setPageSize(10);

        return pageDTO;
    }

    /**
     * 点赞，将文章id加入到点赞表当中
     * @param articleId
     * @param authorId
     * @param authorName
     */
    public int insertArticleLikes(Integer articleId, Integer authorId, String authorName){

        //查看点赞表当中是否已经进行点赞
        List<ArticleForeign> isLike=articleMapper.getIsLike(articleId,authorId);

        log.info("isLike:{}",isLike);

        try {
            //如果列表没有，则添加到点赞列表
            if (isLike.isEmpty()){

                ArticleForeign articleLike=new ArticleForeign();

                //封装
                articleLike.setArticleId(articleId);
                articleLike.setAuthorId(authorId);
                articleLike.setAuthorName(authorName);
                articleLike.setCreateTime(LocalDateTime.now());
                articleLike.setChangeTime(LocalDateTime.now());

                //添加到 点赞表 当中
                articleMapper.insertArticleLike(articleLike);

                //将对应文章的 点赞数 加一
                articleMapper.addArticleLikes(articleId);

                //查询点赞数量
                int likes=articleMapper.getTotalLikesByArticleId(articleId);

                log.info("点赞成功：{}");

                return  likes;

            }
            //如果列表有，则不添加到点赞列表
            else {
                log.info("点赞失败：{}");

                return  -1;

            }

        }catch (Exception e){

            log.info("点赞失败：{}--------",e);

            return 0;
        }

    }

    /**
     * 取消点赞
     * @param articleId
     * @param authorId
     * @param authorName
     * @return
     */
    public int deleteArticleLikes(Integer articleId, Integer authorId, String authorName){

        try {

            //查看点赞表当中是否已经进行点赞
            List<ArticleForeign> isLike=articleMapper.getIsLike(articleId,authorId);

            log.info("isLike:{}",isLike);

            //已经点赞过了
            if(!isLike.isEmpty()){
                articleMapper.deleteArticleLike(articleId,authorId);
                articleMapper.subArticleLikes(articleId);
                int likse=articleMapper.getTotalLikesByArticleId(articleId);
                log.info("取消点赞成功");
                return  likse;
            }
            else {
                log.info("取消点赞失败：未点赞过");
                return  -1;
            }

        }catch (Exception e){


            log.info("取消点赞失败：{}",e);

            return 0;
        }

    }

    /**
     * 收藏，将文章id加入到 收藏夹 当中
     * @param articleId
     * @param authorId
     * @param authorName
     * @return
     */
    public int insertArticleFavorites(Integer articleId, Integer authorId, String authorName) {
        //查看点赞表当中是否已经进行 收藏
        List<ArticleForeign> isFavorites=articleMapper.getIsFavorites(articleId,authorId);

        log.info("isLike:{}",isFavorites);

        try {
            //如果列表没有，则添加到点赞列表
            if (isFavorites.isEmpty()){

                ArticleForeign articleFavorites=new ArticleForeign();

                //封装
                articleFavorites.setArticleId(articleId);
                articleFavorites.setAuthorId(authorId);
                articleFavorites.setAuthorName(authorName);
                articleFavorites.setCreateTime(LocalDateTime.now());
                articleFavorites.setChangeTime(LocalDateTime.now());

                //添加到 点赞表 当中
                articleMapper.insertArticleFavorites(articleFavorites);

                //将对应文章的 点赞数 加一
                //查询对应文章 收藏数
                articleMapper.addArticleFavorites(articleId);

                //查询点赞数量
                int favorites=articleMapper.getTotalFavoritesByArticleId(articleId);

                log.info("收藏成功：{}");

                return  favorites;

            }
            //如果列表有，则不添加到点赞列表
            else {
                //查询点赞数量
                int favorites=articleMapper.getTotalFavoritesByArticleId(articleId);


                log.info("收藏失败：{}");

                return  -1;

            }

        }catch (Exception e){

            log.info("收藏失败：{}--------",e);

            return 0;
        }
    }

    /**
     * 取消收藏，从 收藏表当 中 删除该文章
     * @param articleId
     * @param authorId
     * @param authorName
     * @return
     */
    public int deleteArticleFavorites(Integer articleId, Integer authorId, String authorName) {

        try {

            //查看点赞表当中是否已经进行点赞
            List<ArticleForeign> isFavorites=articleMapper.getIsFavorites(articleId,authorId);

            log.info("isLike:{}",isFavorites);

            if(!isFavorites.isEmpty()){
                articleMapper.deleteArticleFavorites(articleId,authorId);
                articleMapper.subArticleFavorites(articleId);
                int favorites=articleMapper.getTotalFavoritesByArticleId(articleId);
                log.info("取消收藏成功");
                return  favorites;
            }
            else {
                log.info("取消收藏失败：未收藏过");
                return  -1;
            }

        }catch (Exception e){


            log.info("取消点赞失败：{}",e);

            return 0;
        }
    }

    /**
     * 根据文章id，获取文章评论
     * @param articleId
     * @return
     */
    @Override
    public List<ArticleCommentDTO> getArticleComment(Integer articleId) {
        List<ArticleCommentDTO> articleCommentDTOList = articleMapper.getArticleComments(articleId);

        System.out.println(articleCommentDTOList);

        return articleCommentDTOList;

    }

    @Override
    public PageDTO searchArticle(String keyword, Integer currentPage) {
        int start = (currentPage - 1) * 10;
        int pageSize = 10;

        List<ArticlePageDTO> articlePageDTOList = articleMapper.searchByKeyword(keyword, start, pageSize);
        int total = articleMapper.getSearchTotal(keyword);

        PageDTO pageDTO = new PageDTO();
        pageDTO.setArticlePageDTOList(articlePageDTOList);
        pageDTO.setTotal(total);
        pageDTO.setCurrentPage(currentPage);
        pageDTO.setPageSize(pageSize);

        return pageDTO;
    }

    @Override
    public void updateArticle(ArticleVO articleVO) {
        ArticleDTO articleDTO = new ArticleDTO();
        BeanUtils.copyProperties(articleVO, articleDTO);
        articleDTO.setChangeTime(LocalDateTime.now());
        log.info("更新文章: {}", articleDTO);
        articleMapper.updateArticle(articleDTO);
    }

    @Override
    public void deleteArticle(Integer id, Integer authorId) {
        log.info("删除文章, id: {}, authorId: {}", id, authorId);
        articleMapper.deleteArticle(id, authorId);
    }

    @Override
    public List<ArticlePageDTO> getAllByAuthorId(Integer authorId) {
        List<ArticlePageDTO> list = articleMapper.getAllByAuthorId(authorId);
        log.info("获取作者全部文章, authorId: {}, count: {}", authorId, list.size());
        return list;
    }

    @Override
    public KnowledgeGraphDTO getKnowledgeGraph(Integer authorId) {
        List<ArticlePageDTO> articles = articleMapper.getTitlesByAuthorId(authorId);
        List<KnowledgeGraphDTO.GraphNode> nodes = new ArrayList<>();
        List<KnowledgeGraphDTO.GraphEdge> edges = new ArrayList<>();
        Set<String> addedTags = new HashSet<>();
        Map<String, List<Integer>> kwArticleMap = new HashMap<>();

        for (ArticlePageDTO article : articles) {
            String articleId = "article_" + article.getId();
            nodes.add(new KnowledgeGraphDTO.GraphNode(articleId, article.getArticleTitle(), 0, 1));

            String tagId = "tag_" + article.getArticleSign();
            if (addedTags.add(tagId)) {
                nodes.add(new KnowledgeGraphDTO.GraphNode(tagId, article.getArticleSign(), 1, 5));
            }
            edges.add(new KnowledgeGraphDTO.GraphEdge(articleId, tagId, 1));

            List<String> keywords = extractKeywords(article.getArticleTitle());
            for (String kw : keywords) {
                kwArticleMap.computeIfAbsent(kw, k -> new ArrayList<>()).add(Integer.valueOf(article.getId()));
            }
        }

        for (Map.Entry<String, List<Integer>> entry : kwArticleMap.entrySet()) {
            List<Integer> articleIds = entry.getValue();
            if (articleIds.size() >= 2) {
                for (int i = 0; i < articleIds.size(); i++) {
                    for (int j = i + 1; j < articleIds.size(); j++) {
                        edges.add(new KnowledgeGraphDTO.GraphEdge(
                                "article_" + articleIds.get(i),
                                "article_" + articleIds.get(j),
                                1));
                    }
                }
            }
        }

        KnowledgeGraphDTO dto = new KnowledgeGraphDTO();
        dto.setNodes(nodes);
        dto.setEdges(edges);
        log.info("知识图谱生成完成, authorId: {}, 节点数: {}, 边数: {}", authorId, nodes.size(), edges.size());
        return dto;
    }

    private List<String> extractKeywords(String title) {
        List<String> keywords = new ArrayList<>();
        if (title == null || title.isEmpty()) return keywords;

        Set<String> stopWords = Set.of("的", "是", "在", "和", "了", "与", "及", "或",
                "一个", "一种", "都是", "就是", "这个", "那个", "可以", "没有",
                "使用", "进行", "通过", "基于", "利用", "实现", "设计", "开发",
                "笔记", "文章", "测试", "学习", "记录", "总结");

        String clean = title.replaceAll("[\\p{P}\\p{S}0-9a-zA-Z\\s]", "");
        for (int len = 2; len <= 4; len++) {
            for (int i = 0; i <= clean.length() - len; i++) {
                String word = clean.substring(i, i + len);
                if (!stopWords.contains(word)) {
                    keywords.add(word);
                }
            }
        }

        String[] engParts = title.split("[^a-zA-Z0-9]+");
        for (String part : engParts) {
            if (part.length() >= 2) {
                keywords.add(part);
            }
        }

        return new ArrayList<>(new HashSet<>(keywords));
    }

    @Override
    public DashboardDTO getDashboard(Integer authorId) {
        DashboardDTO dto = new DashboardDTO();
        dto.setTotalArticles(articleMapper.countByAuthorId(authorId));
        dto.setTotalWords(articleMapper.sumWordsByAuthorId(authorId));
        dto.setTotalLikes(articleMapper.sumLikesByAuthorId(authorId));
        dto.setTotalFavorites(articleMapper.sumFavoritesByAuthorId(authorId));
        dto.setCategoryDistribution(articleMapper.getCategoryDistribution(authorId));
        dto.setDailyTrend(articleMapper.getDailyTrend(authorId));
        dto.setTopArticles(articleMapper.getTopArticles(authorId));
        log.info("仪表盘数据生成完成, authorId: {}", authorId);
        return dto;
    }


}
