package com.customer;

import org.springframework.beans.factory.annotation.Autowired;

import com.customer.model.CustomerFactory;
import com.customer.setup.IntegrationTest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class CustomerControllerTests extends IntegrationTest {

    private static final int CUSTOMER_ID_ONE = 1;

    @Autowired
    private CustomerFactory customerFactory;

    @BeforeEach
    public void init() {
        customerFactory.buildAndPersistTestData(3);
    }

    @AfterEach
    public void tearDown() {
        customerFactory.wipeTestData();
    }

    @Test
    void GIVEN_expectedCustomer_WHEN_getRequestToCustomerById_THEN_ok() {

        // given
        final Customer expectedCustomer = customerService.findCustomerById(CUSTOMER_ID_ONE);

        // when
        webTestClient
                .get()
                .uri("/customers/" + CUSTOMER_ID_ONE)

                // then
                .exchange()
                .expectStatus()
                .isOk()

                // and
                .expectBody(Customer.class)
                .isEqualTo(expectedCustomer);
    }
//    @Test
//    void GIVEN_expectedCustomers_WHEN_getRequestToCustomers_THEN_ok() {
//
//        // given
//        final List<Customer> expectedCustomers = customerFactory.getDefaultTestCustomers(3);
//
//        // when
//        webTestClient
//            .get()
//                .uri("/customers/")
//
//            // then
//            .exchange()
//                .expectStatus()
//                    .isOk()
//            .expectBodyList(Customer.class)
//                .hasSize(3)
//                .isEqualTo(expectedCustomers);
//    }
//
//    @Test
//    void GIVEN_expectedCustomer_WHEN_getRequestToCustomerById_THEN_ok() {
//
//        // given
//        final Customer expectedCustomer = customerDao.findCustomerById(CUSTOMER_ID_ONE);
//
//        // when
//        webTestClient
//            .get()
//            .uri("/customers/" + CUSTOMER_ID_ONE)
//
//            // then
//            .exchange()
//            .expectStatus()
//                .isOk()
//            .expectBody(Customer.class)
//                .isEqualTo(expectedCustomer);
//    }

    @Test
    void GIVEN_nonExistingId_WHEN_getRequestToCustomerById_THEN_notFound() {

        // given
        final long nonExistingId = 100;

        // when
        webTestClient
            .get()
            .uri("/customers/" + nonExistingId)

            // then
            .exchange()
            .expectStatus()
                .isNotFound()
            .expectBody()
                .jsonPath("$.url").value(Matchers.containsString("/customers/" + nonExistingId))
                .jsonPath("$.message").value(Matchers.containsString(nonExistingId + " cannot be found"))
                .jsonPath("$.errorCode").value(Matchers.equalTo("NOT_FOUND"))
                .jsonPath("$.timestamp").isNotEmpty();
    }

    @Test
    void GIVEN_nonExistingId_WHEN_deleteRequestToCustomerById_THEN_notFound() {

        // given
        final long nonExistingId = 100;

        // when
        webTestClient
            .delete()
            .uri("/customers/" + nonExistingId)
            // then
            .exchange()
            .expectStatus()
                .isNotFound()
            .expectBody()
                .jsonPath("$.url").value(Matchers.containsString("/customers/" + nonExistingId))
                .jsonPath("$.message").value(Matchers.containsString("entity with id " + nonExistingId))
                .jsonPath("$.errorCode").value(Matchers.equalTo("NOT_FOUND"))
                .jsonPath("$.timestamp").isNotEmpty();
    }

    // ToDO: enable db transaction rollback per testcase
//    @Test
//    void GIVEN_existingCustomerId_WHEN_deleteRequestToCustomerById_THEN_noContent() {
//        // given
//        final Customer expectedCustomer = customerDao.findCustomerById(CUSTOMER_ID_ONE);
//
//        // when
//        webTestClient
//            .delete()
//            .uri("/customers/" + CUSTOMER_ID_ONE)
//
//            // then
//            .exchange()
//            .expectStatus()
//            .isNoContent()
//            .expectBody()
//                .isEmpty();
//
//    }

}
