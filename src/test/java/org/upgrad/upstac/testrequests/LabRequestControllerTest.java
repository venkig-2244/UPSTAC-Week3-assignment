package org.upgrad.upstac.testrequests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.web.server.ResponseStatusException;
import org.upgrad.upstac.testrequests.lab.CreateLabResult;
import org.upgrad.upstac.testrequests.lab.LabRequestController;
import org.upgrad.upstac.testrequests.lab.TestStatus;
import org.upgrad.upstac.users.User;
import org.upgrad.upstac.users.models.Gender;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Slf4j
class LabRequestControllerTest {

    @Autowired
    LabRequestController labRequestController;

    @Autowired
    TestRequestQueryService testRequestQueryService;


    @Test
    @WithUserDetails(value = "tester")
    public void calling_assignForLabTest_with_valid_test_request_id_should_update_the_request_status(){

        TestRequest testRequest = getTestRequestByStatus(RequestStatus.INITIATED);
        //Implement this method

        //Create another object of the TestRequest method and explicitly assign this object for Lab Test using assignForLabTest() method
        // from labRequestController class. Pass the request id of testRequest object.
        TestRequest tr1 = new TestRequest();

        tr1.setName("xyz");
        tr1.setCreated(LocalDate.now());
        tr1.setAge(50);
        tr1.setEmail("somemail@gmail.com");
        tr1.setPhoneNumber("989456554");
        tr1.setPinCode(560086);
        tr1.setAddress("some address");
        tr1.setGender(Gender.MALE);
        User user = new User();
        user.setUserName("user");
        tr1.setCreatedBy(user);

        labRequestController.assignForLabTest(testRequest.getRequestId());

        //Use assertThat() methods to perform the following two comparisons
        //  1. the request ids of both the objects created should be same
        //  2. the status of the second object should be equal to 'INITIATED'
        // make use of assertNotNull() method to make sure that the lab result of second object is not null
        // use getLabResult() method to get the lab result

        assertThat(tr1.getRequestId(), equalTo(testRequest.getRequestId()));
        assertThat(tr1.getStatus(), equalTo(RequestStatus.INITIATED));
        assertNotNull(tr1.getLabResult());

    }

    public TestRequest getTestRequestByStatus(RequestStatus status) {
        return testRequestQueryService.findBy(status).stream().findFirst().get();
    }

    @Test
    @WithUserDetails(value = "tester")
    public void calling_assignForLabTest_with_valid_test_request_id_should_throw_exception(){

        Long InvalidRequestId= -34L;

        //Implement this method


        // Create an object of ResponseStatusException . Use assertThrows() method and pass assignForLabTest() method
        // of labRequestController with InvalidRequestId as Id
        ResponseStatusException result = assertThrows(ResponseStatusException.class,()->{

            labRequestController.assignForLabTest(InvalidRequestId);
        });

        //Use assertThat() method to perform the following comparison
        //  the exception message should be contain the string "Invalid ID"
        assertThat(result.getMessage(),containsString("Invalid ID"));

    }

    @Test
    @WithUserDetails(value = "tester")
    public void calling_updateLabTest_with_valid_test_request_id_should_update_the_request_status_and_update_test_request_details(){

        TestRequest testRequest = getTestRequestByStatus(RequestStatus.LAB_TEST_IN_PROGRESS);

        //Implement this method
        //Create an object of CreateLabResult and call getCreateLabResult() to create the object. Pass the above created object as the parameter
        CreateLabResult labResult = getCreateLabResult(testRequest);

        //Create another object of the TestRequest method and explicitly update the status of this object
        // to be 'LAB_TEST_IN_PROGRESS'. Make use of updateLabTest() method from labRequestController class (Pass the previously created two objects as parameters)
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

        labRequestController.updateLabTest(testRequest.getRequestId(), labResult);

        //Use assertThat() methods to perform the following three comparisons
        //  1. the request ids of both the objects created should be same
        //  2. the status of the second object should be equal to 'LAB_TEST_COMPLETED'
        // 3. the results of both the objects created should be same. Make use of getLabResult() method to get the results.

        assertThat(testRequest.getRequestId(), equalTo(tr1.getRequestId()));
        assertThat(testRequest.getStatus(), equalTo(RequestStatus.LAB_TEST_COMPLETED));
        assertThat(testRequest.getLabResult(), equalTo(tr1.getLabResult()));

    }


    @Test
    @WithUserDetails(value = "tester")
    public void calling_updateLabTest_with_invalid_test_request_id_should_throw_exception(){

        TestRequest testRequest = getTestRequestByStatus(RequestStatus.LAB_TEST_IN_PROGRESS);


        //Implement this method

        //Create an object of CreateLabResult and call getCreateLabResult() to create the object. Pass the above created object as the parameter
        CreateLabResult labResult = getCreateLabResult(testRequest);

        // Create an object of ResponseStatusException . Use assertThrows() method and pass updateLabTest() method
        // of labRequestController with a negative long value as Id and the above created object as second parameter
        //Refer to the TestRequestControllerTest to check how to use assertThrows() method
        Long InvalidId= -334L;
        ResponseStatusException result = assertThrows(ResponseStatusException.class,()->{
            labRequestController.updateLabTest(InvalidId, labResult);
        });

        //Use assertThat() method to perform the following comparison
        //  the exception message should be contain the string "Invalid ID"
        assertThat(result.getMessage(),containsString("Invalid ID"));
    }

    @Test
    @WithUserDetails(value = "tester")
    public void calling_updateLabTest_with_invalid_empty_status_should_throw_exception(){

        TestRequest testRequest = getTestRequestByStatus(RequestStatus.LAB_TEST_IN_PROGRESS);

        //Implement this method

        //Create an object of CreateLabResult and call getCreateLabResult() to create the object. Pass the above created object as the parameter
        CreateLabResult labResult = getCreateLabResult(testRequest);

        // Set the result of the above created object to null.
        labResult.setResult(null);

        // Create an object of ResponseStatusException . Use assertThrows() method and pass updateLabTest() method
        // of labRequestController with request Id of the testRequest object and the above created object as second parameter
        ResponseStatusException result = assertThrows(ResponseStatusException.class,()->{
            labRequestController.updateLabTest(testRequest.getRequestId(), labResult);
        });

        //Refer to the TestRequestControllerTest to check how to use assertThrows() method


        //Use assertThat() method to perform the following comparison
        //  the exception message should be contain the string "ConstraintViolationException"
        assertThat(result.getMessage(),containsString("ConstraintViolationException"));

    }

    public CreateLabResult getCreateLabResult(TestRequest testRequest) {

        //Create an object of CreateLabResult and set all the values
        // Return the object
        CreateLabResult labResult = new CreateLabResult();
        labResult.setBloodPressure("100");
        labResult.setHeartBeat("94");
        labResult.setOxygenLevel("96");
        labResult.setTemperature("99");
        labResult.setComments("sample comments");
        labResult.setResult(TestStatus.POSITIVE);

        return labResult; // Replace this line with your code
    }

}