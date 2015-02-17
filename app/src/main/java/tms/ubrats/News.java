package tms.ubrats;


import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by tmsbn on 2/15/15.
 */
public class News extends RealmObject {

    String postType;

    @PrimaryKey
    String postId;

    Date timeStamp;

    boolean active;
    String headline;
    String imageUrl;
    String creator;
    String creatorDp;

    @Ignore
    String sourceUrl;


}
