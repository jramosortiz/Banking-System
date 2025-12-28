Easiest way to make it work:

Install Java Extension Pack if you haven’t already.

Open your project.                                                                                               

Right-click the project → Configure Java Runtime → Add lib/sqlite-jdbc-3.49.1.0.jar (Or newer on this folder) to 
Referenced Libraries.      |
                                                                                                                  |
Rebuild the project (Ctrl+Shift+B) and run.                                                                       |
__________________________________________________________________________________________________________________|

--Other ways to make it work:

Setting up SQLite for Banking System Project (Java)
1. Download SQLite JDBC

Go to the official SQLite JDBC page: https://github.com/xerial/sqlite-jdbc

Download the latest .jar version (e.g., sqlite-jdbc-3.57.2.1.jar).

Create a folder called lib in your project directory and place the jar there:

css
Banking-System-main/
├─ src/
├─ bin/
└─ lib/
   └─ sqlite-jdbc-3.57.2.1.jar
2. Install Java Extension Pack in VS Code

Open VS Code.

Go to Extensions → search for Java Extension Pack → Install.

3. Add the JDBC Jar to Project Classpath
Right-click your project folder → Configure Java Runtime → Referenced Libraries.

Click Add JARs → Select lib/sqlite-jdbc-3.57.2.1.jar → Add it.

4. Compile the Project
Open a terminal in your project folder:

powershell
Copy code
cd C:\Users\Jonathan\Downloads\Banking-System-main
Compile all .java files into the bin folder:

powershell
Copy code
javac -d bin -cp "lib/sqlite-jdbc-3.57.2.1.jar" src\*.java
5. Run the Project













