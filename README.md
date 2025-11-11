# Bajaj Finserv Health – Java Qualifier (Spring Boot)

This project automates the **Bajaj Finserv Health Hiring Challenge** submission using **Spring Boot**.

---

## 👩‍💻 Candidate Details
- **Name:** Sanika A M  
- **Reg No:** PES2UG22CS500  
- **Email:** sanika@example.com  
- **Question Assigned:** Question 2 (Even Reg No)

---

## ⚙️ Project Overview

This Spring Boot app automatically:
1. Registers a webhook using Bajaj’s API (`generateWebhook/JAVA`).
2. Receives the webhook URL and access token.
3. Submits the final SQL query for the assigned question to that webhook.

---

## 🧩 SQL Solution – Question 2

**Question Statement:**
> You are required to calculate the number of employees who are younger than each employee, grouped by their respective departments.

**Final SQL Query:**
```sql
SELECT E1.EMP_ID, E1.FIRST_NAME, E1.LAST_NAME, D.DEPARTMENT_NAME,
       COUNT(E2.EMP_ID) AS YOUNGER_EMPLOYEES_COUNT
FROM EMPLOYEE E1
JOIN DEPARTMENT D ON E1.DEPARTMENT = D.DEPARTMENT_ID
LEFT JOIN EMPLOYEE E2 ON E1.DEPARTMENT = E2.DEPARTMENT
AND E2.DOB > E1.DOB
GROUP BY E1.EMP_ID, E1.FIRST_NAME, E1.LAST_NAME, D.DEPARTMENT_NAME
ORDER BY E1.EMP_ID DESC;
