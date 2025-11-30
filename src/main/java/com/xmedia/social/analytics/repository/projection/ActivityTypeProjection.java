package com.xmedia.social.analytics.repository.projection;

import java.sql.Date;

public interface ActivityTypeProjection {
    Date getActivity_date();
    String getActivity_type();
    Long getTotal_count();
}

