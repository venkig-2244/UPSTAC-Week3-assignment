package org.upgrad.upstac.testrequests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.web.server.ResponseStatusException;
import org.upgrad.upstac.testrequests.TestRequest;
import org.upgrad.upstac.testrequests.consultation.ConsultationController;
import org.upgrad.upstac.testrequests.consultation.CreateConsultationRequest;
import org.upgrad.upstac.testrequests.consultation.DoctorSuggestion;
import org.upgrad.upstac.testrequests.lab.CreateLabResult;
import org.upgrad.upstac.testrequests.lab.TestStatus;
import org.upgrad.upstac.testrequests.RequestStatus;
import org.upgrad.upstac.testrequests.TestRequestQueryService;
import org.upgrad.upstac.users.User;
import org.upgrad.upstac.users.models.Gender;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@Slf4j
class ConsultationControllerTest {


    @Autowired
    ConsultationController consultationController;


    @Autowired
    TestRequestQueryService testRequestQueryService;


    @Test
    @WithUserDetails(value = "doctor")
    public void calling_assignForConsultation_with_valid_test_request_id_should_update_the_request_status(){

        TestRequest testRequest = getTestRequestByStatus(RequestStatus.LAB_TEST_COMPLETED);

        //Implement this method

        //Create another object of the TestRequest method and explicitly assign this object for Consultation using assignForConsultation() method
        // from consultationController class. Pass the request id of testRequest object.

        TestRequest tr1 = new TestRequest();

        tr1.setName("xyz");
        tr1.setCreated(LocalDate.now());
        tr1.setAge(50);
        tr1.setEmail("somemail@gmail.com");
        tr1.setPhoneNumber("989456554");
        tr1.setPinCode(560086);
        tr1.setAddress("some address");
        tr1.setGender(Gender.MALE);
        tr1.setStatus(RequestStatus.LAB_TEST_IN_PROGRESS);
        User user = new User();
        user.setUserName("user");
        tr1.setCreatedBy(user);

        consultationController.assignForConsultation(testRequest.getRequestId());

        //Use assertThat() methods to perform the following two comparisons
        //  1. the request ids of both the objects created should be same
        //  2. the status of the second object should be equal to 'DIAGNOSIS_IN_PROCESS'
        // make use of assertNotNull() method to make sure that the consultation value of second object is not null
        // use getConsultation() method to get the lab result
        assertThat(testRequest.getRequestId(), equalTo(tr1.getRequestId()));
        assertThat(testRequest.getStatus(), equalTo(RequestStatus.DIAGNOSIS_IN_PROCESS));
        assertThat(testRequest.getConsultation(), equalTo(tr1.getConsultation()));

    }

    public TestRequest getTestRequestByStatus(RequestStatus status) {
        return testRequestQueryService.findBy(status).stream().findFirst().get();
    }

    @Test
    @WithUserDetails(value = "doctor")
    public void calling_assignForConsultation_with_valid_test_request_id_should_throw_exception(){

        Long InvalidRequestId= -34L;

        //Implement this method


        // Create an object of ResponseStatusException . Use assertThrows() method and pass assignForConsultation() method
        // of consultationController with InvalidRequestId as Id
        ResponseStatusException result = assertThrows(ResponseStatusException.class,()->{

            consultationController.assignForConsultation(InvalidRequestId);
        });


        //Use assertThat() method to perform the following comparison
        //  the exception message should be contain the string "Invalid ID"
        assertThat(result.getMessage(),containsString("Invalid ID"));

    }

