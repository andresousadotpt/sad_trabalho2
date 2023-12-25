build:
	javac CaesarEnigma.java
	javac SocketServer.java
	javac SocketClient.java
	
server: build
	java SocketServer configs/valid_config.xml

client: build
	java SocketClient configs/valid_config.xml

tests: build
	java SocketServer configs/invalid_plugboard_collision.xml
	java SocketServer configs/invalid_plugboard_format.xml
	java SocketServer configs/invalid_encryption_key.xml
	java SocketServer configs/invalid_increment_factor.xml