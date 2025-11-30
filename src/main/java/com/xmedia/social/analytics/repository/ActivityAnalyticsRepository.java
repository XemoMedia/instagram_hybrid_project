package com.xmedia.social.analytics.repository;

import com.xmedia.social.analytics.repository.projection.ActivityTypeProjection;
import com.xmedia.social.analytics.repository.projection.DailyTotalsProjection;
import com.xmedia.social.instagram.entity.Post;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface ActivityAnalyticsRepository extends JpaRepository<Post, String> {

    @Query(
        value =
            """
            SELECT d.activity_date,
                   COALESCE(p.post_count, 0) AS post_count,
                   COALESCE(c.comment_count, 0) AS comment_count,
                   COALESCE(r.reply_count, 0) AS reply_count
            FROM (
                SELECT DISTINCT DATE(timestamp) AS activity_date FROM post
                WHERE timestamp >= :startDate
                UNION
                SELECT DISTINCT DATE(timestamp) AS activity_date FROM comments
                WHERE timestamp >= :startDate
                UNION
                SELECT DISTINCT DATE(timestamp) AS activity_date FROM replies
                WHERE timestamp >= :startDate
            ) d
            LEFT JOIN (
                SELECT DATE(timestamp) AS dt, COUNT(*) AS post_count
                FROM post
                WHERE timestamp >= :startDate
                GROUP BY dt
            ) p ON d.activity_date = p.dt
            LEFT JOIN (
                SELECT DATE(timestamp) AS dt, COUNT(*) AS comment_count
                FROM comments
                WHERE timestamp >= :startDate
                GROUP BY dt
            ) c ON d.activity_date = c.dt
            LEFT JOIN (
                SELECT DATE(timestamp) AS dt, COUNT(*) AS reply_count
                FROM replies
                WHERE timestamp >= :startDate
                GROUP BY dt
            ) r ON d.activity_date = r.dt
            ORDER BY d.activity_date
            """,
        nativeQuery = true)
    List<DailyTotalsProjection> fetchDailyTotals(@Param("startDate") LocalDateTime startDate);

    @Query(
        value =
            """
            SELECT DATE(timestamp) AS activity_date,
                   'post' AS activity_type,
                   COUNT(*) AS total_count
            FROM post
            WHERE timestamp >= :startDate
            GROUP BY activity_date
            UNION ALL
            SELECT DATE(timestamp) AS activity_date,
                   'comment' AS activity_type,
                   COUNT(*) AS total_count
            FROM comments
            WHERE timestamp >= :startDate
            GROUP BY activity_date
            UNION ALL
            SELECT DATE(timestamp) AS activity_date,
                   'reply' AS activity_type,
                   COUNT(*) AS total_count
            FROM replies
            WHERE timestamp >= :startDate
            GROUP BY activity_date
            ORDER BY activity_date, activity_type
            """,
        nativeQuery = true)
    List<ActivityTypeProjection> fetchActivityTypeSeries(@Param("startDate") LocalDateTime startDate);
}

