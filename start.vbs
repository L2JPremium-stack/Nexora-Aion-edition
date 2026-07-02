Option Explicit

Dim fso, shell, rootDir, javaCmd, command
Set fso = CreateObject("Scripting.FileSystemObject")
Set shell = CreateObject("WScript.Shell")

rootDir = fso.GetParentFolderName(WScript.ScriptFullName)

If Not fso.FolderExists(fso.BuildPath(rootDir, "libs")) Then
	MsgBox "Pasta libs nao encontrada: " & fso.BuildPath(rootDir, "libs"), vbCritical, "Nexora Dashboard"
	WScript.Quit 1
End If

If Not fso.FileExists(fso.BuildPath(rootDir, "libs\dashboard-20.0.jar")) And Not fso.FileExists(fso.BuildPath(rootDir, "libs\dashboard-4.8-SNAPSHOT.jar")) And Not fso.FileExists(fso.BuildPath(rootDir, "bin\com\aionemu\dashboard\Dashboard.class")) Then
	MsgBox "Dashboard ainda nao compilado. Gere o jar pelo build.xml ou deixe o Eclipse compilar em bin/.", vbExclamation, "Nexora Dashboard"
	WScript.Quit 1
End If

javaCmd = "javaw"
If shell.Run("cmd /c where javaw >nul 2>nul", 0, True) <> 0 Then
	javaCmd = "java"
End If

command = "cmd /c cd /d """ & rootDir & """ && " & javaCmd & " -cp ""libs\dashboard-20.0.jar;bin;libs\*"" com.aionemu.dashboard.Dashboard"
shell.Run command, 0, False
