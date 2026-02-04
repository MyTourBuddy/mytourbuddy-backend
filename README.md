<div style="margin-bottom: 20px">
<img src="./assets/mytourbuddy-bg.svg" alt="tourbuddylogo" height="100">
</div>

# MyTourBuddy - Backend

**Your Personal Tour Buddy 🦧**

MyTourBuddy is a comprehensive travel platform designed to connect tourists with local guides for personalized, immersive travel experiences. Built as a campus project, this app empowers users to discover packages, book tours, leave reviews, and interact with an AI-powered buddy for travel recommendations. Whether you're a tourist seeking adventure or a guide sharing your expertise, MyTourBuddy makes travel planning seamless and fun.

## Tech Stack

| Category | Technologies                                                       |
| -------- | ------------------------------------------------------------------ |
| Frontend | Next.js 16, TypeScript, Tailwind CSS v4, shadcn/ui, TanStack Query |
| Backend  | Spring Boot                                                        |
| Database | MongoDB                                                            |
| Tools    | Maven                                                              |

## Prerequisites

Before running the project, ensure you have the following installed:

- **Java**: 17 or higher
- **Maven**: 3.6+ (for building the project)
- **MongoDB**: Local instance or cloud (e.g., MongoDB Atlas)

## Installation & Setup

1. **Clone the Repository**:

   ```bash
   git clone https://github.com/yourusername/mytourbuddy-backend.git
   cd mytourbuddy-backend
   ```

2. **Environment Configuration**:
   - Create a `.env` file in the root directory.
   - Add the following variables (use your actual values):
     ```
     MONGO_URI=mongodb+srv://your-username:your-password@your-cluster.mongodb.net/your-db?appName=YourApp
     JWT_SECRET=your-jwt-secret-key
     JWT_EXPIRATION=259200000
     FRONTEND_URL=http://localhost:3000
     GEMINI_API_KEY=your-gemini-api-key
     ```

3. **Build the Project**:

   ```bash
   ./mvnw clean install
   ```

4. **Run the Application**:
   ```bash
   ./mvnw spring-boot:run
   ```
   The API will be available at `http://localhost:8080`.

## API Endpoints

| Method              | Endpoint                             | Description             |
| ------------------- | ------------------------------------ | ----------------------- |
| **Welcome**         |                                      |                         |
| GET                 | /                                    | Welcome message         |
| **Authentication**  |                                      |                         |
| POST                | /api/v1/auth/login                   | User login              |
| POST                | /api/v1/auth/register                | User registration       |
| POST                | /api/v1/auth/register-admin          | Admin registration      |
| **Users**           |                                      |                         |
| GET                 | /api/v1/users                        | Get all users           |
| GET                 | /api/v1/users/{id}                   | Get user by ID          |
| PUT                 | /api/v1/users/{id}                   | Update user             |
| DELETE              | /api/v1/users/{id}                   | Delete user             |
| **Packages**        |                                      |                         |
| GET                 | /api/v1/packages                     | Get all packages        |
| GET                 | /api/v1/packages/{id}                | Get package by ID       |
| GET                 | /api/v1/packages/guides/{guideId}    | Get packages by guide   |
| GET                 | /api/v1/packages/search              | Search packages         |
| POST                | /api/v1/packages                     | Create package          |
| PUT                 | /api/v1/packages/{id}                | Update package          |
| DELETE              | /api/v1/packages/{id}                | Delete package          |
| **Bookings**        |                                      |                         |
| GET                 | /api/v1/bookings                     | Get all bookings        |
| GET                 | /api/v1/bookings/{id}                | Get booking by ID       |
| POST                | /api/v1/bookings                     | Create booking          |
| PUT                 | /api/v1/bookings/{id}                | Update booking          |
| DELETE              | /api/v1/bookings/{id}                | Delete booking          |
| **Experiences**     |                                      |                         |
| GET                 | /api/v1/experiences                  | Get all experiences     |
| GET                 | /api/v1/experiences/{id}             | Get experience by ID    |
| POST                | /api/v1/experiences                  | Create experience       |
| PUT                 | /api/v1/experiences/{id}             | Update experience       |
| DELETE              | /api/v1/experiences/{id}             | Delete experience       |
| **Reviews**         |                                      |                         |
| GET                 | /api/v1/reviews                      | Get all reviews         |
| GET                 | /api/v1/reviews/{id}                 | Get review by ID        |
| GET                 | /api/v1/reviews/guides/{guideId}     | Get reviews for guide   |
| GET                 | /api/v1/reviews/tourists/{touristId} | Get reviews by tourist  |
| POST                | /api/v1/reviews                      | Create review           |
| PUT                 | /api/v1/reviews/{id}                 | Update review           |
| DELETE              | /api/v1/reviews/{id}                 | Delete review           |
| **Buddy AI**        |                                      |                         |
| GET                 | /api/v1/buddy-ai/recommend           | Get AI recommendations  |
| **Support Tickets** |                                      |                         |
| GET                 | /api/v1/tickets                      | Get user's tickets      |
| GET                 | /api/v1/tickets/{id}                 | Get ticket by ID        |
| GET                 | /api/v1/tickets/all                  | Get all tickets (admin) |
| POST                | /api/v1/tickets                      | Create ticket           |
| PUT                 | /api/v1/tickets/{id}                 | Update ticket           |

## Project Structure

```
src/main/java/com/mytourbuddy/backend/
├── config/         # Configuration classes (CORS, Security)
├── controller/     # REST API controllers
├── dto/            # Data Transfer Objects (request/response)
├── exception/      # Global exception handling
├── listener/       # Event listeners (e.g., user events)
├── mapper/         # Object mappers
├── model/          # Entity models (User, Booking, etc.)
├── repository/     # Data repositories
├── security/       # Authentication and authorization
├── service/        # Business logic services
└── util/           # Utility classes
```

## Contributors

| Name      | GitHub                                                          | Contributions                               |
| --------- | --------------------------------------------------------------- | ------------------------------------------- |
| Hiruni    | [hiruranasinghe](https://github.com/hiruranasinghe)             | User endpoint                               |
| Thilina   | [thilinaofficial](https://github.com/thilinaofficial)           | Package endpoint                            |
| Nimeshika | [nimeshikadew](https://github.com/nimeshikadew)                 | Experiences endpoint                        |
| Dilmi     | [Dil13jay](https://github.com/Dil13jay)                         | Reviews endpoint                            |
| Navindu   | [NavinduRanasinghe691](https://github.com/NavinduRanasinghe691) | Authentication                              |
| Sasmitha  | [sasmeee](https://github.com/sasmeee)                           | Authentication, bookings, tickets, buddy AI |

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact

- **Sasmitha** - [dev.sasmitha@gmail.com](mailto:dev.sasmitha@gmail.com)
- **Project Repository**: [GitHub](https://github.com/MyTourBuddy/mytourbuddy-backend)
