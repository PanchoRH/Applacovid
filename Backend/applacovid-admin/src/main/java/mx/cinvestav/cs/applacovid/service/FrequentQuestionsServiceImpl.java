package mx.cinvestav.cs.applacovid.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import mx.cinvestav.cs.applacovid.jpa.FrequentQuestions;
import mx.cinvestav.cs.applacovid.repository.FrequentQuestionsRepository;

@Service
public class FrequentQuestionsServiceImpl implements FrequentQuestionsService {

	@Autowired
	private FrequentQuestionsRepository frequentQuestionsRepository;
	
	@Override
	public void saveFrequentQuestions(FrequentQuestions frequentQuestions) {
		frequentQuestionsRepository.save(frequentQuestions);
	}

	@Override
	public List<FrequentQuestions> getFrequentQuestions() {	
		return frequentQuestionsRepository.findAll();
	}

	@Override
	public void deleteFrequentQuestions(Long idfrequentQuestions) {
		frequentQuestionsRepository.deleteById(idfrequentQuestions);
	}

}
