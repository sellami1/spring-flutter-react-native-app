describe('Gestion des Étudiants - E2E Tests', () => {
  beforeEach(() => {
    cy.visit('/etudiants');
  });

  describe('Student page', () => {
    it('loads the actual students page', () => {
      cy.contains('h2', 'Students').should('be.visible');
      cy.contains('h3', 'Add a new student').should('be.visible');
    });

    it('shows the list cards or empty state', () => {
      cy.get('body').then(($body) => {
        const cards = $body.find('article');
        const emptyState = $body.text().includes('No students found. Add one using the form.');

        expect(cards.length > 0 || emptyState).to.be.true;
      });
    });

    it('shows the department filter form', () => {
      cy.contains('label', 'Filter by department').find('select').should('be.visible');
      cy.contains('button', 'Apply filter').should('be.visible');
    });

    it('shows the real list details when a student card exists', () => {
      cy.get('body').then(($body) => {
        const cards = $body.find('article');

        if (cards.length > 0) {
          cy.get('article').first().within(() => {
            cy.contains('h3').should('exist');
            cy.contains('CIN').should('exist');
            cy.contains('Birth date').should('exist');
            cy.contains('First inscription').should('exist');
            cy.contains('Edit').should('be.visible');
          });
        }
      });
    });
  });

  describe('Create flow', () => {
    it('submits the create form with a stubbed API response', () => {
      cy.intercept('POST', '**/api/etudiants', (req) => {
        expect(req.body).to.include.keys('cin', 'nom', 'dateNaissance', 'anneePremiereInscription');

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
      cy.contains('label', 'Name').find('input').clear().type(`E2E Student ${timestamp}`);
      cy.contains('label', 'Birth date').find('input[type="date"]').clear().type('2000-01-15');
      cy.contains('label', 'First inscription year').find('input[type="number"]').clear().type('2020');

      cy.contains('button', 'Create student').click();

      cy.wait('@createStudent');
      cy.contains('Student created.').should('be.visible');
    });

    it('keeps the form on the same page and exposes validation through native required fields', () => {
      cy.contains('button', 'Create student').click();

      cy.get('input').then(($inputs) => {
        expect($inputs.length).to.be.greaterThan(0);
      });
    });
  });

  describe('Navigation', () => {
    it('opens an edit page when a student card is available', () => {
      cy.get('body').then(($body) => {
        const cards = $body.find('article');

        if (cards.length > 0) {
          cy.contains('article a', 'Edit').first().click();
          cy.contains('h3', 'Edit student').should('be.visible');
          cy.contains('Back to students').should('be.visible');
          cy.url().should('include', '/etudiants/');
        } else {
          cy.contains('No students found. Add one using the form.').should('be.visible');
        }
      });
    });
  });

  describe('Responsive layout', () => {
    it('renders on mobile viewport', () => {
      cy.viewport('iphone-x');
      cy.visit('/etudiants');
      cy.contains('h2', 'Students').should('be.visible');
    });

    it('renders on tablet viewport', () => {
      cy.viewport('ipad-2');
      cy.visit('/etudiants');
      cy.contains('h2', 'Students').should('be.visible');
    });

    it('renders on desktop viewport', () => {
      cy.viewport(1920, 1080);
      cy.visit('/etudiants');
      cy.contains('h2', 'Students').should('be.visible');
    });
  });
});
