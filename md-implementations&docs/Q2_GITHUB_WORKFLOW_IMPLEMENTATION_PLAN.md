# Q2 — GitHub Workflow Implementation Plan

## Overview
Configure GitHub branch protection, templates, and review conventions for the project. This ensures code quality and team consistency throughout Sprint 3 development.

---

## Task 1: Enable Branch Protection

### Objective
Protect `main` and `version-3` branches from direct pushes and require approved reviews before merge.

### Steps

1. **Navigate to Repository Settings**
   - Go to your GitHub repository
   - Click `Settings` tab

2. **Configure Protection for `main` Branch**
   - Select `Branches` in the left sidebar
   - Click `Add rule` under "Branch protection rules"
   - Pattern name: `main`
   - Check the following options:
     - ✅ Require a pull request before merging
     - ✅ Require approvals (set to 1 approval minimum)
     - ✅ Restrict who can push to matching branches (select your team)
     - ✅ Require conversation resolution before merging
   - Click `Create`

3. **Configure Protection for `version-3` Branch**
   - Click `Add rule` again
   - Pattern name: `version-3`
   - Apply the same settings as step 2
   - Click `Create`

### Acceptance Criteria
- [ ] Pushing directly to `main` is blocked
- [ ] Pushing directly to `version-3` is blocked
- [ ] Pull requests require at least 1 approval before merge
- [ ] Dismissal of pull request reviews is prevented

---

## Task 2: Create Issue Templates

### Objective
Standardize issue reporting with templates for bug reports and feature requests.

### 2.1 — Bug Report Template

**File Path:** `.github/ISSUE_TEMPLATE/bug_report.md`

```markdown
---
name: Bug Report
about: Report a bug to help us improve
title: '[BUG] Brief description'
labels: 'bug'
---

## Description
A clear and concise description of what the bug is.

## Steps to Reproduce
1. ...
2. ...
3. ...

## Expected Behavior
What you expected to happen.

## Actual Behavior
What actually happened.

## Environment
- **OS:** (e.g., Linux, macOS, Windows)
- **Version:** (e.g., v1.0.0)
- **Branch:** (e.g., version-3)

## Logs / Screenshots
Attach logs, error messages, or screenshots if applicable.

## Related Jira Ticket
Link to Jira ticket: `JIRA-XXX`
```

**Steps:**
1. Create `.github/ISSUE_TEMPLATE/` directory if it doesn't exist
2. Create `bug_report.md` with the content above
3. Commit: `git add .github/ISSUE_TEMPLATE/bug_report.md && git commit -m "Add bug report template"`

### 2.2 — Feature Request Template

**File Path:** `.github/ISSUE_TEMPLATE/feature_request.md`

```markdown
---
name: Feature Request
about: Suggest a new feature or enhancement
title: '[FEATURE] Brief description'
labels: 'enhancement'
---

## Problem Statement
Describe the problem this feature would solve.

## Proposed Solution
Describe the solution or feature you'd like to see implemented.

## Alternatives Considered
Any alternative solutions or features considered.

## Additional Context
Any additional context (screenshots, references, etc.).

## Related Jira Ticket
Link to Jira ticket: `JIRA-XXX`
```

**Steps:**
1. Create `feature_request.md` in `.github/ISSUE_TEMPLATE/`
2. Commit: `git add .github/ISSUE_TEMPLATE/feature_request.md && git commit -m "Add feature request template"`

### Acceptance Criteria
- [ ] `.github/ISSUE_TEMPLATE/bug_report.md` exists and contains bug template
- [ ] `.github/ISSUE_TEMPLATE/feature_request.md` exists and contains feature template
- [ ] Both templates include Jira ticket reference field
- [ ] Templates appear in GitHub issue creation dropdown

---

## Task 3: Create Pull Request Template

### Objective
Standardize PR descriptions with sections for context, testing, and review checklist.

**File Path:** `.github/pull_request_template.md`

```markdown
## Description
<!-- Provide a brief description of the changes in this PR -->

### Related Jira Ticket
- **Ticket:** DM-XX (replace with actual ticket number)
- **Sprint:** Sprint 3
- **Type:** Task/Story/Bug

## Changes Made
- [ ] Change 1
- [ ] Change 2
- [ ] Change 3

## How Has This Been Tested?
<!-- Describe the tests you ran and how to reproduce them -->

### Test Coverage
- [ ] Unit tests added/updated
- [ ] Integration tests added/updated
- [ ] Manual testing completed

## Screenshots (if applicable)
<!-- Add screenshots or GIFs if UI changes are involved -->

## Breaking Changes?
- [ ] No breaking changes
- [ ] Yes, breaking changes (describe below)

## Checklist
- [ ] Code follows the project's style guidelines
- [ ] Self-review completed
- [ ] Comments added for complex logic
- [ ] Documentation updated (README, Swagger, etc.)
- [ ] No new warnings generated
- [ ] Tests pass locally
- [ ] Branch is up to date with `version-3`

## Reviewers
<!-- Tag reviewers if applicable -->
@mention-reviewer
```

**Steps:**
1. Create `.github/pull_request_template.md` at repo root
2. Commit: `git add .github/pull_request_template.md && git commit -m "Add pull request template"`

