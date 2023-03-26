JAR_FILE=oh-mybox-api-0.0.1-SNAPSHOT.jar
DB=mongodb-community
API_PORT=8080

.PHONY: start
start:
	@make start_db
	@make start_api

.PHONY: stop
stop:
	@make stop_api
	@make stop_db

.PHONY: start_api
start_api:
	./gradlew build
	java -jar ./build/libs/$(JAR_FILE)

.PHONY: stop_api
stop_api:
	lsof -t -i:$(API_PORT) | xargs kill -9 2> /dev/null
	rm -rf build

.PHONY: restart_api
restart_api:
	@make stop_api
	@make start_api

.PHONY: start_db
start_db:
	brew services start $(DB)

.PHONY: stop_db
stop_db:
	brew services stop $(DB)

.PHONY: restart_db
restart_db:
	brew services restart $(DB)
