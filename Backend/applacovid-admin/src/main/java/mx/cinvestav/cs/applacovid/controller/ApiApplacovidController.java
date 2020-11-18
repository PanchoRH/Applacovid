package mx.cinvestav.cs.applacovid.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import mx.cinvestav.cs.applacovid.jpa.FrequentQuestions;
import mx.cinvestav.cs.applacovid.jpa.News;
import mx.cinvestav.cs.applacovid.service.FrequentQuestionsService;
import mx.cinvestav.cs.applacovid.service.NewsService;

@RestController
@RequestMapping("/api")
public class ApiApplacovidController {

	@Autowired
	private NewsService newsService;
	
	@Autowired
	private FrequentQuestionsService frequentQuestionsService;
	
	@GetMapping("/news")
	@ResponseStatus(HttpStatus.OK)
	public List<News> getNews(){
		return newsService.getNews();
	}
	
	@GetMapping("/questions")
	@ResponseStatus(HttpStatus.OK)
	public List<FrequentQuestions> getQuestions(){
		return frequentQuestionsService.getFrequentQuestions();
	}
}
