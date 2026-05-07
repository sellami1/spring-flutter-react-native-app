describe('Gestion des Étudiants - Smoke Tests', () => {
  beforeEach(() => {
    cy.visit('/etudiants');
  });

  it('loads the students page with the real heading and create form', () => {
    cy.contains('h2', 'Students').should('be.visible');
    cy.contains('h3', 'Add a new student').should('be.visible');
  });

  it('shows the real student form fields', () => {
    cy.contains('label', 'CIN').find('input').should('be.visible');
    cy.contains('label', 'Name').find('input').should('be.visible');
    cy.contains('label', 'Birth date').find('input[type="date"]').should('be.visible');
    cy.contains('label', 'First inscription year').find('input[type="number"]').should('be.visible');
    cy.contains('button', 'Create student').should('be.visible');
  });

  it('shows the department filter controls', () => {
    cy.contains('label', 'Filter by department').find('select').should('be.visible');
    cy.contains('button', 'Apply filter').should('be.visible');
  });

  it('submits the create form without mutating the remote app', () => {
    cy.intercept('POST', '**/api/etudiants', (req) => {
      expect(req.body).to.have.property('cin');
      expect(req.body).to.have.property('nom');
      expect(req.body).to.have.property('dateNaissance');
      expect(req.body).to.have.property('anneePremiereInscription');

      req.reply({
        statusCode: 201,
        body: {
          id: 999999,
          ...req.body,
          age: 0,
        },
      });
    }).as('createStudent');

    const timestamp = Date.now();

    cy.contains('label', 'CIN').find('input').clear().type(`CIN${timestamp}`);
    cy.contains('label', 'Name').find('input').clear().type(`Smoke Student ${timestamp}`);
    cy.contains('label', 'Birth date').find('input[type="date"]').clear().type('2000-01-15');
    cy.contains('label', 'First inscription year').find('input[type="number"]').clear().type('2020');

    cy.contains('button', 'Create student').click();

    cy.wait('@createStudent');
    cy.contains('Student created.').should('be.visible');
  });

  it('opens the edit page when a student card exists', () => {
    cy.get('body').then(($body) => {
      const cards = $body.find('article');

      if (cards.length > 0) {
        cy.contains('article a', 'Edit').first().click();
        cy.contains('h3', 'Edit student').should('be.visible');
        cy.contains('Back to students').should('be.visible');
      } else {
        cy.contains('No students found. Add one using the form.').should('be.visible');
      }
    });
  });
});
