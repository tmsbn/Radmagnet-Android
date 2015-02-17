package tms.ubrats;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by tmsbn on 2/15/15.
 */
public class News extends SugarRecord<News> {

    String title;
    String description;
    Date date;
    String newsLink;
    ArrayList<String> details;
}
