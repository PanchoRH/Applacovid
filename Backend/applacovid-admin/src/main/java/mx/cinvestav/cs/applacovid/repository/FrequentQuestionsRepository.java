/**
 * Computer Department at Cinvestav CDMX.
 * Copyright (c) 2020
 * All rights reserved.
 */

package mx.cinvestav.cs.applacovid.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import mx.cinvestav.cs.applacovid.jpa.FrequentQuestions;



@Repository
public interface FrequentQuestionsRepository extends JpaRepository<FrequentQuestions, Long>
{
	
}
