// Cypress support file for E2E tests
// Add global commands and hooks here

beforeEach(() => {
  // Reset cache before each test
  cy.clearAllCookies();
  cy.clearAllLocalStorage();
});
