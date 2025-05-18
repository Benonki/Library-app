# Library App

A library management system developed in Java with Oracle Database integration, enabling efficient handling of books, users, loans, and returns with secure and reliable data storage.

## Setup (Part 1 - if you have already installed oracle skip to Part 2)
1. Download database for windows: 
    ```bash
    https://download.oracle.com/otn-pub/otn_software/db-express/OracleXE213_Win64.zip
    ```
2. Extract files to your folder
3. Check you current IP and save it or change it to a static one. (It's important cause after the installation if you change your ip adress the connection wouldn't be able to be established).
4. Open the folder where you extracted the files and start the `setup.exe` file.
5. Install the database.
6. After installation you should be able to connect to your database via sqldeveloper for example.
7. Open sql developer as username set `sys` and set your role to `SYSDBA` and enter your password that you typed while installing database.

## Setup (Part 2)
1. Clone the repository:
   ```bash
   git clone https://github.com/Benonki/Library-app.git
   ```
2. Create new user in sql developer:
   ```sql
   CREATE USER C##Admin IDENTIFIED BY qwer;
	GRANT CREATE SESSION TO C##Admin;
	GRANT ALL PRIVILEGES TO C##Admin;
   ```
   
3. In sqldeveloper create a new database connection, in name type `DB` and in username and password those: `C##Admin` and `qwer`.
4. Open file in folder `DB` through sql developer: `LibraryDB.sql` and run it.
5. In `data` folder click `data_import.bat` and insert these parameters (if you have different configuration, change these):
   - Login: _C##Admin_
   - Haslo: _qwer_
   - Host: _localhost_
   - PORT: _1521_
   - SID: _XE_
6. After altering something in sqldeveloper finish everything with `COMMIT;` so the java app would get the changed info in database.

## Running the App

1. Open the `Server` class located in `src/main/java/Server/`.
2. Right-click on the `Server` class file.
3. Select `Run 'Server.main()'`.
4. Open the `LoginApplication` class located in `src/main/java/Views/Login/`.
5. Right-click on the `LoginApplication` class file. 
6. Select `Run 'LoginApplication.main()'`.
7. Example logins for every role:
   - **Pyzel**  : **c4slHKll*f**      - _Employee_
   - **Falba** :   **l^68HMGabu**    - _Reader_
   - **Maciejczuk** :   **q#8sAsJh8V**    - _Manager_
   - **Gamon** :   **M@t2ND1nBd**    - _Coordinator_