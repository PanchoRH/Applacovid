package mx.cinvestav.cs.applacovid.service;

import java.util.List;

import mx.cinvestav.cs.applacovid.jpa.News;

public interface NewsService {
	
	public void saveNews(News news);
	
	public List<News> getNews();
	
	public void deleteNews(Long idNews);

}
