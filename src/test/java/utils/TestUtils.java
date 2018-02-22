package utils;

import com.snorlacs.newse.domain.Article;
import com.snorlacs.newse.domain.Author;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class TestUtils {
    public static Article generateTestArticle() {
        Article article = new Article();
        article.setHeader("Test Article");
        article.setShortDescription("Test");
        article.setText("This article is for testing");
        Author author = new Author();
        author.setName("test-author");
        article.setAuthors(Collections.singletonList(author));
        article.setPublishedOn(new Date());
        article.setKeywords(new ArrayList<String>() {{
            add("test");
        }});

        return article;
    }

}
