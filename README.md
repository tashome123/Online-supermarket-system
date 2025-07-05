# Online Supermarket System

A complete online supermarket system built with Spring Boot, featuring user registration, authentication, product management, shopping cart, and order processing.

## Features

### User Management
- User registration and login
- Role-based access control (Customer/Admin)
- Secure password encryption
- User profile management

### Product Management
- Product catalog with categories
- Product search and filtering
- Stock management
- Product images support

### Shopping Features
- Shopping cart functionality
- Add/remove items from cart
- Quantity management
- Cart total calculation

### Order Management
- Order creation and processing
- Order status tracking
- Order history
- Payment integration

### Admin Features
- Product management (CRUD operations)
- User management
- Order management
- Inventory tracking

## Technology Stack

- **Backend**: Spring Boot 3.5.0
- **Database**: H2 Database (in-memory/file-based)
- **Security**: Spring Security 6
- **Frontend**: Thymeleaf templates with Bootstrap 5
- **Build Tool**: Maven
- **Java Version**: 17

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

## Installation and Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd Online-supermarket-system
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the application**
   - Main application: http://localhost:8082
   - H2 Database Console: http://localhost:8082/h2-console
     - JDBC URL: `jdbc:h2:file:./data/demo`
     - Username: `admin`
     - Password: `password`

## Default Users

The application creates a default admin user on startup:

- **Admin User**
  - Email: `admin@supermarket.com`
  - Password: `admin123`
  - Role: `ROLE_ADMIN`

## Database Schema

### Users Table
- `id` - Primary key
- `email` - Unique email address
- `password` - Encrypted password
- `name` - Full name
- `role` - User role (ROLE_CUSTOMER, ROLE_ADMIN)

### Products Table
- `id` - Primary key
- `name` - Product name
- `description` - Product description
- `price` - Product price
- `stock_quantity` - Available stock
- `category` - Product category
- `image_url` - Product image URL
- `active` - Product availability status

### Cart Items Table
- `id` - Primary key
- `user_id` - Foreign key to users
- `product_id` - Foreign key to products
- `quantity` - Item quantity
- `unit_price` - Price per unit

### Orders Table
- `id` - Primary key
- `user_id` - Foreign key to users
- `order_date` - Order creation date
- `total_amount` - Order total
- `status` - Order status
- `shipping_address` - Delivery address
- `phone_number` - Contact number

### Order Items Table
- `id` - Primary key
- `order_id` - Foreign key to orders
- `product_id` - Foreign key to products
- `quantity` - Item quantity
- `unit_price` - Price per unit
- `total_price` - Total price for this item

### Payments Table
- `id` - Primary key
- `order_id` - Foreign key to orders
- `payment_method` - Payment method used
- `amount` - Payment amount
- `payment_date` - Payment date
- `status` - Payment status
- `transaction_id` - Payment transaction ID

## API Endpoints

### Authentication
- `GET /auth/login` - Login page
- `POST /auth/login` - Process login
- `GET /auth/register` - Registration page
- `POST /auth/register` - Process registration
- `GET /auth/logout` - Logout

### Products
- `GET /products` - List all products
- `GET /products/category/{category}` - Products by category
- `GET /products/search` - Search products
- `GET /products/{id}` - Product details

### Admin (requires ROLE_ADMIN)
- `GET /products/admin` - Admin product list
- `GET /products/admin/new` - Create product form
- `POST /products/admin` - Create product
- `GET /products/admin/{id}/edit` - Edit product form
- `POST /products/admin/{id}` - Update product
- `POST /products/admin/{id}/delete` - Delete product

## Sample Data

The application automatically creates sample data on first run:

- **Products**: 28 sample products across 7 categories
  - Fruits (4 products)
  - Vegetables (4 products)
  - Dairy (4 products)
  - Bakery (4 products)
  - Meat (4 products)
  - Beverages (4 products)
  - Snacks (4 products)

## Security Features

- Password encryption using BCrypt
- Role-based access control
- CSRF protection
- Session management
- Secure form handling

## Customization

### Adding New Categories
1. Add products with the new category name
2. The category will automatically appear in the navigation

### Modifying Product Fields
1. Update the `Product` entity
2. Update the corresponding templates
3. Update the `DataInitializer` if needed

### Changing Database
1. Update `application.properties` with new database configuration
2. Update dependencies in `pom.xml` if needed

## Troubleshooting

### Common Issues

1. **Port already in use**
   - Change the port in `application.properties`
   - Default port is 8082

2. **Database connection issues**
   - Check H2 console at http://localhost:8082/h2-console
   - Verify database file permissions

3. **Build errors**
   - Ensure Java 17 is installed
   - Run `mvn clean install`

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is licensed under the MIT License.

## Support

For support and questions, please contact:
- Email: info@onlinesupermarket.com
- Phone: +254 700 000 000 