    @Test
    @WithUserDetails(value = "doctor")
    public void calling_updateConsultation_with_valid_test_request_id_should_update_the_request_status_and_update_consultation_details(){

        TestRequest testRequest = getTestRequestByStatus(RequestStatus.DIAGNOSIS_IN_PROCESS);

        //Implement this method
        //Create an object of CreateConsultationRequest and call getCreateConsultationRequest() to create the object. Pass the above created object as the parameter
        CreateConsultationRequest consultationRequest = getCreateConsultationRequest(testRequest);

        //Create another object of the TestRequest method and explicitly update the status of this object
        // to be 'COMPLETED'. Make use of updateConsultation() method from labRequestController class (Pass the previously created two objects as parameters)
        TestRequest tr1 = new TestRequest();

        tr1.setName("xyz");
        tr1.setCreated(LocalDate.now());
        tr1.setAge(50);
        tr1.setEmail("somemail@gmail.com");
        tr1.setPhoneNumber("989456554");
        tr1.setPinCode(560086);
        tr1.setAddress("some address");
        tr1.setGender(Gender.MALE);
        tr1.setStatus(RequestStatus.COMPLETED);
        User user = new User();
        user.setUserName("user");
        tr1.setCreatedBy(user);

        consultationController.updateConsultation(tr1.getRequestId(), consultationRequest);

        //Use assertThat() methods to perform the following three comparisons
        //  1. the request ids of both the objects created should be same
        //  2. the status of the second object should be equal to 'COMPLETED'
        // 3. the suggestion of both the objects created should be same. Make use of getSuggestion() method to get the results.
        assertThat(testRequest.getRequestId(), equalTo(tr1.getRequestId()));
        assertThat(testRequest.getStatus(), equalTo(RequestStatus.COMPLETED));
        assertThat(testRequest.getConsultation().getSuggestion(), equalTo(tr1.getConsultation().getSuggestion()));


    }


    @Test
    @WithUserDetails(value = "doctor")
    public void calling_updateConsultation_with_invalid_test_request_id_should_throw_exception(){

        TestRequest testRequest = getTestRequestByStatus(RequestStatus.DIAGNOSIS_IN_PROCESS);

        //Implement this method

        //Create an object of CreateConsultationRequest and call getCreateConsultationRequest() to create the object. Pass the above created object as the parameter
        CreateConsultationRequest consultRequest = getCreateConsultationRequest(testRequest);

        // Create an object of ResponseStatusException . Use assertThrows() method and pass updateConsultation() method
        // of consultationController with a negative long value as Id and the above created object as second parameter

        //Refer to the TestRequestControllerTest to check how to use assertThrows() method
        Long InvalidId= -334L;
        ResponseStatusException result = assertThrows(ResponseStatusException.class,()->{
            consultationController.updateConsultation(InvalidId, consultRequest);
        });

        //Use assertThat() method to perform the following comparison
        //  the exception message should be contain the string "Invalid ID"
        assertThat(result.getMessage(),containsString("Invalid ID"));

    }

    @Test
    @WithUserDetails(value = "doctor")
    public void calling_updateConsultation_with_invalid_empty_status_should_throw_exception(){

        TestRequest testRequest = getTestRequestByStatus(RequestStatus.DIAGNOSIS_IN_PROCESS);

        //Implement this method

        //Create an object of CreateConsultationRequest and call getCreateConsultationRequest() to create the object. Pass the above created object as the parameter
        CreateConsultationRequest consultRequest = getCreateConsultationRequest(testRequest);

        // Set the suggestion of the above created object to null.
        consultRequest.setSuggestion(null);

        // Create an object of ResponseStatusException . Use assertThrows() method and pass updateConsultation() method
        // of consultationController with request Id of the testRequest object and the above created object as second parameter
        //Refer to the TestRequestControllerTest to check how to use assertThrows() method
        ResponseStatusException result = assertThrows(ResponseStatusException.class,()->{
            consultationController.updateConsultation(testRequest.getRequestId(), consultRequest);
        });


    }

    public CreateConsultationRequest getCreateConsultationRequest(TestRequest testRequest) {

        //Create an object of CreateLabResult and set all the values
        // if the lab result test status is Positive, set the doctor suggestion as "HOME_QUARANTINE" and comments accordingly
        // else if the lab result status is Negative, set the doctor suggestion as "NO_ISSUES" and comments as "Ok"
        // Return the object

        CreateLabResult labResult = new CreateLabResult();
        labResult.setResult(testRequest.labResult.getResult());
        labResult.setTemperature(testRequest.labResult.getTemperature());
        labResult.setOxygenLevel(testRequest.labResult.getOxygenLevel());
        labResult.setHeartBeat(testRequest.labResult.getHeartBeat());

        CreateConsultationRequest consRequest = new CreateConsultationRequest();

        if (testRequest.getStatus().equals(TestStatus.POSITIVE)) {
            consRequest.setSuggestion(DoctorSuggestion.HOME_QUARANTINE);
            consRequest.setComments("Patient suggested to be home quaratined for 2 weeks.");
        } else if(testRequest.getStatus().equals(TestStatus.NEGATIVE)) {
            consRequest.setSuggestion(DoctorSuggestion.NO_ISSUES);
            consRequest.setComments("Ok");
        }
        return consRequest; // Replace this line with your code

    }

}