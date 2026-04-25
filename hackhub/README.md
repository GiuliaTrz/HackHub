# HackHub API Documentation

This document lists all the API endpoints available in the HackHub application.

## AccountBoundary
- **POST** `/api/account/registration` - Create a new account

## AuthenticationBoundary
- **POST** `/api/authentication` - Authenticate a user

## GradeBoundary
- **PATCH** `/api/grades/submission/{submissionId}` - Grade a submission

## HackathonCreationBoundary
- **POST** `/api/hackathon/creation` - Create a new hackathon
- **POST** `/api/hackathon/{hackathonId}/task` - Insert a task into a hackathon

## InfoBoundary
- **GET** `/api/info/hackathons` - Get all hackathons
- **GET** `/api/info/report` - Get hackathon report

## InfractionBoundary
- **POST** `/api/infraction/report` - Report an infraction
- **DELETE** `/api/infraction/{team}/expel` - Expel a team
- **PATCH** `/api/infraction/{team}/penalize` - Penalize a team
- **POST** `/api/infraction/handle` - Handle an infraction

## InvitationBoundary
- **POST** `/api/invitation/{team}/invite/{user}` - Invite a user to a team
- **DELETE** `/api/invitation/cancel/{invitation}` - Cancel an invitation

## InvitationReplyBoundary
- **POST** `/api/invitation/{invitation}/accept` - Accept an invitation
- **DELETE** `/api/invitation/{invitation}` - Decline an invitation

## PartecipationBoundary
- **DELETE** `/api/teamPartecipation/unsubscribeTeam/{team}` - Unsubscribe a team from hackathon

## SubmissionBoundary
- **POST** `/api/submission/send` - Send a submission
- **GET** `/api/submission/team/{teamId}` - Get team submissions

## TeamBoundary
- **POST** `/api/team/{hackathonId}/creation` - Create a team

## TeamLeaderChoiceBoundary
- **PATCH** `/api/teamLeader/{t}/choice` - Choose new team leader

## WinnerChoiceBoundary
- **PATCH** `/api/winner/{hackathonId}/auto` - Choose winner automatically
- **PATCH** `/api/winner/{hackathonId}/manual/{teamId}` - Choose winner manually
- **PATCH** `/api/winner/{hackathon}/proclaim` - Proclaim winner</content>
<parameter name="filePath">C:\Users\utente\Desktop\UNICAM\Ingegneria Del Software\Progetto\HackHub\hackhub\README.md
