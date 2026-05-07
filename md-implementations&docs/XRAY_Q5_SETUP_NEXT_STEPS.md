# Q5 Xray Setup - Next Steps

## 1) Add GitHub secrets
In your GitHub repository settings, add:
- `XRAY_TOKEN`: Bearer token for Xray Cloud API
- `XRAY_PROJECT_KEY`: Jira project key (example: `PROJ`)

## 2) Commit workflow
The workflow file is:
- `.github/workflows/test-and-report.yml`

Push this file to branch `version-4`.

## 3) Trigger pipeline
Open Actions tab and run:
- `Test and Report to Xray` (workflow_dispatch), or
- push a commit / open a PR

## 4) Verify in Xray/Jira
After the run:
- A Test Execution should appear in your Jira project
- Imported test cases should show pass/fail status

## 5) Link governance entities (manual in Jira)
- Create/update Xray Test issues for key JUnit tests
- Link them to Sprint 4 stories via Test Coverage
- Add them to Sprint 4 Test Plan

## Troubleshooting
- If upload fails with 401/403: regenerate `XRAY_TOKEN`
- If upload fails with "No JUnit XML reports found": check Maven test output under `rest-spring-api/target/surefire-reports/`
- If tests fail and no report exists: inspect workflow logs in Actions
