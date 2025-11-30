package com.xmedia.social.analytics.repository.projection;

import java.sql.Date;

public interface DailyTotalsProjection {
    Date getActivity_date();
    Long getPost_count();
    Long getComment_count();
    Long getReply_count();
}

