/**
 * Computer Department at Cinvestav CDMX.
 * Copyright (c) 2020
 * All rights reserved.
 */

package mx.cinvestav.cs.applacovid.controller;


import mx.cinvestav.cs.applacovid.jpa.CovidCode;
import mx.cinvestav.cs.applacovid.jpa.CovidTestImpl;
import mx.cinvestav.cs.applacovid.jpa.ExposeeRequest;
import mx.cinvestav.cs.applacovid.jpa.FrequentQuestions;
import mx.cinvestav.cs.applacovid.jpa.News;
import mx.cinvestav.cs.applacovid.rest.Dp3tService;
import mx.cinvestav.cs.applacovid.service.CovidCodeService;
import mx.cinvestav.cs.applacovid.service.FrequentQuestionsService;
import mx.cinvestav.cs.applacovid.service.NewsService;
import mx.cinvestav.cs.applacovid.service.RecordsService;
import mx.cinvestav.cs.applacovid.utils.ValidationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


@Controller
@RequestMapping("/console")
public class AdminConsoleController
{
	@Autowired
	public AdminConsoleController(ApplicationContext applicationContext)
	{
		this.applicationContext = applicationContext;
	}


	@GetMapping("/")
	public String index(ModelMap model)
	{
		return "console/index";
	}

	@GetMapping("/newsForm")
	public String news(ModelMap model) {
		return"console/newsForm";
	}
	
	@PostMapping("/addNews")
	public String addNews(@Valid News news, BindingResult result, ModelMap model) {
		newsService.saveNews(news);		
		return "console/success";
	}
	
	@GetMapping("/refreshnews")
	public String refreshNewsFragment(Model model)
	{
		model.addAttribute("allNews", newsService.getNews());
		return "fragments/records :: newsFragment";
	}
	
	@GetMapping("/questionsForm")
	public String questions(ModelMap model) {
		return"console/questionsForm";
	}

	@GetMapping("/newCovidCode")
	public String newCovidCode(ModelMap model)
	{
		return"console/newCovidCode";
	}
	
	@PostMapping("/addQuestions")
	public String addQuestions(@Valid FrequentQuestions frequentQuestions, BindingResult result, ModelMap model) {
		frequentQuestionsService.saveFrequentQuestions(frequentQuestions);		
		return "console/success";
	}
	
	@GetMapping("/refreshquestions")
	public String refreshQuestions(Model model)
	{
		model.addAttribute("allQuestions", frequentQuestionsService.getFrequentQuestions());
		return "fragments/records :: questionsFragment";
	}

	@GetMapping("/generateCode")
	public ResponseEntity<String> generateCovidCode()
	{
		Random rand = new Random();
		double number = rand.nextDouble();
		String nStr = String.valueOf(number);
		String randomNumber = nStr.substring(2, 14);

		logger.debug("New covid code {}.", randomNumber);

		CovidCode covidCode = new CovidCode();
		covidCode.setCode(randomNumber);
		covidCode.setCreatedAt(Date.from(Instant.ofEpochMilli(System.currentTimeMillis())));
		covidCode.setUsed(false);

		covidCodeService.saveCovidCode(covidCode);

		HttpStatus response = HttpStatus.OK;
		return new ResponseEntity<>(randomNumber, response);
	}

	@GetMapping("/newrecord")
	public String newRecord(
		HttpServletRequest request,
		@RequestParam(name = "key", required = false) String key,
		@RequestParam(name = "dateKey", required = false) String dateKey,
		@RequestParam(name = "uuid", required = false) String uuid,
		ModelMap model)
	{
		logger.debug("Redirecting: key {}, dateKey {}, uuid {} to form.", key, dateKey, uuid);

		model.addAttribute("key", key);
		model.addAttribute("dateKey", dateKey);
		model.addAttribute("uuid", uuid);

		return "console/newrecord";
	}


	@PostMapping({"/saverecord",
		"/saverecord/"})
	@ResponseBody
	public ResponseEntity<String> saveRecord(
		@Valid @ModelAttribute("kdRecord") final CovidTestImpl covidTest,
		final BindingResult bindingResult,
		final ModelMap model)
	{
		ResponseEntity<String> responseEntity;

		if (bindingResult.hasErrors())
		{
			responseEntity = new ResponseEntity<>("La información es incorrecta.",
				HttpStatus.NOT_ACCEPTABLE);
		}
		else
		{
			LocalDateTime now = LocalDateTime.now();
			covidTest.setRegisterDate(now);
			covidTest.setLastUpdate(now);
			covidTest.setIsExposed(false);

			Date keyDate = new Date(covidTest.getDateKey());
			//covidTest.setKeyDate(new java.sql.Timestamp(keyDate.getTime()).toLocalDateTime());
			covidTest.setKeyDate(keyDate);

			logger.debug("Reviewing encoding of key: {}.", covidTest.getKd());
			String encodeKey = covidTest.getKd().replace(" ", "+");
			covidTest.setKd(encodeKey);
			logger.debug("Encoding key: {}.", covidTest.getKd());

			recordsService.addRecord(covidTest);
			model.clear();
			responseEntity = new ResponseEntity<>("Record saved successfully.", HttpStatus.OK);

			logger.debug("Record saved: key {}, dateKey {}.",
				covidTest.getKd(),
				covidTest.getDateKey());
		}

		return responseEntity;
	}


