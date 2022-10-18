package com.backbase.accelerators.fics

class TestData {
    private static final String RESOURCES_ROOT = 'src/test/resources'

    static final String mortgageAccountResponse() {
        return new File("${RESOURCES_ROOT}/get-mortage-account-response-success.xml").getText()
    }

    static final String mortgageTransactionResponse() {
        return new File("${RESOURCES_ROOT}/get-mortage-transactions-response-success.xml").getText()
    }
}
