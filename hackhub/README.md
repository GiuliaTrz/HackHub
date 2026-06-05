# HackHub API Documentation

This document provides a comprehensive guide to all API endpoints available in the HackHub application.

## API Endpoints

### Account Management
- **POST** `/api/account/registration` - Create a new user account
- **PUT** `/api/account/update` - Update user account information
- **DELETE** `/api/account/delete` - Delete user account

### Authentication
- **POST** `/api/authentication` - Authenticate user and get JWT token

### Hackathon Creation
- **POST** `/api/hackathon/creation` - Create a new hackathon
- **POST** `/api/hackathon/{hackathonId}/task` - Insert a task into a hackathon

### Hackathon Management
- **PUT** `/api/hackathon/{hackathonId}` - Update hackathon details
- **DELETE** `/api/hackathon/{hackathonId}` - Delete a hackathon

### Team Management
- **POST** `/api/team/{hackathonId}/creation` - Create a new team
- **PATCH** `/api/team/{teamId}` - Update team name
- **DELETE** `/api/team/{teamId}/members/{memberId}` - Remove a member from team

### Team Leadership
- **PATCH** `/api/teamLeader/{teamId}/choice` - Change team leader

### Team Participation
- **POST** `/api/team-participation/{teamId}/leave` - Leave a team
- **DELETE** `/api/teamPartecipation/unsubscribeTeam/{team}` - Unsubscribe from team

### Invitations
- **POST** `/api/invitation/{team}/invite/{user}` - Send team invitation to user
- **DELETE** `/api/invitation/cancel/{invitation}` - Cancel a pending invitation
- **POST** `/api/invitation/{invitation}/accept` - Accept team invitation
- **DELETE** `/api/invitation/{invitation}` - Decline team invitation

### Staff Management
- **POST** `/api/staff/{hackathonId}/mentors` - Add mentor to hackathon
- **DELETE** `/api/staff/{hackathonId}/mentors/{mentorId}` - Remove mentor from hackathon
- **POST** `/api/staff/{hackathonId}/staff/change-role` - Change staff member role

### Support Requests
- **GET** `/api/support/available-slots/{hackathon}` - Get available mentor support slots
- **POST** `/api/support/propose-call/{team}` - Propose support call to team
- **POST** `/api/support/send-aid-request` - Send aid request to mentors
- **GET** `/api/support/{hackathonId}` - Get all aid requests for hackathon
- **DELETE** `/api/support/{hackathonId}/teams/{teamId}` - Delete aid request for team

### Submissions
- **POST** `/api/submission/send` - Send task submission
- **GET** `/api/submission/all` - Get all team submissions for hackathon

### Evaluation & Grading
- **PATCH** `/api/evaluation/submission/{submissionId}` - Grade a submission
- **GET** `/api/evaluation/{teamId}` - View team evaluation/grade

### Infractions
- **POST** `/api/infraction/report` - Report infraction for team
- **POST** `/api/infraction/handle` - Handle infraction
- **PATCH** `/api/infraction/{team}/penalize` - Penalize team points
- **DELETE** `/api/infraction/{team}/expel` - Expel team from hackathon
- **DELETE** `/api/infraction/{hackathonId}/{infractionIndex}` - Delete specific infraction

### Winner Selection
- **PATCH** `/api/winner/{hackathonId}` - Proclaim hackathon winner
- **GET** `/api/winner/{hackathonId}/allTeams` - Get all teams in hackathon

### Prize Management
- **POST** `/api/prize/{hackathonId}/claim` - Claim prize money for winning team member

### Organizer Permissions
- **PATCH** `/api/organizer/permit/request` - Request permission to organize hackathons

### Hackathon Information
- **GET** `/api/info/hackathons` - Get list of all hackathons
- **GET** `/api/info/{hackathonId}/report` - Get hackathon report

---

## Quick Start Guide

### 1. Submission JSON Format

