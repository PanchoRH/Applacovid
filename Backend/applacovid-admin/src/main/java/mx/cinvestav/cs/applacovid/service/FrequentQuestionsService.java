/**
 * Computer Department at Cinvestav CDMX.
 * Copyright (c) 2020
 * All rights reserved.
 */

package mx.cinvestav.cs.applacovid.service;

import java.util.List;
import mx.cinvestav.cs.applacovid.jpa.FrequentQuestions;


public interface FrequentQuestionsService {
	
	public void saveFrequentQuestions(FrequentQuestions frequentQuestions);
	
	public List<FrequentQuestions> getFrequentQuestions();
	
	public void deleteFrequentQuestions(Long idfrequentQuestions);

}
