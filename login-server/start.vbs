Option Explicit

Dim fso, shell, serverDir, rootDir, libsDir, command, exitCode, dashboardMode
Set fso = CreateObject("Scripting.FileSystemObject")
Set shell = CreateObject("WScript.Shell")
dashboardMode = False

If WScript.Arguments.Count > 0 Then
	If LCase(WScript.Arguments(0)) = "--dashboard" Then
		dashboardMode = True
	End If
End If

serverDir = fso.GetParentFolderName(WScript.ScriptFullName)
rootDir = fso.GetParentFolderName(serverDir)
libsDir = fso.BuildPath(rootDir, "libs")

If Not fso.FolderExists(libsDir) Then
	If dashboardMode Then
		WScript.StdErr.WriteLine "ERRO: Pasta libs nao encontrada: " & libsDir
	Else
		MsgBox "ERRO: Pasta libs nao encontrada:" & vbCrLf & libsDir, vbCritical, "Aion Emu - Login Server"
	End If
	WScript.Quit 1
End If

Do
	command = "cmd /c cd /d """ & serverDir & """ && title Aion Emu - Login Server && java -Xms48m -Xmx48m -XX:+UseNUMA -DconsoleEncoding=CP850 -cp "".;config;" & libsDir & "\*"" com.aionemu.loginserver.LoginServer 2>&1"
	If dashboardMode Then
		exitCode = RunForDashboard(shell, command)
	Else
		exitCode = shell.Run(command, 1, True)
	End If
Loop While exitCode = 2

If exitCode <> 0 Then
	If dashboardMode Then
		WScript.StdErr.WriteLine "Login server terminou com erro. Codigo: " & exitCode
	Else
		MsgBox "Login server terminou com erro. Codigo: " & exitCode, vbExclamation, "Aion Emu - Login Server"
	End If
End If

WScript.Quit exitCode

Function RunForDashboard(shell, command)
	Dim exec, line
	Set exec = shell.Exec(command)
	Do While exec.Status = 0 Or Not exec.StdOut.AtEndOfStream
		Do While Not exec.StdOut.AtEndOfStream
			line = exec.StdOut.ReadLine
			WScript.StdOut.WriteLine line
		Loop
		WScript.Sleep 100
	Loop
	RunForDashboard = exec.ExitCode
End Function
