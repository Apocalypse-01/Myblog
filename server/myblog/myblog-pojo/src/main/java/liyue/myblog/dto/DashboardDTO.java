package liyue.myblog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDTO {

    private int totalArticles;

    private long totalWords;

    private int totalLikes;

    private int totalFavorites;

    private List<CategoryStat> categoryDistribution;

    private List<DailyStat> dailyTrend;

    private List<TopArticle> topArticles;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryStat {
        private String category;
        private int count;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailyStat {
        private String month;
        private int count;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TopArticle {
        private int id;
        private String title;
        private int likes;
        private int favorites;
    }
}
