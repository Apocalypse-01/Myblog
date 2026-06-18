package liyue.myblog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import liyue.myblog.dto.ArticleCommentDTO;
import liyue.myblog.dto.ArticleDTO;
import liyue.myblog.dto.ArticleForeign;
import liyue.myblog.dto.ArticlePageDTO;
import liyue.myblog.dto.KnowledgeGraphDTO;
import liyue.myblog.dto.DashboardDTO;
import liyue.myblog.dto.PageDTO;
import liyue.myblog.mapper.ArticleMapper;
import liyue.myblog.result.Result;
import liyue.myblog.service.ArticleService;
import liyue.myblog.vo.ArticleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/article")
@Tag(name = "文章模块")
@Slf4j
public class ArticleController {

    @Autowired
    ArticleMapper articleMapper;

    @Autowired
    ArticleService articleService;

    /**
     * 获取图像信息，根据articleId
     * @param articleVO
     * @return
     */
//    @PostMapping("/getDetails")
//    public Result<ArticleDTO> getArticleDetail(@RequestBody ArticleVO articleVO){
//
//
//        log.info("userVO:{}",articleVO);
//
////        ArticleDTO articleDTO= articleService.getByArticleId(articleVO.getArticleId());
//
//
////        log.info("articleDTO:{}", articleDTO);
//
//        return Result.success(articleDTO);
//    }


    /**
     * 发表文章
     *
     * @return
     */
    @PutMapping("/release")
    @Operation(summary = "发表文章")
    public Result<Integer> releaseArticle(@RequestBody ArticleVO articleVO) {

        int articleId = articleService.insertArticle(articleVO);

        return Result.success(articleId);
    }

    /**
     * 查询 我的文章
     *
     * @param authorId
     * @param currentPage
     * @return
     */

    @GetMapping("/getArticlePage")
    @Operation(summary = "查询我的文章")
    public Result<PageDTO> getArticlePage(
            @RequestParam("authorId") Integer authorId,
            @RequestParam("currentPage") Integer currentPage) {

        log.info("authorId:{}", authorId);
        log.info("currentPage:{}", currentPage);

        PageDTO pageDTO = articleService.getPageByArticleId(authorId, currentPage);

        log.info("pageDTO:{}", pageDTO);

//        List<ArticlePageDTO> articlePageDTOList = articleService.getPageByArticleId(authorId);
//
//        log.info("articlePageDTOList:{}", articlePageDTOList);

        return Result.success(pageDTO);

    }

    /**
     * 获取所有文章列表
     *
     * @return
     */
    @GetMapping("/getAllArticlePage")
    @Operation(summary = "获取所有文章列表")
    public Result<PageDTO> getAllArticlePage(
            @RequestParam("articleSign") Integer articleSign,
            @RequestParam("order") Integer order,
            @RequestParam("currentPage") Integer currentPage
    ) {

        log.info("articleSign:{}", articleSign);
        log.info("order:{}", order);
        log.info("currentPage:{}", currentPage);

        PageDTO pageDTO = articleService.getAllPage(articleSign, currentPage, order);


        return Result.success(pageDTO);
    }

    /**
     * 获取文章信息
     *
     * @param articleId
     * @return
     */
    @GetMapping("/getArticle")
    @Operation(summary = "获取文章信息")
    public Result<ArticleDTO> getArticle(
            @RequestParam("articleId") Integer articleId
    ) {
        log.info("articleId:{}", articleId);

        ArticleDTO articleDTO = articleService.getArticleByArticleId(articleId);


        return Result.success(articleDTO);
    }

    /**
     * 获取 收藏夹 文章列表
     *
     * @param authorId
     * @return
     */
    @GetMapping("/getArticleFavorites")
    @Operation(summary = "获取 收藏夹 文章列表")
    public Result<PageDTO> getArticleFavorites(
            @RequestParam("authorId") Integer authorId,
            @RequestParam("currentPage") Integer currentPage
    ) {
        log.info("authorId:{}", authorId);

        PageDTO articleDTO = articleService.getFavoritesByAuthorId(authorId, currentPage);


        return Result.success(articleDTO);
    }

    /**
     * 获取点赞 列表
     *
     * @param authorId
     * @param currentPage
     * @return
     */
    @GetMapping("/getArticleLikes")
    @Operation(summary = "获取点赞 列表")
    public Result<PageDTO> getArticleLikes(
            @RequestParam("authorId") Integer authorId,
            @RequestParam("currentPage") Integer currentPage
    ) {
        log.info("authorId:{}", authorId);

        PageDTO articleDTO = articleService.getLikesByAuthorId(authorId, currentPage);


        return Result.success(articleDTO);
    }

