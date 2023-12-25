build:
	javac CaesarEnigma.java
	javac SocketServer.java
	javac SocketClient.java
	
server: build
	java SocketServer valid_config.xml

client: build
	java SocketClient valid_config.xml

tests: build
	java SocketServer invalid_plugboard_collision.xml
	java SocketServer invalid_plugboard_format.xml
	java SocketServer invalid_encryption_key.xml
	java SocketServer invalid_increment_factor.xml