### Acceptance Criteria
- [ ] `.github/pull_request_template.md` exists
- [ ] Template includes Description section
- [ ] Template includes Related Jira Ticket section
- [ ] Template includes test coverage checklist
- [ ] Template appears automatically when creating PRs

---

## Task 4: Update README with Review Conventions

### Objective
Document code review standards and expectations for the team.

**File Path:** `README.md` (add new section)

### Content to Add

```markdown
## Code Review Process & Conventions

### Overview
All pull requests must go through a mandatory review process before merging into `main` or `version-3` branches. This ensures code quality, knowledge sharing, and consistency across the codebase.

### Review Requirements
- **Minimum Approvals:** At least 1 approval required before merge
- **Review Deadline:** Reviews should be completed within **48 hours** of PR creation
- **Mandatory Checks:** All CI/CD checks and tests must pass
- **Conversation Resolution:** All review comments must be resolved before merge

### Review Workflow

1. **Create a Branch**
   ```bash
   git checkout version-3
   git checkout -b feature/your-feature-name
   ```

2. **Create a Pull Request**
   - Push your changes and create a PR targeting `version-3`
   - Fill out the PR template completely
   - Link the related Jira ticket
   - Request reviewers

3. **Address Feedback**
   - Reviewers will add comments and suggestions
   - Address all **blocking comments** before merge
   - Push follow-up commits to the same branch
   - Reply to comments explaining changes

4. **Approval & Merge**
   - Once approved and all checks pass, merge the PR
   - Delete the feature branch after merging
   - Update the related Jira ticket to "Done"

### Reviewer Responsibilities

- **Understand the Context:** Read the PR description and related Jira ticket
- **Review Code Quality:** Check for readability, maintainability, and adherence to project standards
- **Verify Tests:** Ensure tests cover the changes appropriately
- **Check for Regressions:** Consider potential side effects or breaking changes
- **Be Constructive:** Provide helpful, specific feedback with suggestions for improvement
- **Respect Timeline:** Aim to complete reviews within 48 hours

### Best Practices

#### For Authors
- Keep PRs focused and reasonably sized (aim for <400 lines of code)
- Provide clear, descriptive commit messages
- Include relevant unit and integration tests
- Update documentation if needed
- Run tests locally before pushing

#### For Reviewers
- Approve only when you're confident in the changes
- Use "Request Changes" for blocking issues
- Use "Comment" for suggestions or discussions
- Ask questions if you don't understand the rationale
- Acknowledge good code with positive feedback

### Labels
- `bug` — Bug fix
- `enhancement` — New feature or improvement
- `documentation` — Documentation update
- `devops` — DevOps or infrastructure changes
- `testing` — Test-related changes
- `WIP` — Work in progress (do not merge)
- `blocked` — Blocked by another PR or issue

### Escalation
If a review is not completed within 48 hours:
1. Send a reminder to the reviewer
2. Contact the team lead if still unresolved after 24 more hours
3. In urgent cases, escalate to the project manager

---
```

**Steps:**
1. Open `README.md`
2. Navigate to the end of the file (or find an appropriate "Development" section)
3. Add the "Code Review Process & Conventions" section
4. Commit: `git add README.md && git commit -m "Add code review conventions to README"`

### Acceptance Criteria
- [ ] README.md contains a "Code Review Process & Conventions" section
- [ ] Section describes the 48-hour review deadline
- [ ] Section lists requirements for blocking comments resolution
- [ ] Section includes reviewer and author responsibilities
- [ ] Section includes best practices and labels guide

---

## Implementation Checklist

### Phase 1: Branch Protection (Complete First)
- [ ] Task 1.1: Enable protection on `main` branch
- [ ] Task 1.2: Enable protection on `version-3` branch
- [ ] Task 1.3: Verify protection is working

### Phase 2: Templates (Complete After Branch Protection)
- [ ] Task 2.1: Create `.github/ISSUE_TEMPLATE/bug_report.md`
- [ ] Task 2.2: Create `.github/ISSUE_TEMPLATE/feature_request.md`
- [ ] Task 2.3: Create `.github/pull_request_template.md`
- [ ] Task 2.4: Test templates by creating a sample issue/PR

### Phase 3: Documentation (Complete Last)
- [ ] Task 4.1: Update README.md with review conventions
- [ ] Task 4.2: Review content for clarity and accuracy
- [ ] Task 4.3: Verify all links and references are correct

### Verification
- [ ] All template files exist in `.github/` directory
- [ ] Branch protection rules are active
- [ ] README review section is visible and comprehensive
- [ ] Team has been notified of new workflow

---

## Timeline & Effort Estimation

| Task | Effort | Timeline |
|------|--------|----------|
| Branch Protection Setup | 15 min | 2026-05-02 |
| Create Issue Templates | 20 min | 2026-05-02 |
| Create PR Template | 15 min | 2026-05-02 |
| Update README | 20 min | 2026-05-03 |
| **Total** | **~70 min** | **2026-05-03** |

---

## Notes

- Branch protection cannot be reverted without repository admin access
- Templates will appear in GitHub UI automatically once committed
- Review the conventions section with your team to ensure alignment
- Update templates if the team identifies needed adjustments
