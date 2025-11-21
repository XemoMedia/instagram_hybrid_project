package com.example.instagram.dto;

import lombok.Data;
import java.util.List;

@Data
public class InstagramMediaListResponseDto {
    private List<MediaItem> data;
    private Paging paging;

    
    public List<MediaItem> getData() {
		return data;
	}

	public void setData(List<MediaItem> data) {
		this.data = data;
	}

	public Paging getPaging() {
		return paging;
	}

	public void setPaging(Paging paging) {
		this.paging = paging;
	}

	@Data
    public static class MediaItem {
        private String id;
        private String timestamp;
        private String caption;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getTimestamp() {
			return timestamp;
		}
		public void setTimestamp(String timestamp) {
			this.timestamp = timestamp;
		}
		public String getCaption() {
			return caption;
		}
		public void setCaption(String caption) {
			this.caption = caption;
		}
        
        
        
    }

    @Data
    public static class Paging {
        private Cursors cursors;
        private String next;
        private String previous;
        
        

        public Cursors getCursors() {
			return cursors;
		}



		public void setCursors(Cursors cursors) {
			this.cursors = cursors;
		}



		public String getNext() {
			return next;
		}



		public void setNext(String next) {
			this.next = next;
		}



		public String getPrevious() {
			return previous;
		}



		public void setPrevious(String previous) {
			this.previous = previous;
		}



		@Data
        public static class Cursors {
            private String before;
            private String after;
			public String getBefore() {
				return before;
			}
			public void setBefore(String before) {
				this.before = before;
			}
			public String getAfter() {
				return after;
			}
			public void setAfter(String after) {
				this.after = after;
			}
            
            
        }
    }
}
