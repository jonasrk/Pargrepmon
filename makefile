all: 
	javac Pargrepmon.java && jar cfm pargrepmon.jar manifest.txt *.class
