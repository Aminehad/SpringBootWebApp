# DonationManager Spring Boot Project

This project is a Spring Boot application for managing donations. The `Makefile` and `run.sh` script provide convenient ways to manage the project's build and run tasks.

## Prerequisites

Before you begin, ensure that you have the following installed:

- **Java 17+** (required for building and running the Spring Boot application)
- **Maven** (for dependency management and building the project)
- **Make** (for running the tasks defined in the `Makefile`)

You can install Maven and Make by following their respective documentation if not already installed:

- [Maven Installation Guide](https://maven.apache.org/install.html)
- [Make Installation Guide](https://www.gnu.org/software/make/)


### Explanation of Instructions:

- **Running the Application with the Script**: This section now includes the steps to `cd` into the `/DonationManager` directory and run the `./run.sh` script from there, ensuring the user is in the correct directory before executing the script.

```bash
cd DonationManager
./run.sh
```

## Usage

### Clone the Repository

If you haven't already, clone the repository to your local machine:

```bash
git clone <repository-url>
cd <repository-name>

#To install required dependencies (e.g., Java libraries), run:
make setup

# To clean the project and delete the target directory, use:
make clean

# To build the project without running tests, use:
make build

# run app
make run

# To run the project's tests with debug output, use:
make test
```