	@PostMapping({"/addExpose",
		"/addExpose/"})
	@ResponseBody
	public ResponseEntity<String> addExpose(
		@Valid @ModelAttribute("exposeeRequest") final ExposeeRequest exposeeRequest,
		final BindingResult bindingResult,
		final ModelMap model)
	{
		ResponseEntity<String> responseEntity;

		if (!validationUtils.isValidBase64Key(exposeeRequest.getKey()))
		{
			logger.debug("No valid base64 key {}.", exposeeRequest.getKey());
			return new ResponseEntity<>("No valid base64 key",
				HttpStatus.BAD_REQUEST);
		}

		if (bindingResult.hasErrors())
		{
			return new ResponseEntity<>("Error binding data.",
				HttpStatus.NOT_ACCEPTABLE);
		}
		else
		{
			boolean response = dp3tService.postExposed(exposeeRequest);

			if (response)
			{
				CovidTestImpl covidTest = recordsService
					.getRecord(exposeeRequest.getKey());

				if (covidTest != null)
				{
					LocalDateTime now = LocalDateTime.now();

					covidTest.setIsExposed(true);
					covidTest.setLastUpdate(now);
					recordsService.updateRecord(covidTest);
					responseEntity = new ResponseEntity<>("User reported positive.",
						HttpStatus.OK);

					logger.debug("Record reported positive: key {}, dateKey {}.",
						covidTest.getKd(),
						covidTest.getDateKey());
				}
				else
				{
					responseEntity = new ResponseEntity<>("Record not found.",
						HttpStatus.NOT_ACCEPTABLE);
				}
			}
			else
			{
				responseEntity = new ResponseEntity<>(
					"Something went wrong when exposing the record to dp3t.",
					HttpStatus.NOT_ACCEPTABLE);
			}
		}

		return responseEntity;
	}


	@PostMapping({"/deleterecord",
		"/deleterecord/"})
	@ResponseBody
	public ResponseEntity deleteRecord(
		@RequestParam(name = "recordId") String recordId)
	{
		logger.debug("Deleting record: id {}.", recordId);

		recordsService.deleteRecord(Long.parseLong(recordId));

		return new ResponseEntity<>("Record deleted successfully.",
			HttpStatus.OK);
	}


	@RequestMapping(value = "/refreshrecords", method = RequestMethod.GET)
	public String refreshRecordsFragment(Model model)
	{
		model.addAttribute("allRecords", recordsService.getNotExposedRecords());
		return "fragments/records :: recordsFragment";
	}


	@RequestMapping(value = "/refreshhistoric", method = RequestMethod.GET)
	public String refreshHistoricFragment(Model model)
	{
		model.addAttribute("allExposedRecords", recordsService.getExposedRecords());
		return "fragments/records :: historicFragment";
	}


	@ModelAttribute("allRecords")
	public List<CovidTestImpl> allNotExposedRecords()
	{
		return recordsService.getNotExposedRecords();
	}


	@ModelAttribute("allExposedRecords")
	public List<CovidTestImpl> allExposedRecords()
	{
		return recordsService.getExposedRecords();
	}


	@ModelAttribute("allNews")
	public List<News> allNews()
	{
		return newsService.getNews();
	}


	@ModelAttribute("allQuestions")
	public List<FrequentQuestions> allQuestions()
	{
		return frequentQuestionsService.getFrequentQuestions();
	}


	@ModelAttribute("kdRecord")
	public CovidTestImpl kdRecord()
	{
		return new CovidTestImpl();
	}


	@ModelAttribute("exposeeRequest")
	public ExposeeRequest exposeeRequest()
	{
		return new ExposeeRequest();
	}


	@ModelAttribute("module")
	public String module()
	{
		return "applacovid";
	}


	@InitBinder
	public void initBinder(WebDataBinder binder)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMMM/yyyy");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationExceptions(
		MethodArgumentNotValidException ex)
	{
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});

		return errors;
	}


	Logger logger = LoggerFactory.getLogger(AdminConsoleController.class);

	@Autowired
	private RecordsService recordsService;

	@Autowired
	private Dp3tService dp3tService;

	@Autowired
	private ValidationUtils validationUtils;

	private ApplicationContext applicationContext;
	
	@Autowired
	private NewsService newsService;
	
	@Autowired
	private FrequentQuestionsService frequentQuestionsService;

	@Autowired
	private CovidCodeService covidCodeService;
}
