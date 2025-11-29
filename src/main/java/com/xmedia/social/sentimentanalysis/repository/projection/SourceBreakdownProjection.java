package com.xmedia.social.sentimentanalysis.repository.projection;

public interface SourceBreakdownProjection {
    String getSource_type();
    Long getTotal();
    Long getPositive();
    Long getNeutral();
    Long getNegative();
    Double getAvg_score();
}

