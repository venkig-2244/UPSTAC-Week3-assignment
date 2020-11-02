package org.upgrad.upstac.testrequests.consultation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.upgrad.upstac.testrequests.TestRequest;
import org.upgrad.upstac.users.User;

import javax.transaction.Transactional;
import java.time.LocalDate;

@Service
@Validated
public class ConsultationService {

    @Autowired
    private ConsultationRepository consultationRepository;

    private static Logger logger = LoggerFactory.getLogger(ConsultationService.class);


    @Transactional
    public Consultation assignForConsultation( TestRequest testRequest, User doctor) {
        //Implement this method to assign the consultation service
        // create object of Consultation class and use the setter methods to set doctor and testRequest details
        // make use of save() method of consultationRepository to return the Consultation object

        // Implementation done by Venkatesh G
        Consultation cons = new Consultation();
        cons.setRequest(testRequest);
        cons.setDoctor(doctor);

        return consultationRepository.save(cons); // replace this line with your code


    }

    public Consultation updateConsultation(TestRequest testRequest , CreateConsultationRequest createConsultationRequest) {
        //Implement this method to update the consultation
        // create an object of Consultation and make use of setters to set Suggestion, Comments, and UpdatedOn values
        // make use of save() method of consultationRepository to return the Consultation object

        // Implementation done by Venkatesh G
        Consultation cons = consultationRepository.findByRequest(testRequest).get();
        cons.setRequest(testRequest);
        cons.setUpdatedOn(LocalDate.now());
        cons.setComments(createConsultationRequest.getComments());
        cons.setSuggestion(createConsultationRequest.getSuggestion());

        return consultationRepository.save(cons);


    }


}
