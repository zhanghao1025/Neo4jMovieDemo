
package com.springboot.repository;

import com.springboot.domain.Movie;
import com.springboot.domain.Person;

import java.util.List;

/**
 * @author yqbjtu
 * @version 2018/4/19 8:59
 */
public interface CustomizedMovieRepository {
    List<Movie>  someCustomMethod();
    Iterable<Movie>  findUseClassMethod(Integer id);
    Iterable<Person>  findPersonMethod(String name);
}