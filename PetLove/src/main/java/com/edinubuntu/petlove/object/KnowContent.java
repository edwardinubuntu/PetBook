package com.edinubuntu.petlove.object;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by edward_chiang on 13/10/9.
 */
@Table(name = "KnowContents")
public class KnowContent extends Model {

    @Column(name = "Title")
    private String title;

    @Column(name = "Url")
    private String url;

    public KnowContent() {
        super();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