    /**
     * 点赞
     * @param articleId
     * @param authorId
     * @param authorName
     * @return
     */
    @GetMapping("/articleInsertLikes")
    @Operation(summary = "点赞")
    public Result<Integer> articleLike(
            @RequestParam("articleId") Integer articleId,
            @RequestParam("authorId") Integer authorId,
            @RequestParam("authorName") String authorName
    ) {
        log.info("articleId:{}", articleId);
        log.info("authorId:{}", authorId);
        log.info("authorName:{}", authorName);

        int likes = 0;

        try {
            likes = articleService.insertArticleLikes(articleId, authorId, authorName);

            log.info("点赞成功 likes ：{}", likes);

        } catch (Exception e) {
            log.info("点赞失败：{}", e);
            return Result.error("已经点过赞了");

        }

        if (likes==-1){

            return Result.error("已经点过赞了");
        }

        return Result.success(likes);
    }

    /**
     * 取消点赞
     * @param articleId
     * @param authorId
     * @param authorName
     * @return
     */
    @DeleteMapping("/articleCancelLikes")
    @Operation(summary = "取消点赞")
    public Result<Integer> articleNoLike(
            @RequestParam("articleId") Integer articleId,
            @RequestParam("authorId") Integer authorId,
            @RequestParam("authorName") String authorName
    ) {
        log.info("articleId:{}", articleId);
        log.info("authorId:{}", authorId);
        log.info("authorName:{}", authorName);

        int likes = 0;

        try {
            likes = articleService.deleteArticleLikes(articleId, authorId, authorName);
            log.info("取消点赞成功：{}");

        } catch (Exception e) {

            log.info("取消点赞成功：{}", e);
        }

        return Result.success(likes);
    }

    /**
     * 收藏
     *
     * @return
     */
    @GetMapping("/articleInsertFavorites")
    @Operation(summary = "收藏")
    public Result<Integer> articleFavorites(
            @RequestParam("articleId") Integer articleId,
            @RequestParam("authorId") Integer authorId,
            @RequestParam("authorName") String authorName)
    {
        log.info("articleId:{}", articleId);
        log.info("authorId:{}", authorId);
        log.info("authorName:{}", authorName);

        int favorites = 0;

        try {
            favorites = articleService.insertArticleFavorites(articleId, authorId, authorName);
            log.info("收藏成功：{}");

        } catch (Exception e) {
            log.info("收藏失败：{}", e);
            return Result.error("未知异常！");

        }
        if(favorites==-1){
            return Result.error("已经收藏过了");
        }

        return Result.success(favorites);
    }

    /**
     * 取消收藏
     * @param articleId
     * @param authorId
     * @param authorName
     * @return
     */
    @DeleteMapping("/articleCancelFavorites")
    @Operation(summary = "取消收藏")
    public Result<Integer> articleNoFavorites(
            @RequestParam("articleId") Integer articleId,
            @RequestParam("authorId") Integer authorId,
            @RequestParam("authorName") String authorName)
    {
        log.info("articleId:{}", articleId);
        log.info("authorId:{}", authorId);
        log.info("authorName:{}", authorName);

        int favorites = 0;

        try {
            favorites = articleService.deleteArticleFavorites(articleId, authorId, authorName);
            log.info("取消点赞成功：{}");

        } catch (Exception e) {

            log.info("取消点赞成功：{}", e);
        }

        return Result.success(favorites);


    }
    /**
     * 取消收藏
     * @param articleId
     * @return
     */
    @GetMapping("/search")
    @Operation(summary = "搜索文章")
    public Result<PageDTO> searchArticle(
            @RequestParam("keyword") String keyword,
            @RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage
    ) {
        log.info("keyword:{}", keyword);
        log.info("currentPage:{}", currentPage);

        PageDTO pageDTO = articleService.searchArticle(keyword, currentPage);

        return Result.success(pageDTO);
    }

