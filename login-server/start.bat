@ECHO off
TITLE Aion Emu - Login Server

:: Garante que o BAT sempre trabalhe a partir da pasta onde ele está
CD /D "%~dp0"

:: Pasta onde este BAT está
SET "LOGIN_DIR=%~dp0"

:: Pasta anterior
FOR %%I IN ("%LOGIN_DIR%..") DO SET "ROOT_DIR=%%~fI"

:: Libs na pasta anterior
SET "LIBS_DIR=%ROOT_DIR%\libs"

:: Classe principal do LoginServer
SET "MAIN_CLASS=com.aionemu.loginserver.LoginServer"

:: Java
SET "JAVA_EXE=java"

:: Caso queira forçar seu Java instalado, descomente esta linha:
:: SET "JAVA_EXE=C:\Program Files\Java\jdk-25.0.2\bin\java.exe"

:: Classpath
:: .       = pasta atual do login-server
:: config  = caso suas configs estejam dentro da pasta login-server\config
:: ..\libs = libs da pasta anterior
SET "CLASSPATH=.;config;%LIBS_DIR%\*"

ECHO ==========================================
ECHO        Aion Emu - Login Server
ECHO ==========================================
ECHO.
ECHO Pasta LoginServer:
ECHO %LOGIN_DIR%
ECHO.
ECHO Pasta raiz:
ECHO %ROOT_DIR%
ECHO.
ECHO Pasta libs:
ECHO %LIBS_DIR%
ECHO.
ECHO Classe:
ECHO %MAIN_CLASS%
ECHO.

IF NOT EXIST "%LIBS_DIR%" (
    ECHO ERRO: Pasta libs nao encontrada:
    ECHO %LIBS_DIR%
    ECHO.
    PAUSE
    EXIT /B 1
)

:START
CLS
TITLE Aion Emu - Login Server

ECHO ==========================================
ECHO        Aion Emu - Login Server
ECHO ==========================================
ECHO.
ECHO Iniciando LoginServer...
ECHO.
ECHO Usando libs:
ECHO %LIBS_DIR%
ECHO.

"%JAVA_EXE%" ^
-Xms48m ^
-Xmx48m ^
-XX:+UseNUMA ^
-DconsoleEncoding=CP850 ^
-cp "%CLASSPATH%" ^
%MAIN_CLASS%

IF %ERRORLEVEL% EQU 0 GOTO END
IF %ERRORLEVEL% EQU 2 GOTO START

ECHO.
ECHO Login server terminou com erro!
ECHO Codigo de erro: %ERRORLEVEL%
ECHO.
PAUSE
EXIT /B %ERRORLEVEL%

:END
ECHO.
ECHO Login server foi desligado normalmente.
ECHO.
PAUSE
EXIT /B 0