# Default target when no target is provided
.PHONY: help
help:
	@echo "Makefile for Spring Boot project"
	@echo ""
	@echo "Usage:"
	@echo "  make clean        - Clean the project (delete target directory)"
	@echo "  make run          - Run the Spring Boot application"
	@echo "  make test         - Run tests with debug output"
	@echo "  make build        - Build the project (skip tests)"
	@echo "  make setup        - Install required dependencies"

setup:
	@mvn clean install

clean:
	@mvn clean

run:
	@mvn spring-boot:run

test:
	@mvn test -X
	
build:
	@mvn clean package -DskipTests 