```json
{
  "teamId": "<UUID>",
  "fileName": "Submission1"
}
```

### 2. Hackathon Creation Process

#### Step 1: Request Organizer Permission
```json
{
  "fileName": "organizer_certificate"
}
```

#### Step 2: Create Hackathon
**Hackathon 1:**
```json
{
  "name": "HACKATHON1",
  "ruleBook": "Regulation Alpha",
  "expiredSubscriptionsDate": "2026-05-20",
  "maxTeamDimension": 4,
  "mentorsList": ["<MENTOR_UUID_1>", "<MENTOR_UUID_2>"],
  "moneyPrice": {
    "quantity": 100.00,
    "currency": "EUR"
  },
  "judge": "<JUDGE_UUID>",
  "reservation": {
    "location": {
      "name": "Central Headquarters",
      "province": "RM",
      "cap": "00100",
      "address": "Via Roma 1"
    },
    "timeInterval": {
      "startDate": "2026-06-01",
      "endDate": "2026-06-03"
    }
  }
}
```

**Hackathon 2:**
```json
{
  "name": "HACKATHON2",
  "ruleBook": "Regulation Alpha",
  "expiredSubscriptionsDate": "2026-05-20",
  "maxTeamDimension": 4,
  "mentorsList": ["<MENTOR_UUID_1>", "<MENTOR_UUID_2>"],
  "moneyPrice": {
    "quantity": 100.00,
    "currency": "EUR"
  },
  "judge": "<JUDGE_UUID>",
  "reservation": {
    "location": {
      "name": "Tech Park",
      "province": "MI",
      "cap": "20100",
      "address": "Corso Italia 10"
    },
    "timeInterval": {
      "startDate": "2026-07-10",
      "endDate": "2026-07-12"
    }
  }
}
```

**Hackathon 3:**
```json
{
  "name": "HACKATHON3",
  "ruleBook": "Regulation Alpha",
  "expiredSubscriptionsDate": "2026-05-20",
  "maxTeamDimension": 4,
  "mentorsList": ["<MENTOR_UUID_1>", "<MENTOR_UUID_2>"],
  "moneyPrice": {
    "quantity": 100.00,
    "currency": "EUR"
  },
  "judge": "<JUDGE_UUID>",
  "reservation": {
    "location": {
      "name": "Innovation Hub",
      "province": "TO",
      "cap": "10100",
      "address": "Via Torino 5"
    },
    "timeInterval": {
      "startDate": "2026-10-15",
      "endDate": "2026-10-18"
    }
  }
}
```

#### Step 3: Insert Task
**Endpoint:** `POST http://localhost:8080/api/hackathon/{hackathonId}/task`

```json
{
  "title": "TASK1",
  "description": "Task information and details",
  "template": {
    "fileName": "task_template"
  }
}
```

---

## Test Users

### Registration & Authentication
- **Registration:** `POST http://localhost:8080/api/account/registration`
- **Authentication:** `POST http://localhost:8080/api/authentication` (username and password only)

### Coordinator
```json
{
  "userName": "Coordinator",
  "userSurname": "Coordinator",
  "fiscalCode": "COORD123",
  "address": {
    "name": "Roma",
    "province": "RM",
    "cap": "00100",
    "address": "Via 1"
  },
  "email": "coord@test.it",
  "password": "password"
}
```

### Judge
```json
{
  "userName": "Judge",
  "userSurname": "Judge",
  "fiscalCode": "JUDGE123",
  "address": {
    "name": "Milano",
    "province": "MI",
    "cap": "20100",
    "address": "Via 2"
  },
  "email": "judge@test.it",
  "password": "password"
}
```

### Mentors
**Mentor 1:**
```json
{
  "userName": "Mentor1",
  "userSurname": "Mentor1",
  "fiscalCode": "MTR1123",
  "address": {
    "name": "Torino",
    "province": "TO",
    "cap": "10100",
    "address": "Via 3"
  },
  "email": "m1@test.it",
  "password": "password"
}
```