    @PutMapping("/update")
    @Operation(summary = "编辑文章")
    public Result updateArticle(@RequestBody ArticleVO articleVO) {
        log.info("编辑文章: {}", articleVO);
        articleService.updateArticle(articleVO);
        return Result.success();
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除文章")
    public Result deleteArticle(
            @RequestParam("id") Integer id,
            @RequestParam("authorId") Integer authorId
    ) {
        log.info("删除文章, id: {}, authorId: {}", id, authorId);
        articleService.deleteArticle(id, authorId);
        return Result.success();
    }

    @GetMapping("/isLiked")
    @Operation(summary = "查询当前用户是否已点赞")
    public Result<Boolean> isLiked(
            @RequestParam("articleId") Integer articleId,
            @RequestParam("authorId") Integer authorId
    ) {
        List<ArticleForeign> list = articleMapper.getIsLike(articleId, authorId);
        return Result.success(!list.isEmpty());
    }

    @GetMapping("/isFavorited")
    @Operation(summary = "查询当前用户是否已收藏")
    public Result<Boolean> isFavorited(
            @RequestParam("articleId") Integer articleId,
            @RequestParam("authorId") Integer authorId
    ) {
        List<ArticleForeign> list = articleMapper.getIsFavorites(articleId, authorId);
        return Result.success(!list.isEmpty());
    }

    @GetMapping("/getAllByAuthor")
    @Operation(summary = "获取作者全部文章列表(不分页)")
    public Result<List<ArticlePageDTO>> getAllByAuthor(@RequestParam("authorId") Integer authorId) {
        log.info("获取作者全部文章, authorId: {}", authorId);
        List<ArticlePageDTO> list = articleService.getAllByAuthorId(authorId);
        return Result.success(list);
    }

    @GetMapping("/knowledgeGraph")
    @Operation(summary = "获取作者知识图谱数据")
    public Result<KnowledgeGraphDTO> getKnowledgeGraph(@RequestParam("authorId") Integer authorId) {
        log.info("生成知识图谱, authorId: {}", authorId);
        KnowledgeGraphDTO graph = articleService.getKnowledgeGraph(authorId);
        return Result.success(graph);
    }

    @GetMapping("/dashboard")
    @Operation(summary = "获取写作仪表盘数据")
    public Result<DashboardDTO> getDashboard(@RequestParam("authorId") Integer authorId) {
        log.info("生成仪表盘, authorId: {}", authorId);
        DashboardDTO dashboard = articleService.getDashboard(authorId);
        return Result.success(dashboard);
    }

    @GetMapping("/download")
    @Operation(summary = "下载文章")
    public ResponseEntity<byte[]> downloadArticle(
            @RequestParam("articleId") Integer articleId,
            @RequestParam("format") String format
    ) {
        log.info("下载文章, articleId: {}, format: {}", articleId, format);

        try {
            ArticleDTO article = articleService.getArticleByArticleId(articleId);
            if (article == null) {
                return ResponseEntity.notFound().build();
            }

            String content = article.getArticleContent();
            String title = article.getArticleTitle() != null ? article.getArticleTitle() : "未命名文章";
            byte[] fileContent;
            String contentType;
            String fileExtension;

            switch (format.toLowerCase()) {
                case "markdown":
                case "md":
                    fileContent = generateMarkdownFile(title, content);
                    contentType = "text/markdown; charset=utf-8";
                    fileExtension = ".md";
                    break;
                case "txt":
                case "text":
                    fileContent = generateTxtFile(content);
                    contentType = "text/plain; charset=utf-8";
                    fileExtension = ".txt";
                    break;
                case "epub":
                    fileContent = generateEpubFile(title, content);
                    contentType = "application/epub+zip";
                    fileExtension = ".epub";
                    break;
                default:
                    return ResponseEntity.badRequest().build();
            }

            String filename = title + "_" + java.time.LocalDate.now() + fileExtension;
            String encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8.toString())
                    .replace("+", "%20");

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFilename)
                    .contentType(MediaType.parseMediaType(contentType))
                    .contentLength(fileContent.length)
                    .body(fileContent);

        } catch (Exception e) {
            log.error("下载文章失败", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    private byte[] generateMarkdownFile(String title, String content) {
        StringBuilder sb = new StringBuilder();
        sb.append("# ").append(title).append("\n\n");
        
        if (isHtmlContent(content)) {
            sb.append("> ⚠️ 注意：原始内容为HTML格式，以下为转换后的文本\n\n");
            sb.append(convertHtmlToPlainText(content));
        } else {
            sb.append(content);
        }
        
        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    private byte[] generateTxtFile(String content) {
        String plainText;
        
        if (isHtmlContent(content)) {
            plainText = convertHtmlToPlainText(content);
        } else {
            plainText = convertMarkdownToPlainText(content);
        }
        
        return plainText.getBytes(StandardCharsets.UTF_8);
    }

    private byte[] generateEpubFile(String title, String content) {
        try {
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            java.util.zip.ZipOutputStream zos = new java.util.zip.ZipOutputStream(baos);

            String mimetype = "application/epub+zip";
            zos.putNextEntry(new java.util.zip.ZipEntry("mimetype"));
            zos.write(mimetype.getBytes(StandardCharsets.UTF_8));
            zos.closeEntry();

            StringBuilder contentHtml = new StringBuilder();
            contentHtml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            contentHtml.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">\n");
            contentHtml.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">\n");
            contentHtml.append("<head><title>").append(title).append("</title></head>\n");
            contentHtml.append("<body>\n");
            contentHtml.append("<h1>").append(title).append("</h1>\n");
            
            if (isHtmlContent(content)) {
                contentHtml.append(content);
            } else {
                contentHtml.append(convertMarkdownToHtml(content));
            }
            
            contentHtml.append("\n</body>\n</html>");

            zos.putNextEntry(new java.util.zip.ZipEntry("OEBPS/content.xhtml"));
            zos.write(contentHtml.toString().getBytes(StandardCharsets.UTF_8));
            zos.closeEntry();

            String containerXml = "<?xml version=\"1.0\"?>\n<container version=\"1.0\" xmlns=\"urn:oasis:names:tc:opendocument:xmlns:container\">\n<rootfiles>\n<rootfile full-path=\"OEBPS/content.opf\" media-type=\"application/oebps-package+xml\"/>\n</rootfiles>\n</container>";
            zos.putNextEntry(new java.util.zip.ZipEntry("META-INF/container.xml"));
            zos.write(containerXml.getBytes(StandardCharsets.UTF_8));
            zos.closeEntry();

            String opfContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<package xmlns=\"http://www.idpf.org/2007/opf\" unique-identifier=\"BookId\" version=\"2.0\">\n<metadata xmlns:dc=\"http://purl.org/dc/elements/1.1/\">\n<dc:title>" + escapeXml(title) + "</dc:title>\n<dc:language>zh</dc:language>\n</metadata>\n<manifest>\n<item id=\"content\" href=\"content.xhtml\" media-type=\"application/xhtml+xml\"/>\n</manifest>\n<spine toc=\"ncx\">\n<itemref idref=\"content\"/>\n</spine>\n</package>";
            zos.putNextEntry(new java.util.zip.ZipEntry("OEBPS/content.opf"));
            zos.write(opfContent.getBytes(StandardCharsets.UTF_8));
            zos.closeEntry();

            zos.close();
            return baos.toByteArray();
        } catch (Exception e) {
            log.error("生成EPUB文件失败", e);
            throw new RuntimeException("生成EPUB文件失败: " + e.getMessage());
        }
    }
    
    private boolean isHtmlContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            return false;
        }
        String trimmed = content.trim();
        return trimmed.startsWith("<") && 
               (trimmed.contains("<html") || trimmed.contains("<body") || 
                trimmed.contains("<div") || trimmed.contains("<p>") || 
                trimmed.contains("<h1") || trimmed.contains("<h2"));
    }
    
    private String convertHtmlToPlainText(String html) {
        String text = html;
        text = text.replaceAll("(?i)<br\\s*/?>", "\n");
        text = text.replaceAll("(?i)</p>", "\n\n");
        text = text.replaceAll("(?i)</div>", "\n");
        text = text.replaceAll("(?i)</h[1-6]>", "\n\n");
        text = text.replaceAll("(?i)<li>", "• ");
        text = text.replaceAll("(?i)</li>", "\n");
        text = text.replaceAll("<[^>]+>", "");
        text = text.replaceAll("&nbsp;", " ");
        text = text.replaceAll("&amp;", "&");
        text = text.replaceAll("&lt;", "<");
        text = text.replaceAll("&gt;", ">");
        text = text.replaceAll("&quot;", "\"");
        text = text.replaceAll("&#39;", "'");
        text = text.replaceAll("\\n{3,}", "\n\n");
        return text.trim();
    }
    
    private String convertMarkdownToPlainText(String markdown) {
        if (markdown == null || markdown.isEmpty()) {
            return "";
        }
        
        String text = markdown;
        
        String[] lines = text.split("\n");
        StringBuilder result = new StringBuilder();
        boolean inCodeBlock = false;
        boolean inTable = false;
        
        for (String line : lines) {
            String trimmedLine = line.trim();
            
            if (trimmedLine.startsWith("```")) {
                inCodeBlock = !inCodeBlock;
                continue;
            }
            
            if (inCodeBlock) {
                result.append(line).append("\n");
                continue;
            }
            
            if (trimmedLine.startsWith("|") && trimmedLine.endsWith("|")) {
                if (trimmedLine.matches("^\\|\\s*[-:]+\\s*\\|$")) {
                    continue;
                }
                if (!inTable) {
                    inTable = true;
                }
                String tableContent = trimmedLine.substring(1, trimmedLine.length() - 1);
                String[] cells = tableContent.split("\\s*\\|\\s*");
                StringBuilder rowText = new StringBuilder();
                for (String cell : cells) {
                    cell = cleanMarkdownInline(cell.trim());
                    if (!cell.isEmpty()) {
                        if (rowText.length() > 0) {
                            rowText.append("\t");
                        }
                        rowText.append(cell);
                    }
                }
                if (rowText.length() > 0) {
                    result.append(rowText.toString()).append("\n");
                }
                continue;
            } else {
                if (inTable) {
                    inTable = false;
                    result.append("\n");
                }
            }
            
            if (trimmedLine.matches("^(\\*{3,}|-{3,}|_{3,})\\s*$")) {
                result.append("\n");
                continue;
            }
            
            if (trimmedLine.startsWith(">")) {
                String quoteContent = trimmedLine.replaceAll("^>\\s*", "");
                result.append(cleanMarkdownInline(quoteContent)).append("\n");
                continue;
            }
            
            if (trimmedLine.matches("^(#{1,6})\\s+.+")) {
                String headingText = trimmedLine.replaceAll("^#{1,6}\\s+", "");
                result.append(cleanMarkdownInline(headingText)).append("\n\n");
                continue;
            }
            
            if (trimmedLine.matches("^\\s*[-*+]\\s+.+")) {
                String listItem = trimmedLine.replaceAll("^\\s*[-*+]\\s+", "• ");
                result.append(cleanMarkdownInline(listItem)).append("\n");
                continue;
            }
            
            if (trimmedLine.matches("^\\s*\\d+\\.\\s+.+")) {
                String listItem = trimmedLine.replaceAll("^\\s*\\d+\\.\\s+", "");
                result.append(cleanMarkdownInline(listItem)).append("\n");
                continue;
            }
            
            result.append(cleanMarkdownInline(line)).append("\n");
        }
        
        String finalText = result.toString();
        finalText = finalText.replaceAll("\\n{3,}", "\n\n");
        return finalText.trim();
    }
    
    private String cleanMarkdownInline(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        
        String cleaned = text;
        
        cleaned = cleaned.replaceAll("~~(.+?)~~", "$1");
        
        cleaned = cleaned.replaceAll("\\*\\*(.+?)\\*\\*", "$1");
        
        cleaned = cleaned.replaceAll("\\*(.+?)\\*", "$1");
        
        cleaned = cleaned.replaceAll("_(.+?)_", "$1");
        
        cleaned = cleaned.replaceAll("`([^`]+)`", "$1");
        
        cleaned = cleaned.replaceAll("!\\[([^]]*)\\]\\([^)]+\\)", "$1");
        
        cleaned = cleaned.replaceAll("\\[([^]]+)\\]\\([^)]+\\)", "$1");
        
        cleaned = cleaned.replaceAll("\\[([^]]+)\\]\\[]", "$1");
        
        return cleaned;
    }
    
    private String escapeXml(String input) {
        if (input == null) return "";
        return input.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#39;");
    }

    private String convertMarkdownToHtml(String markdown) {
        if (markdown == null || markdown.isEmpty()) {
            return "";
        }
        
        String[] lines = markdown.split("\n");
        StringBuilder html = new StringBuilder();
        boolean inCodeBlock = false;
        boolean inTable = false;
        boolean inList = false;
        String listType = "";
        
        for (String line : lines) {
            String trimmedLine = line.trim();
            
            if (trimmedLine.startsWith("```")) {
                if (inCodeBlock) {
                    html.append("</code></pre>\n");
                    inCodeBlock = false;
                } else {
                    if (inList) {
                        html.append("</").append(listType).append(">\n");
                        inList = false;
                    }
                    html.append("<pre><code>");
                    inCodeBlock = true;
                }
                continue;
            }
            
            if (inCodeBlock) {
                html.append(escapeXml(line)).append("\n");
                continue;
            }
            
            if (trimmedLine.startsWith("|") && trimmedLine.endsWith("|")) {
                if (!inTable) {
                    if (inList) {
                        html.append("</").append(listType).append(">\n");
                        inList = false;
                    }
                    html.append("<table>\n");
                    inTable = true;
                }
                
                if (trimmedLine.matches("^\\|\\s*[-:]+\\s*\\|$")) {
                    continue;
                }
                
                String tableContent = trimmedLine.substring(1, trimmedLine.length() - 1);
                String[] cells = tableContent.split("\\s*\\|\\s*");
                
                html.append("<tr>");
                for (String cell : cells) {
                    cell = cell.trim();
                    String cleanedCell = cleanMarkdownInlineForHtml(cell);
                    html.append("<td>").append(cleanedCell).append("</td>");
                }
                html.append("</tr>\n");
                continue;
            } else {
                if (inTable) {
                    html.append("</table>\n");
                    inTable = false;
                }
            }
            
            if (trimmedLine.matches("^(\\*{3,}|-{3,}|_{3,})\\s*$")) {
                html.append("<hr/>\n");
                continue;
            }
            
            if (trimmedLine.startsWith(">")) {
                if (inList) {
                    html.append("</").append(listType).append(">\n");
                    inList = false;
                }
                String quoteContent = trimmedLine.replaceAll("^>\\s*", "");
                html.append("<blockquote>").append(cleanMarkdownInlineForHtml(quoteContent)).append("</blockquote>\n");
                continue;
            }
            
            if (trimmedLine.matches("^(#{1,6})\\s+.+")) {
                if (inList) {
                    html.append("</").append(listType).append(">\n");
                    inList = false;
                }
                int level = trimmedLine.indexOf(" ");
                String headingText = trimmedLine.substring(level + 1);
                html.append("<h").append(level).append(">")
                   .append(cleanMarkdownInlineForHtml(headingText))
                   .append("</h").append(level).append(">\n");
                continue;
            }
            
            if (trimmedLine.matches("^\\s*[-*+]\\s+.+")) {
                String newItemType = "ul";
                if (!newItemType.equals(listType)) {
                    if (inList) {
                        html.append("</").append(listType).append(">\n");
                    }
                    html.append("<ul>\n");
                    inList = true;
                    listType = newItemType;
                }
                String listItem = trimmedLine.replaceAll("^\\s*[-*+]\\s+", "");
                html.append("<li>").append(cleanMarkdownInlineForHtml(listItem)).append("</li>\n");
                continue;
            }
            
            if (trimmedLine.matches("^\\s*\\d+\\.\\s+.+")) {
                String newItemType = "ol";
                if (!newItemType.equals(listType)) {
                    if (inList) {
                        html.append("</").append(listType).append(">\n");
                    }
                    html.append("<ol>\n");
                    inList = true;
                    listType = newItemType;
                }
                String listItem = trimmedLine.replaceAll("^\\s*\\d+\\.\\s+", "");
                html.append("<li>").append(cleanMarkdownInlineForHtml(listItem)).append("</li>\n");
                continue;
            }
            
            if (trimmedLine.isEmpty()) {
                if (inList) {
                    html.append("</").append(listType).append(">\n");
                    inList = false;
                    listType = "";
                }
                html.append("<br/>\n");
            } else {
                html.append("<p>").append(cleanMarkdownInlineForHtml(line)).append("</p>\n");
            }
        }
        
        if (inCodeBlock) {
            html.append("</code></pre>\n");
        }
        if (inTable) {
            html.append("</table>\n");
        }
        if (inList) {
            html.append("</").append(listType).append(">\n");
        }
        
        return html.toString();
    }
    
    private String cleanMarkdownInlineForHtml(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        
        String cleaned = text;
        
        cleaned = cleaned.replaceAll("~~(.+?)~~", "<del>$1</del>");
        
        cleaned = cleaned.replaceAll("\\*\\*(.+?)\\*\\*", "<strong>$1</strong>");
        
        cleaned = cleaned.replaceAll("\\*(.+?)\\*", "<em>$1</em>");
        
        cleaned = cleaned.replaceAll("_(.+?)_", "<em>$1</em>");
        
        cleaned = cleaned.replaceAll("`([^`]+)`", "<code>$1</code>");
        
        cleaned = cleaned.replaceAll("!\\[([^]]*)\\]\\(([^)]+)\\)", "<img src=\"$2\" alt=\"$1\"/>");
        
        cleaned = cleaned.replaceAll("\\[([^]]+)\\]\\(([^)]+)\\)", "<a href=\"$2\">$1</a>");
        
        cleaned = cleaned.replaceAll("\\[([^]]+)\\]\\[]", "$1");
        
        return cleaned;
    }
}

