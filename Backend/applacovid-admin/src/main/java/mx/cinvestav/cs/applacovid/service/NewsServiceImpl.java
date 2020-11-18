package mx.cinvestav.cs.applacovid.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.cinvestav.cs.applacovid.jpa.News;
import mx.cinvestav.cs.applacovid.repository.NewsRepository;

@Service
public class NewsServiceImpl implements NewsService {
	
	@Autowired
	private NewsRepository newsRepository;

	@Override
	public void saveNews(News news) {
		newsRepository.save(news);
	}

	@Override
	public List<News> getNews() {
		return newsRepository.findAll();
	}

	@Override
	public void deleteNews(Long idNews) {
		newsRepository.deleteById(idNews);
	}

}
