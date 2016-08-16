package com.vilomar.ozzie.udacityprojectportfolio;

/**
 * Created by Ozzie on 8/15/16.
 */
public class ReviewContent {

    String list_movie_synopsis;
    String list_movie_author;

    public ReviewContent(String list_movie_synopsis, String list_movie_author) {
        this.list_movie_synopsis = list_movie_synopsis;
        this.list_movie_author = list_movie_author;
    }

    public String setMovieSynopsis(String list_movie_synopsis) {
        return this.list_movie_synopsis = list_movie_synopsis;
    }
    public String setMovieAuthor(String list_movie_author) {
        return this.list_movie_author = list_movie_author;
    }
}
