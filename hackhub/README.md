# HackHub API Documentation

This document provides a comprehensive guide to all API endpoints available in the HackHub application.

## API Endpoints

### Account Management
- **POST** `/api/account/registration` - Create a new user account

### Authentication
- **POST** `/api/authentication` - Authenticate a user and obtain JWT token

### Hackathon Management
- **POST** `/api/hackathon/creation` - Create a new hackathon
- **POST** `/api/hackathon/{hackathonId}/task` - Insert a task into a hackathon
- **GET** `/api/info/hackathons` - Retrieve all hackathons
- **GET** `/api/info/{hackathonId}/report` - Retrieve hackathon report

### Grades & Submissions
- **PATCH** `/api/grades/submission/{submissionId}` - Grade a submission
- **POST** `/api/submission/send` - Send a team submission
- **GET** `/api/submission/team/{teamId}` - Retrieve team submissions

### Infractions Management
- **POST** `/api/infraction/report` - Report an infraction
- **POST** `/api/infraction/handle` - Handle an infraction
- **PATCH** `/api/infraction/{team}/penalize` - Penalize a team
- **DELETE** `/api/infraction/{team}/expel` - Expel a team

### Team Management
- **POST** `/api/team/{hackathonId}/creation` - Create a new team
- **DELETE** `/api/teamParticipation/unsubscribeTeam/{team}` - Unsubscribe team from hackathon
- **PATCH** `/api/teamLeader/{t}/choice` - Change team leader

### Invitations
- **POST** `/api/invitation/{team}/invite/{user}` - Invite user to team
- **POST** `/api/invitation/{invitation}/accept` - Accept team invitation
- **DELETE** `/api/invitation/cancel/{invitation}` - Cancel invitation
- **DELETE** `/api/invitation/{invitation}` - Decline invitation

### Support & Organizer
- **PATCH** `/api/organizer/permit/request` - Request organizer permission
- **GET** `/api/support/available-slots/{hackathon}` - Get available support slots
- **POST** `/api/support/propose-call/{team}` - Propose a support call
- **POST** `/api/support/send-aid-request` - Send aid request

### Winner Selection
- **PATCH** `/api/winner/{hackathonId}/auto` - Automatically select winner
- **PATCH** `/api/winner/{hackathonId}/manual/{teamId}` - Manually select winner
- **PATCH** `/api/winner/{hackathon}/proclaim` - Proclaim the winner


---

## Quick Start Guide

### 1. Submission JSON Format

```json
{
  "teamId": "<UUID>",
  "taskId": "<UUID>",
  "fileName": "Submission1"
}
```

### 2. Hackathon Creation Process

#### Step 1: Request Organizer Permission
**Endpoint:** `PATCH http://localhost:8080/api/organizer/permit/request`

```json
{
  "fileName": "organizer_certificate"
}
```

#### Step 2: Create Hackathon
**Endpoint:** `POST http://localhost:8080/api/hackathon/creation`

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
