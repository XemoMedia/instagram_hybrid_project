package com.xmedia.social.sentimentanalysis.repository.projection;

import java.sql.Timestamp;

public interface SentimentTrendProjection {
    Timestamp getBucket();
    Long getPositive();
    Long getNeutral();
    Long getNegative();
}

