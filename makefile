run:
	@mvn spring-boot:run

test:
	@mvn test -X
	

build:
	@mvn clean package -DskipTests 

