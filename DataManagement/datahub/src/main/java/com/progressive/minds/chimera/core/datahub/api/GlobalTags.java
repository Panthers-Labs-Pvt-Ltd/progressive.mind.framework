package com.progressive.minds.chimera.core.datahub.api;

import java.util.List;

public class GlobalTags {
    private GlobalTagsValue value;

    // Getters and Setters
    public GlobalTagsValue getValue() {
        return value;
    }

    public void setValue(GlobalTagsValue value) {
        this.value = value;
    }

    public static class GlobalTagsValue {
        private List<Tag> tags;

        // Getters and Setters
        public List<Tag> getTags() {
            return tags;
        }

        public void setTags(List<Tag> tags) {
            this.tags = tags;
        }
    }

    public static class Tag {
        private String tag;

        // Getters and Setters
        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }
    }
}
