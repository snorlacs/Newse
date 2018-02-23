package utils;

import com.snorlacs.newse.domain.Article;
import com.snorlacs.newse.domain.Author;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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

    public static Article generateUpdatedArticle(Article originalArticle) {
        Article article = new Article();
        article.setHeader(originalArticle.getHeader() + "updated");
        article.setShortDescription(originalArticle.getShortDescription() + "updated");
        article.setText(originalArticle.getText() + "updated");

        List<String> updatedKeywords = new ArrayList<>(originalArticle.getKeywords());
        updatedKeywords.add("update");
        article.setKeywords(updatedKeywords);
        article.setPublishedOn(originalArticle.getPublishedOn());
        List<Author> updateAuthors = new ArrayList<>(originalArticle.getAuthors());
        Author newAuthor = new Author();
        newAuthor.setName("test-author2");
        updateAuthors.add(newAuthor);
        article.setAuthors(updateAuthors);
        return article;
    }

}