**Mentor 2:**
```json
{
  "userName": "Mentor2",
  "userSurname": "Mentor2",
  "fiscalCode": "MTR2123",
  "address": {
    "name": "Firenze",
    "province": "FI",
    "cap": "50100",
    "address": "Via 4"
  },
  "email": "m2@test.it",
  "password": "password"
}
```

### Team Leaders & Members

**Team 1 Leader:**
```json
{
  "userName": "TL1",
  "userSurname": "TL1",
  "fiscalCode": "TL1123",
  "address": {
    "name": "Napoli",
    "province": "NA",
    "cap": "80100",
    "address": "Via 5"
  },
  "email": "tl1@test.it",
  "password": "password"
}
```

**Team 1 Members:**
```json
{
  "userName": "TM1.1",
  "userSurname": "TM1.1",
  "fiscalCode": "TM11123",
  "address": {
    "name": "Bologna",
    "province": "BO",
    "cap": "40100",
    "address": "Via 6"
  },
  "email": "tm1.1@test.it",
  "password": "password"
}
```

```json
{
  "userName": "TM1.2",
  "userSurname": "TM1.2",
  "fiscalCode": "TM12123",
  "address": {
    "name": "Bologna",
    "province": "BO",
    "cap": "40100",
    "address": "Via 7"
  },
  "email": "tm1.2@test.it",
  "password": "password"
}
```

```json
{
  "userName": "TM1.3",
  "userSurname": "TM1.3",
  "fiscalCode": "TM13123",
  "address": {
    "name": "Bologna",
    "province": "BO",
    "cap": "40100",
    "address": "Via 8"
  },
  "email": "tm1.3@test.it",
  "password": "password"
}
```

**Team 2 Leader:**
```json
{
  "userName": "TL2",
  "userSurname": "TL2",
  "fiscalCode": "TL2123",
  "address": {
    "name": "Bari",
    "province": "BA",
    "cap": "70100",
    "address": "Via 9"
  },
  "email": "tl2@test.it",
  "password": "password"
}
```

**Team 2 Members:**
```json
{
  "userName": "TM2.1",
  "userSurname": "TM2.1",
  "fiscalCode": "TM21123",
  "address": {
    "name": "Genova",
    "province": "GE",
    "cap": "16100",
    "address": "Via 10"
  },
  "email": "tm2.1@test.it",
  "password": "password"
}
```

```json
{
  "userName": "TM2.2",
  "userSurname": "TM2.2",
  "fiscalCode": "TM22123",
  "address": {
    "name": "Genova",
    "province": "GE",
    "cap": "16100",
    "address": "Via 11"
  },
  "email": "tm2.2@test.it",
  "password": "password"
}
```

```json
{
  "userName": "TM2.3",
  "userSurname": "TM2.3",
  "fiscalCode": "TM23123",
  "address": {
    "name": "Genova",
    "province": "GE",
    "cap": "16100",
    "address": "Via 12"
  },
  "email": "tm2.3@test.it",
  "password": "password"
}
```

### Bystander User
```json
{
  "userName": "BYSTANDER",
  "userSurname": "BYSTANDER",
  "fiscalCode": "BYST123",
  "address": {
    "name": "Palermo",
    "province": "PA",
    "cap": "90100",
    "address": "Via 13"
  },
  "email": "byst@test.it",
  "password": "password"
}
```

** Team leader change **

### BODY
```json
"<UUID of new team leader>"
```
** Staff role change **

### BODY
```json
{
  "toChange": "<UUID of staff member>",
  "role": "MENTOR || JUDGE"
}
```

** Infraction **

### BODY
```json
{
  "description": "tried copying the solution from another team",
  "type": "AI",
  "team": "<ID TEAM>"
}
```

** Evaluation **

### BODY


```json
{
  "grade" : 5,
  "writtenEvaluation" : "Good job, but you can improve the UI"
}
```