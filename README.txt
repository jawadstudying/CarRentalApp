Car Rental Application

This repository contains a JavaFX desktop application, a Laravel web application, and a MySQL database dump. All components share the same database and are organized into a single repository for easier setup and maintenance.

Repository structure:

CarRentalApp/
  javafx-app/        # JavaFX application (Maven/Gradle project)
  laravel-app/       # Laravel application
  db/
    car_rental_full.sql   # Complete MySQL dump (schema, data, procedures)
    README.md             # Database setup instructions

Requirements:

General:
- Git
- MySQL Server 8.x or MariaDB

JavaFX:
- JDK 17+ (or project-specific version)
- Maven or Gradle
- IntelliJ IDEA or any Java IDE

Laravel:
- PHP 8.x
- Composer
- A local server such as Laravel’s built-in server, Laragon, or XAMPP

Database setup:

The file db/car_rental_full.sql contains the full database schema, stored procedures, and optional seed data.


JavaFX setup:

1. Open IntelliJ.
2. Choose File → Open and select CarRentalApp/javafx-app.
3. Let IntelliJ import Maven/Gradle dependencies.
4. Configure database access in src/main/resources/application.properties:

db.url=jdbc:mysql://127.0.0.1:3306/car_rental
db.user=root
db.password=your_password

5. Run the JavaFX main class.

Laravel setup:

1. Open a terminal in CarRentalApp/laravel-app.
2. Install dependencies:

composer install

3. Create the environment file:

cp .env.example .env

4. Configure database settings in .env:

DB_CONNECTION=mysql
DB_HOST=127.0.0.1
DB_PORT=3306
DB_DATABASE=car_rental
DB_USERNAME=root
DB_PASSWORD=your_password


5. Start the server:

php artisan serve

Running the system:

Both the JavaFX desktop application and the Laravel web application connect to the same MySQL database. All tables, procedures, and data are shared, allowing the two applications to interact with the same backend.

Developer notes:

- Sensitive configuration files such as Laravel .env and JavaFX application.properties must be configured locally and are not part of the repository.
- Laravel vendor/ and node_modules/ folders are excluded using .gitignore.
- JavaFX build output such as target/ is also excluded.
- The repository uses a monorepo structure for easier navigation and consistent version control across all components.

This project demonstrates a combined desktop and web architecture using a shared MySQL backend.
