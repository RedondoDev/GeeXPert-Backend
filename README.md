<div id="Badges1">
    <img alt="Stars Count" src="https://img.shields.io/github/stars/RedondoDev/GeeXPert-Backend?style=flat-square&color=yellow">
    <img alt="License" src="https://img.shields.io/badge/License-MIT-purple?style=flat-square">    
</div>
<div id="Badges2">
    <img alt="Java" src="https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white" height="20">
    <img alt="Spring Boot" src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white" height="20">
    <img alt="MySQL" src="https://img.shields.io/badge/MySQL-005C84?style=for-the-badge&logo=mysql&logoColor=white" height="20">
    <img alt="Docker" src="https://img.shields.io/badge/Docker-2CA5E0?style=for-the-badge&logo=docker&logoColor=white" height="20">
    <img alt="Postman" src="https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=Postman&logoColor=white" height="20">
</div>

<br>

*GeeXPert Backend acts as the server-side component for the GeeXPert ecosystem. While the main repository will cover the full application (frontend, backend, etc.), this repository is focused exclusively on backend logic, APIs, and data processing. If you want to see the parent repository click on the following link <a href="https://github.com/RedondoDev/GeeXPert">GeeXPert</a>*

# GeeXPert Backend

This repository contains the backend code for the GeeXPert application. The backend is built with Java and Spring Boot, and its main responsibilities are user authentication, game information integration,  user game collection management, and personalized recommendations.

## Table of Contents
- [Features](#features)
- [Technologies](#technologies)
- [API-Endpoints](#api-endpoints)
- [Author](#author)
- [License](#license)

## Features

### 1. User Authentication & Authorization
- Implements registration and login endpoints (`/auth/signup`, `/auth/signin`), using Spring Security and JWT tokens.
- Passwords are securely hashed with BCrypt.
- Stateless authentication for secure API access.

### 2. Game Information Integration
- Integrates with the <a href="https://api-docs.igdb.com/#getting-started">IGDB API</a> to fetch and cache game data (top-rated, trending, search by name, and details by ID).
- Endpoints like `/games/top`, `/games/trending`, `/games/search`, `/games/{id}`.

### 3. User Game Collection
- Users can add or remove games to/from their personal collection.
- The state of each game on User Collection can be swapped between pending, playing and completed.
- The backend ensures no duplicates and handles game state for each user.
- Endpoints allow retrieving and managing the user's game library.

### 4. Personalized Game Recommendations
- Recommends games based on a user's collection and preferences, leveraging models like `llama3.2:3b`.
- Integrates with <a href="https://github.com/ollama/ollama">Ollama</a> for AI-powered recommendations.

### 5. Security
- Uses JWT tokens for API protection.
- Custom security configurations restrict access to sensitive endpoints and enable CORS for frontend integration.

## Technologies

<ul>
    <li>Java</li>
    <li>Spring Boot</li>
    <ul>
      <li>Spring Web</li>
      <li>Spring Security</li>
      <ul>
        <li>JWT Authentication</li>
      </ul>
      <li>Spring Data JPA</li>
      <ul>
        <li>Hibernate</li>
      </ul>
    </ul>
    <li>MySQL</li>
    <li>Ollama</li>
    <ul>
      <li>llama3.2:3b</li>
    </ul>
    <li>Maven</li>
    <li>Postman</li>
    <li>Docker</li>
    </ul>
</ul>

## API-Endpoints

- `POST /auth/signup` - Register a new user
- `POST /auth/signin` - Authenticate and receive a JWT token
- `GET /games/top` - Get top-rated games
- `GET /games/trending` - Get trending games
- `GET /games/search?name=...` - Search games by name
- `GET /games/explore` - Explore games
- `GET /games/{id}` - Get details of a specific game
- `GET /collection/games` - Get the user's game collection
- `POST /collection/add` - Add a game to the user's collection
- `PUT /collection/{id}/state` - Change the status of a game from the user's collection
- `DELETE /collection/{gameId}` - Remove a game from the user's collection
- `GET /recommendations/ai-assistant/{id}` - Get personalized recommendations

## Author

<table>
    <tr>
        <th>RedondoDEV</th>    
    </tr>
    <tr>        
        <td>
            <a href="https://github.com/RedondoDev">
                <img src="https://avatars.githubusercontent.com/u/163606882?v=1" width="110px"> 
            </a>
        </td>
    </tr>
</table>

## License

This project is licensed under the [MIT License](https://github.com/RedondoDev/GeeXPert-Backend/blob/master/README.md)  
<img alt="License" src="https://img.shields.io/badge/License-MIT-purple?style=flat-square">    
